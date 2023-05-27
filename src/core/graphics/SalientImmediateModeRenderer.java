package core.graphics;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class SalientImmediateModeRenderer implements ImmediateModeRenderer {
    private int primitiveType;
    private int vertexIdx;
    private int numSetTexCoords;
    private final int maxVertices;
    private int numVertices;
    private int numIndices;
    private NodePart part;
    private Renderable renderable;
    private RenderContext context;


    private final Mesh mesh;
    private ShaderProgram shader;
    private boolean ownsShader;
    private final int numTexCoords;
    private Shader immediateModeShader;

    private final int vertexSize;
    private final int normalOffset;
    private final int colorOffset;
    private final int texCoordOffset;
    private final Matrix4 projViewTrans = new Matrix4();
    private final Matrix4 projModelView = new Matrix4();
    private final float[] vertices;
    private short[] indices;
    private final String[] shaderUniformNames;
    private Camera cam;

    public SalientImmediateModeRenderer (boolean hasNormals, boolean hasColors, int numTexCoords, Camera cam) {
        this(cam,5000, hasNormals, hasColors, numTexCoords, createDefaultShader(hasNormals, hasColors, numTexCoords));
        ownsShader = true;
    }

    public SalientImmediateModeRenderer (int maxVertices, boolean hasNormals, boolean hasColors, int numTexCoords,Camera cam) {
        this(cam,maxVertices, hasNormals, hasColors, numTexCoords, createDefaultShader(hasNormals, hasColors, numTexCoords));
        ownsShader = true;
    }

    public SalientImmediateModeRenderer (Camera cam, int maxVertices, boolean hasNormals, boolean hasColors, int numTexCoords,
                                    ShaderProgram shader) {
        this.maxVertices = maxVertices;
        this.numTexCoords = numTexCoords;
        this.shader = shader;
        this.cam = cam;

        VertexAttribute[] attribs = buildVertexAttributes(hasNormals, hasColors, numTexCoords);
        mesh = new Mesh(false, maxVertices, 0, attribs);
        part = new NodePart();
        renderable = new Renderable();
        MeshPart meshPart = new MeshPart();
        meshPart.id = "immediate";
        meshPart.primitiveType = primitiveType;
        renderable.meshPart.mesh = mesh;
        immediateModeShader = new DefaultShader(renderable);
        immediateModeShader.init();
//        renderable.environment= Context.getInstance().sceneManager.environment;
        renderable.worldTransform.set(new Matrix4());


        context = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.MAX_GLES_UNITS, 1));


        vertices = new float[maxVertices * (mesh.getVertexAttributes().vertexSize / 4)];
        vertexSize = mesh.getVertexAttributes().vertexSize / 4;
        normalOffset = mesh.getVertexAttribute(VertexAttributes.Usage.Normal) != null ? mesh.getVertexAttribute(VertexAttributes.Usage.Normal).offset / 4 : 0;
        colorOffset = mesh.getVertexAttribute(VertexAttributes.Usage.ColorPacked) != null ? mesh.getVertexAttribute(VertexAttributes.Usage.ColorPacked).offset / 4
                : 0;
        texCoordOffset = mesh.getVertexAttribute(VertexAttributes.Usage.TextureCoordinates) != null
                ? mesh.getVertexAttribute(VertexAttributes.Usage.TextureCoordinates).offset / 4
                : 0;

        shaderUniformNames = new String[numTexCoords];
        for (int i = 0; i < numTexCoords; i++) {
            shaderUniformNames[i] = "u_sampler" + i;
        }
    }

    private VertexAttribute[] buildVertexAttributes (boolean hasNormals, boolean hasColor, int numTexCoords) {
        Array<VertexAttribute> attribs = new Array<VertexAttribute>();
        attribs.add(new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE));
        if (hasNormals) attribs.add(new VertexAttribute(VertexAttributes.Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE));
        if (hasColor) attribs.add(new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE));
        for (int i = 0; i < numTexCoords; i++) {
            attribs.add(new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + i));
        }
        VertexAttribute[] array = new VertexAttribute[attribs.size];
        for (int i = 0; i < attribs.size; i++)
            array[i] = attribs.get(i);
        return array;
    }

    public void setShader (ShaderProgram shader) {
        if (ownsShader) this.shader.dispose();
        this.shader = shader;
        ownsShader = false;
    }

    public ShaderProgram getShader () {
        return shader;
    }

    public void begin (Matrix4 projModelView, int primitiveType) {
        this.projViewTrans.set(projModelView);
        this.primitiveType = primitiveType;
    }

    public void color (Color color) {
        vertices[vertexIdx + colorOffset] = color.toFloatBits();
    }

    public void color (float r, float g, float b, float a) {
        vertices[vertexIdx + colorOffset] = Color.toFloatBits(r, g, b, a);
    }


    /**
     * Sets the color for the current vertex. The call to {@link #vertex(float, float, float)} must follow.
     */
    public void color (float colorBits) {
        vertices[vertexIdx + colorOffset] = colorBits;
    }

    /**
     * Sets the UV coordinate for the current vertex. The call to {@link #vertex(float, float, float)} must follow.
     */
    public void texCoord (float u, float v) {
        final int idx = vertexIdx + texCoordOffset;
        vertices[idx + numSetTexCoords] = u;
        vertices[idx + numSetTexCoords + 1] = v;
        numSetTexCoords += 2;
    }

    /**
     * Sets the normal for the current vertex. The call to {@link #vertex(float, float, float)} must follow.
     */
    public void normal (float x, float y, float z) {
        final int idx = vertexIdx + normalOffset;
        vertices[idx] = x;
        vertices[idx + 1] = y;
        vertices[idx + 2] = z;
    }

    /**
     * Sets the position for the current vertex. UV coordinates, normals, and colors must be defined before creating an additional vertex.
     */
    public void vertex (float x, float y, float z) {
        final int idx = vertexIdx;
        vertices[idx] = x;
        vertices[idx + 1] = y;
        vertices[idx + 2] = z;

        numSetTexCoords = 0;
        vertexIdx += vertexSize;
        numVertices++;
    }

    //TODO: face method, introduces indexing with per-vertex UVs, normals, and colors
    public void face(short index1, short index2, short index3){
        //v1 = vertices[index1];
        //v2 = vertices[index2];
        //v3 = vertices[index3];

        //if (hasTexCoords)
        //uv1 = vertexUVs[index1+texCoordOffset];
        //uv2 = vertexUVs[index2+texCoordOffset];
        //uv3 = vertexUVs[index3+texCoordOffset];

        //if (hasNormals)
        //n1 = vertexNormals[index1+normalOffset];
        //n2 = vertexNormals[index2+normalOffset];
        //n3 = vertexNormals[index3+normalOffset];

        //if (hasColors)
        //c1 = vertexColors[index1+colorOffset];
        //c2 = vertexColors[index2+colorOffset];

        //vertex(v1,v2,v3);
        //texCoord(uv1,uv2);
        //normal(n1,n2,n3);
        //color(c1,c2,c3);

        //-> sent to mesh and drawn



    }

    public void flush () {
        if (numVertices == 0) return;
        shader.bind();
        //TODO: replace default shader with one that supports per-vertex UVs, normals, and colors; adds textured faces and lighting support
        shader.setUniformMatrix("u_projModelView", projViewTrans);
//        shader.setUniformMatrix("u_worldTrans", projModelView);
//        shader.setUniformMatrix("u_projViewTrans", projViewTrans);
        for (int i = 0; i < numTexCoords; i++)
            shader.setUniformi(shaderUniformNames[i], i);
        mesh.setVertices(vertices, 0, vertexIdx);
        mesh.render(shader, primitiveType);
//        context.begin();
//        immediateModeShader.begin(cam, context);
//        immediateModeShader.render(renderable);
//        immediateModeShader.end();
//        context.end();

        numSetTexCoords = 0;
        vertexIdx = 0;
        numVertices = 0;
    }

    public void end () {
        flush();
    }

    public int getNumVertices () {
        return numVertices;
    }

    @Override
    public int getMaxVertices () {
        return maxVertices;
    }

    public void dispose () {
        if (ownsShader && shader != null) shader.dispose();
        mesh.dispose();
    }

    static private String createVertexShader (boolean hasNormals, boolean hasColors, int numTexCoords) {
        String shader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n"
                + (hasNormals ? "attribute vec3 " + ShaderProgram.NORMAL_ATTRIBUTE + ";\n" : "")
                + (hasColors ? "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" : "");

        for (int i = 0; i < numTexCoords; i++) {
            shader += "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + i + ";\n";
        }

        shader += "uniform mat4 u_projModelView;\n" //
                + (hasColors ? "varying vec4 v_col;\n" : "");

        for (int i = 0; i < numTexCoords; i++) {
            shader += "varying vec2 v_tex" + i + ";\n";
        }

        shader += "void main() {\n" + "   gl_Position = u_projModelView * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n";
        if (hasColors) {
            shader += "   v_col = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                    + "   v_col.a *= 255.0 / 254.0;\n";
        }

        for (int i = 0; i < numTexCoords; i++) {
            shader += "   v_tex" + i + " = " + ShaderProgram.TEXCOORD_ATTRIBUTE + i + ";\n";
        }
        shader += "   gl_PointSize = 1.0;\n" //
                + "}\n";
        return shader;
    }

    static private String createFragmentShader (boolean hasNormals, boolean hasColors, int numTexCoords) {
        String shader = "#ifdef GL_ES\n" + "precision mediump float;\n" + "#endif\n";

        if (hasColors) shader += "varying vec4 v_col;\n";
        for (int i = 0; i < numTexCoords; i++) {
            shader += "varying vec2 v_tex" + i + ";\n";
            shader += "uniform sampler2D u_sampler" + i + ";\n";
        }

        shader += "void main() {\n" //
                + "   gl_FragColor = " + (hasColors ? "v_col" : "vec4(1, 1, 1, 1)");

        if (numTexCoords > 0) shader += " * ";

        for (int i = 0; i < numTexCoords; i++) {
            if (i == numTexCoords - 1) {
                shader += " texture2D(u_sampler" + i + ",  v_tex" + i + ")";
            } else {
                shader += " texture2D(u_sampler" + i + ",  v_tex" + i + ") *";
            }
        }

        shader += ";\n}";
        return shader;
    }

    /** Returns a new instance of the default shader used by SpriteBatch for GL2 when no shader is specified. */
    static public ShaderProgram createDefaultShader (boolean hasNormals, boolean hasColors, int numTexCoords) {
        String vertexShader = createVertexShader(hasNormals, hasColors, numTexCoords);
        String fragmentShader = createFragmentShader(hasNormals, hasColors, numTexCoords);
        ShaderProgram program = new ShaderProgram(vertexShader, fragmentShader);
        if (!program.isCompiled()) throw new GdxRuntimeException("Error compiling shader: " + program.getLog());
        return program;
//        return new ShaderProgram(Gdx.files.internal("shaders/immediate_mode.vs.glsl"), Gdx.files.internal("shaders/immediate_mode.fs.glsl"));

    }
}
