package ui;

import backend.WindowWorker;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;
import app.Editor;
import scene.graph.GameObject;
import scene.graph.GameObjectFactory;
import ui.elements.TitleMenuBar;
import ui.widgets.*;
import ui.widgets.cli.Console;

public class EditorUI {

    private static final float RESIZE_BORDER = 10;

    Stage stage;
    ViewportWidget viewportWidget;
    public TransformWidget transformWidget;
    ScreenViewport viewport;
    public VisWindow window;
    public VisTable root;
    public VisSplitPane leftRight;
    public VisSplitPane topBottom;
    public VisSplitPane fullSplit;
    public VisSplitPane rightSplit;
    public VisTable leftTable, rightTable, topTable, bottomTable;
    public VisWindow rightWindow, bottomWindow;
    public SceneTree sceneTree;
    public VisScrollPane sceneTreeScrollPane;
    public VisWindow fullWindow;
    public TitleMenuBar titleMenuBar =new TitleMenuBar();
    public FooterMenuBar menuBar;
    VisWindow titleWindow;
    VisWindow footerWindow;

    public ConsoleWidget consoleWidget;
    public ProfilerWidget profilerWidget;
    public ProjectWidget projectWidget;
    public GameObjectInspector inspectorWidget;
    public Console console;

    public DragAndDrop dragAndDrop = new DragAndDrop();
    public VisImage dragImage = new VisImage(VisUI.getSkin().getDrawable("icon-file-image"));


    Toolbar toolbar;
    VisTable menuTable;
    private int dragStartX=0;
    private int dragStartY=0;
    private int windowStartX, windowStartY;
    ImageButton visibleButton;


    public EditorUI(Stage stage,ViewportWidget widget){
        this.stage = stage;
        viewportWidget = widget;
        fullWindow = new VisWindow("default.scene - salient");

        fullWindow.setMovable(true);
        fullWindow.setResizable(true);
        root = new VisTable();
        root.setFillParent(true);
        fullWindow.setFillParent(true);

        left = new VisTable();
        middle = new VisTable();
        right = new VisTable();

        sceneTree = new SceneTree(Editor.i().sceneContext);
        consoleWidget = new ConsoleWidget();
        profilerWidget = new ProfilerWidget(Editor.i().profiler);
        projectWidget = new ProjectWidget();
        inspectorWidget = new GameObjectInspector();
        console = new Console();


        createTitleMenuBar();
        createSplitPanes();
        populateBottomTable();
        populateMiddleTable();
        populateLeftTable();
        populateRightTable();
        populateTopTable();

        createFooterMenuBar();


        root.add(titleWindow).expandX().fillX().row();
        root.addSeparator();
//        root.add(leftRight).expand().fill().row();
        root.add(multiSplitPane).expand().fill().row();
        root.add(menuTable).expandX().fillX().row();
        stage.addActor(root);

        createDragAndDrop();

    }

    VisTable left,middle,right,middleBottom;
    MultiSplitPane multiSplitPane;
    VisTable bottom;
    public VisSplitPane bottomSplit;
    public VisSplitPane middleSplit;
    private void createSplitPanes() {



        multiSplitPane = new MultiSplitPane(false);
        multiSplitPane.setWidgets(left,middle,right);
        multiSplitPane.setSplit(0,0.125f);
        multiSplitPane.setSplit(1,0.8f);
    }

    private void populateBottomTable() {

        bottomSplit = new VisSplitPane(profilerWidget,console,false);
        bottomSplit.setSplitAmount(.3f);
        middleBottom = new VisTable();
        middleBottom.add(bottomSplit).expand().fill();
    }

    private void populateTopTable() {
    }

    private void populateRightTable() {

        sceneTreeScrollPane = new VisScrollPane(sceneTree);
        sceneTreeScrollPane.setScrollingDisabled(true,false);
        sceneTreeScrollPane.setFadeScrollBars(false);
        sceneTreeScrollPane.setFlickScroll(false);
        sceneTreeScrollPane.setOverscroll(false,false);
        VisSplitPane rightSplit = new VisSplitPane(sceneTreeScrollPane,inspectorWidget,true);
rightSplit.setSplitAmount(.3f);

        right.add(rightSplit).expand().fill().row();
//        right.addSeparator();
//        right.add(inspectorWidget).expand().fill().row();
//        right.add(projectWidget);
    }

    private void populateLeftTable() {
        left.add(projectWidget).expand().fill();

//        VisSplitPane leftSplit = new VisSplitPane(projectWidget,sceneTreeScrollPane,true);

    }

    private void populateMiddleTable() {


        middleSplit = new VisSplitPane(viewportWidget,middleBottom,true);
        middleSplit.setSplitAmount(.8f);
//        middle.add(viewportWidget).expand().fill();
//        middle.add(bottomSplit).expandX().fillX();
        middle.add(middleSplit).expand().fill();

//        toolbar = new Toolbar();
//        stage.addActor(toolbar);
//        toolbar.setPosition(viewportWidget.origin.x+30, viewportWidget.origin.y+viewportWidget.getHeight()-30, Align.topLeft);

        Drawable drawable = Editor.i().skin.getDrawable("icon-visible-on");
        Drawable drawable2 = Editor.i().skin.getDrawable("icon-visible-off");

        visibleButton = new ImageButton(drawable,drawable,drawable2) {

            @Override
            public void act(float delta) {
                super.act(delta);
//                setPosition(viewportWidget.getX()-20, viewportWidget.getY()+viewportWidget.getHeight(), Align.topLeft);
            }
        };
        stage.addActor(visibleButton);
        visibleButton.setPosition(viewportWidget.getX()-40, viewportWidget.getY()+viewportWidget.getHeight());
    }



    private void createDragAndDrop() {
        dragAndDrop.addTarget(new DragAndDrop.Target(viewportWidget) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                payload.getDragActor().setPosition(x,y);
                return true;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                payload.getDragActor().remove();
                FileHandle file = (FileHandle) payload.getObject();
                GameObject go = GameObjectFactory.createModelObjectFromPath(file,Editor.i().sceneContext.sceneGraph);
                sceneTree.addGameObject(go,sceneTree.root);
                System.out.println(payload.getObject().toString());
            }
        });
    }



    private void createTitleMenuBar() {


        VisTable menuTable = new VisTable();
        menuTable.add(titleMenuBar.getTable()).expandX().fillX();
        titleWindow = new VisWindow("default.scene - salient","menu-bar");
        titleWindow.getTitleTable().clear();
        titleWindow.add(menuTable).height(24).expandX().fillX();
        titleWindow.setMovable(true);
        titleWindow.setResizable(true);

    }

    private void createFooterMenuBar() {
        menuBar = new FooterMenuBar();
        footerWindow = new VisWindow(" ","menu-bar");
        menuTable = new VisTable();

        footerWindow.getTitleTable().clear();
        footerWindow.add(menuTable).height(24).expandX().fillX();
        footerWindow.setMovable(true);
        footerWindow.setResizable(true);

        menuTable.add(menuBar.getTable()).expandX().fillX();

    }






//    private void populateBottomTable() {
//
////        VisSplitPane bottomSplit;
////        VisTable bottomLeftTable = new VisTable();
////        bottomLeftTable.align(Align.topLeft);
////        ProfilerWidget profilerWidget = new ProfilerWidget(Editor.i().profiler);
////        bottomLeftTable.add(profilerWidget).expand().fill().top();
////
//////        TabPane tabPane = new TabPane(Editor.i().skin,"toggle");
////        consoleWidget = new ConsoleWidget();
//////        Table consoleTable = new Table();
//////        consoleTable.add(consoleWidget).expand().fill();
//////        tabPane.addPane("Console",consoleTable);
//////        tabPane.setCurrentIndex(0);
////
////        bottomSplit = new VisSplitPane(bottomLeftTable,consoleWidget,false);
////        bottomSplit.setSplitAmount(0.5f);
////        bottomTable.add(bottomSplit).expand().fill().row();
//
//
//
//    }
//
//    private void populateTopTable() {
//        VisSplitPane topSplit;
//        projectWidget = new ProjectWidget();
//        VisTable viewportTable = new VisTable();
//        viewportTable.add(viewportWidget).expand().fill();
//
//        VisSplitPane viewportSplit;
//        ProfilerWidget profilerWidget = new ProfilerWidget(Editor.i().profiler);
//        ConsoleWidget consoleWidget = new ConsoleWidget();
//        VisSplitPane profilerConsoleSplit = new VisSplitPane(profilerWidget,consoleWidget,false);
//        viewportSplit = new VisSplitPane(viewportWidget,profilerConsoleSplit,true);
//        viewportSplit.setSplitAmount(0.8f);
//        VisTable topRightTable = new VisTable();
//        VisTable topLeftTable = new VisTable();
//        topLeftTable.align(Align.topLeft);
//        topLeftTable.add(projectWidget).fill().expand().width(200).row();
//        topSplit = new VisSplitPane(topLeftTable,viewportSplit,false);
//        topSplit.setSplitAmount(0.15f);
//        topTable.add(topSplit).expand().fill();
//

//    }
//
//    private void populateRightTable() {
//        sceneTree = new SceneTree(Editor.i().sceneContext);
//        sceneTreeScrollPane = new VisScrollPane(sceneTree);
//        sceneTreeScrollPane.setScrollingDisabled(true, false);
//        transformWidget = new TransformWidget();
//        Table sceneTreeTable = new Table();
//        sceneTreeTable.add(sceneTreeScrollPane).expand().fill();
////        sceneTreeTable.debugAll();
//
//        rightSplit = new VisSplitPane(sceneTreeTable, transformWidget, true);
//        rightSplit.setSplitAmount(0.3f);
//        rightTable.add(rightSplit).expand().fill().row();
//        rightTable.setSkin(Editor.i().skin);
//    }
//
//    private void populateLeftTable() {
//    }
//
//    private void createSplitPanes() {
//        leftTable = new VisTable();
//        rightTable = new VisTable();
//        topTable = new VisTable();
//        bottomTable = new VisTable();
//
//        topBottom = new VisSplitPane(topTable, bottomTable, true);
//        topBottom.setSplitAmount(1f);
//        leftTable.add(topBottom).expand().fill().row();
//        leftRight = new VisSplitPane(leftTable, rightTable, false);
//        leftRight.setSplitAmount(0.85f);
//
//        VisTable left = new VisTable();
//        VisTable middle = new VisTable();
//        VisTable right = new VisTable();
//
//
//
//    }


    public void render(float delta){

        if(titleMenuBar.animatedSprite!=null&& titleMenuBar !=null){
            titleMenuBar.animatedSprite.play();
            titleMenuBar.animatedSprite.update(delta);}

        Editor.i().stage.draw();
        Editor.i().stage.act(delta);

        viewportWidget.viewportStage.draw();
        viewportWidget.viewportStage.act(delta);

    }
    private static enum DragState {
        NONE, TOP, BOTTOM, LEFT, RIGHT
    }
    DragState dragState;

    private void addListeners(final VisTable root) {
        WindowWorker windowWorker = Editor.i().nativeInterface;
        DragListener dragListener = new DragListener() {
            private int startX, startY;
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                int translateX = (int) x - startX;
                int translateY = startY - (int) y;

                windowWorker.setPosition(windowWorker.getPositionX() + translateX, windowWorker.getPositionY() + translateY);
            }

            @Override
            public void dragStart(InputEvent event, float x, float y,
                                  int pointer) {
                startX = (int) x;
                startY = (int) y;
            }
        };
        dragListener.setTapSquareSize(0.0f);
//        root.addCaptureListener(dragListener);

        root.addCaptureListener(new InputListener() {
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                if (x < RESIZE_BORDER) {
                    dragState = DragState.LEFT;
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.HorizontalResize);
                } else if (x > root.getWidth() - RESIZE_BORDER) {
                    dragState = DragState.RIGHT;
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.HorizontalResize);
                } else if (y < RESIZE_BORDER+24) {
                    dragState = DragState.BOTTOM;
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.VerticalResize);
                } else if (y > root.getHeight() - RESIZE_BORDER) {
                    dragState = DragState.TOP;
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.VerticalResize);
                } else if (dragState != DragState.NONE) {
                    dragState = DragState.NONE;
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                }

                return false;
            }

        });

        dragListener = new DragListener() {
            private int resizeX, resizeY;
            private int dragX, dragY;

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {

            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                if (dragState == DragState.RIGHT) {
                    int translateX = (int) x - resizeX;

                    Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth() + translateX, Gdx.graphics.getHeight());
                    resizeX = (int) x;
                    System.out.println(Gdx.graphics.getWidth() + translateX+" " + Gdx.graphics.getHeight());
                } else if (dragState == DragState.BOTTOM) {
                    int oldHeight = Gdx.graphics.getHeight();
                    int translateY = resizeY - (int) y;

                    Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() + translateY);
                    System.out.println(Gdx.graphics.getWidth()+" " + Gdx.graphics.getHeight() + translateY);
                    resizeY = (int) y - oldHeight + Gdx.graphics.getHeight();
                } else if (dragState == DragState.LEFT) {
                    int translateX = (int) x - resizeX;

                    Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth() - translateX, Gdx.graphics.getHeight());
                    windowWorker.setPosition(windowWorker.getPositionX() - translateX + (int) x, windowWorker.getPositionY());
                    System.out.println(Gdx.graphics.getWidth() - translateX+" " + Gdx.graphics.getHeight());
                } else if (dragState == DragState.TOP) {
                    int amount = resizeY - (int) y;

                    resizeY = (int) y;

                    windowWorker.setPosition(windowWorker.getPositionX(), windowWorker.getPositionY());
                    Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - amount);

                    System.out.println(Gdx.graphics.getWidth()+" " + (Gdx.graphics.getHeight()-amount));
                }
            }

            @Override
            public void dragStart(InputEvent event, float x, float y,
                                  int pointer) {
                resizeX = (int) x;
                resizeY = (int) y;
                dragX = resizeX;
                dragY = resizeY;
            }
        };
        dragListener.setTapSquareSize(0.0f);
        root.addCaptureListener(dragListener);
    }

}
