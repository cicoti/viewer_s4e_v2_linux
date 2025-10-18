package com.s4etech.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PipelineUtils {

    // Método para obter o valor de video-direction a partir de uma string de configuração
    public static String getVideoDirection(String basePipeline) {
        String direction = null;
        Pattern pattern = Pattern.compile("video-direction=(\\S+)");
        Matcher matcher = pattern.matcher(basePipeline);
        if (matcher.find()) {
            direction = matcher.group(1);
        }
        return direction;
    }

    // Método para obter o valor de method a partir de uma string de configuração
    public static String getMethod(String basePipeline) {
        String method = null;
        Pattern pattern = Pattern.compile("method=(\\S+)");
        Matcher matcher = pattern.matcher(basePipeline);
        if (matcher.find()) {
            method = matcher.group(1);
        }
        return method;
    }

    // Exemplo de outro método útil para manipular pipelines se necessário
    public static String setVideoDirection(String basePipeline, String newDirection) {
        return basePipeline.replaceAll("video-direction=\\S+", "video-direction=" + newDirection);
    }

    public static String setMethod(String basePipeline, String newMethod) {
        return basePipeline.replaceAll("method=\\S+", "method=" + newMethod);
    }
}
