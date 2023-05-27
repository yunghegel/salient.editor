package ui.inspector.material;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.VisUI;
import ui.UI;

public class BaseMaterialInspector extends Table {

    public BaseMaterialInspector() {
        setSkin(VisUI.getSkin());

        setBackground("tab-alpha");
        left();

    }

    protected Actor createTexture(Material material, long type){
        TextureAttribute ta = material.get(TextureAttribute.class, type);
        Texture texture = ta.textureDescription.texture;

        Table t = new Table(getSkin());

        // TODO will messup when ui is removed (lost attributes)
        t.add(UI.toggle(getSkin(), Attribute.getAttributeAlias(ta.type), true, v->{
            if(v) material.set(ta); else material.remove(ta.type);
        }));

        t.row();

        Image img = new Image(texture);
        img.setScaling(Scaling.fit);
        t.add(img).size(64);

        return t;
    }
}
