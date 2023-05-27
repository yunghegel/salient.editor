package backend.settings;

import com.google.gson.JsonObject;

public class Settings
{

    public static  float CAMERA_START_PITCH = 20f;                     // Default Starting pitch
    public static  float CAMERA_MIN_PITCH = Settings.CAMERA_START_PITCH - 20f;  // Min Pitch
    public static  float CAMERA_MAX_PITCH = Settings.CAMERA_START_PITCH + 20f;  // Max Pitch
    public static  float CAMERA_PITCH_FACTOR = 0.3f;
    public static  float CAMERA_ZOOM_LEVEL_FACTOR = 0.5f;              // Our zoom multiplier (speed)
    public static  float CAMERA_ANGLE_AROUND_PLAYER_FACTOR = 0.2f;     // Rotation around player speed
    public static  float CAMERA_MIN_DISTANCE_FROM_PLAYER = 4;          // Min zoom distance
    public static  float CAMERA_MAX_DISTANCE_FROM_PLAYER = 10f;

    public static  String PROJECT_NAME = "Salient";
    public static  String PROJECT_VERSION = "0.0.1";
    public static  String PROJECT_AUTHOR = "Salient Team";
    public static  String PROJECT_DESCRIPTION = "Salient is a 3D game engine written in Java using the LibGDX framework.";

    public enum CursorMode
    {
        CAPTURED, RELEASED
    }



    public enum Resolution
    {
        _720p(1280 , 720), _1080p(1920 , 1080), _1440p(2560 , 1440), _2160p(3840 , 2160);

        private int width;
        private int height;

        Resolution(int width , int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    public static class FilePaths
    {

        public static String ICON = "images/icon.png";
        public static String SKIN = "skin/tixel.json";
        public static String MODELS_DIR = "models";
        public static String RES_DIR = "resources";
        public static String SHADERS_DIR = "shaders/";
        public static String IMAGES_DIR = "images/";
        public static String CUBEMAP_DIR = "cubemaps/";

    }

    public static class Window
    {

        public static int WIDTH = 1920;
        public static int HEIGHT = 1080;
        public static String TITLE = "Salient app.Editor";

    }

    public static class Graphics
    {

        public static int ANTIALIASING = 2;
        public static int DEPTH = 8;
        public static int STENCIL = 8;
        public static boolean VSYNC = false;
        public static int IDLE_FPS = 144;

    }

    public static class GameConfig
    {
        public static boolean PROFILING_ENABLED = true;
        public static boolean DEBUG = true;
    }

    public static class Shadows {
        public static int SHADOWMAP_WIDTH = 1024;
        public static int SHADOWMAP_HEIGHT = 1024;
        public static float SHADOWMAP_VIEWPORT_WIDTH= 128f;
        public static float SHADOWMAP_VIEWPORT_HEIGHT = 128f;
        public static float SHADOWMAP_NEAR = 1f;
        public static float SHADOWMAP_FAR = 1000f;
        public static float SHADOWLIGHT_INTENSITY = 10f;
    }
    static JsonObject camera;
    static JsonObject project;
    static JsonObject graphics;
    static JsonObject shadows;
    static JsonObject paths;

    public static JsonObject getDefaultSettingsAsJsonObject(){
        JsonObject settings = new JsonObject();

        camera = new JsonObject();
        camera.addProperty("camera_start_pitch", CAMERA_START_PITCH);
        camera.addProperty("camera_min_pitch", CAMERA_MIN_PITCH);
        camera.addProperty("camera_max_pitch", CAMERA_MAX_PITCH);
        camera.addProperty("camera_pitch_factor", CAMERA_PITCH_FACTOR);
        camera.addProperty("camera_zoom_level_factor", CAMERA_ZOOM_LEVEL_FACTOR);
        camera.addProperty("camera_angle_around_player_factor", CAMERA_ANGLE_AROUND_PLAYER_FACTOR);
        camera.addProperty("camera_min_distance_from_player", CAMERA_MIN_DISTANCE_FROM_PLAYER);
        camera.addProperty("camera_max_distance_from_player", CAMERA_MAX_DISTANCE_FROM_PLAYER);


        project = new JsonObject();
        project.addProperty("project_name", PROJECT_NAME);
        project.addProperty("project_version", PROJECT_VERSION);
        project.addProperty("project_author", PROJECT_AUTHOR);
        project.addProperty("project_description", PROJECT_DESCRIPTION);


        graphics = new JsonObject();
        graphics.addProperty("antialiasing", Graphics.ANTIALIASING);
        graphics.addProperty("depth", Graphics.DEPTH);
        graphics.addProperty("stencil", Graphics.STENCIL);
        graphics.addProperty("vsync", Graphics.VSYNC);
        graphics.addProperty("idle_fps", Graphics.IDLE_FPS);
        graphics.addProperty("resolution_width",Window.WIDTH);
        graphics.addProperty("resolution_height",Window.HEIGHT);


        shadows = new JsonObject();
        shadows.addProperty("shadowmap_width", Shadows.SHADOWMAP_WIDTH);
        shadows.addProperty("shadowmap_height", Shadows.SHADOWMAP_HEIGHT);
        shadows.addProperty("shadowmap_viewport_width", Shadows.SHADOWMAP_VIEWPORT_WIDTH);
        shadows.addProperty("shadowmap_viewport_height", Shadows.SHADOWMAP_VIEWPORT_HEIGHT);
        shadows.addProperty("shadowmap_near", Shadows.SHADOWMAP_NEAR);
        shadows.addProperty("shadowmap_far", Shadows.SHADOWMAP_FAR);
        shadows.addProperty("shadowlight_intensity", Shadows.SHADOWLIGHT_INTENSITY);


        paths = new JsonObject();
        paths.addProperty("icon", FilePaths.ICON);
        paths.addProperty("skin", FilePaths.SKIN);
        paths.addProperty("models_dir", FilePaths.MODELS_DIR);
        paths.addProperty("res_dir", FilePaths.RES_DIR);
        paths.addProperty("shaders_dir", FilePaths.SHADERS_DIR);
        paths.addProperty("images_dir", FilePaths.IMAGES_DIR);
        paths.addProperty("cubemap_dir", FilePaths.CUBEMAP_DIR);


        settings.add("camera", camera);
        settings.add("project", project);
        settings.add("graphics", graphics);
        settings.add("shadows", shadows);
        settings.add("paths", paths);

        return settings;

    }



}
