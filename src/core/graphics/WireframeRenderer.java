package core.graphics;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import ecs.components.BaseComponent;
import ecs.components.PickableModelComponent;
import scene.graph.GameObject;
import scene.graph.SceneGraph;
import shaders.Shaders;
import shaders.WireframeShader;

public class WireframeRenderer {

    private WireframeShader shader;
    private Mesh mesh;
    private ModelInstance modelInstance;
    private SceneGraph sceneGraph;
    private ModelBatch modelBatch;

    public WireframeRenderer() {
        shader = Shaders.i().getWireframeShader();
        modelBatch = new ModelBatch();

    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public void setModelInstance(ModelInstance modelInstance) {
        this.modelInstance = modelInstance;
    }

    public void render(SceneGraph sceneGraph) {
        modelBatch.begin(sceneGraph.scene.cam);
        for (GameObject g : sceneGraph.scene.sceneGraph.getGameObjects()) {
            if(g.renderWireframe){
                for (BaseComponent bc : g.getComponents()) {
                    if(bc instanceof PickableModelComponent){

                    PickableModelComponent pmc = (PickableModelComponent) bc;
                    modelBatch.render(pmc.getModelInstance(), shader);


        }

        }


    }

}
        modelBatch.end();
    }

}
