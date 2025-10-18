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

import com.s4etech.dto.VNCDTO;

public class VNCConfigurationManager {

    private static final Logger logger = LoggerFactory.getLogger(VNCConfigurationManager.class);

    // Caminho para o arquivo de configuração
    private static final Path pathvncconf = Paths.get(
            System.getProperty("user.dir"),
            "configuracao",
            "vnc.cfg"
    );

    private static VNCDTO vncDTO = null;

    /**
     * Retorna a configuração do VNC.
     */
    public static VNCDTO get() {
        logger.info("Carregando configuração da VNC.");
        try {
            readConfiguracao();
        } catch (IOException e) {
            logger.error("Erro ao ler o arquivo de configuração do VNC, arquivo não encontrado.");
            return null;
        }

        return vncDTO;
    }

    /**
     * Escreve a configuração em texto puro no arquivo de configuração.
     *
     * @param arquivoconfiguracao Conteúdo que deve ser escrito no arquivo de configuração.
     */
    public static void writeConfiguracao(String arquivoconfiguracao) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                pathvncconf,
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
     * Lê o arquivo de configuração (vnc.cfg) em texto puro e processa as linhas.
     */
    private static void readConfiguracao() throws IOException {
        List<String> listVNC = new ArrayList<>();

        // Lê todo o conteúdo do arquivo
        String content = new String(
                Files.readAllBytes(pathvncconf),
                StandardCharsets.UTF_8
        ).trim();

        // Separa o conteúdo em linhas e adiciona na lista
        String[] linhas = content.split("\n");
        for (String linha : linhas) {
            listVNC.add(linha);
        }

        try {
            // Tenta processar as linhas no formato esperado
            processConfiguracao(listVNC);
        } catch (Exception e) {
            // SIGNIFICA QUE O ARQUIVO É INVÁLIDO PARA A NOVA VERSÃO
            logger.error("Arquivo de configuração incompatível. Farei backup e deletarei o arquivo.", e);

            // 1) Cria backup (adapte o nome se quiser)
            Path backupPath = Paths.get(pathvncconf.toString() + ".bak");
            try {
                Files.copy(pathvncconf, backupPath);
            } catch (IOException copyEx) {
                logger.warn("Falhou ao criar backup do arquivo de configuração.", copyEx);
                // Decide se continua ou não sem backup
            }

            // 2) Apaga o arquivo que não serve mais
            try {
                Files.delete(pathvncconf);
                logger.info("Arquivo de configuração deletado: {}", pathvncconf);
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
     * Processa as linhas e popula o objeto VNCDTO.
     * Espera-se que cada linha possua a formatação:
     * servidor|porta|senha|cor|cursorLocal|copyrect|rre|hextile|zlib|ativar
     */
    private static void processConfiguracao(List<String> listVNC) {
        vncDTO = null;

        for (String linha : listVNC) {
            String[] campos = linha.trim().split("\\|", -1);
            // Se não tiver campos suficientes, interrompe.
            if (campos.length <= 1) {
                break;
            }
            // Ignora a linha "cabeçalho" se houver
            if (!"servidor".equals(campos[0])) {
                VNCDTO dto = new VNCDTO();
                dto.setServidor(campos[0]);
                dto.setPorta(campos[1]);
                dto.setSenha(campos[2]);
                dto.setCor(campos[3]);
                dto.setCursorLocal(Boolean.parseBoolean(campos[4]));
                dto.setCopyrect(Boolean.parseBoolean(campos[5]));
                dto.setRre(Boolean.parseBoolean(campos[6]));
                dto.setHextile(Boolean.parseBoolean(campos[7]));
                dto.setZlib(Boolean.parseBoolean(campos[8]));
                dto.setAtivar(Boolean.parseBoolean(campos[9]));
                vncDTO = dto;
            }
        }
    }
}
