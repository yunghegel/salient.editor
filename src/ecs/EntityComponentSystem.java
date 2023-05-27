package ecs;

import backend.tools.Log;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btGImpactMeshShape;
import core.physics.BulletEntity;
import ecs.components.*;
import ecs.components.lights.PickablePointLightComponent;
import ecs.components.lights.PickableSpotLightComponent;
import ecs.components.lights.PointLightComponent;
import ecs.systems.*;
import net.mgsx.gltf.scene3d.scene.Scene;
import scene.graph.GameObject;
import scene.graph.SceneGraph;
import utils.BulletUtils;
import utils.MathUtils;
import utils.ModelUtils;
import utils.Suppliers;

public class EntityComponentSystem {

    public SceneGraph sceneGraph;
    public Engine engine;
    public PickingSystem pickingSystem;
    public RenderingSystem renderingSystem;
    public PhysicsSystem physicsSystem;
    public EnvironmentSystem environmentSystem;
    public ToolSystem toolSystem;

    
    public EntityComponentSystem(SceneGraph sceneGraph,Engine engine) {
        this.sceneGraph = sceneGraph;
        this.engine = engine;
        createDefaultEntities();
        createSystems();

    }

    private void createDefaultEntities() {
        int amnt = 6;
        int count = 0;
        for (int i = 0; i <amnt ; i++) {
            GameObject gameObject = new GameObject(sceneGraph,"Model "+ sceneGraph.getGameObjectCount(), Suppliers.rInt(0,1000));
            gameObject.addTag("model");
            sceneGraph.addGameObject(gameObject);
            Model model;

            Vector3 position = new Vector3((float) Math.random()*10* MathUtils.randomSign(),(float) Math.random()*10,(float) Math.random()*10* MathUtils.randomSign());
            gameObject.setLocalPosition(position.x,position.y,position.z);


            if (count%2==0)
                model = ModelUtils.createCube(1);
            else
                model = ModelUtils.createSphere(1);
            ModelInstance modelInstance = new ModelInstance(model);

            for(Material material : modelInstance.materials){
                MaterialComponent materialComponent = new MaterialComponent(gameObject,material);
                gameObject.addComponent(materialComponent);
            }










            PickableModelComponent pickableModelComponent = new PickableModelComponent(gameObject, modelInstance);
            TransformComponent transformComponent = new TransformComponent(gameObject,gameObject.getTransform());

//            BulletEntity bEntity = BulletUtils.createDyanamicPhysicsEntity(modelInstance,gameObject.getTransform());
//            PhysicsComponent physicsComponent = new PhysicsComponent(gameObject,bEntity,new PhysicsAttribute(PhysicsAttribute.DYNAMIC | PhysicsAttribute.DISABLE_DEACTIVATION));

//            gameObject.addComponent(pointLightComponent);
            gameObject.addComponent(pickableModelComponent);
            gameObject.addComponent(transformComponent);
//            gameObject.addComponent(physicsComponent);
            GameObject lightChild = new GameObject(sceneGraph,"Light "+ sceneGraph.getGameObjectCount(),Suppliers.rInt(0,1000));
            PickablePointLightComponent pointLightComponent = new PickablePointLightComponent(lightChild);
            lightChild.addTag("light");
            lightChild.addComponent(pointLightComponent);
            gameObject.addChild(lightChild);
            lightChild.setLocalPosition(0,1,0);

            GameObject lightChild2 = new GameObject(sceneGraph,"Light "+ sceneGraph.getGameObjectCount(),Suppliers.rInt(0,1000));
            PickableSpotLightComponent spotLightComponent = new PickableSpotLightComponent(lightChild2);
            lightChild2.addTag("light");
            lightChild2.addComponent(spotLightComponent);
            gameObject.addChild(lightChild2);
            lightChild2.setLocalPosition(0,-1,0);







            engine.addEntity(gameObject.getEntity());
            count++;
        }


    }

    private void createSystems() {
        pickingSystem = new PickingSystem();
        renderingSystem = new RenderingSystem(sceneGraph.scene.cam);
        physicsSystem = new PhysicsSystem();
        environmentSystem = new EnvironmentSystem();
        toolSystem= new ToolSystem();

        engine.addSystem(pickingSystem);
        engine.addSystem(renderingSystem);
        engine.addSystem(toolSystem);

        engine.addSystem(physicsSystem);
//        engine.addSystem(environmentSystem);
    }

    public void update(float delta) {
        engine.update(delta);
    }
    
}
