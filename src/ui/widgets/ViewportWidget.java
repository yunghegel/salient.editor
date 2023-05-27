package ui.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import app.Editor;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import utils.MiscUtils;

public class ViewportWidget extends Widget {
    public ScreenViewport viewport;
    private final static Vector2 temp = new Vector2();
    public Vector2 origin = new Vector2();
    public int width;
    public int height;
    Toolbar toolbar;
    public Stage viewportStage;
    ImageButton visibleButton;
//    CheckBox bottomPanelEnabled = new CheckBox("Bottom Panel", VisUI.getSkin());
    public ViewportWidget(ScreenViewport viewport) {
        this.viewport = viewport;
        toolbar = new Toolbar();
        viewportStage = new Stage();
        Editor.i().inputManager.addInputProcessor(viewportStage);

        viewportStage.addActor(toolbar);

        addListener(new InputListener(){

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                getStage().setKeyboardFocus(ViewportWidget.this);
                getStage().setScrollFocus(ViewportWidget.this);

                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
            }
        });
        Drawable drawable = Editor.i().skin.getDrawable("icon-visible-on");
        Drawable drawable2 = Editor.i().skin.getDrawable("icon-visible-off");

        visibleButton = new ImageButton(drawable,drawable,drawable2);
        visibleButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                if (visibleButton.isChecked()){
                    Editor.i().editorUI.middleSplit.setSplitAmount(1f);
                } else {
                    Editor.i().editorUI.middleSplit.setSplitAmount(.8f);
                }

            }
        });
        viewportStage.addActor(visibleButton);


//        viewportStage.addActor(bottomPanelEnabled);

        inputMultiplexer.addProcessor(viewportStage);
        inputMultiplexer.addProcessor(Editor.i().sceneContext.cameraController);
//        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    InputMultiplexer inputMultiplexer = new InputMultiplexer();


    @Override
    public void act(float delta) {
//        toolbar.setPosition(origin.x+15, origin.y+height-150);
        temp.set(0, 0);
        localToScreenCoordinates(temp);
        if (viewport == null) return;

        viewport.setScreenPosition(viewportOriginalX + MathUtils.round(temp.x), viewportOriginalY + MathUtils.round(Gdx.graphics.getHeight() - temp.y));

//        if(MiscUtils.isInsideWidget( Gdx.input.getX(), Gdx.input.getY(), this)||Gdx.input.getY()<27) {
//            Editor.i().inputManager.pauseInput(inputMultiplexer);
//        } else {
//            Editor.i().inputManager.resumeInput();
//        }

        visibleButton.setPosition(origin.x+width-visibleButton.getWidth()-5, (Gdx.graphics.getHeight()-height-25));
    }

    int viewportOriginalX, viewportOriginalY;
    @Override
    public void layout() {
        temp.set(0, 0);
        localToScreenCoordinates(temp);
        if (viewport == null) return;
        viewport.update(MathUtils.round(getWidth()), MathUtils.round(getHeight()));
        viewportOriginalX = viewport.getScreenX();
        viewportOriginalY = viewport.getScreenY();


    }

    @Override
    public void draw(Batch batch , float parentAlpha) {

        batch.end();

        if (viewport != null) {
            calcWidgetBounds();
            viewport.setScreenBounds((int) origin.x , (int) origin.y , width , height);
            viewport.setWorldSize(width * viewport.getUnitsPerPixel() , height * viewport.getUnitsPerPixel());
            viewport.apply();
        }


//        Editor.i().sceneContext.cam.viewportHeight = height;
//        Editor.i().sceneContext.cam.viewportWidth = width;
        Editor.i().sceneContext.sceneRenderer.render(Gdx.graphics.getDeltaTime());
        Editor.i().stage.getViewport().apply();



        batch.begin();
    }

    private void calcWidgetBounds(){
        origin.set(getOriginX() , getOriginY());
        origin = localToStageCoordinates(origin);
        width = (int) getWidth();
        height = (int) getHeight();
    }
}
