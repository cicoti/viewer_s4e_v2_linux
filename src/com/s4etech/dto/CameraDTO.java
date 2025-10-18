package com.s4etech.dto;

public class CameraDTO {

	private String codigo;
	private String ip;
	private String porta;
	private String extensao;
	private String usuario;
	private String senha;
	private int PTZ;
	private String video;
	private String audio;

	public CameraDTO() {

	}

	public CameraDTO(int id, int exibir, String codigo, String ip, String porta, String extensao, String usuario, String senha, int PTZ, String video, String audio) {
		super();
		this.codigo = codigo;
		this.ip = ip;
		this.porta = porta;
		this.extensao = extensao;
		this.usuario = usuario;
		this.senha = senha;
		this.PTZ = PTZ;
		this.video = video;
		this.audio = audio;

	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
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

	public String getExtensao() {
		return extensao;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
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

	public int getPTZ() {
		return PTZ;
	}

	public void setPTZ(int pTZ) {
		PTZ = pTZ;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getAudio() {
		return audio;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}

	@Override
	public String toString() {
		return "CameraDTO [codigo=" + codigo + ", ip=" + ip + ", porta=" + porta + ", extensao=" + extensao
				+ ", usuario=" + usuario + ", senha=" + senha + ", PTZ=" + PTZ + ", video=" + video + ", audio=" + audio
				+ "]";
	}

	

}