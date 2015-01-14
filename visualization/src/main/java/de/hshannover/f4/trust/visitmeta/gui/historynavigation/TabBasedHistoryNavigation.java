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
 * This file is part of visitmeta-visualization, version 0.2.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.gui.historynavigation;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.SortedMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.hshannover.f4.trust.visitmeta.datawrapper.TimeHolder;

public class TabBasedHistoryNavigation implements Observer,
		HistoryNavigationStrategy {

	private JPanel mPanel;
	private TimeHolder mTimeHolder;
	private String mRestUrl;

	private int mCurrentMode;

	private static final String TIMEFORMAT = "yyyy-MM-FF HH:mm:ss";
	private DateFormat mDateFormatter;

	private static final int INDEX_LIVE_VIEW = 0;
	private static final int INDEX_HISTORY_VIEW = 1;
	private static final int INDEX_DELTA_VIEW = 2;

	private static final long TIMESTAMP_NOT_INITIALIZED = -1;
	private static final int INDEX_NOT_INITIALIZED = -1;

	private static final int DELTA_TIMESTAMP_START = 0;
	private static final int DELTA_TIMESTAMP_END = 1;

	private JTabbedPane mTabbedPane;

	private int mMaximumTimestampIndex;
	private int mMinimumTimestampIndex;

	private long mNewestTime;
	private long mOldestTime;
	private SortedMap<Long, Long> mChangesMap;

	private JLabel mLiveViewTimestampLabel;
	private JLabel mLiveViewFirstTimestampLabel;
	private JLabel mLiveViewRestUrlLabel;

	private long mHistoryViewSelectedTimestamp;
	private int mHistoryViewSelectedTimestampIndex;
	private JSpinner mHistoryViewSpinner;
	private JButton mHistoryViewForwardButton;
	private JButton mHistoryViewBackwardButton;
	private JSlider mHistoryViewSlider;
	private JLabel mHistoryViewSelectedTimestampLabel;
	private JLabel mHistoryViewSelectedTimestampRestUrlLabel;
	private ActionListener mHistoryViewBackwardButtonActionListener;
	private ActionListener mHistoryViewForwardButtonActionListener;
	private ChangeListener mHistoryViewSliderChangeListener;

	private ChangeListener mTabbedPaneChangeListener;

	private long mDeltaViewSelectedStartTimestamp;
	private int mDeltaViewSelectedStartTimestampIndex;
	private long mDeltaViewSelectedEndTimestamp;
	private int mDeltaViewSelectedEndTimestampIndex;
	private JLabel mDeltaViewSelectedStartTimestampLabel;
	private JLabel mDeltaViewSelectedEndTimestampLabel;
	private JLabel mDeltaViewRestUrlLabel;
	private JButton mDeltaViewStartBackwardButton;
	private JButton mDeltaViewStartForwardButton;
	private JButton mDeltaViewEndBackwardButton;
	private JButton mDeltaViewEndForwardButton;
	private JButton mDeltaViewIntervalForwardButton;
	private JButton mDeltaViewIntervalBackwardButton;
	private int mChangesMapSize;
	private ActionListener mDeltaViewStartForwardButtonActionListener;
	private ActionListener mDeltaViewStartBackwardButtonActionListener;
	private ActionListener mDeltaViewEndForwardButtonActionListener;
	private ActionListener mDeltaViewEndBackwardButtonActionListener;
	private ActionListener mDeltaViewIntervalForwardButtonActionListener;
	private ActionListener mDeltaViewIntervalBackwardButtonActionListener;
	private ChangeListener mHistoryViewSpinnerChangeListener;

	public TabBasedHistoryNavigation(TimeHolder timeHolder, String restUrl) {
		// this.setPreferredSize(new Dimension(400, 33));
		// this.setMinimumSize(new Dimension(400, 33));
		mPanel = new JPanel();
		mPanel.setSize(600, 400);

		mDateFormatter = new SimpleDateFormat(TIMEFORMAT);

		mHistoryViewSelectedTimestamp = TIMESTAMP_NOT_INITIALIZED;
		mDeltaViewSelectedStartTimestamp = TIMESTAMP_NOT_INITIALIZED;
		mDeltaViewSelectedEndTimestamp = TIMESTAMP_NOT_INITIALIZED;

		mMinimumTimestampIndex = 0;
		mChangesMapSize = 0;

		mTimeHolder = timeHolder;
		mRestUrl = restUrl;

		mTabbedPane = new JTabbedPane();

		JPanel liveViewPanel = setUpLiveViewPanel();
		JPanel historyViewPanel = setUpHistoryViewPanel();
		JPanel deltaViewPanel = setUpDeltaViewPanel();

		mTabbedPane.insertTab("Live view", null, liveViewPanel, null,
				INDEX_LIVE_VIEW);
		mTabbedPane.insertTab("History view", null, historyViewPanel, null,
				INDEX_HISTORY_VIEW);
		mTabbedPane.insertTab("Delta view", null, deltaViewPanel, null,
				INDEX_DELTA_VIEW);
		mCurrentMode = INDEX_LIVE_VIEW;

		mPanel.add(mTabbedPane);

		// initialize values for the first time and initialize the live view tab
		updateValues();
		switchTabEnableState();
		updateLiveView();

		createListeners();
		addListeners();
		mTimeHolder.addObserver(this);
	}

	private JPanel setUpLiveViewPanel() {
		JPanel liveViewPanel = new JPanel();
		liveViewPanel.setLayout(new BoxLayout(liveViewPanel, BoxLayout.Y_AXIS));

		mLiveViewFirstTimestampLabel = new JLabel();
		mLiveViewFirstTimestampLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mLiveViewFirstTimestampLabel.setText(createLiveViewTimestampLabel(
				"Oldest timestamp", mOldestTime, mMinimumTimestampIndex));
		liveViewPanel.add(mLiveViewFirstTimestampLabel);

		mLiveViewTimestampLabel = new JLabel();
		mLiveViewTimestampLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mLiveViewTimestampLabel.setText(createLiveViewTimestampLabel(
				"Newest timestamp", mNewestTime, mMaximumTimestampIndex));
		liveViewPanel.add(mLiveViewTimestampLabel);

		mLiveViewRestUrlLabel = new JLabel(createRestUrlLabel(INDEX_LIVE_VIEW));
		mLiveViewRestUrlLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mLiveViewRestUrlLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		registerLinkHandler(mLiveViewRestUrlLabel);
		liveViewPanel.add(mLiveViewRestUrlLabel);

		return liveViewPanel;
	}

	private JPanel setUpHistoryViewPanel() {
		JPanel historyViewPanel = new JPanel();
		historyViewPanel.setLayout(new BoxLayout(historyViewPanel,
				BoxLayout.Y_AXIS));

		mHistoryViewSelectedTimestampLabel = new JLabel();
		mHistoryViewSelectedTimestampLabel
				.setAlignmentX(Component.LEFT_ALIGNMENT);
		mHistoryViewSelectedTimestampLabel
				.setText(createHistoryViewSelectedTimestampLabel());
		historyViewPanel.add(mHistoryViewSelectedTimestampLabel);

		mHistoryViewSelectedTimestampRestUrlLabel = new JLabel(
				createRestUrlLabel(INDEX_HISTORY_VIEW));
		mHistoryViewSelectedTimestampRestUrlLabel
				.setAlignmentX(Component.LEFT_ALIGNMENT);
		mHistoryViewSelectedTimestampRestUrlLabel.setCursor(new Cursor(
				Cursor.HAND_CURSOR));
		registerLinkHandler(mHistoryViewSelectedTimestampRestUrlLabel);
		historyViewPanel.add(mHistoryViewSelectedTimestampRestUrlLabel);

		JPanel spinnerPanel = new JPanel();
		spinnerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mHistoryViewSpinner = new JSpinner(new SpinnerNumberModel(0,
				mMinimumTimestampIndex, mMaximumTimestampIndex, 1));
		spinnerPanel.add(mHistoryViewSpinner);
		historyViewPanel.add(spinnerPanel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mHistoryViewBackwardButton = new JButton("previous");
		mHistoryViewForwardButton = new JButton("next");
		buttonPanel.add(mHistoryViewBackwardButton);
		buttonPanel.add(mHistoryViewForwardButton);
		historyViewPanel.add(buttonPanel);

		mHistoryViewSlider = new JSlider(0, 1, 0);
		mHistoryViewSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
		mHistoryViewSlider.setMajorTickSpacing(5);
		mHistoryViewSlider.setMinorTickSpacing(1);
		mHistoryViewSlider.setPaintTicks(true);
		mHistoryViewSlider.setPaintLabels(true);
		mHistoryViewSlider.setSnapToTicks(true);
		historyViewPanel.add(mHistoryViewSlider);

		return historyViewPanel;
	}

	private JPanel setUpDeltaViewPanel() {
		JPanel deltaViewPanel = new JPanel();
		deltaViewPanel
				.setLayout(new BoxLayout(deltaViewPanel, BoxLayout.Y_AXIS));

		mDeltaViewSelectedStartTimestampLabel = new JLabel();
		mDeltaViewSelectedStartTimestampLabel
				.setAlignmentX(Component.LEFT_ALIGNMENT);
		mDeltaViewSelectedStartTimestampLabel
				.setText(createDeltaViewSelectedTimestampLabel(DELTA_TIMESTAMP_START));
		deltaViewPanel.add(mDeltaViewSelectedStartTimestampLabel);

		mDeltaViewSelectedEndTimestampLabel = new JLabel();
		mDeltaViewSelectedEndTimestampLabel
				.setAlignmentX(Component.LEFT_ALIGNMENT);
		mDeltaViewSelectedEndTimestampLabel
				.setText(createDeltaViewSelectedTimestampLabel(DELTA_TIMESTAMP_END));
		deltaViewPanel.add(mDeltaViewSelectedEndTimestampLabel);

		mDeltaViewRestUrlLabel = new JLabel(
				createRestUrlLabel(INDEX_DELTA_VIEW));
		mDeltaViewRestUrlLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mDeltaViewRestUrlLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		registerLinkHandler(mDeltaViewRestUrlLabel);
		deltaViewPanel.add(mDeltaViewRestUrlLabel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		mDeltaViewStartBackwardButton = new JButton("<");
		mDeltaViewStartForwardButton = new JButton(">");
		buttonPanel.add(new JLabel("Start: "));
		buttonPanel.add(mDeltaViewStartBackwardButton);
		buttonPanel.add(mDeltaViewStartForwardButton);

		mDeltaViewEndBackwardButton = new JButton("<");
		mDeltaViewEndForwardButton = new JButton(">");
		buttonPanel.add(new JLabel("End: "));
		buttonPanel.add(mDeltaViewEndBackwardButton);
		buttonPanel.add(mDeltaViewEndForwardButton);
		deltaViewPanel.add(buttonPanel);

		JPanel advancedButtonPanel = new JPanel();
		advancedButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		mDeltaViewIntervalForwardButton = new JButton(">");
		mDeltaViewIntervalBackwardButton = new JButton("<");
		advancedButtonPanel.add(new JLabel("Move interval: "));
		advancedButtonPanel.add(mDeltaViewIntervalBackwardButton);
		advancedButtonPanel.add(mDeltaViewIntervalForwardButton);
		deltaViewPanel.add(advancedButtonPanel);

		return deltaViewPanel;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof TimeHolder) {
			int selectedIndex = mTabbedPane.getSelectedIndex();

			// removeListeners();
			updateValues();
			switchTabEnableState();

			switch (selectedIndex) {
				case INDEX_LIVE_VIEW:
					updateLiveView();
					break;
				case INDEX_HISTORY_VIEW:
					updateHistoryView();
					break;
				case INDEX_DELTA_VIEW:
					updateDeltaView();
					break;
				default:
					break;
			}

			// addListeners();
		}
	}

	private void updateValues() {
		mChangesMap = mTimeHolder.getChangesMap();

		if (mChangesMap != null) {
			int newChangesMapSize = mChangesMap.size();

			if (mChangesMapSize < newChangesMapSize) {
				mChangesMapSize = newChangesMapSize;

				mNewestTime = mChangesMap.lastKey();
				mOldestTime = mChangesMap.firstKey();

				mMaximumTimestampIndex = mChangesMap.size();

				if (mHistoryViewSelectedTimestamp == TIMESTAMP_NOT_INITIALIZED) {
					mHistoryViewSelectedTimestamp = mOldestTime;
				}

				if (mDeltaViewSelectedStartTimestamp == TIMESTAMP_NOT_INITIALIZED) {
					mDeltaViewSelectedStartTimestamp = mOldestTime;
				}

				if (mDeltaViewSelectedEndTimestamp == TIMESTAMP_NOT_INITIALIZED) {
					mDeltaViewSelectedEndTimestamp = mNewestTime;
				}

				mHistoryViewSelectedTimestampIndex = findIndexToTimestamp(
						mHistoryViewSelectedTimestamp, mChangesMap);
				mDeltaViewSelectedStartTimestampIndex = findIndexToTimestamp(
						mDeltaViewSelectedStartTimestamp, mChangesMap);
				mDeltaViewSelectedEndTimestampIndex = findIndexToTimestamp(
						mDeltaViewSelectedEndTimestamp, mChangesMap);
			}
		} else {
			mMaximumTimestampIndex = 0;
			mHistoryViewSelectedTimestampIndex = INDEX_NOT_INITIALIZED;
			mDeltaViewSelectedStartTimestampIndex = INDEX_NOT_INITIALIZED;
			mDeltaViewSelectedEndTimestampIndex = INDEX_NOT_INITIALIZED;
		}
	}

	private void updateLiveView() {
		mTimeHolder.setLiveView(true, false);

		mLiveViewFirstTimestampLabel.setText(createLiveViewTimestampLabel(
				"Oldest timestamp", mOldestTime, mMinimumTimestampIndex + 1));
		mLiveViewTimestampLabel.setText(createLiveViewTimestampLabel(
				"Newest timestamp", mNewestTime, mMaximumTimestampIndex));
	}

	private void updateHistoryView() {
		mTimeHolder.setLiveView(false, false);

		if (mHistoryViewSelectedTimestamp != TIMESTAMP_NOT_INITIALIZED
				&& mHistoryViewSelectedTimestampIndex != INDEX_NOT_INITIALIZED) {
			mHistoryViewSelectedTimestampRestUrlLabel
					.setText(createRestUrlLabel(INDEX_HISTORY_VIEW));

			mHistoryViewSpinner.setModel(new SpinnerNumberModel(
					mHistoryViewSelectedTimestampIndex + 1,
					mMinimumTimestampIndex + 1, mMaximumTimestampIndex, 1));

			mHistoryViewSlider.setMinimum(mMinimumTimestampIndex + 1);
			mHistoryViewSlider.setMaximum(mMaximumTimestampIndex);
			mHistoryViewSlider.setValue(mHistoryViewSelectedTimestampIndex + 1);
			mHistoryViewSlider
					.setLabelTable(createHistoryViewSliderLabelTable());
			mHistoryViewSlider.setEnabled(true);

			if (mHistoryViewSelectedTimestampIndex > mMinimumTimestampIndex) {
				mHistoryViewBackwardButton.setEnabled(true);
			} else {
				mHistoryViewBackwardButton.setEnabled(false);
			}

			if (mHistoryViewSelectedTimestampIndex < (mMaximumTimestampIndex - 1)) {
				mHistoryViewForwardButton.setEnabled(true);
			} else {
				mHistoryViewForwardButton.setEnabled(false);
			}

			mHistoryViewSelectedTimestampLabel
					.setText(createHistoryViewSelectedTimestampLabel());

			mTimeHolder.setDeltaTimeStart(mHistoryViewSelectedTimestamp, false);
			mTimeHolder.setDeltaTimeEnd(mHistoryViewSelectedTimestamp, false);
			mTimeHolder.notifyObservers();
		} else {
			mHistoryViewSlider.setEnabled(false);
			mHistoryViewBackwardButton.setEnabled(false);
			mHistoryViewForwardButton.setEnabled(false);
		}
	}

	private void updateDeltaView() {
		mTimeHolder.setLiveView(false, false);

		if (mDeltaViewSelectedStartTimestamp != TIMESTAMP_NOT_INITIALIZED
				&& mDeltaViewSelectedEndTimestamp != TIMESTAMP_NOT_INITIALIZED
				&& mDeltaViewSelectedStartTimestampIndex != INDEX_NOT_INITIALIZED
				&& mDeltaViewSelectedEndTimestampIndex != INDEX_NOT_INITIALIZED) {
			mDeltaViewRestUrlLabel
					.setText(createRestUrlLabel(INDEX_DELTA_VIEW));

			if (mDeltaViewSelectedStartTimestampIndex < (mDeltaViewSelectedEndTimestampIndex - 1)) {
				mDeltaViewStartForwardButton.setEnabled(true);
			} else {
				mDeltaViewStartForwardButton.setEnabled(false);
			}

			if (mDeltaViewSelectedStartTimestampIndex > mMinimumTimestampIndex) {
				mDeltaViewStartBackwardButton.setEnabled(true);
			} else {
				mDeltaViewStartBackwardButton.setEnabled(false);
			}

			if (mDeltaViewSelectedEndTimestampIndex < (mMaximumTimestampIndex - 1)) {
				mDeltaViewEndForwardButton.setEnabled(true);
			} else {
				mDeltaViewEndForwardButton.setEnabled(false);
			}

			if (mDeltaViewSelectedEndTimestampIndex > (mDeltaViewSelectedStartTimestampIndex + 1)) {
				mDeltaViewEndBackwardButton.setEnabled(true);
			} else {
				mDeltaViewEndBackwardButton.setEnabled(false);
			}

			if (mDeltaViewSelectedStartTimestampIndex == mMinimumTimestampIndex) {
				mDeltaViewIntervalBackwardButton.setEnabled(false);
			} else {
				mDeltaViewIntervalBackwardButton.setEnabled(true);
			}

			if (mDeltaViewSelectedEndTimestampIndex == (mMaximumTimestampIndex - 1)) {
				mDeltaViewIntervalForwardButton.setEnabled(false);
			} else {
				mDeltaViewIntervalForwardButton.setEnabled(true);
			}

			mDeltaViewSelectedStartTimestampLabel
					.setText(createDeltaViewSelectedTimestampLabel(DELTA_TIMESTAMP_START));
			mDeltaViewSelectedEndTimestampLabel
					.setText(createDeltaViewSelectedTimestampLabel(DELTA_TIMESTAMP_END));

			mTimeHolder.setDeltaTimeStart(mDeltaViewSelectedStartTimestamp,
					false);
			mTimeHolder.setDeltaTimeEnd(mDeltaViewSelectedEndTimestamp, false);
			mTimeHolder.notifyObservers();
		} else {
			mDeltaViewStartForwardButton.setEnabled(false);
			mDeltaViewStartBackwardButton.setEnabled(false);
			mDeltaViewEndForwardButton.setEnabled(false);
			mDeltaViewEndBackwardButton.setEnabled(false);
			mDeltaViewIntervalForwardButton.setEnabled(false);
			mDeltaViewIntervalBackwardButton.setEnabled(false);
		}
	}

	private void createListeners() {
		mTabbedPaneChangeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				mCurrentMode = mTabbedPane.getSelectedIndex();

				switch (mCurrentMode) {
					case INDEX_LIVE_VIEW:
						updateLiveView();
						break;
					case INDEX_HISTORY_VIEW:
						updateHistoryView();
						break;
					case INDEX_DELTA_VIEW:
						updateDeltaView();
						break;
					default:
						break;
				}
			}
		};

		createHistoryViewListeners();
		createDeltaViewListeners();
	}

	private void createHistoryViewListeners() {
		mHistoryViewForwardButtonActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				incrementSelectedHistoryTimestamp();
				updateHistoryView();
			}
		};

		mHistoryViewBackwardButtonActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				decrementSelectedHistoryTimestamp();
				updateHistoryView();
			}
		};

		mHistoryViewSliderChangeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (!mHistoryViewSlider.getValueIsAdjusting()) {
					mHistoryViewSelectedTimestampIndex = mHistoryViewSlider
							.getValue() - 1;
					mHistoryViewSelectedTimestamp = findTimestampToIndex(
							mHistoryViewSelectedTimestampIndex, mChangesMap);
					updateHistoryView();
				}
			}
		};

		mHistoryViewSpinnerChangeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				mHistoryViewSelectedTimestampIndex = (int) mHistoryViewSpinner
						.getValue() - 1;
				mHistoryViewSelectedTimestamp = findTimestampToIndex(
						mHistoryViewSelectedTimestampIndex, mChangesMap);
				updateHistoryView();
			}
		};
	}

	private void createDeltaViewListeners() {
		mDeltaViewStartForwardButtonActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				incrementDeltaStartTimestamp();
				updateDeltaView();
			}
		};

		mDeltaViewStartBackwardButtonActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				decrementDeltaStartTimestamp();
				updateDeltaView();
			}
		};

		mDeltaViewEndForwardButtonActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				incrementDeltaEndTimestamp();
				updateDeltaView();
			}
		};

		mDeltaViewEndBackwardButtonActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				decrementDeltaEndTimestamp();
				updateDeltaView();
			}
		};

		mDeltaViewIntervalForwardButtonActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveDeltaIntervalForward();
				updateDeltaView();
			}
		};

		mDeltaViewIntervalBackwardButtonActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveDeltaIntervalBackward();
				updateDeltaView();
			}
		};
	}

	private void addListeners() {
		mTabbedPane.addChangeListener(mTabbedPaneChangeListener);
		mHistoryViewForwardButton
				.addActionListener(mHistoryViewForwardButtonActionListener);
		mHistoryViewBackwardButton
				.addActionListener(mHistoryViewBackwardButtonActionListener);
		mHistoryViewSlider.addChangeListener(mHistoryViewSliderChangeListener);
		mHistoryViewSpinner
				.addChangeListener(mHistoryViewSpinnerChangeListener);

		mDeltaViewStartForwardButton
				.addActionListener(mDeltaViewStartForwardButtonActionListener);
		mDeltaViewStartBackwardButton
				.addActionListener(mDeltaViewStartBackwardButtonActionListener);
		mDeltaViewEndForwardButton
				.addActionListener(mDeltaViewEndForwardButtonActionListener);
		mDeltaViewEndBackwardButton
				.addActionListener(mDeltaViewEndBackwardButtonActionListener);

		mDeltaViewIntervalForwardButton
				.addActionListener(mDeltaViewIntervalForwardButtonActionListener);
		mDeltaViewIntervalBackwardButton
				.addActionListener(mDeltaViewIntervalBackwardButtonActionListener);
	}

	private void removeListeners() {
		mTabbedPane.removeChangeListener(mTabbedPaneChangeListener);

		mHistoryViewForwardButton
				.removeActionListener(mHistoryViewForwardButtonActionListener);
		mHistoryViewBackwardButton
				.removeActionListener(mHistoryViewBackwardButtonActionListener);
		mHistoryViewSlider
				.removeChangeListener(mHistoryViewSliderChangeListener);
		mHistoryViewSpinner
				.removeChangeListener(mHistoryViewSpinnerChangeListener);

		mDeltaViewStartForwardButton
				.removeActionListener(mDeltaViewStartForwardButtonActionListener);
		mDeltaViewStartBackwardButton
				.removeActionListener(mDeltaViewStartBackwardButtonActionListener);
		mDeltaViewEndForwardButton
				.removeActionListener(mDeltaViewEndForwardButtonActionListener);
		mDeltaViewEndBackwardButton
				.removeActionListener(mDeltaViewEndBackwardButtonActionListener);

		mDeltaViewIntervalForwardButton
				.removeActionListener(mDeltaViewIntervalForwardButtonActionListener);
		mDeltaViewIntervalBackwardButton
				.removeActionListener(mDeltaViewIntervalBackwardButtonActionListener);
	}

	private String createLiveViewTimestampLabel(String prefix, long timestamp,
			int index) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<b>");
		sb.append(prefix);
		sb.append(":</b> ");
		sb.append(Long.toString(timestamp));
		sb.append(" (<i>");
		sb.append(mDateFormatter.format(new Date(timestamp)));
		sb.append("</i>), #");
		sb.append(Integer.toString(index));
		sb.append("</html>");
		return sb.toString();
	}

	private String createHistoryViewSelectedTimestampLabel() {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<b>Selected timestamp:</b> ");
		sb.append(Long.toString(mHistoryViewSelectedTimestamp));
		sb.append(" (<i>");
		sb.append(mDateFormatter
				.format(new Date(mHistoryViewSelectedTimestamp)));
		sb.append("</i>), #");
		sb.append(Integer.toString(mHistoryViewSelectedTimestampIndex + 1));
		sb.append(" of ");
		sb.append(Integer.toString(mMaximumTimestampIndex));
		sb.append("</html>");
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private Dictionary<Integer, JLabel> createHistoryViewSliderLabelTable() {
		Dictionary<Integer, JLabel> table;
		if (mMaximumTimestampIndex >= 5) {
			table = mHistoryViewSlider.createStandardLabels(5, 5);
		} else {
			table = new Hashtable<Integer, JLabel>();
		}

		table.put(new Integer(mMinimumTimestampIndex + 1),
				new JLabel(Integer.toString(mMinimumTimestampIndex + 1)));

		if (mMaximumTimestampIndex % 5 != 0) {
			table.put(new Integer(mMaximumTimestampIndex),
					new JLabel(Integer.toString(mMaximumTimestampIndex)));
		}
		return table;
	}

	private String createDeltaViewSelectedTimestampLabel(int mode) {
		String modeText;
		long timestamp;
		int index;

		if (mode == DELTA_TIMESTAMP_START) {
			modeText = "<b>Start";
			timestamp = mDeltaViewSelectedStartTimestamp;
			index = mDeltaViewSelectedStartTimestampIndex;
		} else {
			modeText = "<b>End";
			timestamp = mDeltaViewSelectedEndTimestamp;
			index = mDeltaViewSelectedEndTimestampIndex;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append(modeText);
		sb.append(" timestamp:</b> ");
		sb.append(Long.toString(timestamp));
		sb.append(" (<i>");
		sb.append(mDateFormatter.format(new Date(timestamp)));
		sb.append("</i>), #");
		sb.append(Integer.toString(index + 1));
		sb.append(" of ");
		sb.append(Integer.toString(mMaximumTimestampIndex));
		sb.append("</html>");
		return sb.toString();
	}

	private String createRestUrlLabel(int mode) {
		return "<html> REST url: <a href=\"\">" + createRestCall(mode)
				+ "</a></html>";
	}

	private String createRestCall(int mode) {
		switch (mode) {
			case INDEX_LIVE_VIEW:
				return mRestUrl + "/current";
			case INDEX_HISTORY_VIEW:
				return mRestUrl + "/" + mHistoryViewSelectedTimestamp;
			case INDEX_DELTA_VIEW:
				return mRestUrl + "/" + mDeltaViewSelectedStartTimestamp + "/"
						+ mDeltaViewSelectedEndTimestamp;
			default:
				return null;
		}
	}

	private void registerLinkHandler(JLabel label) {
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					String restCall = createRestCall(mCurrentMode);
					if (restCall != null) {
						URI uri = new URI(restCall);
						Desktop.getDesktop().browse(uri);
					}
				} catch (URISyntaxException | IOException ex) {
					// It looks like there's a problem
				}
			}
		});
	}

	private void switchTabEnableState() {
		if (mChangesMapSize == 0) {
			mTabbedPane.setEnabledAt(INDEX_HISTORY_VIEW, false);
			mTabbedPane.setTitleAt(INDEX_HISTORY_VIEW,
					"<html><font color=gray>History view</font></html>");
			mTabbedPane.setEnabledAt(INDEX_DELTA_VIEW, false);
			mTabbedPane.setTitleAt(INDEX_DELTA_VIEW,
					"<html><font color=gray>Delta view</font></html>");
		} else {
			mTabbedPane.setEnabledAt(INDEX_HISTORY_VIEW, true);
			mTabbedPane.setTitleAt(INDEX_HISTORY_VIEW, "History view");
			mTabbedPane.setEnabledAt(INDEX_DELTA_VIEW, true);
			mTabbedPane.setTitleAt(INDEX_DELTA_VIEW, "Delta view");
		}
	}

	private void incrementSelectedHistoryTimestamp() {
		if (mHistoryViewSelectedTimestampIndex != INDEX_NOT_INITIALIZED
				&& mHistoryViewSelectedTimestampIndex < (mMaximumTimestampIndex - 1)) {
			mHistoryViewSelectedTimestampIndex++;
			mHistoryViewSelectedTimestamp = findTimestampToIndex(
					mHistoryViewSelectedTimestampIndex, mChangesMap);
		}
	}

	private void decrementSelectedHistoryTimestamp() {
		if (mHistoryViewSelectedTimestampIndex != INDEX_NOT_INITIALIZED
				&& mHistoryViewSelectedTimestampIndex > mMinimumTimestampIndex) {
			mHistoryViewSelectedTimestampIndex--;
			mHistoryViewSelectedTimestamp = findTimestampToIndex(
					mHistoryViewSelectedTimestampIndex, mChangesMap);
		}
	}

	private void incrementDeltaStartTimestamp() {
		if (mDeltaViewSelectedStartTimestampIndex != INDEX_NOT_INITIALIZED
				&& mDeltaViewSelectedStartTimestampIndex < (mMaximumTimestampIndex - 1)
				&& mDeltaViewSelectedStartTimestampIndex < (mDeltaViewSelectedEndTimestampIndex - 1)) {
			mDeltaViewSelectedStartTimestampIndex++;
			mDeltaViewSelectedStartTimestamp = findTimestampToIndex(
					mDeltaViewSelectedStartTimestampIndex, mChangesMap);
		}
	}

	private void decrementDeltaStartTimestamp() {
		if (mDeltaViewSelectedStartTimestampIndex != INDEX_NOT_INITIALIZED
				&& mDeltaViewSelectedStartTimestampIndex > mMinimumTimestampIndex) {
			mDeltaViewSelectedStartTimestampIndex--;
			mDeltaViewSelectedStartTimestamp = findTimestampToIndex(
					mDeltaViewSelectedStartTimestampIndex, mChangesMap);
		}
	}

	private void incrementDeltaEndTimestamp() {
		if (mDeltaViewSelectedEndTimestampIndex != INDEX_NOT_INITIALIZED
				&& mDeltaViewSelectedEndTimestampIndex < (mMaximumTimestampIndex - 1)) {
			mDeltaViewSelectedEndTimestampIndex++;
			mDeltaViewSelectedEndTimestamp = findTimestampToIndex(
					mDeltaViewSelectedEndTimestampIndex, mChangesMap);
		}
	}

	private void decrementDeltaEndTimestamp() {
		if (mDeltaViewSelectedEndTimestampIndex != INDEX_NOT_INITIALIZED
				&& mDeltaViewSelectedEndTimestampIndex > mMinimumTimestampIndex
				&& mDeltaViewSelectedEndTimestampIndex > (mDeltaViewSelectedStartTimestampIndex + 1)) {
			mDeltaViewSelectedEndTimestampIndex--;
			mDeltaViewSelectedEndTimestamp = findTimestampToIndex(
					mDeltaViewSelectedEndTimestampIndex, mChangesMap);
		}
	}

	private void moveDeltaIntervalForward() {
		if (mDeltaViewSelectedStartTimestampIndex != INDEX_NOT_INITIALIZED
				&& mDeltaViewSelectedEndTimestampIndex != INDEX_NOT_INITIALIZED
				&& mDeltaViewSelectedEndTimestampIndex < (mMaximumTimestampIndex - 1)) {
			mDeltaViewSelectedStartTimestampIndex++;
			mDeltaViewSelectedEndTimestampIndex++;
			mDeltaViewSelectedStartTimestamp = findTimestampToIndex(
					mDeltaViewSelectedStartTimestampIndex, mChangesMap);
			mDeltaViewSelectedEndTimestamp = findTimestampToIndex(
					mDeltaViewSelectedEndTimestampIndex, mChangesMap);
		}
	}

	private void moveDeltaIntervalBackward() {
		if (mDeltaViewSelectedStartTimestampIndex != INDEX_NOT_INITIALIZED
				&& mDeltaViewSelectedEndTimestampIndex != INDEX_NOT_INITIALIZED
				&& mDeltaViewSelectedStartTimestampIndex > mMinimumTimestampIndex) {
			mDeltaViewSelectedStartTimestampIndex--;
			mDeltaViewSelectedEndTimestampIndex--;
			mDeltaViewSelectedStartTimestamp = findTimestampToIndex(
					mDeltaViewSelectedStartTimestampIndex, mChangesMap);
			mDeltaViewSelectedEndTimestamp = findTimestampToIndex(
					mDeltaViewSelectedEndTimestampIndex, mChangesMap);
		}
	}

	private int findIndexToTimestamp(long timestamp,
			SortedMap<Long, Long> changesMap) {
		int index = 0;

		if (changesMap != null) {
			for (Iterator<Long> iterator = changesMap.keySet().iterator(); iterator
					.hasNext();) {
				Long key = iterator.next();
				if (key == timestamp) {
					return index;
				}
				index++;
			}
		}

		return 0;
	}

	private long findTimestampToIndex(int index,
			SortedMap<Long, Long> changesMap) {
		long timestamp = 0l;

		int i = 0;
		if (changesMap != null) {
			for (Iterator<Long> iterator = changesMap.keySet().iterator(); iterator
					.hasNext();) {
				timestamp = iterator.next();
				if (i == index) {
					return timestamp;
				}
				i++;
			}
		}

		return timestamp;
	}

	@Override
	public JPanel getJPanel() {
		return mPanel;
	}
}
