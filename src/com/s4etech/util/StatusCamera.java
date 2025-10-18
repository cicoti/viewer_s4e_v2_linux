package com.s4etech.util;
public enum StatusCamera {
    CONFIGURADA_NAO_ATIVA(0),//00
    ATIVA(1),//01
    NAO_CONFIGURADA(2),//10
    ERRO(3);//11

    private final int valor;

    StatusCamera(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public static StatusCamera fromValor(int valor) {
        for (StatusCamera status : values()) {
            if (status.valor == valor) {
                return status;
            }
        }
        throw new IllegalArgumentException("Valor inválido para StatusCamera: " + valor);
    }
}
