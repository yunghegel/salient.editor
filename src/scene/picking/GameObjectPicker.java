package scene.picking;

import backend.tools.Log;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import ecs.components.BaseComponent;
import ecs.components.PickableModelComponent;
import ecs.components.lights.PickablePointLightComponent;
import ecs.components.lights.PointLightComponent;
import scene.SceneContext;
import scene.graph.GameObject;
import scene.graph.SceneGraph;

public class GameObjectPicker extends BasePicker {

    public GameObjectPicker() {
        super();
    }

    public GameObject pick(SceneContext scene, int screenX, int screenY) {
        begin(scene.viewport);
        renderPickableScene(scene.sceneGraph);
        end();
        Pixmap pm = getFrameBufferPixmap(scene.viewport);

        int x = screenX - scene.viewport.getScreenX();
        int y = screenY - (Gdx.graphics.getHeight() - (scene.viewport.getScreenY() + scene.viewport.getScreenHeight()));

        int id = PickerColorEncoder.decode(pm.getPixel(x, y));
        for (GameObject go : scene.sceneGraph.getGameObjects()) {
            if (id == go.id) return go;
            if (go.getChildren() != null) {
                for (GameObject child : go.getChildren()) {
                    if (id == child.id){
                        Log.info("Picked child: " + child.name + " with id: " + child.id);
                        return child;
                    }
                }
            }

        }

        return null;
    }

    public GameObject pick(SceneContext scene,int screenX,int screenY,GameObject go){
        begin(scene.viewport);
        renderPickableGameObject(go);
        end();
        Pixmap pm = getFrameBufferPixmap(scene.viewport);
        int x = screenX - scene.viewport.getScreenX();
        int y = screenY - (Gdx.graphics.getHeight() - (scene.viewport.getScreenY() + scene.viewport.getScreenHeight()));
        int id = PickerColorEncoder.decode(pm.getPixel(x, y));
        if (id == go.id){
            System.out.println("Picked id: " + go.id);
            return go;
        }
        return null;
    }

    public GameObject pick(SceneContext scene,int screenX,int screenY,GameObject[] objects){
        begin(scene.viewport);
        renderPickableGameObjects(objects,scene.sceneGraph);
        end();
        Pixmap pm = getFrameBufferPixmap(scene.viewport);
        int x = screenX - scene.viewport.getScreenX();
        int y = screenY - (Gdx.graphics.getHeight() - (scene.viewport.getScreenY() + scene.viewport.getScreenHeight()));

        int id = PickerColorEncoder.decode(pm.getPixel(x, y));
        Log.info("GameObjectPicker", "Picked id: " + id);
        for (GameObject go : objects) {
            if (id == go.id) return go;
            if (go.getChildren() != null) {
                for (GameObject child : go.getChildren()) {
                    if (id == child.id){
                        Log.info("Picked child: " + child.name + " with id: " + child.id);
                        return child;
                    }
                }
            }

        }
        return null;
    }

    public int pick(SceneGraph sceneGraph, int screenX, int screenY, Pickable[] pickables){
        begin(sceneGraph.scene.viewport);
        renderPickables(pickables,sceneGraph);
        end();
        Pixmap pm = getFrameBufferPixmap(sceneGraph.scene.viewport);

        int x = screenX - sceneGraph.scene.viewport.getScreenX();
        int y = screenY - (Gdx.graphics.getHeight() - (sceneGraph.scene.viewport.getScreenY() + sceneGraph.scene.viewport.getScreenHeight()));

        int id = PickerColorEncoder.decode(pm.getPixel(x, y));
        Log.info("GameObjectPicker", "Picked id: " + id+", PixMap id: "+pm.getPixel(x,y));
        for (int i = 0; i < pickables.length; i++) {
            if (id == pickables[i].getId()) {
                Log.info("Picked id: " + pickables[i].getId());
                return i;
            }

        }

        return -1;
    }

    private void renderPickableScene(SceneGraph sceneGraph) {
        sceneGraph.scene.batch.begin(sceneGraph.scene.cam);
        for (GameObject go : sceneGraph.getGameObjects()) {
            renderPickableGameObject(go);
        }
        sceneGraph.scene.batch.end();
    }

    private void renderPickableGameObject(GameObject go) {
        for (BaseComponent c : go.components) {
            if (c instanceof PickableModelComponent) {
                ((PickableModelComponent) c).renderPick();
            }
            if (c instanceof PickablePointLightComponent){
                ((PickablePointLightComponent) c).renderPick();
            }
            if (c instanceof PickablePointLightComponent){
                ((PickablePointLightComponent) c).renderPick();
            }
        }

        if (go.getChildren() != null) {
            for (GameObject goc : go.getChildren()) {
                renderPickableGameObject(goc);
            }
        }
    }

    private void renderPickableGameObjects(GameObject[] objects,SceneGraph sceneGraph){
        sceneGraph.scene.batch.begin(sceneGraph.scene.cam);
        for (GameObject go : objects) {
            renderPickableGameObject(go);
        }
        sceneGraph.scene.batch.end();
    }

    public void renderPickables(Pickable[] pickables,SceneGraph sceneGraph){
        sceneGraph.scene.batch.begin(sceneGraph.scene.cam);
        for (int i = 0; i < pickables.length; i++) {
            pickables[i].renderPick();
        }
        sceneGraph.scene.batch.end();
    }


}
