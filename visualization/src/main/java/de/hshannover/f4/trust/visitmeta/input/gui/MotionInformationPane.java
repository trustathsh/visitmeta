package de.hshannover.f4.trust.visitmeta.input.gui;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;

/**
 * A {@link JLayeredPane} that encapsulates the rendering panel and a
 * {@link MotionInformationPanel} in such a way, that the latter is rendered
 * "above" the rendering panel.
 * 
 * This allows to display information about the motion control device on top
 * of the actual graph representation.
 * 
 * @author Bastian Hellmann
 * @author Oleg Wetzler
 *
 */
public class MotionInformationPane extends JLayeredPane {

	private static final long serialVersionUID = 1L;
	private JComponent component;
	private MotionInformationPanel motionInformationPanel;

	/**
	 * Creates a new {@link MotionInformationPane} that renders
	 * 
	 * @param component the {@link JComponent}, that will be rendered
	 * "at the bottom", with a {@link MotionInformationPanel} "on top".
	 */
	public MotionInformationPane(JComponent component) {
		this.component = component;
		this.motionInformationPanel = new MotionInformationPanel();

		this.add(motionInformationPanel, 101);
		this.add(component, 100);
		motionInformationPanel.setVisible(false);
		repaint();
	}

	/**
	 * Sets the cursor position, un-hide the {@link MotionInformationPanel} and
	 * repaints the {@link JLayeredPane}.
	 * 
	 * @param x x coordinate of the cursor
	 * @param y y coordinate of the cursor
	 * @param z z coordinate of the cursor
	 */
	public void setCursorPosition(double x, double y, double z) {
		motionInformationPanel.setCursorPosition(x, y, z);
		motionInformationPanel.setVisible(true);
		repaint();
	}

	/**
	 * Hides the {@link MotionInformationPanel}.
	 */
	public void hideMotionInformationPanel() {
		motionInformationPanel.setVisible(false);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#repaint()
	 */
	@Override
	public void repaint() {
		super.repaint();
		motionInformationPanel.setBounds(0, 0, getWidth(), getHeight());
		component.setBounds(0, 0, getWidth(), getHeight());
	}
}
