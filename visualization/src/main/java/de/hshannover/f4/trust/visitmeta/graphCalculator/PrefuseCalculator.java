package de.hshannover.f4.trust.visitmeta.graphCalculator;

import java.util.Observable;

import de.hshannover.f4.trust.visitmeta.datawrapper.ExpandedLink;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeIdentifier;
import de.hshannover.f4.trust.visitmeta.datawrapper.Position;
import de.hshannover.f4.trust.visitmeta.datawrapper.UpdateContainer;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.util.force.DragForce;
import prefuse.util.force.ForceSimulator;
import prefuse.util.force.NBodyForce;
import prefuse.util.force.RungeKuttaIntegrator;
import prefuse.util.force.SpringForce;

public class PrefuseCalculator implements Calculator {

	private ForceDirectedLayout mForceLayoutAction;

	public PrefuseCalculator() {
		mForceLayoutAction = new ForceDirectedLayout("graph", false);
		ForceSimulator fsim = new ForceSimulator(new RungeKuttaIntegrator());
		float gravConstant = -40f; // the more negative, the more repelling
		float minDistance = 300f; // -1 for always on, the more positive, the
									// more space between nodes
		float theta = 0.3f; // the lower, the more single-node repell
							// calculation
		float drag = 0.01f;
		float springCoeff = 1E-4f; // 1E-4
		fsim.addForce(new NBodyForce(gravConstant, minDistance, theta));
		fsim.addForce(new DragForce(drag));
		fsim.addForce(new SpringForce(springCoeff, 5));
		fsim.getForces()[2].setParameter(SpringForce.SPRING_LENGTH, 150f);
		mForceLayoutAction.setForceSimulator(fsim);
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void calculatePositions() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRemoveNodesLinksMetadatas(UpdateContainer uc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteNode(Position node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteLink(ExpandedLink expandedLink) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearGraph() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNode(Position p, double x, double y, double z, boolean pinNode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void adjustGraphAnew(int iterations) {
		// TODO Auto-generated method stub

	}

	@Override
	public void adjustAllNodes(int iterations) {
		// TODO Auto-generated method stub

	}

	@Override
	public void adjustAllNodes(int iterations, boolean checkPermission, boolean checkPicking) {
		// TODO Auto-generated method stub

	}

	@Override
	public void adjustIdentifierNodes(int iterations) {
		// TODO Auto-generated method stub

	}

	@Override
	public void adjustMetadataNodes(int iterations) {
		// TODO Auto-generated method stub

	}

	@Override
	public void adjustNewNodes(int iterations) {
		// TODO Auto-generated method stub

	}

	@Override
	public void adjustByStrategy(int iterations) {
		// TODO Auto-generated method stub

	}

	@Override
	public void alterLevelOfDetail(NodeIdentifier ni, MetadataCollocation mc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void alterLevelOfDetail(ExpandedLink el, MetadataCollocation mc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void alterLevelOfDetailForAllIdentifiers(MetadataCollocation mc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void alterLevelOfDetailForAllExpandedLinks(MetadataCollocation mc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void alterLevelOfDetailForEntireGraph(MetadataCollocation mc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setIterations(int iterations) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLayoutType(LayoutType layoutType) {
		// TODO Auto-generated method stub

	}

	@Override
	public LayoutType getLayoutType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLayoutForceDirected(double attractionMultiplier, double repulsionMultiplier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLayoutSpring(boolean useIndividualEdgeLength, int dimensionX, int dimensionY, double stretch,
			double forceMultiplier, int repulsionRange) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLayoutBipartite() {
		// TODO Auto-generated method stub

	}

}
