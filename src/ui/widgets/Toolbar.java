package ui.widgets;


import app.Editor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import ecs.systems.PickingSystem;


public class Toolbar extends VisWindow
{

    public VisImageButton physicsButton;
    public VisImageButton translateButton;
    public VisImageButton scaleButton;
    public VisImageButton rotateButton;
    public VisImageButton selectButton;
    private VisTable toolSidebar;

    public Toolbar() {
        super(" ");
        setBackground("color-picker-bg");
//        setColor(0.5f, 0.5f, 0.5f, 0.5f);

        removeActor(getTitleTable());
        getTitleTable().clear();
        getTitleLabel().clear();
        init();
    }

    Texture translateIcon = new Texture("button_icons/translate_tool_icon.png");
    TextureRegionDrawable translateDrawable = new TextureRegionDrawable(translateIcon);

    Texture scaleIcon = new Texture("button_icons/scale_tool_icon.png");
    TextureRegionDrawable scaleDrawable = new TextureRegionDrawable(scaleIcon);

    Texture physicsIcon= new Texture("button_icons/physics_tool_button.png");
    TextureRegionDrawable physicsDrawable = new TextureRegionDrawable(physicsIcon);

    Texture rotateIcon = new Texture("button_icons/rotate_tool_icon.png");
    TextureRegionDrawable rotateDrawable = new TextureRegionDrawable(rotateIcon);

    Texture selectIcon = new Texture("button_icons/selection_tool_icon.png");
    TextureRegionDrawable selectDrawable = new TextureRegionDrawable(selectIcon);

    private void init(){
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();

        VerticalGroup buttonGroup = new VerticalGroup();
        buttonGroup.space(5);

        physicsButton = new VisImageButton(physicsDrawable, physicsDrawable, physicsDrawable,"default");
        physicsButton.setSkin(Editor.i().skin);

        physicsButton.getImageCell().getTable().setBackground("clear");
        translateButton = new VisImageButton(translateDrawable, translateDrawable, translateDrawable,"default") {
          @Override
            public void act(float delta) {
                super.act(delta);

            }
        };

        scaleButton = new VisImageButton(scaleDrawable, scaleDrawable, scaleDrawable,"default");
        rotateButton = new VisImageButton(rotateDrawable, rotateDrawable, rotateDrawable,"default");
        selectButton = new VisImageButton(selectDrawable, selectDrawable, selectDrawable,"default");
        selectButton.setChecked(true);


        translateButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event , Actor actor) {

                if(translateButton.isChecked()){
                    Editor.i().ecs.toolSystem.toolManager.translateTool.enable();
                    translateButton.setChecked(true);
                } else {
                    translateButton.setChecked(false);
                    Editor.i().ecs.toolSystem.toolManager.translateTool.disable();
                }





            }
        });

        scaleButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {


                if (PickingSystem.pickedObject == null) {
                    return;
                }

//                if (!enabled) {
//
//                } else {
//                    translateButton.setChecked(false);
//                    rotateButton.setChecked(false);
//                }
            }});


                rotateButton.addListener(new ChangeListener() {
                @Override
                public void changed (ChangeEvent event, Actor actor){
                    if(translateButton.isChecked()){
                        Editor.i().ecs.toolSystem.toolManager.rotateTool.enable();
                        rotateButton.setChecked(true);
                    } else {
                        rotateButton.setChecked(false);
                        Editor.i().ecs.toolSystem.toolManager.rotateTool.disable();
                    }

//                    Editor.i().ecs.toolSystem.toolManager.rotateTool.enable();
                }
            });

                selectButton.addListener(new

            ChangeListener() {
                @Override
                public void changed (ChangeEvent event, Actor actor){
                    PickingSystem.selectModeEnabled = !PickingSystem.selectModeEnabled;

                    if (PickingSystem.selectModeEnabled) {


                    } else {

                        Editor.i().ecs.pickingSystem.setPickedObject(null);
                    }
                    System.out.println("select mode enabled: " + PickingSystem.selectModeEnabled);

                }
            });
            toolSidebar =new

            VisTable();
//        selectButton.setFocusBorderEnabled(true);
//        buttonGroup.addActor(physicsButton);
                buttonGroup.addActor(selectButton);
                buttonGroup.addActor(translateButton);
                buttonGroup.addActor(scaleButton);
                buttonGroup.addActor(rotateButton);


            add(buttonGroup).width(15).

            top().

            pad(5).

            row();


//        add(translateButton).top().pad(5).row();
//        add(scaleButton).top().pad(5).row();
////        add(physicsButton).top().pad(5).row();
//        add(rotateButton).top().pad(5).row();

            setWidth(30);

            setHeight(110);

            setMovable(false);

            float y = Gdx.graphics.getHeight() - 125;

            setPosition(25,y, Align.top);


        }
    @Override
    public void act(float delta) {
        super.act(delta);

        setPosition(Editor.i().viewportWidget.origin.x+10,Gdx.graphics.getHeight()-144);
    }
}


