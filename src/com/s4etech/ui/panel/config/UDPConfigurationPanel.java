package com.s4etech.ui.panel.config;

import java.awt.Color;
import java.net.InetAddress;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JInternalFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s4etech.config.manager.UDPConfigurationManager;
import com.s4etech.dto.UDPDTO;
import com.s4etech.ui.screens.Viewer;
import com.s4etech.util.UdpConfigurationService;

public class UDPConfigurationPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(UDPConfigurationPanel.class);
    private Viewer viewer;
    private Path pathUdpConf;
    private static UDPDTO udpDTO;
    private JInternalFrame menuInternalFrame;

    public UDPConfigurationPanel(JInternalFrame menuInternalFrame, Viewer viewer) {
        this.menuInternalFrame = menuInternalFrame;
        this.viewer = viewer;
        pathUdpConf = Paths.get(System.getProperty("user.dir"), "configuracao/udp.cfg");
        initComponents();
        carregarConfiguracao();
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jCheckBox6 = new javax.swing.JCheckBox();
        jTextField1 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(575, 463));

        jLabel1.setText("Configuração UDP");
        jLabel2.setText("IP remoto:");
        jLabel3.setText("IP local:");
        jLabel6.setText("Porta:");
        jLabel8.setText("Porta:");
        jLabel7.setText("");
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jButton3.setText("Salvar");
        jButton3.addActionListener(evt -> salvarConfiguracao());

        jCheckBox6.setText("Ativar UDP");

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
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBox6)
                                .addGap(0, 365, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField3)
                                    .addComponent(jTextField1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addGap(14, 14, 14))
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
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 303, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(12, 12, 12))
        );
    }

    private void carregarConfiguracao() {
        udpDTO = UDPConfigurationManager.get();

        // Valores padrão
        String ipDestino = getLocalIp();
        String portaDestino = "9878";
        String ipLocal = getLocalIp(); 
        String portaLocal = "9876";
        boolean ativar = false;

        // Se houver configuração válida, sobrepõe os padrões
        if (udpDTO != null) {
            if (udpDTO.getIpDestino() != null) ipDestino = udpDTO.getIpDestino();
            if (udpDTO.getPortaDestino() != null) portaDestino = udpDTO.getPortaDestino();
            if (udpDTO.getIpLocal() != null) ipLocal = udpDTO.getIpLocal();
            if (udpDTO.getPortaLocal() != null) portaLocal = udpDTO.getPortaLocal();
            ativar = udpDTO.isAtivar();
        }

        jTextField1.setText(ipDestino);
        jTextField4.setText(portaDestino);
        jTextField3.setText(ipLocal);
        jTextField5.setText(portaLocal);
        jCheckBox6.setSelected(ativar);
    }

    private String getLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            logger.warn("Não foi possível obter o IP local: {}", e.getMessage());
            return "127.0.0.1";
        }
    }

    private void salvarConfiguracao() {
        UDPDTO dto = new UDPDTO();
        dto.setIpDestino(jTextField1.getText().trim());
        dto.setPortaDestino(jTextField4.getText().trim());
        dto.setIpLocal(jTextField3.getText().trim());
        dto.setPortaLocal(jTextField5.getText().trim());
        dto.setAtivar(jCheckBox6.isSelected());

        UdpConfigurationService service = new UdpConfigurationService();

        boolean sucesso = service.salvarConfiguracao(dto, pathUdpConf, jLabel7);

        if (sucesso) {
        	
            viewer.reloadUDPConfig();

            menuInternalFrame.remove(this);
            menuInternalFrame.add(new EstadoConfigurationPanel());
            menuInternalFrame.revalidate();
            menuInternalFrame.repaint();
            
        } 
    }
 

    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
}
