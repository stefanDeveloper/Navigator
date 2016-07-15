package dhbw.navigator.implementation;

import dhbw.navigator.interfaces.ISerialiser;
import dhbw.navigator.models.Node;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * Created by Konrad Mueller on 15.07.2016.
 */
public class Serialiser implements ISerialiser {

    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<Node> deserialize(String path) {
        Timer timer = new Timer("Deserilisation");
        ArrayList<Node> nodes;
        try {
            FileInputStream fin = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fin);
            Object object = ois.readObject();
            nodes = (ArrayList<Node>)object;
            ois.close();
            timer.printDuration();
            return nodes;

        } catch (Exception ex) {
            //ex.printStackTrace();
            return null;
        }

    }

    @Override
    public void serialize(ArrayList<Node> nodes, String path) {
        Timer timer = new Timer("Serilisation");

        try {
            //Delete old files
            File file = new File(path);
            boolean result = Files.deleteIfExists(file.toPath());
            System.out.println("Old file deleted: " + result);
            FileOutputStream fout = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(nodes);
            oos.close();
            System.out.println("Serialized map");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        timer.printDuration();
    }

}
