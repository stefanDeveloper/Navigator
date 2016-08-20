package de.dhbw.navigator.gui;

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
public class UiTest {
	private FxRobot robot;

	@Before
	public void setUp() throws Exception {
		FxToolkit.registerPrimaryStage();
		FxToolkit.setupApplication(StartNavigator.class);
		robot = new FxRobot();
		robot.sleep(5000);
	}

	@Test
	public void testHelpMenu() {
		robot.sleep(1000);
		robot.clickOn("#help");
		robot.clickOn("#info");
		robot.clickOn("OK");
		robot.sleep(1000);
		robot.clickOn("#help");
		robot.clickOn("#sync");
		robot.sleep(1000);
	}

	@Test
	public void testSyncMenu() {
		robot.clickOn("#loadFile");
		robot.sleep(1000);
		robot.closeCurrentWindow();
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
