package renderer;

import static org.lwjgl.opengl.GL30.*;

public class FrameBuffer
{
    private int fboID = 0;

    private Texture texture = null;


    public FrameBuffer(int width, int height)
    {
        // Generate Framebuffer
        fboID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);

        // Create texture to render the data to and attach it to frame buffer
        this.texture = new Texture(width, height);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.texture.getTexID(), 0);

        // Create renderbuffer to store the depth info
        int rboID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);

        assert glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE : "Error: Framebuffer not complete";

        glBindFramebuffer(GL_FRAMEBUFFER, 0);

    }

    public void bind() {glBindFramebuffer(GL_FRAMEBUFFER, fboID);}
    public void unbind() {glBindFramebuffer(GL_FRAMEBUFFER, 0);}

    public int getFboID() {return fboID;}
    public int getTextureId(){return texture.getTexID();}


}
