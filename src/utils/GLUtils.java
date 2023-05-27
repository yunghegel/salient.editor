package utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.JNI;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.windows.RECT;
import org.lwjgl.system.windows.User32;
import org.lwjgl.system.windows.WINDOWPLACEMENT;
import org.lwjgl.system.windows.WindowProc;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

public final class GLUtils {

    private GLUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * The buffer used internally. A size of 64 bytes is required as at most 16
     * integer elements can be returned.
     */
    private static final IntBuffer INT_BUFF = ByteBuffer
            .allocateDirect(64).order(ByteOrder.nativeOrder())
            .asIntBuffer();

    /**
     * Returns the name of the currently bound framebuffer
     * ({@code GL_FRAMEBUFFER_BINDING}).
     *
     * @return the name of the currently bound framebuffer; the initial value is
     *         {@code 0}, indicating the default framebuffer
     */
    public static synchronized int getBoundFboHandle() {
        IntBuffer intBuf = INT_BUFF;
        Gdx.gl.glGetIntegerv(GL20.GL_FRAMEBUFFER_BINDING, intBuf);
        return intBuf.get(0);
    }

    /**
     * @return the current gl viewport ({@code GL_VIEWPORT}) as an array,
     *         containing four values: the x and y window coordinates of the
     *         viewport, followed by its width and height.
     */
    public static synchronized int[] getViewport() {
        IntBuffer intBuf = INT_BUFF;
        Gdx.gl.glGetIntegerv(GL20.GL_VIEWPORT, intBuf);

        return new int[] { intBuf.get(0), intBuf.get(1), intBuf.get(2),
                intBuf.get(3) };
    }

    /**
     * Originally gdx-gltf library PBRShaderProvider
     *
     * @return if target platform is running with at least OpenGL ES 3 (GLSL 300 es), WebGL 2.0 (GLSL 300 es)
     *  or desktop OpenGL 3.0 (GLSL 130).
     */
    public static boolean isGL3(){
        return Gdx.graphics.getGLVersion().isVersionEqualToOrHigher(3, 0);
    }

    public static void clearScreen(Color color) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(color.r, color.g, color.b, 1);
    }

    public static void overwriteWindowProc2(long lwjglWindow) {
        if (!SystemUtils.IS_OS_WINDOWS) return;

        long hwnd = GLFWNativeWin32.glfwGetWin32Window(lwjglWindow);
        long pWindowProc = User32.GetWindowLongPtr(hwnd, User32.GWL_WNDPROC);
        System.out.println("oldptr: " + pWindowProc);

        WindowProc proc = new WindowProc() {
            private final Vector2 tmp = new Vector2();
            private final DoubleBuffer cursorX = BufferUtils.createDoubleBuffer(1);
            private final DoubleBuffer cursorY = BufferUtils.createDoubleBuffer(1);
            private RECT rect;

            private int getX() {
                return MathUtils.floor((float) cursorX.get(0));
            }
            private int getY() {
                return MathUtils.floor((float) cursorY.get(0));
            }

            @Override
            public long invoke(long hwnd, int uMsg, long wParam, long lParam) {
                if (uMsg == User32.WM_NCHITTEST) {
                    try (MemoryStack stack = MemoryStack.stackPush()) {
                        short x = (short) (lParam & 0xFFFF);
                        short y = (short) ((lParam & 0xFFFF0000) >> 16);
                        GLFW.glfwGetCursorPos(GLFW.glfwGetCurrentContext(), cursorX, cursorY);

                        if (rect == null)
                            rect = RECT.calloc(stack);
                        User32.GetWindowRect(hwnd, rect);

                        if (y < rect.top() + 16 && x < rect.left() + 16) {
                            return User32.HTTOPLEFT;
                        }
                        if (y > rect.bottom() - 16 && x > rect.right() - 16) {
                            return User32.HTBOTTOMRIGHT;
                        }
                        if (y < rect.top() + 12 && x > rect.right() - 16) {
                            return User32.HTTOPRIGHT;
                        }
                        if (y > rect.bottom() - 16 && x < rect.left() + 16) {
                            return User32.HTBOTTOMLEFT;
                        }

                        if (y < rect.top() + 8) {
                            return User32.HTTOP;
                        }
                        if (x < rect.left() + 16) {
                            return User32.HTLEFT;
                        }
                        if (y > rect.bottom() - 16) {
                            return User32.HTBOTTOM;
                        }
                        if (x > rect.right() - 16) {
                            return User32.HTRIGHT;
                        }

                        //Test if the pointer is in Title Bar


                        int glfwX = getX();
                        int glfwY = getY();


                        if (getY()<24&&getX()>200&&getX()<Gdx.graphics.getWidth()-30) {
                            return User32.HTCAPTION;
                        }

                        return JNI.callPPPP(hwnd, uMsg, wParam, lParam, pWindowProc);
                    }
                }
                if (uMsg == User32.WM_NCCALCSIZE) {
                    if (wParam == 1) {
                        try (MemoryStack stack = MemoryStack.stackPush()) {
                            WINDOWPLACEMENT windowplacement = WINDOWPLACEMENT.calloc(stack);
                            User32.GetWindowPlacement(hwnd, windowplacement);
                            // ...but instead we're gonna just pretend it's just a RECT struct
                            // the NCCALCSIZE_PARAMS struct conveniently has what we need
                            // at the very start, so we can quietly say it's a RECT struct lol
                            // hacky because LWJGL doesn't include the structs to the aforementioned
                            // struct, nor some of the other structs contained
                            RECT rect = RECT.create(lParam);
//                            if (windowplacement.showCmd() != User32.SW_MAXIMIZE) {
//                                rect.left(rect.left() + 8);
////								rect.top(rect.top() + 0);
//                                rect.right(rect.right() - 8);
//                                rect.bottom(rect.bottom() - 8);
//                            } else {
//                                rect.left(rect.left() + 8);
//                                rect.top(rect.top() + 8);
//                                rect.right(rect.right() - 8);
//                                rect.bottom(rect.bottom() - 8);
//                            }

                            return rect.address();
                        }
                    }
                }
                return JNI.callPPPP(hwnd, uMsg, wParam, lParam, pWindowProc);
            }
        };
        System.out.println("procaddr: " + proc.address());
        System.out.println("setptr: " + User32.SetWindowLongPtr(hwnd, User32.GWL_WNDPROC, proc.address()));
        System.out.println("setwinptr: " + User32.SetWindowPos(hwnd, 0, 0, 0, 0, 0, User32.SWP_NOMOVE | User32.SWP_NOSIZE | User32.SWP_NOZORDER | User32.SWP_FRAMECHANGED));
    }

    public static void setWindowResizeListener(Actor actor) {
        actor.addListener(new InputListener() {
            private static final int TOP_BORDER = 0;
            private static final int BOTTOM_BORDER = 1;
            private static final int LEFT_BORDER = 2;
            private static final int RIGHT_BORDER = 3;
            private static final int BOTTOM_LEFT_CORNER = 4;
            private static final int BOTTOM_RIGHT_CORNER = 5;
            private static final int TOP_LEFT_CORNER = 6;
            private static final int TOP_RIGHT_CORNER = 7;

            private static final int BORDER_SIZE = 10;

            private final long context = GLFW.glfwGetCurrentContext();
            private float startX = 0;
            private float startY = 0;
            private float startW = 0;
            private float startH = 0;
            private float startWindowX = 0;
            private float startWindowY = 0;
            private int anchorPoint = -1;
            private final DoubleBuffer cursorX = BufferUtils.createDoubleBuffer(1);
            private final DoubleBuffer cursorY = BufferUtils.createDoubleBuffer(1);

            private final IntBuffer windowX = BufferUtils.createIntBuffer(1);
            private final IntBuffer windowY = BufferUtils.createIntBuffer(1);
            private final IntBuffer windowW = BufferUtils.createIntBuffer(1);
            private final IntBuffer windowH = BufferUtils.createIntBuffer(1);

            private int getX() {
                return MathUtils.floor((float) cursorX.get(0));
            }
            private int getY() {
                return MathUtils.floor((float) cursorY.get(0));
            }
            private int getWidth() {
                return MathUtils.floor((float) windowW.get(0));
            }
            private int getHeight() {
                return MathUtils.floor((float) windowH.get(0));
            }
            private int getWindowX() {
                return MathUtils.floor((float) windowX.get(0));
            }
            private int getWindowY() {
                return MathUtils.floor((float) windowY.get(0));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                GLFW.glfwGetCursorPos(context, cursorX, cursorY);
                GLFW.glfwGetWindowSize(context, windowW, windowH);
                GLFW.glfwGetWindowPos(context, windowX, windowY);
                anchorPoint = getAnchorPoint();

                if (anchorPoint == -1)
                    return false;

                startX = getX();
                startY = getY();
                startW = getWidth();
                startH = getHeight();
                startWindowX = getWindowX();
                startWindowY = getWindowY();
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                GLFW.glfwGetWindowPos(context, windowX, windowY);
                GLFW.glfwGetCursorPos(context, cursorX, cursorY);
                float offsetX = getX() - startX + (startWindowX - getWindowX());
                float offsetY = getY() - startY;
                GLFW.glfwGetWindowSize(context, windowW, windowH);
                switch (anchorPoint) {
                    case BOTTOM_BORDER:
                        GLFW.glfwSetWindowSize(context, (int) startW, (int)(startH + offsetY));
                        break;
                    case TOP_BORDER:
                        GLFW.glfwSetWindowSize(context, (int) startW, (int)(startH + offsetY));
                        GLFW.glfwSetWindowPos(context, getWindowX(), (int)(-getWindowY()+startWindowY + offsetY));
                        break;
                    case LEFT_BORDER:
                        GLFW.glfwSetWindowSize(context, (int)(startW - offsetX), (int) startH);
                        break;
                    case RIGHT_BORDER:
                        GLFW.glfwSetWindowSize(context, (int)(startW + offsetX), (int) startH);
                        break;
                    case BOTTOM_RIGHT_CORNER:
                        GLFW.glfwSetWindowSize(context, (int)(startW + offsetX), (int)(startH + offsetY));
                        break;
                    case TOP_RIGHT_CORNER:
                        GLFW.glfwSetWindowSize(context, (int)(startW + offsetX), (int)(startH - offsetY));
                        break;
                    case BOTTOM_LEFT_CORNER:
                        GLFW.glfwSetWindowSize(context, (int)(startW - offsetX), (int)(startH + offsetY));
                        break;
                    case TOP_LEFT_CORNER:
                        GLFW.glfwSetWindowSize(context, (int)(startW - offsetX), (int)(startH - offsetY));
                        break;
                }
            }

            private int getAnchorPoint() {
                if (getX() < BORDER_SIZE && getY() > BORDER_SIZE && getY() < getHeight() - BORDER_SIZE)
                    return LEFT_BORDER;
                if (getX() < BORDER_SIZE && getY() < BORDER_SIZE)
                    return TOP_LEFT_CORNER;
                if (getX() < BORDER_SIZE && getY() > getHeight() - BORDER_SIZE)
                    return BOTTOM_LEFT_CORNER;
                if (getX() > BORDER_SIZE && getY() > getHeight() - BORDER_SIZE && getX() < getWidth() - BORDER_SIZE)
                    return TOP_BORDER;
                if (getX() > getWidth() - BORDER_SIZE && getY() > getHeight() - BORDER_SIZE)
                    return BOTTOM_RIGHT_CORNER;
                if (getX() > getWidth() - BORDER_SIZE && getY() > BORDER_SIZE && getY() < getHeight() - BORDER_SIZE)
                    return RIGHT_BORDER;
                if (getX() > getWidth() - BORDER_SIZE && getY() < BORDER_SIZE)
                    return TOP_RIGHT_CORNER;
                if (getX() > BORDER_SIZE && getY() < BORDER_SIZE && getX() < getWidth() - BORDER_SIZE)
                    return BOTTOM_BORDER;
                return -1;
            }
        });
    }
    /**
     * OpenGL functionality, that goes beyond OpenGL ES.
     *
     * It's not 'unsafe' to run this on a desktop, but this kind of
     * functionality uses raw LWJGL and is not available on mobile devices or in
     * the browser.
     */
    public static class Unsafe {

        public static void polygonModeFill() {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        }

        public static void polygonModeWireframe() {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        }

    }

}



