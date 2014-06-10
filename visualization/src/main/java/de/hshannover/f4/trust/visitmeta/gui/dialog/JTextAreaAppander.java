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
 * This file is part of visitmeta visualization, version 0.0.5,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2014 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JTextArea;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

public class JTextAreaAppander implements Appender {

	private static final String mNewline = System.getProperty("line.separator");

	private static final String DEFAULT_DELIMITER = "-";

	private SimpleDateFormat dateFormat;
	private SimpleDateFormat timeFormat;

	private String mName;

	private boolean mClosed;

	private static List<JTextArea> mJtaMain;

	public JTextAreaAppander(){
		timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.GERMANY);
		mJtaMain = new ArrayList<JTextArea>();
	}

	public static void addJTextArea(JTextArea textArea){
		mJtaMain.add(textArea);
		textArea.setText(mJtaMain.get(0).getText());
	}

	public void removeJTextArea(JTextArea textArea){
		mJtaMain.remove(textArea);
	}

	public static void clearJTextAreas(){
		mJtaMain.clear();
	}

	@Override
	public void addFilter(Filter filter) {
	}

	@Override
	public void clearFilters() {
	}

	@Override
	public void close() {
		mClosed = true;
	}

	@Override
	public void doAppend(LoggingEvent event) {
		if(!mClosed){

			Throwable throwable = null;

			if(event.getThrowableInformation() != null){
				throwable = event.getThrowableInformation().getThrowable();
			}

			for(JTextArea textArea: mJtaMain){

				switch (event.getLevel().toInt()) {
				case Level.INFO_INT: textArea.append(toString(event.getLoggerName(), event.getLevel(), event.getMessage().toString(), event.getTimeStamp(), throwable) + mNewline);
				break;
				case Level.ERROR_INT: textArea.append(toString(event.getLoggerName(), event.getLevel(), event.getMessage().toString(), event.getTimeStamp(), throwable) + mNewline);
				break;
				case Level.WARN_INT: textArea.append(toString(event.getLoggerName(), event.getLevel(), event.getMessage().toString(), event.getTimeStamp(), throwable) + mNewline);
				break;

				default:
					break;
				}

				textArea.setCaretPosition(textArea.getDocument().getLength());

			}

		}

	}

	private String toString(String name, Level level, String message, long time, Throwable throwable){
		StringBuilder sb = new StringBuilder();

		sb.append(timeFormat.format(time));

		sb.append(" [");

		if(level != null){
			sb.append(level.toString());
		}else{
			sb.append("LEVEL NULL");
		}

		sb.append("] ");

		if(message != null){
			sb.append(message);
		}

		sb.append(' ');

		if(throwable != null){
			sb.append(throwable.toString());
			//			StackTraceElement[] stackTrace = throwable.getStackTrace();
			//			for (int i = 0; i < stackTrace.length; i++) {
			//				StackTraceElement element = stackTrace[i];
			//				sb.append(mNewline);
			//				sb.append("\tat ");
			//				sb.append(element.toString());
			//			}
		}

		return sb.toString();
	}

	@Override
	public ErrorHandler getErrorHandler() {
		System.out.println("getErrorHandler");
		return null;
	}

	@Override
	public Filter getFilter() {
		System.out.println("getFilter");
		return null;
	}

	@Override
	public Layout getLayout() {
		System.out.println("getLayout");
		return null;
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	@Override
	public void setErrorHandler(ErrorHandler handler) {
	}

	@Override
	public void setLayout(Layout layout) {
	}

	@Override
	public void setName(String name) {
		mName = name;
	}

}
