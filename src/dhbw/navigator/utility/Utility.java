package dhbw.navigator.utility;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * AutoComplete TextField, give suggestions from a SortedSet<String>.
 * @author Stefan
 *
 */
public class Utility {
	public static void AutoComplete(TextField textField, SortedSet<String> t, ContextMenu entriesPopup) {
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (textField.getText().length() == 0) {
					entriesPopup.hide();
				} else {
					LinkedList<String> searchResult = new LinkedList<>();
					searchResult.addAll(t.subSet(textField.getText(), textField.getText() + Character.MAX_VALUE));
					if (t.size() > 0) {
						populatePopup(searchResult, t, entriesPopup, textField);
						if (!entriesPopup.isShowing()) {
							entriesPopup.show(textField, Side.BOTTOM, 0, 0);
						}
					} else {
						entriesPopup.hide();
					}
				}

			}
		});

		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean,
					Boolean aBoolean2) {
				entriesPopup.hide();
			}
		});

	}

	private static void populatePopup(List<String> searchResult, SortedSet<String> t, ContextMenu entriesPopup,
			TextField textField) {
		List<CustomMenuItem> menuItems = new LinkedList<>();
		// If you'd like more entries, modify this line.
		int maxEntries = 10;
		int count = Math.min(searchResult.size(), maxEntries);
		for (int i = 0; i < count; i++) {
			final String result = searchResult.get(i);
			Label entryLabel = new Label(result);
			CustomMenuItem item = new CustomMenuItem(entryLabel, true);
			item.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					textField.setText(result);
					entriesPopup.hide();
				}
			});
			menuItems.add(item);
		}
		entriesPopup.getItems().clear();
		entriesPopup.getItems().addAll(menuItems);

	}

//	 public SortedSet<String> getEntries() { return t; }
	
	public static void checkInput(TextField textField, Button button){
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (textField.getText().length() > 0 && !(textField.getText().equals(" ")) ){
					button.setVisible(true);
				} else if (textField.getText().length() == 0){
					button.setVisible(false);
				}
			}
		});
	}

}
