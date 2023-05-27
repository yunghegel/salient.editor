package ui.inspector.material;

import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.esotericsoftware.minlog.Log;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import ui.UI;
import ui.elements.Frame;

import static com.badlogic.gdx.scenes.scene2d.ui.Cell.defaults;

public class OpacityInspector extends BaseMaterialInspector {

    BlendingAttribute blendingAttribute;
    Material material;
    public OpacityInspector(Material mat){
        this.material = mat;


        inspect(mat);




    }

    public void inspect(Material material){



        defaults().pad(UI.DEFAULT_PADDING).growX();

        Table table = this;
        table.clear();

        UI.header(table, "Material opacity");

        {
            Frame frame = UI.frameToggle("Blending", VisUI.getSkin(), material.has(BlendingAttribute.Type), b->{
                if(b){
                    material.set(new BlendingAttribute(true, 1f));
                }else{
                    material.remove(BlendingAttribute.Type);
                }
//                inspect(material);
            });
            if(material.has(BlendingAttribute.Type)){
                BlendingAttribute fa = material.get(BlendingAttribute.class, BlendingAttribute.Type);
                UI.slider(frame.getContentTable(), Attribute.getAttributeAlias(fa.type), 0, 1, fa.opacity, v->fa.opacity=v);
                // TODO blend modes
            }
            frame.getContentTable().align(Align.left);
            table.add(frame).fill().left().row();
        }
        {
            Frame frame = UI.frameToggle("Alpha test", VisUI.getSkin(), material.has(FloatAttribute.AlphaTest), b->{
                if(b){
                    material.set(FloatAttribute.createAlphaTest(.5f));
                }else{
                    material.remove(FloatAttribute.AlphaTest);
                }
//                inspect(material);
            });
            if(material.has(FloatAttribute.AlphaTest)){
                FloatAttribute fa = material.get(FloatAttribute.class, FloatAttribute.AlphaTest);
                UI.slider(frame.getContentTable(), Attribute.getAttributeAlias(fa.type), 0, 1, fa.value, v->fa.value=v);
            }
            frame.getContentTable().align(Align.left);
            table.add(frame).fill().left().row();
        }

    }

}
