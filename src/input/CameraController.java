package input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntIntMap;

public class CameraController extends InputAdapter {


    public final IntIntMap keys = new IntIntMap();
    public PerspectiveCamera camera;
    public Vector3 target = new Vector3();
    public float translateUnits = 30f;
    public float rotateAngle = 360f;
    boolean rotateButton = Gdx.input.isButtonPressed(0);
    boolean translateButton = Gdx.input.isButtonPressed(1);


    private float startX, startY;

    private final Vector3 tmpV0 = new Vector3();
    private final Vector3 tmpV1 = new Vector3();
    private final Vector3 tmpV2 = new Vector3();

    private Vector3 oldCameraDir = new Vector3();
    private Vector3 oldCameraPos = new Vector3();

    public CameraController (PerspectiveCamera cam) {
        this.camera = cam;
    }

    @Override
    public boolean keyDown(int keycode) {
        keys.put(keycode, keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        keys.remove(keycode, 0);
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        startX = screenX;
        startY = screenY;
        oldCameraDir.set(camera.direction);
        oldCameraPos.set(camera.position);
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        final float deltaX = (screenX - startX) / Gdx.graphics.getWidth();
        final float deltaY = (startY - screenY) / Gdx.graphics.getHeight();
        startX = screenX;
        startY = screenY;
        oldCameraDir.set(camera.direction);
        oldCameraPos.set(camera.position);

        if (Gdx.input.isButtonPressed(0)) {
            tmpV1.set(camera.direction).crs(camera.up).y = 0f;
            tmpV1.set(camera.direction).crs(camera.up).y = 0f;
            camera.rotateAround(target, tmpV1.nor(), deltaY * rotateAngle);
            camera.rotateAround(target, Vector3.Y, deltaX * -rotateAngle);
        } else if (Gdx.input.isButtonPressed(1)) {
            camera.translate(tmpV1.set(camera.direction).crs(camera.up).nor().scl(-deltaX * translateUnits));
            camera.translate(tmpV2.set(camera.up).scl(-deltaY * translateUnits));
            target.add(tmpV1).add(tmpV2);
            target.y=0;
        }


        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {

        float sign = Math.copySign(1f, amountY);
        final float delta = Gdx.graphics.getDeltaTime()*-sign;
        camera.translate(tmpV1.set(camera.direction).scl(delta * translateUnits));

        //interpolate camera position
        return super.scrolled(amountX, amountY);
    }

    final Vector3 tmpVec = new Vector3();


}
