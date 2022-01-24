/* This class is created as a helper for any
   type of controls we want to do with our mose. */

package ECS.Components;

import ECS.Component;
import ECS.GameObject;
import engine.MouseListener;
import engine.Window;
import util.Helpers;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component
{
    // Held object is the gameObject the mouse is holding
    GameObject heldObject = null;

    public void pickupObject(GameObject GObj) {
        this.heldObject = GObj;

        // After we hold the object, we want to make it visible to the user
        Window.getScene().addGameObjectToScene(GObj);
    }

    @Override
    public void update(float dt)
    {
        if(heldObject != null)
        {
            heldObject.transform.position.x = MouseListener.getOrthoX();
            heldObject.transform.position.y = MouseListener.getOrthoY();

            heldObject.transform.position.x = (int) (heldObject.transform.position.x / Helpers.GRID_WIDTH) * Helpers.GRID_WIDTH;
            heldObject.transform.position.y = (int) (heldObject.transform.position.y / Helpers.GRID_HEIGHT) * Helpers.GRID_HEIGHT;

            if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
            {
                this.place();
            }
        }
    }


    public void place() {this.heldObject = null;}
}
