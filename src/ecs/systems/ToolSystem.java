package ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import app.Editor;
import core.graphics.SalientShapeRenderer;
import ecs.components.PickableModelComponent;
import ecs.components.TransformComponent;
import scene.graph.GameObject;
import tools.RotateTool;
import tools.ScaleTool;
import tools.ToolManager;
import tools.TranslateTool;

public class ToolSystem extends IteratingSystem {

    public ToolManager toolManager;
    public SalientShapeRenderer shapeRenderer;


    public ToolSystem() {
        super(Family.all(PickableModelComponent.class, TransformComponent.class).get());
        toolManager = new ToolManager();
        shapeRenderer = new SalientShapeRenderer(Editor.i().sceneContext.cam);
        shapeRenderer.setProjectionMatrix(Editor.i().sceneContext.cam.combined);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        toolManager.update(deltaTime);

        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            toggleTranslateTool();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            toggleScaleTool();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            toggleRotateTool();
        }



        pickRotateHandle();
        pickTranslateHandle();
        pickScaleHandle();

    }

    private void pickRotateHandle() {
        if(toolManager.rotateTool.enabled){
        if (Gdx.input.isButtonJustPressed(0)){
            for (GameObject gameObject: toolManager.rotateTool.handles){
                GameObject picked = Editor.i().ecs.pickingSystem.picker.pick(Editor.i().sceneContext,Gdx.input.getX(), Gdx.input.getY(),toolManager.rotateTool.handles);
                if (picked != null) {
                    toolManager.rotateTool.selectedHandle = picked;
                    PickableModelComponent pickableModelComponent = picked.getEntity().getComponent(PickableModelComponent.class);
                    if(Editor.i().sceneContext.outlineRenderer.modelInstances.contains(pickableModelComponent.getModelInstance(), true)){
                        Editor.i().sceneContext.outlineRenderer.modelInstances.add(pickableModelComponent.getModelInstance());
                    }
                    System.out.println("picked: " + picked.name);
                    if (toolManager.rotateTool.selectedHandle.id==toolManager.rotateTool.handles[0].id) {
                        toolManager.rotateTool.rotationState = RotateTool.RotationState.ROTATE_X;
//                        Editor.i().sceneContext.outlineRenderer.modelInstances.add(pickableModelComponent.getModelInstance());
                        System.out.println("ROTATE_X");
                    } else if (toolManager.rotateTool.selectedHandle.id==toolManager.rotateTool.handles[1].id) {
                        toolManager.rotateTool.rotationState = RotateTool.RotationState.ROTATE_Y;
//                        Editor.i().sceneContext.outlineRenderer.modelInstances.add(pickableModelComponent.getModelInstance());
                        System.out.println("ROTATE_Y");
                    } else if (toolManager.rotateTool.selectedHandle.id==toolManager.rotateTool.handles[2].id) {
                        toolManager.rotateTool.rotationState = RotateTool.RotationState.ROTATE_Z;
//                        Editor.i().sceneContext.outlineRenderer.modelInstances.add(pickableModelComponent.getModelInstance());
                        System.out.println("ROTATE_Z");
                    } else {
                        if(Editor.i().sceneContext.outlineRenderer.modelInstances.contains(pickableModelComponent.getModelInstance(), true)){
                            Editor.i().sceneContext.outlineRenderer.modelInstances.removeValue(pickableModelComponent.getModelInstance(), true);
                        }
                    }

                }
            }
        }
        }
    }

    private void pickTranslateHandle(){
        if(toolManager.translateTool.enabled){
            if (Gdx.input.isButtonJustPressed(0)){
                for (GameObject gameObject: toolManager.translateTool.handles){
                    GameObject picked = Editor.i().ecs.pickingSystem.picker.pick(Editor.i().sceneContext,Gdx.input.getX(), Gdx.input.getY(),toolManager.translateTool.handles);
                    if (picked != null) {
                        toolManager.translateTool.selectedHandle = picked;
                        PickableModelComponent pickableModelComponent = picked.getEntity().getComponent(PickableModelComponent.class);
                        if(Editor.i().sceneContext.outlineRenderer.modelInstances.contains(pickableModelComponent.getModelInstance(), true)){
                            Editor.i().sceneContext.outlineRenderer.modelInstances.removeValue(pickableModelComponent.getModelInstance(), true);
                        }
                        System.out.println("picked: " + picked.name);
                        if (toolManager.translateTool.selectedHandle.id==toolManager.translateTool.handles[0].id) {
                            toolManager.translateTool.transformState = TranslateTool.TranslationState.TRANSLATE_X;}
                        else if (toolManager.translateTool.selectedHandle.id==toolManager.translateTool.handles[1].id) {
                            toolManager.translateTool.transformState = TranslateTool.TranslationState.TRANSLATE_Y;}
                         else if (toolManager.translateTool.selectedHandle.id ==toolManager.translateTool.handles[2].id){
                        toolManager.translateTool.transformState = TranslateTool.TranslationState.TRANSLATE_Z;}
                    else {
                        if(Editor.i().sceneContext.outlineRenderer.modelInstances.contains(pickableModelComponent.getModelInstance(), true)){
                            Editor.i().sceneContext.outlineRenderer.modelInstances.removeValue(pickableModelComponent.getModelInstance(), true);
                        }
                    }

                }

    }}}}

    public void pickScaleHandle(){
        if(toolManager.scaleTool.enabled){
            if (Gdx.input.isButtonJustPressed(0)){
                for (GameObject gameObject: toolManager.scaleTool.handles){
                    GameObject picked = Editor.i().ecs.pickingSystem.picker.pick(Editor.i().sceneContext,Gdx.input.getX(), Gdx.input.getY(),toolManager.scaleTool.handles);
                    if (picked != null) {
                        toolManager.scaleTool.selectedHandle = picked;
                        PickableModelComponent pickableModelComponent = picked.getEntity().getComponent(PickableModelComponent.class);
                        if(Editor.i().sceneContext.outlineRenderer.modelInstances.contains(pickableModelComponent.getModelInstance(), true)){
                            Editor.i().sceneContext.outlineRenderer.modelInstances.removeValue(pickableModelComponent.getModelInstance(), true);
                        }
                        System.out.println("picked: " + picked.name);
                        if (toolManager.scaleTool.selectedHandle.id==toolManager.scaleTool.handles[0].id) {
                            toolManager.scaleTool.scaleState = ScaleTool.ScaleState.SCALE_X;}
                        else if (toolManager.scaleTool.selectedHandle.id==toolManager.scaleTool.handles[1].id) {
                            toolManager.scaleTool.scaleState = ScaleTool.ScaleState.SCALE_Y;}
                        else if (toolManager.scaleTool.selectedHandle.id ==toolManager.scaleTool.handles[2].id){
                            toolManager.scaleTool.scaleState = ScaleTool.ScaleState.SCALE_Z;}
                        else if (toolManager.scaleTool.selectedHandle.id ==toolManager.scaleTool.handles[3].id) {
                            {
                                toolManager.scaleTool.scaleState = ScaleTool.ScaleState.SCALE_XYZ;}
                        }
                        else {
                            if(Editor.i().sceneContext.outlineRenderer.modelInstances.contains(pickableModelComponent.getModelInstance(), true)){
                                Editor.i().sceneContext.outlineRenderer.modelInstances.removeValue(pickableModelComponent.getModelInstance(), true);
                            }
                        }

                    }

                }

            }}}



    private void toggleTranslateTool() {
        if (toolManager.translateTool.enabled) {
            toolManager.translateTool.disable();
        } else {
            toolManager.translateTool.enable();
        }
    }

    private void toggleScaleTool() {
        if (toolManager.scaleTool.enabled) {
            toolManager.scaleTool.disable();
        } else {
            toolManager.scaleTool.enable();
        }
    }

    private void toggleRotateTool() {
        if (toolManager.rotateTool.enabled) {
            toolManager.rotateTool.disable();
        } else {
            toolManager.rotateTool.enable();
        }

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
    }

    public void render(float delta){
        toolManager.render(delta);
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.setProjectionMatrix(Editor.i().sceneContext.cam.combined);
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(1, 1, 1, .5f);
        Vector3 target = Editor.i().sceneContext.cameraController.target.cpy();
        Vector3 targetX = target.cpy();
        targetX.x += 0.125f;
        Vector3 targetY = target.cpy();
        targetY.y += 0.125f;
        Vector3 targetZ = target.cpy();
        targetZ.z += 0.125f;
        Vector3 targetNegX = target.cpy();
        targetNegX.x -= 0.125f;
        Vector3 targetNegY = target.cpy();
        targetNegY.y -= 0.125f;
        Vector3 targetNegZ = target.cpy();
        targetNegZ.z -= 0.125f;

        shapeRenderer.setColor(.9f, .9f, .9f, .5f);
        shapeRenderer.line(target, targetNegX);
        shapeRenderer.line(target, targetNegZ);
        shapeRenderer.line(target.cpy(), targetX);
        shapeRenderer.line(target.cpy(), targetZ);
        shapeRenderer.circle(target.cpy(), Vector3.Y,0.125f,30);





        shapeRenderer.end();

    }
}
