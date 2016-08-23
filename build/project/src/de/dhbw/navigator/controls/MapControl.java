package de.dhbw.navigator.controls;

import java.util.ArrayList;

import de.dhbw.navigator.models.Edge;
import de.dhbw.navigator.models.Node;
import de.dhbw.navigator.models.NodeCoordinate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * MapControl Control that can visualize a Node-Map with a Path
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public class MapControl extends StackPane {

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

	//Navigation parameters
	private double zoom = 1;
	private double xOffset = 0;
	private double yOffset = 0;
	private double xSize = 0;
	private double ySize = 0;
	private double lat1;
	private double lon1;
	private double xMulti;
	private double yMulti;
	private double xZoomOffset;
	private double yZoomOffset;


	public MapControl() {
		// Add mapLayer (map) and pathLayer (path)
		getChildren().addAll(mapLayer, startMarkLayer, destinatinMarkLayer, pathLayer);
		// Set default values for the layer
		pathLayer.getGraphicsContext2D().setStroke(Color.AQUA);
		pathLayer.getGraphicsContext2D().setLineWidth(3);

		startMarkLayer.getGraphicsContext2D().setFill(Color.GREEN);
		destinatinMarkLayer.getGraphicsContext2D().setFill(Color.RED);

		// Listen to a change of the parent object
		parentProperty().addListener(new ChangeListener<Parent>() {
			@Override
			public void changed(ObservableValue<? extends Parent> ov, Parent oldP, Parent newP) {
				if (newP != null) {
					// Set new parent
					parent = (BorderPane) getParent().getParent();

					// Update the graph
					updateGraph();
					// Listen to the height and width of the parent
					// and redraw the map
					parent.heightProperty().addListener(new ChangeListener<Number>() {
						@Override
						public void changed(ObservableValue<? extends Number> observable, Number oldValue,
								Number newValue) {
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


	/**
	 * Defines how much the map translates with each navigation input.
	 * @return
	 */
	int movement()
	{
		return (int)(getWidth() / 10);
	}

	double zoomVar = 0.2;


	/**
	 * Navigate to the left.
	 */
	public void moveLeft() {
		xOffset += movement();
		updateGraph();
	}

	/**
	 * Navigate to the right.
	 */
	public void moveRight() {
		xOffset -= movement();
		updateGraph();
	}

	/**
	 * Navigate down.
	 */
	public void moveDown() {
		yOffset -= movement();
		updateGraph();
	}

	/**
	 * Navigate up.
	 */
	public void moveUp() {
		yOffset += movement();
		updateGraph();
	}


	/**
	 * Zoom in to the map.
	 */
	public void zoomIn() {
		zoom += zoomVar;
		updateZoom(true);
		updateGraph();
	}


	/**
	 * Zoom out of the map.
	 */
	public void zoomOut() {
		zoom -= zoomVar;
		updateZoom(false);
		updateGraph();
	}

	/**
	 * Set the shown path. Path will be highlighted and drown on top of the origin map.
	 * @param pPathList To be shown path as ArrayList<Node>
	 */
	public void setPath(ArrayList<Node> pPathList) {
		pathList = pPathList;
		updateGraph();
	}

	/**
	 * Set the base map.
	 * @param pNodeList
	 */
	public void setOriginMap(ArrayList<Node> pNodeList) {
		nodeList = pNodeList;
		updateGraph();
	}

	/**
	 * Set the route start point.
	 * @param n Route start Node.
	 */
	public void setStart(Node n) {
		startNode = n;
		updateGraph();
	}

	/**
	 * Set the route end point.
	 * @param n Route end Node.
	 */
	public void setDestinationNode(Node n) {
		destinationNode = n;
		updateGraph();
	}


	/**
	 * Redraw the graph
	 */
	void updateGraph() {

		double lon2;
		double lat2;

		if (parent != null && nodeList != null) {
			xSize = parent.getWidth();
			ySize = parent.getHeight();
			if (xSize < ySize) {
				ySize = xSize;
			} else {
				xSize = ySize;
			}

			Node firstNode = nodeList.get(0);

			lat1 = firstNode.getLat().doubleValue();
			lon1 = firstNode.getLon().doubleValue();
			// lower right

			lon2 = firstNode.getLon().doubleValue();

			lat2 = firstNode.getLat().doubleValue();

			double compareLat;
			double compareLon;
			// Get the highest left and lowest right coordinate
			for (Node n : nodeList) {
				compareLat = n.getLat().doubleValue();
				compareLon = n.getLon().doubleValue();
				if (compareLat < lat1)
					lat1 = compareLat; // compare is smaller
				if (compareLon > lon1)
					lon1 = compareLon; // compare is smaller

				if (compareLat > lat2)
					lat2 = compareLat; // Compare is greater
				if (compareLon < lon2)
					lon2 = compareLon; // compare is greater
			}
			// Calculate the relation between X and Y
			// and calculate the strechting factor to fit the window
			double latDif = lat2 - lat1; // Breitenunterschied
			double lonDif = lon2 - lon1; // Höhenunterschied

			xMulti = -(-xSize / lonDif);
			yMulti = -(ySize / latDif);

			@SuppressWarnings("unused")
			double multi = 0;
			// Make sure that the drawing won't overflow the screen
			if (xMulti < yMulti) {
				multi = xMulti;
			}

			xMulti = xMulti * zoom;
			yMulti = yMulti * zoom;

			//Clear map
			clearAndResizeAllLayer(mapLayer, pathLayer, startMarkLayer, destinatinMarkLayer);
			drawLines(nodeList, mapLayer, false);

			if (pathList != null) {
				drawLines(pathList, pathLayer, true);
			}

			drawPoint(startNode, startMarkLayer);
			drawPoint(destinationNode, destinatinMarkLayer);


		}
	}

	/**
	 * Clear a list of Canvases (Canvass, Canvasses ? I don't know ...)
	 * @param l
	 */
	void clearAndResizeAllLayer(Canvas... l) {
		for (Canvas c : l) {
			GraphicsContext graphic = c.getGraphicsContext2D();
			graphic.getCanvas().setHeight(ySize);
			graphic.getCanvas().setWidth(xSize);
			graphic.clearRect(0, 0, xSize, ySize);
		}
	}

	/**
	 * Draw a point on a layer.
	 * @param n Node to be drawn.
	 * @param layer Canvas the point should be drown on.
	 */
	void drawPoint(Node n, Canvas layer) {
		if (n != null) {
			double highlightDiameter = diameter * 1.5;
			double nLon = calculateX(n.getLon().doubleValue());
			double nLat = calculateY(n.getLat().doubleValue());
			layer.getGraphicsContext2D().fillOval(nLon - highlightDiameter / 2, nLat - highlightDiameter / 2,
					highlightDiameter, highlightDiameter);
		}
	};

	/**
	 * Draw a ArrayList<Node> on a Canvas. Lines can be highlighted.
	 * @param nodes ArrayList<Node> that will be drown on the Canvas.
	 * @param layer Canvas to be drown on.
	 * @param highlightPath Should the path get highlighted.
	 */
	void drawLines(ArrayList<Node> nodes, Canvas layer, Boolean highlightPath) {
		// Set base settings for the canvas
		GraphicsContext graphic = layer.getGraphicsContext2D();

		// Draw all edges
		for (Node n : nodes) {
			double nLon = calculateX(n.getLon().doubleValue());
			double nLat = calculateY(n.getLat().doubleValue());

			if (!highlightPath && -xMulti > 550) {
				if (n.getJunctionsCount() > 2 || -xMulti > 450) // graphic.fillText(n.getName(),
																// nLon + 15,
																// nLat);
					graphic.fillOval(nLon - diameter / 2, nLat - diameter / 2, diameter, diameter);
				graphic.fillText(n.getName() + " " + n.getShortestDistance(), nLon + 15, nLat);
			}

			// Draw each path of the edge
			for (Edge e : n.getEdges()) {
				ArrayList<NodeCoordinate> allNodes = e.allNodes();

				boolean drawPath = !highlightPath;
				if (highlightPath && ((e.getEndNode() == n && nodes.contains(e.getStartNode())
						|| (e.getStartNode() == n && nodes.contains(e.getEndNode()))))) {
					drawPath = true;
				}

				if (drawPath) {
					for (int i = 0; i < allNodes.size() - 1; i++) {
						NodeCoordinate en = allNodes.get(i);
						NodeCoordinate enNext = allNodes.get(i + 1);
						double lo1 = calculateX(en.getLon().doubleValue());
						double la1 = calculateY(en.getLat().doubleValue());
						double lo2 = calculateX(enNext.getLon().doubleValue());
						double la2 = calculateY(enNext.getLat().doubleValue());
						graphic.strokeLine(lo2, la2, lo1, la1);
					}
				}
			}
		}
	}

	/**
	 * Calculate how a x coordinate should be positioned to fit the window.
	 * @param input Unscaled coordinate.
	 * @return Coordinate scaled to fit on the window.
	 */
	double calculateX(double input) {
		return (lon1 - input) * xMulti + xSize + (xOffset * zoom) + xZoomOffset;
	}


	/**
	 * Update the zoom multiplier and fit the x and y coordiantes to the new zoom.
	 * @param add
	 */
	void updateZoom(Boolean add){
		@SuppressWarnings("unused")
		double yAddition = getHeight() * (zoomVar/2);
		double xAddition = getWidth() * (zoomVar/2);
		xZoomOffset += add ? +xAddition : -xAddition;
		yZoomOffset += add ? +xAddition : -xAddition;
	}

	/**
	 * Calculate how a y coordinate should be positioned to fit the window.
	 * @param input Unscaled coordinate.
	 * @return Coordinate scaled to fit on the window.
	 */
	double calculateY(double input) {
		return (input - lat1) * yMulti + ySize + (yOffset * zoom) + yZoomOffset;
	}
}
