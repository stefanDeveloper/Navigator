package dhbw.navigator.utility;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Stack;

import dhbw.navigator.models.Edge;
import dhbw.navigator.models.Node;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Map extends Canvas{

	ArrayList<Node> nodeList;
	double xSize = 0;
	double ySize = 0;
	StackPane parent = null;


	public Map(double width, double height, ArrayList<Node> nodeList, boolean route) {
		GraphicsContext graphic = this.getGraphicsContext2D();
		this.nodeList = nodeList;

		if(route) graphic.setStroke(Color.AQUA);

		this.parentProperty().addListener(new ChangeListener<Parent>() {

			@Override
			public void changed(ObservableValue<? extends Parent> ov, Parent oldP, Parent newP) {
				if (newP != null) {
					System.out.println("my parent is " + newP);
					parent = (StackPane)getParent();


					updateGraph();
					parent.heightProperty().addListener(new ChangeListener<Number>() {
						@Override
						public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
							updateGraph();

						}
					});
					parent.widthProperty().addListener(new ChangeListener<Number>() {
						@Override
						public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

							updateGraph();

						}
					});
				}
			}
		});
	}


	void updateGraph()
	{
		if(parent != null) {
			xSize = parent.getWidth();
			ySize = parent.getHeight();
			if(xSize<ySize)
			{
				ySize = xSize;
			}
			else
			{
				xSize = ySize;
			}

			GraphicsContext graphic = this.getGraphicsContext2D();

			graphic.clearRect(0, 0, xSize, ySize);

			graphic.getCanvas().setHeight(ySize);
			graphic.getCanvas().setWidth(xSize);

			graphic.strokeLine(0, 0, xSize / 2, ySize / 2);
			graphic.strokeLine(xSize, 0, xSize / 2, ySize / 2);
			graphic.strokeLine(xSize, ySize, xSize / 2, ySize / 2);
			graphic.strokeLine(0, ySize, xSize / 2, ySize / 2);


			Node firstNode = nodeList.get(0);
			// upper left
			BigDecimal lat1 = firstNode.getLat();
			BigDecimal lon1 = firstNode.getLon();
			// lower right
			BigDecimal lon2 = firstNode.getLon();
			BigDecimal lat2 = firstNode.getLat();

			BigDecimal compareLat;
			BigDecimal compareLon;
			for (Node n : nodeList) {
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
			double latDif = lat2.subtract(lat1).doubleValue(); // Breitenunterschied
			double lonDif = lon2.subtract(lon1).doubleValue(); // Höhenunterschied


			double xMulti = -(-xSize / lonDif);
			double yMulti = -(ySize / latDif);

			double multi = 0;
			if (xMulti < yMulti) {
				multi = xMulti;
			}

			//multi = 500;
			//y = 600;
			int diameter = 10;

			for (Node n : nodeList) {
				if (n.getIsJunction() && false) {
					double nLon = lon1.subtract(n.getLon()).doubleValue() * xMulti + xSize;
					double nLat = n.getLat().subtract(lat1).doubleValue() * yMulti + ySize;
					graphic.fillOval(nLon - diameter / 2, nLat - diameter / 2, diameter, diameter);
				}

				for (Edge e : n.getEdges()) {
					ArrayList<Node> allNodes = e.allNodes();
					for (int i = 0; i < allNodes.size() - 1; i++) {
						Node en = allNodes.get(i);
						Node enNext = allNodes.get(i + 1);
						double lo1 = lon1.subtract(en.getLon()).doubleValue() * xMulti + xSize;
						double la1 = en.getLat().subtract(lat1).doubleValue() * yMulti + ySize;
						double lo2 = lon1.subtract(enNext.getLon()).doubleValue() * xMulti + xSize;
						double la2 = enNext.getLat().subtract(lat1).doubleValue() * yMulti + ySize;
						graphic.strokeLine(lo2, la2, lo1, la1);
					}
				}
			}
		}
	}
}
