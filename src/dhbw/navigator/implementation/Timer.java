package dhbw.navigator.implementation;

/**
 * Created by Konrad Mueller on 12.07.2016.
 */
public class Timer {
    private long startTime;
    private String description;

    public Timer(String pDescription)
    {
        startTime = System.nanoTime();
        this.description = pDescription;
    }

    public void printDuration() {
        long endTime = System.nanoTime();
        long duration = (((endTime - startTime) / 1000000));
        System.out.println("TIMER: " + description + " ,duration: " + duration + " ms.");
    }

}
