package com.s4etech.dto;

public class ConfiguracaoVisualPtzDTO {

	private String codigoPTZ;
	private int estado;
	
	public ConfiguracaoVisualPtzDTO() {
	}
	
	public ConfiguracaoVisualPtzDTO(String codigoPTZ, int estado) {
		super();
		this.codigoPTZ = codigoPTZ;
		this.estado = estado;
	}

	public String getCodigoPTZ() {
		return codigoPTZ;
	}

	public void setCodigoPTZ(String codigoPTZ) {
		this.codigoPTZ = codigoPTZ;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}
	
	
		
}
