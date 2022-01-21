package engine;

import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener
{

    // We only declare the type of the variable ML, not its value
    private static MouseListener ML;
    private double scrollX, scrollY, xPos, yPos, lastX, lastY;
    private boolean b_IsDragging;

    // Create array of which mouse button was pressed
    private final boolean[] mouseButtonPressed = new boolean[9];


    // Creating private constructor because we don't need anyone forming this class outside this class
    private MouseListener()
    {
        this.scrollX = 0.0f;
        this.scrollY = 0.0f;
        this.xPos = 0.0f;
        this.yPos = 0.0f;
        this.lastX = 0.0f;
        this.lastY = 0.0f;
    }

    // getter for instance MouseListener, used in every callback within this class
    public static MouseListener get()
    {
        if(ML == null)
        {
            ML = new MouseListener();
        }
        return ML;
    }

    // Method for getting mouse position
    public static void mousePosCallback(long window, double xpos, double ypos)
    {
        // Get local reference to ML
        ML = get();

        ML.lastX = ML.xPos;
        ML.lastY = ML.yPos;

        // Set the new values of MouseListener instance to passed xpos and ypos
        ML.xPos = xpos;
        ML.yPos = ypos;

        // If any of the mouse buttons is pressed (or) and if the b_IsDragging is true, then it must be dragging
        ML.b_IsDragging = ML.mouseButtonPressed[0] || ML.mouseButtonPressed[1] || ML.mouseButtonPressed[2];

    }

    // Which mouse button was pressed and where. Mods are if a combo of keys was pressed, maybe CTRL + left mouse btn?
    public static void mouseButtonCallback(long window, int button, int action, int mods)
    {
        // Get local reference to ML
        ML = get();

        // Indicate which mouse button was pressed depending on the position in the array
        if (action == GLFW_PRESS)
        {
            // If more than one button is pressed
            if (button < ML.mouseButtonPressed.length)
            {
                ML.mouseButtonPressed[button] = true;
            }
        }
        else if (action == GLFW_RELEASE)
        {
            // Same check
            if (button < ML.mouseButtonPressed.length)
            {
                ML.mouseButtonPressed[button] = false;
                ML.b_IsDragging = false;
            }
        }
    }


    // Taking the Scroll input
    public static void mouseScrollCallback(long window, double xOffset, double yOffset)
    {
        // Get local reference to ML
        ML = get();

        ML.scrollX = xOffset;
        ML.scrollY = yOffset;
    }

    // endFrame, probably what happens at the last frame
    public static void endFrame()
    {
        // Get local reference to ML
        ML = get();

        ML.scrollX = 0;
        ML.scrollY = 0;
        ML.lastX = ML.xPos;
        ML.lastY = ML.yPos;
    }

    // GETTERS

    public static float getX()
    {
        // Here the get() is reference to ML
        return (float)get().xPos;
    }

    public static float getY()
    {
        // Here the get() is reference to ML
        return (float)get().yPos;
    }

    // Returns the elapsed x position in the current frame
    public static float getDX()
    {
        // Here the get() is reference to ML
        return (float)(get().lastX - get().xPos);
    }

    // Returns the elapsed y position in the current frame
    public static float getDY()
    {
        // Here the get() is reference to ML
        return (float)(get().lastY - get().yPos);
    }

    // Returns the scrollX
    public static float getScrollX()
    {
        return (float)get().scrollX;
    }

    // Returns the scrollY
    public static float getScrollY()
    {
        return (float)get().scrollY;
    }

    // Check dragging status of variable isDragging
    public boolean isDragging()
    {
        return get().b_IsDragging;
    }

    public static boolean mouseButtonDown(int button)
    {
        if (button < get().mouseButtonPressed.length)
        {
            return get().mouseButtonPressed[button];
        }
        else return false;
    }

    public static float getOrthoX()
    {
        float currentX = getX();
        // Converts to -1 to 1 range for the normalized world coordinates
        currentX = (currentX / (float) Window.getWidth()) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(currentX, 0, 0, 1);

        // Undo normalized world coordinates, now we should get real world coordinates 1920 x 1080
        tmp.mul(Window.getScene().getCamera().getInverseProjection().mul(Window.getScene().getCamera().getInverseView()));
        currentX = tmp.x;

        return currentX;
    }

    public static float getOrthoY()
    {
        float currentY = Window.getHeight() - getY();
        // Converts to -1 to 1 range for the normalized world coordinates
        currentY = (currentY / (float) Window.getHeight()) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(0, currentY, 0, 1);

        // Undo normalized world coordinates, now we should get real world coordinates 1920 x 1080
        tmp.mul(Window.getScene().getCamera().getInverseProjection().mul(Window.getScene().getCamera().getInverseView()));
        currentY = tmp.y;

        return currentY;
    }

}

















