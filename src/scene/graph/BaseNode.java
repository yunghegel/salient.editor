package scene.graph;

import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Marcus Brummer
 * @version 22-06-2016
 */
public abstract class BaseNode<T extends BaseNode> implements Node<T> {

    public final int id;

    protected Array<T> children;
    protected T parent;

    public BaseNode(int id) {
        this.id = id;
    }

    @Override
    public void initChildrenArray() {
        this.children = new Array<T>();
    }

    @Override
    public void addChild(T child) {
        if (children == null) children = new Array<T>();
        children.add(child);
        child.setParent(this);
    }

    @Override
    public boolean isChildOf(GameObject other) {
        for (GameObject go : other) {
            if (go.id == this.id) return true;
        }

        return false;
    }

    @Override
    public Array<T> getChildren() {
        return this.children;
    }

    @Override
    public T getParent() {
        return this.parent;
    }

    @Override
    public void setParent(T parent) {
        this.parent = parent;
    }

    @Override
    public void remove() {
        if (parent != null) {
            parent.getChildren().removeValue(this, true);
            this.parent = null;
        }
    }

}
