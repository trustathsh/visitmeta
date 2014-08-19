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
package de.hshannover.f4.trust.metalyzer.gui.charts.renderer;

import java.awt.Color;
import java.awt.Paint;
import java.util.List;

import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.SortOrder;

public class PieColorRenderer { 
	private Paint[] colors;

	public PieColorRenderer() {
		this.colors = new Paint[0];
	}

	public PieColorRenderer(Paint[] colors) {
		this.colors = colors;
	}
    
	/**
	 * Generates a amount of colors
	 * @param amount the amount of colors that will be generated.
	 * */
	public void generateColors(int amount) {
		float hue = 0f;
		colors = new Paint[amount];
		for(int i = 0; i < amount; i++) {
			hue += 0.11f;
			int rgb = Color.HSBtoRGB(hue, 0.5f, 0.5f);
			colors[i] = new Color(rgb);
		}
	}

    /**
     * Colorizes the section of a pie chart by given dataset
     * @param plot the  pie chart that is colorized
     * @param dataset the data of the pie chart
     * */
    public void setPieSectionPaint(PiePlot plot, DefaultPieDataset dataset)  { 
        List<Comparable> keys = dataset.getKeys();
        int sections = dataset.getItemCount();
        for (int i = 0; i < sections; i++) { 
            plot.setSectionPaint(keys.get(i), this.colors[i % this.colors.length]); 
        } 
    } 
} 
