package com.s4etech.util;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeepGenerator {
	
	private static final Logger logger = LoggerFactory.getLogger(BeepGenerator.class);
	
    private volatile boolean running = false; // Garantir consistência entre threads

    public void generateBeep(int frequency, float sampleRate) throws LineUnavailableException, InterruptedException {
        running = true; // Sempre iniciar com running = true

        AudioFormat format = new AudioFormat(sampleRate, 8, 1, true, true);
        SourceDataLine line = AudioSystem.getSourceDataLine(format);
        line.open(format);
        line.start();

        byte[] buffer = new byte[1];
        double angle = 0;
        double increment = 2.0 * Math.PI * frequency / sampleRate;

        while (running) {
            for (int i = 0; i < sampleRate; i++) { // 1 segundo de áudio
                buffer[0] = (byte) (Math.sin(angle) * 127);
                line.write(buffer, 0, 1);
                angle += increment;
                if (angle > (2.0 * Math.PI)) {
                    angle -= (2.0 * Math.PI);
                }
                if (!running) { // Verifica a cada iteração se precisa parar
                    break;
                }
            }

            line.drain(); // Aguarda o buffer de áudio terminar

            if (!running) { // Verifica antes da próxima iteração
                break;
            }

            Thread.sleep(100); // Pequena pausa entre os loops
        }

        line.stop();
        line.close();
    }

    public void stopBeep() {
        running = false;
    }
}
