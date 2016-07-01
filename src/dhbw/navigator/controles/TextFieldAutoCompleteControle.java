package dhbw.navigator.controles;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * AutoComplete TextField, give suggestions from a SortedSet<String>.
 * 
 * @author Stefan
 *
 */
public class TextFieldAutoCompleteControle extends TextField {
	private List<String> namesOfJunction;
	private ContextMenu contextMenu;

	public TextFieldAutoCompleteControle(List<String> namesOfJunctions) {
		namesOfJunction = namesOfJunctions;
		contextMenu = new ContextMenu();
		setContextMenu(contextMenu);
		textProperty().addListener(
				(ChangeListener<String>) (o, oldVal, newVal) -> TextFieldAutoCompleteControle.this.AutoComplete());
	}

	private void AutoComplete() {
		if (this.getText().length() == 0) {
			contextMenu.hide();
		} else {
			LinkedList<String> searchResult = new LinkedList<>();
			searchResult.addAll(findJunctions());

			if (namesOfJunction.size() > 0) {
				populatePopup(searchResult);
				if (!contextMenu.isShowing()) {
					contextMenu.show(this, Side.BOTTOM, 0, 0);
				}
			} else {
				contextMenu.hide();
			}
		}

		focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean,
					Boolean aBoolean2) {
				contextMenu.hide();
			}
		});

	}

	private void populatePopup(List<String> searchResult) {
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
					TextFieldAutoCompleteControle.this.setText(result);
					contextMenu.hide();
				}
			});
			menuItems.add(item);
		}
		contextMenu.getItems().clear();
		contextMenu.getItems().addAll(menuItems);

	}

	private ArrayList<String> findJunctions() {
		ArrayList<String> searchResult = new ArrayList<>();
		String input = this.getText().toLowerCase();
		for (String string : namesOfJunction) {
			if (string.toLowerCase().startsWith(input)) {
				searchResult.add(string);
			}
		}
		if (searchResult.size() < 10) {
			for (String string : namesOfJunction) {
				if (string.toLowerCase().contains(input)) {
					searchResult.add(string);
				}
			}
		}
		return searchResult;
	}

}
