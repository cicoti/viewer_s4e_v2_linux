package com.s4etech.config.manager;

import com.s4e.CryptoUtils;
import com.s4etech.dto.AutorizacaoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class AutorizacaoConfigurationManager {

    private static final Logger logger = LoggerFactory.getLogger(AutorizacaoConfigurationManager.class);

    private static final Path pathautorizacaoconf = Paths.get(
            System.getProperty("user.dir"),
            "configuracao",
            "autorizacao.cfg"
    );

    private static AutorizacaoDTO autorizacaoDTO = null;

    public static AutorizacaoDTO get() {
        logger.info("Carregando configuração para a autorização.");
        try {
            readConfiguracao();
        } catch (IOException e) {
            logger.error("Erro ao ler o arquivo de configuração da Autorizacao, arquivo não encontrado.");
            return null;
        }
        return autorizacaoDTO;
    }

    public static void writeConfiguracao(String conteudoPlano) {
        try {
            String criptografado = CryptoUtils.encrypt(conteudoPlano);

            try (BufferedWriter writer = Files.newBufferedWriter(
                    pathautorizacaoconf,
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
        List<String> listAutorizacao = new ArrayList<>();

        String contentArquivo = new String(
                Files.readAllBytes(pathautorizacaoconf),
                StandardCharsets.UTF_8
        ).trim();

        String conteudoDescriptografado;

        try {
            // Tenta descriptografar
            conteudoDescriptografado = CryptoUtils.decrypt(contentArquivo);

            // Verificação simples: se o resultado for nulo ou ilegível, pode não estar criptografado
            if (conteudoDescriptografado == null || conteudoDescriptografado.trim().isEmpty()) {
                throw new IllegalArgumentException("Arquivo não parece estar criptografado.");
            }

        } catch (Exception ex) {
            logger.warn("Arquivo de configuração não criptografado detectado. Tentando ler em texto puro...");

            // Se falhar, assume que é texto plano e usa diretamente
            conteudoDescriptografado = contentArquivo;

            // Regrava o arquivo de forma criptografada
            writeConfiguracao(contentArquivo);
            logger.info("Arquivo legado regravado agora de forma criptografada.");
        }

        // Divide e processa o conteúdo
        String[] linhas = conteudoDescriptografado.split("\n");
        for (String linha : linhas) {
            listAutorizacao.add(linha);
        }

        processConfiguracao(listAutorizacao);
    }


    private static void processConfiguracao(List<String> listWebBrowser) {
        autorizacaoDTO = null;

        for (String linha : listWebBrowser) {
            String[] campos = linha.trim().split("\\|", -1);
            if (campos.length <= 1) {
                break;
            }
            if (!"autorizacao".equals(campos[0])) {
                AutorizacaoDTO dto = new AutorizacaoDTO();
                dto.setUsuario(campos[0]);
                dto.setAutorizacao(campos[1]);
                dto.setDataAlteracao(campos[2]);
                autorizacaoDTO = dto;
            }
        }
    }
}
