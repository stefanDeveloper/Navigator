package de.dhbw.navigator.implementation;

import de.dhbw.navigator.models.Edge;
import de.dhbw.navigator.models.Node;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * Created by Konrad Mueller on 15.07.2016.
 */
public class Serialiser {

    @SuppressWarnings("unchecked")
    public ArrayList<Node> deserialize(String path) {
        Timer timer = new Timer("Deserilisation");
        ArrayList<Node> nodes;
        try (FileInputStream fin = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fin);){
            Object object = ois.readObject();
            nodes = (ArrayList<Node>)object;
            ois.close();
            timer.printDuration();
            nodes = RestoreNodes(nodes);
            return RestoreNodes(nodes);

        } catch (Exception ex) {
            //ex.printStackTrace();
            return null;
        }

    }

    public ArrayList<Node> RestoreNodes(ArrayList<Node> allNodes){
        for(Node n: allNodes){
            for(Edge e: n.getEdges()){
                if(e.getStartNode()==null) e.setStartNode(FindNodeById(e.getStartNodeId(), allNodes));
                if(e.getEndNode()==null) e.setEndNode(FindNodeById(e.getEndNodeId(), allNodes));
            }
        }
        return allNodes;
    }

    Node FindNodeById(long id, ArrayList<Node>allNodes){
        for(Node n: allNodes){
            if(n.getPrimaryId()==id)
                return n;
        }
        return null;
    }


    public void serialize(ArrayList<Node> nodes, String path) {
        Timer timer = new Timer("Serilisation");


        try {
            File file = new File(path);
            boolean result = false;
            result = Files.deleteIfExists(file.toPath());
            System.out.println("Old file deleted: " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try ( FileOutputStream fout = new FileOutputStream(path);
              ObjectOutputStream oos = new ObjectOutputStream(fout);){
            //Delete old files

            oos.writeObject(nodes);
            oos.close();
            System.out.println("Serialized map");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        timer.printDuration();
    }

}
