package tools;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import ecs.components.PickableModelComponent;
import scene.graph.GameObject;

public class ToolHandle extends PickableModelComponent {

    public ToolHandle(GameObject go, ModelInstance renderable) {
        super(go, renderable);
    }
//    public ModelInstance modelInstance;
//    public int id;
//    public String name;
//
//
//    public ToolHandle(ModelInstance instance,int id,String name) {
//        this.modelInstance = instance;
//        this.id = id;
//        this.name = name;
//        encodeRaypickColorId();
//    }
//
//    @Override
//    public void encodeRaypickColorId() {
//        PickerIDAttribute goIDa = PickerColorEncoder.encodeRaypickColorId(modelInstance,id);
//        modelInstance.materials.first().set(goIDa);
//    }
//
//    @Override
//    public void renderPick() {
//        Editor.i().sceneContext.sceneGraph.scene.batch.render(modelInstance, Shaders.i().getPickerShader());
//    }
//
//    @Override
//    public int getId() {
//        return id;
//    }

}
