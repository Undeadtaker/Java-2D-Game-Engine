package engine;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{
    private final int width, height;
    private final String title;
    private static Window window = null;
    private long final_window;

    // We create a new object Scene type
    private static Scene currentScene = null;

    private Window()
    {
        // Standard format for HD, fullscreen
        this.width = 1920;
        this.height = 1080;
        this.title = "2D_G4M3_3NG1N3";

    }

    // We call this function to change the scenes in our window
    public static void changeScene(int newScene)
    {
        switch(newScene)
        {
            case(0):
                currentScene = new LevelEditorScene();
                currentScene.init();
                currentScene.start();
                break;

            case(1):
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
                break;

            default:
                assert false : "Unknown scene " + newScene;
                break;
        }
    }

    public static Window get()
    {
        if(Window.window == null)
        {
            Window.window = new Window();
        }
        return Window.window;
    }






    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void run()
    {
        System.out.println("Hello LWJGL" + Version.getVersion() + "!");

        this.init();
        this.loop();
    }

    public void init() throws IllegalStateException
    {
        // Set up an error callback, is created so that the errors are printed to the log
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit())
        {
            throw new IllegalStateException("Unable to initialize GLFW window.");
        }

        /* Configure GLFW
            1 DefaultWindowHints - minimize, resize, close buttons
            2 Disable Visibility toggle
            3 Enable Window resizing
            4 When Window starts will be maximized
         */
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window, glfwCreateWindow returns the memory address where this window is in the memory space
        final_window = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

        if (final_window == NULL)
        {
            throw new IllegalStateException("Failed to create new GLFW window");
        }

        // We crate a lambda function to get the mouse location, :: -> lambda function
        // We don't have to create an instance of MouseListener, we simply call the static
        // functions for the glfw
        glfwSetCursorPosCallback(final_window, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(final_window, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(final_window, MouseListener::mouseScrollCallback);

        // Making callback to key listener
        glfwSetKeyCallback(final_window, KeyListener::keyCallback);


        // Make OpenGL context current
        glfwMakeContextCurrent(final_window);

        // Enabling v-sync (buffer swapping? No pause between the frames. The refresh rate will be the refresh rate
        // of the monitor)
        glfwSwapInterval(1);

        // Finally, make the window visible, takes the pointer to the final_window memory
        glfwShowWindow(final_window);

        /* "This line is critical for LWJGL's interoperation with GLFW's
           OpenGL context, or any context that is managed externally.
           LWJGL detects the context that is current in the current thread,
           creates the GLCapabilities instance and makes the OpenGL
           bindings available for use." Comment copied from https://www.lwjgl.org/guide
        */
        GL.createCapabilities();

        // Initialize scene
        changeScene(0);

    }

    public void loop()
    {

        // Initialize both start and current time
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;

        while(!glfwWindowShouldClose(final_window))
        {

            // Poll Events, important for key listeners
            glfwPollEvents();

            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0) currentScene.update(dt + 0.03f);

            // Swap back to the original window buffer
            glfwSwapBuffers(final_window);

            // We get the time at the end
            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }


}
