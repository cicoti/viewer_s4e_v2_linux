package com.s4etech.dto;

public class UDPDTO {
	
	private String ipDestino;
	private String portaDestino;
	private String ipLocal;
	private String portaLocal;
	private boolean isAtivar;
	
	public UDPDTO() {
	}

	public UDPDTO(String ipDestino, 
			String portaDestino, String ipLocal, String portaLocal, boolean isAtivar) {
			this.ipDestino = ipDestino;
			this.portaDestino = portaDestino;
			this.ipLocal = ipLocal;
			this.portaLocal = portaLocal;
			this.isAtivar = isAtivar;
	}

	public String getIpDestino() {
		return ipDestino;
	}

	public void setIpDestino(String ipDestino) {
		this.ipDestino = ipDestino;
	}

	public String getPortaDestino() {
		return portaDestino;
	}

	public void setPortaDestino(String portaDestino) {
		this.portaDestino = portaDestino;
	}

	public String getIpLocal() {
		return ipLocal;
	}

	public void setIpLocal(String ipLocal) {
		this.ipLocal = ipLocal;
	}

	public String getPortaLocal() {
		return portaLocal;
	}

	public void setPortaLocal(String portaLocal) {
		this.portaLocal = portaLocal;
	}

	public boolean isAtivar() {
		return isAtivar;
	}

	public void setAtivar(boolean isAtivar) {
		this.isAtivar = isAtivar;
	}
	
}
