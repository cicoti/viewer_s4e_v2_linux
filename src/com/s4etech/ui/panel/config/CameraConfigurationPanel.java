package com.s4etech.ui.panel.config;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.formdev.flatlaf.FlatClientProperties;
import com.s4etech.config.manager.CameraConfigurationManager;
import com.s4etech.dto.CameraDTO;
import com.s4etech.dto.ConfiguracaoVisualDTO;
import com.s4etech.util.MessageUtils;
import com.s4etech.util.ParallelCodecTester;
import com.s4etech.util.RSTPInfo;

public class CameraConfigurationPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(CameraConfigurationPanel.class);
	
	private static List<CameraDTO> cameras;
	private DefaultTableModel tableModel;
	private Path pathvisualconf;
	private Path pathcameraconf;
	private JInternalFrame menuInternalFrame;

	public CameraConfigurationPanel(String usuario, JInternalFrame menuInternalFrame) {
		this.menuInternalFrame = menuInternalFrame;
		pathvisualconf = Paths.get(System.getProperty("user.dir"), "configuracao/" + usuario + "_visual.cfg");
		pathcameraconf = Paths.get(System.getProperty("user.dir"), "configuracao/rtspcamera.cfg");
		initComponents();
		DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
		model.setRowCount(0);
		cameras = CameraConfigurationManager.get();
		 if (cameras != null && cameras.size() > 0) {
			for (CameraDTO cameraDTO : cameras) {
				
				tableModel.addRow(new Object[] { cameraDTO.getCodigo(), 
						cameraDTO.getIp(),
						cameraDTO.getPorta(), 
						cameraDTO.getExtensao(),
						(cameraDTO.getPTZ() == 1) ? "SIM" : "NÃO", 
								cameraDTO.getVideo().equals("video codec") ? "NÃO" : cameraDTO.getVideo(), 
								cameraDTO.getAudio().equals("audio codec") ? "NÃO" : cameraDTO.getAudio(),
								cameraDTO.getUsuario(), 
								cameraDTO.getSenha() });
			}
		}
	}

	private void initComponents() {

		jTable1 = new javax.swing.JTable();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jScrollPane4 = new javax.swing.JScrollPane();
		jButton3 = new javax.swing.JButton();
		jLabel5 = new javax.swing.JLabel();
		//jTextField2 = new javax.swing.JTextField();
		jCheckBox1 = new javax.swing.JCheckBox();
		jLabel6 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		jLabel10 = new javax.swing.JLabel();
		jTextField3 = new javax.swing.JTextField();
		
		jTextField4 = new javax.swing.JPasswordField();
		jTextField4.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");
		
		jTextField5 = new javax.swing.JTextField();
		jTextField6 = new javax.swing.JTextField();
		
		jComboBox3 = new javax.swing.JComboBox<>();
		jComboBox3.setEditable(true);
		
		jComboBoxCodCam = new javax.swing.JComboBox<>();
		jComboBoxCodCam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "CAM-1", "CAM-2", "CAM-3", "CAM-4", "CAM-5", "CAM-6", "CAM-7", "CAM-8", "CAM-9", "CAM-10", "CAM-11", "CAM-12", "CAM-13", "CAM-14", "CAM-15", "CAM-16" }));

		// ActionListener para atualizar a lista de jComboBox3 com base na seleção
		jComboBoxCodCam.addActionListener(e -> {
		    String cameraSelecionada = (String) jComboBoxCodCam.getSelectedItem();
		    
		    // Atualiza a porta se estiver vazia
		    String porta = jTextField6.getText(); 
		    if (porta == null || porta.trim().isEmpty()) {
		        jTextField6.setText("554");
		    }

		    // Atualiza a lista do jComboBox3 com base na câmera selecionada
		    if (cameraSelecionada != null && !cameraSelecionada.isEmpty()) {
		 
		        String camNumber = cameraSelecionada.split("-")[1];  // Extrai o número da câmera (CAM-1 -> 1)
		        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {
		        		"Digite aqui ou escolha uma opção.",
		            "/cam" + camNumber + "/substream",
		            "/cam" + camNumber + "/mainstream",
		            "/cam/realmonitor?channel=1&subtype=0",
		            "/cam/realmonitor?channel=1&subtype=1"
		            
		        }));
		    } else {
		        // Se nenhuma câmera for selecionada, limpar ou redefinir o jComboBox3
		        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
		    }
		});


		
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();

		tableModel = new DefaultTableModel(new Object[][] { { null, null, null, null, null, null, null, null, null } },
				new String[] { "Código", "IP", "Porta", "Extensão", "PTZ", "Video", "Audio", "user", "pass" }) {
			private static final long serialVersionUID = 1L;
			boolean[] canEdit = new boolean[] { false, false, false, false, false, false, false, false, false };

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		};

		jTable1.setModel(tableModel);

		TableColumnModel tcm = jTable1.getColumnModel();
		tcm.getColumn(tcm.getColumnIndex("Código")).setPreferredWidth(50);
		tcm.getColumn(tcm.getColumnIndex("IP")).setPreferredWidth(75);
		tcm.getColumn(tcm.getColumnIndex("Porta")).setPreferredWidth(50);
		tcm.getColumn(tcm.getColumnIndex("Extensão")).setPreferredWidth(100);
		tcm.getColumn(tcm.getColumnIndex("PTZ")).setPreferredWidth(50);
		tcm.getColumn(tcm.getColumnIndex("Video")).setPreferredWidth(50);
		tcm.getColumn(tcm.getColumnIndex("Audio")).setPreferredWidth(50);

		tcm.removeColumn(tcm.getColumn(tcm.getColumnIndex("user")));
		tcm.removeColumn(tcm.getColumn(tcm.getColumnIndex("pass")));

		jTable1.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        // Obtém a linha clicada
		        int rowIndex = jTable1.getSelectedRow();

		        // Obtém os dados da linha clicada
		        String codigo = (String) tableModel.getValueAt(rowIndex, 0);
		        String ip = (String) tableModel.getValueAt(rowIndex, 1);
		        String porta = (String) tableModel.getValueAt(rowIndex, 2);
		        String extensao = (String) tableModel.getValueAt(rowIndex, 3);
		        String ptz = (String) tableModel.getValueAt(rowIndex, 4);
		        String video = (String) tableModel.getValueAt(rowIndex, 5);
		        String audio = (String) tableModel.getValueAt(rowIndex, 6);
		        String usuario = (String) tableModel.getValueAt(rowIndex, 7);
		        String senha = (String) tableModel.getValueAt(rowIndex, 8);

		        jComboBoxCodCam.setSelectedItem(codigo);
		        jTextField5.setText(ip);
		        jTextField6.setText(porta);
		        jComboBox3.setSelectedItem(extensao);

		        if (ptz != null && ptz.equals("SIM")) {
		            jCheckBox1.setSelected(true);
		        } else {
		            jCheckBox1.setSelected(false);
		        }

		        jTextField3.setText(usuario);
		        jTextField4.setText(senha);

		        jComboBox2.setSelectedItem(video);
		        jComboBox1.setSelectedItem(audio);

		        jLabel8.setText("");
		    }
		});

		jScrollPane4.setViewportView(jTable1);

		jLabel1.setText("Painel de Configuração de Câmeras");

		jLabel2.setText("Código:");

		jLabel3.setText("IP:");

		jLabel4.setText("Porta:");
		
		jLabel9.setText("Codec de vídeo");
		jLabel10.setText("Codec de áudio");
		
		jButton1.setText("Excluir");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				excluirJButton1ActionPerformed(evt);
			}
		});

		jButton2.setText("Incluir");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				incluirJButton2ActionPerformed(evt);
			}
		});

		jButton3.setText("Salvar");

		jButton3.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent evt) {
		            salvarJButton3ActionPerformed(evt);
		    }
		});

		jLabel5.setText("Extensão:");

		jCheckBox1.setText("PTZ");

		jLabel6.setText("Usuário:");

		jLabel7.setText("Senha:");
		
		jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "desligado", "AAC", "PCM", "G.726" }));
		jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "H.264", "H.265"}));
	
    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(12, 12, 12)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane4))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)))
                            .addComponent(jLabel2))
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxCodCam)
                            .addComponent(jTextField3)
                            .addComponent(jTextField5)
                            .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField4)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBox1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(33, 33, 33)
                                        .addComponent(jButton2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton1))
                                    .addComponent(jLabel10)))
                            .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addComponent(jButton3))
            .addContainerGap(13, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(12, 12, 12)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(jLabel8))
            .addGap(12, 12, 12)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2)
                .addComponent(jComboBoxCodCam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel3)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(12, 12, 12)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel4)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(12, 12, 12)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel5)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(12, 12, 12)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel6))
            .addGap(12, 12, 12)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel7))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel9)
                .addComponent(jLabel10))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton2)
                .addComponent(jButton1)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jCheckBox1))
            .addGap(8, 8, 8)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButton3)
            .addGap(12, 12, 12))
    );
}// </editor-fold> 

	
	private void excluirJButton1ActionPerformed(java.awt.event.ActionEvent evt) {
	    int rowIndex = jTable1.getSelectedRow();
	    tableModel.removeRow(rowIndex);

	    jComboBoxCodCam.setSelectedIndex(0);
	    jTextField5.setText("");
	    jTextField6.setText("");
	    jComboBox3.setSelectedIndex(0);
	    jCheckBox1.setSelected(false);
	    jComboBox1.setSelectedIndex(0);
	    jComboBox2.setSelectedIndex(0);
	    jTextField3.setText("");
	    jTextField4.setText("");
	}

	private void incluirJButton2ActionPerformed(java.awt.event.ActionEvent evt) {
	    jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

	    if (isCodCamValid() && isIPValid()) {
	    	

	    	String textoDigitadoOuSelecionado = (String) jComboBox3.getSelectedItem();

	    	
	    	if(jComboBox3.getSelectedIndex()==0) {

	    		textoDigitadoOuSelecionado = "";
	    		
	    	}
	    	
	        tableModel.addRow(new Object[] {
	            jComboBoxCodCam.getSelectedItem().toString(),
	            jTextField5.getText(),
	            jTextField6.getText(),
	            textoDigitadoOuSelecionado,
	            jCheckBox1.isSelected() ? "SIM" : "NÃO",
	            jComboBox2.getItemAt(jComboBox2.getSelectedIndex()),
	    	    jComboBox1.getItemAt(jComboBox1.getSelectedIndex()),
	            jTextField3.getText(),
	            jTextField4.getText()
	        });

	        jLabel8.setText("");

	        RSTPInfo rstpInfo = new RSTPInfo();
	        rstpInfo.setIp(jTextField5.getText());
	        rstpInfo.setPorta(jTextField6.getText());
	        rstpInfo.setExtensao(textoDigitadoOuSelecionado);
	        rstpInfo.setUsuario(jTextField3.getText());
	        rstpInfo.setSenha(jTextField4.getText());

	        String rstp = "rtsp://" + (rstpInfo.montarURL().trim());

	        Thread animationThread = createAnimationThread();
	        animationThread.start();

	        new Thread(() -> {
	            try {
	                ParallelCodecTester parallelCodecTester = new ParallelCodecTester();
	                parallelCodecTester.testStreams(rstp, jLabel8);
	            } catch (Exception e) {
	                SwingUtilities.invokeLater(() -> MessageUtils.updateDescriptionLabel(jLabel8, "Erro ao testar.", Color.RED));
	            } finally {
	                animationThread.interrupt();
	            }
	        }).start();
	    }
	}
	
	private Thread createAnimationThread() {
	    return new Thread(() -> {

	        while (!Thread.currentThread().isInterrupted()) {

	            SwingUtilities.invokeLater(() -> MessageUtils.updateDescriptionLabel(jLabel8,"Aguarde...",Color.WHITE));
	      
	            try {
	                Thread.sleep(1000); // Ajuste o tempo de acordo com a necessidade
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	            }
	        }
	        
	    });
	}

	private String repeatDot(int count) {
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < count; i++) {
	        sb.append(".");
	    }
	    return sb.toString();
	}
	
	private boolean isCodCamValid() {
	    String codCam = jComboBoxCodCam.getSelectedItem().toString();
	    if (codCam == null || codCam.isEmpty()) {
	        MessageUtils.updateDescriptionLabel(jLabel8, "O código da câmera não foi informado.", Color.RED);
	        return false;
	    }
	    return true;
	}
	
	private boolean isIPValid() {
				
		 String ip = jTextField5.getText();
		 String ipRegex = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
		    
		if (!ip.matches(ipRegex)) {
				MessageUtils.updateDescriptionLabel(jLabel8,"O endereço IP informado não é válido.",Color.RED);
				return false;
		}
		
		return true;
	}

	private void salvarJButton3ActionPerformed(java.awt.event.ActionEvent evt) {
	    try {
	        Files.createDirectories(pathcameraconf.getParent());
	        if (!Files.exists(pathcameraconf)) {
	            Files.createFile(pathcameraconf);
	        }
	    } catch (IOException e) {
	    }

	    int rowCount = tableModel.getRowCount();
	    List<CameraDTO> cameras = new ArrayList<>();

	    for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
	        String codigo = ((String) tableModel.getValueAt(rowIndex, 0)).toUpperCase().trim();
	        String ip = ((String) tableModel.getValueAt(rowIndex, 1)).trim();
	        String porta = ((String) tableModel.getValueAt(rowIndex, 2)).trim();
	        String extensao = ((String) tableModel.getValueAt(rowIndex, 3)).trim();
	        String ptz = ((String) tableModel.getValueAt(rowIndex, 4)).trim();
	        String video = ((String) tableModel.getValueAt(rowIndex, 5)).trim();
	        String audio = ((String) tableModel.getValueAt(rowIndex, 6)).trim();
	        String usuario = ((String) tableModel.getValueAt(rowIndex, 7)).trim();
	        String senha = ((String) tableModel.getValueAt(rowIndex, 8)).trim();
	        
	        CameraDTO cameraDTO = new CameraDTO();
	        cameraDTO.setCodigo(codigo);
	        cameraDTO.setIp(ip);
	        cameraDTO.setPorta(porta);
	        cameraDTO.setExtensao(extensao);
	        cameraDTO.setPTZ(ptz.equals("SIM") ? 1 : 0);
	        cameraDTO.setVideo(video);
	        cameraDTO.setAudio(audio);
	        cameraDTO.setUsuario(usuario);
	        cameraDTO.setSenha(senha);

	        cameras.add(cameraDTO);
	    }

	    Collections.sort(cameras, (c1, c2) -> {
	        String codigo1 = c1.getCodigo();
	        String codigo2 = c2.getCodigo();

	        // Extrai os números após o prefixo "CAM-"
	        int numero1 = Integer.parseInt(codigo1.replaceAll("\\D+", ""));
	        int numero2 = Integer.parseInt(codigo2.replaceAll("\\D+", ""));

	        // Compara os números extraídos
	        return Integer.compare(numero1, numero2);
	    });
	    
	    
	    Set<String> uniqueCodes = new HashSet<>();
	    boolean hasDuplicate = false;

	    for (CameraDTO cameraDTO : cameras) {
	        if (!uniqueCodes.add(cameraDTO.getCodigo())) {
	            hasDuplicate = true;
	            break;
	        }
	    }

	    if (hasDuplicate) {
	        MessageUtils.updateDescriptionLabel(
	            jLabel8,
	            "Existem códigos de câmeras duplicados na lista.",
	            Color.RED
	        );
	        
	        return;
	    } 
	    

	    String arquivoconfiguracao = "codcam|ip|porta|extensao|ptz|video|audio|usuario|senha\n";

	    for (CameraDTO cameraDTO : cameras) {
	        arquivoconfiguracao += cameraDTO.getCodigo() 
	                + "|" + cameraDTO.getIp() 
	                + "|" + cameraDTO.getPorta()  
	                + "|" + cameraDTO.getExtensao()
	                + "|" + cameraDTO.getPTZ() 
	                + "|" + cameraDTO.getVideo()
	                + "|" + cameraDTO.getAudio()
	                + "|" + cameraDTO.getUsuario()
	                + "|" + cameraDTO.getSenha() 
	                + "\n";
	    }

	    try {
	        CameraConfigurationManager.writeConfiguracao(arquivoconfiguracao);
	    } catch (Exception e) {
	    }

	    jComboBoxCodCam.setSelectedIndex(0);
	    jTextField5.setText("");
	    jTextField6.setText("");
	    jComboBox3.setSelectedIndex(0);
	    jCheckBox1.setSelected(false);
	    jTextField3.setText("");
	    jTextField4.setText("");

	    List<ConfiguracaoVisualDTO> configuracaoVisual = loadConfigurationsFromVisualConf();

	    Iterator<ConfiguracaoVisualDTO> iterator = configuracaoVisual.iterator();
	    while (iterator.hasNext()) {
	        ConfiguracaoVisualDTO configuracao = iterator.next();

	        // Verifica se é VNC ou WEB, se for, não remove
	        if (configuracao.getCodigoCamera().contains("VNC") || configuracao.getCodigoCamera().contains("WEB-1") || configuracao.getCodigoCamera().contains("WEB-2") || configuracao.getCodigoCamera().contains("WEBC")) {
	            continue;
	        }

	        // Verifica se existe nas câmeras configuradas
	        boolean existsInCameras = cameras.stream()
	                .anyMatch(camera -> camera.getCodigo().contains(configuracao.getCodigoCamera()));

	        // Remove se não existir
	        if (!existsInCameras) {
	            iterator.remove();
	        }
	    }

	    arquivoconfiguracao = "codcam|tamh|tamv|posh|posv|rot|flip\n";

	    for (ConfiguracaoVisualDTO configuracaoVisualDTO : configuracaoVisual) {
	        arquivoconfiguracao += configuracaoVisualDTO.getCodigoCamera() + "|"
	                + configuracaoVisualDTO.getTamanhoHorizontal() + "|" + configuracaoVisualDTO.getTamanhoVertical() + "|"
	                + configuracaoVisualDTO.getPosicaoHorizontal() + "|" + configuracaoVisualDTO.getPosicaoVertical() + "|" + configuracaoVisualDTO.getRotate() + "|" + configuracaoVisualDTO.getFlip() + "\n";
	    }

	    try (BufferedWriter writer = Files.newBufferedWriter(pathvisualconf, StandardOpenOption.TRUNCATE_EXISTING,
	            StandardOpenOption.CREATE)) {
	        writer.write(arquivoconfiguracao);
	        writer.newLine();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    this.menuInternalFrame.remove(this);
	    this.menuInternalFrame.add(new EstadoConfigurationPanel());
	    this.menuInternalFrame.revalidate();
	    this.menuInternalFrame.repaint();
	}
	
	

	private List<ConfiguracaoVisualDTO> loadConfigurationsFromVisualConf() {

		try {
			Files.createDirectories(pathvisualconf.getParent());
			if (!Files.exists(pathvisualconf)) {
				Files.createFile(pathvisualconf); // Cria o arquivo se não existir
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<ConfiguracaoVisualDTO> configuracoesVisualDTO = new ArrayList<>();

		try {

			List<String> listVisual = Files.readAllLines(pathvisualconf);

			if (listVisual.isEmpty()) {

			}

			for (String linha : listVisual) {
				if (linha.isEmpty())
					break;

				String[] campos = linha.split("\\|");
				if (!"codcam".equals(campos[0])) {

					ConfiguracaoVisualDTO configuracaoVisualDTO = new ConfiguracaoVisualDTO();
					configuracaoVisualDTO.setCodigoCamera(campos[0]);
					configuracaoVisualDTO.setTamanhoHorizontal(Integer.parseInt(campos[1]));
					configuracaoVisualDTO.setTamanhoVertical(Integer.parseInt(campos[2]));
					configuracaoVisualDTO.setPosicaoHorizontal(Integer.parseInt(campos[3]));
					configuracaoVisualDTO.setPosicaoVertical(Integer.parseInt(campos[4]));
					configuracaoVisualDTO.setRotate(campos[5]);
					configuracaoVisualDTO.setFlip(campos[6]);

					configuracoesVisualDTO.add(configuracaoVisualDTO);

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return configuracoesVisualDTO;
	}

	// Variables declaration - do not modify
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JCheckBox jCheckBox1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JLabel jLabel10;
	
	private javax.swing.JScrollPane jScrollPane4;
	private javax.swing.JTable jTable1;
	//private javax.swing.JTextField jTextField2;
	private javax.swing.JTextField jTextField3;
	private javax.swing.JPasswordField jTextField4;
	private javax.swing.JTextField jTextField5;
	private javax.swing.JTextField jTextField6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBoxCodCam;
    private javax.swing.JComboBox<String> jComboBox3;

}
