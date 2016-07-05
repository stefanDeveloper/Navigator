package dhbw.navigator.controles;

import dhbw.navigator.models.Node;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import net.aksingh.owmjapis.DailyForecast;
import net.aksingh.owmjapis.OpenWeatherMap;

/**
 * Created by Konrad Mueller on 01.07.2016.
 */
public class NodeInformationControle extends VBox {

    OpenWeatherMap owm;
    private Label nodeName = new Label();
    private Label [] weatherLabels = {new Label(), new Label(), new Label()};

    public NodeInformationControle()
    {
        GridPane grid = new GridPane();

        RowConstraints iconRow = new RowConstraints();
        RowConstraints infoRow = new RowConstraints();
        grid.getRowConstraints().addAll(iconRow, infoRow);
        setPadding(new Insets(10,5,0,5));
        for (int i = 0; i < weatherLabels.length; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0/weatherLabels.length);
            grid.getColumnConstraints().add(col);
            grid.add(weatherLabels[i], i, 1);
        }
        HBox dailyWeather = new HBox();
        owm = new OpenWeatherMap(new secrets().WeatherAPI);
        //dailyWeather.getChildren().addAll(weatherLabels);
        getChildren().addAll(nodeName, grid);
    }

    public void setNode(Node node)
    {
        Boolean activate = !new secrets().WeatherAPI.equals("");
        this.setVisible(node != null);
        this.setMinWidth(0);
        if(node != null && activate)
        {
            DailyForecast cwd = owm.dailyForecastByCoordinates( node.getLat().floatValue(),node.getLon().floatValue(), new Byte("0"));
            nodeName.setText(node.getName());
            int forecast = 3;
            String [] forecastPrinted = new String[forecast];
            for (int i = 0; i < forecast; i++) {
                DailyForecast.Forecast f = cwd.getForecastInstance(i);
                float temp = f.getTemperatureInstance().getDayTemperature();
                int tmpInC = (int)(((temp - 32)*5)/9);

                int weatherCode = f.getWeatherInstance(0).getWeatherCode();
                String weatherName = f.getWeatherInstance(0).getWeatherName();

                forecastPrinted[i] = "\nTemp.: " + tmpInC + "°C\n" + weatherName + " (" + weatherCode + ")";
            }
            weatherLabels[0].setText("Heute" + forecastPrinted[0]);
            weatherLabels[1].setText("Morgen" + forecastPrinted[1]);
            weatherLabels[2].setText("Übermorgen" + forecastPrinted[2]);
        }
    }
}
