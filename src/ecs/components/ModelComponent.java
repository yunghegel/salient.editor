package ecs.components;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.Shader;
import scene.graph.GameObject;

public class ModelComponent extends RenderableComponent{

    protected ModelInstance modelInstance;
    protected Shader shader;
    protected boolean useModelCache = false;
    GameObject go;

    public ModelComponent(GameObject go, ModelInstance renderable){
        super(go, renderable);
        this.go = go;
        this.modelInstance = renderable;
    }

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public void setModelInstance(ModelInstance modelInstance) {
        this.modelInstance = modelInstance;
    }


    @Override
    public void render(float delta) {


    }

    @Override
    public void update(float delta) {
        TransformComponent transformComponent = (TransformComponent) gameObject.getEntity().getComponent(TransformComponent.class);
        if (transformComponent != null) {
            modelInstance.transform.set(transformComponent.getTransform());
        }
//        modelInstance.transform.set(go.getTransform());
    }
}
