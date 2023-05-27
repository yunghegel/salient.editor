package scene.env;

import app.Editor;
import com.badlogic.gdx.graphics.Color;
import com.github.ykrasik.jaci.api.Command;
import com.github.ykrasik.jaci.api.CommandPath;

@CommandPath("env/config/fog")
public class FogConfig {

    public float near = .1f;
    public float far = 100f;
    public float exponent = .5f;
    public Color color = new Color(Color.valueOf("7f7f7f4d"));

    public FogConfig(){

    }

    @Command
    public void setNear(float near){
        this.near = near;
        apply();
    }

    @Command
    public void setFar(float far){
        this.far = far;
        apply();
    }

    @Command
    public void setExponent(float exponent){
        this.exponent = exponent;
        apply();
    }

    @Command
    public void setColor(float r, float g, float b, float a){
        this.color = new Color(r,g,b,a);
        apply();
    }

    public void apply(){
        Editor.i().sceneContext.salientEnvironment.setEnvironment();
    }



}
