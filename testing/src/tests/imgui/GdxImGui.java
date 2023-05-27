package tests.imgui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.extension.imguizmo.ImGuizmo;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImFloat;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

import java.util.Arrays;

public class GdxImGui {
    ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    public ImFloat sliderValue = new ImFloat(0.5f);



    public GdxImGui() {
        create();
    }

    public void create() {
        long windowHandle = ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();
        GLFW.glfwMakeContextCurrent(windowHandle);
        GL.createCapabilities();
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.setIniFilename(null);
        io.getFonts().addFontDefault();
        io.getFonts().build();
        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init("#version 110");
    }

    public void render() {
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT | Gdx.gl.GL_DEPTH_BUFFER_BIT);
        imGuiGlfw.newFrame();
        ImGui.newFrame();




        ImGui.begin("new window");
        ImGui.beginMainMenuBar();
        if (ImGui.beginMenu("Tests")) {
            if (ImGui.menuItem("ShaderTest", "1"));
            ImGui.endMenu();
        }
        ImGui.endMainMenuBar();


        ImGui.beginTabBar("debug menu");
        if (ImGui.beginTabItem("tab 1")) {
            ImGui.text("this is tab 1");
            ImGui.endTabItem();
        }
        if (ImGui.beginTabItem("tab 2")) {
            ImGui.text("this is tab 2");
            ImGui.endTabItem();
        }
        ImGui.endTabBar();
        ImGuizmo.setOrthographic(false);
        ImGuizmo.setEnabled(true);
        ImGuizmo.setDrawList();
        float aspect = ImGui.getWindowWidth() / ImGui.getWindowHeight();

        float[] cameraProjection = perspective(27, aspect, 0.1f, 100f);
        float windowWidth = ImGui.getWindowWidth();
        float windowHeight = ImGui.getWindowHeight();



        ImGui.end();





        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    public void begin(){
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    public void end(){
        ImGui.end();
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    private static final float[] INPUT_CAMERA_VIEW = {
            1.f, 0.f, 0.f, 0.f,
            0.f, 1.f, 0.f, 0.f,
            0.f, 0.f, 1.f, 0.f,
            0.f, 0.f, 0.f, 1.f
    };
    private static final float[] IDENTITY_MATRIX = {
            1.f, 0.f, 0.f, 0.f,
            0.f, 1.f, 0.f, 0.f,
            0.f, 0.f, 1.f, 0.f,
            0.f, 0.f, 0.f, 1.f
    };
    private static final float[][] OBJECT_MATRICES = {
            {
                    1.f, 0.f, 0.f, 0.f,
                    0.f, 1.f, 0.f, 0.f,
                    0.f, 0.f, 1.f, 0.f,
                    0.f, 0.f, 0.f, 1.f
            },
            {
                    1.f, 0.f, 0.f, 0.f,
                    0.f, 1.f, 0.f, 0.f,
                    0.f, 0.f, 1.f, 0.f,
                    2.f, 0.f, 0.f, 1.f
            },
            {
                    1.f, 0.f, 0.f, 0.f,
                    0.f, 1.f, 0.f, 0.f,
                    0.f, 0.f, 1.f, 0.f,
                    2.f, 0.f, 2.f, 1.f
            },
            {
                    1.f, 0.f, 0.f, 0.f,
                    0.f, 1.f, 0.f, 0.f,
                    0.f, 0.f, 1.f, 0.f,
                    0.f, 0.f, 2.f, 1.f
            }
    };
    private static float[] frustum(float left, float right, float bottom, float top, float near, float far) {
        float[] r = new float[16];
        float temp = 2.0f * near;
        float temp2 = right - left;
        float temp3 = top - bottom;
        float temp4 = far - near;
        r[0] = temp / temp2;
        r[1] = 0.0f;
        r[2] = 0.0f;
        r[3] = 0.0f;
        r[4] = 0.0f;
        r[5] = temp / temp3;
        r[6] = 0.0f;
        r[7] = 0.0f;
        r[8] = (right + left) / temp2;
        r[9] = (top + bottom) / temp3;
        r[10] = (-far - near) / temp4;
        r[11] = -1.0f;
        r[12] = 0.0f;
        r[13] = 0.0f;
        r[14] = (-temp * far) / temp4;
        r[15] = 0.0f;
        return r;
    }

    private static float[] perspective(float fovY, float aspect, float near, float far) {
        float ymax, xmax;
        ymax = (float) (near * Math.tan(fovY * Math.PI / 180.0f));
        xmax = ymax * aspect;
        return frustum(-xmax, xmax, -ymax, ymax, near, far);
    }
    public void dispose() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }
}
