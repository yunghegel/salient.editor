package ecs.components;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import scene.picking.GameObjectPicker;
import shaders.Shaders;
import scene.graph.GameObject;
import scene.picking.Pickable;
import scene.picking.PickerColorEncoder;
import scene.picking.PickerIDAttribute;

public class PickableModelComponent extends ModelComponent implements Pickable {

        GameObject go;




        public PickableModelComponent(GameObject go, ModelInstance renderable){
                super(go, renderable);
                this.go = go;

                encodeRaypickColorId();
        }

        @Override
        public void render(float delta) {
                gameObject.sceneGraph.scene.batch.render(modelInstance, gameObject.sceneGraph.scene.salientEnvironment);

        }

        @Override
        public void update(float delta) {
                super.update(delta);
        }

        @Override
        public void encodeRaypickColorId() {
                PickerIDAttribute goIDa = PickerColorEncoder.encodeRaypickColorId(go);
                modelInstance.materials.first().set(goIDa);
        }

        @Override
        public void renderPick() {
                gameObject.sceneGraph.scene.batch.render(modelInstance, Shaders.i().getPickerShader());
        }

        @Override
        public int getId(){
                return go.id;
        }
}
