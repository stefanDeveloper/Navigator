package de.dhbw.navigator.controls;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;

/**
 * Button to extend
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public class ToggleButtonControl extends Button {
	/**
	 * Button to extend SlideBar
	 */
	public ToggleButtonControl(){
		//Set Text
		setText("<");
		setPrefHeight(80);
		setBorder(Border.EMPTY);
		setTooltip(new Tooltip("Einfahren"));
	}
	/**
	 * If button is clicked, title changed and tooltip.
	 * Check if slideBar is expanded or not
	 * @param slideBar
	 * 
	 */
	public void setChangeListener(SlideBarControl slideBar){
		//If button clicked, title change and tooltip
		slideBar.addPropertyChangeListener(isExpanded -> {
			if ((boolean) isExpanded.getNewValue()) {
				setText("<");
				setTooltip(new Tooltip("Einfahren"));
			} else {
				setText(">");
				setTooltip(new Tooltip("Ausfahren"));
			}
		});
	}
}
