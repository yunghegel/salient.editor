package scene;

import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import app.Editor;
import core.graphics.OutlineRenderer;
import core.graphics.WireframeRenderer;
import input.CameraController;
import input.SalientCamera;
import net.mgsx.gltf.scene3d.lights.DirectionalShadowLight;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.shaders.PBRShaderConfig;
import net.mgsx.gltf.scene3d.shaders.PBRShaderProvider;
import scene.env.SalientEnvironment;
import scene.graph.SceneGraph;
import utils.SceneUtils;

public class SceneContext {

    public SceneGraph sceneGraph;

    public SceneManager sceneManager;
    public SceneRenderer sceneRenderer;

    public PerspectiveCamera cam;
    public OrthographicCamera orthoCam;
    public SalientCamera salientCamera;
    public CameraInputController camController;

    public Environment environment;
    public SceneSkybox skybox;
    public ModelBatch batch;
    public ModelBatch depthBatch;
    public DirectionalShadowLight shadowLight;
    public DirectionalLight directionalLight;
    public ScreenViewport viewport;

    public OutlineRenderer outlineRenderer;
    public WireframeRenderer wireframeRenderer;

    public SalientEnvironment salientEnvironment;
    public CameraController cameraController;



    public SceneContext(){
        createScene();
    }

    public void createScene(){
        viewport = new ScreenViewport();
        sceneGraph = new SceneGraph(this);






        PBRShaderConfig config = new PBRShaderConfig();
        config.numBones = 128;
        config.numDirectionalLights = 1;
        config.numPointLights = 12;
        config.numSpotLights = 12;
        DepthShaderProvider depthShaderProvider = new DepthShaderProvider();
        depthShaderProvider.config.numBones = 128;
        depthShaderProvider.config.numDirectionalLights = 1;
        depthShaderProvider.config.numPointLights = 12;
        depthShaderProvider.config.numSpotLights = 12;

        sceneManager = new SceneManager(new PBRShaderProvider(config), depthShaderProvider);
        batch = sceneManager.getBatch();
        salientEnvironment = new SalientEnvironment(this);
        sceneRenderer = new SceneRenderer(this);

        outlineRenderer = new OutlineRenderer();
        wireframeRenderer = new WireframeRenderer();

        sceneManager.environment = salientEnvironment;

        salientEnvironment.createDefaultSkybox();
        cam = new PerspectiveCamera(67, 1, 1);
        orthoCam = new OrthographicCamera(1, 1);
        salientCamera = new SalientCamera(cam, orthoCam,sceneManager, Vector3.Zero,10,1);

        camController = new CameraInputController(cam) {

            @Override
            protected boolean process(float deltaX, float deltaY, int button) {
                if(Editor.i().inputManager.paused){
                    return super.process(deltaX, deltaY, -1);
                } else return super.process(deltaX, deltaY, button);
            }
        };

        cameraController = new CameraController(cam);
        cam.far = 150f;

        Editor.i().inputManager.addInputProcessor(cameraController);
        Editor.i().inputManager.setCameraController(salientCamera);

        viewport.setCamera(cam);



//        shadowLight = new DirectionalShadowLight(1024 , 1024 , 128 , 128 , 1f , 1000) {
//            @Override
//            public void begin() {
//                super.begin();
//                Gdx.gl.glClearColor(0, 0, 0, 0f);
//            }
//        };
//        shadowLight.direction.set(-.4f, -.4f, -.4f).nor();
//
//        directionalLight = new DirectionalLightEx();
//        directionalLight.direction.set(-.4f, -.4f, -.4f).nor();
//        sceneManager.setAmbientLight(.01f);
//        sceneManager.environment.add(directionalLight);

        sceneManager.setCamera(cam);
        environment = sceneManager.environment;
        Cubemap customSkyboxCubemap = SceneUtils.createCubemapDirectionFormat("editor");
        skybox = new SceneSkybox(customSkyboxCubemap);
        sceneManager.setSkyBox(skybox);



    }



}
