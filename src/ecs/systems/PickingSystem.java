package ecs.systems;

import backend.tools.Log;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import app.Editor;
import ecs.components.BaseComponent;
import ecs.components.PickableModelComponent;
import scene.graph.GameObject;
import scene.picking.GameObjectPicker;

public class PickingSystem extends IteratingSystem {

    public GameObjectPicker picker;
    public static GameObject pickedObject;
    public static boolean selectModeEnabled = true;

    public PickingSystem() {
        super(Family.one(PickableModelComponent.class).get());
        picker = new GameObjectPicker();
    }

    @Override
    public void update(float deltaTime) {
        if (!selectModeEnabled) return;
        if (Gdx.input.isButtonJustPressed(0)){
           GameObject obj = picker.pick(Editor.i().sceneContext, Gdx.input.getX(), Gdx.input.getY());
            if (obj != null) {
                setPickedObject(obj);
            }
        }

        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PickableModelComponent pickableModelComponent = entity.getComponent(PickableModelComponent.class);
        if (pickableModelComponent != null) {
            GameObject go = pickableModelComponent.gameObject;

        }
    }

    public void setPickedObject(GameObject obj){
        if (obj != null) {
        pickedObject = obj;
        for (BaseComponent c : pickedObject.getComponents()) {
            if (c instanceof PickableModelComponent component) {
                if(pickedObject.renderOutline) Editor.i().sceneContext.outlineRenderer.setModelInstance(component.getModelInstance());
            }
        }

        Editor.i().editorUI.inspectorWidget.setGameObject(pickedObject);
        Log.info("PickingSystem", "picked " + pickedObject.name);
    }}
}
