package com.s4etech.ui.panel.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JInternalFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s4etech.config.manager.VNCConfigurationManager;
import com.s4etech.dto.VNCDTO;

public class VNCConfigurationPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(VNCConfigurationPanel.class);
	
	private Path pathvncconf;
	private static VNCDTO vncDTO;
	private JInternalFrame menuInternalFrame;

	public VNCConfigurationPanel(JInternalFrame menuInternalFrame) {
		this.menuInternalFrame = menuInternalFrame;
		pathvncconf = Paths.get(System.getProperty("user.dir"), "configuracao/vnc.cfg");
		initComponents();
		vncDTO = VNCConfigurationManager.get();
		if (vncDTO != null) {
			jTextField1.setText(vncDTO.getServidor());
			jTextField4.setText(vncDTO.getPorta());
			jTextField3.setText(vncDTO.getSenha());
			jComboBox1.setSelectedItem(vncDTO.getCor());
			jCheckBox1.setSelected(vncDTO.isCursorLocal());
			jCheckBox2.setSelected(vncDTO.isCopyrect());
			jCheckBox3.setSelected(vncDTO.isRre());
			jCheckBox4.setSelected(vncDTO.isHextile());
			jCheckBox5.setSelected(vncDTO.isZlib());
			jCheckBox6.setSelected(vncDTO.isAtivar());
		}
	}

	private void initComponents() {
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jButton3 = new javax.swing.JButton();
		jTextField1 = new javax.swing.JTextField();
		jTextField3 = new javax.swing.JTextField();
		jLabel6 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		jTextField4 = new javax.swing.JTextField();
		jComboBox1 = new javax.swing.JComboBox<>();
		jCheckBox1 = new javax.swing.JCheckBox();
		jLabel5 = new javax.swing.JLabel();
		jCheckBox2 = new javax.swing.JCheckBox();
		jCheckBox3 = new javax.swing.JCheckBox();
		jCheckBox4 = new javax.swing.JCheckBox();
		jCheckBox5 = new javax.swing.JCheckBox();
		jCheckBox6 = new javax.swing.JCheckBox();

		setPreferredSize(new java.awt.Dimension(575, 463));

		jLabel1.setText("Painel de Configuração do VNC");
		jLabel2.setText("Servidor:");
		jLabel3.setText("Senha:");
		jLabel4.setText("Cores:");
		jButton3.setText("Salvar");

		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
		            salvarJButton3ActionPerformed(evt);
			}
		});

		jLabel6.setText("Porta:");
		jTextField4.setText("5900");
		jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "8-bit Indexed Color",
				"8-bit True Color", "16-bit True Color", "24-bit True Color" }));
		jCheckBox1.setText(" Usar Cursor Local");
		jLabel5.setText("Codificação:");
		jCheckBox2.setText("COPYRECT");
		jCheckBox3.setText("RRE");
		jCheckBox4.setText("HEXTILE");
		jCheckBox5.setText("ZLIB");
		jCheckBox6.setText("Ativar o VNC?");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton3))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(60, 60, 60)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox6)
                                    .addComponent(jLabel5)
                                    .addComponent(jCheckBox1)
                                    .addComponent(jCheckBox2)
                                    .addComponent(jCheckBox3)
                                    .addComponent(jCheckBox4)
                                    .addComponent(jCheckBox5))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel7))
                .addGap(9, 9, 9)
                .addComponent(jCheckBox6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jCheckBox1)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(12, 12, 12))
        );
	}

	private void salvarJButton3ActionPerformed(java.awt.event.ActionEvent evt) {

		try {
			Files.createDirectories(pathvncconf.getParent());
			if (!Files.exists(pathvncconf)) {
				Files.createFile(pathvncconf); // Cria o arquivo se não existir
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		vncDTO = new VNCDTO();
		vncDTO.setServidor(jTextField1.getText());
		vncDTO.setPorta(jTextField4.getText());
		vncDTO.setSenha(jTextField3.getText());
		vncDTO.setCor(jComboBox1.getItemAt(jComboBox1.getSelectedIndex()));
		vncDTO.setCursorLocal(jCheckBox1.isSelected());
		vncDTO.setCopyrect(jCheckBox2.isSelected());
		vncDTO.setRre(jCheckBox3.isSelected());
		vncDTO.setHextile(jCheckBox4.isSelected());
		vncDTO.setZlib(jCheckBox5.isSelected());
		vncDTO.setAtivar(jCheckBox6.isSelected());

		String arquivoconfiguracao = "servidor|porta|senha|cor|cursorlocal|copyrect|rre|hextile|zlib|ativar\n";

		arquivoconfiguracao += vncDTO.getServidor() + "|" + vncDTO.getPorta() + "|" + vncDTO.getSenha() + "|"
				+ vncDTO.getCor() + "|" + vncDTO.isCursorLocal() + "|" + vncDTO.isCopyrect() + "|" + vncDTO.isRre()
				+ "|" + vncDTO.isHextile() + "|" + vncDTO.isZlib() + "|" + vncDTO.isAtivar() + "\n";

		try {
			VNCConfigurationManager.writeConfiguracao(arquivoconfiguracao);
		} catch (Exception e) {
			e.printStackTrace();
		}

		jTextField1.setText("");
		jTextField4.setText("5900");
		jTextField3.setText("");
		jComboBox1.setSelectedIndex(0);
		jCheckBox1.setSelected(false);
		jCheckBox2.setSelected(false);
		jCheckBox3.setSelected(false);
		jCheckBox4.setSelected(false);
		jCheckBox5.setSelected(false);
		jCheckBox6.setSelected(false);

		this.menuInternalFrame.remove(this);

		this.menuInternalFrame.add(new EstadoConfigurationPanel()); // Adicionar EstadoConfiguracao
		this.menuInternalFrame.revalidate(); // Atualizar o container
		this.menuInternalFrame.repaint(); // Redesenhar o container

	}

	private javax.swing.JButton jButton3;
	private javax.swing.JCheckBox jCheckBox1;
	private javax.swing.JCheckBox jCheckBox2;
	private javax.swing.JCheckBox jCheckBox3;
	private javax.swing.JCheckBox jCheckBox4;
	private javax.swing.JCheckBox jCheckBox5;
	private javax.swing.JCheckBox jCheckBox6;
	private javax.swing.JComboBox<String> jComboBox1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JTextField jTextField1;
	private javax.swing.JTextField jTextField3;
	private javax.swing.JTextField jTextField4;

}
