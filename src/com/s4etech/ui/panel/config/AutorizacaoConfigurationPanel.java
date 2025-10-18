package com.s4etech.ui.panel.config;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JInternalFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.formdev.flatlaf.FlatClientProperties;
import com.s4etech.config.manager.AutorizacaoConfigurationManager;
import com.s4etech.dto.AutorizacaoDTO;
import com.s4etech.util.MessageUtils;

public class AutorizacaoConfigurationPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(AutorizacaoConfigurationPanel.class);

	private Path pathautorizacaoconf;
	private static AutorizacaoDTO autorizacaoDTO;
	private JInternalFrame menuInternalFrame;
	private String usuario = null;

	public AutorizacaoConfigurationPanel(String usuario, JInternalFrame menuInternalFrame) {
		this.usuario = usuario;
		this.menuInternalFrame = menuInternalFrame;
		pathautorizacaoconf = Paths.get(System.getProperty("user.dir"), "configuracao/autorizacao.cfg");
		autorizacaoDTO = AutorizacaoConfigurationManager.get();
		if(autorizacaoDTO==null) {
			autorizacaoDTO = new AutorizacaoDTO();
			autorizacaoDTO.setUsuario(usuario);
			autorizacaoDTO.setAutorizacao("S4e@2020#");
		}
		initComponents();
	}

	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel4 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField(autorizacaoDTO.getUsuario());
		jTextField2 = new javax.swing.JPasswordField(autorizacaoDTO.getAutorizacao());
		jTextField2.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");
		jTextField3 = new javax.swing.JPasswordField(autorizacaoDTO.getAutorizacao());
		jTextField3.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");
		jButton3 = new javax.swing.JButton();
		jLabel1.setText("Painel de Autorização");
		
		jLabel2.setText("Usuário:");
		jLabel4.setText("Nova Autorização:");
		jLabel5.setText("Confirmação:");
		
		jButton3.setText("Salvar");

		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
		            salvarJButton3ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton3)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(133, 133, 133)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                            .addComponent(jTextField2)
                            .addComponent(jTextField3))))
                .addGap(9, 9, 9))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 299, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(12, 12, 12))
        );
    }// </editor-fold>      

	
	
	private void salvarJButton3ActionPerformed(java.awt.event.ActionEvent evt) {

		if(!jTextField2.getText().equals(jTextField3.getText())) {
			
			MessageUtils.updateDescriptionLabel(jLabel3,"O novo valor da autorização não foi confirmado.",Color.RED);
  	         return;
			
		}
				
		try {
			Files.createDirectories(pathautorizacaoconf.getParent());
			if (!Files.exists(pathautorizacaoconf)) {
				Files.createFile(pathautorizacaoconf);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		autorizacaoDTO = new AutorizacaoDTO();
		LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String dataFormatada = agora.format(formatter);
        autorizacaoDTO.setUsuario(jTextField1.getText());
        autorizacaoDTO.setAutorizacao(jTextField2.getText());
        autorizacaoDTO.setDataAlteracao(dataFormatada);
        
		String arquivoconfiguracao = "usuario|autorizacao|dataAtualizacao\n";
		arquivoconfiguracao += autorizacaoDTO.getUsuario() + "|" + autorizacaoDTO.getAutorizacao() + "|" + autorizacaoDTO.getDataAlteracao() + "\n";

		try {
			AutorizacaoConfigurationManager.writeConfiguracao(arquivoconfiguracao);
		} catch (Exception e) {
			e.printStackTrace();
		}

		jTextField1.setText("");
		jTextField2.setText("");
		jTextField3.setText("");

		this.menuInternalFrame.remove(this);

		this.menuInternalFrame.add(new EstadoConfigurationPanel());
		this.menuInternalFrame.revalidate();
		this.menuInternalFrame.repaint();

	}

	private javax.swing.JButton jButton3;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JTextField jTextField1;
	private javax.swing.JPasswordField jTextField2;
	private javax.swing.JPasswordField jTextField3;

}
