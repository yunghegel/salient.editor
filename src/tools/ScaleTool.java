package tools;

import app.Editor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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
import ecs.components.PickableModelComponent;
import ecs.components.TransformComponent;
import ecs.systems.PickingSystem;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import scene.graph.GameObject;
import utils.MaterialUtils;
import utils.ModelUtils;
import utils.Suppliers;

public class ScaleTool extends AbstractTool<Matrix4>{

    protected static Color COLOR_X = Color.RED;
    protected static Color COLOR_Y = Color.GREEN;
    protected static Color COLOR_Z = Color.BLUE;
    protected static Color COLOR_XYZ = Color.CYAN;



    public enum ScaleState
    {
        NONE, SCALE_X, SCALE_Y, SCALE_Z, SCALE_XYZ
    }

    public ModelInstance xHandle, yHandle, zHandle, xyzHandle;
    public ModelInstance xHandleOutline, yHandleOutline, zHandleOutline, xyzPlaneHandleOutline;
    public GameObject[] handles = new GameObject[3];
    public GameObject selectedHandle;

    public ScaleState scaleState = ScaleState.NONE;
    public boolean enabled = false;
    private boolean initTranslate = true;

    double dst = 0;
    private Vector3 localPosition = new Vector3();
    private Vector3 position = new Vector3();
    private Matrix4 tmp = new Matrix4();
    private final Vector3 lastPos = new Vector3();
    private final Vector3 temp0 = new Vector3();
    private final Vector3 temp1 = new Vector3();
    private final Matrix4 tempMat0 = new Matrix4();

    public ModelBatch batch;

    public ScaleTool(ModelBatch batch) {
        super();
        this.batch = batch;
        ModelBuilder modelBuilder = new ModelBuilder();

        Material matXYZ = new Material(PBRColorAttribute.createEmissive(COLOR_XYZ) , PBRColorAttribute.createSpecular(COLOR_XYZ));

        Model xPlaneHandleModel = ModelUtils.createArrowStub(MaterialUtils.createGenericBDSFMaterial(COLOR_X) , Vector3.Zero , new Vector3(1 , 0 , 0));
        Model yPlaneHandleModel = ModelUtils.createArrowStub(MaterialUtils.createGenericBDSFMaterial(COLOR_Y) , Vector3.Zero , new Vector3(0 , 1 , 0));
        Model zPlaneHandleModel = ModelUtils.createArrowStub(MaterialUtils.createGenericBDSFMaterial(COLOR_Z) , Vector3.Zero , new Vector3(0 , 0 , 1));
        Model xyzPlaneHandleModel = modelBuilder.createBox(0.08f , 0.08f , 0.08f , MaterialUtils.createGenericBDSFMaterial(COLOR_XYZ) , VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal| VertexAttributes.Usage.TextureCoordinates);

        xHandle = new ModelInstance(xPlaneHandleModel);
        yHandle = new ModelInstance(yPlaneHandleModel);
        zHandle = new ModelInstance(zPlaneHandleModel);
        xyzHandle = new ModelInstance(xyzPlaneHandleModel);

        Model xHandleCopy = ModelUtils.createArrowStub(MaterialUtils.createGenericBDSFMaterial(COLOR_X) , Vector3.Zero , new Vector3(1 , 0 , 0));
        Model yHandleCopy = ModelUtils.createArrowStub(MaterialUtils.createGenericBDSFMaterial(COLOR_Y) , Vector3.Zero , new Vector3(0 , 1 , 0));
        Model zHandleCopy = ModelUtils.createArrowStub(MaterialUtils.createGenericBDSFMaterial(COLOR_Z) , Vector3.Zero , new Vector3(0 , 0 , 1));
        Model xyzHandleCopy = modelBuilder.createBox(0.08f , 0.08f , 0.08f , MaterialUtils.createGenericBDSFMaterial(COLOR_XYZ) , VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        ModelUtils.createOutlineModel(xHandleCopy,Color.WHITE,0.005f);
        ModelUtils.createOutlineModel(yHandleCopy,Color.WHITE,0.005f);
        ModelUtils.createOutlineModel(zHandleCopy,Color.WHITE,0.005f);
        ModelUtils.createOutlineModel(xyzHandleCopy,Color.WHITE,0.005f);

        xHandleOutline = new ModelInstance(xHandleCopy);
        yHandleOutline = new ModelInstance(yHandleCopy);
        zHandleOutline = new ModelInstance(zHandleCopy);
        xyzPlaneHandleOutline = new ModelInstance(xyzHandleCopy);

        GameObject xHandleGO = new GameObject(Editor.i().sceneContext.sceneGraph, "xHandle", Suppliers.rInt(0,1000));
        xHandleGO.visible = false;

        GameObject yHandleGO = new GameObject(Editor.i().sceneContext.sceneGraph, "yHandle",Suppliers.rInt(0,1000));
        yHandleGO.visible = false;

        GameObject zHandleGO = new GameObject(Editor.i().sceneContext.sceneGraph, "zHandle",Suppliers.rInt(0,1000));
        zHandleGO.visible = false;

        GameObject xyzHandleGO = new GameObject(Editor.i().sceneContext.sceneGraph, "xyzHandle",Suppliers.rInt(0,1000));
        xyzHandleGO.visible = false;

        Editor.i().engine.addEntity(xHandleGO.getEntity());
        Editor.i().engine.addEntity(yHandleGO.getEntity());
        Editor.i().engine.addEntity(zHandleGO.getEntity());
        Editor.i().engine.addEntity(xyzHandleGO.getEntity());

        xHandleGO.addComponent(new PickableModelComponent(xHandleGO, xHandle));
        yHandleGO.addComponent(new PickableModelComponent(yHandleGO, yHandle));
        zHandleGO.addComponent(new PickableModelComponent(zHandleGO, zHandle));
        xyzHandleGO.addComponent(new PickableModelComponent(xyzHandleGO, xyzHandle));
        xHandleGO.addComponent(new TransformComponent(xHandleGO, xHandle.transform));
        yHandleGO.addComponent(new TransformComponent(yHandleGO, yHandle.transform));
        zHandleGO.addComponent(new TransformComponent(zHandleGO, zHandle.transform));
        xyzHandleGO.addComponent(new TransformComponent(xyzHandleGO, xyzHandle.transform));


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

    }

    public void scale(){
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

        if (scaleState == ScaleState.SCALE_X) {
            vec.set(1 + rayEnd.x - lastPos.x , 1 , 1);
            modified = true;
        }

        else if (scaleState == ScaleState.SCALE_Y) {
            vec.set(1 , 1 + rayEnd.y - lastPos.y , 1);
            modified = true;
        }
        else if (scaleState == ScaleState.SCALE_Z) {
            vec.set(1 , 1 , 1 + rayEnd.z - lastPos.z);
            modified = true;
        }
        else if (scaleState == ScaleState.SCALE_XYZ) {
            vec.set(1 + rayEnd.x - lastPos.x , 1 + rayEnd.y - lastPos.y , 1 + rayEnd.z - lastPos.z);
            float avg = ( vec.x + vec.y + vec.z ) / 3;
            vec.set(avg , avg , avg);
            modified = true;
        }
        if (vec.x == 0) vec.x = 1;
        if (vec.y == 0) vec.y = 1;
        if (vec.z == 0) vec.z = 1;


        go.scale(vec);

        lastPos.set(rayEnd);

    }

    @Override
    public void render(float delta) {
        if (!enabled) return;

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        batch.begin(Editor.i().sceneContext.cam);
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
        if (scaleState== ScaleState.SCALE_X){
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
            batch.render(yHandle, Editor.i().sceneContext.environment);
            batch.render(zHandle,Editor.i().sceneContext.environment);
            batch.render(xHandleOutline,Editor.i().sceneContext.environment);
            batch.render(xyzHandle,Editor.i().sceneContext.environment);

            Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
            batch.render(xHandle,Editor.i().sceneContext.environment);
            Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

        } else if (scaleState== ScaleState.SCALE_Y){
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
            batch.render(xHandle,Editor.i().sceneContext.environment);
            batch.render(zHandle,Editor.i().sceneContext.environment);
            batch.render(yHandleOutline,Editor.i().sceneContext.environment);
            batch.render(xyzHandle,Editor.i().sceneContext.environment);

            Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
            batch.render(yHandle, Editor.i().sceneContext.environment);
            Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        } else if (scaleState== ScaleState.SCALE_Z){
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
            batch.render(yHandle, Editor.i().sceneContext.environment);
            batch.render(xHandle,Editor.i().sceneContext.environment);
            batch.render(zHandleOutline,Editor.i().sceneContext.environment);
            batch.render(xyzHandle,Editor.i().sceneContext.environment);

            Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
            batch.render(zHandle,Editor.i().sceneContext.environment);
            Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        } else if(scaleState== ScaleState.SCALE_XYZ){

            Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
            batch.render(xHandle,Editor.i().sceneContext.environment);
            batch.render(yHandle,Editor.i().sceneContext.environment);
            batch.render(zHandle,Editor.i().sceneContext.environment);
            batch.render(xyzPlaneHandleOutline,Editor.i().sceneContext.environment);
            Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
            batch.render(xyzHandle,Editor.i().sceneContext.environment);
            Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

        } else {
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

        if (scaleState != ScaleState.NONE) {
            inputManager.pauseInput(this);
        }

        scale();

        localPosition = new Vector3();
        PickingSystem.pickedObject.getPosition(position);
        PickingSystem.pickedObject.getTransform().getTranslation(localPosition);

        dst = position.dst(Editor.i().sceneContext.cam.position);
        dst = Math.sqrt(dst) / .8f;
        float outlineScale = 0;
        tmp.setToTranslationAndScaling(position , new Vector3((float) dst+outlineScale , (float) dst+outlineScale , (float) dst+outlineScale));
//        gizmo.transform.set(tmp);
        xHandleOutline.transform.set(tmp);
        yHandleOutline.transform.set(tmp);
        zHandleOutline.transform.set(tmp);
        xyzPlaneHandleOutline.transform.set(tmp);


        for (GameObject gameObject: handles){
            gameObject.setLocalPosition(localPosition.x, localPosition.y, localPosition.z);
            gameObject.setLocalScale((float) dst , (float) dst , (float) dst);
        };
        xyzHandle.transform.setToTranslationAndScaling(position , new Vector3((float) dst, (float) dst , (float) dst));
    }

    @Override
    public void enable() {
        enabled = true;

    }

    @Override
    public void disable() {
        enabled = false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        inputManager.resumeInput();
        scaleState = ScaleState.NONE;
        if(selectedHandle!=null) {
            Editor.i().sceneContext.outlineRenderer.modelInstances.removeValue(selectedHandle.getEntity().getComponent(PickableModelComponent.class).getModelInstance(), true);
            selectedHandle = null;
        }
        return super.touchUp(screenX, screenY, pointer, button);
    }
}
