package tests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import utils.ModelUtils;
import utils.StringUtils;

public class RotateGizmoMeshTest extends ApplicationAdapter {

    private PerspectiveCamera camera;
    ModelBatch modelBatch;
    CameraInputController cameraInputController;
    ModelInstance modelInstance;
    ShapeRenderer shapeRenderer;
    SpriteBatch spriteBatch;
    BitmapFont font;
float time = 0;
    float GIZMO_CIRCLE_SIZE = 1.1f;
    float GIZMO_SCALE_OFFSET = GIZMO_CIRCLE_SIZE + 0.3f;
    float GIZMO_ARROW_OFFSET = GIZMO_CIRCLE_SIZE + 0.3f;
    @Override
    public void create() {
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(4f, 4f, 4f);
        camera.lookAt(0, 0, 0);
        camera.near = 1f;
        camera.far = 300f;

        modelBatch = new ModelBatch();
        cameraInputController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(cameraInputController);

        Model model = ModelUtils.createCube(1);
        modelInstance = new ModelInstance(model);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setAutoShapeType(true);


        int n = 128; // number of circle segments
        int m = 3; // number of thickness segments
        float MATH_TAU = 6.283185307179586f;
        float step = MATH_TAU / n;
        for (int j = 0; j < n; ++j){

            spriteBatch = new SpriteBatch();
            font = new BitmapFont();



            //C++ code:
/*            Basis basis = Basis(ivec, j * step);

            Vector3 vertex = basis.xform(ivec2 * GIZMO_CIRCLE_SIZE);

            for (int k = 0; k < m; ++k) {
                Vector2 ofs = Vector2(Math::cos((Math_TAU * k) / m), Math::sin((Math_TAU * k) / m));
                Vector3 normal = ivec * ofs.x + ivec2 * ofs.y;

                surftool->set_normal(basis.xform(normal));
                surftool->add_vertex(vertex);
            }*/
            // Java code:




        Gdx.gl.glLineWidth(8);

        }

    }
    int n = 128;
    float count = 128;
    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        time += deltaTime;
//        if (count > 128){
//            count -= deltaTime * 128;
//        } else {
//            count += deltaTime * 128;
//        }



        cameraInputController.update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        camera.update();
//        modelBatch.begin(camera);
//        modelBatch.render(modelInstance);
//        modelBatch.end();
         // number of circle segments
        int m = 3; // number of thickness segments
        float MATH_TAU = 6.283185307179586f;
        float step = MATH_TAU / n;
        float radius = 2;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1);
        //3d semi-circle
        step = MATH_TAU / n;
        shapeRenderer.setProjectionMatrix(camera.combined);

        float dot = camera.direction.dot(Vector3.Y)+2-1;
        count = 128*(dot);

        float dotX = Math.abs(count*camera.direction.y);
        float dotY = Math.abs(count*camera.direction.y);
        float dotZ = Math.abs(count*camera.direction.y);



//       count= MathUtils.ceil(96);


//        count = MathUtils.clamp(dotZ,32,100);
        for (int j = 0; j <  count; ++j){
            float x = -(float)Math.cos(j * step);
            float y = -(float)Math.sin(j * step);
            float x2 = -(float)Math.cos((j+1) * step);
            float y2 = -(float)Math.sin((j+1) * step);
            shapeRenderer.line(x, y, 0, x2, y2, 0);
        }
        //now in xz plane

//        count = MathUtils.clamp(dotY,32,100);
        shapeRenderer.setColor(0, 1, 0, 1);
        for (int j = 0; j < count; ++j){
            float x = -(float)Math.cos(j * step);
            float y = -(float)Math.sin(j * step);
            float x2 = -(float)Math.cos((j+1) * step);
            float y2 = -(float)Math.sin((j+1) * step);
            shapeRenderer.line(x, 0, y, x2, 0, y2);
        }
//
//        count = MathUtils.clamp(dotX,32,100);
        shapeRenderer.setColor(0, 0, 1, 1);
        for (int j = 0; j <  count; ++j){
            float x = -(float)Math.cos(j * step);
            float y = -(float)Math.sin(j * step);
            float x2 = -(float)Math.cos((j+1) * step);
            float y2 = -(float)Math.sin((j+1) * step);
            shapeRenderer.line(0, x, y, 0, x2, y2);
        }

        spriteBatch.begin();
        font.draw(spriteBatch, StringUtils.trimVector3(camera.direction),100,100);
        spriteBatch.end();



        shapeRenderer.end();
//if (count > 128){
//    count = 0;
//}

    }

    void semiCircle(int segments,float step){
        float MATH_TAU = 6.283185307179586f;
        for (int j = 0; j < segments; ++j){
            float x = (float)Math.cos(j *step);
            float y = (float)Math.sin(j * step);
            float x2 = (float)Math.cos((j+1) * step);
            float y2 = (float)Math.sin((j+1) * step);
            shapeRenderer.line(x, y, 0, x2, y2, 0);
        }
    }
}
