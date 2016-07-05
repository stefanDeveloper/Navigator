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
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;

public class AutoCompleteControle extends GridPane {
	private Button clearButton;
	private TextFieldAutoCompleteControle textField;
	private Label label;
	private SortedSet<String> namesOfJunctions;
	private PropertyChangeSupport changes = new PropertyChangeSupport( this );



	private String text;

	public AutoCompleteControle(String labelText) {
		this.label = new Label(labelText);
		this.clearButton = new Button("X");
		this.textField = new TextFieldAutoCompleteControle();
		this.clearButton.setVisible(false);
		//this.getChildren().addAll(this.label, this.textField, this.clearButton);

		for (int i = 0; i < 3; i++) {
			ColumnConstraints col = new ColumnConstraints();
			col.setPercentWidth(33.3);
			getColumnConstraints().add(col);
		}

		getColumnConstraints().get(0).setPercentWidth(15);
		getColumnConstraints().get(1).setPercentWidth(75);
		getColumnConstraints().get(2).setPercentWidth(10);

		add(label, 0, 0);
		add(textField, 1 , 0);
		add(clearButton, 2, 0);

		this.clearButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				AutoCompleteControle.this.textField.setText("");
			}
		});
		this.textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (AutoCompleteControle.this.textField.getText().length() > 0
						&& !(AutoCompleteControle.this.textField.getText().equals(" "))) {
					AutoCompleteControle.this.clearButton.setVisible(true);

				} else if (AutoCompleteControle.this.textField.getText().length() == 0) {
					AutoCompleteControle.this.clearButton.setVisible(false);
				}
				setText(newValue);
				changes.firePropertyChange("text", oldValue, newValue);
			}
		});
	}

	public void setText(String value)
	{
		//Fire event that text changed
	}

	public String getText() {
		return this.textField.getText();
	}

	public void setNamesOfJunctions(SortedSet<String> namesOfJunctions) {
		this.namesOfJunctions = namesOfJunctions;
		this.textField.setContext(namesOfJunctions);
	}

	public void addPropertyChangeListener(String x, PropertyChangeListener l)
	{
		changes.addPropertyChangeListener( l );
	}

	public void removePropertyChangeListener( PropertyChangeListener l )
	{
		changes.removePropertyChangeListener( l );
	}
}
