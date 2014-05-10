package de.hshannover.f4.trust.visitmeta.gui.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;

public class HintTextField extends JTextField {

	private static final long serialVersionUID = -3261867067230514829L;

	private String mHint;

	public HintTextField() {
		this("");
	}

	public HintTextField(final String hint) {
		mHint = hint;
		setHint(mHint);
	}

	public void setHint(String hint) {
		setUI(new HintTextFieldUI(hint, true));
	}

	@Override
	public String getText() {
		String typed = super.getText();
		return typed.equals(mHint) ? "" : typed;
	}
}

class HintTextFieldUI extends BasicTextFieldUI implements FocusListener {

	private String mHint;
	private boolean mHideOnFocus;
	private Color mHintColor;

	public HintTextFieldUI(String hint) {
		this(hint, false);
	}

	public HintTextFieldUI(String hint, boolean hideOnFocus) {
		this(hint, hideOnFocus, null);
	}

	public HintTextFieldUI(String hint, boolean hideOnFocus, Color color) {
		mHint = hint;
		mHideOnFocus = hideOnFocus;
		mHintColor = color;
	}

	public Color getColor() {
		return mHintColor;
	}

	public void setColor(Color color) {
		mHintColor = color;
		repaint();
	}

	private void repaint() {
		if (getComponent() != null) {
			getComponent().repaint();
		}
	}

	public boolean isHideOnFocus() {
		return mHideOnFocus;
	}

	public void setHideOnFocus(boolean hideOnFocus) {
		mHideOnFocus = hideOnFocus;
		repaint();
	}

	public String getHint() {
		return mHint;
	}

	public void setHint(String hint) {
		mHint = hint;
		repaint();
	}

	@Override
	protected void paintSafely(Graphics g) {
		super.paintSafely(g);
		JTextComponent comp = getComponent();
		if (mHint != null && comp.getText().length() == 0 && (!(mHideOnFocus && comp.hasFocus()))) {
			if (mHintColor != null) {
				g.setColor(mHintColor);
			} else {
				g.setColor(Color.gray);
			}
			int padding = (comp.getHeight() - comp.getFont().getSize()) / 2;
			g.drawString(mHint, 5, comp.getHeight() - padding - 1);
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (mHideOnFocus)
			repaint();

	}

	@Override
	public void focusLost(FocusEvent e) {
		if (mHideOnFocus)
			repaint();
	}

	@Override
	protected void installListeners() {
		super.installListeners();
		getComponent().addFocusListener(this);
	}

	@Override
	protected void uninstallListeners() {
		super.uninstallListeners();
		getComponent().removeFocusListener(this);
	}
}
