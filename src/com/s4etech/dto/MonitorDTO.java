package com.s4etech.dto;

public class MonitorDTO {
	private int number;
	private int width;
	private int height;

	public MonitorDTO(int number, int width, int height) {
		this.number = number;
		this.width = width;
		this.height = height;
	}

	public int getNumber() {
		return number;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public String toString() {
		return "Monitor " + number + ": " + width + "x" + height;
	}
}