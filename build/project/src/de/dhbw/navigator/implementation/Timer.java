package de.dhbw.navigator.implementation;

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
        String type = "ms";
        if(duration>1000){
            duration = duration/1000;
            type = "seconds";
            if(duration > 60){
                duration = duration / 60;
                type = "minutes";
            }
        }
        System.out.println("TIMER: " + description + " ,duration: " + duration + " " + type);
    }

}
