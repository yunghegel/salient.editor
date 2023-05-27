package scene.env;

import app.Editor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.github.ykrasik.jaci.api.Command;
import com.github.ykrasik.jaci.api.CommandPath;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;

@CommandPath("config")
public class LightConfig {

    public Color color = new Color(Color.WHITE);
    public Vector3 direction = new Vector3(-1,-1,-1);
    public float intensity = 40;
    public float lum = 0.01f;

    public LightConfig(){

    }
    public DirectionalLightEx configure(DirectionalLightEx light){
        return light.set(color, direction, intensity);
    }
    public void set(DirectionalLightEx light) {
        light.intensity = intensity;
        light.color.set(color);
        light.direction.set(direction);
    }

    @Command(description = "Set light color")
    public void setColor(double r, double g, double b, double a){
        this.color = new Color((float) r,(float)g,(float)b,(float)a);
        apply();
    }

    @Command
    public void setDirection(double x, double y, double z){
        this.direction = new Vector3((float)x,(float)y,(float)z);
        apply();
    }

    @Command
    public void setIntensity(int intensity){
        this.intensity = intensity;

        apply();
    }

    @Command
    public void setLum(double lum){
        this.lum =(float) lum;
        Editor.i().sceneContext.sceneManager.setAmbientLight((float)lum);
        apply();
    }



    private void apply() {
        Editor.i().sceneContext.salientEnvironment.setEnvironment();
    }


}
