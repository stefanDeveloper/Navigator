package de.dhbw.navigator.models;

/**
 * Created by Konrd on 14.08.2016.
 */
public class ParserProgress {
    private ParserState state;
    private double totalItems;
    private double leftItems;

    public ParserState getState() {
        return state;
    }

    public void setState(ParserState state) {
        this.state = state;
    }

    public double getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public double getLeftItems() {
        return leftItems;
    }

    public void setLeftItems(int leftItems) {
        this.leftItems = leftItems;
    }
}
