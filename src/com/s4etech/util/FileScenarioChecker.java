package com.s4etech.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileScenarioChecker {

    private static final Logger logger = LoggerFactory.getLogger(FileScenarioChecker.class);

    // .../configuracao/cenarios
    private static final Path BASE_DIR =
            Path.of(System.getProperty("user.dir"), "configuracao", "cenarios");

    /**
     * Lista todos os cenários (arquivos .json) do usuário.
     * Retorna um mapa {CHAVE_EM_UPPER -> Path do arquivo}, ordenado alfabeticamente pela chave.
     * A "chave" é o trecho entre "<usuario>_" e ".json".
     * Ex.: komatsu_BLADE.json  -> CHAVE "BLADE"
     *     komatsu_around.json -> CHAVE "AROUND"
     */
    public static Map<String, Path> listarCenarios(String usuario) {
        Map<String, Path> result = new LinkedHashMap<>();

        if (!Files.isDirectory(BASE_DIR)) {
            logger.warn("Diretório de cenários não existe: {}", BASE_DIR.toAbsolutePath());
            return result; // vazio
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(BASE_DIR, "*.json")) {
            final String prefix = (usuario + "_").toLowerCase(Locale.ROOT);

            // Filtra só arquivos do usuário e extrai a "chave"
            List<Path> matches = new ArrayList<>();
            for (Path p : stream) {
                if (Files.isRegularFile(p)) {
                    String file = p.getFileName().toString();
                    String fileLower = file.toLowerCase(Locale.ROOT);
                    if (fileLower.startsWith(prefix) && fileLower.endsWith(".json")) {
                        matches.add(p);
                    }
                }
            }

            // Monta {CHAVE -> Path}
            result = matches.stream()
                    .collect(Collectors.toMap(
                            p -> extrairChave(usuario, p.getFileName().toString()),
                            p -> p,
                            (a, b) -> a,
                            LinkedHashMap::new
                    ));

            // Ordena por chave (opcional)
            result = result.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (a, b) -> a,
                            LinkedHashMap::new
                    ));

            logger.info("Cenários encontrados para '{}': {}", usuario, result.keySet());
        } catch (Exception e) {
            logger.error("Erro ao listar cenários do usuário '{}': {}", usuario, e.getMessage(), e);
        }

        return result;
    }

    /**
     * Versão “compatível” com a antiga: devolve {CHAVE -> true} para cada .json encontrado.
     * (Não inclui entradas 'false' — se não existe, não aparece no mapa.)
     */
    public static Map<String, Boolean> mapaIsExist(String usuario) {
        Map<String, Path> cenarios = listarCenarios(usuario);
        Map<String, Boolean> out = new LinkedHashMap<>();
        cenarios.forEach((k, v) -> out.put(k, true));
        return out;
    }

    /**
     * Testa se existe o arquivo do cenário <key> para o usuário.
     * A comparação de key é case-insensitive.
     */
    public static boolean mapaIsExist(String usuario, String key) {
        return getPath(usuario, key).isPresent();
    }

    /**
     * Retorna o Path do cenário <key>, se existir.
     * A comparação de key é case-insensitive.
     */
    public static Optional<Path> getPath(String usuario, String key) {
        Map<String, Path> cenarios = listarCenarios(usuario);
        String wanted = key.toUpperCase(Locale.ROOT);
        for (Map.Entry<String, Path> e : cenarios.entrySet()) {
            if (e.getKey().equalsIgnoreCase(wanted)) {
                return Optional.of(e.getValue());
            }
        }
        return Optional.empty();
    }

    /**
     * Extrai a CHAVE do nome do arquivo: "<usuario>_<CHAVE>.json" -> "CHAVE" (em UPPER).
     */
    private static String extrairChave(String usuario, String fileName) {
        // remove prefixo "<usuario>_"
        String withoutPrefix = fileName.substring((usuario + "_").length());
        // remove sufixo ".json" (qualquer caixa)
        int dot = withoutPrefix.lastIndexOf('.');
        String key = (dot > 0 ? withoutPrefix.substring(0, dot) : withoutPrefix);
        return key.toUpperCase(Locale.ROOT);
    }
}
