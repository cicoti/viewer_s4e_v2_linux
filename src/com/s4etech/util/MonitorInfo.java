package com.s4etech.util;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;

import com.s4etech.dto.MonitorDTO;

public class MonitorInfo {

	public static List<MonitorDTO> getMonitorInfo() {
		List<MonitorDTO> monitors = new ArrayList<>();

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] screens = ge.getScreenDevices();

		int monitorNumber = 0;
		for (GraphicsDevice screen : screens) {
			DisplayMode mode = screen.getDisplayMode();
			int width = mode.getWidth();
			int height = mode.getHeight();
			monitors.add(new MonitorDTO(monitorNumber, width, height));
			monitorNumber++;
		}

		return monitors;
	}

	public static boolean isValid(int width, int height) {

	
		int sunWidth = 0;
		int sunHeight = 0;

		List<MonitorDTO> monitores = getMonitorInfo();

		for (MonitorDTO monitorDTO : monitores) {

			sunWidth += monitorDTO.getWidth();
			sunHeight += monitorDTO.getHeight();

		}

		if (width <= sunWidth && height <= sunHeight) {
			return true;
		}

		return false;
	}

}
