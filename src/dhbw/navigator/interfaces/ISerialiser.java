package dhbw.navigator.interfaces;

import dhbw.navigator.models.Node;

import java.util.ArrayList;

/**
 * Created by Konrad Mueller on 15.07.2016.
 */
public interface ISerialiser {
    public ArrayList<Node> deserialize(String path);
    public void serialize(ArrayList<Node> nodes, String path);
}
