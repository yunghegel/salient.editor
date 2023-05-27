package scene.picking;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.minlog.Log;

import java.nio.ByteBuffer;

public abstract class BasePicker implements Disposable {

    public FrameBuffer fbo;

    public BasePicker() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        try {
            fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        } catch (Exception e) {
            Log.info("BasePicker", "Exception creating FrameBuffer: " + e.getMessage());
            width *= 0.9f;
            height *= 0.9f;
            try {
                fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
            } catch (Exception ee) {
                Log.info("BasePicker", "Exception creating FrameBuffer: " + ee.getMessage());
            }
        }

    }

    protected void begin(Viewport viewport) {

        fbo.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        HdpiUtils.glViewport(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(),
                viewport.getScreenHeight());
    }

    protected void end() {
        fbo.end();
    }

    public Pixmap getFrameBufferPixmap(Viewport viewport) {
        int w = viewport.getScreenWidth();
        int h = viewport.getScreenHeight();
        int x = viewport.getScreenX();
        int y = viewport.getScreenY();
        final ByteBuffer pixelBuffer = BufferUtils.newByteBuffer(w * h * 4);

        Gdx.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, fbo.getFramebufferHandle());
        Gdx.gl.glReadPixels(x, y, w, h, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, pixelBuffer);
        Gdx.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, 0);

        final int numBytes = w * h * 4;
        byte[] imgLines = new byte[numBytes];
        final int numBytesPerLine = w * 4;
        for (int i = 0; i < h; i++) {
            pixelBuffer.position((h - i - 1) * numBytesPerLine);
            pixelBuffer.get(imgLines, i * numBytesPerLine, numBytesPerLine);
        }

        Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        BufferUtils.copy(imgLines, 0, pixmap.getPixels(), imgLines.length);

        return pixmap;
    }

    @Override
    public void dispose() {
        fbo.dispose();
    }

}
