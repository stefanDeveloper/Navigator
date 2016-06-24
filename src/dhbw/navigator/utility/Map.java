package dhbw.navigator.utility;

import dhbw.navigator.models.Edge;
import dhbw.navigator.models.Node;
import javafx.scene.canvas.GraphicsContext;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Map {
	
	public static void drawMap(GraphicsContext graphic, ArrayList<Node> nodeList){
		Node firstNode = nodeList.get(0);
		//lower left
		BigDecimal lat1 = firstNode.getLat();
		BigDecimal lon1 = firstNode.getLon();
		//upper right
		BigDecimal lon2 = firstNode.getLon();
		BigDecimal lat2 = firstNode.getLat();

		BigDecimal compareLat;
		BigDecimal compareLon;
		for(Node n: nodeList)
		{
			compareLat = n.getLat();
			compareLon = n.getLon();
			if(compareLat.compareTo(lat1) ==  -1) lat1 = compareLat; //compare is smaller
			if(compareLat.compareTo(lat2) ==  1) lat2 = compareLat; //Compare is greater
			if(compareLon.compareTo(lon1) == -1) lon1 = compareLon; //compare is smaller
			if(compareLon.compareTo(lon1) == 1) lon2 = compareLon; //compare is greater
		}
		int multi = 500;
		//graphic.strokeLine(40,10,10,40);
		for(Node n: nodeList)
		{
			for (Edge e: n.getEdges())
			{
				ArrayList<Node> allNodes = e.allNodes();
				for (int i = 0; i < allNodes.size()-1; i++) {
					Node en = allNodes.get(i);
					Node enNext = allNodes.get(i+1);
					double lo1 = en.getLon().subtract(lon1).doubleValue();
					double la1 = en.getLat().subtract(lat1).doubleValue();
					double lo2 = enNext.getLon().subtract(lon1).doubleValue();
					double la2 = enNext.getLat().subtract(lat1).doubleValue();
					graphic.strokeLine(lo1*multi,la1*multi,lo2*multi,la2*multi );
				}
			}

		}
	}

}
