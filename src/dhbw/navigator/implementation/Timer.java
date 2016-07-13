package dhbw.navigator.implementation;

/**
 * Timer
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
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
