package engine;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener
{
    private static KeyListener KL;
    private final boolean[] b_keyPressed = new boolean[350];


    private KeyListener()
    {

    }

    private static KeyListener get()
    {
        if(KL == null)
        {
            KL = new KeyListener();
        }
        return KL;
    }


    public static void keyCallback(long window, int key, int scancode, int action, int mods)
    {

        // Crate local reference to KL
        KL = get();

        if(action == GLFW_PRESS)
        {
            KL.b_keyPressed[key] = true;
        }
        else if(action == GLFW_RELEASE)
        {
            KL.b_keyPressed[key] = false;
        }
    }


    // GETTER
    public static boolean isKeyPressed(int keyCode)
    {
        return get().b_keyPressed[keyCode];
    }

}
