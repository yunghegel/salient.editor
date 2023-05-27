package scene.graph;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import ecs.components.PickableModelComponent;
import ecs.components.TransformComponent;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import utils.Suppliers;

public class GameObjectFactory {

    public static GameObject createModelObjectFromPath(FileHandle fileHandle, SceneGraph sceneGraph) {
        GameObject go = new GameObject(sceneGraph,fileHandle.name(), Suppliers.rInt(0,10000));
        go.addTag("model");
        sceneGraph.addGameObject(go);
        SceneAsset model = new GLTFLoader().load(fileHandle);
        Scene scene = new Scene(model.scene);
        PickableModelComponent pickableModelComponent = new PickableModelComponent(go, scene.modelInstance);
        TransformComponent transformComponent = new TransformComponent(go,go.getTransform());
        go.addComponent(pickableModelComponent);
        go.addComponent(transformComponent);


        return go;
    }

}
