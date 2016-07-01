package dhbw.navigator.controles;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class AutoCompleteControle extends HBox {
	private Button clearButton;
	private TextFieldAutoCompleteControle textField;
	private Label label;

	public AutoCompleteControle(String labelText, List<String> context) {
		label = new Label(labelText);
		textField = new TextFieldAutoCompleteControle(context);
		clearButton = new Button("X");
		clearButton.setVisible(false);

		getChildren().addAll(label, textField, clearButton);
		checkInput();
		clearButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				textField.setText("");
			}
		});
	}

	public String getText() {
		return textField.getText();
	}

	private void checkInput() {
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (textField.getText().length() > 0 && !(textField.getText().equals(" "))) {
					clearButton.setVisible(true);
				} else if (textField.getText().length() == 0) {
					clearButton.setVisible(false);
				}
			}
		});
	}

}
