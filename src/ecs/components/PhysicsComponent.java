package ecs.components;

import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import core.physics.BulletEntity;
import scene.graph.GameObject;


public class PhysicsComponent extends BaseComponent{


    public BulletEntity bulletEntity;
    public boolean loaded = false;
    PhysicsAttribute physicsAttribute;
    public GameObject go;
    TransformComponent transformComponent;

    public PhysicsComponent(GameObject go,BulletEntity bulletEntity,PhysicsAttribute physicsAttribute){
        super(go);
        this.physicsAttribute = physicsAttribute;
        this.bulletEntity = bulletEntity;
        this.go = go;
        for (BaseComponent component : go.components) {
            if (component instanceof TransformComponent) {
                transformComponent = (TransformComponent) component;
            }
        }

        if (!physicsAttribute.checkFlag(PhysicsAttribute.DEBUG_DRAW_ENABLED)){
            bulletEntity.getBody().setCollisionFlags(bulletEntity.getBody().getCollisionFlags() | btCollisionObject.CollisionFlags.CF_DISABLE_VISUALIZE_OBJECT);
        }

        if(physicsAttribute.checkFlag(PhysicsAttribute.DISABLE_DEACTIVATION)) {
            bulletEntity.getBody().setActivationState(Collision.DISABLE_DEACTIVATION);
        }

        bulletEntity.getBody().setWorldTransform(transformComponent.getTransform());
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void update(float delta) {
        go.getTransform().set(bulletEntity.getBody().getWorldTransform());






    }
}
