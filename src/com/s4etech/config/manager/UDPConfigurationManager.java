package com.s4etech.config.manager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s4etech.dto.UDPDTO;

public class UDPConfigurationManager {

    private static final Logger logger = LoggerFactory.getLogger(UDPConfigurationManager.class);

    // Caminho do arquivo de configuração
    private static final Path pathpcanconf = Paths.get(
            System.getProperty("user.dir"),
            "configuracao",
            "udp.cfg"
    );

    // DTO que armazenará a configuração
    private static UDPDTO udpDTO = null;

    /**
     * Obtém a configuração do pcan.
     */
    public static UDPDTO get() {
        logger.info("Carregando configuracao da udp.");
        try {
            readConfiguracao();
        } catch (IOException e) {
            logger.error("Erro ao ler o arquivo de configuração, arquivo não encontrado.", e);
            return null;
        }
        return udpDTO;
    }

    /**
     * Escreve a configuração diretamente em texto puro.
     *
     * @param arquivoconfiguracao Conteúdo que deve ser gravado no arquivo de configuração
     */
    public static void writeConfiguracao(String arquivoconfiguracao) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                pathpcanconf,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.CREATE
        )) {
            writer.write(arquivoconfiguracao);
            writer.newLine();
        } catch (IOException e) {
            logger.error("Erro ao gravar o arquivo de configuração.", e);
        }
    }

    /**
     * Lê o arquivo de configuração em texto puro e processa seu conteúdo.
     */
    private static void readConfiguracao() throws IOException {
        List<String> listPCan = new ArrayList<>();

        // Lê o conteúdo em texto puro
        String content = new String(
                Files.readAllBytes(pathpcanconf),
                StandardCharsets.UTF_8
        ).trim();

        // Separa as linhas e adiciona na lista
        String[] linhas = content.split("\n");
        for (String linha : linhas) {
            listPCan.add(linha);
        }

        try {
            // Tenta processar as linhas no formato esperado
            processConfiguracao(listPCan);
        } catch (Exception e) {
            // SIGNIFICA QUE O ARQUIVO É INVÁLIDO PARA A NOVA VERSÃO
            logger.error("Arquivo de configuração incompatível. Farei backup e deletarei o arquivo.", e);

            // 1) Cria backup (adapte o nome se quiser)
            Path backupPath = Paths.get(pathpcanconf.toString() + ".bak");
            try {
                Files.copy(pathpcanconf, backupPath);
            } catch (IOException copyEx) {
                logger.warn("Falhou ao criar backup do arquivo de configuração.", copyEx);
                // Decide se continua ou não sem backup
            }

            // 2) Apaga o arquivo que não serve mais
            try {
                Files.delete(pathpcanconf);
                logger.info("Arquivo de configuração deletado: {}", pathpcanconf);
            } catch (IOException delEx) {
                logger.warn("Falhou ao deletar o arquivo incompatível.", delEx);
            }

            // Lança novamente a exceção para avisar quem chamou que não foi possível carregar
            if (e instanceof IOException) {
                throw (IOException) e;
            } else {
                throw new IOException("Configuração incompatível.", e);
            }
        }
    }

    private static void processConfiguracao(List<String> listPCan) {
    	udpDTO = null;

        for (String linha : listPCan) {
            String[] campos = linha.trim().split("\\|", -1);
            // Se não tiver campos suficientes, interrompe.
            if (campos.length <= 1) {
                break;
            }
            // Ignora a linha "cabeçalho" se houver
            if (!"ip_destino".equals(campos[0])) {
                UDPDTO dto = new UDPDTO();
                dto.setIpDestino(campos[0]);
                dto.setPortaDestino(campos[1]);
                dto.setIpLocal(campos[2]);
                dto.setPortaLocal(campos[3]);
                dto.setAtivar(Boolean.parseBoolean(campos[4]));
                udpDTO = dto;
            }
        }
    }
}
