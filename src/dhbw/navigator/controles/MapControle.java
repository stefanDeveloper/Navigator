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
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
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

	private ArrayList<Node> nodeList;
	private ArrayList<Node> pathList;
	private Node startNode;
	private Node destinationNode;

	private BorderPane parent = null;
	private double diameter = 10;
	private Canvas mapLayer = new Canvas();
	private Canvas pathLayer = new Canvas();
	private Canvas startMarkLayer = new Canvas();
	private Canvas destinatinMarkLayer = new Canvas();

	// upper left
	private double zoom = 0;
	private double xOffset = 0;
	private double yOffset = 0;
	private double xSize = 0;
	private double ySize = 0;
	private BigDecimal lon2;
	private BigDecimal lat2;
	private BigDecimal lat1;
	private BigDecimal lon1;
	private double xMulti;
	private double yMulti;

	public MapControle()
	{
		//Add mapLayer (map) and pathLayer (path)
		getChildren().addAll(mapLayer, startMarkLayer, destinatinMarkLayer, pathLayer);

		//Set default values for the layer
		pathLayer.getGraphicsContext2D().setStroke(Color.AQUA);
		pathLayer.getGraphicsContext2D().setLineWidth(3);

		startMarkLayer.getGraphicsContext2D().setFill(Color.GREEN);
		destinatinMarkLayer.getGraphicsContext2D().setFill(Color.RED);


		//Listen to a change of the parent object
		this.parentProperty().addListener(new ChangeListener<Parent>() {
			@Override
			public void changed(ObservableValue<? extends Parent> ov, Parent oldP, Parent newP) {
				if (newP != null) {
					//Set new parent
					MapControle.this.parent = (BorderPane)getParent().getParent();
					
					//Update the graph
					updateGraph();
					//Listen to the height and width of the parent
					//and redraw the map
					parent.heightProperty().addListener(new ChangeListener<Number>() {
						@Override
						public void changed(ObservableValue<? extends Number> observable, Number oldValue,
											Number newValue){
							updateGraph();
						}
					});
					parent.widthProperty().addListener(new ChangeListener<Number>() {
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

	public void setStart(Node n){
		startNode = n;
		updateGraph();
	}

	public void setDestinationNode(Node n){
		destinationNode = n;
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
			double lonDif = lon2.subtract(lon1).doubleValue(); // Höhenunterschied



			xMulti = -(-this.xSize / lonDif);
			yMulti = -(this.ySize / latDif);

			double multi = 0;
			//Make sure that the drawing won't overflow the screen
			if (xMulti < yMulti) {
				multi = xMulti;
			}


			clearAndResizeAllLayer(mapLayer, pathLayer, startMarkLayer, destinatinMarkLayer);
			drawLines(nodeList,
					mapLayer,
					false);

			if(pathList != null)
			{
				drawLines(pathList,
						pathLayer,
						true);
			}

			drawPoint(startNode, startMarkLayer);
			drawPoint(destinationNode, destinatinMarkLayer);
		}
	}

	void clearAndResizeAllLayer(Canvas ... l){
		for(Canvas c: l){
			GraphicsContext graphic = c.getGraphicsContext2D();
			graphic.getCanvas().setHeight(this.ySize);
			graphic.getCanvas().setWidth(this.xSize);
			graphic.clearRect(0, 0, xSize, ySize);
		}
	}

	void drawPoint(Node n,
				   Canvas layer)
	{
		if(n!=null){
			double highlightDiameter = diameter * 1.5;
			double nLon = lon1.subtract(n.getLon()).doubleValue() * xMulti + this.xSize;
			double nLat = n.getLat().subtract(lat1).doubleValue() * yMulti + this.ySize;
			layer.getGraphicsContext2D().fillOval(nLon - highlightDiameter / 2,
					nLat - highlightDiameter / 2, highlightDiameter,
					highlightDiameter);
		}
	};
	
	//Draw the lines on the canvas
	void drawLines(ArrayList<Node> nodes,
				   Canvas layer,
				   Boolean highlightPath)
	{
		//Set base settings for the canvas
		GraphicsContext graphic = layer.getGraphicsContext2D();
		
		//Draw all edges
		for (Node n : nodes) {
			double nLat = n.getLat().subtract(lat1).doubleValue() * yMulti + this.ySize;
			double nLon = lon1.subtract(n.getLon()).doubleValue() * xMulti + this.xSize;
			graphic.fillText(n.getName() + " "+n.getShortestDistance(), nLon + 15, nLat);

			if(!highlightPath && -xMulti > 350){
				if(n.getJunctionsCount()>2||-xMulti > 450) //graphic.fillText(n.getName(), nLon + 15, nLat);
				graphic.fillOval(nLon - diameter / 2,
						nLat - diameter / 2,
						diameter,
						diameter);
			}


			//Draw each path of the edge
			for (Edge e : n.getEdges()) {
				ArrayList<Node> allNodes = e.allNodes();

				boolean drawPath = !highlightPath;
				if(highlightPath && (
						(e.getEndNode() == n && nodes.contains(e.getStartNode())
						||(e.getStartNode() == n && nodes.contains(e.getEndNode())
						)
						))){
					drawPath = true;
				}

				if(drawPath){
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
}
