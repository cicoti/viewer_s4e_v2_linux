package com.s4etech.dto;

public class PerformanceDTO {

    private String configuracao; // "Velocidade" ou "Qualidade" para indicar a escolha do RadioButton
    private int bufferSize; // Tamanho do buffer
    private int latency; // Latência configurada (em ms)
    private String protocolo;
    private int timeout; // Timeout configurado (em milissegundo )
    private String aceleracao;

    // Construtor vazio
    public PerformanceDTO() {
    }

    // Construtor com parâmetros
    public PerformanceDTO(String configuracao, int bufferSize, int latency, String protocolo, int timeout, String aceleracao) {
        this.configuracao = configuracao;
        this.bufferSize = bufferSize;
        this.latency = latency;
        this.protocolo = protocolo;
        this.timeout = timeout;
        this.aceleracao = aceleracao;
    }

	public String getConfiguracao() {
		return configuracao;
	}

	public void setConfiguracao(String configuracao) {
		this.configuracao = configuracao;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int getLatency() {
		return latency;
	}

	public void setLatency(int latency) {
		this.latency = latency;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	

	public String getAceleracao() {
		return aceleracao;
	}

	public void setAceleracao(String aceleracao) {
		this.aceleracao = aceleracao;
	}

	@Override
	public String toString() {
		return "PerformanceDTO [configuracao=" + configuracao + ", bufferSize=" + bufferSize + ", latency=" + latency
				+ ", protocolo=" + protocolo + ", timeout=" + timeout + ", aceleracao=" + aceleracao + "]";
	}


    
}
