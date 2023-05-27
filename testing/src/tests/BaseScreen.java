package tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Array;
import utils.ModelUtils;

public class BaseScreen extends ScreenAdapter {

    public PerspectiveCamera camera;
    public ModelBatch modelBatch;
    public ModelInstance sphere;
    public CameraInputController cameraController;
    public Array<RenderableProvider> renderables = new Array<>();
    public Environment environment = new Environment();
    public DirectionalLight directionalLight = new DirectionalLight();


    public BaseScreen() {
        camera = new PerspectiveCamera(67, 800, 480);
        camera.position.set(10f, 10f, 10f);
        camera.lookAt(0,0,0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();

        modelBatch = new ModelBatch();

        Model sphereModel = ModelUtils.createSphere(1f);
        sphere = new ModelInstance(sphereModel);
        renderables.add(sphere);
        cameraController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(cameraController);

        directionalLight.set(1f, 1f, 1f, 0f, 0f, 0f);
        environment.add(directionalLight);



    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT | Gdx.gl.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
        cameraController.update();

    }
}
