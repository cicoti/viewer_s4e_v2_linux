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

        // Obtém dimensões do monitor — assumindo que seu MonitorDTO já traz x, y, width, height.
        int startX = monitorDTO.getX();
        int startY = monitorDTO.getY();
        int width  = monitorDTO.getWidth();
        int height = monitorDTO.getHeight();

        logger.info("Iniciando gravação do monitor {} na área: x={}, y={}, width={}, height={}",
                monitorDTO.getNumber(), startX, startY, width, height);

        // Define o framerate e bitrate com base na qualidade configurada
        int frameRate = 15;
        int bitrate;
        switch (gravacaoDTO.getQualidade()) {
            case "640x360":
                width = 640;
                height = 360;
                bitrate = 500;
                break;
            case "1280x720":
                width = 1280;
                height = 720;
                bitrate = 750;
                break;
            case "1920x1080":
                width = 1920;
                height = 1080;
                bitrate = 2000;
                break;
            default:
                throw new IllegalArgumentException("Qualidade desconhecida: " + gravacaoDTO.getQualidade());
        }

        /*
         *  🎥 PIPELINE PARA LINUX (X11)
         *  - ximagesrc: captura da tela no X11
         *  - startx/starty/endx/endy: área de captura do monitor
         *  - videoconvert/videoscale: converte e redimensiona
         *  - x264enc + mp4mux: codifica e grava
         */
        pipelineDesc = String.format(
            "ximagesrc use-damage=0 show-pointer=false startx=%d starty=%d endx=%d endy=%d "
          + "! videoconvert ! videoscale "
          + "! video/x-raw,width=%d,height=%d,framerate=%d/1 "
          + "! queue "
          + "! x264enc bitrate=%d speed-preset=ultrafast tune=zerolatency key-int-max=60 "
          + "! mp4mux ! filesink location=\"%s\"",
          startX, startY, startX + width, startY + height,
          width, height, frameRate,
          bitrate, outputFileName
        );

        logger.debug("Pipeline gerado: {}", pipelineDesc);

        Pipeline pipeline = (Pipeline) Gst.parseLaunch(pipelineDesc);
        pipeline.getBus().connect((Bus.MESSAGE) (bus, message) -> handleBusMessage(message));
        activePipelines.add(pipeline);
        pipeline.play();

        logger.info("Pipeline do monitor {} iniciado com sucesso. Gravando em {}", monitor, outputFileName);

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
        Gst.deinit(); 
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
