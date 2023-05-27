package ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Matrix4;
import scene.graph.GameObject;

public class TransformComponent extends BaseComponent {

    private Matrix4 transform = new Matrix4();

    public TransformComponent(GameObject go, Matrix4 transform){
        super(go);
        this.transform=transform;
    }

    public Matrix4 getTransform() {
        return transform;
    }

    public void setTransform(Matrix4 transform) {
        this.transform=transform;
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void update(float delta) {
        this.transform = gameObject.getTransform();
    }
}
