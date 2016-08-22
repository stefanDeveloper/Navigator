package de.dhbw.navigator.implementation;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import de.dhbw.navigator.controls.ProgressBarControle;
import de.dhbw.navigator.models.Node;
import de.dhbw.navigator.models.ParserProgress;
import de.dhbw.navigator.models.ParserState;
import javafx.application.Platform;

/**
 * Load the map data. Data will be parsed form source if serialised data is not
 * avaliable.
 */
public class LoadData {
	private ArrayList<Node> testedNodes = new ArrayList<>();
	private ArrayList<Node> alreadyParsed = new ArrayList<>();
	private ArrayList<Node> nodes;


	/**
	 * Methods who adress the ui thread need a special treatment ... because they are special ...
	 * @param func
	 */
	void invokeOnMainthread(Callable<?> func) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					func.call();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}


	/**
	 * Load the map data.
	 * @param parseData Should the last serialised data get loaded ?
	 * @param xmlFilePath Path to the xmlFile that should get loaded.
	 * @param serialiseFilePath Path to the location where the new data gets serialized.
	 * @param progressBar Reference to a progress bar that will display the progress.
	 * @return The loaded map data as {@Code ArrayList<Node>.}
	 */
	public ArrayList<Node> load(Boolean parseData, String xmlFilePath, String serialiseFilePath, ProgressBarControle progressBar) {
		try{
			ParseService parser = new ParseService();
			parser.addPropertyChangeListener(evt -> {
				invokeOnMainthread(() -> {
					ParserProgress parserProgress = (ParserProgress)evt.getNewValue();
					if(parserProgress.getState() != ParserState.FINDING_EDGES)
						progressBar.setProgress(-1);
					else if(parserProgress.getLeftItems()==0 || parserProgress.getTotalItems() == 0)
						progressBar.setProgress(0);
					else
					{
						double value = (parserProgress.getTotalItems()-parserProgress.getLeftItems())/(parserProgress.getTotalItems()/100);
						progressBar.setProgress(value/100);
					}

					switch (parserProgress.getState()){
						case EXTRACTING_NODES:
							progressBar.setInfoLabel("Spioniere Ihre Daten aus.");
							break;
						case FINDING_EDGES:
							progressBar.setInfoLabel("Verbinde Knoten ...");
							break;
						case EXTRACTING_WAYS:
							progressBar.setInfoLabel("Extrahiere Wege.");
							break;
						case READING_XML:
							progressBar.setInfoLabel("Lese Rohdaten.");
							break;
						case MERGING_NODES:
							progressBar.setInfoLabel("Extrahiere Knoten");
							break;
						default:
							progressBar.setInfoLabel("Fehler");
							break;
					}
					return null;
				});
			});
			Serialiser serialiser = new Serialiser();
			// Check boolean
			if (parseData) {
				invokeOnMainthread(()->{
						progressBar.show();
						return null;
				});

				// Parse file and serialize it
				nodes = parser.getNodes(xmlFilePath);
				nodes = findConnectedMap(nodes);
				serialiser.serialize(nodes, serialiseFilePath);
			} else {
				invokeOnMainthread(()->{
						progressBar.setInfoLabel("Deserialisiere Daten.");
						progressBar.setProgress(-1);
						progressBar.show();
					return null;
				});
				nodes = serialiser.deserialize(serialiseFilePath);

			}
		}catch (Exception ex){
			ex.printStackTrace();
		}finally {
			invokeOnMainthread(()->{
				progressBar.hide();
				return null;
			});
		}
		return nodes;

	}

	/**
	 * Find a map that is part of the source
	 * @param source
	 * @return
	 */
	private ArrayList<Node> findConnectedMap(ArrayList<Node> source) {
		ArrayList<Node> biggestConnectedMap = new ArrayList<>();
		for (Node n : source) {
			if (!testedNodes.contains(n)) {
				alreadyParsed.clear();
				ArrayList<Node> newTry = findConnectedNodes(n);
				if (newTry.size() > biggestConnectedMap.size())
					biggestConnectedMap = newTry;
			}
		}
		return biggestConnectedMap;
	}

	private ArrayList<Node> findConnectedNodes(Node n) {
		if (!testedNodes.contains(n))
			testedNodes.add(n);
		ArrayList<Node> nodes = new ArrayList<>();
		nodes.add(n);
		alreadyParsed.add(n);
		for (Node neuN : n.getNeighbours()) {
			if (!alreadyParsed.contains(neuN)) {
				nodes.addAll(findConnectedNodes(neuN));
			}
		}
		return nodes;
	}
}
