package dhbw.navigator.implementation;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import dhbw.navigator.generated.Osm;
import dhbw.navigator.interfaces.IParser;
import dhbw.navigator.models.Edge;
import dhbw.navigator.models.Node;

/**
 * Parser
 * Can parse, Serialize and deserialize nodes
 *
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public class Parser2 implements IParser {
    /**
     * Parse XML-File into OSM-Object
     */
    private Osm parseFile(File pFile) {
        Osm customer = new Osm();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Osm.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            customer = (Osm) jaxbUnmarshaller.unmarshal(pFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Sort Collection to enhance speed in the future
        Collections.sort(customer.getWay(), new Comparator<Osm.Way>() {
            @Override
            public int compare(Osm.Way way2, Osm.Way way1) {
                return ((Long) way1.getNd().get(0).getRef()).compareTo(way2.getNd().get(0).getRef());
            }
        });
        return customer;
    }

    @Override
    public ArrayList<Node> getNodes(String xmlFilePath) {
        File file = new File(xmlFilePath);
        Osm pOsm = parseFile(file);

        Timer timer = new Timer("Get nodes");
        ArrayList<Node> result = extractNodes(pOsm);
        result = mergeNodes(result);
        result = extractWays(pOsm, result);
        for(Node n: result)
        {
            n.setAllIds(null);
        }
        ArrayList<Node> cleared = new ArrayList<>();
        for(Node n: result)
        {
            if(n.getEdges().size()>1){
                n.setAllIds(null);
                cleared.add(n);
            }
        }
        timer.printDuration();
        return result;
    }

    /**
     * Merges duplicated nodes.
     * @param givenNodes
     * @return
     */
    ArrayList<Node> mergeNodes(ArrayList<Node> givenNodes) {
        Timer timer = new Timer("Merging");
        ArrayList<Node> extractedNodes = new ArrayList<>();

        while(!givenNodes.isEmpty()){
            boolean exists = false;
            for(Node en: extractedNodes){
                if(en.isSameNode(givenNodes.get(0)))
                {
                    //Node already exists, add the reference id to the id list of the existing node.
                    en.addDuplicatedNode(givenNodes.get(0));
                    givenNodes.remove(0);
                    exists = true;
                    break;
                }
            }
            if(!exists)
            {
                //Add node to extracted nodes
                extractedNodes.add(givenNodes.get(0));
                //Remove node from the given node list
                givenNodes.remove(0);
            }
        }

        for(Node n: extractedNodes){
            for(Node duplicatedN: n.getAllIds()){
                if(duplicatedN!=n){
                    n.setLon(n.getLon().subtract((n.getLon().subtract(duplicatedN.getLon()))));
                    n.setLat(n.getLat().subtract((n.getLat().subtract(duplicatedN.getLat()))));
                }
            }
        }

        timer.printDuration();
        return extractedNodes;
    }

    /**
     * Finds the edges for all nodes from an osm source object.
     *
     * @param pOsm
     *            Osm source object.
     * @param existingNodes
     *            Nodes to append the edges to.
     * @return The given nodes with the new edges added.
     */
    ArrayList<Node> extractWays(Osm pOsm, ArrayList<Node> existingNodes) {
        System.out.println("Extracting edges started.");

        ArrayList<Edge> edges = new ArrayList<>();
        List<Osm.Way> way = pOsm.getWay();
        long startNodeId;
        long endNodeId;
        int length = way.size();
        ArrayList<Edge> finalEdges = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            Osm.Way w = way.get(i);
            if (w.getTag("highway").equals("motorway")) {
                List<Osm.Way.Nd> nds = w.getNd();
                startNodeId = nds.get(0).getRef();
                endNodeId = nds.get(nds.size() - 1).getRef();
                // Check if way is the start of an edge
                for (Node n : existingNodes) {
                    if (n.isPartOfWay(startNodeId)) {
                        // Way is a start of an edge, create edge
                        Osm.Node endNode = pOsm.getNodeById(endNodeId);
                        Edge edge = new Edge(n, new Node(endNode));

                        // Add the new edge to the node and to the temporary
                        // edge list
                        n.addEdge(edge);
                        if(endNode.doesTagExist("name")){
                            finalEdges.add(edge);
                        }else{
                            edges.add(edge);
                        }
                        // Remove from list
                        way.remove(i);
                        length--;
                        break;
                    }
                }
            }
        }


        boolean somethingRemoved = true;
        int forCounter = 0;
        int addedThisRun = 0;

        while (somethingRemoved) {
            addedThisRun = 0;
            somethingRemoved = false;
            for (int i = 0; i < length; i++) {
                forCounter++;

                List<Osm.Way.Nd> nds = way.get(i).getNd();
                startNodeId = nds.get(0).getRef();

                Edge e = findEdgeByEndId(startNodeId,edges);
                if(e!=null){
                    endNodeId = nds.get(nds.size() - 1).getRef();
                    Osm.Node endNode = pOsm.getNodeById(endNodeId);
                    Node tmpEndNode = new Node(endNode);
                    e.addPart(new Node(endNode));

                    if (tmpEndNode.getName() != null && tmpEndNode.getIsJunction()) {
                        finalEdges.add(e);
                        edges.remove(e);
                    }
                    length--;
                    way.remove(i);
                    somethingRemoved = true;
                    addedThisRun++;
                }

                //TODO debug code
                if(forCounter%10000==0){
                    System.out.println("DEBUG: For durchläufe: " + forCounter
                            + " Restliche ways: " + length
                            + " Kanten: " + edges.size());
                }
            }
            System.out.println("Diesen run entfernt: " + addedThisRun);
        }

        //finalEdges.clear();
        for(Node n: existingNodes){
            for(Node dN: n.getAllIds()){
                Edge e = findEdgeByEndId(dN.getPrimaryId(), finalEdges);
                if(e!=null) {
                    finalEdges.remove(e);
                    e.addPart(n);
                    n.addEdge(e);
                }
            }
        }
        System.out.println("DEBUG: Übrige edges: " + finalEdges.size());


        for (int i = 0; i < finalEdges.size(); i++) {

            Edge e = finalEdges.get(i);
            System.out.println("Nicht verbunden: " + e.getStartNode().getName()
                    + "Letzter Knoten: " + e.getEndNode().getPrimaryId());
            e.getStartNode().getEdges().remove(e);
        }
        finalEdges.clear();
        for (int i = 0; i < existingNodes.size(); i++) {
            Node n = existingNodes.get(i);
            if (n.getEdges().size() == 0) {
                existingNodes.remove(n);
                i--;
            }
        }

        for(Node n: existingNodes){
            for (int i = 0; i < n.getEdges().size(); i++) {
                Edge e = n.getEdges().get(i);
                if(e.getStartNode().equals(e.getEndNode()))
                {
                    n.getEdges().remove(i);
                    i--;
                }
            }
        }

        System.out.println("Extracting edges finished.");
        return existingNodes;
    }

    Edge findEdgeByEndId(long wayStartId, ArrayList<Edge> edges){
        for (Edge e: edges){
            if(e.getEndNode().getPrimaryId()==wayStartId)return  e;
        }
        return null;
    }

    /**
     * Extracts all valid junctions (junctions that have a reference number)
     * from an Osm source. The junctions get saved in a list of nodes,
     * containing only the name, coordinates and reference number.
     *
     * @param pOsm
     *            An osm source object.
     * @return A list of nodes.
     */
    ArrayList<Node> extractNodes(Osm pOsm) {
        ArrayList<Node> result = new ArrayList<Node>();
        for (Osm.Node n : pOsm.getNode()) {
            Object tag = n.getTag("highway");
            // A node has to be a junction and needs a ref to be a valid junction!
            if (tag != null && n.doesTagExist("name") && tag.equals("motorway_junction")) {
                result.add(new Node(n));
            }
        }
        return result;
    }
}
