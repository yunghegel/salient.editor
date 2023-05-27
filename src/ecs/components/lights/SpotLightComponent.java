package ecs.components.lights;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.environment.SpotLight;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import net.mgsx.gltf.scene3d.lights.SpotLightEx;
import scene.graph.GameObject;

public class SpotLightComponent extends LightComponent{

    public static final float DEFAULT_CUTOFF = 40.0f;

    public Vector3 direction;
    private float cutoff;

    // For PBR Shader only
    public float exponent;
    public float cutoffAngle;
    public float range = 10;

    public SpotLightEx spotLight;

    public SpotLightComponent(GameObject go) {
        super(go);
        this.direction = DEFAULT_DIRECTION.cpy();
        this.cutoff = DEFAULT_CUTOFF;
        spotLight = new SpotLightEx();
        spotLight.direction.set(DEFAULT_DIRECTION);
        spotLight.color.set(DEFAULT_COLOR);
        spotLight.intensity = DEFAULT_INTENSITY;
        spotLight.setConeDeg(DEFAULT_CUTOFF, DEFAULT_CUTOFF / 2f);

        go.sceneGraph.scene.salientEnvironment.addSpotLight(spotLight);
    }



    @Override
    public void render(float delta) {

    }

    @Override
    public void update(float delta) {
        spotLight.position.set(gameObject.getPosition(tmp));
    }

    public float getCutoff() {
        return cutoff;
    }

    public void setCutoff(float cutoff) {
        this.cutoff = cutoff;
        setConeDeg(cutoff, cutoff / 2f);
    }

    public void setRange(float range){
        this.range = range;
        spotLight.range = range;
    }

    public void setExponent(float exponent){
        this.exponent = exponent;
        spotLight.exponent = exponent;
    }

    public void setCutoffAngle(float cutoffAngle){
        this.cutoffAngle = cutoffAngle;
        spotLight.cutoffAngle = cutoffAngle;
    }

    public void setDirection(float x, float y, float z){
        this.direction.set(x,y,z);
        spotLight.direction.set(direction);
    }

    public void setDirection(Vector3 direction){
        this.direction = direction;
        spotLight.direction.set(direction);
    }

    public void setColor(Color color){
        this.color = color;
        spotLight.color.set(color);
    }

    public void setColor(float r, float g, float b, float a){
        this.color.set(r,g,b,a);
        spotLight.color.set(color);
    }

    public void setIntensity(float intensity){
        this.intensity = intensity;
        spotLight.intensity = intensity;
    }



    private void setConeRad(float outerConeAngleRad, float innerConeAngleRad)
    {
        spotLight.setConeRad(outerConeAngleRad, innerConeAngleRad);
    }

    private void setConeDeg(float outerConeAngleDeg, float innerConeAngleDeg)
    {
        spotLight.setConeDeg(outerConeAngleDeg,innerConeAngleDeg);
    }
    @Override
    public void toggleVisible() {
        super.toggleVisible();
        if(gameObject.sceneGraph.scene.salientEnvironment.spotLightsAttribute.lights.contains(spotLight, true))
            gameObject.sceneGraph.scene.salientEnvironment.spotLightsAttribute.lights.removeValue(spotLight, true);
        else
            gameObject.sceneGraph.scene.salientEnvironment.spotLightsAttribute.lights.add(spotLight);
        gameObject.sceneGraph.scene.salientEnvironment.setPointLights();
    }

}
