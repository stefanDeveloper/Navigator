package dhbw.navigator.models;

import dhbw.navigator.generated.Osm;

import java.util.ArrayList;

public class Edge {

    private float distance;
    private String label;
    private Node startNode;
    private Node endNode;
    private ArrayList<Node> allNodes = new ArrayList<Node>();

    public Edge(Node startNode, Node endNode)
    {
        setStartNode(startNode);
        setEndNode(startNode);
        allNodes.add(startNode);
        addPart(endNode);
    }

    /**
     * Append a new node to the end of the edge.
     * @param endNode
     */
    public void addPart(Node endNode)
    {
        distance = distance + distFrom(this.endNode, endNode);
        setEndNode(endNode);
        allNodes.add(endNode);
    }

    public ArrayList<Node> allNodes()
    {
        return allNodes;
    }

    /**
     * Calculates the distance between two nodes.
     * @param node1 Node one.
     * @param node2 Node two.
     * @return
     */
    float distFrom(Node node1, Node node2)
    {
        return distance = distance + distFrom(node1.getLat().floatValue(),
                node1.getLon().floatValue(),
                node2.getLat().floatValue(),
                node2.getLon().floatValue());
    }

    /**
     * Calculates the distance between two coordinates.
     * @param lat1  Latitude coordinate one.
     * @param lng1  Longitude coordinate one.
     * @param lat2  Latitude coordinate two.
     * @param lng2  Longitude coordinate two.
     * @return
     */
    float distFrom(float lat1, float lng1, float lat2, float lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);
        return dist / 1000000;
    }


    //Setter and Getter
    public void setDistance(float distance) {
        //Dont allow negative values
        if (distance > 0) {
            this.distance = distance;
        } else {
            //Error
        }
    }

    public float getDistance() {
        return distance;
    }

    public String getLabel() {
        return label;
    }

    public Node getStartNode() {
        return startNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
        if (endNode.getName() != null) {
            label = "[" + getStartNode().getName() +
                    "]-[" +
                    getEndNode().getName() + "]";
        }
    }
    
    public Node getNeighbour(Node n){
		if(this.getEndNode() != n)
		{
			return this.getEndNode();
		}
		else {
			return this.getStartNode();
		}
    }
}
