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
package de.hshannover.f4.trust.visitmeta.graphDrawer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.datawrapper.ExpandedLink;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeIdentifier;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeMetadata;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeType;
import de.hshannover.f4.trust.visitmeta.datawrapper.Position;
import de.hshannover.f4.trust.visitmeta.graphDrawer.edgepainter.EdgePainter;
import de.hshannover.f4.trust.visitmeta.graphDrawer.nodeinformation.identifier.IdentifierInformationStrategy;
import de.hshannover.f4.trust.visitmeta.graphDrawer.nodeinformation.identifier.IdentifierInformationStrategyFactory;
import de.hshannover.f4.trust.visitmeta.graphDrawer.nodeinformation.identifier.IdentifierInformationStrategyType;
import de.hshannover.f4.trust.visitmeta.graphDrawer.nodeinformation.metadata.MetadataInformationStrategy;
import de.hshannover.f4.trust.visitmeta.graphDrawer.nodeinformation.metadata.MetadataInformationStrategyFactory;
import de.hshannover.f4.trust.visitmeta.graphDrawer.nodeinformation.metadata.MetadataInformationStrategyType;
import de.hshannover.f4.trust.visitmeta.graphDrawer.nodepainter.NodePainter;
import de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.ClickEventHandler;
import de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.NodeEventHandler;
import de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.ZoomEventHandler;
import de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.edgerenderer.Piccolo2dEdgeRenderer;
import de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.edgerenderer.Piccolo2dEdgeRendererFactory;
import de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.edgerenderer.Piccolo2dEdgeRendererType;
import de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.noderenderer.Piccolo2dNodeRenderer;
import de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.noderenderer.Piccolo2dNodeRendererFactory;
import de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.noderenderer.Piccolo2dNodeRendererType;
import de.hshannover.f4.trust.visitmeta.gui.GraphConnection;
import de.hshannover.f4.trust.visitmeta.gui.search.SearchAndFilterStrategy;
import de.hshannover.f4.trust.visitmeta.gui.search.Searchable;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;
import de.hshannover.f4.trust.visitmeta.util.MetadataHelper;
import de.hshannover.f4.trust.visitmeta.util.VisualizationConfig;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.activities.PActivity;
import edu.umd.cs.piccolo.activities.PTransformActivity;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolo.util.PUtil;
import edu.umd.cs.piccolox.nodes.PComposite;

/**
 * Implementation of the {@link GraphPanel} and {@link Searchable} interfaces.
 * Creates a Piccolo2D-based panel for drawing a graph. Can also mark nodes
 * based on search terms provided by a {@link SearchAndFilterStrategy}.
 *
 * @author Bastian Hellmann
 *
 */
public class Piccolo2DPanel implements GraphPanel, Searchable {
	private static final Logger LOGGER = Logger.getLogger(Piccolo2DPanel.class);
	private static final Properties mConfig = Main.getConfig();
	private PLayer mLayerNode = null;
	private PLayer mLayerEdge = null;
	private PLayer mLayerGlow = null;
	private PCanvas mPanel = new PCanvas();
	private HashMap<Object, PComposite> mMapNode = new HashMap<>();
	private HashMap<NodeMetadata, ArrayList<PPath>> mMapEdge = new HashMap<>();
	private int mNodeTranslationDuration = 0;

	private double mAreaOffsetX = 0.0;
	private double mAreaOffsetY = 0.0;
	private double mAreaWidth = 650.0;
	private double mAreaHeight = 650.0;
	public double mGlowWidth = 80.0;
	public double mGlowHeight = 40.0;
	private int mFontSize = 20;

	private Color mColorBackground = null;
	private Color mColorNewNode = null;
	private Color mColorDeleteNode = null;

	private List<String> mPublisher = new ArrayList<>();

	private IdentifierInformationStrategy mIdentifierInformationStrategy;
	private MetadataInformationStrategy mMetadataInformationStrategy;

	private Propable mSelectedNode = null;
	private Propable mMouseOverNode = null;

	private String mSearchTerm = "";
	private SearchAndFilterStrategy mSearchAndFilterStrategy = null;

	private List<NodePainter> mNodePainter = new ArrayList<>();
	private List<EdgePainter> mEdgeRenderer = new ArrayList<>();

	private Piccolo2dNodeRenderer mPiccolo2dIdentifierRenderer = null;
	private Piccolo2dNodeRenderer mPiccolo2dMetadataRenderer = null;
	private Piccolo2dEdgeRenderer mPiccolo2dEdgeRenderer = null;

	private boolean mHideSearchMismatches = false;

	public Piccolo2DPanel(GraphConnection connection) {
		mNodeTranslationDuration = connection.getSettingManager()
				.getNodeTranslationDuration();

		mPanel.setDefaultRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
		mPanel.setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
		mPanel.setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
		mLayerNode = mPanel.getLayer();

		mLayerEdge = new PLayer();
		mPanel.getRoot().addChild(0, mLayerEdge);
		mPanel.getCamera().addLayer(0, mLayerEdge);

		mLayerGlow = new PLayer();
		mPanel.getRoot().addChild(0, mLayerGlow);
		mPanel.getCamera().addLayer(0, mLayerGlow);

		mPanel.addMouseListener(new ClickEventHandler(this));
		mPanel.setZoomEventHandler(new ZoomEventHandler(this));
		mPanel.addInputEventListener(new NodeEventHandler(connection, this));

		String vColorBackground = mConfig.getString(VisualizationConfig.KEY_COLOR_BACKGROUND,
				VisualizationConfig.DEFAULT_VALUE_COLOR_BACKGROUND);
		mColorBackground = Color.decode(vColorBackground);
		mPanel.setBackground(mColorBackground);

		String vColorNewNode =
				mConfig.getString(VisualizationConfig.KEY_COLOR_NODE_NEW,
						VisualizationConfig.DEFAULT_VALUE_COLOR_NODE_NEW);
		String vColorDeleteNode = mConfig.getString(VisualizationConfig.KEY_COLOR_NODE_DELETE,
				VisualizationConfig.DEFAULT_VALUE_COLOR_NODE_DELETE);
		mColorNewNode = Color.decode(vColorNewNode);
		mColorDeleteNode = Color.decode(vColorDeleteNode);

		String identifierInformationStyle = mConfig.getString(
				VisualizationConfig.KEY_IDENTIFIER_TEXT_STYLE, VisualizationConfig.DEFAULT_VALUE_IDENTIFIER_TEXT_STYLE);
		IdentifierInformationStrategyType identifierInformationStyleType;
		try {
			identifierInformationStyleType = IdentifierInformationStrategyType
					.valueOf(identifierInformationStyle);
		} catch (IllegalArgumentException e) {
			identifierInformationStyleType = IdentifierInformationStrategyType
					.valueOf(VisualizationConfig.DEFAULT_VALUE_IDENTIFIER_TEXT_STYLE);
		}
		mIdentifierInformationStrategy = IdentifierInformationStrategyFactory
				.create(identifierInformationStyleType);

		String metadataInformationStyle = mConfig.getString(
				VisualizationConfig.KEY_METADATA_TEXT_STYLE, VisualizationConfig.DEFAULT_VALUE_METADATA_TEXT_STYLE);
		MetadataInformationStrategyType metadataInformationStyleType;
		try {
			metadataInformationStyleType = MetadataInformationStrategyType
					.valueOf(metadataInformationStyle);
		} catch (IllegalArgumentException e) {
			metadataInformationStyleType = MetadataInformationStrategyType
					.valueOf(VisualizationConfig.DEFAULT_VALUE_METADATA_TEXT_STYLE);
		}
		mMetadataInformationStrategy = MetadataInformationStrategyFactory
				.create(metadataInformationStyleType);

		mPiccolo2dMetadataRenderer = createPiccolo2dNodeRenderer(VisualizationConfig.KEY_PICCOLO2D_METADATA_NODE_STYLE,
				VisualizationConfig.DEFAULT_VALUE_PICCOLO2D_METADATA_NODE_STYLE);

		mPiccolo2dIdentifierRenderer =
				createPiccolo2dNodeRenderer(VisualizationConfig.KEY_PICCOLO2D_IDENTIFIER_NODE_STYLE,
						VisualizationConfig.DEFAULT_VALUE_PICCOLO2D_IDENTIFIER_NODE_STYLE);

		String edgeRendererStyle = mConfig.getString(VisualizationConfig.KEY_PICCOLO2D_EDGE_STYLE,
				VisualizationConfig.DEFAULT_VALUE_PICCOLO2D_EDGE_STYLE);
		Piccolo2dEdgeRendererType type;
		try {
			type = Piccolo2dEdgeRendererType.valueOf(edgeRendererStyle);
		} catch (IllegalArgumentException e) {
			type =
					Piccolo2dEdgeRendererType
							.valueOf(VisualizationConfig.DEFAULT_VALUE_PICCOLO2D_EDGE_STYLE);
		}
		mPiccolo2dEdgeRenderer = Piccolo2dEdgeRendererFactory.create(type);

	}

	private Piccolo2dNodeRenderer createPiccolo2dNodeRenderer(String key, String defaultValue) {
		String configEntry = mConfig.getString(key, defaultValue);
		Piccolo2dNodeRendererType type;
		try {
			type = Piccolo2dNodeRendererType.valueOf(configEntry);
		} catch (IllegalArgumentException e) {
			type =
					Piccolo2dNodeRendererType
							.valueOf(defaultValue);
		}
		return Piccolo2dNodeRendererFactory.create(type);
	}

	@Override
	public JComponent getPanel() {
		LOGGER.trace("Method getPanel() called.");
		return mPanel;
	}

	@Override
	public synchronized void addIdentifier(NodeIdentifier pNode) {
		LOGGER.trace("Method addIdentifier("
				+ pNode + ") called.");
		if (!mMapNode.containsKey(pNode)) {
			PText vText = new PText(
					mIdentifierInformationStrategy.getText(pNode
							.getIdentifier()));
			vText.setHorizontalAlignment(Component.CENTER_ALIGNMENT);
			vText.setFont(new Font(null, Font.PLAIN, mFontSize));
			vText.setOffset(-0.5F
					* (float) vText.getWidth(), -0.5F
							* (float) vText.getHeight());

			final PPath vNode = mPiccolo2dIdentifierRenderer.createNode(pNode.getIdentifier(), vText);

			/* Composite */
			final PComposite vCom = new PComposite();
			vCom.addChild(vNode);
			vCom.addChild(vText);
			vCom.setOffset( // Set position
					mAreaOffsetX
							+ pNode.getX()
									* mAreaWidth, // x
					mAreaOffsetY
							+ pNode.getY()
									* mAreaHeight // y
			);
			// Add edges to node
			vCom.addAttribute("type", NodeType.IDENTIFIER);
			vCom.addAttribute("position", pNode);
			vCom.addAttribute("data", pNode.getIdentifier());
			vCom.addAttribute("edges", new ArrayList<ArrayList<PPath>>());

			paintIdentifierNode(pNode.getIdentifier(), vNode, vText);

			mMapNode.put(pNode, vCom); // Add node to HashMap.
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					mLayerNode.addChild(vCom); // Add node to layer.
				}
			});
		}
	}

	private synchronized void addMetadata(NodeMetadata pNode) {
		LOGGER.trace("Method addMetadata("
				+ pNode + ") called.");
		if (!mMapNode.containsKey(pNode)) {
			final String vPublisher = MetadataHelper.extractPublisherId(pNode.getMetadata());
			if (!mPublisher.contains(vPublisher)) {
				mPublisher.add(vPublisher);
			}
			/* Text */
			PText vText = new PText(mMetadataInformationStrategy.getText(pNode
					.getMetadata()));
			vText.setHorizontalAlignment(Component.CENTER_ALIGNMENT);
			vText.setFont(new Font(null, Font.PLAIN, mFontSize));
			vText.setOffset(-0.5F
					* (float) vText.getWidth(), -0.5F
							* (float) vText.getHeight());

			/* Rectangle */
			final PPath vNode = mPiccolo2dMetadataRenderer.createNode(pNode.getMetadata(), vText);

			/* Composite */
			final PComposite vCom = new PComposite();
			vCom.addChild(vNode);
			vCom.addChild(vText);
			vCom.setOffset(mAreaOffsetX
					+ pNode.getX()
							* mAreaWidth,
					mAreaOffsetY
							+ pNode.getY()
									* mAreaHeight);
			vCom.addAttribute("type", NodeType.METADATA);
			vCom.addAttribute("publisher", vPublisher);
			vCom.addAttribute("position", pNode);
			vCom.addAttribute("data", pNode.getMetadata());
			vCom.addAttribute("edges", new ArrayList<ArrayList<PPath>>()); // Add
			// edges
			// to
			// node

			paintMetadataNode(pNode.getMetadata(), vNode, vText);
			mMapNode.put(pNode, vCom); // Add node to HashMap.
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					mLayerNode.addChild(vCom); // Add node to layer.
				}
			});
		}
	}

	@Override
	public void addMetadata(NodeIdentifier pIdentifier, NodeMetadata pMetadata) {
		LOGGER.trace("Method addMetadata("
				+ pIdentifier + ", " + pMetadata
				+ ") called.");
		addMetadata(pMetadata);
		addEdge(pMetadata, pIdentifier, pMetadata);
	}

	@Override
	public void addMetadata(ExpandedLink pLink, NodeMetadata pMetadata) {
		LOGGER.trace("Method addMetadata("
				+ pLink + ", " + pMetadata
				+ ") called.");
		addMetadata(pMetadata);
		/* Edges form Identifier to Metadata. */
		addEdge(pMetadata, pLink.getFirst(), pMetadata);
		/* Edges from Metadata to Identifier. */
		addEdge(pMetadata, pMetadata, pLink.getSecond());
	}

	/**
	 * Add an edge to the graph.
	 *
	 * @param pKey
	 *            a key to group the edge.
	 * @param pNodeFirst
	 *            the node where the edge starts.
	 * @param pNodeSecond
	 *            the node where the edge ends.
	 */
	@SuppressWarnings("unchecked")
	private synchronized void addEdge(NodeMetadata pKey, Position pNodeFirst,
			Position pNodeSecond) {
		LOGGER.trace("Method addEdge("
				+ pKey + ", " + pNodeFirst + ", "
				+ pNodeSecond + ") called.");
		PPath vEdge = new PPath();
		PComposite vNodeFirst = mMapNode.get(pNodeFirst);
		PComposite vNodeSecond = mMapNode.get(pNodeSecond);
		/* Add Edge to Node. */
		((ArrayList<PPath>) vNodeFirst.getAttribute("edges")).add(vEdge);
		((ArrayList<PPath>) vNodeSecond.getAttribute("edges")).add(vEdge);
		/* Add Node to Edge. */
		vEdge.addAttribute("nodes", new ArrayList<PComposite>());
		((ArrayList<PComposite>) vEdge.getAttribute("nodes")).add(vNodeFirst);
		((ArrayList<PComposite>) vEdge.getAttribute("nodes")).add(vNodeSecond);
		/* Add edge to layer. */
		mLayerEdge.addChild(vEdge);
		/* Add edge to HashMap. */
		ArrayList<PPath> vEdges = mMapEdge.get(pKey);
		if (vEdges == null) { // Is first entry?
			vEdges = new ArrayList<>();
			mMapEdge.put(pKey, vEdges);
		}
		vEdges.add(vEdge);
		updateEdge(vEdge, pKey);
	}

	@SuppressWarnings("unchecked")
	private void deleteEdge(Object pKey, PPath pEdge) {
		LOGGER.trace("Method deleteEdge("
				+ pKey + ", " + pEdge + ") called.");
		/* Remove edge from layer */
		mLayerEdge.removeChild(pEdge);
		/* Remove edge from node */
		for (PComposite vNode : ((ArrayList<PComposite>) pEdge
				.getAttribute("nodes"))) {
			((ArrayList<PPath>) vNode.getAttribute("edges")).remove(pEdge);
		}
	}

	@Override
	public void updateIdentifier(NodeIdentifier pNode) {
		LOGGER.trace("Method updateIdentifier("
				+ pNode + ") called.");
		/* Update position */
		updatePosition(pNode);
	}

	@Override
	public void updateMetadata(NodeMetadata pNode) {
		LOGGER.trace("Method updateMetadata("
				+ pNode + ") called.");
		/* Update position */
		updatePosition(pNode);
	}

	/**
	 * The the position of a node and its edges.
	 *
	 * @param pNode
	 *            the node in the graph.
	 */
	@SuppressWarnings("unchecked")
	private synchronized void updatePosition(final Position pNode) {
		LOGGER.trace("Method updatePosition("
				+ pNode + ") called.");
		if (!pNode.isInUse()) {
			final double vX = mAreaOffsetX
					+ pNode.getX()
							* mAreaWidth;
			final double vY = mAreaOffsetY
					+ pNode.getY()
							* mAreaHeight;
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (mNodeTranslationDuration > 0) {
						final PComposite vNode = mMapNode.get(pNode);
						if (vNode != null) {
							final PAffineTransform vDestTransform = vNode
									.getTransform();
							vDestTransform.setOffset(vX, vY);
							final PTransformActivity.Target vTarget = new PTransformActivity.Target() {
								@Override
								public void setTransform(
										final AffineTransform pTransform) {
									vNode.setTransform(pTransform);
									synchronized (vNode) {
										PPath vShadow = (PPath) vNode
												.getAttribute("glow");
										if (vShadow != null) {
											vShadow.setTransform(pTransform);
										}
									}
								}

								@Override
								public void getSourceMatrix(
										final double[] aSource) {
									vNode.getTransformReference(true)
											.getMatrix(aSource);
								}
							};
							PActivity vNodeTranslation = new PTransformActivity(
									mNodeTranslationDuration,
									PUtil.DEFAULT_ACTIVITY_STEP_RATE, vTarget,
									vDestTransform) {
								@Override
								protected void activityStep(long time) {
									/* Set position of the node. */
									super.activityStep(time);
									/* Redraw edges. */
									ArrayList<PPath> vEdges = (ArrayList<PPath>) vNode
											.getAttribute("edges");
									for (PPath vEdge : vEdges) {
										updateEdge(vEdge, pNode);
									}
								}
							};
							mMapNode.get(pNode).addAttribute("activity",
									vNodeTranslation);
							/* Add node activity */
							vNode.addActivity(vNodeTranslation);
						}
					} else {
						final PComposite vNode = mMapNode.get(pNode);
						vNode.setOffset(vX, vY);
						ArrayList<PPath> vEdges = (ArrayList<PPath>) vNode
								.getAttribute("edges");
						for (PPath vEdge : vEdges) {
							updateEdge(vEdge, pNode);
						}
					}
				}
			});
		}
	}

	/**
	 * Redraw a edge with the new position on the panel.
	 *
	 * @param pEdge
	 *            the edge to redraw.
	 * @param pNodeMetadata
	 */
	@SuppressWarnings("unchecked")
	public void updateEdge(PPath pEdge, Position pNode) {
		LOGGER.trace("Method updateEdge("
				+ pEdge + ") called.");
		synchronized (pEdge) {
			List<PComposite> nodes = (ArrayList<PComposite>) pEdge.getAttribute("nodes");
			PNode vNode1 = nodes
					.get(0);
			PNode vNode2 = nodes
					.get(1);

			Metadata metadata = null;
			Identifier identifier = null;

			Position p1 = (Position) nodes.get(0).getAttribute("position");
			Position p2 = (Position) nodes.get(1).getAttribute("position");

			if (p1 instanceof NodeMetadata) {
				metadata = ((NodeMetadata) p1).getMetadata();
			} else {
				identifier = ((NodeIdentifier) p1).getIdentifier();
			}

			if (p2 instanceof NodeMetadata) {
				metadata = ((NodeMetadata) p2).getMetadata();
			} else {
				identifier = ((NodeIdentifier) p2).getIdentifier();
			}

			Point2D vStart = vNode1.getFullBoundsReference().getCenter2D();
			Point2D vEnd = vNode2.getFullBoundsReference().getCenter2D();

			pEdge.reset();

			/* Draw edge */
			mPiccolo2dEdgeRenderer.drawEdge(pEdge, vStart, vEnd, metadata, identifier);

			/* Set edge color */
			paintEdge(pEdge);

			pEdge.repaint();
		}
	}

	@Override
	public synchronized void deleteNode(Position pPosition) {
		LOGGER.trace("Method deleteNode("
				+ pPosition + ") called.");
		PComposite vNode = mMapNode.get(pPosition);
		if (vNode != null) {
			/* Delete node from layer */
			mLayerNode.removeChild(vNode);
			/* Remove Shadow */
			PPath vShadow = (PPath) vNode.getAttribute("glow");
			if (vShadow != null) {
				mLayerGlow.removeChild(vShadow);
			}
			/* Metadata */
			if (pPosition instanceof NodeMetadata) {
				/* Delete edges from layer */
				for (PPath vEdge : mMapEdge.get(pPosition)) {
					deleteEdge(pPosition, vEdge);
				}
				/* Delete edge from HashMap */
				mMapEdge.remove(pPosition);
			}
			/* Delete node from HashMap */
			mMapNode.remove(pPosition);
		}
	}

	@Override
	public synchronized void markAsNew(final Position pPosition) {
		LOGGER.trace("Method markAsNew("
				+ pPosition + ") called.");
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				PComposite vNode = mMapNode.get(pPosition);
				if (vNode != null) {
					addGlow(vNode, mColorNewNode);
				} else {
					LOGGER.debug("Coundn't find "
							+ pPosition
							+ " to mark as new.");
				}
			}
		});
	}

	@Override
	public synchronized void markAsDelete(final Position pPosition) {
		LOGGER.trace("Method markAsDelete("
				+ pPosition + ") called.");
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				PComposite vNode = mMapNode.get(pPosition);
				if (vNode != null) {
					addGlow(vNode, mColorDeleteNode);
				} else {
					LOGGER.debug("Coundn't find "
							+ pPosition
							+ " to mark as delete.");
				}
			}
		});
	}

	@Override
	public synchronized void clearHighlight(final Position pPosition) {
		LOGGER.trace("Method clearHighlight("
				+ pPosition + ") called.");
		/* Reset the Stroke of the node */
		final PComposite vNode = mMapNode.get(pPosition);
		if (vNode != null) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					synchronized (vNode) {
						/* Remove Shadow */
						final PPath vShadow = (PPath) vNode
								.getAttribute("glow");
						if (vShadow != null) {
							vNode.addAttribute("glow", null);
							PActivity vFading = vShadow.animateToTransparency(
									0.0f, 500); // TODO
							// Use
							// SettingManager
							PActivity vRemove = new PActivity(0) {
								@Override
								protected void activityStep(long time) {
									mLayerGlow.removeChild(vShadow);
								}
							};
							vRemove.startAfter(vFading);
							vShadow.addActivity(vRemove);
						}
					}
				}
			});
		} else {
			LOGGER.debug("Coundn't find "
					+ pPosition
					+ " to clear its highlights.");
		}
	}

	private void addGlow(PComposite pNode, Color pHighlight) {
		Point2D vPosition = pNode.getOffset();
		PBounds vBound = pNode.getFullBoundsReference();
		float vShadowWidth = (float) (1.1
				* vBound.getWidth()
				+ mGlowWidth);
		float vShadowHeight = (float) (vBound.getHeight()
				+ mGlowHeight);
		PPath vShadow = PPath.createEllipse(-0.5F
				* vShadowWidth, // x
				-0.5F
						* vShadowHeight, // y
				vShadowWidth, // width
				vShadowHeight // height
		);
		vShadow.setOffset((float) (vPosition.getX()), // x
				(float) (vPosition.getY()) // y
		);
		vShadow.setStroke(null);
		Color opaqueHighlight = new Color(pHighlight.getRed(),
				pHighlight.getGreen(), pHighlight.getBlue(), 255);
		Color transparentHighlight = new Color(pHighlight.getRed(),
				pHighlight.getGreen(), pHighlight.getBlue(), 0);
		vShadow.setPaint(ColorHelper.createGradientColor(new Piccolo2DGraphicWrapper(vShadow, null), opaqueHighlight,
				transparentHighlight));
		vShadow.setTransparency(0.0f);
		synchronized (pNode) {
			mLayerGlow.addChild(vShadow);
			pNode.addAttribute("glow", vShadow);
		}
		vShadow.animateToTransparency(1.0f, 500); // TODO Use SettingManager
	}

	@Override
	public synchronized void clearGraph() {
		mLayerEdge.removeAllChildren();
		mLayerNode.removeAllChildren();
		mLayerGlow.removeAllChildren();
		mMapEdge.clear();
		mMapNode.clear();
	}

	/**
	 * Adjust the size of the panel depending on the number and size of labels.
	 */
	@Override
	public synchronized void adjustPanelSize() {
		LOGGER.trace("Method adjustPanelSize() called.");
		int vNumberOfNodes = mMapNode.size();
		int vNumberOfChars = 0;
		if (vNumberOfNodes > 0) {
			for (PComposite vNode : mMapNode.values()) {
				vNumberOfChars += ((PText) vNode.getChild(1)).getText()
						.length();
			}
			double vAverage = vNumberOfChars
					/ vNumberOfNodes;
			double vSize = (Math.sqrt(vNumberOfNodes)
					+ vAverage)
					* 50;
			mAreaHeight = vSize;
			mAreaWidth = vSize;
		}
	}

	/**
	 * Move the camera to the center of the graph.
	 */
	public synchronized void setFocusToCenter() {
		LOGGER.trace("Method setFocusToCenter() called.");
		double xMin = Double.POSITIVE_INFINITY;
		double xMax = Double.NEGATIVE_INFINITY;
		double yMin = Double.POSITIVE_INFINITY;
		double yMax = Double.NEGATIVE_INFINITY;
		if (mMapNode.values().size() > 0) {
			for (PComposite vNode : mMapNode.values()) {
				if (vNode.getOffset().getX() > xMax) {
					xMax = vNode.getOffset().getX();
				}
				if (vNode.getOffset().getX() < xMin) {
					xMin = vNode.getOffset().getX();
				}
				if (vNode.getOffset().getY() > yMax) {
					yMax = vNode.getOffset().getY();
				}
				if (vNode.getOffset().getY() < yMin) {
					yMin = vNode.getOffset().getY();
				}
			}
			int vOffset = 50;
			final PBounds vBounds = new PBounds(xMin
					- vOffset, yMin
							- vOffset,
					xMax
							- xMin + 2
									* vOffset,
					yMax
							- yMin + 2
									* vOffset);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					mPanel.getCamera().setViewBounds(vBounds);
				}
			});
		}
	}

	@Override
	public synchronized void repaintNodes(NodeType pType) {
		LOGGER.trace("Method repaintNodes("
				+ pType + ") called.");
		for (Object key : mMapNode.keySet()) {
			PComposite vCom = mMapNode.get(key);
			PPath vNode = (PPath) vCom.getChild(0);
			PText vText = (PText) vCom.getChild(1);

			if (pType == NodeType.IDENTIFIER) {
				if (vCom.getAttribute("type").equals(pType)) {
					NodeIdentifier i = (NodeIdentifier) key;
					Identifier identifier = i.getIdentifier();
					paintIdentifierNode(identifier, vNode, vText);
				}
			} else if (pType == NodeType.METADATA) {
				if (vCom.getAttribute("type").equals(pType)) {
					NodeMetadata m = (NodeMetadata) key;
					Metadata metadata = m.getMetadata();
					paintMetadataNode(metadata, vNode, vText);
				}
			}
		}
	}

	private void paintMetadataNode(Metadata metadata, PPath vNode, PText vText) {
		GraphicWrapper g = new Piccolo2DGraphicWrapper(vNode, vText);
		for (NodePainter r : mNodePainter) {
			r.paintMetadataNode(metadata, g);
		}
	}

	private void paintIdentifierNode(Identifier identifier, PPath vNode, PText vText) {
		GraphicWrapper g = new Piccolo2DGraphicWrapper(vNode, vText);
		for (NodePainter r : mNodePainter) {
			r.paintIdentifierNode(identifier, g);
		}
	}

	private void paintEdge(PPath pEdge) {
		GraphicWrapper g = new Piccolo2DGraphicWrapper(pEdge, null);
		for (EdgePainter r : mEdgeRenderer) {
			r.paintEdge(g);
		}
	}

	@Override
	public void repaint() {
		mPanel.repaint();
	}

	public double getAreaOffsetX() {
		LOGGER.trace("Method getAreaOffsetX called.");
		return mAreaOffsetX;
	}

	public double getAreaOffsetY() {
		LOGGER.trace("Method getAreaOffsetY called.");
		return mAreaOffsetY;
	}

	public double getAreaWidth() {
		LOGGER.trace("Method getAreaWidth called.");
		return mAreaWidth;
	}

	public double getAreaHeight() {
		LOGGER.trace("Method getAreaHeight called.");
		return mAreaHeight;
	}

	@Override
	public List<String> getPublisher() {
		LOGGER.trace("Method getPublisher called.");
		return mPublisher;
	}

	public void setAreaOffsetX(double pAreaOffsetX) {
		LOGGER.trace("Method setAreaOffsetX("
				+ pAreaOffsetX + ") called.");
		mAreaOffsetX = pAreaOffsetX;
	}

	public void setAreaOffsetY(double pAreaOffsetY) {
		LOGGER.trace("Method setAreaOffsetY("
				+ pAreaOffsetY + ") called.");
		mAreaOffsetY = pAreaOffsetY;
	}

	public void setAreaWidth(double pAreaWidth) {
		LOGGER.trace("Method setAreaWidth("
				+ pAreaWidth + ") called.");
		mAreaWidth = pAreaWidth;
	}

	public void setAreaHeight(double pAreaHeight) {
		LOGGER.trace("Method setAreaHeight("
				+ pAreaHeight + ") called.");
		mAreaHeight = pAreaHeight;
	}

	@Override
	public void setNodeTranslationDuration(int pNodeTranslationDuration) {
		mNodeTranslationDuration = pNodeTranslationDuration;
	}

	@Override
	public void selectNode(Propable propable) {
		LOGGER.trace("Method selectNode("
				+ propable + ") called.");
		if ((propable != null)
				&& (mSelectedNode != propable)) {
			mSelectedNode = propable;
			repaintNodes(NodeType.IDENTIFIER);
			repaintNodes(NodeType.METADATA);
		}
	}

	@Override
	public void unselectNode() {
		LOGGER.trace("Method unselectNode() called.");
		mSelectedNode = null;
		repaintNodes(NodeType.IDENTIFIER);
		repaintNodes(NodeType.METADATA);
	}

	@Override
	public void search(String searchTerm) {
		LOGGER.trace("Search for '"
				+ searchTerm + "'");
		mSearchTerm = searchTerm;

		repaintNodes(NodeType.IDENTIFIER);
		repaintNodes(NodeType.METADATA);
	}

	@Override
	public void setSearchAndFilterStrategy(SearchAndFilterStrategy strategy) {
		this.mSearchAndFilterStrategy = strategy;
	}

	@Override
	public void setHideSearchMismatches(boolean b) {
		this.mHideSearchMismatches = b;

		repaintNodes(NodeType.IDENTIFIER);
		repaintNodes(NodeType.METADATA);
	}

	@Override
	public void addNodePainter(List<NodePainter> nodeRenderer) {
		this.mNodePainter.addAll(nodeRenderer);
	}

	@Override
	public void addEdgeRenderer(List<EdgePainter> edgeRenderer) {
		this.mEdgeRenderer.addAll(edgeRenderer);
	}

	@Override
	public SearchAndFilterStrategy getSearchAndFilterStrategy() {
		return mSearchAndFilterStrategy;
	}

	@Override
	public boolean getHideSearchMismatches() {
		return mHideSearchMismatches;
	}

	@Override
	public String getSearchTerm() {
		return mSearchTerm;
	}

	@Override
	public Propable getSelectedNode() {
		return mSelectedNode;
	}

	@Override
	public void mouseEntered(Propable node) {
		LOGGER.trace("Method mouseEntered("
				+ node + ") called.");
		if ((node != null)
				&& (mMouseOverNode != node)) {
			mMouseOverNode = node;
			repaintNodes(NodeType.IDENTIFIER);
			repaintNodes(NodeType.METADATA);
		}
	}

	@Override
	public void mouseExited() {
		mMouseOverNode = null;
		repaintNodes(NodeType.IDENTIFIER);
		repaintNodes(NodeType.METADATA);
	}

	@Override
	public Propable getMouseOverNode() {
		return mMouseOverNode;
	}
}
