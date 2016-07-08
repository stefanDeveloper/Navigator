package dhbw.navigator.controles;

import dhbw.navigator.models.Node;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.DailyForecast;
import net.aksingh.owmjapis.OpenWeatherMap;

import java.text.DecimalFormat;

/**
 * Created by Konrad Mueller on 01.07.2016.
 */
public class NodeInformationControle extends VBox {

    OpenWeatherMap owm;
    private Label nodeName = new Label();
    private Label weatherLabel = new Label();
    private Label tempLabel = new Label();
    private ImageView weatherIcon = new ImageView();

    public NodeInformationControle()
    {
        //Define grid
        GridPane grid = new GridPane();

        for (int i = 0; i < 3; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(33.3);
            grid.getRowConstraints().add(row);
        }
        ColumnConstraints columnLeft = new ColumnConstraints();
        ColumnConstraints columnRight = new ColumnConstraints();
        columnLeft.setPercentWidth(66);
        columnRight.setPercentWidth(33);
        grid.getColumnConstraints().addAll(columnLeft, columnRight);
        weatherIcon.setFitWidth(75);
        weatherIcon.setFitHeight(75);

        /**
        //Testdata
        nodeName.setText("TESTNAME");
        weatherLabel.setText("Wetter: Sonnig");
        tempLabel.setText("Temperatur: 99°C");
         **/

        StackPane sp = new StackPane();
        sp.setAlignment(weatherIcon, Pos.CENTER);
        sp.getChildren().add(weatherIcon);



        grid.getChildren().add(nodeName);
        grid.getChildren().add(sp);
        grid.getChildren().add(weatherLabel);
        grid.getChildren().add(tempLabel);
        grid.setColumnIndex(sp, 1);
        grid.setRowIndex(tempLabel, 1);
        grid.setRowIndex(weatherLabel, 2);
        grid.setColumnSpan(tempLabel, 2);
        grid.setColumnSpan(weatherLabel, 2);
        grid.setRowSpan(sp,3);


        getChildren().addAll(grid, new Separator());

        setActive(false);
        owm = new OpenWeatherMap(new secrets().WeatherAPI);
        //setMinHeight(0);
    }

    public void setNode(Node node)
    {
        Boolean keyAvailable = !new secrets().WeatherAPI.equals("");
        if(node != null)
        {
            nodeName.setText(node.getName());
            setActive(true);
            int weatherCode = 650;
            double temperature = 90;
            if(false) {
                CurrentWeather weather = owm.currentWeatherByCoordinates(node.getLat().floatValue(), node.getLon().floatValue());
               weatherCode = weather.getWeatherInstance(0).getWeatherCode();
                temperature = weather.getMainInstance().getTemperature();
            }

                String weatherName = "";
                String fileName = "none";
                if(weatherCode < 300){
                    weatherName = "Gewitter";
                    fileName = "bolt";
                }else if(weatherCode < 500){
                    weatherName = "Nieselregen";
                    fileName = "";
                }else if(weatherCode < 600){
                    weatherName = "Regen";
                    fileName = "raining";
                }else if(weatherCode < 700){
                    weatherName = "Schnee";
                    fileName = "snowflake";
                }else if(weatherCode == 741){
                    weatherName = "Nebel";
                    fileName = "";
                }else if(weatherCode == 800){
                    weatherName = "Klar";
                    fileName = "sunny";
                }else if(weatherCode < 900){
                    weatherName = "Bewölkt";
                    fileName = "clouds";
                }else if(weatherCode == 905){
                    weatherName = "Windig";
                    fileName = "umbrella";
                }else if(weatherCode == 960 || weatherCode == 958){
                    weatherName = "Sturm";
                    fileName = "storm";
                }

                if(fileName!="none")
                {
                    Image image = new Image("file:images/png/"+ fileName +".png");
                    weatherIcon.setImage(image);
                }
                String tmpWeather = "Wetter: " + weatherName;

                temperature = ((temperature - 32)*5)/9;

                DecimalFormat f = new DecimalFormat("##.0");
                String tmpTemperature = "Temperatur: " + f.format(temperature) + "°C";
                weatherLabel.setText(tmpWeather);
                tempLabel.setText(tmpTemperature);

        }
        else
        {
            setActive(false);
        }

    }

    void setActive(boolean isActive)
    {
        if(isActive)
        {
            setMaxHeight(900);
            setVisible(true);
        }else{
            setMaxHeight(0);
            setVisible(false);
        }

    }
}
