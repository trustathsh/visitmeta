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
 * This file is part of visitmeta-visualization, version 0.5.1,
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
package de.hshannover.f4.trust.visitmeta.graphDrawer;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.visitmeta.IfmapStrings;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.util.IdentifierHelper;
import de.hshannover.f4.trust.visitmeta.util.IdentifierWrapper;
import de.hshannover.f4.trust.visitmeta.util.VisualizationConfig;

/**
 * Utility class for creating colors depending on node types.
 * Uses {@link GraphicWrapper} to encapsulate concrete implementations like Piccolo2D.
 *
 * @author Bastian Hellmann
 *
 */
public class ColorHelper {

	private static final Logger LOGGER = Logger.getLogger(ColorHelper.class);

	private static final Properties mConfig = Main.getConfig();

	private static Map<String, Paint> mIdentifierColor = new HashMap<String, Paint>();

	private static Map<String, Color> mIdentifierColorText = new HashMap<String, Color>();

	private static Map<String, Color> mIdentifierColorStroke = new HashMap<String, Color>();

	private static Map<String, Paint> mPublisherColor = new HashMap<String, Paint>();

	private static Map<String, Paint> mPublisherColorText = new HashMap<String, Paint>();

	private static Map<String, Color> mPublisherColorStroke = new HashMap<String, Color>();


	/**
	 * Private constructor, as the the class only contains static methods.
	 */
	private ColorHelper() {
	}

	/**
	 * Create a gradient color depending on the node size.
	 *
	 * @param g
	 *            the wrapper for the graphic object.
	 * @param pColorInside
	 *            the color inside of the gradient.
	 * @param pColorOutside
	 *            the color outside of the gradient.
	 * @return the gradient color.
	 */
	public static Paint createGradientColor(GraphicWrapper g, Color pColorInside,
			Color pColorOutside) {
		LOGGER.trace("Method createGradientColor("
				+ g + ", "
				+ pColorInside + ", " + pColorOutside + ") called.");
		Color[] colors = {pColorInside, pColorOutside};
		float[] dist = {0.0f, 0.5f};
		float radius = (float) g.getWidth();
		Point2D zero = new Point2D.Double(0.0, 0.0);
		Point2D center = g.getCenter2D();
		AffineTransform vTransformation = AffineTransform.getScaleInstance(1.0, // x
				// scaling
				g.getHeight()
						/ g.getWidth() // y scaling
		);
		vTransformation.translate(center.getX(), // x center
				center.getY()
						* (g.getWidth()
								/ g.getHeight()) // y
		// center
		// *
		// invert
		// scaling
		);
		return new RadialGradientPaint(zero, radius, zero, dist, colors,
				RadialGradientPaint.CycleMethod.NO_CYCLE,
				RadialGradientPaint.ColorSpaceType.SRGB, vTransformation);
	}

	/**
	 * Get the color for an identifier node.
	 * Takes into account the typename and whether it is an extended identifier.
	 *
	 * @param g
	 *            the wrapper for the graphic object.
	 * @param identifier
	 *            the identifier object.
	 * @return the color.
	 */
	public static Paint getColor(GraphicWrapper g, Identifier identifier) {
		LOGGER.trace("Method getColor("
				+ g + ", " + identifier + ") called.");

		String typName = identifier.getTypeName();

		if (!mIdentifierColor.containsKey(typName)) {

			String vIdentifierInside = VisualizationConfig.DEFAULT_VALUE_COLOR_IDENTIFIER_INSIDE;
			String vIdentifierOutside = VisualizationConfig.DEFAULT_VALUE_COLOR_IDENTIFIER_OUTSIDE;

			String typeName = identifier.getTypeName();

			if (IfmapStrings.IDENTIFIER_TYPES.contains(typeName)) {
				vIdentifierInside =
						mConfig.getString(VisualizationConfig.KEY_COLOR_IDENTIFIER_PREFIX
								+ typeName + VisualizationConfig.KEY_COLOR_INSIDE_POSTFIX,
								VisualizationConfig.DEFAULT_VALUE_COLOR_IDENTIFIER_INSIDE);
				vIdentifierOutside =
						mConfig.getString(VisualizationConfig.KEY_COLOR_IDENTIFIER_PREFIX
								+ typeName + VisualizationConfig.KEY_COLOR_OUTSIDE_POSTFIX,
								VisualizationConfig.DEFAULT_VALUE_COLOR_IDENTIFIER_OUTSIDE);

				// Special case: extended identifier
				if (typeName.equals(IfmapStrings.IDENTITY_EL_NAME)) {
					IdentifierWrapper wrapper = IdentifierHelper
							.identifier(identifier);
					String type = wrapper.getValueForXpathExpression("@"
							+ IfmapStrings.IDENTITY_ATTR_TYPE);
					if (type != null
							&& type.equals("other")) {
						vIdentifierInside = mConfig.getString(
								VisualizationConfig.KEY_COLOR_IDENTIFIER_EXTENDED_INSIDE,
								VisualizationConfig.DEFAULT_VALUE_COLOR_IDENTIFIER_EXTENDED_INSIDE);
						vIdentifierOutside = mConfig.getString(
								VisualizationConfig.KEY_COLOR_IDENTIFIER_EXTENDED_OUTSIDE,
								VisualizationConfig.DEFAULT_VALUE_COLOR_IDENTIFIER_EXTENDED_OUTSIDE);
					}
				}
			}

			Color vColorInside = Color.decode(vIdentifierInside);
			Color vColorOutside = Color.decode(vIdentifierOutside);
			mIdentifierColor.put(typName, ColorHelper.createGradientColor(g, vColorInside, vColorOutside));
		}

		return mIdentifierColor.get(typName);
	}

	/**
	 * Get the text color for a metadata node.
	 *
	 * @param pPublisher
	 *            the publisher-id of the metadata.
	 * @return the text color.
	 */
	public static Paint getColorText(String pPublisher) {
		LOGGER.trace("Method getColorText(" + pPublisher + ") called.");

		if (!mPublisherColorText.containsKey(pPublisher)) {
			String vDefaultText = mConfig.getString(VisualizationConfig.KEY_COLOR_METADATA_TEXT_DEFAULT,
					VisualizationConfig.DEFAULT_VALUE_COLOR_METADATA_TEXT_DEFAULT);
			String vText = mConfig.getString(VisualizationConfig.KEY_COLOR_PREFIX
					+ pPublisher + VisualizationConfig.KEY_COLOR_TEXT_POSTFIX, vDefaultText);

			mPublisherColorText.put(pPublisher, Color.decode(vText));
		}
		return mPublisherColorText.get(pPublisher);
	}

	/**
	 * Get the text color for an identifier node.
	 * Takes into account the typename and whether it is an extended identifier.
	 *
	 * @param identifier
	 *            the identifier object
	 * @return the text color.
	 */
	public static Color getColorIdentifierText(Identifier identifier) {
		LOGGER.trace("Method getColorIdentifierStroke() called.");
		String vColor = VisualizationConfig.DEFAULT_VALUE_COLOR_IDENTIFIER_TEXT;

		String typeName = identifier.getTypeName();

		if (!mIdentifierColorText.containsKey(typeName)) {

			if (IfmapStrings.IDENTIFIER_TYPES.contains(typeName)) {
				vColor =
						mConfig.getString(
								VisualizationConfig.KEY_COLOR_IDENTIFIER_PREFIX
										+ typeName + VisualizationConfig.KEY_COLOR_TEXT_POSTFIX,
								VisualizationConfig.DEFAULT_VALUE_COLOR_IDENTIFIER_TEXT);

				// Special case: extended identifier
				if (typeName.equals(IfmapStrings.IDENTITY_EL_NAME)) {
					IdentifierWrapper wrapper = IdentifierHelper
							.identifier(identifier);
					String type = wrapper.getValueForXpathExpression("@"
							+ IfmapStrings.IDENTITY_ATTR_TYPE);
					if (type != null
							&& type.equals("other")) {
						vColor = mConfig.getString(
								VisualizationConfig.KEY_COLOR_IDENTIFIER_EXTENDED_TEXT,
								VisualizationConfig.DEFAULT_VALUE_COLOR_IDENTIFIER_EXTENDED_TEXT);
					}
				}
			}
			mIdentifierColorText.put(typeName, Color.decode(vColor));
		}

		return mIdentifierColorText.get(typeName);
	}

	/**
	 * Get the stroke color for an identifier node.
	 * Takes into account the typename and whether it is an extended identifier.
	 *
	 * @param identifier
	 *            the identifier object
	 * @return the stroke color.
	 */
	public static Color getColorIdentifierStroke(Identifier identifier) {
		LOGGER.trace("Method getColorIdentifierStroke() called.");
		String vOutside = VisualizationConfig.DEFAULT_VALUE_COLOR_IDENTIFIER_BORDER;

		String typeName = identifier.getTypeName();

		if (!mIdentifierColorStroke.containsKey(typeName)) {

			if (IfmapStrings.IDENTIFIER_TYPES.contains(typeName)) {
				vOutside =
						mConfig.getString(VisualizationConfig.KEY_COLOR_IDENTIFIER_PREFIX
								+ typeName
								+ VisualizationConfig.KEY_COLOR_BORDER_POSTFIX,
								VisualizationConfig.DEFAULT_VALUE_COLOR_IDENTIFIER_BORDER);

				// Special case: extended identifier
				if (typeName.equals(IfmapStrings.IDENTITY_EL_NAME)) {
					IdentifierWrapper wrapper = IdentifierHelper
							.identifier(identifier);
					String type = wrapper.getValueForXpathExpression("@"
							+ IfmapStrings.IDENTITY_ATTR_TYPE);
					if (type != null
							&& type.equals("other")) {
						vOutside = mConfig.getString(
								VisualizationConfig.KEY_COLOR_IDENTIFIER_EXTENDED_BORDER,
								VisualizationConfig.DEFAULT_VALUE_COLOR_IDENTIFIER_EXTENDED_BORDER);
					}
				}
			}
			mIdentifierColorStroke.put(typeName, Color.decode(vOutside));
		}

		return mIdentifierColorStroke.get(typeName);
	}

	/**
	 * Get the stroke color for a metadata node.
	 *
	 * @param pPublisher
	 *            the publisher-id of the metadata.
	 * @return the stroke color.
	 */
	public static Color getColorMetadataStroke(String pPublisher) {
		LOGGER.trace("Method getColorMetadataStroke(" + pPublisher + ") called.");

		if (!mPublisherColorStroke.containsKey(pPublisher)) {
			String vDefaultStroke = mConfig.getString(VisualizationConfig.KEY_COLOR_METADATA_BORDER_DEFAULT,
					VisualizationConfig.DEFAULT_VALUE_COLOR_METADATA_BORDER_DEFAULT);
			String vStroke = mConfig.getString(VisualizationConfig.KEY_COLOR_PREFIX
					+ pPublisher + VisualizationConfig.KEY_COLOR_BORDER_POSTFIX, vDefaultStroke);
			mPublisherColorStroke.put(pPublisher, Color.decode(vStroke));
		}
		return mPublisherColorStroke.get(pPublisher);
	}

	/**
	 * Get the color for a metadata node.
	 *
	 * @param pPublisher
	 *            the publisher of the metadata.
	 * @param g
	 *            the wrapper for the graphic object.
	 * @return the color.
	 */
	public static Paint getColor(String pPublisher, GraphicWrapper g) {
		LOGGER.trace("Method getColor(" + pPublisher + ", " + g + ") called.");

		if (!mPublisherColor.containsKey(pPublisher)) {
			String vDefaultInside = mConfig.getString(VisualizationConfig.KEY_COLOR_METADATA_INSIDE_DEFAULT,
					VisualizationConfig.DEFAULT_VALUE_COLOR_METADATA_INSIDE_DEFAULT);
			String vDefaultOutside = mConfig.getString(VisualizationConfig.KEY_COLOR_METADATA_OUTSIDE_DEFAULT,
					VisualizationConfig.DEFAULT_VALUE_COLOR_METADATA_OUTSIDE_DEFAULT);
			String vInside = mConfig.getString(VisualizationConfig.KEY_COLOR_PREFIX
					+ pPublisher + VisualizationConfig.KEY_COLOR_INSIDE_POSTFIX, vDefaultInside);
			String vOutside = mConfig.getString(VisualizationConfig.KEY_COLOR_PREFIX
					+ pPublisher + VisualizationConfig.KEY_COLOR_OUTSIDE_POSTFIX, vDefaultOutside);
			Color vColorInside = Color.decode(vInside);
			Color vColorOutside = Color.decode(vOutside);

			mPublisherColor.put(pPublisher, ColorHelper.createGradientColor(g, vColorInside, vColorOutside));
		}
		return mPublisherColor.get(pPublisher);
	}

	public static void propertyChanged(String vProperty, String vColor) {
		clearPaintCache();
	}

	private static void clearPaintCache() {
		mIdentifierColor.clear();
		mIdentifierColorStroke.clear();
		mIdentifierColorText.clear();
		mPublisherColor.clear();
		mPublisherColorStroke.clear();
		mPublisherColorText.clear();
	}
}
