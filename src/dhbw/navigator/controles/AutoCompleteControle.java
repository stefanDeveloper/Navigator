package dhbw.navigator.controles;

import java.util.SortedSet;

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

	public AutoCompleteControle(String labelText, SortedSet<String> namesOfJunctions) {
		this.label = new Label(labelText);
		this.textField = new TextFieldAutoCompleteControle(namesOfJunctions);
		this.clearButton = new Button("X");
		this.clearButton.setVisible(false);

		this.getChildren().addAll(this.label, this.textField, this.clearButton);
		this.checkInput();
		this.clearButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				AutoCompleteControle.this.textField.setText("");
			}
		});
	}

	public String getText() {
		return this.textField.getText();
	}

	private void checkInput() {
		this.textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (AutoCompleteControle.this.textField.getText().length() > 0
						&& !(AutoCompleteControle.this.textField.getText().equals(" "))) {
					AutoCompleteControle.this.clearButton.setVisible(true);
				} else if (AutoCompleteControle.this.textField.getText().length() == 0) {
					AutoCompleteControle.this.clearButton.setVisible(false);
				}
			}
		});
	}

}
