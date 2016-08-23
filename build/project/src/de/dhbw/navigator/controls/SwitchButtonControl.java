package de.dhbw.navigator.controls;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

/**
 * Switch to inputs
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public class SwitchButtonControl extends Button {
	/**
	 * Switch the Input of two AutoCompleteControls
	 * @param startPositionInput
	 * @param destinationPositionInput
	 */
	public SwitchButtonControl(AutoCompleteControl startPositionInput, AutoCompleteControl destinationPositionInput){
		// Set Text an tooltip
		setText("</>");
		setTooltip(new Tooltip("Vertauschen"));
		setOnAction(event -> {
			//Exchange the input 
			String tmpText = destinationPositionInput.getText();
			destinationPositionInput.setText(startPositionInput.getText());
			startPositionInput.setText(tmpText);
		});
	}
}
