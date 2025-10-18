package com.s4etech.config.manager;

import com.s4e.CryptoUtils;
import com.s4etech.dto.CameraDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class CameraConfigurationManager {

    private static final Logger logger = LoggerFactory.getLogger(CameraConfigurationManager.class);

    private static final Path pathcameraconf = Paths.get(
            System.getProperty("user.dir"),
            "configuracao",
            "rtspcamera.cfg"
    );

    private static List<CameraDTO> camerasDTO = null;

    public static List<CameraDTO> get() {
        logger.info("Carregando lista de câmeras.");
        try {
            readConfiguracao();
        } catch (IOException e) {
            logger.error("Erro ao ler o arquivo de configuração, arquivo não encontrado.", e);
            return new ArrayList<>();
        }

        return camerasDTO;
    }

    public static void writeConfiguracao(String conteudoPlano) throws Exception {
        try {
            String criptografado = CryptoUtils.encrypt(conteudoPlano);

            try (BufferedWriter writer = Files.newBufferedWriter(
                    pathcameraconf,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.CREATE
            )) {
                writer.write(criptografado);
                writer.newLine();
            }

        } catch (Exception e) {
            logger.error("Erro ao gravar o arquivo de configuração criptografado.", e);
            throw e;
        }
    }

    private static void readConfiguracao() throws IOException {
        List<String> listRTSP = new ArrayList<>();

        String contentArquivo = new String(
                Files.readAllBytes(pathcameraconf),
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

            // Assume como texto puro e regrava de forma segura
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
            listRTSP.add(linha);
        }

        try {
            processConfiguracao(listRTSP);
        } catch (Exception e) {
            logger.error("Arquivo de configuração incompatível. Farei backup e deletarei o arquivo.", e);

            Path backupPath = Paths.get(pathcameraconf.toString() + ".bak");
            try {
                Files.copy(pathcameraconf, backupPath);
            } catch (IOException copyEx) {
                logger.warn("Falhou ao criar backup do arquivo de configuração.", copyEx);
            }

            try {
                Files.delete(pathcameraconf);
                logger.info("Arquivo de configuração deletado: {}", pathcameraconf);
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

    private static void processConfiguracao(List<String> listRTSP) {
        camerasDTO = new ArrayList<>();

        for (String linha : listRTSP) {
            String[] campos = linha.trim().split("\\|", -1);
            if (campos.length <= 1) {
                break;
            }
            if (!"codcam".equals(campos[0])) {
                CameraDTO camera = new CameraDTO();
                camera.setCodigo(campos[0]);
                camera.setIp(campos[1]);
                camera.setPorta(campos[2]);
                camera.setExtensao(campos[3]);
                camera.setPTZ(Integer.parseInt(campos[4]));
                camera.setVideo(campos[5]);
                camera.setAudio(campos[6]);
                camera.setUsuario(campos[7]);
                camera.setSenha(campos[8]);
                camerasDTO.add(camera);
            }
        }
    }

    public static CameraDTO getCameraByCodigo(String codigo) {
        try {
            if (camerasDTO == null) {
                readConfiguracao();
            }
            for (CameraDTO camera : camerasDTO) {
                if (camera.getCodigo().equals(codigo)) {
                    return camera;
                }
            }
        } catch (IOException e) {
            logger.error("Erro ao ler o arquivo de configuração, arquivo não encontrado.", e);
        }
        return null;
    }
}
