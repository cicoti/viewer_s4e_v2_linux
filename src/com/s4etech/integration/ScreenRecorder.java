package com.s4etech.integration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.freedesktop.gstreamer.Bus;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.Pipeline;
import org.freedesktop.gstreamer.event.EOSEvent;
import org.freedesktop.gstreamer.message.Message;
import org.freedesktop.gstreamer.message.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s4etech.config.manager.GravacaoConfigurationManager;
import com.s4etech.dto.GravacaoDTO;
import com.s4etech.dto.MonitorDTO;
import com.s4etech.util.MonitorInfo;

public class ScreenRecorder {

    private static final Logger logger = LoggerFactory.getLogger(ScreenRecorder.class);
    private List<Pipeline> activePipelines = Collections.synchronizedList(new ArrayList<>());
    private Map<String, Pipeline> monitorToPipelineMap = new HashMap<>();
    private CountDownLatch latch;
    private ScheduledExecutorService scheduler;
    private volatile boolean continuarMonitoramento = true;
    private Path pathConfigurado;
    private String usuarioConfigurado;
    private boolean isRecording = false;
    private GravacaoDTO gravacaoDTO;

    static {
        Gst.init("ScreenRecorder", new String[] {});
    }

    public synchronized void startCameras(Path path, String usuario) {
        this.pathConfigurado = path;
        this.usuarioConfigurado = usuario;

        logger.info("Iniciando startCameras para usuário: {}, path: {}", usuario, path);

        if (isRecording) {
            logger.warn("Já existe um processo de gravação em andamento.");
            return;
        }

        isRecording = true;
        continuarMonitoramento = true;

        gravacaoDTO = GravacaoConfigurationManager.get();
        String pathrecordconf = gravacaoDTO.getCaminho().replace("\\", "/");
        Path saveDirectory = getSaveDirectory(pathrecordconf, usuario);

        logger.info("Diretório base de gravação: {}", saveDirectory);

        for (MonitorDTO monitorDTO : MonitorInfo.getMonitorInfo()) {
            logger.info("Iniciando gravação para monitor {}", monitorDTO.getNumber());
            startRecord(monitorDTO, saveDirectory);
        }

        iniciarGerenciamentoHora();
        logger.info("Gravação iniciada com sucesso.");
    }

    public Path getSaveDirectory(String pathrecordconf, String usuario) {
        LocalDateTime agora = LocalDateTime.now();
        String dataFormatada = agora.format(DateTimeFormatter.ofPattern("ddMMyy"));
        Path saveDirectory = Paths.get(pathrecordconf, usuario, dataFormatada).toAbsolutePath();

        try {
            Files.createDirectories(saveDirectory);
            logger.info("Diretório de data criado ou já existente: {}", saveDirectory);
        } catch (IOException e) {
            logger.error("Erro ao criar o diretório: {}", saveDirectory, e);
        }

        return saveDirectory;
    }

    public void startRecord(MonitorDTO monitorDTO, Path saveDirectory) {
        String monitor = "monitor" + monitorDTO.getNumber();
        LocalDateTime agora = LocalDateTime.now();
        String horaDiretorio = agora.format(DateTimeFormatter.ofPattern("HH"));
        String nomeArquivo = agora.format(DateTimeFormatter.ofPattern("HHmmss")) + ".mp4";

        Path camDir = saveDirectory.resolve(horaDiretorio).resolve(monitor);

        try {
            Files.createDirectories(camDir);
            logger.info("Diretório de gravação para monitor criado: {}", camDir);
        } catch (IOException e) {
            logger.error("Erro ao criar diretório: {}", camDir, e);
        }

        String outputFileName = camDir.resolve(nomeArquivo).toString().replace("\\", "/");
        logger.info("Arquivo de saída para monitor {}: {}", monitor, outputFileName);

        try {
            Pipeline pipeline = startScreenRecording(monitorDTO, outputFileName);
            monitorToPipelineMap.put(monitor, pipeline);
            logger.info("Pipeline da câmera {} iniciado.", monitor);
        } catch (Exception e) {
            logger.error("Erro ao iniciar a câmera {}: {}", monitor, e.getMessage());
        }
    }

    private Pipeline startScreenRecording(MonitorDTO monitorDTO, String outputFileName) {
   	
        String monitor = "monitor" + monitorDTO.getNumber();
        String pipelineDesc;

        switch (gravacaoDTO.getQualidade()) {
            case "640x360":
                pipelineDesc = "d3d11screencapturesrc monitor-index=" + monitorDTO.getNumber()
                        + " ! videoconvert ! videoscale"
                        + " ! video/x-raw,width=640,height=360,framerate=15/1"
                        + " ! queue ! x264enc bitrate=500 speed-preset=superfast tune=zerolatency key-int-max=120"
                        + " ! mp4mux ! filesink location=\"" + outputFileName + "\"";
                break;
            case "1280x720":
                pipelineDesc = "d3d11screencapturesrc monitor-index=" + monitorDTO.getNumber()
                        + " ! videoconvert ! videoscale"
                        + " ! video/x-raw,width=1280,height=720,framerate=15/1"
                        + " ! queue ! x264enc bitrate=750 speed-preset=ultrafast tune=zerolatency key-int-max=60"
                        + " ! mp4mux ! filesink location=\"" + outputFileName + "\"";
                break;
            case "1920x1080":
                pipelineDesc = "d3d11screencapturesrc monitor-index=" + monitorDTO.getNumber()
                        + " ! videoconvert ! videoscale"
                        + " ! video/x-raw,width=1920,height=1080,framerate=15/1"
                        + " ! queue ! x264enc bitrate=2000 speed-preset=fast tune=zerolatency key-int-max=30"
                        + " ! mp4mux ! filesink location=\"" + outputFileName + "\"";
                break;
            default:
                throw new IllegalArgumentException("Qualidade desconhecida: " + gravacaoDTO.getQualidade());
        }

        Pipeline pipeline = (Pipeline) Gst.parseLaunch(pipelineDesc);
        pipeline.getBus().connect((Bus.MESSAGE) (bus, message) -> handleBusMessage(message));
        activePipelines.add(pipeline);
        pipeline.play();
        return pipeline;
    }

    private void handleBusMessage(Message message) {
        if (message.getType() == MessageType.EOS) {
            Pipeline pipeline = (Pipeline) message.getSource();
            pipeline.stop();
            activePipelines.remove(pipeline);
            if (latch != null) latch.countDown();
            isRecording = false;
        } else if (message.getType() == MessageType.ERROR) {
            logger.error("Erro detectado na gravação. Tipo de mensagem: ERROR");
        }
    }

    public synchronized void stopAllRecords() {
        continuarMonitoramento = false;
        latch = new CountDownLatch(activePipelines.size());

        for (Pipeline pipeline : activePipelines) {
            logger.info("Enviando EOS para pipeline {}", pipeline);
            pipeline.sendEvent(new EOSEvent());
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        for (Pipeline pipeline : new ArrayList<>(activePipelines)) {
            logger.info("Descartando pipeline {}", pipeline);
            pipeline.dispose();
        }

        activePipelines.clear();
        isRecording = false;
        logger.info("Todas as gravações foram encerradas e recursos liberados.");
    }

    public void iniciarGerenciamentoHora() {
        if (scheduler != null && !scheduler.isShutdown()) return;

        scheduler = Executors.newSingleThreadScheduledExecutor();
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime proximaHoraCheia = agora.plusHours(1).withMinute(0).withSecond(0).withNano(0);
        long delayInicial = Duration.between(agora, proximaHoraCheia).toMillis();

        scheduler.scheduleAtFixedRate(() -> {
            if (!continuarMonitoramento) return;
            logger.info("Hora cheia detectada. Reiniciando gravações...");
            stopAllRecords();
            startCameras(pathConfigurado, usuarioConfigurado);
        }, delayInicial, TimeUnit.HOURS.toMillis(1), TimeUnit.MILLISECONDS);
    }

    public synchronized boolean isRecording() {
        return isRecording;
    }
}
