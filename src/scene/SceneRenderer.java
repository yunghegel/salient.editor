package scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Array;
import app.Editor;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import scene.picking.GameObjectPicker;
import utils.ModelUtils;

public class SceneRenderer{

    public SceneManager sceneManager;
    public SceneContext sceneContext;
    public ModelBatch batch;
    public ModelBatch axisBatch;
    public ModelInstance grid;
    public ModelInstance axis;
    public GameObjectPicker gameObjectPicker = new GameObjectPicker();
    public Texture texture;
    public Array<RenderableProvider> renderables= new Array<RenderableProvider>();
    public SpriteBatch spriteBatch;

    public boolean drawGrid = true;

    public enum AxesStep
    {
        EIGTH,
        FOURTH,
        HALF,
        ONE,
        TWO,
        FOUR,
        EIGHT,
        SIXTEEN;

        @Override
        public String toString() {
            if (this == EIGTH) return "1/8";
            if (this == FOURTH) return "1/4";
            if (this == HALF) return "1/2";
            if (this == ONE) return "1X";
            if (this == TWO) return "2X";
            if (this == FOUR) return "4X";
            if (this == EIGHT) return "8X";
            if (this == SIXTEEN) return "16X";
            return null;
        }
    }

    public AxesStep axesStep = AxesStep.ONE;


    ModelInstance axesEighth;
    ModelInstance axesQuarter;
    ModelInstance axesHalf;
    ModelInstance axes1;
    ModelInstance axes2;
    ModelInstance axes4;
    ModelInstance axes8;
    ModelInstance axes16;

    {
        axesEighth = ModelUtils.createGrid(0.125f , 0.5f);
        axesQuarter = ModelUtils.createGrid(0.25f , 0.5f);
        axesHalf = ModelUtils.createGrid(0.5f , 0.5f);
        axes1 = ModelUtils.createGrid(1f , 0.5f);
        axes2 = ModelUtils.createGrid(2f , 0.5f);
        axes4 = ModelUtils.createGrid(4f , 0.5f);
        axes8 = ModelUtils.createGrid(8f , 0.5f);
        axes16 = ModelUtils.createGrid(16f , 0.5f);
        grid = ModelUtils.createGrid(2,0.5f);
        axis = ModelUtils.createAxisLines(0.8f);

    }

    public SceneRenderer(SceneContext sceneContext){
        this.sceneContext = sceneContext;
        this.sceneManager = sceneContext.sceneManager;

        sceneManager.getRenderableProviders().addAll(renderables);
        batch = sceneContext.batch;
        spriteBatch = new SpriteBatch();
        axisBatch = new ModelBatch();


    }

    public void render(float delta){
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glCullFace(GL20.GL_BACK);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 0f);
//        Editor.i().sceneContext.cam.update();

//        sceneManager.renderDepth();
        sceneManager.render();
        sceneManager.update(delta);
        drawAxis(sceneManager.camera);

        batch.begin(Editor.i().sceneContext.cam);
        batch.render(renderables);
        sceneContext.sceneGraph.update(delta);
        sceneContext.sceneGraph.render(delta);
        batch.end();

        Editor.i().ecs.toolSystem.render(delta);
        if(Editor.i().ecs.physicsSystem.enablePhysics){
        Editor.i().ecs.physicsSystem.debugDraw(sceneManager.camera);}

        sceneContext.wireframeRenderer.render(sceneContext.sceneGraph);


//        sceneContext.outlineRenderer.renderDepthOutline(sceneContext.sceneGraph);
        if (!sceneContext.outlineRenderer.modelInstances.isEmpty()) {
            sceneContext.outlineRenderer.captureFBO(sceneContext.sceneGraph);
            sceneContext.outlineRenderer.renderFBO(sceneContext.sceneGraph);

        };


    }
    public void drawAxis(Camera cam){
        axisBatch.begin(cam);
        //Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glDisable(Gdx.gl20.GL_DEPTH_TEST);
        switch (axesStep) {
            case EIGTH -> axisBatch.render(axesEighth , sceneManager.environment);
            case FOURTH -> axisBatch.render(axesQuarter , sceneManager.environment);
            case HALF -> axisBatch.render(axesHalf , sceneManager.environment);
            case ONE -> axisBatch.render(axes1 , sceneManager.environment);
            case TWO -> axisBatch.render(axes2 , sceneManager.environment);
            case FOUR -> axisBatch.render(axes4 , sceneManager.environment);
            case EIGHT -> axisBatch.render(axes8 , sceneManager.environment);
            case SIXTEEN -> axisBatch.render(axes16 , sceneManager.environment);
        }


        Gdx.gl.glEnable(Gdx.gl20.GL_DEPTH_TEST);

        //axisBatch.render(axes4, sceneManager.environment);
        Gdx.gl.glLineWidth(3);
        axisBatch.render(axis);
        Gdx.gl.glLineWidth(1);
        axisBatch.end();
    }

//    @Override
//    public void render(Camera cam) {
//        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//        Gdx.gl.glCullFace(GL20.GL_FRONT);
//        Gdx.gl.glEnable(GL20.GL_BLEND);
//        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
//        float delta = Gdx.graphics.getDeltaTime();
//
//        batch = Editor.i().sceneContext.batch;
//        Editor.i().sceneContext.salientCamera.update(delta);
//        sceneManager.update(delta);
//        sceneManager.render();
//        batch.begin(Editor.i().sceneContext.cam);
//        batch.render(renderables, sceneManager.environment);
//        batch.end();
//    }
}
