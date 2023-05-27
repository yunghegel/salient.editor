package ecs.components.lights;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import app.Editor;
import scene.graph.GameObject;
import scene.picking.Pickable;
import scene.picking.PickerColorEncoder;
import scene.picking.PickerIDAttribute;
import shaders.Shaders;

public class PickableSpotLightComponent extends SpotLightComponent implements Pickable {

    private final ModelInstance modelInstance;

    public PickableSpotLightComponent(GameObject go) {
        super(go);
        Material material = new Material();


        // Build simple cube as a workaround for making lights pickable
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part("ID"+go.id, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material);
        BoxShapeBuilder.build(builder, 0,0,0, .25f, .25f, .25f);
        Model model = modelBuilder.end();
        modelInstance = new ModelInstance(model);
        encodeRaypickColorId();


    }


    @Override
    public void encodeRaypickColorId() {
        PickerIDAttribute goIDa = PickerColorEncoder.encodeRaypickColorId(gameObject);
        modelInstance.materials.first().set(goIDa);
    }

    @Override
    public void renderPick() {
        gameObject.getPosition(tmp);
        modelInstance.transform.setToTranslation(tmp);
        gameObject.sceneGraph.scene.batch.render(modelInstance, Shaders.i().getPickerShader());
    }

    @Override
    public int getId() {
        return gameObject.id;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
//        gameObject.sceneGraph.scene.batch.render(modelInstance);
        gameObject.getPosition(tmp);
        modelInstance.transform.setToTranslation(tmp);
        spotLight.position.set(tmp);
        double dst = Editor.i().sceneContext.cam.position.dst(tmp);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
        dst=Math.sqrt(dst);
        decal.setPosition(tmp);
        decal.setScale((float)dst/2);
        decal.lookAt(Editor.i().sceneContext.cam.position, Editor.i().sceneContext.cam.up);
        decalBatch.add(decal);
        decalBatch.flush();
    }

}
