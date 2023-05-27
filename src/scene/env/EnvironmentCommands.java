package scene.env;

import app.Editor;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.github.ykrasik.jaci.api.Command;
import com.github.ykrasik.jaci.api.CommandPath;
import com.github.ykrasik.jaci.api.DoubleParam;
import com.github.ykrasik.jaci.api.ToggleCommand;
import net.mgsx.gltf.scene3d.attributes.FogAttribute;
import scene.SceneContext;

@CommandPath("env")
public class EnvironmentCommands {

    SalientEnvironment env;
    SceneContext sc;

    public EnvironmentCommands(){
        sc = Editor.i().sceneContext;
        env = sc.salientEnvironment;
    }

    @Command(description = "Enable fog")
    public void enableFog(){
        env.setFog();
    }

    @Command(description = "Disable fog")
    public void disableFog(){
        env.removeFog();
    }

    @Command(description = "Enable ambient light")
    public void enableAmbientLight(){
        env.setAmbientLight();
    }

    @Command(description = "Disable ambient light")
    public void disableAmbientLight(){

    }

    @Command(description = "Set ambient light intensity")
    public void ambientIntensity(@DoubleParam double intensity){

    }

    @Command(description = "Enable all environment features")
    public void enableAll(){
        env.setFog();
        env.setAmbientLight();
        env.setPointLights();
        env.setSpotLights();
        env.setDirectionalLight();
    }

    @Command(description = "Disable all environment features")
    public void disableAll(){
        env.removeFog();
        env.removeAmbientLight();
        env.removePointLights();
        env.removeSpotLights();
        env.removeDirectionalLight();

    }

}
