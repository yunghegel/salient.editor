package ui.widgets;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import dev.lyze.flexbox.FlexBox;
import ecs.systems.PickingSystem;
import scene.graph.GameObject;

public class TransformWidget extends VisTable {

    public Matrix4 transform = new Matrix4();
    public VisTable contentTable,posTable,rotTable,scaleTable;
    public VisLabel posX, posY, posZ;
    public VisLabel rotX, rotY, rotZ;
    public VisLabel scaleX, scaleY, scaleZ;
    public VisTextField posXField, posYField, posZField;
    public VisTextField rotXField, rotYField, rotZField;
    public VisTextField scaleXField, scaleYField, scaleZField;
    public VisTextButton applyButton, resetButton;

    public GameObject gameObject;


    public TransformWidget() {
        super();
        create();
        align(Align.top);
    }

    private void create() {
        contentTable = new VisTable();
        contentTable.add(new VisLabel("Translation")).left();
        contentTable.add(posTable = new VisTable()).growX().fillX().row();
        contentTable.addSeparator().colspan(2).row();
        contentTable.add(new VisLabel("Rotation")).left();
        contentTable.add(rotTable = new VisTable()).growX().fillX().row();
        contentTable.addSeparator().colspan(2).row();
        contentTable.add(new VisLabel("Scale")).left();
        contentTable.add(scaleTable = new VisTable()).growX().fillX().row();
        contentTable.addSeparator().colspan(2).row();

        posTable.align(Align.center);
        rotTable.align(Align.center);
        scaleTable.align(Align.center);

        posTable.pad(2,5,2,5);
        rotTable.pad(2,5,2,5);
        scaleTable.pad(2,5,2,5);


        posX = new VisLabel("X");
        posY = new VisLabel("Y");
        posZ = new VisLabel("Z");
        posTable.add(posX).size(10).align(Align.center).center().pad(3);
        posTable.add(posXField = new VisTextField()).size(40,20);
        posTable.add(posY).size(10).align(Align.center).center().pad(3);
        posTable.add(posYField = new VisTextField()).size(40,20);
        posTable.add(posZ).size(10).align(Align.center).center().pad(3);
        posTable.add(posZField = new VisTextField()).size(40,20).row();

        rotX = new VisLabel("X");
        rotY = new VisLabel("Y");
        rotZ = new VisLabel("Z");
        rotTable.add(rotX).size(10).align(Align.center).center().pad(3);
        rotTable.add(rotXField = new VisTextField()).size(40,20);
        rotTable.add(rotY).size(10).align(Align.center).center().pad(3);
        rotTable.add(rotYField = new VisTextField()).size(40,20);
        rotTable.add(rotZ).size(10).align(Align.center).center().pad(3);
        rotTable.add(rotZField = new VisTextField()).size(40,20).row();

        scaleX = new VisLabel("X");
        scaleY = new VisLabel("Y");
        scaleZ = new VisLabel("Z");
        scaleTable.add(scaleX).size(10).align(Align.center).center().pad(3);
        scaleTable.add(scaleXField = new VisTextField()).size(40,20);
        scaleTable.add(scaleY).size(10).align(Align.center).center().pad(3);
        scaleTable.add(scaleYField = new VisTextField()).size(40,20);
        scaleTable.add(scaleZ).size(10).align(Align.center).center().pad(3);
        scaleTable.add(scaleZField = new VisTextField()).size(40,20).row();


        contentTable.pad(5,2,5,2);

        VisTable buttonTable = new VisTable();
        buttonTable.pad(0,15,0,15);
        buttonTable.align(Align.center);
        buttonTable.add(applyButton = new VisTextButton("Apply")).left();
        buttonTable.add(resetButton = new VisTextButton("Reset")).left().row();
        contentTable.add(buttonTable).growX().fillX().colspan(2);

        add(contentTable);
    }

    public void setTransform(Matrix4 transform) {
        this.transform = transform;
        updateFields(transform);
    }

    private void updateFields(Matrix4 transform){
        Vector3 pos = new Vector3();
        Quaternion rot = new Quaternion();
        Vector3 scale = new Vector3();

        transform.getTranslation(pos);
        transform.getRotation(rot);
        transform.getScale(scale);

        posXField.setText(String.valueOf(pos.x));
        posYField.setText(String.valueOf(pos.y));
        posZField.setText(String.valueOf(pos.z));

        rotXField.setText(String.valueOf(rot.getPitch()));
        rotYField.setText(String.valueOf(rot.getYaw()));
        rotZField.setText(String.valueOf(rot.getRoll() ));

        scaleXField.setText(String.valueOf(scale.x));
        scaleYField.setText(String.valueOf(scale.y));
        scaleZField.setText(String.valueOf(scale.z));

    }

    @Override
    public void act(float delta){
        super.act(delta);
        if (PickingSystem.pickedObject != null) {
            setTransform(PickingSystem.pickedObject.getTransform().cpy());
        }
    }

    public void setGameObject(GameObject gameObject) {
    }
}
