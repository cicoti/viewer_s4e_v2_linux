package com.s4etech.util;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmSoundHandler {

	private static final Logger logger = LoggerFactory.getLogger(AlarmSoundHandler.class);

    private BeepGenerator beepGenerator;
    private VisualAlarmHandler visualAlarmHandler;
    private Thread beepThread;
    private boolean isPlaying = false;
    private Timer autoStopTimer;

    public AlarmSoundHandler() {
        this.visualAlarmHandler = new VisualAlarmHandler();
    }

    public synchronized void playAlarm() {
        if (!isPlaying) {
            isPlaying = true;
            beepGenerator = new BeepGenerator();

            beepThread = new Thread(() -> {
                try {
                    beepGenerator.generateBeep(2850, 44100);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isPlaying = false;
                }
            });

            beepThread.setDaemon(true);
            beepThread.start();
            visualAlarmHandler.activateAlarm();

            // Inicia o timer de 3 segundos para desligar automaticamente
            iniciarTimerAutoStop();
        }
    }

    public synchronized void stopAlarm() {
        if (isPlaying) {
            isPlaying = false;
            if (beepGenerator != null) beepGenerator.stopBeep();
            if (beepThread != null && beepThread.isAlive()) beepThread.interrupt();
        }

        if (autoStopTimer != null) {
            autoStopTimer.cancel();
            autoStopTimer = null;
        }

        visualAlarmHandler.deactivateAlarm();
    }

    private void iniciarTimerAutoStop() {
        if (autoStopTimer != null) {
            autoStopTimer.cancel();
        }

        autoStopTimer = new Timer(true);
        autoStopTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                logger.warn("Desligando alarme automaticamente após 3s sem OFF.");
                stopAlarm();
            }
        }, 3000);
    }
}
