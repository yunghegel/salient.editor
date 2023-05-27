package ui.widgets;

import app.App;
import app.Editor;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import net.dermetfan.gdx.scenes.scene2d.ui.FileChooser;
import net.dermetfan.gdx.scenes.scene2d.ui.ListFileChooser;
import net.dermetfan.gdx.scenes.scene2d.ui.TreeFileChooser;
import ui.EditorUI;

import java.io.File;
import java.io.FileFilter;

public class ProjectWidget extends VisTable {

    private ListFileChooser listFileChooser;
    public TreeFileChooser treeFileChooser;
    Texture modelIcon = new Texture("button_icons/geometry_icon.png");
    Texture imageIcon = new Texture("button_icons/image_icon.png");
    TextButton addAssetButton = new TextButton("Add Assets", VisUI.getSkin());

    public ProjectWidget() {
        super();
        listFileChooser = new ListFileChooser(VisUI.getSkin(),new ProjectFileChosenListener()){

        };
        listFileChooser.setDirectory(Gdx.files.getFileHandle(App.USER_HOME_PATH, Files.FileType.Absolute));
        VisImageButton parent = new VisImageButton(VisUI.getSkin().getDrawable("arrow-over"));
        listFileChooser.setParentButton(parent);

        listFileChooser.getCell(parent).size(20,20);

        FileFilter gltf = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".gltf")||pathname.getName().endsWith(".png")|| pathname.isDirectory();
            }
        };


        TextureRegionDrawable modelIconDrawable = new TextureRegionDrawable(new TextureRegion(modelIcon));
        TextureRegionDrawable imageIconDrawable = new TextureRegionDrawable(new TextureRegion(imageIcon));

        treeFileChooser = new TreeFileChooser(VisUI.getSkin(),new ProjectFileChosenListener());

        treeFileChooser.setFileFilter(gltf);
        treeFileChooser.pad(10);
        treeFileChooser.getTree().setIndentSpacing(5);
        treeFileChooser.getCell(treeFileChooser.getTreePane()).expand().fill().padBottom(14);
        treeFileChooser.getCell(treeFileChooser.getChooseButton()).size(45,20);
        treeFileChooser.getCell(treeFileChooser.getCancelButton()).size(45,20);
        treeFileChooser.align(Align.topLeft);
//        treeFileChooser.setFillParent(true);
        add(new VisLabel("Project")).pad(5,2,5,5).expandX().fillX();
        add(addAssetButton).pad(5,5,5,2).size(100,20).align(Align.right).size(70,20).row();

        Tree.Node assets = treeFileChooser.add(Gdx.files.getFileHandle(App.DEFAULT_PROJECT_PATH+"assets", Files.FileType.Absolute));//        root.setIcon(VisUI.getSkin().getDrawable("icon-folder"));
        assets.setExpanded(true);
        Array<Tree.Node> nodes = assets.getChildren();

        add(treeFileChooser).fill().expand().grow().width(200).align(Align.topLeft).colspan(2);
        nodes.first().setIcon(modelIconDrawable);
        nodes.get(1).setIcon(imageIconDrawable);

        addSeparator(true);

        align(Align.topLeft);

        treeFileChooser.getTreePane().setScrollingDisabled(true,false);

        treeFileChooser.getTree().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(treeFileChooser.getTree().getSelectedNode()!=null){
                    Editor.i().editorUI.dragAndDrop.addSource(new DragAndDrop.Source(treeFileChooser.getTree().getSelectedNode().getActor()) {
                        @Override
                        public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                            DragAndDrop.Payload payload = new DragAndDrop.Payload();
                            payload.setDragActor(Editor.i().editorUI.dragImage);
                            payload.setObject(treeFileChooser.getTree().getSelectedNode().getValue());
                            Editor.i().stage.addActor(Editor.i().editorUI.dragImage);
                            return payload;
                        }
                    });
                }
            }
        });
    }


    public static class ProjectFileChosenListener implements FileChooser.Listener {

        @Override
        public void choose(FileHandle file) {

        }

        @Override
        public void choose(Array<FileHandle> files) {

        }

        @Override
        public void cancel() {

        }
    }


}
