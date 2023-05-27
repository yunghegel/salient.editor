package utils;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import ecs.components.ModelComponent;
import ecs.components.PickableModelComponent;
import ecs.components.TransformComponent;
import scene.graph.GameObject;
import scene.graph.SceneGraph;

import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class Suppliers {

    public static final Random r = new Random();



    //private constructor to prevent instantiation
    private Suppliers() {
    }

    /**
     * Provides a random float between a max and min value
     * @param min
     * @param max
     * @return {@link Float} random float
     */

    public static float rFloat(float min, float max) {
        return r.nextFloat() * (max - min) + min;
    }

    /**
     * Provides a random int between a max and min value
     * @param min
     * @param max
     * @return {@link Integer} random int
     *
     */
    public static int rInt(int min, int max) {
        return r.nextInt(max - min) + min;
    }

    /**
     * Provides a random {@link Vector3} between a max and min value
     * @param min
     * @param max
     * @return {@link Vector3} random int
     *
     */
    public static Vector3 rVec3(float min, float max) {
        return new Vector3(rFloat(min, max), rFloat(min, max), rFloat(min, max));
    }

    public static GameObject pickableModelGameObject(ModelInstance instance, SceneGraph sceneGraph) {
        GameObject gameObject = new GameObject(sceneGraph, "Model " + sceneGraph.getGameObjects().size + 1, sceneGraph.getGameObjects().size + 1);
        PickableModelComponent modelComponent = new PickableModelComponent(gameObject, instance);
        TransformComponent transformComponent = new TransformComponent(gameObject, gameObject.getTransform());
        Consumers.collectComponents(gameObject, modelComponent, transformComponent);
        return gameObject;
    }

    public static GameObject modelGameObject(ModelInstance instance,SceneGraph sceneGraph){
        GameObject gameObject = new GameObject(sceneGraph, "Model " + sceneGraph.getGameObjects().size + 1, sceneGraph.getGameObjects().size + 1);
        ModelComponent modelComponent = new ModelComponent(gameObject, instance);
        TransformComponent transformComponent = new TransformComponent(gameObject, gameObject.getTransform());
        Consumers.collectComponents(gameObject, modelComponent, transformComponent);
        return gameObject;
    }







}
