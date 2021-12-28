package util;

public class Time
{
    // Static variables are initialized during the system start up, which means this is the time
    // in nanoseconds when the engine was started.
    public static float timeStarted = System.nanoTime();


    // To get the time elapsed in seconds, we will multiply the current time - time starter with 10^-9
    public static float getTime()
    {
        return(float) ((System.nanoTime() - timeStarted) * 1E-9);
    }

}
