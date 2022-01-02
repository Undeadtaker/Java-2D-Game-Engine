package engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera
{
    private final Matrix4f projectionMatrix, viewMatrix;
    public Vector2f position;


    public Camera(Vector2f pos)
    {
        this.position = pos;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.adjustProjection();
    }

    public void adjustProjection()
    {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, 32.0f * 40.0f ,0.0f, 32.0f * 21.0f, 0.0f, 100.0f);
    }

    public Matrix4f getViewMatrix()
    {
        // The camera is looking in the negative z direction
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
                                            cameraFront.add(position.x, position.y, 0.0f),
                                            cameraUp);
        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix()
    {
        return this.projectionMatrix;
    }


}
