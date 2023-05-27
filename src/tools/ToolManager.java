package tools;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.Array;
import app.Editor;

public class ToolManager {

    public RotateTool rotateTool;
    public TranslateTool translateTool;
    public ScaleTool scaleTool;

    public Array<AbstractTool> tools=new Array<>();

    public ModelBatch toolBatch;

    public ToolManager(){
        toolBatch = Editor.i().sceneContext.sceneManager.getBatch();
        rotateTool = new RotateTool(toolBatch);
        translateTool = new TranslateTool(toolBatch);
        scaleTool = new ScaleTool(toolBatch);
        tools.add(scaleTool);
        tools.add(translateTool);
        tools.add(rotateTool);
    }

    public void render(float delta){
        for (AbstractTool tool : tools) {
            tool.render(delta);
        }
    }

    public void update(float delta){
        for (AbstractTool tool : tools) {
            tool.update(delta);
        }
    }

}
