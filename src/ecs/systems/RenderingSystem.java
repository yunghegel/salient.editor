package ecs.systems;

import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.esotericsoftware.minlog.Log;
import core.graphics.SalientShapeRenderer;
import ecs.components.PickableModelComponent;
import ecs.components.RenderableComponent;
import ecs.components.TransformComponent;

public class RenderingSystem extends IteratingSystem {

        public SalientShapeRenderer drawer;
        public Camera cam;

        public RenderingSystem(Camera cam) {
            super(Family.one(PickableModelComponent.class, TransformComponent.class, RenderableComponent.class).get());
            this.cam=cam;
            drawer = new SalientShapeRenderer(cam);
        }

    @Override
    public void update(float deltaTime) {

        super.update(deltaTime);
    }

    @Override
        protected void processEntity(com.badlogic.ashley.core.Entity entity, float deltaTime) {
            PickableModelComponent pickableModelComponent = entity.getComponent(PickableModelComponent.class);
            TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
            RenderableComponent renderableComponent = entity.getComponent(RenderableComponent.class);
            if (transformComponent != null) {
                pickableModelComponent.getModelInstance().transform.set(pickableModelComponent.gameObject.getTransform());
            }
            if(renderableComponent != null){
                    if(!renderableComponent.gameObject.sceneGraph.scene.sceneRenderer.sceneManager.getRenderableProviders().contains(renderableComponent.renderable,true)||renderableComponent.gameObject.visible) {
                        renderableComponent.gameObject.sceneGraph.scene.sceneRenderer.sceneManager.getRenderableProviders().add(renderableComponent.renderable);
                        Log.info("RenderingSystem", "Added renderable to renderables list");
                } else if(renderableComponent.gameObject.sceneGraph.scene.sceneRenderer.sceneManager.getRenderableProviders().contains(renderableComponent.renderable,true)&&!renderableComponent.gameObject.visible){
                        renderableComponent.gameObject.sceneGraph.scene.sceneRenderer.sceneManager.getRenderableProviders().removeValue(renderableComponent.renderable,true);
                        Log.info("RenderingSystem", "Removed renderable from renderables list");
                    }
                    }
            }


        }

