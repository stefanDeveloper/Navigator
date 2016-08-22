package de.dhbw.navigator.controls;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.SortedSet;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
/**
 * AutoCompleteControl
 * Control that combines a TextFieldAutoCompleteControl with a
 * Name Label and ClearButton.
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public class AutoCompleteControl extends GridPane {
	private Button clearButton; // Button can clear the TextField
	private TextFieldAutoCompleteControl textField; // TextField with
														// AutoComplete
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);

	// Constructor
	public AutoCompleteControl(String labelText) {
		Label label = new Label(labelText);
		clearButton = new Button("X");
		textField = new TextFieldAutoCompleteControl();
		clearButton.setVisible(false);
		setPadding(new Insets(5, 0, 0, 5));
		//Set ToolTip
		clearButton.setTooltip(new Tooltip("Lösche"));
		// Create four columns for the controls
		for (int i = 0; i < 4; i++) {
			ColumnConstraints col = new ColumnConstraints();
			getColumnConstraints().add(col);
		}

		getColumnConstraints().get(0).setPercentWidth(15);
		getColumnConstraints().get(1).setPercentWidth(70);
		getColumnConstraints().get(2).setPercentWidth(2.5);
		getColumnConstraints().get(3).setPercentWidth(12.5);

		// Add controls
		add(label, 0, 0);
		add(textField, 1, 0);
		add(clearButton, 3, 0);

		// Listen to button action
		// Clear TextField if button gets pushed
		clearButton.setOnAction(event -> AutoCompleteControl.this.textField.setText(""));
		// Listen to the input
		// Show ClearButton if input is not empty
		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (AutoCompleteControl.this.textField.getText().length() > 0
					&& !(AutoCompleteControl.this.textField.getText().equals(" "))) {
				AutoCompleteControl.this.clearButton.setVisible(true);

			} else if (AutoCompleteControl.this.textField.getText().length() == 0) {
				AutoCompleteControl.this.clearButton.setVisible(false);
			}
			changes.firePropertyChange("text", oldValue, newValue);
		});
	}

	/**
	 * Set the junction name list of this control.
	 * @param namesOfJunctions
	 */
	public void setNamesOfJunctions(SortedSet<String> namesOfJunctions) {
		textField.setContext(namesOfJunctions);
	}

	/**
	 * Add a listener to register changes in the selected text.
	 * @param l
	 */
	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}


	/**
	 * @return The selected text.
	 */
	public String getText() {
		return textField.getText();
	}

	/**
	 * Set the text of the control.
	 * @param text Set the text of the textfield.
	 */
	public void setText(String text) {
		textField.setText(text);
	}
	/**
	 * @return The TextField for the UI Test
	 */
	public TextFieldAutoCompleteControl getTextField() {
		return textField;
	}
	
	/**
	 * @return Button to clear TextField for the UI Test
	 */
	public Button getClearButton() {
		return clearButton;
	}
	
	
	
}
