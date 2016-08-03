package dhbw.navigator.controles;

import java.text.DecimalFormat;

import dhbw.navigator.models.Node;
import dhbw.navigator.views.UtilityViews;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;

/**
 * NodeInformationControle
 * Shows some informations about the node
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad M�ller, Markus Menrath
 *
 */
public class NodeInformationControle extends VBox {

    GridPane grid = new GridPane();

    OpenWeatherMap owm;
    private Label nodeName = new Label();
    private Label weatherLabel = new Label();
    private Label tempLabel = new Label();
    private ImageView weatherIcon = new ImageView();

    private double height;

    @SuppressWarnings({ "static-access" })
	public NodeInformationControle(double pHeight)
    {
        this.height = pHeight;
        setPadding(new Insets(5,0,0,0));
        setSpacing(3);

        //Define grid
        grid.setPadding(new Insets(0,0,0,5));
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
        weatherIcon.setFitWidth(height);
        weatherIcon.setFitHeight(height);

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


        getChildren().addAll(new Separator(), grid);

        setActive(false);
        owm = new OpenWeatherMap(new secrets().WeatherAPI);
         setMinHeight(0);
        grid.setMinHeight(0);
    }

    public void setNode(Node node)
    {
        @SuppressWarnings("static-access")
		Boolean keyAvailable = !new secrets().WeatherAPI.equals("");
        keyAvailable = false;
        nodeName.setText(node.getName());
        setActive(true);
        int weatherCode = 960;
        double temperature = 90;
        Boolean weatherLoaded = false;
        if(keyAvailable){
            CurrentWeather weather = owm.currentWeatherByCoordinates(node.getLat().floatValue(), node.getLon().floatValue());
            if(weather.getResponseCode()==200)
            {
                weatherCode = weather.getWeatherInstance(0).getWeatherCode();
                temperature = weather.getMainInstance().getTemperature();
                weatherLoaded = true;
            }
            else{
            	//TODO Add alert
                UtilityViews.Error("Request failed, respond code: " + weather.getResponseCode());
            }
        }

        if(weatherLoaded)
        {
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
                try{
                    Image image = new Image("file:images/png/"+ fileName +".png");
                    weatherIcon.setImage(image);
                }catch(Exception ex){
                    System.out.println(ex);
                }
            }
            String tmpWeather = "Wetter: " + weatherName;

            temperature = ((temperature - 32)*5)/9;
            DecimalFormat f = new DecimalFormat("##.0");
            String tmpTemperature = "Temperatur: " + f.format(temperature) + "°C";
            weatherLabel.setText(tmpWeather);
            tempLabel.setText(tmpTemperature);
        }
    }

    public void clearNode()
    {
        setActive(false);
    }

    void setActive(boolean isActive)
    {
        if(isActive)
        {
            setPrefHeight(height);
            grid.setPrefHeight(height - 5);
            setMinHeight(height);

            setVisible(true);

        }else{
            setPrefHeight(0);
            setHeight(0);
            setMinHeight(0);
            grid.setPrefHeight(0);
            setVisible(false);
        }
    }
}
