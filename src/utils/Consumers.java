package utils;

import com.badlogic.gdx.utils.Array;
import ecs.components.BaseComponent;
import scene.graph.GameObject;

import java.util.function.BiConsumer;

public class Consumers {

    public static final ComponentBiConsumer componentBiConsumer = new ComponentBiConsumer();

    private Consumers() {
    }

    public static void collectComponents(GameObject gameObject, BaseComponent... components){
        componentBiConsumer.accept(new Array<>(components), gameObject);
    }

    static class ComponentBiConsumer implements BiConsumer<Array<BaseComponent>, GameObject> {

        @Override
        public void accept(Array<BaseComponent> baseComponents, GameObject gameObject) {
            baseComponents.forEach(gameObject::addComponent);
        }
    }

}
