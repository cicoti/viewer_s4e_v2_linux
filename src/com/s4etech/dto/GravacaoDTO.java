package com.s4etech.dto;

public class GravacaoDTO {

	private String caminho;
	private boolean ativar;
	private String qualidade;

	public GravacaoDTO() {
	}

	public GravacaoDTO(String caminho, boolean ativar, String qualidade) {
		this.caminho = caminho;
		this.ativar = ativar;
		this.qualidade = qualidade;
	}

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}

	public boolean isAtivar() {
		return ativar;
	}

	public void setAtivar(boolean ativar) {
		this.ativar = ativar;
	}

	public String getQualidade() {
		return qualidade;
	}

	public void setQualidade(String qualidade) {
		this.qualidade = qualidade;
	}



}
