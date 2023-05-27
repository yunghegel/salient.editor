package scene.graph;

import backend.tools.Log;
import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import ecs.components.BaseComponent;
import ecs.components.TransformComponent;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class GameObject extends SimpleNode<GameObject> implements Iterable<GameObject> {

    public static final String DEFAULT_NAME = "GameObject";

    public String name;
    public boolean active;
    private Entity entity;
    public Array<BaseComponent> components=new Array<>();
    private Array<String> tags;
    public SceneGraph sceneGraph;
    public boolean renderWireframe = false;
    public boolean renderOutline = true;
    public boolean visible = true;

    public GameObject(SceneGraph sceneGraph, String name, int id) {
        super(id);
        entity = new Entity();
        this.sceneGraph = sceneGraph;
        this.name = (name == null) ? DEFAULT_NAME : name;
        this.active = true;
        this.tags = null;
        Log.info("GameObject created: [" + this.name + "]  [" + this.id+"]");
    }

    public void render(float delta){

        for (BaseComponent component : components) {
           if(visible) component.render(delta);
        }
        if(getChildren()!=null){
            for (GameObject child : getChildren()) {
                if(child.visible) child.render(delta);
            }
        }
    }

    public void update(float delta){
        for (BaseComponent component : components) {
            component.update(delta);
        }
        if(getChildren()!=null){
            for (GameObject child : getChildren()) {
                child.update(delta);
            }
        }
    }

    public Entity getEntity() {
        return entity;
    }

    public GameObject findChildrenByName(String name) {
        for (GameObject go : this) {
            if (go.name.equals(name)) {
                return go;
            }
        }

        return null;
    }




    public void addComponent(BaseComponent component) {
        components.add(component);
        entity.add(component);
    }

    public void removeComponent(BaseComponent component) {
        components.removeValue(component, true);
        entity.remove(component.getClass());
    }

    public boolean hasComponent(Class<? extends BaseComponent> component) {
        return entity.getComponent(component) != null;
    }

    public Array<String> getTags() {
        return this.tags;
    }

    /**
     * Adds a tag.
     *
     * @param tag
     *            tag to add
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new Array<String>(2);
        }

        this.tags.add(tag);
    }


    @Override
    public Iterator<GameObject> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super GameObject> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<GameObject> spliterator() {
        return Iterable.super.spliterator();
    }

    public Array<BaseComponent> getComponents() {
        ImmutableArray<Component> components = entity.getComponents();
        Array<BaseComponent> baseComponents = new Array<BaseComponent>();
        for (Component component : components) {
            baseComponents.add((BaseComponent) component);
        }
        return baseComponents;
    }

    public BaseComponent getComponentByClass(Class<? extends BaseComponent> transformComponentClass) {
        BaseComponent component = entity.getComponent(transformComponentClass);
        Log.info("Returning component of type: " + component.getClass().getComponentType());
        return component;

    }

    public void toggleVisibility(){
        for(BaseComponent component:components){
            component.toggleVisible();
        }

    }
}
