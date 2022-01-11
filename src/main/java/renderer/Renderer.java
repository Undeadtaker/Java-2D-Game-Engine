package renderer;

import ECS.Components.SpriteRenderer;
import ECS.GameObject;

import java.util.ArrayList;
import java.util.Collections;
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
            if(rb.hasRoom() && rb.getZ_index() == spr.gameObject.getZ_index())
            {
                Texture tex = spr.getTexture();
                if(tex == null || (rb.hasTexture(tex) || rb.hasTextureRoom()))
                {
                    rb.addSprite(spr);
                    added = true;
                    break;
                }
            }
        }

        if (!added)
        {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, spr.gameObject.getZ_index());
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(spr);
            Collections.sort(batches); // sort the batches according to their Z-index
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
