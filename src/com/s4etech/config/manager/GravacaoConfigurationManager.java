package com.s4etech.config.manager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s4etech.dto.GravacaoDTO;

public class GravacaoConfigurationManager {

    private static final Logger logger = LoggerFactory.getLogger(GravacaoConfigurationManager.class);

    // Caminho padrão Linux (~/.config/s4etech/gravacao.cfg)
    private static final Path CONFIG_DIR = Paths.get(System.getProperty("user.home"), ".config", "s4etech");
    private static final Path CONFIG_FILE = CONFIG_DIR.resolve("gravacao.cfg");

    private static GravacaoDTO gravacaoDTO = null;

    /**
     * Obtém as configurações de gravação.
     */
    public static GravacaoDTO get() {
        logger.info("Carregando configuração de gravação a partir de {}", CONFIG_FILE);

        try {
            ensureConfigDirectory();
            readConfiguracao();
        } catch (IOException e) {
            logger.error("Erro ao ler o arquivo de configuração: {}", CONFIG_FILE, e);
            return null;
        }

        return gravacaoDTO;
    }

    /**
     * Garante que o diretório ~/.config/s4etech exista e seja gravável.
     */
    private static void ensureConfigDirectory() throws IOException {
        if (!Files.exists(CONFIG_DIR)) {
            try {
                Files.createDirectories(CONFIG_DIR);
                logger.info("Diretório de configuração criado: {}", CONFIG_DIR);
            } catch (IOException e) {
                logger.error("Falha ao criar o diretório de configuração: {}", CONFIG_DIR, e);
                throw e;
            }
        } else if (!Files.isWritable(CONFIG_DIR)) {
            throw new IOException("Diretório de configuração sem permissão de escrita: " + CONFIG_DIR);
        }
    }

    /**
     * Escreve a configuração diretamente em texto puro no arquivo.
     *
     * @param conteudoConfig Conteúdo a ser gravado no arquivo de configuração
     */
    public static void writeConfiguracao(String conteudoConfig) {
        try {
            ensureConfigDirectory();

            try (BufferedWriter writer = Files.newBufferedWriter(CONFIG_FILE,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.CREATE)) {
                writer.write(conteudoConfig);
                writer.newLine();
            }

            logger.info("Arquivo de configuração gravado com sucesso em {}", CONFIG_FILE);

        } catch (IOException e) {
            logger.error("Erro ao gravar o arquivo de configuração em {}", CONFIG_FILE, e);
        }
    }

    /**
     * Lê o arquivo de configuração e processa seu conteúdo em texto puro.
     */
    private static void readConfiguracao() throws IOException {
        if (!Files.exists(CONFIG_FILE)) {
            logger.warn("Arquivo de configuração não encontrado: {}", CONFIG_FILE);
            return;
        }

        List<String> linhas = Files.readAllLines(CONFIG_FILE, StandardCharsets.UTF_8);
        if (linhas.isEmpty()) {
            logger.warn("Arquivo de configuração vazio: {}", CONFIG_FILE);
            return;
        }

        try {
            processConfiguracao(linhas);
        } catch (Exception e) {
            logger.error("Arquivo de configuração incompatível. Criando backup e recriando.", e);
            backupAndDelete();
            throw new IOException("Arquivo de configuração inválido.", e);
        }
    }

    /**
     * Cria backup do arquivo de configuração e o deleta se for inválido.
     */
    private static void backupAndDelete() {
        Path backupPath = CONFIG_FILE.resolveSibling("gravacao.cfg.bak");

        try {
            Files.copy(CONFIG_FILE, backupPath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Backup criado em: {}", backupPath);
        } catch (IOException e) {
            logger.warn("Falhou ao criar backup do arquivo de configuração.", e);
        }

        try {
            Files.deleteIfExists(CONFIG_FILE);
            logger.info("Arquivo de configuração inválido foi removido: {}", CONFIG_FILE);
        } catch (IOException e) {
            logger.warn("Falhou ao remover o arquivo de configuração inválido.", e);
        }
    }

    /**
     * Processa as linhas obtidas do arquivo e converte em um objeto GravacaoDTO.
     * Espera-se que cada linha possua a formatação:
     * caminho|ativar|qualidade
     */
    private static void processConfiguracao(List<String> linhas) {
        gravacaoDTO = null;

        for (String linha : linhas) {
            String[] campos = linha.trim().split("\\|", -1);

            if (campos.length < 3) {
                logger.warn("Linha de configuração inválida (esperado: caminho|ativar|qualidade): {}", linha);
                continue;
            }

            // Ignora cabeçalho, se existir
            if ("caminho".equalsIgnoreCase(campos[0])) {
                continue;
            }

            GravacaoDTO dto = new GravacaoDTO();
            dto.setCaminho(campos[0].replace("\\", "/")); // garante compatibilidade Linux
            dto.setAtivar(Boolean.parseBoolean(campos[1]));
            dto.setQualidade(campos[2]);
            gravacaoDTO = dto;

            logger.info("Configuração carregada: caminho={}, ativar={}, qualidade={}",
                    dto.getCaminho(), dto.isAtivar(), dto.getQualidade());
        }
    }
}
