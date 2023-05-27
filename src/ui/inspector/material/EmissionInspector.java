package ui.inspector.material;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRFloatAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import ui.UI;
import ui.elements.Frame;

public class EmissionInspector extends BaseMaterialInspector{
    
    Material material;
    
    public EmissionInspector(Material material) {
        this.material = material;
        inspect(material);
    }

    private void inspect(Material material) {
        defaults().pad(UI.DEFAULT_PADDING).growX();

        Table table = this;
        table.clear();

        UI.header(table, "Material emission");

        {
            Frame frame = UI.frameToggle("Emission", getSkin(), material.has(PBRColorAttribute.Emissive), b->{
                if(b){
                    material.set(PBRColorAttribute.createEmissive(Color.WHITE));
                }else{
                    material.remove(PBRColorAttribute.Emissive);
                }
                inspect(material);
            });
            if(material.has(PBRColorAttribute.Emissive)){
                ColorAttribute fa = material.get(ColorAttribute.class, ColorAttribute.Emissive);
                UI.colorBox(frame.getContentTable(), "Emissive color", fa.color, false);
            }
            if(material.has(PBRTextureAttribute.EmissiveTexture)){
                frame.getContentTable().add(createTexture(material, PBRTextureAttribute.EmissiveTexture)).row();
            }
            frame.getContentTable().align(Align.left);
            table.add(frame).fill().row();
        }

        {
            Frame frame = UI.frameToggle("Strength", getSkin(), material.has(PBRFloatAttribute.EmissiveIntensity), b->{
                if(b){
                    material.set(PBRFloatAttribute.createEmissiveIntensity(1f));
                }else{
                    material.remove(PBRFloatAttribute.EmissiveIntensity);
                }
                inspect(material);
            });
            if(material.has(PBRFloatAttribute.EmissiveIntensity)){
                PBRFloatAttribute fa = material.get(PBRFloatAttribute.class, PBRFloatAttribute.EmissiveIntensity);
                UI.slider(frame.getContentTable(), Attribute.getAttributeAlias(fa.type), 0, 50, fa.value, v->fa.value=v);
            }
            frame.getContentTable().align(Align.left);
            table.add(frame).fill().row();
        }

    }


}
