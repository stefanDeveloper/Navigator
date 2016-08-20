package de.dhbw.navigator.views;

import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import de.dhbw.navigator.start.StartNavigator;

@RunWith(JUnit4.class)
public class RootLayoutControllerTest {
	RootLayoutController controller;
	public static StartNavigator start;
	private FxRobot robot;

	@Before
	public void setUp() throws Exception {
		FxToolkit.registerPrimaryStage();
		start = (StartNavigator) FxToolkit.setupApplication(StartNavigator.class);
		robot = new FxRobot();
		controller = start.getRoot();
		robot.sleep(5000);
	}

	@Test
	public void testSlideBarAutoCompleteSwitchButton() {
		robot.sleep(1000);
		robot.clickOn("#toggle");
		robot.clickOn("#start");
		robot.write("Ludwigsburg-Süd");
		robot.clickOn("#destination");
		robot.write("Neuenstein");
		robot.sleep(1000);
		robot.clickOn("#switch");
		robot.sleep(1000);
		robot.clickOn(controller.getStartPositionInput().getClearButton());
		robot.clickOn(controller.getDestinationPositionInput().getClearButton());
		robot.sleep(1000);
	}

	@Test
	public void testMapFunctionality() {
		robot.sleep(1000);
		robot.clickOn("#toggle");
		robot.clickOn(controller.getNavigationControl().getUp());
		robot.clickOn(controller.getNavigationControl().getDown());
		robot.clickOn(controller.getNavigationControl().getRight());
		robot.clickOn(controller.getNavigationControl().getLeft());
		robot.clickOn(controller.getNavigationControl().getZoomIn());
		robot.clickOn(controller.getNavigationControl().getZoomOut());
		robot.sleep(1000);
	}

	@After
	public void clean() {
		try {
			FxToolkit.cleanupStages();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
}
