package com.s4etech.ui.panel.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JInternalFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s4etech.config.manager.WebBrowserConfigurationManager;
import com.s4etech.dto.WebBrowserDTO;

public class WebBrowserConfigurationPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(WebBrowserConfigurationPanel.class);

	private Path pathwebbrowserconf;
	private static WebBrowserDTO webBrowserDTO;
	private JInternalFrame menuInternalFrame;

	public WebBrowserConfigurationPanel(JInternalFrame menuInternalFrame) {
		this.menuInternalFrame = menuInternalFrame;
		pathwebbrowserconf = Paths.get(System.getProperty("user.dir"), "configuracao/webbrowser.cfg");
		initComponents();
		webBrowserDTO = WebBrowserConfigurationManager.get();
		if (webBrowserDTO != null) {
			jTextField1.setText(webBrowserDTO.getUrl1());
			jTextField2.setText(webBrowserDTO.getUrl2());
			if (webBrowserDTO.isAtivar()) {
				jCheckBox1.setSelected(true);
			} else {
				jCheckBox1.setSelected(false);
			}
		}
	}

	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField();
		jTextField2 = new javax.swing.JTextField();
		jButton3 = new javax.swing.JButton();
		jCheckBox1 = new javax.swing.JCheckBox();
		jLabel1.setText("Painel de Configuração do Web Browser");
		jLabel2.setText("WEB-1:");
		jLabel4.setText("WEB-2:");
		jButton3.setText("Salvar");
 
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
		            salvarJButton3ActionPerformed(evt);
			}
		});
		
		jCheckBox1.setText("Iniciar web browser?");

		 javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
	        this.setLayout(layout);
	        layout.setHorizontalGroup(
	        	    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	        	        .addGroup(layout.createSequentialGroup()
	        	            .addGap(12, 12, 12)
	        	            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	        	                .addGroup(layout.createSequentialGroup()
	        	                    .addComponent(jLabel1)
	        	                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	        	                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
	        	                .addGroup(layout.createSequentialGroup()
	        	                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	        	                        .addComponent(jLabel2)
	        	                        .addComponent(jLabel4))
	        	                    .addGap(66, 66, 66)
	        	                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	        	                        .addComponent(jCheckBox1)
	        	                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
	        	                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)))
	        	                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	        	                    .addGap(0, 0, Short.MAX_VALUE)
	        	                    .addComponent(jButton3)))
	        	            .addContainerGap(13, Short.MAX_VALUE))
	        	);
	        layout.setVerticalGroup(
	        	    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	        	        .addGroup(layout.createSequentialGroup()
	        	            .addGap(12, 12, 12)
	        	            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	        	                .addComponent(jLabel1)
	        	                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
	        	            .addGap(12, 12, 12)
	        	            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	        	                .addComponent(jLabel2)
	        	                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	        	            .addGap(11, 11, 11)
	        	            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	        	                .addComponent(jLabel4)
	        	                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	        	            .addGap(18, 18, 18)
	        	            .addComponent(jCheckBox1)
	        	            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 295, Short.MAX_VALUE)
	        	            .addComponent(jButton3)
	        	            .addGap(12, 12, 12))
	        	);
	}

	private void salvarJButton3ActionPerformed(java.awt.event.ActionEvent evt) {

		try {
			Files.createDirectories(pathwebbrowserconf.getParent());
			if (!Files.exists(pathwebbrowserconf)) {
				Files.createFile(pathwebbrowserconf);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		webBrowserDTO = new WebBrowserDTO();
		webBrowserDTO.setUrl1(jTextField1.getText().trim());
		webBrowserDTO.setUrl2(jTextField2.getText().trim());
		webBrowserDTO.setAtivar(jCheckBox1.isSelected() ? true : false);

		String arquivoconfiguracao = "url1|url2|ativar\n";
		arquivoconfiguracao += webBrowserDTO.getUrl1() + "|" + webBrowserDTO.getUrl2() + "|" + webBrowserDTO.isAtivar() + "\n";

		try {
			WebBrowserConfigurationManager.writeConfiguracao(arquivoconfiguracao);
		} catch (Exception e) {
			e.printStackTrace();
		}

		jTextField1.setText("");
		jCheckBox1.setSelected(false);

		this.menuInternalFrame.remove(this);

		this.menuInternalFrame.add(new EstadoConfigurationPanel());
		this.menuInternalFrame.revalidate();
		this.menuInternalFrame.repaint();

	}

	private javax.swing.JButton jButton3;
	private javax.swing.JCheckBox jCheckBox1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JTextField jTextField1;
	private javax.swing.JTextField jTextField2;

}
