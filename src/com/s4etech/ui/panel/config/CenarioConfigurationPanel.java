package com.s4etech.ui.panel.config;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.s4etech.config.manager.CameraConfigurationManager;
import com.s4etech.dto.CameraDTO;
import com.s4etech.dto.CenarioDTO;
import com.s4etech.util.MessageUtils;

/**
 * Exemplo: Ao remover da lista2, o item volta para lista1 em ordem alfabética.
 */
public class CenarioConfigurationPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = -6297559721882407513L;
    private static final Logger logger = LoggerFactory.getLogger(CenarioConfigurationPanel.class);

    // >>> Removidos os paths fixos (blade/around/ripper)
    private final String usuario;
    private final Path dirCenarios;

    private Map<String, CameraDTO> mapaCameras;
    private Map<String, CenarioDTO> cenarios;

    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    
    // Aceita somente a–z e 0–9, com 3 a 12 caracteres
    private static final Pattern NOME_CENARIO_PATTERN = Pattern.compile("^[a-z0-9]{3,12}$");
    
    private static final Pattern DIGITS_AT_END = Pattern.compile("(\\d+)$");

    private static int numericSuffix(String s) {
        Matcher m = DIGITS_AT_END.matcher(s);
        return m.find() ? Integer.parseInt(m.group(1)) : Integer.MAX_VALUE; // se não tiver número no fim
    }

    private static final Comparator<String> CENARIO_COMPARATOR =
        Comparator.<String, String>comparing(s -> s.replaceAll("\\d+$", "").toLowerCase(Locale.ROOT))
                  .thenComparingInt(CenarioConfigurationPanel::numericSuffix)
                  .thenComparing(String.CASE_INSENSITIVE_ORDER);

    public CenarioConfigurationPanel(String usuario, JInternalFrame menuInternalFrame) {
        this.usuario = usuario;
        this.dirCenarios = Paths.get(System.getProperty("user.dir"), "configuracao", "cenarios");

        // Inicializa o Map de cenarios (agora dinâmico)
        cenarios = new TreeMap<>(CENARIO_COMPARATOR);

        // Constrói a UI
        initComponents();

        // Carrega todos os cenários dinâmicos (JSONs do usuário)
        carregarCenariosDinamicos();

        // Atualiza combo e seleciona o primeiro
        atualizarCombo();
        if (!cenarios.isEmpty()) {
            jComboBox1.setSelectedIndex(0);
        }
    }

 // Pede o nome ao usuário e só retorna quando válido (ou null se cancelar)
    private String solicitarNomeCenario(java.awt.Component parent, String mensagem, String valorInicial) {
        while (true) {
            String input = (String) JOptionPane.showInputDialog(
                    parent, mensagem, "Nome do cenário",
                    JOptionPane.PLAIN_MESSAGE, null, null, valorInicial
            );
            if (input == null) { // cancelado
                return null;
            }
            String nome = input.trim().toLowerCase();
            if (NOME_CENARIO_PATTERN.matcher(nome).matches()) {
                return nome;
            }
            JOptionPane.showMessageDialog(
                    parent,
                    "Nome inválido.\nUse somente letras minúsculas (a–z) e números (0–9), de 3 a 12 caracteres.",
                    "Validação de nome",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }
    
    private Path buildConfigPath(String nomeCenario) {
        // Mantém padrão usuario_nome.json, mas normaliza tudo em minúsculo
        String nomeArquivo = (usuario + "_" + nomeCenario).toLowerCase() + ".json";
        return dirCenarios.resolve(nomeArquivo);
    }

    /**
     * Lê o JSON do caminho especificado e atualiza o CenarioDTO em 'cenarios' se o arquivo existir.
     */
    private void loadCenarioFromFile(Path caminhoArquivo, String nomeCenario) {
        if (Files.exists(caminhoArquivo)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                CenarioDTO dto = objectMapper.readValue(caminhoArquivo.toFile(), CenarioDTO.class);
                // Garante que o nome do cenário seja o esperado (por arquivo)
                dto.setNomeCenario(nomeCenario);
                cenarios.put(nomeCenario, dto);

                logger.info("Cenário carregado do arquivo: " + caminhoArquivo.toAbsolutePath());
            } catch (IOException e) {
                logger.error("Erro ao ler cenário " + nomeCenario + " do arquivo: " + caminhoArquivo, e);
            }
        } else {
            logger.info("Arquivo não encontrado para cenário " + nomeCenario + ": " + caminhoArquivo);
        }
    }

    /**
     * Carrega dinamicamente todos os cenários do diretório configurado (apenas do usuário atual).
     * Caso não exista nenhum, cria um cenário "default".
     */
    private void carregarCenariosDinamicos() {
        try {
            Files.createDirectories(dirCenarios);
            final String prefix = usuario + "_";

            try (java.util.stream.Stream<Path> stream = Files.list(dirCenarios)) {
                stream.filter(f -> f.toString().endsWith(".json"))
                      .filter(f -> f.getFileName().toString().startsWith(prefix))
                      .forEach(f -> {
                          String fileName = f.getFileName().toString();
                          String nome = fileName.substring(prefix.length(), fileName.length() - ".json".length());
                          loadCenarioFromFile(f, nome);
                      });
            }
        } catch (IOException e) {
            logger.error("Erro ao listar cenários em: " + dirCenarios, e);
        }

    }

    private Thread createAnimationThread(String mensagem, Color corDaMensagem) {
        return new Thread(() -> {
            int count = 0;
            while (!Thread.currentThread().isInterrupted() && count < 3) {
                SwingUtilities.invokeLater(() -> {
                    MessageUtils.updateDescriptionLabel(jLabel3, mensagem, corDaMensagem);
                });
                count++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            SwingUtilities.invokeLater(() -> jLabel3.setText(""));
        });
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        jButton4.setText("Gerenciar");

        setPreferredSize(new Dimension(575, 463));

        jLabel1.setText("Painel de Configuração de Cenários");
        jButton3.setText("Salvar");
        jLabel2.setText("Cenário:");

        // >>> Combo dinâmico: inicia vazio; será preenchido por atualizarCombo()
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>());
        // Obtém a lista de câmeras
        List<CameraDTO> cameras = CameraConfigurationManager.get();
        cameras.sort(Comparator.comparing(CameraDTO::getCodigo, Comparator.nullsLast(String::compareTo)));
        mapaCameras = mapearCamerasPorCodigo(cameras);

        // Lista 1 (câmeras disponíveis) - modelo inicial com TODAS as câmeras
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (CameraDTO cam : cameras) {
            if (cam.getCodigo() != null) {
                listModel.addElement(cam.getCodigo());
            }
        }
        jList1 = new javax.swing.JList<>(listModel);
        jScrollPane1.setViewportView(jList1);

        // Lista 2 (vazia inicialmente)
        DefaultListModel<String> secondListModel = new DefaultListModel<>();
        jList2.setModel(secondListModel);
        jScrollPane2.setViewportView(jList2);

        // Botão "Adicionar -->"
        jButton1.setText("Adicionar -->");
        jButton1.addActionListener(e -> {
            String selected = jList1.getSelectedValue();
            String cenarioSelecionado = (String) jComboBox1.getSelectedItem();

            if (selected != null && cenarioSelecionado != null) {
                ((DefaultListModel<String>) jList1.getModel()).removeElement(selected);
                ((DefaultListModel<String>) jList2.getModel()).addElement(selected);

                cenarios.get(cenarioSelecionado).adicionarCamera(selected);
            }
        });

        // Botão "<-- Remover"
        jButton2.setText("<-- Remover");
        jButton2.addActionListener(e -> {
            String selected = jList2.getSelectedValue();
            String cenarioSelecionado = (String) jComboBox1.getSelectedItem();

            if (selected != null && cenarioSelecionado != null) {
                ((DefaultListModel<String>) jList2.getModel()).removeElement(selected);

                // Reordena a lista1
                DefaultListModel<String> model1 = (DefaultListModel<String>) jList1.getModel();
                List<String> items = new ArrayList<>();
                for (int i = 0; i < model1.size(); i++) {
                    items.add(model1.getElementAt(i));
                }
                items.add(selected);
                items.sort(String::compareToIgnoreCase);

                model1.clear();
                for (String item : items) {
                    model1.addElement(item);
                }

                cenarios.get(cenarioSelecionado).removerCamera(selected);
            }
        });

        // Evento do combo: carregar câmeras do cenário
        jComboBox1.addActionListener(e -> {
            String cenarioSelecionado = (String) jComboBox1.getSelectedItem();
            if (cenarioSelecionado != null) {
                // Recarrega a jList1 com todas as câmeras disponíveis
                List<String> listaOrdenada = new ArrayList<>(mapaCameras.keySet());

                // Remove da lista as câmeras que já estão no cenário selecionado
                // >>> não converter para lowerCase (nomes dinâmicos)
                List<String> camerasNoCenario = cenarios.get(cenarioSelecionado).getCodigoCameras();
                camerasNoCenario.forEach(listaOrdenada::remove);

                // Ordena alfabeticamente
                listaOrdenada.sort(String::compareToIgnoreCase);

                // Atualiza jList1
                DefaultListModel<String> model1 = new DefaultListModel<>();
                for (String codigo : listaOrdenada) {
                    model1.addElement(codigo);
                }
                jList1.setModel(model1);

                // Atualiza jList2
                DefaultListModel<String> model2 = new DefaultListModel<>();
                for (String codigo : camerasNoCenario) {
                    model2.addElement(codigo);
                }
                jList2.setModel(model2);
            }
        });

        // Botão "Salvar"
        jButton3.addActionListener(e -> {
            salvarCenarios();
        });

        jButton4.addActionListener(e -> abrirGerenciador());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))
                .addGap(0, 2, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(486, 486, 486)
                        .addComponent(jButton3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4)))
                .addGap(13, 13, 13))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 134, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(12, 12, 12))
        );
    }// </editor-fold>

    private void abrirGerenciador() {
        Window parent = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parent instanceof Frame ? (Frame) parent : null, false);

        dialog.setUndecorated(true); // sem barra do sistema
        dialog.setSize(350, 250);
        dialog.setLocationRelativeTo(this);

        // =========================
        // Barra de título customizada
        // =========================
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(30, 30, 30)); // tom escuro próximo do resto da UI
        titleBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY));

        JLabel titulo = new JLabel(" Gerenciar Cenários");

        JButton btnFechar = new JButton("X");
        btnFechar.setFocusPainted(false);
        btnFechar.setBorderPainted(false);
        btnFechar.setContentAreaFilled(false);
        btnFechar.setForeground(Color.LIGHT_GRAY);
        btnFechar.setFont(new Font("Dialog", Font.BOLD, 12));
        btnFechar.addActionListener(e -> dialog.dispose());

        titleBar.add(titulo, BorderLayout.WEST);
        titleBar.add(btnFechar, BorderLayout.EAST);

        // Permitir arrastar o diálogo
        final Point[] mouseDown = {null};
        titleBar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                mouseDown[0] = e.getPoint();
            }
        });
        titleBar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                dialog.setLocation(currCoords.x - mouseDown[0].x,
                                   currCoords.y - mouseDown[0].y);
            }
        });

        // =========================
        // Conteúdo principal
        // =========================
        DefaultListModel<String> listModel = new DefaultListModel<>();

        cenarios.keySet().forEach(listModel::addElement);

        JList<String> lista = new JList<>(listModel);
        lista.setBackground(new Color(40, 40, 40));
        lista.setForeground(Color.WHITE);
        lista.setFont(new Font("Dialog", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(lista);

        JButton btnNovo = new JButton("Novo");
        JButton btnRenomear = new JButton("Renomear");
        JButton btnExcluir = new JButton("Excluir");

        for (JButton b : Arrays.asList(btnNovo, btnRenomear, btnExcluir)) {
            b.setFocusPainted(false);
            b.setBackground(new Color(60, 60, 60));
            b.setForeground(Color.WHITE);
        }

        // -------- INCLUIR --------
        btnNovo.addActionListener(ev -> {
            if (cenarios.size() >= 6) {
                JOptionPane.showMessageDialog(dialog, "Limite de 6 cenários atingido!");
                return;
            }
            String nome = solicitarNomeCenario(dialog, "Nome do novo cenário:", "");
            if (nome == null) return; // usuário cancelou
            if (nomeEmUso(nome)) {
                JOptionPane.showMessageDialog(dialog, "Já existe um cenário com esse nome.");
                return;
            }
            if (nomeEmUso(nome)) {
                JOptionPane.showMessageDialog(dialog, "Já existe um cenário com esse nome.");
                return;
            }

            CenarioDTO dto = new CenarioDTO(nome);
            try {
                persistirCenarioEmArquivo(dto);
            } catch (IOException ex) {
                logger.error("Falha ao criar arquivo do cenário: {}", nome, ex);
                JOptionPane.showMessageDialog(dialog, "Erro ao criar arquivo do cenário.");
                return;
            }

            cenarios.put(nome, dto);
            listModel.addElement(nome);
            ordenarListModel(listModel);
            atualizarCombo();
            jComboBox1.setSelectedItem(nome);
            notificarStatus("Cenário criado.", Color.WHITE);
        });

        // -------- RENOMEAR --------
        btnRenomear.addActionListener(ev -> {
            String selecionado = lista.getSelectedValue();
            if (selecionado == null) {
                JOptionPane.showMessageDialog(dialog, "Selecione um cenário para renomear.");
                return;
            }
            String novoNome = solicitarNomeCenario(dialog, "Novo nome:", selecionado);
            if (novoNome == null) return; // cancelado
            if (selecionado.equals(novoNome)) return;
            if (nomeEmUsoExcluindoAtual(novoNome, selecionado)) {
                JOptionPane.showMessageDialog(dialog, "Já existe um cenário com esse nome.");
                return;
            }
            if (selecionado.equals(novoNome)) {
                return; // nada a fazer
            }
            // Evita conflitos em FS case-insensitive (Windows/macOS)
            if (nomeEmUsoExcluindoAtual(novoNome, selecionado)) {
                JOptionPane.showMessageDialog(dialog, "Já existe um cenário com esse nome.");
                return;
            }

            CenarioDTO dto = cenarios.get(selecionado);
            Path oldPath = buildConfigPath(selecionado);
            Path newPath = buildConfigPath(novoNome);

            try {
                // Se arquivo antigo existir, mova-o para o novo nome (não sobrescreve)
                if (Files.exists(oldPath)) {
                    if (Files.exists(newPath)) {
                        JOptionPane.showMessageDialog(dialog, "Arquivo de destino já existe. Escolha outro nome.");
                        return;
                    }
                    Files.createDirectories(newPath.getParent());
                    Files.move(oldPath, newPath);
                }
                // Atualiza DTO e regrava para garantir nomeCenario consistente no JSON
                dto.setNomeCenario(novoNome);
                persistirCenarioEmArquivo(dto);

                // Atualiza estruturas em memória e UI
                cenarios.remove(selecionado);
                cenarios.put(novoNome, dto);

                listModel.removeElement(selecionado);
                listModel.addElement(novoNome);
                ordenarListModel(listModel);

                atualizarCombo();
                jComboBox1.setSelectedItem(novoNome);
                notificarStatus("Cenário renomeado.", Color.WHITE);

            } catch (IOException ex) {
                logger.error("Erro ao renomear cenário {} -> {}", selecionado, novoNome, ex);
                JOptionPane.showMessageDialog(dialog, "Erro ao renomear arquivo do cenário.");
            }
        });

        // -------- EXCLUIR --------
        btnExcluir.addActionListener(ev -> {
            String selecionado = lista.getSelectedValue();
            if (selecionado == null) {
                JOptionPane.showMessageDialog(dialog, "Selecione um cenário para excluir.");
                return;
            }

            int resp = JOptionPane.showConfirmDialog(
                    dialog,
                    "Excluir o cenário \"" + selecionado + "\"?\nEsta ação não pode ser desfeita.",
                    "Confirmar exclusão",
                    JOptionPane.YES_NO_OPTION
            );
            
            if (resp != JOptionPane.YES_OPTION) return;

            Path path = buildConfigPath(selecionado);
            try {
                // Tenta apagar o arquivo primeiro
                Files.deleteIfExists(path);
                
                //Apaga também os arquivos visuais relacionados
                excluirArquivosVisuaisRelacionados(usuario, selecionado);

                // Se arquivo removido com sucesso (ou não existia), removemos do mapa/UI
                cenarios.remove(selecionado);
                listModel.removeElement(selecionado);
                atualizarCombo();

                if (jComboBox1.getItemCount() > 0) {
                    jComboBox1.setSelectedIndex(0);
                } else {
                    // Sem cenários: lista1 = todas as câmeras, lista2 vazia
                    DefaultListModel<String> model1 = new DefaultListModel<>();
                    List<String> todosCodigos = new ArrayList<>(mapaCameras.keySet());
                    todosCodigos.sort(String::compareToIgnoreCase);
                    for (String codigo : todosCodigos) {
                        model1.addElement(codigo);
                    }
                    jList1.setModel(model1);

                    DefaultListModel<String> model2 = new DefaultListModel<>();
                    jList2.setModel(model2);
                }
                
                notificarStatus("Cenário excluído.", Color.WHITE);

            } catch (IOException ex) {
                logger.error("Erro ao excluir arquivo do cenário: {}", selecionado, ex);
                JOptionPane.showMessageDialog(dialog, "Erro ao excluir arquivo do cenário.");
            }
        });

        JPanel botoes = new JPanel();
        botoes.setBackground(new Color(30, 30, 30));
        botoes.add(btnNovo);
        botoes.add(btnRenomear);
        botoes.add(btnExcluir);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(25, 25, 25));
        content.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        content.add(titleBar, BorderLayout.NORTH);
        content.add(scroll, BorderLayout.CENTER);
        content.add(botoes, BorderLayout.SOUTH);

        dialog.setContentPane(content);
        dialog.setVisible(true);
    }

    private void notificarStatus(String msg, Color cor) {
        createAnimationThread(msg, cor).start();
    }

    private boolean nomeEmUso(String nome) {
        // verifica em memória (case-insensitive) e no FS
        boolean emMemoria = cenarios.keySet().stream().anyMatch(k -> k.equalsIgnoreCase(nome));
        boolean emDisco = Files.exists(buildConfigPath(nome));
        return emMemoria || emDisco;
    }

    private boolean nomeEmUsoExcluindoAtual(String novoNome, String nomeAtual) {
        // usado no renomear: ignora o próprio nome atual; previne conflitos case-insensitive
        if (novoNome.equalsIgnoreCase(nomeAtual)) {
            // Caso de renomear só mudando maiúsculas/minúsculas — em FS case-insensitive isso colide.
            // Trate como "em uso" para evitar problemas.
            return true;
        }
        return nomeEmUso(novoNome);
    }

    private void persistirCenarioEmArquivo(CenarioDTO dto) throws IOException {
        Path p = buildConfigPath(dto.getNomeCenario());
        Files.createDirectories(p.getParent());
        ObjectMapper mapper = new ObjectMapper();
        byte[] json = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(dto);
        Files.write(p, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private void ordenarListModel(DefaultListModel<String> model) {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < model.size(); i++) {
            items.add(model.getElementAt(i));
        }
        items.sort(String::compareToIgnoreCase);
        model.clear();
        for (String s : items) {
            model.addElement(s);
        }
    }


    
    private void atualizarCombo() {
        jComboBox1.setModel(new DefaultComboBoxModel<>(cenarios.keySet().toArray(new String[0])));
        if (!cenarios.isEmpty()) {
            jComboBox1.setSelectedIndex(0);
        }
    }

    // -----------------------------
    // Método para salvar cenários (dinâmico)
    // -----------------------------
    private void salvarCenarios() {
        ObjectMapper objectMapper = new ObjectMapper();

        for (Map.Entry<String, CenarioDTO> entry : cenarios.entrySet()) {
            String nomeCenario = entry.getKey();
            CenarioDTO cenario = entry.getValue();
            Path caminhoArquivo = buildConfigPath(nomeCenario);

            try {
                Files.createDirectories(caminhoArquivo.getParent());

                String json = objectMapper.writerWithDefaultPrettyPrinter()
                                          .writeValueAsString(cenario);
                Files.write(caminhoArquivo, json.getBytes(),
                            StandardOpenOption.CREATE,
                            StandardOpenOption.TRUNCATE_EXISTING);

                logger.info("Cenário salvo: " + caminhoArquivo.toAbsolutePath());

                Thread animationThread = createAnimationThread(
                    "Cenários salvos com sucesso.", Color.WHITE
                );
                animationThread.start();

            } catch (IOException e) {
                Thread animationThread = createAnimationThread(
                    "Erro ao salvar cenário.", Color.RED
                );
                animationThread.start();

                logger.error("Erro ao salvar cenário: " + nomeCenario, e);
            }
        }
    }

    // -----------------------------
    // Método para mapear câmeras
    // -----------------------------
    private Map<String, CameraDTO> mapearCamerasPorCodigo(List<CameraDTO> cameras) {
        Map<String, CameraDTO> mapa = new HashMap<>();
        for (CameraDTO cam : cameras) {
            if (cam.getCodigo() != null) {
                mapa.put(cam.getCodigo(), cam);
            }
        }
        return mapa;
    }
    
 // Exclui arquivos visuais relacionados ao usuário + cenário:
 // <usuario>_<cenario>_visual.cfg  e  <usuario>_<cenario>_visual_ptz.cfg
 private void excluirArquivosVisuaisRelacionados(String usuario, String nomeCenario) {
     String base = usuario + "_" + nomeCenario;
     String[] arquivos = {
         base + "_visual.cfg",
         base + "_visual_ptz.cfg"
     };

     // Diretórios prováveis onde esses .cfg podem estar.
     // Incluí o mesmo diretório dos cenários por segurança,
     // e as pastas "configuracao" e raiz da app.
     Path raiz = Paths.get(System.getProperty("user.dir"));
     Path[] pastas = {
         dirCenarios,                                  // .../configuracao/cenarios
         raiz.resolve("configuracao"),                 // .../configuracao
         raiz                                          // raiz da aplicação
     };

     for (Path pasta : pastas) {
         for (String nomeArq : arquivos) {
             Path alvo = pasta.resolve(nomeArq);
             try {
                 if (Files.deleteIfExists(alvo)) {
                     logger.info("Arquivo visual removido: {}", alvo.toAbsolutePath());
                 }
             } catch (IOException ex) {
                 logger.warn("Falha ao excluir arquivo visual {}: {}", alvo.toAbsolutePath(), ex.toString());
             }
         }
     }
 }

}
