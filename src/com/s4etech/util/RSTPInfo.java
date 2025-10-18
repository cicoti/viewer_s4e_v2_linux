package com.s4etech.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class RSTPInfo {
	
    private String ip;
    private String porta;
    private String usuario;
    private String senha;
    private String extensao;
    
    public RSTPInfo() {
    	
    }

    public RSTPInfo(String ip, String porta, String usuario, String senha, String extensao) {
        this.ip = ip;
        this.porta = porta;
        this.usuario = usuario;
        this.senha = senha;
        this.extensao = extensao;
    }

    public String montarURL() {
        StringBuilder url = new StringBuilder();

        if (usuario != null && !usuario.isEmpty() && senha != null && !senha.isEmpty()) {
        	
            try {
				url.append(usuario).append(":").append(URLEncoder.encode(senha, StandardCharsets.UTF_8.toString())).append("@");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        url.append(ip);
             
        if (porta != null && !porta.isEmpty()) {
            url.append(":").append(porta);
        }
 
        if (extensao != null && !extensao.isEmpty()) {
            // Apenas adiciona uma barra se a porta não for fornecida
            if (porta == null || porta.isEmpty()) {
                url.append("/");
            }
            url.append(extensao); // Aqui corrigimos para não adicionar uma barra extra antes da extensão
        }
        
        return url.toString();
    }

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPorta() {
		return porta;
	}

	public void setPorta(String porta) {
		this.porta = porta;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getExtensao() {
		return extensao;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}
        
}
