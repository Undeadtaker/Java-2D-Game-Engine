package engine;

import engine.GUI.ImGUI_Engine;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{
    private int width;
    private int height;
    private final String title;
    private static Window window = null;
    private long pointer_final_window;

    // We create a new object Scene type
    private static Scene currentScene;

    private String glslVersion = null;
    private ImGUI_Engine imGUI_Engine_obj;

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

    public static Scene getScene() { return get().currentScene; }
    public static int getWidth() {return get().width;}
    public static int getHeight() {return get().height;}
    public static void setWidth(int newWidth) {get().width = newWidth;}
    public static void setHeight(int newHeight) {get().height = newHeight;}



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void run()
    {
        System.out.println("Hello LWJGL" + Version.getVersion() + "!");

        this.init();
        this.loop();

        glfwFreeCallbacks(pointer_final_window);
        glfwDestroyWindow(pointer_final_window);

        // Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
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

        glslVersion = "#version 130";
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);

        // Create the window, glfwCreateWindow returns the memory address where this window is in the memory space
        pointer_final_window = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

        // Fixes bug of out of scale for now
        Window.setHeight(1017);

        if (pointer_final_window == NULL)
        {
            throw new IllegalStateException("Failed to create new GLFW window");
        }

        // We crate a lambda function to get the mouse location, :: -> lambda function
        // We don't have to create an instance of MouseListener, we simply call the static
        // functions for the glfw
        glfwSetCursorPosCallback(pointer_final_window, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(pointer_final_window, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(pointer_final_window, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(pointer_final_window, KeyListener::keyCallback); // Making callback to key listener

        // We need to inline the mouse position being created by the ImGUI
        glfwSetWindowSizeCallback(pointer_final_window, (window_reference, newWidth, newHeight) ->
        {
            Window.setHeight(newHeight);
            Window.setWidth(newWidth);
        });

        // Make OpenGL context current
        glfwMakeContextCurrent(pointer_final_window);

        // Enabling v-sync (buffer swapping? No pause between the frames. The refresh rate will be the refresh rate
        // of the monitor)
        glfwSwapInterval(1);

        // Finally, make the window visible, takes the pointer to the final_window memory
        glfwShowWindow(pointer_final_window);

        /* "This line is critical for LWJGL's interoperation with GLFW's
           OpenGL context, or any context that is managed externally.
           LWJGL detects the context that is current in the current thread,
           creates the GLCapabilities instance and makes the OpenGL
           bindings available for use." Comment copied from https://www.lwjgl.org/guide
        */
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        // Create ImGUI reference
        this.imGUI_Engine_obj = new ImGUI_Engine(pointer_final_window);

        // Initialize all ImGUI elements
        this.imGUI_Engine_obj.initImGui();

        // Initialize scene
        Window.changeScene(0);

    }

    public void loop()
    {

        // Initialize both start and current time
        float beginTime = (float) glfwGetTime();
        float endTime;
        float dt = -1.0f;

        while(!glfwWindowShouldClose(pointer_final_window))
        {

            // Poll Events, important for key listeners
            glfwPollEvents();

            glClearColor(0, 0, 0, 0);
            glClear(GL_COLOR_BUFFER_BIT);

            // Update current scene selected
            if (dt >= 0) currentScene.update(dt + 0.03f);

            this.imGUI_Engine_obj.update(dt + 0.03f, currentScene);

            // Swap back to the original window buffer
            glfwSwapBuffers(pointer_final_window);

            // We get the time at the end
            endTime = (float) glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

}
