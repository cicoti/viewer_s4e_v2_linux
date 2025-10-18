package com.s4etech.config.manager;

import com.s4e.CryptoUtils;
import com.s4etech.dto.PerformanceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class PerformanceConfigurationManager {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceConfigurationManager.class);

    private static final Path pathperformanceconf = Paths.get(
            System.getProperty("user.dir"),
            "configuracao",
            "performance.cfg"
    );

    private static PerformanceDTO performanceDTO = null;

    public static PerformanceDTO get() {
        logger.info("Carregando configuração da performance.");
        try {
            readConfiguracao();
        } catch (IOException e) {
            logger.error("Erro ao ler o arquivo de configuração, arquivo não encontrado.", e);
            return null;
        }
        return performanceDTO;
    }

    public static void writeConfiguracao(String conteudoPlano) {
        try {
            String criptografado = CryptoUtils.encrypt(conteudoPlano);

            try (BufferedWriter writer = Files.newBufferedWriter(
                    pathperformanceconf,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.CREATE
            )) {
                writer.write(criptografado);
                writer.newLine();
            }

        } catch (Exception e) {
            logger.error("Erro ao gravar o arquivo de configuração criptografado.", e);
        }
    }

    private static void readConfiguracao() throws IOException {
        List<String> listPerformance = new ArrayList<>();

        String contentArquivo = new String(
                Files.readAllBytes(pathperformanceconf),
                StandardCharsets.UTF_8
        ).trim();

        String conteudoDescriptografado;

        try {
            conteudoDescriptografado = CryptoUtils.decrypt(contentArquivo);

            if (conteudoDescriptografado == null || conteudoDescriptografado.trim().isEmpty()) {
                throw new IllegalArgumentException("Arquivo não parece estar criptografado.");
            }

        } catch (Exception ex) {
            logger.warn("Arquivo de configuração não criptografado detectado. Tentando ler em texto puro...");

            conteudoDescriptografado = contentArquivo;

            try {
                writeConfiguracao(contentArquivo);
                logger.info("Arquivo legado regravado agora de forma criptografada.");
            } catch (Exception writeEx) {
                logger.error("Erro ao tentar regravar arquivo legado como criptografado.", writeEx);
            }
        }

        String[] linhas = conteudoDescriptografado.split("\n");
        for (String linha : linhas) {
            listPerformance.add(linha);
        }

        try {
            processConfiguracao(listPerformance);
        } catch (Exception e) {
            logger.error("Arquivo de configuração incompatível. Farei backup e deletarei o arquivo.", e);

            Path backupPath = Paths.get(pathperformanceconf.toString() + ".bak");
            try {
                Files.copy(pathperformanceconf, backupPath);
            } catch (IOException copyEx) {
                logger.warn("Falhou ao criar backup do arquivo de configuração.", copyEx);
            }

            try {
                Files.delete(pathperformanceconf);
                logger.info("Arquivo de configuração deletado: {}", pathperformanceconf);
            } catch (IOException delEx) {
                logger.warn("Falhou ao deletar o arquivo incompatível.", delEx);
            }

            if (e instanceof IOException) {
                throw (IOException) e;
            } else {
                throw new IOException("Configuração incompatível.", e);
            }
        }
    }

    private static void processConfiguracao(List<String> listPerformance) {
        performanceDTO = null;

        for (String linha : listPerformance) {
            String[] campos = linha.trim().split("\\|", -1);
            if (campos.length <= 1) {
                break;
            }
            if (!"configuracao".equals(campos[0])) {
                PerformanceDTO dto = new PerformanceDTO();
                dto.setConfiguracao(campos[0]);
                dto.setBufferSize(Integer.parseInt(campos[1]));
                dto.setLatency(Integer.parseInt(campos[2]));
                dto.setProtocolo(campos[3]);
                dto.setTimeout(Integer.parseInt(campos[4]));
                dto.setAceleracao(campos[5]);
                performanceDTO = dto;
            }
        }
    }
}
