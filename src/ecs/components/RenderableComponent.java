package ecs.components;

import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import scene.graph.GameObject;

public abstract class RenderableComponent extends BaseComponent {

    public RenderableProvider renderable;

    public RenderableComponent(GameObject go, RenderableProvider renderable) {
        super(go);
        this.renderable = renderable;
    }

}
