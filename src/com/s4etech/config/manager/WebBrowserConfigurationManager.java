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

import com.s4etech.dto.WebBrowserDTO;

public class WebBrowserConfigurationManager {

    private static final Logger logger = LoggerFactory.getLogger(WebBrowserConfigurationManager.class);

    // Caminho para o arquivo de configuração
    private static final Path pathwebbrowserconf = Paths.get(
            System.getProperty("user.dir"),
            "configuracao",
            "webbrowser.cfg"
    );

    private static WebBrowserDTO webBrowserDTO = null;

    /**
     * Retorna a configuração do WebBrowser.
     */
    public static WebBrowserDTO get() {
        logger.info("Carregando configuração para o webbrowser.");
        try {
            readConfiguracao();
        } catch (IOException e) {
            logger.error("Erro ao ler o arquivo de configuração do WebBrowser, arquivo não encontrado.");
            return null;
        }
        return webBrowserDTO;
    }

    /**
     * Grava a configuração em texto puro no arquivo.
     *
     * @param arquivoconfiguracao Conteúdo que deve ser escrito no arquivo de configuração.
     */
    public static void writeConfiguracao(String arquivoconfiguracao) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                pathwebbrowserconf,
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
     * Lê o arquivo de configuração (webbrowser.cfg) em texto puro e processa as linhas.
     */
    private static void readConfiguracao() throws IOException {
        List<String> listWebBrowser = new ArrayList<>();

        // Lê todo o conteúdo do arquivo
        String content = new String(
                Files.readAllBytes(pathwebbrowserconf),
                StandardCharsets.UTF_8
        ).trim();

        // Separa o conteúdo em linhas e adiciona na lista
        String[] linhas = content.split("\n");
        for (String linha : linhas) {
            listWebBrowser.add(linha);
        }

        try {
            // Tenta processar as linhas no formato esperado
            processConfiguracao(listWebBrowser);
        } catch (Exception e) {
            // SIGNIFICA QUE O ARQUIVO É INVÁLIDO PARA A NOVA VERSÃO
            logger.error("Arquivo de configuração incompatível. Farei backup e deletarei o arquivo.", e);

            // 1) Cria backup (adapte o nome se quiser)
            Path backupPath = Paths.get(pathwebbrowserconf.toString() + ".bak");
            try {
                Files.copy(pathwebbrowserconf, backupPath);
            } catch (IOException copyEx) {
                logger.warn("Falhou ao criar backup do arquivo de configuração.", copyEx);
                // Decide se continua ou não sem backup
            }

            // 2) Apaga o arquivo que não serve mais
            try {
                Files.delete(pathwebbrowserconf);
                logger.info("Arquivo de configuração deletado: {}", pathwebbrowserconf);
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

    /**
     * Processa as linhas obtidas do arquivo e converte em um objeto WebBrowserDTO.
     * Espera-se que cada linha possua a formatação:
     * url1|url2|ativar
     */
    private static void processConfiguracao(List<String> listWebBrowser) {
        webBrowserDTO = null;

        for (String linha : listWebBrowser) {
            String[] campos = linha.trim().split("\\|", -1);
            // Se não tiver campos suficientes, interrompe.
            if (campos.length <= 1) {
                break;
            }
            // Ignora a linha "cabeçalho" se houver
            if (!"url".equals(campos[0])) {
                WebBrowserDTO dto = new WebBrowserDTO();
                dto.setUrl1(campos[0]);
                dto.setUrl2(campos[1]);
                dto.setAtivar(Boolean.parseBoolean(campos[2]));
                webBrowserDTO = dto;
            }
        }
    }
}
