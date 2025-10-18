package com.s4etech.config.manager;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.s4etech.dto.CenarioDTO;

/**
 * Gerencia a leitura e escrita dos arquivos de configuração de cenários.
 */
public class CenarioFileManager {

    private static final Logger logger = LoggerFactory.getLogger(CenarioFileManager.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, Path> caminhoArquivos; // chave: nome do cenario (lowercase), valor: Path
    private final String usuario;
    private final Path dirCenarios;

    /**
     * Verifica se o arquivo de configuração de um cenário existe.
     * @param nomeCenario Nome do cenário a verificar.
     * @return true se o arquivo existir, false caso contrário.
     */
    public boolean cenarioExiste(String nomeCenario) {
        if (nomeCenario == null) return false;

        Path caminho = caminhoArquivos.get(nomeCenario.toLowerCase());
        if (caminho == null) {
            logger.warn("Cenário inválido: {}", nomeCenario);
            return false;
        }
        return Files.exists(caminho);
    }

    public CenarioFileManager(String usuario) {
        this.usuario = usuario;
        String basePath = System.getProperty("user.dir") + "/configuracao/cenarios";
        this.dirCenarios = Paths.get(basePath);

        this.caminhoArquivos = new HashMap<>();

        // Regex: ^<usuario>_([^.]+)\.json$  (case-insensitive)
        final Pattern padraoArquivo = Pattern.compile("^" + Pattern.quote(usuario) + "_([^.]+)\\.json$", Pattern.CASE_INSENSITIVE);

        try {
            if (Files.isDirectory(dirCenarios)) {
                Files.list(dirCenarios)
                        .filter(p -> {
                            String f = p.getFileName().toString();
                            return f.toLowerCase().endsWith(".json");
                        })
                        .forEach(p -> {
                            String file = p.getFileName().toString();
                            Matcher m = padraoArquivo.matcher(file);
                            if (m.matches()) {
                                String chave = m.group(1).toLowerCase(); // ex.: "BLADE" -> "blade"
                                caminhoArquivos.put(chave, p);
                            }
                        });
            } else {
                logger.warn("Diretório de cenários não existe: {}", dirCenarios.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao listar arquivos JSON em " + dirCenarios.toAbsolutePath(), e);
        }

        logger.info("Cenários detectados para '{}': {}", usuario, caminhoArquivos.keySet());
    }

    /**
     * Carrega todos os cenários a partir dos arquivos JSON.
     * @return Map contendo os cenários carregados.
     */
    public Map<String, CenarioDTO> carregarCenarios() {
        Map<String, CenarioDTO> cenarios = new HashMap<>();

        for (Map.Entry<String, Path> entry : caminhoArquivos.entrySet()) {
            String nomeCenario = entry.getKey(); // já em lowercase
            Path caminho = entry.getValue();

            if (Files.exists(caminho)) {   
                try {
                    CenarioDTO cenario = objectMapper.readValue(caminho.toFile(), CenarioDTO.class);
                    cenario.setNomeCenario(nomeCenario);
                    cenarios.put(nomeCenario, cenario);
                    logger.info("Cenário carregado do arquivo: {}", caminho.toAbsolutePath());
                } catch (IOException e) {
                    logger.error("Erro ao ler cenário {} do arquivo: {}", nomeCenario, caminho, e);
                }
            } else {
                logger.info("Arquivo não encontrado para cenário {}: {}", nomeCenario, caminho);
                cenarios.put(nomeCenario, new CenarioDTO(nomeCenario)); 
            }
        }

        return cenarios;
    }

    /**
     * Salva todos os cenários nos arquivos JSON correspondentes.
     * Se o cenário não existir em caminhoArquivos, cria o path como <usuario>_<nome>.json.
     * @param cenarios Map contendo os cenários a serem salvos.
     */
    public void salvarCenarios(Map<String, CenarioDTO> cenarios) {
        for (Map.Entry<String, CenarioDTO> entry : cenarios.entrySet()) {
            String nomeCenario = entry.getKey();
            CenarioDTO cenario = entry.getValue();

            // garante chave lowercase
            String chave = nomeCenario == null ? null : nomeCenario.toLowerCase();
            if (chave == null || chave.isEmpty()) continue;

            Path caminho = caminhoArquivos.get(chave);
            if (caminho == null) {
                // cenário novo: cria caminho padrão <usuario>_<nome>.json
                caminho = dirCenarios.resolve(usuario + "_" + chave + ".json");
                caminhoArquivos.put(chave, caminho);
            }

            try {
                Files.createDirectories(caminho.getParent());
                String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cenario);
                Files.write(caminho, json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                logger.info("Cenário salvo: {}", caminho.toAbsolutePath());
            } catch (IOException e) {
                logger.error("Erro ao salvar cenário: {}", chave, e);
            }
        }
    }
}
