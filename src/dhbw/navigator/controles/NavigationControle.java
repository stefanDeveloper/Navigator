package dhbw.navigator.controles;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

import java.util.Stack;

/**
 * Created by Konrad Mueller on 01.08.2016.
 */
public class NavigationControle extends Pane {

    public NavigationControle(MapControle mc){

        GridPane containerGp = new GridPane();
        GridPane gp = new GridPane();
        double spacing = 10;
        for (int i = 0; i < 5; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth((100 - spacing) / 4);
            gp.getColumnConstraints().add(col);
        }
        gp.getColumnConstraints().get(3).setPercentWidth(spacing);
        for (int i = 0; i < 3; i++) {
            RowConstraints ref = new RowConstraints();
            ref.setPercentHeight(100/3);
            gp.getRowConstraints().add(ref);
        }

        Button up = new Button("U");
        Button down = new Button("D");
        Button left = new Button("<");
        Button right = new Button(">");
        Button zoomIn = new Button("+");
        Button zoomOut = new Button("-");

        //Events
        up.setOnAction((event ->{
            mc.moveUp();
        }));
        down.setOnAction((event ->{
            mc.moveDown();
        }));
        left.setOnAction((event ->{
            mc.moveLeft();
        }));
        right.setOnAction((event ->{
            mc.moveRight();
        }));
        zoomIn.setOnAction((event ->{
            mc.zoomIn();
        }));
        zoomOut.setOnAction((event ->{
            mc.zoomOut();
        }));

        int btnSize = 40;
        Button [] allBtns = {up, down, left, right, zoomIn, zoomOut};
        for(Button n: allBtns){
            n.setPrefHeight(btnSize);
            n.setPrefWidth(btnSize);
            n.setMinHeight(btnSize);
            n.setMinWidth(btnSize);
        }
        gp.add(up, 1, 0);
        gp.add(down, 1, 2);
        gp.add(left, 0, 1);
        gp.add(right, 2,1);
        gp.add(zoomIn, 4, 0);
        gp.add(zoomOut, 4, 2);

        for (int i = 0; i < 2; i++) {
            RowConstraints rc = new RowConstraints();
            ColumnConstraints cc = new ColumnConstraints();
            containerGp.getRowConstraints().add(rc);
            containerGp.getColumnConstraints().add(cc);
        }

        getChildren().add(gp);

        gp.setPrefWidth(btnSize*4 + 20);
        gp.setPrefHeight(btnSize*3);
        gp.setMaxWidth(btnSize*4 + 20);
        gp.setMaxHeight(btnSize*3);
        setPadding(new Insets(30));
    }
}
