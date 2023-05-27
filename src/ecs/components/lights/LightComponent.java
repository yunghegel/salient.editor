package ecs.components.lights;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import app.Editor;
import core.graphics.SalientShapeRenderer;
import ecs.components.BaseComponent;
import scene.graph.GameObject;

public abstract class LightComponent extends BaseComponent {
    public static final Color DEFAULT_COLOR = new Color(1, 1, 1, 1);
    public static final Vector3 DEFAULT_POSITION = new Vector3(0, 5, 0);
    public static final float DEFAULT_INTENSITY = 10;
    public static final Vector3 DEFAULT_DIRECTION = Vector3.X.cpy();

    public SalientShapeRenderer shapeRenderer;
    public Decal decal;
    public DecalBatch decalBatch;
    public Texture texture;
    public TextureRegion textureRegion;

    public Vector3 position = new Vector3(0,0,0);
    public Color color = new Color(1,1,1,1);
    public float intensity = 1;
    protected final Vector3 tmp = new Vector3();

    public LightComponent(GameObject go) {
        super(go);
        shapeRenderer = new SalientShapeRenderer(Editor.i().sceneContext.cam);
        shapeRenderer.setProjectionMatrix(Editor.i().sceneContext.cam.combined);
        texture = new Texture("images/light_decal.png");
        textureRegion = new TextureRegion(texture);
        TextureRegion tex = Editor.i().skin.getRegion("icon-light");
        decal = Decal.newDecal(.18f, .18f, tex, true);
        decal.setColor(Color.YELLOW);
        decalBatch = new DecalBatch(new CameraGroupStrategy(Editor.i().sceneContext.cam));
    }
}
