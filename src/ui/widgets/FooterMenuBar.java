package ui.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.*;
import app.Editor;
import scene.SceneRenderer;
import utils.StringUtils;

public class FooterMenuBar extends MenuBar {
    VisTable menuBarButtons;
    Table itemsTable;
    private VisLabel camPos;
    Table camPosTable;
    private VisSelectBox<SceneRenderer.AxesStep> gridSize;
    Table gridSizeTable;
    private VisLabel mousePos;
    Table mousePosTable;
        public FooterMenuBar() {
            super("footer");
            getTable().setSkin(Editor.i().skin);
            getTable().setColor(0.5f, 0.5f, 0.5f, 1f);
            getTable().setColor(0.4f, 0.4f, 0.4f, 1f);
            menuBarButtons = new VisTable();
            menuBarButtons.align(Align.right);
            getTable().add(menuBarButtons).expandX().fillX().setActorHeight(5);
            itemsTable = (Table) getTable().getChild(0);

            VisLabel fps = new VisLabel("FPS: ") {
                @Override
                public void act(float delta) {
                    super.act(delta);
                    setText("FPS: " + Gdx.graphics.getFramesPerSecond());
                }
            };
            itemsTable.add(fps).pad(3,6,3,6).minWidth(30);


            camPos = new VisLabel("xyz: ");
            Separator separator = new Separator();
            separator.getStyle().thickness = 1;
            getTable().add(separator).growY().pad(2,0,4,0);
            camPosTable = new Table() {
                @Override
                public void act(float delta) {
                    super.act(delta);
                    String x = StringUtils.trimFloat(Editor.i().sceneContext.cam.position.x);
                    String y = StringUtils.trimFloat(Editor.i().sceneContext.cam.position.y);
                    String z = StringUtils.trimFloat(Editor.i().sceneContext.cam.position.z);
                    camPos.setText("xyz: (" + x + ", " + y + ", " + z+ ")");
                }
            };
            camPosTable.add(camPos);
            getTable().add(camPosTable).pad(2,6,2,6).minWidth(135);
            separator = new Separator();
            getTable().add(separator).growY().pad(2,0,2,0);
            mousePos = new VisLabel("mousePos: ");
            mousePosTable = new Table() {
                @Override
                public void act(float delta) {
                    super.act(delta);
                    int x = Gdx.input.getX();
                    int y =(Gdx.graphics.getHeight()- Gdx.input.getY());


                    mousePos.setText("mousePos: (" + x + ", " + y + ")");
                }
            };
            mousePosTable.add(mousePos);
            getTable().add(mousePosTable).pad(2,6,2,6).minWidth(135);
            separator = new Separator();
            getTable().add(separator).growY().pad(2,0,2,0);


            //GRID CONTROLS

            gridSize = new VisSelectBox<SceneRenderer.AxesStep>();
            gridSize.setItems(SceneRenderer.AxesStep.values());
            gridSize.setSelected(SceneRenderer.AxesStep.ONE);

            gridSize.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    Editor.i().sceneContext.sceneRenderer.axesStep = gridSize.getSelected();
                }
            });
            gridSizeTable = new Table();

            gridSizeTable.add(new VisLabel("Grid: ")).pad(0,3,0,3);
            gridSizeTable.add(gridSize);

            getTable().add(gridSizeTable).pad(2,6,2,6);

        }
}
