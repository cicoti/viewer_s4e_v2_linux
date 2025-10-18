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

import com.s4etech.dto.GravacaoDTO;

public class GravacaoConfigurationManager {

    private static final Logger logger = LoggerFactory.getLogger(GravacaoConfigurationManager.class);

    // Caminho do arquivo de configuração
    private static final Path pathgravacaoconf = Paths.get(
            System.getProperty("user.dir"), "configuracao", "gravacao.cfg");

    private static GravacaoDTO gravacaoDTO = null;

    /**
     * Obtém as configurações de gravação.
     */
    public static GravacaoDTO get() {
        logger.info("Carregando configuração da gravação.");
        try {
            readConfiguracao();
        } catch (IOException e) {
            logger.error("Erro ao ler o arquivo de configuração, arquivo não encontrado.", e);
            return null;
        }
        return gravacaoDTO;
    }

    /**
     * Escreve a configuração diretamente em texto puro no arquivo.
     *
     * @param arquivoconfiguracao Conteúdo que deve ser gravado no arquivo de configuração
     */
    public static void writeConfiguracao(String arquivoconfiguracao) {
        try (BufferedWriter writer = Files.newBufferedWriter(pathgravacaoconf,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.CREATE)) {
            writer.write(arquivoconfiguracao);
            writer.newLine();
        } catch (IOException e) {
            logger.error("Erro ao gravar o arquivo de configuração.", e);
        }
    }

    /**
     * Lê o arquivo de configuração e processa seu conteúdo em texto puro.
     */
    private static void readConfiguracao() throws IOException {
        List<String> listGravacao = new ArrayList<>();

        // Lê o conteúdo em texto puro
        String content = new String(Files.readAllBytes(pathgravacaoconf), StandardCharsets.UTF_8).trim();

        // Separa as linhas e adiciona na lista
        String[] linhas = content.split("\n");
        for (String linha : linhas) {
            listGravacao.add(linha);
        }

        try {
            // Tenta processar as linhas no formato esperado
            processConfiguracao(listGravacao);
        } catch (Exception e) {
            // SIGNIFICA QUE O ARQUIVO É INVÁLIDO PARA A NOVA VERSÃO
            logger.error("Arquivo de configuração incompatível. Farei backup e deletarei o arquivo.", e);

            // 1) Cria backup (adapte o nome se quiser)
            Path backupPath = Paths.get(pathgravacaoconf.toString() + ".bak");
            try {
                Files.copy(pathgravacaoconf, backupPath);
            } catch (IOException copyEx) {
                logger.warn("Falhou ao criar backup do arquivo de configuração.", copyEx);
                // Decide se continua ou não sem backup
            }

            // 2) Apaga o arquivo que não serve mais
            try {
                Files.delete(pathgravacaoconf);
                logger.info("Arquivo de configuração deletado: {}", pathgravacaoconf);
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
     * Processa as linhas obtidas do arquivo e converte em um objeto GravacaoDTO.
     * Espera-se que cada linha possua a formatação:
     * caminho|ativar|qualidade
     */
    private static void processConfiguracao(List<String> listGravacao) {
        gravacaoDTO = null;

        for (String linha : listGravacao) {
            String[] campos = linha.trim().split("\\|", -1);
            // Se não tiver campos suficientes, interrompe.
            if (campos.length <= 1) {
                break;
            }
            // Ignora a linha "cabeçalho" se houver
            if (!"caminho".equals(campos[0])) {
                GravacaoDTO dto = new GravacaoDTO();
                dto.setCaminho(campos[0]);
                dto.setAtivar(Boolean.parseBoolean(campos[1]));
                dto.setQualidade(campos[2]);
                gravacaoDTO = dto;
            }
        }
    }
}
