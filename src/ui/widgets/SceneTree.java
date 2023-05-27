package ui.widgets;

import backend.tools.Log;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;
import app.Editor;
import ecs.components.BaseComponent;
import ecs.systems.PickingSystem;
import scene.SceneContext;
import scene.graph.GameObject;

public class SceneTree extends VisTree {

    public SceneContext scene;
    public TitleNode title;
    public GameObjectNode root;
    VisImageButton visibleButton;

    public static SceneTree sceneTree;
    Texture modelIcon = new Texture("button_icons/geometry_icon.png");
    Texture lightIcon = new Texture("button_icons/lights_icon.png");
    static Drawable visibleOn;
    static Drawable visibleOff;
    Skin skin;

    public static GameObjectNode hoveredNode;

    public static SceneTree getInstance() {
        if (sceneTree == null) {
            sceneTree = new SceneTree(Editor.i().sceneContext);
        }
        return sceneTree;
    }

    public SceneTree(SceneContext scene){
        super();
        sceneTree = this;
        this.scene = scene;
        root = new GameObjectNode(scene.sceneGraph.getRoot());
        title = new TitleNode("Scene");

        add(title);
        title.add(root);
        root.expandTo();
        root.setExpanded(true);

        setPadding(5);
        populateTree();
        initListeners();

        createDragAndDrop();
        skin = Editor.i().skin;
        visibleOn = skin.getDrawable("icon-visible-on");
        visibleOff = skin.getDrawable("icon-visible-off");
        VisImageButton visibleButton = new VisImageButton(visibleOn, visibleOff, visibleOn);




    }

    private void createDragAndDrop() {


        addListener(new ClickListener() {


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(getOverNode()!=null){
                    if(getOverNode() !=null){
                        if(getOverNode() instanceof GameObjectNode node){
                            if(node.obj!=null){

                                Editor.i().editorUI.dragAndDrop.addSource(new DragAndDrop.Source(node.getActor()) {
                                    @Override
                                    public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                                        DragAndDrop.Payload payload = new DragAndDrop.Payload();
                                        payload.setObject(getOverNode());
                                        payload.setDragActor(((GameObjectNode) getOverNode()).table);
                                        return payload;
                                    }
                                });
                            }
                        }
                    }
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(getOverNode().getActor()!=null){
                Editor.i().editorUI.dragAndDrop.addTarget(new DragAndDrop.Target(getOverNode().getActor()) {
                    @Override
                    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                        GameObjectNode goNode = (GameObjectNode) payload.getObject();
                        goNode.getActor().setPosition(x,y);

                        payload.getDragActor().setPosition(x,y);
                        return true;
                    }

                    @Override
                    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                        GameObjectNode goNode = (GameObjectNode) payload.getObject();

                        goNode.getParent().remove(goNode);

                        goNode.obj.getParent().getChildren().removeValue(goNode.obj, true);


                        getOverNode().add((GameObjectNode) payload.getObject());
                        GameObjectNode overNode = (GameObjectNode) getOverNode();
                        overNode.obj.addChild(goNode.obj);



                    }
                });
                super.touchUp(event, x, y, pointer, button);
            }}
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(getOverNode()!=null){
                    if(getOverNode() instanceof GameObjectNode node){
                        if(node.obj!=null){
                            Editor.i().ecs.pickingSystem.setPickedObject(node.obj);
                        }
                    }
                    if(getTapCount()==2){
                        if(getOverNode() instanceof GameObjectNode node){
                            if(node.obj!=null){
                                Editor.i().ecs.pickingSystem.setPickedObject(null);
                            }
                        }
                    }
                }
                super.clicked(event, x, y);
            }

        });

    }


    private void initListeners() {
        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(getSelectedNode() instanceof GameObjectNode node){
//                    Editor.i().ecs.pickingSystem.setPickedObject(node.obj);
                }
            }
        });
    }

    public void populateTree(){
        root.clearChildren();
        for (GameObject go : scene.sceneGraph.getGameObjects()){
            addGameObject(go, root);
        }
//        expandAll();
    }



    public void addGameObject(GameObject go, GameObjectNode parent){
        GameObjectNode goNode = new GameObjectNode(go);
        Array<String> tags = go.getTags();
        if(tags!=null){
            for (String tag : tags) {
                if (tag.equals("model")) {
                    TextureRegion model = new TextureRegion(modelIcon);
                    TextureRegionDrawable modelIcon = new TextureRegionDrawable(model);
                    goNode.setIcon(modelIcon);
                } else if (tag.equals("light")) {
                    TextureRegion light = new TextureRegion(lightIcon);
                    TextureRegionDrawable lightIcon = new TextureRegionDrawable(light);

                    goNode.setIcon(lightIcon);
                }
            }
        }
        parent.add(goNode);
        for (Component bc : go.getEntity().getComponents()){
            ComponentNode cn = new ComponentNode((BaseComponent) bc);
            goNode.title.add(cn);
        }
        if(go.getChildren()!=null){
            for (GameObject child : go.getChildren()){
                addGameObject(child, goNode);
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);




        if(PickingSystem.pickedObject!=null){
            for (Node node : root.getChildren())
                if(node instanceof GameObjectNode n)
                    if (PickingSystem.pickedObject == n.obj && !n.isExpanded()) {
//                        Editor.i().editorUI.transformWidget.setTransform(n.obj.getTransform());

                }
        }

        if(getSelectedNode()instanceof GameObjectNode gonNode){
            gonNode.getActor().setBackground("white-alpha");
        }

//        Array<Node> nodes = root.getChildren();
//        for(Node node : nodes){
//            if(node instanceof GameObjectNode gon&&node!=getSelectedNode()){
//                int index = gon.getParent().getChildren().indexOf(gon, true);
//                if(index%2==0) {
//                    gon.getActor().setBackground("highlighted");
//                }
//            }
//        }

    }

    private void highlightNode(Node node){
        if(node instanceof GameObjectNode gon){
            int index = gon.getParent().getChildren().indexOf(gon, true);
            if(index%2==0) {
                gon.getActor().setBackground("highlighted");
            }
            for(Node child : gon.getChildren()){
                highlightNode(child);
            }
        }
    }

    @Override
    public Node getSelectedNode() {
        return hoveredNode;
    }

    public static class TitleNode extends Tree.Node<VisTree.Node, String, VisLabel>
    {

        public TitleNode(String value) {
            super(new VisLabel(value));
        }

    }

    public static class GameObjectNode extends Tree.Node<VisTree.Node, GameObject, VisTable>
    {
        GameObject obj;
        TitleNode title;

        VisTable table;




        public GameObjectNode(GameObject value) {

            super(new VisTable());
            this.obj = value;
            Drawable drawable = Editor.i().skin.getDrawable("icon-visible-on");
            Drawable drawable2 = Editor.i().skin.getDrawable("icon-visible-off");
            ImageButton visibleButton = new ImageButton(drawable,drawable,drawable2);
            visibleButton.setChecked(false);
            visibleButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    obj.visible=!visibleButton.isChecked();
                    obj.toggleVisibility();
                }
            });
            HorizontalGroup group = new HorizontalGroup();
            group.fill();
            group.expand();

            group.setFillParent(true);
            group.align(Align.right);
//            group.padLeft(100);

            Table buttonTable = new Table();



            group.addActor(buttonTable);

            MenuBar menuBar = new MenuBar();
            table = getActor();
            table.padLeft(5);
            table.padTop(2);
            table.padBottom(2);
            table.padRight(5);
            table.setSkin(VisUI.getSkin());
            table.setTouchable(Touchable.enabled);
            table.add(new VisLabel(value.name));
            table.add(visibleButton).size(20,20).padLeft(5);


            table.addListener(new InputListener(){

                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    table.setBackground("white-alpha");
                    Editor.i().editorUI.sceneTree.setOverNode(GameObjectNode.this);

                    super.enter(event, x, y, pointer, fromActor);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    table.setBackground("clear");
                    SceneTree.hoveredNode = null;
                    super.exit(event, x, y, pointer, toActor);
                }

                ;
            });
//            table.add(menuBar.getTable()).expandX().growX();







            table.setSkin(VisUI.getSkin());
//            table.add(new CheckBox("",Editor.i().skin,"default")).size(10,10);
            title = new TitleNode("Components");
            add(title);
            obj = value;
        }

    }

    public static class ComponentNode extends Tree.Node<VisTree.Node, BaseComponent, VisLabel>
    {

        public ComponentNode(BaseComponent value) {
            super(new VisLabel(value.getClass().getSimpleName()));
        }

    }

}
