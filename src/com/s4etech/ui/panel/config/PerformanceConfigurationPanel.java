package com.s4etech.ui.panel.config;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JInternalFrame;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.s4etech.config.manager.PerformanceConfigurationManager;
import com.s4etech.dto.PerformanceDTO;
import com.s4etech.util.MessageUtils;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

/**
 *
 * @author SilvioCicoti
 */
public class PerformanceConfigurationPanel extends javax.swing.JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1040168009579125777L;
	
	private static PerformanceDTO performanceDTO;
    private Path pathbufferconf;
    private JInternalFrame menuInternalFrame;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.ButtonGroup buttonGroupBuffer;
    private javax.swing.ButtonGroup buttonGroupProtocolo;
    private javax.swing.ButtonGroup buttonGroupAceleracao;
    // End of variables declaration                   
    public PerformanceConfigurationPanel(JInternalFrame menuInternalFrame) {
        this.menuInternalFrame = menuInternalFrame;
        pathbufferconf = Paths.get(System.getProperty("user.dir"), "configuracao/performance.cfg");
        initComponents();

        performanceDTO = PerformanceConfigurationManager.get();
        
        if (performanceDTO != null) {
        	
        	try {
        		
        	
	            if (performanceDTO.getConfiguracao().equals("velocidade")) {
	                jRadioButton1.setSelected(true);
	                jRadioButton2.setSelected(false);
	                jRadioButton3.setSelected(false);
	                setPresetValuesForSpeed();
	                setFieldsEditable(false);
	            } else if (performanceDTO.getConfiguracao().equals("qualidade")) {
	                jRadioButton1.setSelected(false);
	                jRadioButton2.setSelected(true);
	                jRadioButton3.setSelected(false);
	                setPresetValuesForQuality();
	                setFieldsEditable(false);
	            } else if (performanceDTO.getConfiguracao().equals("customizado")) {
	                jRadioButton1.setSelected(false);
	                jRadioButton2.setSelected(false);
	                jRadioButton3.setSelected(true);
	                setPresetValuesForCustomized(performanceDTO);
	                setFieldsEditable(true);
	            }
	            
	            if(performanceDTO.getProtocolo().equals("tcp")) {
	            	jRadioButton6.setSelected(true);
	            	jRadioButton7.setSelected(false);
	            }else {
	            	jRadioButton6.setSelected(false);
	            	jRadioButton7.setSelected(true);
	            }
	            	
		        if(performanceDTO.getAceleracao().equals("hardware")) {
		          	jRadioButton4.setSelected(true);
		           	jRadioButton5.setSelected(false);
		        }else {
		           	jRadioButton4.setSelected(false);
		           	jRadioButton5.setSelected(true);
		        }
		        
        	} catch (NullPointerException npe) {
        		
        		performanceDTO = new PerformanceDTO();
        		performanceDTO.setBufferSize(0);
        		performanceDTO.setLatency(0);
        		performanceDTO.setTimeout(500);
        		
        		jRadioButton1.setSelected(false);
                jRadioButton2.setSelected(false);
                jRadioButton3.setSelected(true);
                setPresetValuesForCustomized(performanceDTO);
                setFieldsEditable(true);
                
                jRadioButton6.setSelected(true);
            	jRadioButton7.setSelected(false);
        		
        		jRadioButton4.setSelected(true);
	           	jRadioButton5.setSelected(false);
        		
        	}
            
        } else {
        	
        	
        	performanceDTO = new PerformanceDTO();
    		performanceDTO.setBufferSize(0);
    		performanceDTO.setLatency(0);
    		performanceDTO.setTimeout(500);
    		
    		jRadioButton1.setSelected(false);
            jRadioButton2.setSelected(false);
            jRadioButton3.setSelected(true);
            setPresetValuesForCustomized(performanceDTO);
            setFieldsEditable(true);
            
            jRadioButton6.setSelected(true);
        	jRadioButton7.setSelected(false);
    		
    		jRadioButton4.setSelected(true);
           	jRadioButton5.setSelected(false);
        	
        }

        // Listeners for the radio buttons
        jRadioButton1.addActionListener(evt -> {
            setPresetValuesForSpeed();
            setFieldsEditable(false); // Desabilita campos para "Velocidade"
        });
        jRadioButton2.addActionListener(evt -> {
            setPresetValuesForQuality();
            setFieldsEditable(false); // Desabilita campos para "Qualidade"
        });
        jRadioButton3.addActionListener(evt -> {
            setPresetValuesForCustomized(performanceDTO);
            setFieldsEditable(true); // Habilita campos para "Customizado"
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		jButton3 = new javax.swing.JButton();
		jLabel3 = new javax.swing.JLabel();
		jRadioButton1 = new javax.swing.JRadioButton();
		jRadioButton2 = new javax.swing.JRadioButton();
		
		jRadioButton4 = new javax.swing.JRadioButton();
		jRadioButton5 = new javax.swing.JRadioButton();
		
		jScrollPane2 = new javax.swing.JScrollPane();
		jTextPane1 = new javax.swing.JTextPane();
		jLabel2 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField();
		jLabel4 = new javax.swing.JLabel();
		jTextField2 = new javax.swing.JTextField();
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();

		jRadioButton3 = new javax.swing.JRadioButton();
		jLabel8 = new javax.swing.JLabel();
		jRadioButton6 = new javax.swing.JRadioButton();
		jRadioButton7 = new javax.swing.JRadioButton();
		buttonGroupBuffer = new javax.swing.ButtonGroup();
		buttonGroupProtocolo = new javax.swing.ButtonGroup();
		buttonGroupAceleracao = new javax.swing.ButtonGroup();
		jTextField3 = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(575, 391));

        jLabel1.setText("Painel de Configuração de Performance");

        jButton3.setText("Salvar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salvarJButton3ActionPerformed(evt);
            }
        });

        jRadioButton1.setText("Velocidade de Processamento");
        jRadioButton2.setText("Qualidade de Processamento");
        jRadioButton3.setText("Customizado");
        
    
        jRadioButton4.setText("Hardware");
        jRadioButton5.setText("Software");
        
        
        buttonGroupBuffer.add(jRadioButton1);
        buttonGroupBuffer.add(jRadioButton2);
        buttonGroupBuffer.add(jRadioButton3);
        jScrollPane2.setViewportView(jTextPane1);
        jLabel2.setText("Buffer:");
        jLabel4.setText("Latencia:");
        jLabel5.setText("Tempo de Retenção do Último Frame:");
        jLabel6.setText("Aceleração Gráfica:");
        jLabel8.setText("Protocolo:");
        jRadioButton6.setText("Reliable");
        jRadioButton7.setText("Fast");
        
        jRadioButton6.addActionListener(evt -> atualizarPresetAtual());
        jRadioButton7.addActionListener(evt -> atualizarPresetAtual());
        
        buttonGroupProtocolo.add(jRadioButton6);
        buttonGroupProtocolo.add(jRadioButton7);
        
        buttonGroupAceleracao.add(jRadioButton4);
        buttonGroupAceleracao.add(jRadioButton5);
       
       
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(93, 93, 93)
                                    .addComponent(jLabel8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jRadioButton6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jRadioButton7)
                                    .addGap(271, 271, 271))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jButton3)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(315, 315, 315)
                                .addComponent(jRadioButton5))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addGap(18, 18, 18)
                                    .addComponent(jRadioButton4))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(96, 96, 96)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jRadioButton3)
                            .addComponent(jRadioButton1)
                            .addComponent(jRadioButton2))))
                .addGap(13, 13, 13))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jRadioButton6)
                    .addComponent(jRadioButton7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton3)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jRadioButton4)
                    .addComponent(jRadioButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(12, 12, 12))
        );
        
    }// </editor-f    
        
    private void atualizarPresetAtual() {
        if (jRadioButton1.isSelected()) {
            this.setPresetValuesForSpeed();
        } else if (jRadioButton2.isSelected()) {
            this.setPresetValuesForQuality();
        }
    }    
    
    private void salvarJButton3ActionPerformed(java.awt.event.ActionEvent evt) {

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        try {
            Files.createDirectories(pathbufferconf.getParent());
            if (!Files.exists(pathbufferconf)) {
                Files.createFile(pathbufferconf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Converte o texto dos JTextFields para inteiros antes de definir no PerformanceDTO
            performanceDTO = new PerformanceDTO();
            performanceDTO.setBufferSize(Integer.parseInt(jTextField1.getText()));  // Converte o valor de buffer para int
            performanceDTO.setLatency(Integer.parseInt(jTextField2.getText()));  // Converte o valor de latência para int
            performanceDTO.setTimeout(Integer.parseInt(jTextField3.getText()));  // Converte o valor de latência para int
        } catch (NumberFormatException e) {
            // Caso o valor nos campos não seja um número válido, exibe uma mensagem de erro
            MessageUtils.updateDescriptionLabel(jLabel3, "Por favor, insira valores numéricos válidos para buffer, latência e timeout.", Color.RED);
            return;
        }

        if (jRadioButton1.isSelected()) {
            performanceDTO.setConfiguracao("velocidade"); 
        } else if (jRadioButton2.isSelected()) {
            performanceDTO.setConfiguracao("qualidade"); 
        } else if (jRadioButton3.isSelected()) {
            performanceDTO.setConfiguracao("customizado"); 
        } else {
            MessageUtils.updateDescriptionLabel(jLabel3, "Por favor, selecione uma configuração de desempenho.", Color.RED);
            return;
        }
        
        if(jRadioButton6.isSelected()){
        	performanceDTO.setProtocolo("tcp");
        }else {
        	performanceDTO.setProtocolo("udp");
        }
        
        if(jRadioButton4.isSelected()){
        	performanceDTO.setAceleracao("hardware");
        }else {
        	performanceDTO.setAceleracao("software");
        }

        String arquivoconfiguracao = "configuracao|buffer|latency|protocolo|timeout|aceleracao\n";
        arquivoconfiguracao += performanceDTO.getConfiguracao() + "|" + performanceDTO.getBufferSize() + "|" + performanceDTO.getLatency() + "|" + performanceDTO.getProtocolo() + "|" + performanceDTO.getTimeout() + "|" + performanceDTO.getAceleracao()  + "\n";

        try {
            PerformanceConfigurationManager.writeConfiguracao(arquivoconfiguracao);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.menuInternalFrame.remove(this);

        this.menuInternalFrame.add(new EstadoConfigurationPanel());
        this.menuInternalFrame.revalidate();
        this.menuInternalFrame.repaint();
    }
    // Método para habilitar ou desabilitar os campos de texto
    private void setFieldsEditable(boolean editable) {
        jTextField1.setEditable(editable);
        jTextField2.setEditable(editable);
    }
    
    // Método para preencher os campos com valores customizados
    private void setPresetValuesForCustomized(PerformanceDTO dto) {
        setPresetValueForTimeout();
        int buffer = dto.getBufferSize();
        int latency = dto.getLatency();

        if (buffer < latency * 1.5) {
            buffer = (int)(latency * 1.5);
        }

        jTextField1.setText(String.valueOf(buffer));
        jTextField2.setText(String.valueOf(latency));
    }
    
    // Método para preencher os campos com valores padrão de Qualidade
    private void setPresetValuesForQuality() {
        setPresetValueForTimeout();
        if (jRadioButton6.isSelected()) { // TCP
            jTextField1.setText("300");
            jTextField2.setText("200");
        } else { // UDP
            jTextField1.setText("450");
            jTextField2.setText("300");
        }
    }
    // Método para preencher os campos com valores padrão de Velocidade
    private void setPresetValuesForSpeed() {
        setPresetValueForTimeout();
        if (jRadioButton6.isSelected()) { // TCP
            jTextField1.setText("100");
            jTextField2.setText("50");
        } else { // UDP
            jTextField1.setText("150");
            jTextField2.setText("100");
        }
    }
    
    private void setPresetValueForTimeout() {
    	 if (performanceDTO != null) {
             jTextField3.setText(String.valueOf(performanceDTO.getTimeout()));  // Timeout (ms) 
         } else {
             // Se performanceDTO for nulo, usar os valores padrão de "0"
             jTextField3.setText("500");  // Timeout (ms)
         }
    }
}
