package input;

import backend.tools.Log;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

public class InputManager {

    public InputMultiplexer inputMultiplexer;
    public SalientCamera salientCamera;
    public Array<InputProcessor> processors = new Array<>();
    public boolean paused = false;

    public InputManager(){
        inputMultiplexer = new InputMultiplexer();
        setAsInputProcessor();
    }

    /**
     * @param inputProcessor the processor we wish to accept input from. can be null
     */
    public void pauseInput(InputProcessor inputProcessor){
        paused = true;


        if (inputProcessor != null) {
            Gdx.input.setInputProcessor(inputProcessor);
        }
//        Log.info("paused input, storing " + processors.size + " processors and setting input to " + inputProcessor.getClass().getSimpleName());
    }

    public void resumeInput(){
        paused = false;

        for (InputProcessor input: processors)
        {
            if(!hasInputProcessor(input)){
                inputMultiplexer.addProcessor(input);
                Log.info("resumed input, removing " + processors.size + " processors and setting input to inputMultiplexer with processors " + inputMultiplexer.getProcessors().toString());
            }
        }
        if(Gdx.input.getInputProcessor()==inputMultiplexer){
            return;
        }
        setAsInputProcessor();
    }

    public boolean hasInputProcessor(InputProcessor inputProcessor){
        return inputMultiplexer.getProcessors().contains(inputProcessor, true);
    }

    public void addInputProcessor(InputProcessor inputProcessor){
        if(hasInputProcessor(inputProcessor)){
            return;
        }
        inputMultiplexer.addProcessor(inputProcessor);
        processors.add(inputProcessor);
    }

    public void removeInputProcessor(InputProcessor inputProcessor){
        inputMultiplexer.removeProcessor(inputProcessor);
        processors.removeValue(inputProcessor, true);
    }

    public void clearInputProcessors(){
        inputMultiplexer.clear();
    }

    public void setAsInputProcessor(){
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void setCameraController(SalientCamera cameraController){
        this.salientCamera = cameraController;
    }

}
