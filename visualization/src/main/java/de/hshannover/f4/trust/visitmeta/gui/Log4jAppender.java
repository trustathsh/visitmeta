/*
 * #%L
 * =====================================================
 *   _____                _     ____  _   _       _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \| | | | ___ | | | |
 *    | | | '__| | | / __| __|/ / _` | |_| |/ __|| |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _  |\__ \|  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_| |_||___/|_| |_|
 *                             \____/
 * 
 * =====================================================
 * 
 * Hochschule Hannover
 * (University of Applied Sciences and Arts, Hannover)
 * Faculty IV, Dept. of Computer Science
 * Ricklinger Stadtweg 118, 30459 Hannover, Germany
 * 
 * Email: trust@f4-i.fh-hannover.de
 * Website: http://trust.f4.hs-hannover.de/
 * 
 * This file is part of visitmeta-visualization, version 0.5.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2015 Trust@HsH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package de.hshannover.f4.trust.visitmeta.gui;

import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

public class Log4jAppender extends AppenderSkeleton {
	private static final Logger LOGGER = Logger.getLogger(Log4jAppender.class);
	private JTextPane mTextPane = null;
	private JScrollPane mScrollPane = null;
	private Layout mLayout = null;

	public Log4jAppender() {
		super();
		mTextPane = new JTextPane() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean getScrollableTracksViewportWidth() {
				return false;
			}

			@Override
			public boolean getScrollableTracksViewportHeight() {
				return false;
			}
		};
		mScrollPane = new JScrollPane(mTextPane);
		mLayout = new PatternLayout("%r [%t] %-5p %c %x - %m%n");
	}

	@Override
	public void close() {
		/* No logging. */
	}

	@Override
	public boolean requiresLayout() {
		/* No logging. */
		return false;
	}

	@Override
	protected void append(LoggingEvent pE) {
		/* No logging -> Infinity loop. */
		String vText = mLayout.format(pE);
		StyledDocument vDoc = mTextPane.getStyledDocument();
		try {
			vDoc.insertString(vDoc.getLength(), vText, null);
			scrollToBottom();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void scrollToBottom() {
		/* No logging -> Infinity loop. */
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Rectangle vRect = mTextPane.modelToView(mTextPane.getDocument().getLength());
					if (vRect != null) {
						vRect.setSize(mScrollPane.getViewport().getWidth(), // width
								vRect.height // height
						);
						mTextPane.scrollRectToVisible(vRect);
					}
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public JComponent getComponent() {
		LOGGER.trace("Method getComponent() called.");
		return mScrollPane;
	}
}
