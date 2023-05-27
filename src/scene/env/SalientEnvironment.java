package scene.env;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.DirectionalLightsAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.PointLightsAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.SpotLightsAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.environment.SpotLight;
import com.github.ykrasik.jaci.api.Command;
import com.github.ykrasik.jaci.api.CommandOutput;
import com.github.ykrasik.jaci.api.CommandPath;
import net.mgsx.gltf.scene3d.attributes.FogAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;
import scene.SceneContext;
import utils.SceneUtils;

@CommandPath("env")
public class SalientEnvironment extends Environment {

    public SceneContext sceneContext;
    public SceneManager sceneManager;

    public FogConfig fogConfig;
    public LightConfig lightConfig;

    public DirectionalLightsAttribute directionalLightsAttribute;
    public SpotLightsAttribute spotLightsAttribute;
    public PointLightsAttribute pointLightsAttribute;
    public FogAttribute fogEquationAttribute;
    public ColorAttribute fogColorAttribute;
    public ColorAttribute ambientColorAttribute;


    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;

    private SceneSkybox skybox;
    private Cubemap skyboxCubemap;
    private DirectionalLightEx light;

    private CommandOutput output;

    public static SalientEnvironment i;



    public SalientEnvironment(SceneContext sceneContext){
        i = this;
        this.sceneContext = sceneContext;
        this.sceneManager = sceneContext.sceneManager;
        fogConfig = new FogConfig();
        lightConfig = new LightConfig();


        setEnvironment();
        createDefaultMaps();
        createDefaultSkybox();


    }

    private void createAttributes(){
        ambientColorAttribute = new ColorAttribute(ColorAttribute.AmbientLight, lightConfig.lum, lightConfig.lum, lightConfig.lum, 1f);
        fogEquationAttribute = new FogAttribute(FogAttribute.FogEquation).set(fogConfig.near, fogConfig.far, fogConfig.exponent);
        fogColorAttribute = new ColorAttribute(ColorAttribute.Fog , new Color(fogConfig.color));
        directionalLightsAttribute = new DirectionalLightsAttribute();
        spotLightsAttribute = new SpotLightsAttribute();
        pointLightsAttribute = new PointLightsAttribute();

        light = new DirectionalLightEx();
        lightConfig.configure(light);
        directionalLightsAttribute.lights.clear();
        directionalLightsAttribute.lights.add(light);

    }

    public void setEnvironment(){
        createAttributes();
        setFog();
        setAmbientLight();
        setDirectionalLight();
        setSpotLights();
        setPointLights();
    }

    private void createDefaultMaps(){
        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        environmentCubemap = iblBuilder.buildEnvMap(1024);
        diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();

        brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));
    }

    public void createDefaultSkybox(){


        skyboxCubemap = SceneUtils.createCubemapDirectionFormat("editor");
        skybox = new SceneSkybox(skyboxCubemap);
        sceneManager.setSkyBox(skybox);
        sceneManager.setEnvironmentRotation(360);
    }

    @Command(description = "Enable fog")
    public void setFog(){
        set(fogEquationAttribute);
        set(fogColorAttribute);
        set(( FogAttribute.createFog(fogConfig.near , fogConfig.far , fogConfig.exponent) ));
    }

    @Command(description = "Disable fog")
    public void removeFog(){
        remove(FogAttribute.FogEquation);
        remove(ColorAttribute.Fog);
    }

    @Command(description = "Enable ambient light")
    public void setAmbientLight(){
        set(ambientColorAttribute);
    }

    @Command(description = "Disable ambient light")
    public void removeAmbientLight(){
        remove(ColorAttribute.AmbientLight);
    }

    @Command(description = "Enabledirectional light")
    public void setDirectionalLight(){
        set(directionalLightsAttribute);
    }

    @Command(description = "Disable directional light")
    public void removeDirectionalLight(){
        remove(DirectionalLightsAttribute.Type);
    }

    @Command(description = "Enable spot lights")
    public void setSpotLights(){
        set(spotLightsAttribute);
    }

    @Command(description = "Disable spot lights")
    public void removeSpotLights(){
        remove(SpotLightsAttribute.Type);
    }

    @Command(description = "Enable point lights")
    public void setPointLights(){
        set(pointLightsAttribute);
    }

    @Command(description = "Disable point lights")
    public void removePointLights(){
        remove(PointLightsAttribute.Type);
    }

    public void addPointLight(PointLight light){
        pointLightsAttribute.lights.add(light);
        setPointLights();
    }

    public void removePointLight(PointLight light){
        pointLightsAttribute.lights.removeValue(light, true);
        setPointLights();
    }

    public void addSpotLight(SpotLight light){
        spotLightsAttribute.lights.add(light);
        setSpotLights();
    }

    public void removeSpotLight(SpotLight light){
        spotLightsAttribute.lights.removeValue(light, true);
        setSpotLights();
    }

}
