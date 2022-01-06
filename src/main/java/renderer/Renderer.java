package renderer;

import ECS.Components.SpriteRenderer;
import ECS.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Renderer
{
    // VARIABLES
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    // CONSTRUCTOR
    public Renderer()
    {
        this.batches = new ArrayList<>();
    }

    public void add(GameObject go_obj)
    {
        SpriteRenderer spr = go_obj.getComponent(SpriteRenderer.class);
        if (spr != null)
        {
            this.add(spr);
        }
    }

    private void add(SpriteRenderer spr)
    {
        boolean added = false;

        for(RenderBatch rb : batches)
        {
            if(rb.hasRoom())
            {
                rb.addSprite(spr);
                added = true;
                break;
            }
        }

        if (!added)
        {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(spr);
        }
    }

    public void render()
    {
        for(RenderBatch rb : batches)
        {
            rb.render();
        }
    }



}
