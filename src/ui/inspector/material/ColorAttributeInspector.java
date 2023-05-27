package ui.inspector.material;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import net.mgsx.gltf.scene3d.attributes.*;
import ui.UI;

import static com.badlogic.gdx.scenes.scene2d.ui.Cell.defaults;

public class ColorAttributeInspector extends BaseMaterialInspector {

    Material material;


    public ColorAttributeInspector(Material material) {
        this.material = material;
        inspect();
    }

    void inspect(){
        defaults().pad(UI.DEFAULT_PADDING).growX();

        Table table = this;
        table.clear();



        UI.header(table, "Material " + material.id);

        if(!material.has(IntAttribute.CullFace)){
            material.set(IntAttribute.createCullFace(GL20.GL_BACK));
        }

        // Base color
        if(material.has(PBRColorAttribute.BaseColorFactor)){
            PBRColorAttribute fa = material.get(PBRColorAttribute.class, PBRColorAttribute.BaseColorFactor);
            UI.colorBox(table, "Base color", fa.color, false);
        }
        if(material.has(PBRTextureAttribute.BaseColorTexture)){
            table.add(createTexture(material, PBRTextureAttribute.BaseColorTexture)).row();
        }

        if(material.has(PBRFlagAttribute.Unlit)){

            table.add("Unlit material").row();

        }else{

            // metal roughness
            if(material.has(PBRFloatAttribute.Metallic)){
                PBRFloatAttribute fa = material.get(PBRFloatAttribute.class, PBRFloatAttribute.Metallic);
                Table t = new Table();
                t.align(Align.left);
                t.setSkin(getSkin());
                UI.slider(t, Attribute.getAttributeAlias(fa.type), 0, 1, fa.value, v->fa.value=v);
//                Slider slider = UI.slider(0f,1f,0.01f,false,getSkin(),fa.value, v->fa.value=v);
//                Table t = new Table();
//                t.setSkin(getSkin());
//                t.add(Attribute.getAttributeAlias(fa.type));
//                t.add(slider).left().width(150).row();
//                table.add(t).row();
                table.add(t).left().row();
            }
            if(material.has(PBRFloatAttribute.Roughness)){
                PBRFloatAttribute fa = material.get(PBRFloatAttribute.class, PBRFloatAttribute.Roughness);
                Table t = new Table();
                t.align(Align.left);
                t.setSkin(getSkin());
                UI.slider(t, Attribute.getAttributeAlias(fa.type), 0, 1, fa.value, v->fa.value=v);

                table.add(t).left().row();
            }
            if(material.has(PBRTextureAttribute.MetallicRoughnessTexture)){
                table.add(createTexture(material, PBRTextureAttribute.MetallicRoughnessTexture)).row();
            }

            // normals
            if(material.has(PBRTextureAttribute.NormalTexture)){
                table.add(createTexture(material, PBRTextureAttribute.NormalTexture)).row();

                if(material.has(PBRFloatAttribute.NormalScale)){
                    PBRFloatAttribute fa = material.get(PBRFloatAttribute.class, PBRFloatAttribute.NormalScale);
                    Table t = new Table();
                    t.align(Align.left);
                    t.setSkin(getSkin());
                    UI.slider(t, Attribute.getAttributeAlias(fa.type), 0, 2, fa.value, v->fa.value=v);
                    table.add(t).left().row();
                }
            }

            // AO
            if(material.has(PBRTextureAttribute.OcclusionTexture)){
                table.add(createTexture(material, PBRTextureAttribute.OcclusionTexture)).row();

                if(material.has(PBRFloatAttribute.OcclusionStrength)){
                    Table t = new Table();
                    t.align(Align.left);
                    t.setSkin(getSkin());
                    PBRFloatAttribute fa = material.get(PBRFloatAttribute.class, PBRFloatAttribute.OcclusionStrength);
                    UI.slider(t, Attribute.getAttributeAlias(fa.type), 0, 2, fa.value, v->fa.value=v);
                    table.add(t).left().row();
                }
            }
        }



        // Options



        if(material.has(IntAttribute.CullFace)){
            IntAttribute ia = material.get(IntAttribute.class, IntAttribute.CullFace);

            Table t = new Table(getSkin());
            t.align(Align.left);
            t.add("Cull Face").padLeft(5);

            // TODO static
            IntMap<String> cullingMap = new IntMap<String>();
            cullingMap.put(GL20.GL_FRONT, "GL_FRONT");
            cullingMap.put(GL20.GL_BACK, "GL_BACK");
            cullingMap.put(GL20.GL_FRONT_AND_BACK, "GL_FRONT_AND_BACK");
            cullingMap.put(GL20.GL_NONE, "GL_NONE");
            cullingMap.put(-1, "Inherit default");

            Array<Integer> items = new Array<Integer>();
            items.addAll(GL20.GL_FRONT, GL20.GL_BACK, GL20.GL_FRONT_AND_BACK, GL20.GL_NONE, -1);

            t.add(UI.selector(getSkin(), items, ia.value, v->cullingMap.get(v), v->ia.value=v)).padLeft(20);

            t.align(Align.left);
            table.add(t).row();
        }

        table.align(Align.left);



    }

}
