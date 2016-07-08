package dhbw.navigator.controles;

import dhbw.navigator.models.Node;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import net.aksingh.owmjapis.DailyForecast;
import net.aksingh.owmjapis.OpenWeatherMap;

import static sun.plugin.javascript.navig.JSType.Image;

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
        iconRow.setMaxHeight(315/weatherLabels.length);
        RowConstraints infoRow = new RowConstraints();
        grid.getRowConstraints().addAll(iconRow, infoRow);
        setPadding(new Insets(10,5,0,5));
        for (int i = 0; i < weatherLabels.length; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setMaxWidth(315/weatherLabels.length);
            col.setPrefWidth(315/weatherLabels.length);
            grid.getColumnConstraints().add(col);

/**
            Image img = new Image("file:images\\png\\bolt.png");

            ImageView image = new ImageView(img);
            image.setPreserveRatio(false);
            image.fitHeightProperty().bind(weatherLabels[i].widthProperty());
            image.fitWidthProperty().bind(weatherLabels[i].widthProperty());
            grid.add(image, i, 0);

            **/
            //weatherLabels[i].setText("TESTTEST");

            grid.add(weatherLabels[i], i, 1);
        }
        HBox dailyWeather = new HBox();
        owm = new OpenWeatherMap(new secrets().WeatherAPI);
        owm.setLang(OpenWeatherMap.Language.GERMAN);
        getChildren().addAll(nodeName, grid);

        setMinHeight(0);
        setActive(false);
    }

    public void setNode(Node node)
    {
        Boolean keyAvailable = !new secrets().WeatherAPI.equals("");
        if(node != null && keyAvailable)
        {
            nodeName.setText(node.getName());
            setActive(true);
            if(false)
            {
                DailyForecast cwd = owm.dailyForecastByCoordinates( node.getLat().floatValue(),node.getLon().floatValue(), new Byte("0"));
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
