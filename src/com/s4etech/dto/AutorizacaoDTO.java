package com.s4etech.dto;

public class AutorizacaoDTO {

    private String usuario;
    private String autorizacao;
    private String dataAlteracao;
    
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        // garante sempre minúsculo (mesmo que seja null-safe)
        this.usuario = (usuario != null) ? usuario.toLowerCase() : null;
    }

    public String getAutorizacao() {
        return autorizacao;
    }

    public void setAutorizacao(String autorizacao) {
        this.autorizacao = autorizacao;
    }

    public String getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(String dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }
}
