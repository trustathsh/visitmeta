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
 * This file is part of visitmeta metalyzer, version 0.0.1,
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
package de.hshannover.f4.trust.metalyzer.gui.views;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.Format;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;

/** 
 * Project: Metalyzer
 * Author: Daniel Huelse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class TableView extends View {

	private JTable mTable;
	private DefaultTableModel mTableModel;
	private JScrollPane scrollPane;
	private Map<Integer,Format> mFormatter;

	public TableView() {
		this(0, false, new String[] {});
	}

	public TableView(int rows) {
		this(rows, false, new String[] {});
	}

	public TableView(int rows, String... header) {
		this(rows, false, header);
	}

	public TableView(int rows, final boolean editable, String... header) {
		mFormatter = new HashMap<Integer,Format>();
		
		this.setLayoutAlignment(FlowLayout.CENTER);
		mTableModel = new DefaultTableModel(header, rows) {
			public boolean isCellEditable(int row, int column) {
				return editable;
			}
		};
		mTable = new JTable(mTableModel);
		scrollPane = new JScrollPane(mTable);
		this.add(scrollPane);
	}

	/**
	 * Sets the width and height of the TableViews table
	 * 
	 * @param width
	 *            of the table
	 * @param height
	 *            of the table
	 * @see JScrollPane#setPreferredSize(Dimension)
	 * */
	public void setTableSize(int width, int height) {
		scrollPane.setPreferredSize(new Dimension(width, height));
	}

	/**
	 * Sets the names of the columns
	 * 
	 * @param names
	 * */
	public void setColumnNames(String... names) {
		mTableModel.setColumnIdentifiers(names);
	}

	/**
	 * Sets the data of a column by given index and data, existing data will be
	 * overwritten
	 * 
	 * @param index
	 *            of the column
	 * @param values
	 *            the column values as collection
	 * */
	public void setColumn(int index, Collection<?> values) {
		int rowCount = mTableModel.getRowCount();
		rowCount = Math.max(rowCount, values.size());
		mTableModel.setRowCount(rowCount);

		int r = 0;
		for (Object o : values) {
			mTableModel.setValueAt(o, r, index);
			r++;
		}
	}

	/**
	 * Sets the data of a column by given index and data, existing data will be
	 * overwritten
	 * 
	 * @param index
	 *            of the column
	 * @param values
	 *            the column values as object array
	 * */
	public void setColumn(int index, Object... values) {
		int rowCount = mTableModel.getRowCount();
		rowCount = Math.max(rowCount, values.length);
		mTableModel.setRowCount(rowCount);

		int r = 0;
		for (Object o : values) {
			mTableModel.setValueAt(o, r, index);
			r++;
		}
	}

	/**
	 * Adds a column with given name and its data
	 * 
	 * @param name
	 *            the name of the new column
	 * @param values
	 *            the column values
	 * */
	public void addColumn(String name, Collection<?> values) {
		mTableModel.addColumn(name);

		int rowCount = mTableModel.getRowCount();
		rowCount = Math.max(rowCount, values.size());
		mTableModel.setRowCount(rowCount);

		int r = 0;
		for (Object o : values) {
			mTableModel.setValueAt(o, r, mTableModel.getColumnCount() - 1);
			r++;
		}
	}

	/**
	 * Sets the data of a row by given index and data, existing data will be
	 * overwritten
	 * 
	 * @param index
	 *            of the column
	 * @param values
	 *            the row values as collection
	 * */
	public void setRow(int index, Collection<?> values) {
		int c = 0;
		for (Object o : values) {
			mTableModel.setValueAt(o, index, c);
			c++;
		}
	}

	/**
	 * Sets the data of a row by given index and data, existing data will be
	 * overwritten
	 * 
	 * @param index
	 *            of the column
	 * @param values
	 *            the row values as object array
	 * */
	public void setRow(int index, Object... values) {
		int c = 0;
		for (Object o : values) {
			mTableModel.setValueAt(o, index, c);
			c++;
		}
	}

	/**
	 * Adds a row to the table, with given values
	 * 
	 * @param values
	 *            of the row set as Collection
	 * */
	public void addRow(Collection<?> values) {
		int rowCount = mTableModel.getRowCount();
		mTableModel.setRowCount(rowCount + 1);

		int c = 0;
		for (Object o : values) {
			mTableModel.setValueAt(o, mTableModel.getRowCount() - 1, c);
			c++;
		}
	}

	/**
	 * Adds a row to the table, with given values
	 * 
	 * @param values
	 *            of the row set as object array
	 * */
	public void addRow(Object... values) {
		int rowCount = mTableModel.getRowCount();
		mTableModel.setRowCount(rowCount + 1);

		int c = 0;
		for (Object o : values) {
			mTableModel.setValueAt(o, mTableModel.getRowCount() - 1, c);
			c++;
		}
	}

	/**
	 * Sets all data of the table at once
	 * 
	 * @param values
	 *            the whole table given as a list of collections which represent
	 *            the columns
	 * */
	public void setValues(List<Collection<?>> values) {
		int c = 0;
		int r = 0;

		int rowCount = mTableModel.getRowCount();

		for (Collection<?> data : values) {
			rowCount = Math.max(rowCount, data.size());
			mTableModel.setRowCount(rowCount);
			for (Object o : data) {
				mTableModel.setValueAt(o, r, c);
				r++;
			}
			r = 0;
			c++;
		}
	}

	/**
	 * Returns the whole table as a list of collections
	 * 
	 * @return values
	 * */
	public List<Collection<?>> getValues() {
		List<Collection<?>> values = new ArrayList<Collection<?>>();
		int rowCount = mTableModel.getRowCount();
		int colCount = mTableModel.getColumnCount();

		for (int c = 0; c < colCount; c++) {
			List<Object> col = new ArrayList<Object>();
			for (int r = 0; r < rowCount; r++) {
				col.add(mTableModel.getValueAt(r, c));
			}
			values.add(col);
		}
		return values;
	}

	/**
	 * Formats a specific column, e.g you can set a {@link NumberFormat} for
	 * columns that store integer or double
	 * 
	 * @param columnIndex
	 *            the index of the column
	 * @param format
	 *            the {@link Format} that will be applid to the column
	 * */
	public void setColumnFormat(int columnIndex, Format format) {
		TableColumnModel columnModel = mTable.getColumnModel();
		TableColumn column = columnModel.getColumn(columnIndex);
		mFormatter.put(columnIndex, format);
		column.setCellRenderer(new FormatRenderer(format));
	}
	
	/**
	 * Returns a map which holds the formats of rows
	 * @return format map
	 * */
	public Map<Integer,Format> getColumnFormats() {
		return mFormatter;
	}

	/**
	 * Returns the name of a column by given column index
	 * 
	 * @param index
	 *            of the column
	 * @return name of the column
	 * */
	public String getColumnName(int column) {
		return mTableModel.getColumnName(column);
	}

	/**
	 * Returns an array of all column names
	 * 
	 * @return column names
	 * */
	public String[] getColumnNames() {
		int colCount = mTableModel.getColumnCount();
		String[] colNames = new String[colCount];
		for (int c = 0; c < colCount; c++) {
			colNames[c] = getColumnName(c);
		}
		return colNames;
	}

	/**
	 * Clears all data of the table
	 * */
	public void clearTableView() {
		mTableModel.setRowCount(0);
		mTableModel.fireTableStructureChanged();
	}
}

/**
 * This class renders the table cell with a given format
 * */
class FormatRenderer extends DefaultTableCellRenderer {
	
	private static final Logger log = Logger.getLogger(FormatRenderer.class);
	private Format formatter;

	public FormatRenderer(Format formatter) {
		this.formatter = formatter;
	}

	public void setValue(Object value) {
		try {
			if (value != null) {
				value = formatter.format(value);
			}
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage());
		}
		super.setValue(value);
	}
}
