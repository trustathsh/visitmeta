package de.hshannover.f4.trust.visitmeta.input.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import de.hshannover.f4.trust.visitmeta.input.device.Device;

/**
 * A {@link JPanel} that displays information about the current status
 * of a motion controller {@link Device}.
 * This information may help the user to understand why and how the software
 * interprets the actions of the user with a specific device.
 * 
 * @author Bastian Hellmann
 * @author Oleg Wetzler
 *
 */
public class MotionInformationPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private int rp = 10;
	private int rm = 40;
	private int x;
	private int y;
	private double gap = 1 / 5.0;
	private double size = 0.5;
	private double z;
	private boolean joystick = true;

	/**
	 * Sets the cursor position.
	 * The x and y coordinates are multiplied with radius/2.
	 * 
	 * @param x x coordinate of the cursor
	 * @param y y coordinate of the cursor
	 * @param z z coordinate of the cursor
	 */
	public void setCursorPosition(double x, double y, double z) {
		this.x = (int) (x * rm / 2);
		this.y = (int) (y * rm / 2);
		this.z = z;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		int width = getWidth();
		int high = getHeight();

		if (joystick) {
			g.setColor(Color.GRAY);
			g.drawOval(width / 2 - rm / 2, high / 2 - rm / 2, rm, rm);
			g.drawLine((int) (width / 2 + rm / 2 + gap * rm),
					high / 2 + rm / 2, (int) (width / 2 + rm / 2 + gap + rm
							* size), high / 2 + rm / 2);
			g.drawLine((int) (width / 2 + rm / 2 + gap * rm),
					high / 2 - rm / 2, (int) (width / 2 + rm / 2 + gap + rm
							* size), high / 2 - rm / 2);
		}

		g.setColor(Color.RED);
		g.fillOval(width / 2 - rp / 2 + x, high / 2 - rp / 2 + y, rp, rp);
		g.drawLine((int) (width / 2 + rm / 2 + gap * rm), (int) (high / 2 - z
				* rm / 2), (int) (width / 2 + rm / 2 + gap + rm * size),
				(int) (high / 2 - z * rm / 2));
	}

}
