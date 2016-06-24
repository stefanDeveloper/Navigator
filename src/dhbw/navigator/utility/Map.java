package dhbw.navigator.utility;

import dhbw.navigator.models.Edge;
import dhbw.navigator.models.Node;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Map {
	
	public static void drawMap(GraphicsContext graphic, ArrayList<Node> nodeList){
		Node firstNode = nodeList.get(0);
		//upper left
		BigDecimal lat1 = firstNode.getLat();
		BigDecimal lon1 = firstNode.getLon();
		//lower right
		BigDecimal lon2 = firstNode.getLon();
		BigDecimal lat2 = firstNode.getLat();

		BigDecimal compareLat;
		BigDecimal compareLon;
		for(Node n: nodeList)
		{
			compareLat = n.getLat();
			compareLon = n.getLon();
			if(compareLat.compareTo(lat1) == -1) lat1 = compareLat; //compare is smaller
			if(compareLon.compareTo(lon1) == 1) lon1 = compareLon; //compare is smaller


			if(compareLat.compareTo(lat2) == 1) lat2 = compareLat; //Compare is greater
			if(compareLon.compareTo(lon2) == -1) lon2 = compareLon; //compare is greater
		}
		double latDif = lat2.subtract(lat1).doubleValue(); //Breitenunterschied
		double lonDif = lon2.subtract(lon1).doubleValue(); //Höhenunterschied

		//Fenstergröße
		int xSize = 680;
		int ySize = 720;

		double xMulti = -xSize/lonDif;
		double yMulti = ySize/latDif;

		int multi = 500;
		int y = 600;
		int diameter = 10;

		for(Node n: nodeList)
		{
			double nLon = lon1.subtract(n.getLon()).doubleValue() * xMulti;
			double nLat = n.getLat().subtract(lat1).doubleValue() * yMulti;
			graphic.fillOval(nLon - diameter/2, nLat - diameter/2, diameter , diameter);


			for (Edge e: n.getEdges())
			{
				ArrayList<Node> allNodes = e.allNodes();
				for (int i = 0; i < allNodes.size()-1; i++) {
					Node en = allNodes.get(i);
					Node enNext = allNodes.get(i+1);
					double lo1 = lon1.subtract(en.getLon()).doubleValue()* xMulti;
					double la1 = en.getLat().subtract(lat1).doubleValue() * yMulti;
					double lo2 = lon1.subtract(enNext.getLon()).doubleValue()* xMulti;
					double la2 = enNext.getLat().subtract(lat1).doubleValue()* yMulti;
					graphic.strokeLine(lo2 , la2 ,lo1 ,la1 );
				}
			}

		}
	}

}
