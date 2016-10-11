package de.hshannover.f4.trust.visitmeta.gui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.hshannover.f4.trust.visitmeta.datawrapper.GraphContainer;
import de.hshannover.f4.trust.visitmeta.graphDrawer.DrawProjection;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by tietze on 07.09.16.
 */
public class NavigationPanel extends JPanel implements Observer{

    private DrawProjection drawProjection;

    GraphConnection connection;

    public NavigationPanel(GraphConnection connection){}

    public NavigationPanel(){
        super(new BorderLayout());

        this.connection = connection;

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

}
