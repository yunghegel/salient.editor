package core.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import app.Editor;
import net.mgsx.gltf.scene3d.shaders.PBRShaderProvider;
import scene.graph.SceneGraph;
import shaders.OutlineDepth;
import ui.widgets.ViewportWidget;

public class OutlineRenderer {

    public SpriteBatch batch;
    public GenFrameBuffer buf;
    public ModelInstance modelInstance;
    public ShaderProgram outlineShaderProgram;
    public ModelBatch modelBatch;
    public Array<ModelInstance> modelInstances = new Array<>();
    public Texture texture;
    public TextureRegion textureRegion;

    public OutlineDepth outlineDepth;
    private FrameBuffer depthBuffer;
    private Texture depthTexture;
    private ModelBatch depthBatch;


    public OutlineRenderer() {
        batch = new SpriteBatch();
        modelBatch = new ModelBatch();
        buf = new GenFrameBuffer(true);
        outlineShaderProgram = new ShaderProgram(Gdx.files.internal("shaders/object_outline.vert").readString(), Gdx.files.internal("shaders/object_outline.frag").readString());
        if (!outlineShaderProgram.isCompiled()) {
            System.out.println(outlineShaderProgram.getLog());
        }
        outlineShaderProgram.bind();
        batch.setShader(outlineShaderProgram);

        outlineDepth = new OutlineDepth(true);
        depthBuffer = new FrameBuffer(Pixmap.Format.RGB888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        depthBatch = new ModelBatch(PBRShaderProvider.createDefaultDepth(144));

    }

    public void setModelInstance(ModelInstance modelInstance) {
        modelInstances.clear();
        modelInstances.add(modelInstance);
//        Editor.i().sceneContext.sceneManager.getRenderableProviders().add(modelInstance);
    }

    public void deselectModelInstances() {
        modelInstances.clear();
    }

    public void captureFBO(SceneGraph sceneGraph){
        buf.ensureFboSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        buf.begin(Editor.i().viewportWidget.viewport);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        modelBatch.begin(sceneGraph.scene.cam);
        modelBatch.render(modelInstances);
        modelBatch.end();
        buf.end();
//        texture = buf.getColorBufferTexture();
//        textureRegion = new TextureRegion(texture);
//        textureRegion.flip(false, true);
    }

    public void renderFBO(SceneGraph sceneGraph) {
        ViewportWidget widget = Editor.i().viewportWidget;
        batch.begin();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.draw(buf.getFboTexture(),0 , 0 , Gdx.graphics.getWidth() , Gdx.graphics.getHeight() );
        batch.end();
    }

    public void render(SceneGraph sceneGraph) {
        if (modelInstances.isEmpty()) return;
        captureFBO(sceneGraph);
        renderFBO(sceneGraph);
    }

    public void captureDepth(SceneGraph sceneGraph){
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        depthBuffer.begin();
        ScreenUtils.clear(.2f,.2f,.2f,0, true);
        depthBatch.begin(sceneGraph.scene.cam);
        depthBatch.render(modelInstances,sceneGraph.scene.environment);
        depthBatch.end();

        Editor.i().sceneContext.sceneManager.renderDepth();
        depthBuffer.end();
        depthTexture = depthBuffer.getColorBufferTexture();
    }

    public void renderDepthOutline(SceneGraph sceneGraph) {
        captureDepth(sceneGraph);
        outlineDepth.render(batch,depthTexture,sceneGraph.scene.cam);
    }


}


