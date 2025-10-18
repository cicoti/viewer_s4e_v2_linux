package com.s4etech.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.Timer;

public class MessageUtils {

    public static void updateDescriptionLabel(JLabel label, String text, Color color) {
        label.setText(text);
        label.setForeground(color);
        label.setFont(new Font("Roboto", Font.PLAIN, 12));
        showMessageWithFadeIn(label);
    }

    private static void showMessageWithFadeIn(JLabel label) {
        Color originalColor = label.getForeground();
        Timer timer = new Timer(20, new ActionListener() {
            private float alpha = 0.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                alpha += 0.05f;
                if (alpha >= 1.0f) {
                    alpha = 1.0f;
                    ((Timer) e.getSource()).stop();
                }

                label.setForeground(new Color(originalColor.getRed(), originalColor.getGreen(), originalColor.getBlue(),
                        Math.round(alpha * 255))); // Cor vermelha com opacidade

            }
        });

        timer.start();

        Timer fadeOutTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.setDelay(20);
                timer.setInitialDelay(0);
                timer.setRepeats(true);
                timer.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!timer.isRunning()) {
                            return;
                        }
                        ((Timer) e.getSource()).stop();
                    }
                });
                timer.restart();
            }
        });
        fadeOutTimer.setRepeats(false);
        fadeOutTimer.start();
    }
}
