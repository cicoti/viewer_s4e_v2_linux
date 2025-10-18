package com.s4etech.integration;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;

import org.freedesktop.gstreamer.Bin;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.Pipeline;
import org.freedesktop.gstreamer.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.s4etech.util.GstVideoComponent;

public class WebCamera {

	private static final Logger logger = LoggerFactory.getLogger(WebCamera.class);

	private static Pipeline pipeline;
	private static GstVideoComponent videoComponente;
	private static Point initialClick;
	private static JFrame jframe;

	public static void bringToFront() {

		if (jframe != null) {
			jframe.setExtendedState(JFrame.NORMAL);
			jframe.toFront();
			jframe.repaint();
		}

	}

	public static void startCameras() {

		String[] args = Gst.init("WebCamera", new String[] {});

		Gst.init(Version.BASELINE, "WebCamera", args);

		EventQueue.invokeLater(() -> {
			videoComponente = new GstVideoComponent();
			Bin bin = createBinWithAspectRatio();
			bin.setName("WEBC");

			pipeline = new Pipeline();
			pipeline.addMany(bin, videoComponente.getElement());
			pipeline.linkMany(bin, videoComponente.getElement());

			jframe = new JFrame("Camera Web");
			jframe.setSize(300, 200);
			jframe.setLocation(300, 200);
			jframe.setUndecorated(true);
			jframe.add(videoComponente);
			videoComponente.setPreferredSize(new Dimension(300, 200));
			jframe.pack();
			jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			jframe.addMouseListener(new MouseAdapter() {
				private int clickCount = 0;

				@Override
				public void mouseClicked(MouseEvent e) {
					clickCount++;
					if (clickCount % 2 == 0) {
						switchToNextAspectRatio();
					}
				}
			});

			jframe.addMouseListener(new MouseAdapter() {
				private int clickCount = 0;

				@Override
				public void mouseClicked(MouseEvent e) {
					clickCount++;
					if (clickCount % 2 == 0) {
						switchToNextAspectRatio();
					}
				}

				public void mousePressed(MouseEvent e) {
					initialClick = e.getPoint();
				}
			});

			jframe.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					int thisX = jframe.getLocation().x;
					int thisY = jframe.getLocation().y;

					if (jframe.getCursor().getType() == Cursor.DEFAULT_CURSOR) {
						int xMoved = (thisX + e.getX()) - (thisX + initialClick.x);
						int yMoved = (thisY + e.getY()) - (thisY + initialClick.y);
						int X = thisX + xMoved;
						int Y = thisY + yMoved;
						jframe.setLocation(X, Y);
					}
				}
			});

			pipeline.play();
			jframe.setVisible(true);
		});
	}

	private static Bin createBinWithAspectRatio() {

		return Gst.parseBinFromDescription(
				"mfvideosrc ! " + "videoscale ! videoconvert ! " + "capsfilter caps=video/x-raw,width=300,height=100",
				true);
	}

	private static void switchToNextAspectRatio() {
		logger.info("Mudando para a próxima proporção de aspecto");

		pipeline.stop();
		pipeline.remove(pipeline.getElementByName("WEBC"));

		Bin newBin = createBinWithAspectRatio();
		newBin.setName("WEBC");

		pipeline.add(newBin);
		Pipeline.linkMany(newBin, videoComponente.getElement());

		videoComponente.setPreferredSize(new Dimension(300, 200));
		JFrame topFrame = (JFrame) javax.swing.SwingUtilities.getWindowAncestor(videoComponente);
		topFrame.pack();

		pipeline.play();

	}

	public static void stopWebCam() {
		logger.info("Parando a webcam");
		if (pipeline != null) {
			pipeline.stop();
			pipeline.dispose();
		}
		if (jframe != null) {
			jframe.dispose();
		}
	}
}
