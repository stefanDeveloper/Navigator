package dhbw.navigator.controles;

import dhbw.navigator.models.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;

/**
 * Created by Konrad Mueller on 01.07.2016.
 */
public class NodeInformationControle extends HBox {

    //OpenWeatherMap owm = new OpenWeatherMap(new secrets().WeatherAPI);

    public void setNode(Node node)
    {
        //CurrentWeather cwd = owm.currentWeatherByCoordinates(node.getLat().floatValue(),node.getLon().floatValue());
        //System.out.println(cwd.getCityName() + " " + node.getName());
    }
}
