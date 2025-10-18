package com.s4etech;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.s4e.SystemInfo;
import com.s4etech.config.manager.AutorizacaoConfigurationManager;
import com.s4etech.dto.AutorizacaoDTO;
import com.s4etech.dto.LicencaDTO;
import com.s4etech.ui.screens.Login;
import com.s4etech.ui.screens.Viewer;
import com.s4etech.ui.screens.ViewerLoggerInjector;

public class Main {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	private static final boolean isAuthenticate = true;
	
    private static FileLock lock;
    private static FileChannel channel;
    private static final String LOCK_FILE = "app.lock";  // Nome do arquivo de bloqueio
    private static List<LicencaDTO> listaLicenca = new ArrayList<>();
    private static final String LICENCA_FILE = "licenca/licenca.cfg";
    private static final List<String> VALORES_INVALIDOS = Arrays.asList(
    	    "", "unknown", "none", "null", "system serial number", 
    	    "to be filled by o.e.m.", "default string", "0000000000000000"
    	);
    
    public static void main(String[] args) {
        // Configure a LookAndFeel antes de inicializar qualquer componente
        FlatMacDarkLaf.setup();
        //FlatRobotoFont.install();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));

        // Verifica se já existe uma instância rodando
        if (!isSingleInstance()) {
            // Cria um JFrame temporário para garantir que o JOptionPane siga o LookAndFeel
            JFrame tempFrame = new JFrame();
            SwingUtilities.updateComponentTreeUI(tempFrame);

            JOptionPane.showMessageDialog(tempFrame, "O programa já está em execução!", "Aviso", JOptionPane.WARNING_MESSAGE);
            tempFrame.dispose(); // Fecha o JFrame temporário

            return;  // Encerra o programa se já houver uma instância rodando
        }

        // Comando para desativar a interface Wi-Fi
        try {
            String command = "netsh interface set interface Wi-Fi admin=disable";
            Process process = Runtime.getRuntime().exec(command);
            printStream(process.getInputStream(), "OUTPUT");
            printStream(process.getErrorStream(), "ERROR");
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            logger.error("Falha ao executar o comando ou interrompido.", e);
        }

        // Inicializa a interface do usuário
        java.awt.EventQueue.invokeLater(() -> {
            iniciarInterfaceUsuario();
        });
    }
    
    private static void printStream(InputStream stream, String type) {
        new Thread(() -> {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
                String line;
                while ((line = br.readLine()) != null) {
                	logger.info("{}> {}", type, line);
                }
            } catch (IOException e) {
            	logger.error("Error reading stream.", e);
            }
        }).start();
    }

    private static boolean isSerialValido(String serial) {
        if (serial == null) return false;

        String s = serial.trim().toLowerCase();

        if (VALORES_INVALIDOS.contains(s)) return false;

        // Só letras? Rejeita
        if (s.matches("^[a-zA-Z]+$")) return false;

        // Aceita se for só números com pelo menos 5 dígitos
        if (s.matches("^\\d{5,}$")) return true;

        // Aceita se for alfanumérico com pelo menos 5 caracteres (letras e/ou números)
        return s.matches("^[a-zA-Z0-9\\-]{5,}$");
    }
    
    public static void iniciarInterfaceUsuario() {
       
    	String licenca = null;
        String numeroSerie = null;
        boolean isAutorizado = false;
        carregarArquivoLicenca();
            	
        numeroSerie = null;

	     // Tenta BIOS
	     try {
	         numeroSerie = SystemInfo.getBiosSerialNumber();
	         if (isSerialValido(numeroSerie)) {
	             logger.info("Número de série do BIOS recuperado com sucesso: {}", numeroSerie);
	         } else {
	             logger.warn("Número de série do BIOS inválido: {}. Tentando placa-mãe...", numeroSerie);
	             numeroSerie = null;
	         }
	     } catch (Exception e) {
	         logger.warn("Erro ao recuperar número de série do BIOS. Tentando placa-mãe...", e);
	     }
	
	     // Tenta placa-mãe se BIOS falhou ou inválido
	     if (numeroSerie == null) {
	         try {
	             numeroSerie = SystemInfo.getMotherboardSerialNumber();
	             if (isSerialValido(numeroSerie)) {
	                 logger.info("Número de série da placa-mãe recuperado com sucesso: {}", numeroSerie);
	             } else {
	                 logger.warn("Número de série da placa-mãe inválido: {}. Tentando UUID...", numeroSerie);
	                 numeroSerie = null;
	             }
	         } catch (Exception e) {
	             logger.warn("Erro ao recuperar número de série da placa-mãe. Tentando UUID...", e);
	             numeroSerie = null;
	         }
	     }
	     
	     // Tenta UUID se os anteriores falharam
	     if (numeroSerie == null) {
	         try {
	             numeroSerie = SystemInfo.getUUIDSerialNumber();
	             if (isSerialValido(numeroSerie)) {
	                 logger.info("Número de série UUID recuperado com sucesso: {}", numeroSerie);
	             } else {
	                 logger.error("Número de série UUID também inválido: {}", numeroSerie);
	                 numeroSerie = null;
	             }
	         } catch (Exception e) {
	             logger.error("Erro ao recuperar número de série UUID.", e);
	             numeroSerie = null;
	         }
	     }
	     
        try {
            licenca = SystemInfo.shortKey(numeroSerie);
        } catch (Exception e) {
            logger.error("Falha ao recuperar a chave curta da licença.", e);
        }
   
        for (LicencaDTO licencaDTO : listaLicenca) {
            if (numeroSerie != null && numeroSerie.equals(licencaDTO.getNumeroSerie()) && licenca != null
                    && licenca.equals(licencaDTO.getCodigoLicenca())) {
                isAutorizado = true;
                logger.info("Licença autorizada para número de série: {}", numeroSerie);
                break;
            }
        }
    	
        if (isAuthenticate && isAutorizado) {
            logger.info("Iniciando diretamente o Viewer - modo autorizado).");
            //ViewerLoggerInjector.instrument(); // para logar os metodos na entrada e saída. // remover ao fazer o pacote.
            AutorizacaoDTO autorizacaoDTO = AutorizacaoConfigurationManager.get();
            Viewer gui = null;
            if(autorizacaoDTO!=null) {
            	gui = new Viewer(autorizacaoDTO);
            } else {
    			autorizacaoDTO = new AutorizacaoDTO();
    			autorizacaoDTO.setUsuario("s4eviewer");
    			autorizacaoDTO.setAutorizacao("S4etech@2025");
            	gui = new Viewer(autorizacaoDTO);
            }
             
            gui.inicializa();
            return;
        }

        // Só cria o JFrame se realmente for necessário
        ImageIcon logoIcon = new ImageIcon(Viewer.class.getResource("/icons/icone_s4etech.png"));
        JFrame frame = new JFrame("S4E Tech - Viewer");
        frame.setIconImage(logoIcon.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new Login(frame, isAuthenticate)); // login tradicional
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    private static void carregarArquivoLicenca() {
        Path path = Paths.get(LICENCA_FILE);
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("mac")) {
                    String[] campos = line.split("\\|");
                    LicencaDTO licencaDTO = new LicencaDTO(campos[0], campos[1]);
                    listaLicenca.add(licencaDTO);
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao carregar arquivo de licença.", e);
        }
    }

    /**
     * Verifica se apenas uma instância está rodando, usando um arquivo de bloqueio.
     */
    private static boolean isSingleInstance() {
        try {
            File file = new File(System.getProperty("java.io.tmpdir"), LOCK_FILE);  // Cria o arquivo de bloqueio no diretório temporário
            channel = new RandomAccessFile(file, "rw").getChannel();

            // Tenta bloquear o arquivo
            lock = channel.tryLock();
            if (lock == null) {
                channel.close();
                return false;  // O arquivo já está bloqueado por outra instância
            }

            // Adiciona um hook para liberar o bloqueio e deletar o arquivo ao sair
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    lock.release();
                    channel.close();
                    file.delete();
                } catch (IOException e) {
                	logger.error("Falha ao liberar o bloqueio.", e);
                }
            }));
            return true;
        } catch (IOException e) {
        	logger.error("Erro ao verificar instância única.", e);
            return false;
        }
    }
}
