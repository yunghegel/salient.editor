package shaders;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import app.Editor;
import utils.GLUtils;

public class WireframeShader extends BaseShader {
//    private static final String VERTEX_SHADER = "shaders/wire.vert.glsl";
//    private static final String FRAGMENT_SHADER = "shaders/wire.frag.glsl";

    private static final String VERTEX_SHADER = "attribute vec3 a_position;" + "uniform mat4 u_transMatrix;"
            + "uniform mat4 u_projViewMatrix;" + "void main(void) {"
            + "vec4 worldPos = u_transMatrix * vec4(a_position, 1.0);" + "gl_Position = u_projViewMatrix * worldPos;"
            + "}";

    private static final String FRAGMENT_SHADER = "#ifdef GL_ES\n" + "precision highp float;\n" + "#endif \n"
            + "const vec4 COLOR_WHITE = vec4(1,1,1, 1.0);\n" + "void main(void) {"
            + "    gl_FragColor = COLOR_WHITE;\n" + "}";

    private final int UNIFORM_PROJ_VIEW_MATRIX = register(new Uniform("u_projViewMatrix"));
    private final int UNIFORM_TRANS_MATRIX = register(new Uniform("u_transMatrix"));

    public WireframeShader() {
        program = new ShaderProgram(VERTEX_SHADER, FRAGMENT_SHADER);
    }

    @Override
    public void init() {
        super.init(program, null);
    }

    @Override
    public int compareTo(Shader other) {
        return 0;
    }

    @Override
    public boolean canRender(Renderable instance) {
        return true;
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        this.context = context;
        this.context.setDepthTest(GL20.GL_LEQUAL, 0f, 1f);
        this.context.setDepthMask(true);
        this.context.setCullFace(GL20.GL_FRONT_AND_BACK);

        program.bind();

        set(UNIFORM_PROJ_VIEW_MATRIX, Editor.i().sceneContext.cam.combined);
    }

    @Override
    public void render(Renderable renderable) {
        set(UNIFORM_TRANS_MATRIX, renderable.worldTransform);
        GLUtils.Unsafe.polygonModeWireframe();

        renderable.meshPart.render(program);
    }

    @Override
    public void end() {
        GLUtils.Unsafe.polygonModeFill();
    }

    @Override
    public void dispose() {
        program.dispose();
    }
}
