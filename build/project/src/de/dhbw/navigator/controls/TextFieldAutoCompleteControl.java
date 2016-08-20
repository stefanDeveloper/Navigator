package de.dhbw.navigator.controls;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

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
 * TextFieldAutoCompleteControl
 * AutoComplete TextField, give suggestions from a SortedSet<String>.
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public class TextFieldAutoCompleteControl extends TextField {
	private SortedSet<String> context;
	private ContextMenu contextMenu;

	public TextFieldAutoCompleteControl() {
		// Initialize
		contextMenu = new ContextMenu();
		setContextMenu(contextMenu);
		// Set ChangeListener
		textProperty().addListener(
				(ChangeListener<String>) (o, oldVal, newVal) -> TextFieldAutoCompleteControl.this.AutoComplete());
	}

	public void setContext(SortedSet<String> context) {
		this.context = context;
	}

	/**
	 * Fill ContextMenu with names of junctions
	 */
	private void AutoComplete() {
		if (context != null && context.size() > 0) {
			if (this.getText().length() == 0) {
				contextMenu.hide();
			} else {
				// Add names of Junctions
				LinkedList<String> searchResult = new LinkedList<>();
				searchResult.addAll(findJunctions());

				if (context.size() > 0 && !(searchResult.size()==1&&searchResult.get(0).toUpperCase().equals(getText().toUpperCase()))) {
					// Set ContextMenu
					populatePopup(searchResult);
					if (!contextMenu.isShowing()) {
						contextMenu.show(this, Side.BOTTOM, 0, 0);
					}
				} else {
					contextMenu.hide();
				}
			}
			// Set ChangeListener to hide ContextMenu
			focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean,
						Boolean aBoolean2) {
					contextMenu.hide();
				}
			});
		}
	}

	/**
	 * Add fitting names to the context menu.
	 * @param searchResult Fitting names.
	 */
	private void populatePopup(List<String> searchResult) {
		List<CustomMenuItem> menuItems = new LinkedList<>();
		// Set max entries
		int maxEntries = 10;
		int count = Math.min(searchResult.size(), maxEntries);
		for (int i = 0; i < count; i++) {
			final String result = searchResult.get(i);
			Label entryLabel = new Label(result);
			CustomMenuItem item = new CustomMenuItem(entryLabel, true);
			item.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					TextFieldAutoCompleteControl.this.setText(result);
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
		// Fill all names which start with input
		for (String string : context) {
			if (string.toLowerCase().startsWith(input)) {
				searchResult.add(string);
			}
		}
		// Fill all names, which contains input
		if (searchResult.size() < 10) {
			for (String string : context) {
				if (string.toLowerCase().contains(input) &! searchResult.contains(string)) {
					searchResult.add(string);
				}
			}
		}
		return searchResult;
	}

}
