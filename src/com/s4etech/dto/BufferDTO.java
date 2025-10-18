package com.s4etech.dto;

public class BufferDTO {

	private String valor;
	private boolean ativar;

	public BufferDTO() {
	}

	public BufferDTO(String valor, boolean ativar) {
		this.valor = valor;
		this.ativar = ativar;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public boolean getAtivar() {
		return ativar;
	}

	public void setAtivar(boolean ativar) {
		this.ativar = ativar;
	}

}
