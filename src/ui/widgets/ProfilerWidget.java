package ui.widgets;

import app.Editor;
import backend.Profiler;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisTable;
import ui.elements.TabPane;


public class ProfilerWidget extends VisTable {

    Profiler profiler;

    public ProfilerWidget(Profiler profiler){
        this.profiler = profiler;
        setSkin(Editor.i().skin);
        align(Align.topLeft);
        Separator separator = new Separator();
        pad(15);
        TabPane tabPane = new TabPane(getSkin(), "default");
        tabPane.addPane("OpenGL", new GLTable());
        tabPane.addPane("Java", new JavaTable());
        tabPane.addPane("GPU", new GPUTable());
        tabPane.pad(3);
        add(tabPane).row();


    }

    @Override
    public void act(float delta) {
        super.act(delta);

    }

    static class GLTable extends Table{

        public GLTable(){
            setSkin(Editor.i().skin);
            align(Align.topLeft);
            pad(8);
        }
        @Override
        public void act(float delta) {
            super.act(delta);
            clearChildren();

            add("Draw calls: " + Profiler.getDrawCalls()).left().row();
            add("Render calls: " + Profiler.getRenderCalls()).left().row();
            add("Shader switches: " + Profiler.getShaderSwitches()).left().row();
            add("Texture bindings: " + Profiler.getTextureBindings()).left().row();
            add("Vertex count: " + Profiler.getVertexCount()).left().row();
        }
    }

    static class JavaTable extends Table {

        public JavaTable(){
            setSkin(Editor.i().skin);
            align(Align.topLeft);
            pad(8);
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            clearChildren();

            add(Profiler.VERSION).row();
            add("Free memory: " + Profiler.getJavaAvailableMemoryGB()).left().row();
            add("Total memory: " + Profiler.getJavaUsedMemoryGB()).left().row();
            add("Max memory: " + Profiler.getJavaMaxMemoryGB()).left().row();
        }
    }

    static class GPUTable extends Table {

        public GPUTable(){
            setSkin(Editor.i().skin);
            align(Align.topLeft);
            pad(8);
        }


        @Override
        public void act(float delta) {
            super.act(delta);
            clearChildren();

            add("Available memory: " + Profiler.getGLAvailableMemoryKB()).left().row();
            add("Used memory: " + Profiler.getGLUsedMemoryGB()).left().row();
            add("Max memory: " + Profiler.getGLMaxMemoryGB()).left().row();
        }

    }


}
