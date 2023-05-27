package ui.inspector;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import ecs.components.MaterialComponent;
import ui.elements.ColorBox;

import java.util.function.Supplier;

public class MaterialInspector extends VisTable {

    private MaterialComponent mc;

    public MaterialInspector(MaterialComponent material){
        this.mc = material;
        inspectMaterial(material.mat);
    }

    private void inspectMaterial(Material material){
        add("Color Attributes").row();
        for (Attribute attribute : mc.colorAttributes) {
            if (attribute instanceof ColorAttribute colorAttribute) {
                VisTable colorAttrTable = new VisTable();
                String name = colorAttribute.getClass().getSimpleName();
                Supplier<Color> colorSupplier = new Supplier<Color>() {
                    @Override
                    public Color get() {
                        return colorAttribute.color;
                    }
                };
                ColorBox colorBox = new ColorBox(colorAttribute.toString(),colorSupplier,true, VisUI.getSkin());

                colorAttrTable.add(new VisLabel(attribute.getClass().getSimpleName()));
                colorAttrTable.add(colorBox);
                add(colorAttrTable).row();
            }
        }
        add("Texture Attributes").row();
    for (Attribute attribute: mc.textureAttributes){
        if (attribute instanceof TextureAttribute textureAttribute){
            VisTable textureAttrTable = new VisTable();
            Texture attributeTexture = textureAttribute.textureDescription.texture;
            String name = textureAttribute.getClass().getSimpleName();
            VisImage textureImage = new VisImage(attributeTexture);
            textureAttrTable.add(new VisLabel(attribute.getClass().getSimpleName()));
            textureAttrTable.add(textureImage).size(50,50);
            add(textureAttrTable).row();
        }

    }

    }

}
