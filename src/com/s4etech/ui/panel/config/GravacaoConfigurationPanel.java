package com.s4etech.ui.panel.config;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s4etech.config.manager.GravacaoConfigurationManager;
import com.s4etech.dto.GravacaoDTO;
import com.s4etech.util.MensagemUtils;

public class GravacaoConfigurationPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(GravacaoConfigurationPanel.class);
	
	private Path pathgravacaoconf;
	private static GravacaoDTO gravacaoDTO;
	private JInternalFrame menuInternalFrame;

	public GravacaoConfigurationPanel(JInternalFrame menuInternalFrame) {
	    this.menuInternalFrame = menuInternalFrame;
	    pathgravacaoconf = Paths.get(System.getProperty("user.dir"), "configuracao/gravacao.cfg");
	    initComponents();
	    try {
	        gravacaoDTO = GravacaoConfigurationManager.get();
	    } catch (Exception e) {
	        //JOptionPane.showMessageDialog(
	        //    this,
	        //    "Ocorreu um problema ao ler o arquivo de configuração de gravação.\n" +
	        //    "Por favor, verifique o arquivo e tente novamente.",
	        //    "Erro de Leitura",
	        //    JOptionPane.ERROR_MESSAGE
	        //);
	        e.printStackTrace();
	        return;
	    }
	    if (gravacaoDTO != null) {
	        jTextField1.setText(gravacaoDTO.getCaminho());
	        jCheckBox1.setSelected(gravacaoDTO.isAtivar());
	        jComboBox1.setSelectedItem(gravacaoDTO.getQualidade());
	    }
	}

	private void initComponents() {
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField();
		jButton3 = new javax.swing.JButton();
		jCheckBox1 = new javax.swing.JCheckBox();
		jLabel1.setText("Painel de Configuração de Gravação");
		jLabel2.setText("Caminho:");
		jTextField1.setText("C:/s4etech-viewer/gravacoes");
		jTextField1.setEditable(false); // Impede a edição direta do campo de texto
        jLabel4.setText("Qualidade da gravação:");
        jComboBox1 = new JComboBox<String>();
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"640x360", "1280x720", "1920x1080"}));
		
		jTextField1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);

				int option = fileChooser.showOpenDialog(jTextField1);

				if (option == JFileChooser.APPROVE_OPTION) {
					File selectedDir = fileChooser.getSelectedFile();
					jTextField1.setText(selectedDir.getAbsolutePath());
				}
			}
		});

		jButton3.setText("Salvar");
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
		            salvarJButton3ActionPerformed(evt);
			}
		});

		jCheckBox1.setText("Ativar a gravação?");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
		    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		    .addGroup(layout.createSequentialGroup()
		        .addGap(12, 12, 12)
		        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		            .addComponent(jLabel1)
		            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
		                .addComponent(jButton3)  // Botão "Salvar" alinhado
		                .addGroup(layout.createSequentialGroup()
		                    .addComponent(jLabel2)
		                    .addGap(57, 57, 57)
		                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                        .addComponent(jCheckBox1)
		                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
		                        .addGroup(layout.createSequentialGroup()
		                            .addComponent(jLabel4)
		                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
		        .addContainerGap(13, Short.MAX_VALUE))
		);
		layout.setVerticalGroup(
		    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		    .addGroup(layout.createSequentialGroup()
		        .addGap(12, 12, 12)
		        .addComponent(jLabel1)
		        .addGap(12, 12, 12)
		        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		            .addComponent(jLabel2)
		            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
		        .addGap(10, 10, 10)
		        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		            .addComponent(jLabel4)
		            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
		        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
		        .addComponent(jCheckBox1)
		        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 352, Short.MAX_VALUE)
		        .addComponent(jButton3)  // Botão "Salvar" alinhado
		        .addGap(12, 12, 12))
		);
	}

	private javax.swing.JButton jButton3;
	private javax.swing.JCheckBox jCheckBox1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JTextField jTextField1;
    private javax.swing.JComboBox<String> jComboBox1;

    private void salvarJButton3ActionPerformed(java.awt.event.ActionEvent evt) {

        try {
            Files.createDirectories(pathgravacaoconf.getParent());
            if (!Files.exists(pathgravacaoconf)) {
                Files.createFile(pathgravacaoconf);
            }
        } catch (IOException e) {
            logger.error("Erro ao preparar o arquivo de configuração de gravação.", e);
            MensagemUtils.showError(menuInternalFrame,
                "Erro ao preparar o arquivo de configuração: " + e.getMessage(),
                "Erro");
            return;
        }

        String caminhoSelecionado = jTextField1.getText().trim();

        // 🔍 Validação do caminho
        if (!isPathValido(caminhoSelecionado)) {
            MensagemUtils.showError(
                menuInternalFrame,
                "O caminho contém caracteres inválidos (acentos, cedilha, espaços ou símbolos especiais).\n" +
                "Por favor, escolha um diretório com apenas letras, números, '_' ou '-'.\n\n" +
                "Exemplo válido: C:/s4etech/gravacoes",
                "Caminho inválido"
            );
            return;
        }

        gravacaoDTO = new GravacaoDTO();
        gravacaoDTO.setCaminho(caminhoSelecionado);
        gravacaoDTO.setAtivar(jCheckBox1.isSelected());
        gravacaoDTO.setQualidade(jComboBox1.getItemAt(jComboBox1.getSelectedIndex()));

        String arquivoconfiguracao = "caminho|ativa|qualidade\n";
        arquivoconfiguracao += gravacaoDTO.getCaminho() + "|" +
                               gravacaoDTO.isAtivar() + "|" +
                               gravacaoDTO.getQualidade() + "\n";

        try {
            GravacaoConfigurationManager.writeConfiguracao(arquivoconfiguracao);
            // Mensagem de sucesso removida conforme solicitado
        } catch (Exception e) {
            logger.error("Erro ao salvar configuração de gravação.", e);
            MensagemUtils.showError(menuInternalFrame,
                "Erro ao salvar a configuração: " + e.getMessage(),
                "Erro");
            return;
        }

        // 🔄 Reseta o painel
        jTextField1.setText("");
        jCheckBox1.setSelected(false);

        this.menuInternalFrame.remove(this);
        this.menuInternalFrame.add(new EstadoConfigurationPanel());
        this.menuInternalFrame.revalidate();
        this.menuInternalFrame.repaint();
    }


    /**
     * Valida se o caminho contém apenas caracteres permitidos.
     * Permitidos: letras, números, barra, traço, underscore, ponto e dois-pontos.
     */
    private boolean isPathValido(String caminho) {
        // Bloqueia acentos, espaços e caracteres especiais
        return caminho.matches("^[A-Za-z0-9_:/\\\\.\\-]+$");
    }

}
