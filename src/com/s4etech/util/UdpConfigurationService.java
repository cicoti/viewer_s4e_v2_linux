package com.s4etech.util;

import com.s4etech.dto.UDPDTO;
import com.s4etech.util.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class UdpConfigurationService {

    private static final Logger logger = LoggerFactory.getLogger(UdpConfigurationService.class);

    public boolean salvarConfiguracao(UDPDTO dto, Path path, JLabel feedbackLabel) {
        if (!isValidIp(dto.getIpDestino())) {
            showMessage(feedbackLabel, "IP remoto inválido.", Color.RED);
            return false;
        }
        if (!isValidIp(dto.getIpLocal())) {
            showMessage(feedbackLabel, "IP local inválido.", Color.RED);
            return false;
        }
        if (!isValidPort(dto.getPortaDestino())) {
            showMessage(feedbackLabel, "Porta remota inválida.", Color.RED);
            return false;
        }
        if (!isValidPort(dto.getPortaLocal())) {
            showMessage(feedbackLabel, "Porta local inválida.", Color.RED);
            return false;
        }

        int portaDestino = Integer.parseInt(dto.getPortaDestino());
        int portaLocal = Integer.parseInt(dto.getPortaLocal());

        if (!isUdpRemoteReachable(dto.getIpDestino(), portaDestino)) {
            showMessage(feedbackLabel, "IP/porta remota inacessível (ou sem confirmação).", Color.RED);
            return false;
        }

        try {
            Files.createDirectories(path.getParent());
            if (!Files.exists(path)) Files.createFile(path);
        } catch (IOException e) {
            logger.error("Erro criando arquivo de configuração UDP", e);
            showMessage(feedbackLabel, "Erro criando arquivo.", Color.RED);
            return false;
        }

        String conteudo = "ipDestino|portaDestino|ipLocal|portaLocal|ativar\n" +
                dto.getIpDestino() + "|" +
                dto.getPortaDestino() + "|" +
                dto.getIpLocal() + "|" +
                dto.getPortaLocal() + "|" +
                dto.isAtivar() + "\n";

        try {
            com.s4etech.config.manager.UDPConfigurationManager.writeConfiguracao(conteudo);
        } catch (Exception e) {
            logger.error("Erro ao salvar configuração UDP", e);
            showMessage(feedbackLabel, "Erro ao salvar configuração.", Color.RED);
            return false;
        }

        showMessage(feedbackLabel, "Configuração salva com sucesso!", new Color(0, 128, 0));
        return true;
    }

    private boolean isValidIp(String ip) {
        try {
            if (ip == null || ip.isEmpty()) return false;
            String[] parts = ip.split("\\.");
            if (parts.length != 4) return false;
            for (String part : parts) {
                int i = Integer.parseInt(part);
                if (i < 0 || i > 255) return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidPort(String portStr) {
        try {
            int port = Integer.parseInt(portStr);
            return port >= 1 && port <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isUdpRemoteReachable(String ip, int port) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(1000);
            byte[] buf = "ping".getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(ip), port);
            socket.send(packet);

            // ❗ Aviso: UDP não garante resposta
            logger.info("Pacote UDP enviado para {}:{} (não há confirmação de recepção com UDP)", ip, port);

            return true;
        } catch (Exception e) {
            logger.warn("Falha ao alcançar IP/porta remota: {}:{} - {}", ip, port, e.getMessage());
            return false;
        }
    }

    private boolean canBindLocalPort(int port) {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            return true;
        } catch (IOException e) {
            logger.warn("Porta local {} indisponível: {}", port, e.getMessage());
            return false;
        }
    }

    private void showMessage(JLabel label, String msg, Color color) {
        SwingUtilities.invokeLater(() -> MessageUtils.updateDescriptionLabel(label, msg, color));
    }
}
