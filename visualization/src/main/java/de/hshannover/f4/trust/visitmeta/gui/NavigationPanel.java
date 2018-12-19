package de.hshannover.f4.trust.visitmeta.gui;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import de.hshannover.f4.trust.visitmeta.graphDrawer.DrawProjection;

/**
 * Created by tietze on 07.09.16.
 */
public class NavigationPanel extends JPanel implements Observer{

	private static final long serialVersionUID = 1033527581043002529L;

	private DrawProjection drawProjection;

    GraphConnection connection;

    public NavigationPanel(GraphConnection connection) {
	    	super(new BorderLayout());
    		this.connection = connection;
    		
    		this.drawProjection = new DrawProjection(500, 500);
    		this.add(drawProjection, BorderLayout.CENTER);
    }

    public NavigationPanel() {
    		super(new BorderLayout());

        this.drawProjection = new DrawProjection(500, 500);
        this.add(drawProjection, BorderLayout.CENTER);
    }

    @Override
    public void updateUI() {
        super.updateUI();
    }

    @Override
    public void update(Observable o, Object arg) {
        updateUI();

    }

    // TODO set/change connection, inform drawprojection to redraw
}
