package ecs.components.lights;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Vector3;
import net.mgsx.gltf.scene3d.lights.PointLightEx;
import scene.graph.GameObject;

public class PointLightComponent extends LightComponent {

    public PointLightEx pointLight;
    public float range=10;



    public PointLightComponent(GameObject go) {
        super(go);
        pointLight = new PointLightEx();
        pointLight.set(DEFAULT_COLOR, DEFAULT_POSITION,DEFAULT_INTENSITY);
        pointLight.range = range;

        go.sceneGraph.scene.salientEnvironment.addPointLight(pointLight);
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void update(float delta) {
        pointLight.position.set(gameObject.getPosition(tmp));
    }

    public void setRange(float range){
        this.range = range;
        pointLight.range = range;
    }

    public float getRange(){
        return range;
    }

    public void setIntensity(float intensity){
        this.intensity = intensity;
        pointLight.intensity = intensity;
    }

    public float getIntensity(){
        return intensity;
    }

    public void setColor(Color color){
        this.color = color;
        pointLight.color.set(color);
    }

    public Color getColor(){
        return color;
    }

    @Override
    public void toggleVisible(){
        if(gameObject.sceneGraph.scene.salientEnvironment.pointLightsAttribute.lights.contains(pointLight, true))
            gameObject.sceneGraph.scene.salientEnvironment.removePointLight(pointLight);
        else
            gameObject.sceneGraph.scene.salientEnvironment.addPointLight(pointLight);

    }




}
