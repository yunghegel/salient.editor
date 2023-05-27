package tests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.ScreenUtils;

public class ShaderTest extends BaseScreen {



    public ShaderTest(){

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        modelBatch.begin(camera);
        modelBatch.render(renderables,environment);
        modelBatch.end();
    }
}
