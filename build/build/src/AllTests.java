import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.dhbw.navigator.gui.UiTest;
import de.dhbw.navigator.implementation.DijkstraTest;
import de.dhbw.navigator.models.EdgeTest;
import de.dhbw.navigator.models.NodeTest;
import de.dhbw.navigator.views.RootLayoutControllerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ UiTest.class, RootLayoutControllerTest.class, EdgeTest.class, NodeTest.class, DijkstraTest.class })
public class AllTests {

}
