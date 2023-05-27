package backend.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;

public class LocalSettings
{

    private static LocalSettings instance;

    String userHome = System.getProperty("user.home");
    String separator = File.separator;
    String projectDir = userHome + separator + ".salient";
    String projectSettingsDir = projectDir + separator + "settings";
    String projectSettingsFileName = "settings.salient";
    String projectSettingsFile = projectSettingsDir + separator + projectSettingsFileName;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public JsonObject localSettingsJson;
    public boolean hasSettingsFile = false;
    public JsonObject cameraSettings = new JsonObject();
    public JsonObject graphicsSettings = new JsonObject();
    public JsonObject pathSettings = new JsonObject();
    public JsonObject shadowSettings = new JsonObject();
    public JsonObject projectSettings = new JsonObject();

    public static LocalSettings getInstance() throws IOException
    {
        if (instance == null)
        {
            instance = new LocalSettings();
        }
        return instance;
    }

    public LocalSettings() throws IOException {







    }

    public void setup() throws IOException {

        File dir = new File(projectDir);
        if (!dir.exists()) {
            dir.mkdir();
        }

        File settingsDir = new File(projectSettingsDir);
        if (!settingsDir.exists()) {
            settingsDir.mkdir();

        }
        File settingsFile = new File(projectSettingsFile);
        if (!settingsFile.exists()) {
            FileWriter writer = new FileWriter(settingsFile);
            JsonObject jsonObject = Settings.getDefaultSettingsAsJsonObject();
            String output=  gson.toJson(jsonObject);
            PrintWriter printWriter = new PrintWriter(writer);
            printWriter.print(output);


            printWriter.close();
            writer.close();

        }
            hasSettingsFile = true;
            FileReader reader = new FileReader(projectSettingsFile);
            BufferedReader bufferedReader = new BufferedReader(reader);


            localSettingsJson = gson.fromJson(bufferedReader, JsonObject.class);
//            cameraSettings = localSettingsJson.get("camera").getAsJsonObject();
//            graphicsSettings = localSettingsJson.get("graphics").getAsJsonObject();
//            pathSettings = localSettingsJson.get("path").getAsJsonObject();
//            shadowSettings = localSettingsJson.get("shadow").getAsJsonObject();
//            projectSettings = localSettingsJson.get("app.project").getAsJsonObject();

            System.out.println(localSettingsJson);
            bufferedReader.close();
            reader.close();

    }

    public void save() throws IOException {
        FileWriter writer = new FileWriter(projectSettingsFile);
        String output=  gson.toJson(localSettingsJson);
        PrintWriter printWriter = new PrintWriter(writer);
        printWriter.print(output);
        printWriter.close();
        writer.close();
    }

    public void setCameraSetting(String key, String value) throws IOException {
        cameraSettings.remove(key);
        cameraSettings.addProperty(key, value);
        localSettingsJson.add("camera", cameraSettings);
        save();
    }

    public void setGraphicsSetting(String key, String value) throws IOException {
        graphicsSettings.addProperty(key, value);
        localSettingsJson.add("graphics", graphicsSettings);
        save();
    }

    public void setPathSetting(String key, String value) throws IOException {
        pathSettings.addProperty(key, value);
        localSettingsJson.add("path", pathSettings);

        save();
    }

    public void setShadowSetting(String key, String value) throws IOException {
        //replace the old value with the new value
        shadowSettings.remove(key);
        shadowSettings.addProperty(key, value);
        localSettingsJson.add("shadows", shadowSettings);
        save();
    }

    public void setProjectSetting(String key, String value) throws IOException {
        projectSettings.addProperty(key, value);
        localSettingsJson.add("project", projectSettings);
        save();
    }








}
