package com.s4etech.ui.panel.config;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

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

    private static GravacaoDTO gravacaoDTO;
    private JInternalFrame menuInternalFrame;

    // Caminho padrão Linux (~/.config/s4etech/gravacao.cfg)
    private static final Path CONFIG_DIR = Paths.get(System.getProperty("user.home"), ".config", "s4etech");
    private static final Path CONFIG_FILE = CONFIG_DIR.resolve("gravacao.cfg");

    public GravacaoConfigurationPanel(JInternalFrame menuInternalFrame) {
        this.menuInternalFrame = menuInternalFrame;

        initComponents();

        try {
            gravacaoDTO = GravacaoConfigurationManager.get();
        } catch (Exception e) {
            logger.error("Erro ao ler o arquivo de configuração de gravação.", e);
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
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jComboBox1 = new JComboBox<>();

        jLabel1.setText("Painel de Configuração de Gravação");
        jLabel2.setText("Caminho:");
        jTextField1.setText("/home/" + System.getProperty("user.name") + "/Videos/s4etech"); // valor padrão Linux
        jTextField1.setEditable(false);

        jLabel4.setText("Qualidade da gravação:");
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"640x360", "1280x720", "1920x1080"}));

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
        jButton3.addActionListener(evt -> salvarConfiguracao());

        jCheckBox1.setText("Ativar a gravação?");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(57, 57, 57)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jCheckBox1)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING))
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
                    .addComponent(jButton3)
                    .addGap(12, 12, 12))
        );
    }

    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JComboBox<String> jComboBox1;

    private void salvarConfiguracao() {
        try {
            Files.createDirectories(CONFIG_DIR);
            if (!Files.exists(CONFIG_FILE)) {
                Files.createFile(CONFIG_FILE);
            }
        } catch (IOException e) {
            logger.error("Erro ao preparar o arquivo de configuração de gravação.", e);
            MensagemUtils.showError(menuInternalFrame,
                "Erro ao preparar o arquivo de configuração: " + e.getMessage(),
                "Erro");
            return;
        }

        String caminhoSelecionado = jTextField1.getText().trim();
        if (!isPathValido(caminhoSelecionado)) {
            MensagemUtils.showError(
                menuInternalFrame,
                "O caminho contém caracteres inválidos.\n" +
                "Use apenas letras, números, '/', '-', '_' e '.'.\n\n" +
                "Exemplo válido: /home/usuario/Videos/s4etech",
                "Caminho inválido"
            );
            return;
        }

        gravacaoDTO = new GravacaoDTO();
        gravacaoDTO.setCaminho(caminhoSelecionado);
        gravacaoDTO.setAtivar(jCheckBox1.isSelected());
        gravacaoDTO.setQualidade(jComboBox1.getItemAt(jComboBox1.getSelectedIndex()));

        String conteudo = "caminho|ativa|qualidade\n" +
                          gravacaoDTO.getCaminho() + "|" +
                          gravacaoDTO.isAtivar() + "|" +
                          gravacaoDTO.getQualidade() + "\n";

        try {
            GravacaoConfigurationManager.writeConfiguracao(conteudo);
            logger.info("Configuração salva com sucesso em {}", CONFIG_FILE);
        } catch (Exception e) {
            logger.error("Erro ao salvar configuração de gravação.", e);
            MensagemUtils.showError(menuInternalFrame,
                "Erro ao salvar a configuração: " + e.getMessage(),
                "Erro");
            return;
        }

        // 🔄 Atualiza o painel
        jCheckBox1.setSelected(false);
        this.menuInternalFrame.remove(this);
        this.menuInternalFrame.add(new EstadoConfigurationPanel());
        this.menuInternalFrame.revalidate();
        this.menuInternalFrame.repaint();
    }

    /**
     * Valida se o caminho contém apenas caracteres permitidos.
     */
    private boolean isPathValido(String caminho) {
        return caminho.matches("^[A-Za-z0-9_:/\\\\.\\-]+$");
    }
}
