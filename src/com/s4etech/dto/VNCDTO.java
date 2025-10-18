package com.s4etech.dto;

public class VNCDTO {

	private String servidor;
	private String porta;
	private String senha;
	private String cor;
	private boolean cursorLocal;
	private boolean copyrect;
	private boolean rre;
	private boolean hextile;
	private boolean zlib;
	private boolean ativar;

	public VNCDTO() {
	}

	public VNCDTO(String servidor, String porta, String senha, String cor, boolean cursorLocal, boolean copyrect,
			boolean rre, boolean hextile, boolean zlib, boolean ativar) {
		this.servidor = servidor;
		this.porta = porta;
		this.senha = senha;
		this.cor = cor;
		this.cursorLocal = cursorLocal;
		this.copyrect = copyrect;
		this.rre = rre;
		this.hextile = hextile;
		this.zlib = zlib;
		this.ativar = ativar;
	}

	public String getServidor() {
		return servidor;
	}

	public void setServidor(String servidor) {
		this.servidor = servidor;
	}

	public String getPorta() {
		return porta;
	}

	public void setPorta(String porta) {
		this.porta = porta;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public boolean isCursorLocal() {
		return cursorLocal;
	}

	public void setCursorLocal(boolean cursorLocal) {
		this.cursorLocal = cursorLocal;
	}

	public boolean isCopyrect() {
		return copyrect;
	}

	public void setCopyrect(boolean copyrect) {
		this.copyrect = copyrect;
	}

	public boolean isRre() {
		return rre;
	}

	public void setRre(boolean rre) {
		this.rre = rre;
	}

	public boolean isHextile() {
		return hextile;
	}

	public void setHextile(boolean hextile) {
		this.hextile = hextile;
	}

	public boolean isZlib() {
		return zlib;
	}

	public void setZlib(boolean zlib) {
		this.zlib = zlib;
	}

	public boolean isAtivar() {
		return ativar;
	}

	public void setAtivar(boolean ativar) {
		this.ativar = ativar;
	}

}
