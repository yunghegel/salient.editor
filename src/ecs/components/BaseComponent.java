package ecs.components;

import com.badlogic.ashley.core.Component;
import scene.graph.GameObject;

public abstract class BaseComponent implements Component {

    public GameObject gameObject;

    public BaseComponent(GameObject go){
        this.gameObject = go;
        gameObject.addComponent(this);
    }

    public abstract void render(float delta);

    public abstract void update(float delta);

    public void toggleVisible(){

    }

}
