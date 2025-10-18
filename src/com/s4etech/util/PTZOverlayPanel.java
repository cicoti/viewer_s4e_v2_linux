package com.s4etech.util;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import org.onvif.ver10.schema.Profile;

import com.s4etech.dto.CameraDTO;
import com.s4etech.integration.VideoOverlayManager;
import com.s4etech.ui.screens.Viewer;

import de.onvif.soap.OnvifDevice;
import de.onvif.soap.SOAP;
import de.onvif.soap.devices.PtzDevices;

public class PTZOverlayPanel extends JPanel {
    private static final long serialVersionUID = 3152494908561796052L;
    
    private String text;
    private String profileToken;
    private JButton resizeButton;
    private JButton panButtonIn, tiltButtonIn, zoomButtonIn;
    private JButton panButtonOut, tiltButtonOut, zoomButtonOut;
        
    private void configureButton(JButton button, Color color, Font font, Dimension size) {
        button.setBackground(color);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(size);
    }

    public PTZOverlayPanel(OnvifDevice onvifDevice, Viewer viewer, CameraDTO camera) {
        this.text = "";
        SOAP soap = onvifDevice.getSoap();
        PtzDevices ptzDevices = onvifDevice.getPtz();
        Profile profile = onvifDevice.getDevices().getProfiles().get(0);
        profileToken = profile.getToken();
        
        Color pastelGreen = new Color(156, 220, 156);
        Font font = new Font("Arial", Font.BOLD, 12);
        Dimension buttonSize = new Dimension(80, 35);
        Dimension resizeButtonSize = new Dimension(80, 80); // Aumenta a altura do botão Resize
                
        panButtonIn = new JButton("Pan +");
        tiltButtonIn = new JButton("Tilt +");
        zoomButtonIn = new JButton("Zoom +");
        resizeButton = new JButton("Re-Size");
        panButtonOut = new JButton("Pan -");
        tiltButtonOut = new JButton("Tilt -");
        zoomButtonOut = new JButton("Zoom -");

        configureButton(panButtonIn, pastelGreen, font, buttonSize);
        configureButton(tiltButtonIn, pastelGreen, font, buttonSize);
        configureButton(zoomButtonIn, pastelGreen, font, buttonSize);
        configureButton(resizeButton, pastelGreen, font, resizeButtonSize);
        configureButton(panButtonOut, pastelGreen, font, buttonSize);
        configureButton(tiltButtonOut, pastelGreen, font, buttonSize);
        configureButton(zoomButtonOut, pastelGreen, font, buttonSize);
        
        //  ao criar os botões
        panButtonIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Manter o OverlayPanel visível
                PTZOverlayPanel.this.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Manter o OverlayPanel visível
                PTZOverlayPanel.this.setVisible(true);
            }
        });
        
        //  ao criar os botões
        tiltButtonIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Manter o OverlayPanel visível
                PTZOverlayPanel.this.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Manter o OverlayPanel visível
                PTZOverlayPanel.this.setVisible(true);
            }
        });
        
        //  ao criar os botões
        zoomButtonIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Manter o OverlayPanel visível
                PTZOverlayPanel.this.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Manter o OverlayPanel visível
                PTZOverlayPanel.this.setVisible(true);
            }
        });
        
    //  ao criar os botões
        resizeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Manter o OverlayPanel visível
                PTZOverlayPanel.this.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Manter o OverlayPanel visível
                PTZOverlayPanel.this.setVisible(true);
            }
        });
        
        
        //  ao criar os botões
        panButtonOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Manter o OverlayPanel visível
                PTZOverlayPanel.this.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Manter o OverlayPanel visível
                PTZOverlayPanel.this.setVisible(true);
            }
        });
        
        //  ao criar os botões
        tiltButtonOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Manter o OverlayPanel visível
                PTZOverlayPanel.this.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Manter o OverlayPanel visível
                PTZOverlayPanel.this.setVisible(true);
            }
        });
        
        //  ao criar os botões
        zoomButtonOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Manter o OverlayPanel visível
                PTZOverlayPanel.this.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Manter o OverlayPanel visível
                PTZOverlayPanel.this.setVisible(true);
            }
        });


        JPanel buttonBox = new JPanel();
        buttonBox.setLayout(new BoxLayout(buttonBox, BoxLayout.Y_AXIS));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        topPanel.add(panButtonIn);
        topPanel.add(tiltButtonIn);
        topPanel.add(zoomButtonIn);
        topPanel.setOpaque(false);
        
     // Definir os listeners para os botões
        panButtonIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ptzDevices.continuousMove(profileToken, +0.5f, 0.0f, 0.0f);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ptzDevices.stopMove(profileToken);
            }
        });
        tiltButtonIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ptzDevices.continuousMove(profileToken, 0, +0.5f, 0);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ptzDevices.stopMove(profileToken);
            }
        });
        zoomButtonIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ptzDevices.continuousMove(profileToken, 0.0f, 0.0f, +0.5f);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ptzDevices.stopMove(profileToken);
            }
        });
        resizeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        viewer.recreateCameraPipelineResize(camera);
                        return null;
                    }
                }.execute();
            }
        });


        JPanel middlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        middlePanel.add(panButtonOut);
        middlePanel.add(tiltButtonOut);
        middlePanel.add(zoomButtonOut);
        middlePanel.setOpaque(false);
        
     // Definir os listeners para os botões
        panButtonOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ptzDevices.continuousMove(profileToken, -0.5f, 0.0f, 0.0f);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ptzDevices.stopMove(profileToken);
            }
        });
        tiltButtonOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ptzDevices.continuousMove(profileToken, 0.0f, -0.5f, 0.0f);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ptzDevices.stopMove(profileToken);
            }
        });
        zoomButtonOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ptzDevices.continuousMove(profileToken, 0.0f, 0.0f, -0.5f);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ptzDevices.stopMove(profileToken);
            }
        });

        
        buttonBox.add(topPanel);
        buttonBox.add(middlePanel);
        buttonBox.setOpaque(false);

        JPanel mainContainer = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        mainContainer.add(buttonBox, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainContainer.add(resizeButton, gbc);
        mainContainer.setOpaque(false);

        setLayout(new BorderLayout());
        add(mainContainer, BorderLayout.NORTH);
        setOpaque(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString(text, 10, 20);
    }

    public void setText(String text) {
        this.text = text;
        repaint();
    }

	public String getProfileToken() {
		return profileToken;
	}

	public void setProfileToken(String profileToken) {
		this.profileToken = profileToken;
	}

	public JButton getResizeButton() {
		return resizeButton;
	}

	public void setResizeButton(JButton resizeButton) {
		this.resizeButton = resizeButton;
	}

	public JButton getPanButtonIn() {
		return panButtonIn;
	}

	public void setPanButtonIn(JButton panButtonIn) {
		this.panButtonIn = panButtonIn;
	}

	public JButton getTiltButtonIn() {
		return tiltButtonIn;
	}

	public void setTiltButtonIn(JButton tiltButtonIn) {
		this.tiltButtonIn = tiltButtonIn;
	}

	public JButton getZoomButtonIn() {
		return zoomButtonIn;
	}

	public void setZoomButtonIn(JButton zoomButtonIn) {
		this.zoomButtonIn = zoomButtonIn;
	}

	public JButton getPanButtonOut() {
		return panButtonOut;
	}

	public void setPanButtonOut(JButton panButtonOut) {
		this.panButtonOut = panButtonOut;
	}

	public JButton getTiltButtonOut() {
		return tiltButtonOut;
	}

	public void setTiltButtonOut(JButton tiltButtonOut) {
		this.tiltButtonOut = tiltButtonOut;
	}

	public JButton getZoomButtonOut() {
		return zoomButtonOut;
	}

	public void setZoomButtonOut(JButton zoomButtonOut) {
		this.zoomButtonOut = zoomButtonOut;
	}

	public String getText() {
		return text;
	}
    
    
}
