package com.s4etech.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NetworkPingChecker {

    public static boolean pingHost(String host) {
        if (host == null || host.trim().isEmpty()) {
            throw new IllegalArgumentException("Host cannot be null or empty");
        }

        String os = System.getProperty("os.name").toLowerCase();
        String pingCommand = os.contains("win") ? "ping -n 1 " + host : "ping -c 1 " + host;

        try {
            Process process = Runtime.getRuntime().exec(pingCommand);

            try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

                String line;
                while ((line = stdInput.readLine()) != null) {
                    if (line.contains("TTL=") || line.contains("ttl=")) {
                        return true;
                    }
                }

                // Captura saída de erro, se necessário
                while ((line = stdError.readLine()) != null) {
                    System.err.println(line); // Apenas para depuração, remova se necessário
                }

                // Verifica o código de saída
                return process.waitFor() == 0;

            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaura o status de interrupção
        }

        return false;
    }

}
