package ecs.components;

import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.utils.Array;
import scene.graph.GameObject;

public class MaterialComponent extends BaseComponent{

    public Material mat;
    public GameObject go;
    public Array<com.badlogic.gdx.graphics.g3d.Attribute> colorAttributes = new Array<>();
    public Array<com.badlogic.gdx.graphics.g3d.Attribute> textureAttributes = new Array<>();
    public Array<com.badlogic.gdx.graphics.g3d.Attribute> floatAttributes = new Array<>();
    public Array<Attribute> intAttributes = new Array<>();


    public MaterialComponent(GameObject go, Material material){
        super(go);
        this.mat = material;


        material.get(colorAttributes, ColorAttribute.Emissive|ColorAttribute.Diffuse|ColorAttribute.Ambient|ColorAttribute.Specular);
        material.get(textureAttributes, TextureAttribute.Diffuse|TextureAttribute.Normal| TextureAttribute.Specular);
        material.get(floatAttributes, FloatAttribute.Shininess| FloatAttribute.AlphaTest);
        material.get(intAttributes, IntAttribute.CullFace);
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void update(float delta) {

    }
}
