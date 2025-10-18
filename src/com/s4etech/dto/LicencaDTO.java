package com.s4etech.dto;

public class LicencaDTO {

	private String numeroSerie;
	private String codigoLicenca;

	public LicencaDTO() {
	}

	public LicencaDTO(String numeroSerie, String codigoLicenca) {
		super();
		this.numeroSerie = numeroSerie;
		this.codigoLicenca = codigoLicenca;
	}

	public String getNumeroSerie() {
		return numeroSerie;
	}

	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}

	public String getCodigoLicenca() {
		return codigoLicenca;
	}

	public void setCodigoLicenca(String codigoLicenca) {
		this.codigoLicenca = codigoLicenca;
	}
}
