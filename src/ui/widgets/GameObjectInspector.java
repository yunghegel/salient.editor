package ui.widgets;

import app.Editor;
import backend.tools.Log;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.*;
import ecs.components.BaseComponent;
import ecs.components.MaterialComponent;
import ecs.components.TransformComponent;
import scene.graph.GameObject;
import ui.inspector.material.ColorAttributeInspector;
import ui.inspector.material.EmissionInspector;
import ui.inspector.material.OpacityInspector;

public class GameObjectInspector extends VisTable {

    Texture cameraIconTexture = new Texture("button_icons/camera_icon.png");
    Texture envIconTexture = new Texture("button_icons/lighting_icon.png");
    Texture geometryIconTexture = new Texture("button_icons/geometry_icon.png");
    Texture materialIconTexture = new Texture("button_icons/materials_icon.png");
    Texture transformIconTexture = new Texture("button_icons/transform_icon.png");
    Texture settingsIconTexture = new Texture("button_icons/settings_icon.png");
    TextureRegionDrawable cameraIcon = new TextureRegionDrawable(cameraIconTexture);
    TextureRegionDrawable envIcon = new TextureRegionDrawable(envIconTexture);
    TextureRegionDrawable geometryIcon = new TextureRegionDrawable(geometryIconTexture);
    TextureRegionDrawable materialIcon = new TextureRegionDrawable(materialIconTexture);
    TextureRegionDrawable transformIcon = new TextureRegionDrawable(transformIconTexture);
    TextureRegionDrawable settingsIcon = new TextureRegionDrawable(settingsIconTexture);
    VisImageButton cameraButton = new VisImageButton(cameraIcon);
    VisImageButton envButton = new VisImageButton(envIcon);
    VisImageButton geometryButton = new VisImageButton(geometryIcon);
    VisImageButton materialButton = new VisImageButton(materialIcon);
    VisImageButton transformButton2 = new VisImageButton(transformIcon);
    VisImageButton settingsButton = new VisImageButton(settingsIcon);

    public VisTable root;
    public VisTable menuBar;
    public Table displayTable;
    public VisScrollPane rootScrollPane;
    public GameObject inspectedGameObject;
    public TransformWidget transformWidget;

    public Table lastTable;

    public Table transformTable, geometryTable, materialTable, cameraTable, envTable, settingsTable;



    public GameObjectInspector(){
        padTop(0);
        align(Align.center);

        root = new VisTable();
        menuBar = new VisTable();
        menuBar.setSkin(Editor.i().skin);
        menuBar.setBackground("dark-highlighted");
//        menuBar.setColor(0f, 0f, 0, 0.5f);
        menuBar.setWidth(14);
        menuBar.align(Align.top);
        menuBar.padLeft(3);
        menuBar.padRight(3);


        VerticalGroup buttonGroup = new VerticalGroup();
        buttonGroup.space(5);
        buttonGroup.addActor(settingsButton);
        buttonGroup.addActor(geometryButton);
        buttonGroup.addActor(materialButton);
        buttonGroup.addActor(transformButton2);
        buttonGroup.addActor(cameraButton);
        buttonGroup.addActor(envButton);

        menuBar.add(buttonGroup).expand().fill().align(Align.top).padTop(10);

        root.add(menuBar).expandY().fillY().align(Align.topLeft);

        displayTable = new Table();
        displayTable.align(Align.topLeft);
        root.add(displayTable).expand().fill().align(Align.topLeft);


        add(root).expand().fill().align(Align.topLeft);

        createTables();


        createListeners();
        displayTable.pad(10);

        displayTable.add(createTransformTable(new Matrix4())).growX().center();


    }

    private void createListeners() {
        materialButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                displayTable.clear();
                displayTable.add(materialTable).expand().fill().align(Align.topLeft);
                lastTable = materialTable;
            }
        });
        transformButton2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                displayTable.clear();
                displayTable.add(transformTable).expand().fill().align(Align.topLeft);
                lastTable = transformTable;
            }
        });
        cameraButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                displayTable.clear();
                displayTable.add(cameraTable).expand().fill().align(Align.topLeft);
            }
        });
        envButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                displayTable.clear();
                displayTable.add(envTable).expand().fill().align(Align.topLeft);
            }
        });
        geometryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                displayTable.clear();
                displayTable.add(geometryTable).expand().fill().align(Align.topLeft);
            }
        });
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                displayTable.clear();
                displayTable.add(settingsTable).expand().fill().align(Align.topLeft);
            }
        });

    }

    private void createTables() {

        transformTable = new VisTable();
        geometryTable = new VisTable();
        materialTable = new Table();
        materialTable.align(Align.topLeft);
        cameraTable = new VisTable();
        envTable = new VisTable();
        settingsTable = new VisTable();

        transformTable.add(new VisLabel("Transform"));
        geometryTable.add(new VisLabel("Geometry"));
        materialTable.add(new VisLabel("Material"));
        cameraTable.add(new VisLabel("Camera"));
        envTable.add(new VisLabel("Environment"));
        settingsTable.add(new VisLabel("Settings"));
    }

    public void setGameObject(GameObject gameObject){
        this.inspectedGameObject = gameObject;
        inspect();
        if(lastTable != null){
            displayTable.clear();
            displayTable.add(lastTable).expand().fill().align(Align.topLeft);
        }
        transformWidget.setGameObject(gameObject);
    }

    public void inspect(){


        if (inspectedGameObject == null) {
            return;
        }
        Array<BaseComponent> components = inspectedGameObject.components;
        for (BaseComponent component : components){
            if (component instanceof MaterialComponent mc){
                materialTable.clear();
                materialTable = createMaterialTable(mc.mat);
            }
            if(component instanceof TransformComponent tc){
                transformTable.clear();
                transformTable = createTransformTable(tc.getTransform());
            }
        }

    }

    private Table createTransformTable(Matrix4 transform){
        Table table = new Table();
        transformWidget = new TransformWidget();
        transformWidget.setTransform(transform);


        table.add(transformWidget).expand().fill().align(Align.topLeft);
        return table;
    }

    private Table createGeometryTable(){
        Table table = new Table();

        return table;
    }

    private Table createMaterialTable(Material material){
        Table table = new Table();

        OpacityInspector opacityInspector = new OpacityInspector(material);
        table.add(opacityInspector).align(Align.topLeft).growX().pad(5).row();
        ColorAttributeInspector colorAttributeInspector = new ColorAttributeInspector(material);
        table.add(colorAttributeInspector).align(Align.topLeft).growX().pad(5).row();
        EmissionInspector emissionInspector = new EmissionInspector(material);
        table.add(emissionInspector).align(Align.topLeft).growX().pad(5).row();
        table.pad(3);

        table.align(Align.topLeft);

        return table;
    }

    private Table createCameraTable(){
        Table table = new Table();

        return table;
    }

    private Table createEnvTable(){
        Table table = new Table();

        return table;
    }

    private Table createSettingsTable(){
        Table table = new Table();
        table.add(new VisLabel("Settings"));
        return table;
    }



}
