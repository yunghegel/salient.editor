package tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import app.Editor;
import core.graphics.SalientShapeRenderer;
import ecs.components.PickableModelComponent;
import ecs.components.TransformComponent;
import ecs.systems.PickingSystem;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRFlagAttribute;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import scene.graph.GameObject;
import utils.MaterialUtils;
import utils.Suppliers;

public class RotateTool extends AbstractTool<Matrix4>{

    public enum RotationState
    {
        DISABLED, ROTATE_X, ROTATE_Y, ROTATE_Z
    }

    public boolean enabled = false;
    public boolean initRotate = true;

    public RotationState rotationState = RotationState.DISABLED;

    public ModelInstance xHandle,yHandle,zHandle;
    public ModelInstance xHandleSelected,yHandleSelected,zHandleSelected;
    public ModelInstance gizmo;

    public ModelBatch batch;
    public GameObject[] handles = new GameObject[3];
    public GameObject selectedHandle;

    private Vector3 a = new Vector3();
    private Vector3 b = new Vector3();
    private Vector3 localPosition = new Vector3();
    private Vector3 position = new Vector3();
    private Matrix4 tmp = new Matrix4();
    private Quaternion tmpQ = new Quaternion();

    private SalientShapeRenderer drawer;


    float degree = 0;
    float rot = 0;
    float lastRot = 0;
    double dst = 0;


    public RotateTool(ModelBatch batch){
        this.batch =batch;
        SceneAsset x = new GLTFLoader().load(Gdx.files.internal("models/rotate_gizmo_x.gltf"));
        SceneAsset y = new GLTFLoader().load(Gdx.files.internal("models/rotate_gizmo_y.gltf"));
        SceneAsset z = new GLTFLoader().load(Gdx.files.internal("models/rotate_gizmo_z.gltf"));
        SceneAsset gizmoasset = new GLTFLoader().load(Gdx.files.internal("models/rotate_gizmo.gltf"));
        Scene gizmoScene = new Scene(gizmoasset.scene);
        Scene xScene = new Scene(x.scene);
        Scene yScene = new Scene(y.scene);
        Scene zScene = new Scene(z.scene);
        xHandle = xScene.modelInstance;
        xHandle.materials.get(0).clear();
        xHandle.materials.get(0).set(IntAttribute.createCullFace(0));
        xHandle.materials.get(0).set(PBRColorAttribute.createSpecular(Color.BLUE));
        xHandle.materials.get(0).set(PBRColorAttribute.createBaseColorFactor(Color.BLUE));
        xHandle.materials.get(0).set(PBRColorAttribute.createEmissive(Color.BLUE));
        xHandle.materials.get(0).set(PBRColorAttribute.createAmbient(Color.BLUE));
        xHandle.materials.get(0).set(PBRColorAttribute.createReflection(Color.BLUE));
        xHandle.materials.get(0).set(FloatAttribute.createShininess(1f));
        xHandle.materials.get(0).set(new PBRFlagAttribute(PBRFlagAttribute.Unlit));

        yHandle = yScene.modelInstance;
        yHandle.materials.get(0).clear();
        yHandle.materials.get(0).set(IntAttribute.createCullFace(0));
        yHandle.materials.get(0).set(PBRColorAttribute.createSpecular(Color.GREEN));
        yHandle.materials.get(0).set(PBRColorAttribute.createBaseColorFactor(Color.GREEN));
        yHandle.materials.get(0).set(PBRColorAttribute.createEmissive(Color.GREEN));
        yHandle.materials.get(0).set(PBRColorAttribute.createAmbient(Color.GREEN));
        yHandle.materials.get(0).set(PBRColorAttribute.createReflection(Color.GREEN));
        yHandle.materials.get(0).set(new PBRFlagAttribute(PBRFlagAttribute.Unlit));

        zHandle = zScene.modelInstance;
        zHandle.materials.get(0).clear();
        zHandle.materials.get(0).set(IntAttribute.createCullFace(0));
        zHandle.materials.get(0).set(PBRColorAttribute.createSpecular(Color.RED));
        zHandle.materials.get(0).set(PBRColorAttribute.createBaseColorFactor(Color.RED));
        zHandle.materials.get(0).set(PBRColorAttribute.createEmissive(Color.RED));
        zHandle.materials.get(0).set(PBRColorAttribute.createAmbient(Color.RED));
        zHandle.materials.get(0).set(PBRColorAttribute.createReflection(Color.RED));
        zHandle.materials.get(0).set(new PBRFlagAttribute(PBRFlagAttribute.Unlit));

        gizmo = gizmoScene.modelInstance;
        GameObject xHandleGO = new GameObject(Editor.i().sceneContext.sceneGraph, "xHandle", Suppliers.rInt(0,1000));
        xHandleGO.visible = false;
//        xHandleGO.renderOutline = false;
//        Editor.i().sceneContext.sceneGraph.addGameObject(xHandleGO);
        GameObject yHandleGO = new GameObject(Editor.i().sceneContext.sceneGraph, "yHandle",Suppliers.rInt(0,1000));
        yHandleGO.visible = false;
//        yHandleGO.renderOutline = false;
//        Editor.i().sceneContext.sceneGraph.addGameObject(yHandleGO);
        GameObject zHandleGO = new GameObject(Editor.i().sceneContext.sceneGraph, "zHandle",Suppliers.rInt(0,1000));
//        zHandleGO.renderOutline = false;
        zHandleGO.visible = false;
//        Editor.i().sceneContext.sceneGraph.addGameObject(zHandleGO);
        xHandleGO.addComponent(new PickableModelComponent(xHandleGO, xHandle));
        yHandleGO.addComponent(new PickableModelComponent(yHandleGO, yHandle));
        zHandleGO.addComponent(new PickableModelComponent(zHandleGO, zHandle));
        xHandleGO.addComponent(new TransformComponent(xHandleGO, xHandle.transform));
        yHandleGO.addComponent(new TransformComponent(yHandleGO, yHandle.transform));
        zHandleGO.addComponent(new TransformComponent(zHandleGO, zHandle.transform));
        handles[0] = xHandleGO;
        handles[1] = yHandleGO;
        handles[2] = zHandleGO;

        Editor.i().engine.addEntity(xHandleGO.getEntity());
        Editor.i().engine.addEntity(yHandleGO.getEntity());
        Editor.i().engine.addEntity(zHandleGO.getEntity());

        SceneAsset xC = new GLTFLoader().load(Gdx.files.internal("models/rotate_gizmo_x.gltf"));
        SceneAsset yC = new GLTFLoader().load(Gdx.files.internal("models/rotate_gizmo_y.gltf"));
        SceneAsset zC = new GLTFLoader().load(Gdx.files.internal("models/rotate_gizmo_z.gltf"));

        Scene xCScene = new Scene(xC.scene);
        Scene yCScene = new Scene(yC.scene);
        Scene zCScene = new Scene(zC.scene);

        Material outlineMaterial = MaterialUtils.getOutlineMaterial();

        xHandleSelected = xCScene.modelInstance;
        yHandleSelected = yCScene.modelInstance;
        zHandleSelected = zCScene.modelInstance;

//        ModelUtils.createOutlineModel(xHandleSelected.model, Color.WHITE,0.1f);
//        ModelUtils.createOutlineModel(yHandleSelected.model, Color.WHITE,0.1f);
//        ModelUtils.createOutlineModel(zHandleSelected.model, Color.WHITE,0.1f);


//        xHandleSelected.materials.clear();
        xHandleSelected.materials.get(0).clear();
        xHandleSelected.materials.get(0).set(outlineMaterial);

//        xHandleSelected.materials.get(0).set(IntAttribute.createCullFace(GL20.GL_FRONT));
//        xHandleSelected.materials.get(0).set(PBRColorAttribute.createEmissive(Color.WHITE));
//        xHandleSelected.materials.get(0).set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
//        xHandleSelected.materials.get(0).set(new BlendingAttribute(0.3f));

//        yHandleSelected.materials.clear();
        yHandleSelected.materials.get(0).clear();
        yHandleSelected.materials.get(0).set(outlineMaterial);


        zHandleSelected.materials.get(0).clear();
        zHandleSelected.materials.get(0).set(outlineMaterial);



        drawer = Editor.i().drawer;




    }

    @Override
    public void render(float delta) {
        if (!enabled) return;
        calculateAngle();
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        batch.begin(Editor.i().sceneContext.cam);
        if (rotationState==RotationState.ROTATE_X){
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
            batch.render(yHandle, Editor.i().sceneContext.environment);
            batch.render(zHandle,Editor.i().sceneContext.environment);

            batch.render(xHandleSelected,Editor.i().sceneContext.environment);
            Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
            batch.render(xHandle,Editor.i().sceneContext.environment);
            Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

        } else if (rotationState==RotationState.ROTATE_Y){
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
            batch.render(xHandle,Editor.i().sceneContext.environment);
            batch.render(zHandle,Editor.i().sceneContext.environment);
            batch.render(yHandleSelected,Editor.i().sceneContext.environment);
            Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
            batch.render(yHandle, Editor.i().sceneContext.environment);
            Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        } else if (rotationState==RotationState.ROTATE_Z){
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
            batch.render(yHandle, Editor.i().sceneContext.environment);
            batch.render(xHandle,Editor.i().sceneContext.environment);
            batch.render(zHandleSelected,Editor.i().sceneContext.environment);
            Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
            batch.render(zHandle,Editor.i().sceneContext.environment);
            Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        } else {
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
            batch.render(xHandle,Editor.i().sceneContext.environment);
            batch.render(yHandle,Editor.i().sceneContext.environment);
            batch.render(zHandle,Editor.i().sceneContext.environment);
        }

        batch.end();

        if(PickingSystem.pickedObject == null) return;
        Ray ray = Editor.i().viewportWidget.viewport.getPickRay(Gdx.input.getX(), Gdx.input.getY());

        drawer.setProjectionMatrix(Editor.i().sceneContext.cam.combined);
        drawer.begin(ShapeRenderer.ShapeType.Line);
        drawer.setColor(Color.RED);
        drawer.arc(position.x, position.y, position.z, new Vector3(1,1,1),1,0, degree, 300);
        drawer.end();


    }

    @Override
    public void update(float delta) {
        if (PickingSystem.pickedObject == null) {
            return;
        }

        if (rotationState != RotationState.DISABLED) {
            inputManager.pauseInput(this);
        }

        rotate();

        localPosition = new Vector3();
        PickingSystem.pickedObject.getPosition(position);
        PickingSystem.pickedObject.getTransform().getTranslation(localPosition);

        dst = position.dst(Editor.i().sceneContext.cam.position);
        dst = Math.sqrt(dst) / 3;
        float outlineScale = 0.05f;
        tmp.setToTranslationAndScaling(position , new Vector3((float) dst+outlineScale , (float) dst+outlineScale , (float) dst+outlineScale));

        xHandleSelected.transform.set(tmp);
        yHandleSelected.transform.set(tmp);
        zHandleSelected.transform.set(tmp);



//        gizmo.transform.set(tmp);
        for (GameObject gameObject: handles){
            gameObject.setLocalPosition(localPosition.x, localPosition.y, localPosition.z);
            gameObject.setLocalScale((float) dst , (float) dst , (float) dst);
        };


    }

    public void rotate(){
        if (initRotate){
            initRotate = false;
        }

        rot = degree - lastRot;



        if (rotationState==RotationState.ROTATE_X){
            tmpQ.setEulerAngles(0, -rot, 0);
            PickingSystem.pickedObject.rotate(tmpQ);

        }
        if (rotationState==RotationState.ROTATE_Y) {
            tmpQ.setEulerAngles(0, 0, -rot);
            PickingSystem.pickedObject.rotate(tmpQ);

        }
        if (rotationState==RotationState.ROTATE_Z) {
            tmpQ.setEulerAngles(-rot,0, 0);
            PickingSystem.pickedObject.rotate(tmpQ);

        }

        lastRot = degree;
    }

    @Override
    public void enable() {
        enabled = true;

        inputManager.pauseInput(this);
//
    }

    @Override
    public void disable() {
        enabled = false;
//        inputManager.resumeInput();

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//        for (GameObject gameObject: handles){
//            GameObject picked = Editor.i().ecs.pickingSystem.picker.pick(Editor.i().sceneContext,screenX,screenY,handles);
//            if (picked != null) {
//                selectedHandle = picked;
//                PickableModelComponent pickableModelComponent = picked.getEntity().getComponent(PickableModelComponent.class);
//                Editor.i().sceneContext.outlineRenderer.modelInstances.add(pickableModelComponent.getModelInstance());
//                System.out.println("picked: " + picked.name);
//                if (selectedHandle.id==handles[0].id) {
//                    rotationState = RotationState.ROTATE_X;
//                    System.out.println("ROTATE_X");
//                } else if (selectedHandle.id==handles[1].id) {
//                    rotationState = RotationState.ROTATE_Y;
//                    System.out.println("ROTATE_Y");
//                } else if (selectedHandle.id==handles[2].id) {
//                    rotationState = RotationState.ROTATE_Z;
//                    System.out.println("ROTATE_Z");
//                }
//                return true;
//            }
//        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        rotationState = RotationState.DISABLED;
        if(selectedHandle!=null) {
            Editor.i().sceneContext.outlineRenderer.modelInstances.removeValue(selectedHandle.getEntity().getComponent(PickableModelComponent.class).getModelInstance(), true);
            selectedHandle = null;
        }
        inputManager.resumeInput();

        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        return super.touchDragged(screenX, screenY, pointer);
    }

    public float calculateAngle(){
        Ray ray = Editor.i().viewportWidget.viewport.getPickRay(Gdx.input.getX(), Gdx.input.getY());

        a = Editor.i().sceneContext.cam.project(position);
        b = Editor.i().sceneContext.cam.project(ray.origin);
        degree = (float)Math.toDegrees(Math.atan2(b.x-a.x, b.y-a.y));
        if (degree<0){
            degree = 360+degree;
        }
        return degree;
    }

}
