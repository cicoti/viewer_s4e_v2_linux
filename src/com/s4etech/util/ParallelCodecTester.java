package com.s4etech.util;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.freedesktop.gstreamer.Gst;

import java.awt.Color;
import java.util.concurrent.*;

public class ParallelCodecTester {

    public void testStreams(String rtspUrl, JLabel jLabel) {
        ExecutorService executor = Executors.newSingleThreadExecutor(); // Usa um executor com uma única thread

        Callable<String> videoCallable = () -> {
            // Cria uma instância da classe ValidaRTSP
            ValidaRTSP player = new ValidaRTSP(rtspUrl);
            try {
                // Chama o método testVideoStream e retorna o resultado
                String result = player.testVideoStream();
                return result;
            } finally {
                // Limpeza: parar e liberar o pipeline
                player.stop();
                player.release();
                Gst.deinit();
            }
        };

        Future<String> futureVideo = executor.submit(videoCallable); // Submete o Callable ao executor

        try {
            // Aguarda a conclusão da tarefa de vídeo
            String resultVideo = futureVideo.get();

            // Define o resultado baseado na resposta do teste de vídeo
            String resultado;
            if ("sucesso".equals(resultVideo)) {
                resultado = "Sucesso!";
            } else {
                resultado = "Sem resposta.";
            }

            // Atualiza a UI com o resultado
            SwingUtilities.invokeLater(() -> {
                jLabel.setText(resultado);
                jLabel.setForeground(Color.WHITE);
            });

        } catch (InterruptedException | ExecutionException e) {
            // Trata exceções e atualiza a UI em caso de erro
            SwingUtilities.invokeLater(() -> {
                jLabel.setText("Erro ao processar: " + e.getMessage());
                jLabel.setForeground(Color.RED);
            });
            Thread.currentThread().interrupt(); // Restaura o estado interrompido
        } finally {
            executor.shutdown(); // Desativa novas tarefas
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow(); // Força o encerramento se demorar demais
                }
            } catch (InterruptedException e) {
                executor.shutdownNow(); // Força o encerramento em caso de interrupção
                Thread.currentThread().interrupt();
            }
        }
    }
}
