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
package de.hshannover.f4.trust.metalyzer.gui.exporter;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.Format;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import de.hshannover.f4.trust.metalyzer.gui.CategoryPanel;
import de.hshannover.f4.trust.metalyzer.gui.MetalyzerGuiController;
import de.hshannover.f4.trust.metalyzer.gui.misc.MessageBox;

/** 
 * Project: Metalyzer
 * Author: Daniel Hülse, Sven Steinbach
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class PdfExporter extends Exporter {

	private static final Logger log = Logger.getLogger(PdfExporter.class);

	private PdfWriter writer = null;
	private Document pdfDocument = null;

	public PdfExporter(MetalyzerGuiController guiController, String exportOutput) {
		super(guiController, exportOutput);
		FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter(
				"PDF File", ".pdf");
		addExportFilter(pdfFilter);
		setDefaultFilter(pdfFilter);
	}

	/**
	 * Exports a single tab with diagram and table
	 * */
	@Override
	public void export(File exportFile, CategoryPanel category) {
		if (exportFile != null) {
			String filename = exportFile.getAbsolutePath();
			try {
				pdfDocument = createPdfDocument(filename);
				pdfDocument.open();
				if(prepareExportData(category)) {
					writePdf();
				}
			} catch (FileNotFoundException | DocumentException e) {
				MessageBox.showErrorDialog("Export Error", e.getMessage());
			} finally {
				if (pdfDocument != null) {
					pdfDocument.close();
				}
				if (writer != null) {
					writer.close();
				}
			}
		}
	}

	private void writePdf() throws DocumentException {
		log.debug("Export called");
		createTitle(pdfDocument, exportTitle);
		pdfDocument.add(Chunk.NEWLINE);
		if (exportCharts != null) {
			for (JFreeChart chart : exportCharts) {
				createChart(pdfDocument, chart, 400, 300);
			}
			if(exportCharts.size() > 1) {
				pdfDocument.newPage();
			}
		}
		if (!exportTimestamp.isEmpty()) {
			createLabel(pdfDocument, "Timestamp: ", exportTimestamp,
					Element.ALIGN_BASELINE);
		}
		if (!exportTimestampFrom.isEmpty() && !exportTimestampTo.isEmpty()) {
			createLabel(pdfDocument, "Intervall: ", "", Element.ALIGN_BASELINE);
			createLabel(pdfDocument, "From: ", exportTimestampFrom,
					Element.ALIGN_BASELINE);
			createLabel(pdfDocument, "To:   ", exportTimestampTo,
					Element.ALIGN_BASELINE);
		}
		if (exportItem != null) {
			if (!exportDesc.isEmpty() && !exportItem.isEmpty()) {
				createLabel(pdfDocument, exportDesc, exportItem,
						Element.ALIGN_BASELINE);
			}
		}
		pdfDocument.add(Chunk.NEWLINE);
		if (exportLabels != null) {
			for (String desc : exportLabels.keySet())
				createLabel(pdfDocument, desc, exportLabels.get(desc),
						Element.ALIGN_BASELINE);
		}
		createTable(pdfDocument);

	}

	/**
	 * Exports all current available diagram and table in tabs
	 * */
	@Override
	public void exportAll(File exportFile, List<CategoryPanel> categories) {
		if (exportFile != null) {
			try {
				String filename = exportFile.getAbsolutePath();
				pdfDocument = createPdfDocument(filename);
				pdfDocument.open();
				
				for (CategoryPanel category : categories) {
					if(prepareExportData(category)) {
						pdfDocument.newPage();
						writePdf();
					}
				}
			} catch (FileNotFoundException | DocumentException e) {
				MessageBox.showErrorDialog("Export Error", e.getMessage());
			} finally {
				if (pdfDocument != null) {
					pdfDocument.close();
				}
				if (writer != null) {
					writer.close();
				}
			}
		}
	}

	private Document createPdfDocument(String filename)
			throws FileNotFoundException, DocumentException {
		Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
		writer = PdfWriter.getInstance(doc, new FileOutputStream(filename));
		HeaderFooter footer = new HeaderFooter(new Phrase("p."), true);
		footer.setBorder(Rectangle.NO_BORDER);
		footer.setAlignment(Element.ALIGN_RIGHT);
		doc.setFooter(footer);

		return doc;
	}

	private void createTitle(Document doc, String text)
			throws DocumentException {
		Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
		Paragraph title = new Paragraph(text, titleFont);
		title.setAlignment(Element.ALIGN_CENTER);
		doc.add(title);
	}

	private void createLabel(Document doc, String desc, String text,
			int alignment) throws DocumentException {
		Font labelFont = new Font(Font.HELVETICA, 12, Font.BOLD);
		Paragraph label = new Paragraph(desc + " " + text, labelFont);
		label.setIndentationLeft(50);
		label.setAlignment(alignment);
		doc.add(label);
	}

	private void createChart(Document doc, JFreeChart chart, int width,
			int height) throws DocumentException {
		PdfContentByte pdfContentByte = writer.getDirectContent();
		PdfTemplate pdfChartTemplate = pdfContentByte.createTemplate(width,
				height);
		Graphics2D chartGraphic = pdfChartTemplate.createGraphics(width,
				height, new DefaultFontMapper());
		Rectangle2D chartRegion = new Rectangle2D.Double(0, 0, width, height);

		chart.draw(chartGraphic, chartRegion);
		chartGraphic.dispose();

		Image chartImage = Image.getInstance(pdfChartTemplate);
		chartImage.setAlignment(Image.MIDDLE);
		doc.add(chartImage);
	}

	private void createTable(Document doc) throws DocumentException {
		if (exportTableData != null) {
			Table table = new Table(exportTableColumns.length);
			for (String column : exportTableColumns) {
				Cell colCell = new Cell(column);
				colCell.setHeader(true);
				table.addCell(colCell);
			}
			table.endHeaders();
			int dataSize = exportTableData.get(0).size();
			int col = 0;
			for (int i = 0; i < dataSize; i++) {
				for (Collection<?> data : exportTableData) {
					List<Object> colList = new ArrayList<Object>(data);
					Object element = colList.get(i);
					if (element == null) {
						Cell cell = new Cell("");
						table.addCell(cell);
					} else {
						Format format = propertyFormats.get(col);
						if (format != null && element instanceof Number) {
							element = format.format(element);
						}
						Cell cell = new Cell(element.toString());
						table.addCell(cell);
					}
					col++;
				}
				col = 0;
			}
			doc.add(table);
		}
	}
}
