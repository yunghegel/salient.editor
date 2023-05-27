package scene.graph;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import scene.SceneContext;

public class SceneGraph {

    public SceneContext scene;
    protected GameObject root;
    protected Array<GameObject> selectedObjects = new Array<>();

    public SceneGraph(SceneContext scene) {
        root = new GameObject(this, "root", -1);
        root.initChildrenArray();
        root.active = false;
        this.scene = scene;
    }

    public void render(float delta) {
        for (GameObject go : root.getChildren()) {
            if(go.visible) go.render(delta);
        }
    }

    public void update(float delta) {
        for (GameObject go : root.getChildren()) {
            go.update(delta);
        }
    }

    public void addGameObject(GameObject go) {
        root.addChild(go);
    }

    public Array<GameObject> getGameObjects() {
        return root.getChildren();
    }

    public GameObject getGameObjectByName(String name) {
        return root.findChildrenByName(name);
    }

    public GameObject getRoot() {
        return root;
    }

    public void setSelectedObjects(Array<GameObject> selectedObjects) {
        this.selectedObjects = selectedObjects;
    }

    public void setSelectedObject(GameObject selectedObject) {
        this.selectedObjects.clear();
        this.selectedObjects.add(selectedObject);
    }

    public int getGameObjectCount(){
        int count = 0;
        for (GameObject go : root.getChildren()) {
            count++;
            countChildren(go,count);
        }
        return count;
    }

    public int countChildren(GameObject go,int count){
        if (go.getChildren() == null) return count;
        for (GameObject child : go.getChildren()) {
            if (child != null) {
                count++;
                countChildren(child,count);
            }

        }
        return count;
    }





}
