package dhbw.navigator.controles;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.SortedSet;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public class AutoCompleteControle extends GridPane {
	Button clearButton;
	TextFieldAutoCompleteControle textField;
	Label label;
	SortedSet<String> namesOfJunctions;
	PropertyChangeSupport changes = new PropertyChangeSupport(this);
	String text;

	public AutoCompleteControle(String labelText) {
		// Initialize
		label = new Label(labelText);
		clearButton = new Button("X");
		textField = new TextFieldAutoCompleteControle();
		clearButton.setVisible(false);
		// Set Columns
		for (int i = 0; i < 3; i++) {
			ColumnConstraints col = new ColumnConstraints();
			col.setPercentWidth(33.3);
			getColumnConstraints().add(col);
		}

		// Set Width of Columns
		getColumnConstraints().get(0).setPercentWidth(15);
		getColumnConstraints().get(1).setPercentWidth(75);
		getColumnConstraints().get(2).setPercentWidth(10);

		// Add Nodes to Column
		add(label, 0, 0);
		add(textField, 1, 0);
		add(clearButton, 2, 0);

		// Set EventHandler to Button, to Clear TextProperty
		clearButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				textField.setText("");
			}
		});
		// Set ChangeListener to TextField, to change visibility of Button
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (textField.getText().length() > 0 && !(textField.getText().equals(" "))) {
					clearButton.setVisible(true);

				} else if (textField.getText().length() == 0) {
					clearButton.setVisible(false);
				}
				setText(newValue);
				changes.firePropertyChange("text", oldValue, newValue);
			}
		});
	}

	public void setText(String value) {
		// Fire event that text changed
	}

	public String getText() {
		return textField.getText();
	}

	public void setNamesOfJunctions(SortedSet<String> namesOfJunctions) {
		this.namesOfJunctions = namesOfJunctions;
		textField.setContext(namesOfJunctions);
	}

	public void addPropertyChangeListener(String x, PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		changes.removePropertyChangeListener(l);
	}

	public void clearNode() {

	}
}
