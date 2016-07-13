package dhbw.navigator.controles;

import java.math.BigDecimal;
import java.util.ArrayList;

import dhbw.navigator.models.Edge;
import dhbw.navigator.models.Node;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * MapControle
 * Control that can visualize a Node-Map with a Path
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */


public class MapControle extends StackPane {

	ArrayList<Node> nodeList;
	ArrayList<Node> pathList;
	double xSize = 0;
	double ySize = 0;
	StackPane parent = null;
	int diameter = 5;
	Canvas layer1 = new Canvas();
	Canvas layer2 = new Canvas();

	public MapControle()
	{
		//Add layer1 (map) and layer2 (path)
		getChildren().add(layer1);
		getChildren().add(layer2);
		//Listen to a change of the parent object
		this.parentProperty().addListener(new ChangeListener<Parent>() {
			@Override
			public void changed(ObservableValue<? extends Parent> ov, Parent oldP, Parent newP) {
				if (newP != null) {
					//Set new parent
					MapControle.this.parent = (StackPane) MapControle.this.getParent();
					
					//Update the graph
					MapControle.this.updateGraph();
					//Listen to the height and width of the parent
					//and redraw the map
					MapControle.this.parent.heightProperty().addListener(new ChangeListener<Number>() {
						@Override
						public void changed(ObservableValue<? extends Number> observable, Number oldValue,
											Number newValue){
							updateGraph();

						}
					});
					MapControle.this.parent.widthProperty().addListener(new ChangeListener<Number>() {
						@Override
						public void changed(ObservableValue<? extends Number> observable, Number oldValue,
											Number newValue) {

							updateGraph();
						}
					});
				}
			}
		});
	}
	
	public void setPath(ArrayList<Node> pPathList){
		pathList = pPathList;
		updateGraph();
	}

	public void setOriginMap(ArrayList<Node> pNodeList) {
		nodeList = pNodeList;
		updateGraph();
	}
	
	//Update the graph
	//Calculate how to scale the map
	void updateGraph() {
		if (this.parent != null && nodeList!=null) {
			this.xSize = this.parent.getWidth();
			this.ySize = this.parent.getHeight();
			if (this.xSize < this.ySize) {
				this.ySize = this.xSize;
			} else {
				this.xSize = this.ySize;
			}

			Node firstNode = this.nodeList.get(0);
			// upper left
			BigDecimal lon2;
			BigDecimal lat2;
			BigDecimal lat1;
			BigDecimal lon1;

			lat1 = firstNode.getLat();
			lon1 = firstNode.getLon();
			// lower right

			lon2 = firstNode.getLon();

			lat2 = firstNode.getLat();

			BigDecimal compareLat;
			BigDecimal compareLon;
			//Get the highest left and lowest right coordinate
			for (Node n : this.nodeList) {
				compareLat = n.getLat();
				compareLon = n.getLon();
				if (compareLat.compareTo(lat1) == -1)
					lat1 = compareLat; // compare is smaller
				if (compareLon.compareTo(lon1) == 1)
					lon1 = compareLon; // compare is smaller

				if (compareLat.compareTo(lat2) == 1)
					lat2 = compareLat; // Compare is greater
				if (compareLon.compareTo(lon2) == -1)
					lon2 = compareLon; // compare is greater
			}
			//Calculate the relation between X and Y
			//and calculate the strechting factor to fit the window
			double latDif = lat2.subtract(lat1).doubleValue(); // Breitenunterschied
			double lonDif = lon2.subtract(lon1).doubleValue(); // HÃ¶henunterschied

			double xMulti = -(-this.xSize / lonDif);
			double yMulti = -(this.ySize / latDif);

			double multi = 0;
			//Make sure that the drawing won't overflow the screen
			if (xMulti < yMulti) {
				multi = xMulti;
			}

			drawLines(nodeList,
					layer1,
					lon1,
					lon2,
					lat1,
					lat2,
					xMulti,
					yMulti,
					false);

			if(pathList != null && pathList.size()>0)
			{
				drawLines(pathList,
						layer2,
						lon1,
						lon2,
						lat1,
						lat2,
						xMulti,
						yMulti,
						true);
			}
		}
	}
	
	//Draw the lines on the canvas
	void drawLines(ArrayList<Node> nodes,
				   Canvas layer,
				   BigDecimal lon1,
				   BigDecimal lon2,
				   BigDecimal lat1,
				   BigDecimal lat2,
				   double xMulti,
				   double yMulti,
				   Boolean highlightPath)
	{
		//Set base settings for the canvas
		GraphicsContext graphic = layer.getGraphicsContext2D();
		graphic.getCanvas().setHeight(this.ySize);
		graphic.getCanvas().setWidth(this.xSize);
		graphic.clearRect(0, 0, this.xSize, this.ySize);
		if(highlightPath){
			graphic.setStroke(Color.AQUA);
			graphic.setLineWidth(3);
		}
		
		//Draw all nodes
		for (Node n : nodes) {
			//If it's an highlighted path draw a dot for each junction
			if (n.getIsJunction()&&highlightPath) {
				double nLon = lon1.subtract(n.getLon()).doubleValue() * xMulti + this.xSize;
				double nLat = n.getLat().subtract(lat1).doubleValue() * yMulti + this.ySize;
				graphic.fillOval(nLon - diameter / 2, nLat - diameter / 2, diameter, diameter);
			}
			
			//Draw each path of the edge
			for (Edge e : n.getEdges()) {
				ArrayList<Node> allNodes = e.allNodes();
				for (int i = 0; i < allNodes.size() - 1; i++) {
					Node en = allNodes.get(i);
					Node enNext = allNodes.get(i + 1);
					double lo1 = lon1.subtract(en.getLon()).doubleValue() * xMulti + this.xSize;
					double la1 = en.getLat().subtract(lat1).doubleValue() * yMulti + this.ySize;
					double lo2 = lon1.subtract(enNext.getLon()).doubleValue() * xMulti + this.xSize;
					double la2 = enNext.getLat().subtract(lat1).doubleValue() * yMulti + this.ySize;
					graphic.strokeLine(lo2, la2, lo1, la1);
				}
			}
		}
	}
}
