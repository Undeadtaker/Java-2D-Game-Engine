
// This Component is in charge of drawing grid lines

package ECS.Components;

import ECS.Component;
import engine.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import renderer.DebugDraw;
import util.Helpers;

public class GridLines extends Component
{



    @Override
    public void update(float dt)
    {
        Vector2f cameraPos = Window.getScene().getCamera().position;
        Vector2f projectionSize = Window.getScene().getCamera().getProjectionSize();

        int firstX = ((int) (cameraPos.x / Helpers.GRID_WIDTH) * Helpers.GRID_HEIGHT) * Helpers.GRID_WIDTH;
        int firstY = ((int) (cameraPos.y / Helpers.GRID_HEIGHT) * Helpers.GRID_WIDTH);

        int numVerLines = (int) (projectionSize.x / Helpers.GRID_WIDTH);
        int numHorLines = (int) (projectionSize.y / Helpers.GRID_HEIGHT);

        int width = (int) projectionSize.x;
        int height = (int) projectionSize.y;

        int maxLines = Math.max(numHorLines, numVerLines);

        // Color
        Vector3f color = new Vector3f(0.2f, 0.2f, 0.2f);

        for(int i = 0; i < maxLines; i++)
        {
            int x = firstX + (Helpers.GRID_WIDTH * i);
            int y = firstY + (Helpers.GRID_HEIGHT * i);

            if (i < numVerLines) {DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), color);}
            if (i < numHorLines) {DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), color);}

        }

    }
}
