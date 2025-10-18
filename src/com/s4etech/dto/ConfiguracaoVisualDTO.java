package com.s4etech.dto;

public class ConfiguracaoVisualDTO {

	private String codigoCamera;
	private int tamanhoHorizontal;
	private int tamanhoVertical;
	private int posicaoHorizontal;
	private int posicaoVertical;
	private String rotate;
	private String flip;
	
	public ConfiguracaoVisualDTO() {
		
	}
	
	public ConfiguracaoVisualDTO(String codigoCamera, int tamanhoHorizontal, int tamanhoVertical, int posicaoHorizontal,
			int posicaoVertical, String rotate, String flip) {
		super();
		this.codigoCamera = codigoCamera;
		this.tamanhoHorizontal = tamanhoHorizontal;
		this.tamanhoVertical = tamanhoVertical;
		this.posicaoHorizontal = posicaoHorizontal;
		this.posicaoVertical = posicaoVertical;
		this.rotate = rotate;
		this.flip = flip;
	}
	public String getCodigoCamera() {
		return codigoCamera;
	}
	public void setCodigoCamera(String codigoCamera) {
		this.codigoCamera = codigoCamera;
	}
	public int getTamanhoHorizontal() {
		return tamanhoHorizontal;
	}
	public void setTamanhoHorizontal(int tamanhoHorizontal) {
		this.tamanhoHorizontal = tamanhoHorizontal;
	}
	public int getTamanhoVertical() {
		return tamanhoVertical;
	}
	public void setTamanhoVertical(int tamanhoVertical) {
		this.tamanhoVertical = tamanhoVertical;
	}
	public int getPosicaoHorizontal() {
		return posicaoHorizontal;
	}
	public void setPosicaoHorizontal(int posicaoHorizontal) {
		this.posicaoHorizontal = posicaoHorizontal;
	}
	public int getPosicaoVertical() {
		return posicaoVertical;
	}
	public void setPosicaoVertical(int posicaoVertical) {
		this.posicaoVertical = posicaoVertical;
	}
	public String getRotate() {
		return rotate;
	}
	public void setRotate(String rotate) {
		this.rotate = rotate;
	}
	public String getFlip() {
		return flip;
	}
	public void setFlip(String flip) {
		this.flip = flip;
	}
}
