package tools;

import backend.tools.Log;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import app.Editor;
import ecs.components.PickableModelComponent;
import ecs.components.TransformComponent;
import ecs.systems.PickingSystem;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import scene.graph.GameObject;
import utils.MaterialUtils;
import utils.ModelUtils;
import utils.Suppliers;

public class TranslateTool extends AbstractTool<Matrix4>{



    public enum TranslationState
    {
        DISABLED, TRANSLATE_X, TRANSLATE_Y, TRANSLATE_Z
    }

    public TranslationState transformState = TranslationState.DISABLED;

    private static final float ARROW_THIKNESS = 0.3f;
    private static final float ARROW_CAP_SIZE = 0.08f;
    private static final int ARROW_DIVISIONS = 12;

    protected static Color COLOR_X = Color.RED;
    protected static Color COLOR_Y = Color.GREEN;
    protected static Color COLOR_Z = Color.BLUE;
    protected static Color COLOR_XYZ = Color.CYAN;

    public boolean enabled = false;

    public Model xHandleModel,yHandleModel,zHandleModel,xyzHandleModel;
    public ModelInstance xHandle;
    public ModelInstance yHandle;
    public ModelInstance zHandle;
    public ModelInstance xyzHandle;
    public ModelInstance xHandleCopy,yHandleCopy,zHandleCopy,xyzHandleCopy;

    public ModelBatch batch;

    public GameObject[] handles = new GameObject[3];
    public GameObject selectedHandle;
    Ray ray;
    double dst = 0;
    private Vector3 localPosition = new Vector3();
    private Vector3 position = new Vector3();
    private Matrix4 tmp = new Matrix4();
    private final Vector3 lastPos = new Vector3();
    private boolean initTranslate = true;
    private final Vector3 temp0 = new Vector3();
    private final Vector3 temp1 = new Vector3();
    private final Matrix4 tempMat0 = new Matrix4();

    public TranslateTool (ModelBatch batch){
        this.batch = batch;
        ModelBuilder modelBuilder = new ModelBuilder();
        xHandleModel = modelBuilder.createArrow(0 , 0 , 0 , .5f , 0 , 0 , ARROW_CAP_SIZE , ARROW_THIKNESS , ARROW_DIVISIONS , GL20.GL_TRIANGLES , new Material(PBRColorAttribute.createEmissive(COLOR_X) , PBRColorAttribute.createSpecular(COLOR_X)) , VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal |VertexAttributes.Usage.TextureCoordinates);
        yHandleModel = modelBuilder.createArrow(0 , 0 , 0 , 0 , .5f , 0 , ARROW_CAP_SIZE , ARROW_THIKNESS , ARROW_DIVISIONS , GL20.GL_TRIANGLES , new Material(PBRColorAttribute.createEmissive(COLOR_Y) , PBRColorAttribute.createSpecular(COLOR_Y)) , VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal|VertexAttributes.Usage.TextureCoordinates);
        zHandleModel = modelBuilder.createArrow(0 , 0 , 0 , 0 , 0 , .5f , ARROW_CAP_SIZE , ARROW_THIKNESS , ARROW_DIVISIONS , GL20.GL_TRIANGLES , new Material(PBRColorAttribute.createEmissive(COLOR_Z) , PBRColorAttribute.createSpecular(COLOR_Z)) , VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal|VertexAttributes.Usage.TextureCoordinates);
        xyzHandleModel = modelBuilder.createSphere(.25f/5 , .25f/5 , .25f/5 , 20 , 20 , new Material(PBRColorAttribute.createSpecular(COLOR_XYZ),PBRColorAttribute.createEmissive(COLOR_XYZ)) , VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal|VertexAttributes.Usage.TextureCoordinates);

        xHandle = new ModelInstance(xHandleModel);
        yHandle = new ModelInstance(yHandleModel);
        zHandle = new ModelInstance(zHandleModel);
        xyzHandle = new ModelInstance(xyzHandleModel);

        xHandle.materials.first().set(PBRColorAttribute.createBaseColorFactor(COLOR_X));
        yHandle.materials.first().set(PBRColorAttribute.createBaseColorFactor(COLOR_Y));
        zHandle.materials.first().set(PBRColorAttribute.createBaseColorFactor(COLOR_Z));
        xyzHandle.materials.first().set(PBRColorAttribute.createBaseColorFactor(COLOR_XYZ));

        GameObject xHandleGO = new GameObject(Editor.i().sceneContext.sceneGraph, "xHandle", Suppliers.rInt(0,1000));
        xHandleGO.visible = false;

        GameObject yHandleGO = new GameObject(Editor.i().sceneContext.sceneGraph, "yHandle",Suppliers.rInt(0,1000));
        yHandleGO.visible = false;

        GameObject zHandleGO = new GameObject(Editor.i().sceneContext.sceneGraph, "zHandle",Suppliers.rInt(0,1000));
        zHandleGO.visible = false;
        Editor.i().engine.addEntity(xHandleGO.getEntity());
        Editor.i().engine.addEntity(yHandleGO.getEntity());
        Editor.i().engine.addEntity(zHandleGO.getEntity());

        xHandleGO.addComponent(new PickableModelComponent(xHandleGO, xHandle));
        yHandleGO.addComponent(new PickableModelComponent(yHandleGO, yHandle));
        zHandleGO.addComponent(new PickableModelComponent(zHandleGO, zHandle));
        xHandleGO.addComponent(new TransformComponent(xHandleGO, xHandle.transform));
        yHandleGO.addComponent(new TransformComponent(yHandleGO, yHandle.transform));
        zHandleGO.addComponent(new TransformComponent(zHandleGO, zHandle.transform));

        Model xCopy = modelBuilder.createArrow(0 , 0 , 0 , .5f , 0 , 0 , ARROW_CAP_SIZE , ARROW_THIKNESS , ARROW_DIVISIONS , GL20.GL_TRIANGLES , new Material(PBRColorAttribute.createEmissive(COLOR_X) , PBRColorAttribute.createSpecular(COLOR_X)) , VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal |VertexAttributes.Usage.TextureCoordinates);
        Model yCopy = modelBuilder.createArrow(0 , 0 , 0 , 0 , .5f , 0 , ARROW_CAP_SIZE , ARROW_THIKNESS , ARROW_DIVISIONS , GL20.GL_TRIANGLES , new Material(PBRColorAttribute.createEmissive(COLOR_Y) , PBRColorAttribute.createSpecular(COLOR_Y)) , VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal|VertexAttributes.Usage.TextureCoordinates);
        Model zCopy = modelBuilder.createArrow(0 , 0 , 0 , 0 , 0 , .5f , ARROW_CAP_SIZE , ARROW_THIKNESS , ARROW_DIVISIONS , GL20.GL_TRIANGLES , new Material(PBRColorAttribute.createEmissive(COLOR_Z) , PBRColorAttribute.createSpecular(COLOR_Z)) , VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal|VertexAttributes.Usage.TextureCoordinates);
        Model xyzCopy = modelBuilder.createSphere(.25f/5 , .25f/5 , .25f/5 , 20 , 20 , new Material(PBRColorAttribute.createSpecular(COLOR_XYZ),PBRColorAttribute.createEmissive(COLOR_XYZ)) , VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal|VertexAttributes.Usage.TextureCoordinates);
        ModelUtils.createOutlineModel(xCopy,Color.WHITE,0.005f);
        ModelUtils.createOutlineModel(yCopy,Color.WHITE,0.005f);
        ModelUtils.createOutlineModel(zCopy,Color.WHITE,0.005f);
        ModelUtils.createOutlineModel(xyzCopy,Color.WHITE,0.01f);

        xHandleCopy = new ModelInstance(xCopy);
        yHandleCopy = new ModelInstance(yCopy);
        zHandleCopy = new ModelInstance(zCopy);
        xyzHandleCopy = new ModelInstance(xyzCopy);

        Material outlineMaterial = MaterialUtils.getOutlineMaterial();
        outlineMaterial.set(IntAttribute.createCullFace(GL20.GL_FRONT));
        xHandleCopy.materials.get(0).clear();
        yHandleCopy.materials.get(0).clear();
        zHandleCopy.materials.get(0).clear();
        xyzHandleCopy.materials.get(0).clear();

        xHandleCopy.materials.get(0).set(outlineMaterial);
        yHandleCopy.materials.get(0).set(outlineMaterial);
        zHandleCopy.materials.get(0).set(outlineMaterial);
        xyzHandleCopy.materials.get(0).set(outlineMaterial);




        handles[0] = xHandleGO;
        handles[1] = yHandleGO;
        handles[2] = zHandleGO;

//       ray = Editor.i().viewportWidget.viewport.getPickRay(Gdx.input.getX(), Gdx.input.getY());

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        inputManager.resumeInput();
        transformState = TranslationState.DISABLED;
        if(selectedHandle!=null) {
            Editor.i().sceneContext.outlineRenderer.modelInstances.removeValue(selectedHandle.getEntity().getComponent(PickableModelComponent.class).getModelInstance(), true);
            selectedHandle = null;
        }

        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public void render(float delta) {
        if (!enabled) return;

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        batch.begin(Editor.i().sceneContext.cam);
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
        if (transformState== TranslationState.TRANSLATE_X){
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
            batch.render(yHandle, Editor.i().sceneContext.environment);
            batch.render(zHandle,Editor.i().sceneContext.environment);
            batch.render(xHandleCopy,Editor.i().sceneContext.environment);
            batch.render(xyzHandle,Editor.i().sceneContext.environment);
            batch.render(xyzHandleCopy,Editor.i().sceneContext.environment);
            Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
            batch.render(xHandle,Editor.i().sceneContext.environment);
            Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

        } else if (transformState== TranslationState.TRANSLATE_Y){
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
            batch.render(xHandle,Editor.i().sceneContext.environment);
            batch.render(zHandle,Editor.i().sceneContext.environment);
            batch.render(yHandleCopy,Editor.i().sceneContext.environment);
            batch.render(xyzHandle,Editor.i().sceneContext.environment);
            batch.render(xyzHandleCopy,Editor.i().sceneContext.environment);
            Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
            batch.render(yHandle, Editor.i().sceneContext.environment);
            Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        } else if (transformState== TranslationState.TRANSLATE_Z){
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
            batch.render(yHandle, Editor.i().sceneContext.environment);
            batch.render(xHandle,Editor.i().sceneContext.environment);
            batch.render(zHandleCopy,Editor.i().sceneContext.environment);
            batch.render(xyzHandle,Editor.i().sceneContext.environment);
            batch.render(xyzHandleCopy,Editor.i().sceneContext.environment);
            Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
            batch.render(zHandle,Editor.i().sceneContext.environment);
            Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        } else {

            Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
            batch.render(xHandle,Editor.i().sceneContext.environment);
            batch.render(yHandle,Editor.i().sceneContext.environment);
            batch.render(zHandle,Editor.i().sceneContext.environment);
            batch.render(xyzHandle,Editor.i().sceneContext.environment);
        }

        batch.end();

        if(PickingSystem.pickedObject == null) return;
    }

    @Override
    public void update(float delta) {
        if (PickingSystem.pickedObject == null) {
            return;
        }

        if (transformState != TranslationState.DISABLED) {
            inputManager.pauseInput(this);
        }

        translate();

        localPosition = new Vector3();
        PickingSystem.pickedObject.getPosition(position);
        PickingSystem.pickedObject.getTransform().getTranslation(localPosition);

        dst = position.dst(Editor.i().sceneContext.cam.position);
        dst = Math.sqrt(dst) / .8f;
        float outlineScale = 0;
        tmp.setToTranslationAndScaling(position , new Vector3((float) dst+outlineScale , (float) dst+outlineScale , (float) dst+outlineScale));
//        gizmo.transform.set(tmp);
        xHandleCopy.transform.set(tmp);
        yHandleCopy.transform.set(tmp);
        zHandleCopy.transform.set(tmp);
        xyzHandleCopy.transform.set(tmp);

        for (GameObject gameObject: handles){
            gameObject.setLocalPosition(localPosition.x, localPosition.y, localPosition.z);
            gameObject.setLocalScale((float) dst , (float) dst , (float) dst);
        };
        xyzHandle.transform.setToTranslationAndScaling(position , new Vector3((float) dst, (float) dst , (float) dst));


    }

    public void translate(){
        Ray ray = Editor.i().viewportWidget.viewport.getPickRay(Gdx.input.getX(), Gdx.input.getY());
        if(PickingSystem.pickedObject == null) return;
        Vector3 rayEnd = PickingSystem.pickedObject.getPosition(temp0);
        float dst = Editor.i().sceneContext.cam.position.dst(rayEnd);
        rayEnd = ray.getEndPoint(rayEnd, dst);

        if (initTranslate) {
            initTranslate = false;
            lastPos.set(rayEnd);
        }
        GameObject go = PickingSystem.pickedObject;
        boolean modified = false;
        Vector3 vec = new Vector3();
        if(transformState==TranslationState.TRANSLATE_X){
            vec.set(rayEnd.x - lastPos.x, 0, 0);
            modified = true;
        } else if(transformState==TranslationState.TRANSLATE_Y){
            vec.set(0, rayEnd.y - lastPos.y, 0);
            modified = true;
        } else if(transformState==TranslationState.TRANSLATE_Z){
            vec.set(0, 0, rayEnd.z - lastPos.z);
            modified = true;
        }
        if (go.getParent() != null) {
            // First, get the world transform from parent and apply translation
            Matrix4 worldTrans = tempMat0.set(go.getParent().getTransform());
            worldTrans.trn(vec.scl(-1)); // I believe we have to scale this by -1 due to inv()

            // Convert that new translation from world to local space for child
            Matrix4 localTrans = go.getTransform().mulLeft(worldTrans.inv());
            Vector3 localPos = localTrans.getTranslation(temp1);

            // apply position
            go.setLocalPosition(localPos.x, localPos.y, localPos.z);
        } else {
            go.translate(vec);
        }
        lastPos.set(rayEnd);

    }

    @Override
    public void enable() {
            enabled = true;
//            inputManager.pauseInput(this);
        Log.info("TranslateTool enabled");


    }

    @Override
    public void disable() {

        enabled = false;
//        inputManager.resumeInput();
        Log.info("TranslateTool disabled");
    }
}
