package input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.mgsx.gltf.scene3d.scene.SceneManager;


public class SalientCamera
{

    public SalientCameraConfig<PerspectiveCamera> perspectiveCameraConfig;
    public SalientCameraConfig<OrthographicCamera> orthographicCameraConfig;
    public SalientCameraConfig current;
    private RaycastHandler raycastHandler;

    private PerspectiveCamera perspectiveCamera;
    private OrthographicCamera orthographicCamera;
    private int renderWidth = 1920;
    private int renderHeight = 1080;
    public boolean isPerspective = true;
    public SceneManager sceneManager;

    private InputMultiplexer inputs;
    private int button;

    public static boolean pauseInput = false;

    public enum CameraMode {
        PERSPECTIVE,
        ORTHOGRAPHIC
    }

    public interface RaycastHandler {
        boolean rayCast(Ray ray, Vector3 position);
    }

    public static class SalientCameraController extends CameraInputController
    {
        private final Vector3 tangent = new Vector3();
        private final Vector3 prevTarget = new Vector3();
        private final Vector3 prevPosition = new Vector3();
        private Vector3 moveTarget;
        private Vector3 movePosition;
        private final Vector3 homeTarget;
        private float time;
        private float homeDistance;
        private int button;




        public SalientCameraController(Camera camera,Vector3 homeTarget, float homeDistance, int button)
        {
            super(camera);
            this.homeTarget = new Vector3(homeTarget);
            this.target.set(homeTarget);
            this.homeDistance = homeDistance;
            this.button = button;

            this.activateKey = 0;
            this.alwaysScroll = true;
            this.backwardKey = 0;
            this.forwardButton = -1;
            this.forwardKey = 0;
            this.forwardTarget = false;

            this.rotateLeftKey = 0;
            this.rotateRightKey = 0;
            this.scrollTarget = false;
            this.translateTarget = true;

            this.autoUpdate = false;
        }

        public void pause(){
            pauseInput = true;
            this.button = -1;
            this.rotateButton = -1;
            this.translateButton = -1;
            this.forwardButton = -1;
        }

        public void resume(){
            pauseInput = false;
            this.rotateButton = 0;
            this.translateButton = 1;
            this.forwardButton = 2;
        }

        @Override
        public void update() {

            if (pauseInput) {
                this.button = -1;
                return;
            }


            boolean changed = false;

            if(Gdx.input.isKeyJustPressed(Input.Keys.FORWARD_DEL) || Gdx.input.isKeyJustPressed(Input.Keys.PERIOD)){
                moveTarget = new Vector3(homeTarget);
                prevTarget.set(target);
                prevPosition.set(camera.position);
                movePosition = new Vector3().set(homeTarget).sub(prevPosition).nor().scl(-homeDistance).add(homeTarget);
                time = 0;
            }
            if(moveTarget != null){
                this.translateButton = -1;
                this.rotateButton = -1;
                this.forwardButton = -1;
                time += Gdx.graphics.getDeltaTime() * 5f;
                this.target.set(prevTarget).lerp(moveTarget, time);
                this.camera.position.set(prevPosition).lerp(movePosition, time);
                if(time >= 1){
                    this.target.set(moveTarget);
                    camera.position.set(movePosition);
                    moveTarget = null;
                }
                camera.up.set(Vector3.Y);
                camera.lookAt(target);
                changed = true;
            }else{
                if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)){
                    this.translateButton = -1;
                    this.rotateButton = -1;
                    this.forwardButton = button;
                }else if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)){
                    this.translateButton = button;
                    this.rotateButton = -1;
                    this.forwardButton = -1;
                }else{
                    this.forwardButton = -1;
                    this.translateButton = -1;
                    this.rotateButton = button;
                }
            }

            tangent.set(camera.direction).crs(camera.up).nor();

            float rotationSteps = 90f / 6;
            if(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_4)){
                camera.rotateAround(target, Vector3.Y, -rotationSteps);
                changed = true;
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_6)){
                camera.rotateAround(target, Vector3.Y, rotationSteps);
                changed = true;
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_8)){
                camera.rotateAround(target, tangent, -rotationSteps);
                changed = true;
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2)){
                camera.rotateAround(target, tangent, rotationSteps);
                changed = true;
            }

            if(changed && !pauseInput){
                camera.update();
            }

            this.pinchZoomFactor = 10f;
            this.translateUnits = camera.position.dst(target);

        }

        @Override
        protected boolean process(float deltaX, float deltaY, int button) {
            if (pauseInput) {
                return super.process(deltaX, deltaY, this.button);
            }
            return super.process(deltaX, deltaY, button);

        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            if (!pauseInput) {
                return super.touchDragged(screenX, screenY, pointer);
            }
            return false;
        }
    }


    public static class SalientCameraConfig<T extends Camera> {
        public SalientCameraController controller;
        T camera;
        Viewport viewport;

        public SalientCameraConfig(T camera, int width, int height, Vector3 homeTarget, float homeDistance, int button) {
            this.camera = camera;
            viewport = new FitViewport(width, height, camera);
            controller = new SalientCameraController(camera, homeTarget, homeDistance, button);
        }

        public void resize(int width, int height){
            viewport.update(width, height, false);
            // XXX
            camera.viewportWidth = width;
            camera.viewportHeight = height;
            camera.update();
        }
        public void update(){
            if (pauseInput) {
                return;
            }
            controller.update();
        }
        public void set(Vector3 position, Vector3 target, float near, float far) {
            camera.position.set(position);
            camera.up.set(Vector3.Y);
            camera.lookAt(target);
            camera.near = near;
            camera.far = far;
            camera.update();
            controller.target.set(target);
        }
        public void setButton(int button) {
            controller.button = button;
        }

        public SalientCameraController getController() {
            return controller;
        }


    }

    public SalientCamera(PerspectiveCamera cam, OrthographicCamera ocam, SceneManager sceneManager, Vector3 homeTarget, float homeDistance, int button) {
        this.perspectiveCameraConfig = new SalientCameraConfig<>(cam, 0, 0, homeTarget, homeDistance, button);
        this.orthographicCameraConfig = new SalientCameraConfig<>(ocam, 0, 0, homeTarget, homeDistance, button);
        this.perspectiveCamera = cam;
        this.orthographicCamera = ocam;
        this.sceneManager = sceneManager;


        cam.fieldOfView = 39.6f;
        cam.near = .01f;
        cam.far = 1000;
        cam.position.set(1, 1, 1).scl(homeDistance);
        cam.up.set(Vector3.Y);
        cam.lookAt(0,0,0);
        cam.update();

        ocam.zoom = 2f / this.renderHeight; // 7.314f;
        ocam.near = 0.1f;
        ocam.far = 100;
        ocam.position.set(1, 1, 1).scl(homeDistance);
        ocam.up.set(Vector3.Y);
        ocam.lookAt(0,0,0);
        ocam.update();

        current = perspectiveCameraConfig;
        inputs = new InputMultiplexer(current.controller);
    }

    public InputProcessor getInputs(){
        return inputs;
    }
    private SalientCameraConfig current() {
        return current;
    }
    public void resize(int width, int height){
        perspectiveCameraConfig.resize(width, height);
        orthographicCameraConfig.resize(width, height);
    }

    public void update(float delta){
        if(pauseInput)  return;

        if(current == orthographicCameraConfig){
            float d = orthographicCameraConfig.controller.target.dst(orthographicCameraConfig.camera.position);
            orthographicCameraConfig.camera.zoom = d / orthographicCameraConfig.camera.viewportHeight;
            orthographicCameraConfig.camera.update();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_5)){
            if(current == perspectiveCameraConfig){
                switchTo(orthographicCameraConfig);

            }else{
                switchTo(perspectiveCameraConfig);
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)){
            if(Gdx.input.isButtonJustPressed(button)){
                if(raycastHandler != null){
                    Vector3 inter = new Vector3();
                    boolean b = raycastHandler.rayCast(current.camera.getPickRay(Gdx.input.getX(), Gdx.input.getY()), inter);

                    System.out.println(current.camera.position.dst(inter));
                    if(b){
                        // TODO set focus and translate to it (keeping camera direction)
                    }
                }
            }
        }
        current().update();
    }

    private void switchTo(SalientCameraConfig toConfig) {
        // sync
        toConfig.camera.position.set(current.camera.position);
        toConfig.camera.direction.set(current.camera.direction);
        toConfig.camera.up.set(current.camera.up);
        toConfig.camera.update();


        current = toConfig;
        sceneManager.setCamera(current.camera);

    }

    public Camera getCamera(){
        return current().camera;
    }
    public PerspectiveCamera getPerspectiveCamera(){
        return perspectiveCameraConfig.camera;
    }
    public OrthographicCamera getOrthographicCamera(){
        return orthographicCameraConfig.camera;
    }
    public void apply() {
        current().viewport.apply();
    }
    public void setTarget(float x, float y, float z) {
        perspectiveCameraConfig.controller.target.set(x,y,z);
        orthographicCameraConfig.controller.target.set(x,y,z);
    }

    public void set(Vector3 position, Vector3 target, float near, float far) {
        perspectiveCameraConfig.set(position, target, near, far);
        orthographicCameraConfig.set(position, target, near, far);
    }

    public Vector3 getPerspectiveTarget() {
        return perspectiveCameraConfig.controller.target;
    }

    public void switchTo(boolean perspective) {
        if(perspective){
            switchTo(this.perspectiveCameraConfig);
        }else{
            switchTo(orthographicCameraConfig);
        }
    }

    public void setTarget(Vector3 target) {
        setTarget(target.x, target.y, target.z);
    }

    public void setButton(int button) {
        this.button = button;
        perspectiveCameraConfig.setButton(button);
        orthographicCameraConfig.setButton(button);
    }

    public void pause(){
        current.controller.pause();
        pauseInput = true;

    }
    public void resume(){
        current.controller.resume();
        pauseInput = false;
    }



}
