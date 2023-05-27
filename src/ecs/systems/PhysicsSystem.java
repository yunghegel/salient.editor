package ecs.systems;

import app.Editor;
import backend.tools.Log;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.ConeShapeBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.CylinderShapeBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.SphereShapeBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.github.ykrasik.jaci.api.Command;
import com.github.ykrasik.jaci.api.CommandPath;
import core.physics.BulletWorld;
import core.physics.MotionState;
import ecs.components.PhysicsComponent;
import ecs.components.PickableModelComponent;
import ecs.components.TransformComponent;
import scene.graph.GameObject;
import utils.Suppliers;

import static utils.MiscUtils.getRandomColor;
@CommandPath("physics")
public class PhysicsSystem extends IteratingSystem {

     static BulletWorld bulletWorld;

    private static final Vector3 rayFromWorld = new Vector3();
    private static final Vector3 rayToWorld   = new Vector3();
    private final ClosestRayResultCallback callback = new ClosestRayResultCallback(new Vector3(), new Vector3());

    public boolean enablePhysics = false;


    public PhysicsSystem() {
        super(Family.one(PhysicsComponent.class).get());

        bulletWorld = new BulletWorld();

//        createFloor(100f, 1f, 100f);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(!enablePhysics){
            return;
        }
        bulletWorld.update(deltaTime);

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            // Reset the callback
            callback.setClosestHitFraction(1.0f);
            callback.setCollisionObject(null);

            // Get a pick ray of the current mouse coordinates
            Ray ray = Editor.i().viewportWidget.viewport.getPickRay(Gdx.input.getX(), Gdx.input.getY());

            rayFromWorld.set(ray.origin);
            rayToWorld.set(ray.direction).scl(100f).add(ray.origin);

            bulletWorld.raycast(rayFromWorld, rayToWorld, callback);

            if (callback.hasHit()) {
                btCollisionObject collisionObject = callback.getCollisionObject();
                if (collisionObject instanceof btRigidBody) {
                    // Activate and push the object in the direction of the ray
                    collisionObject.activate();
                    ((btRigidBody) collisionObject).applyCentralImpulse(ray.direction.scl(50f));
                }
            }
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PhysicsComponent physicsComponent = entity.getComponent(PhysicsComponent.class);
        if (!physicsComponent.loaded){
            bulletWorld.addBody(physicsComponent.bulletEntity.getBody());
            physicsComponent.loaded = true;
            Log.info("PhysicsSystem","# of bodies: " + bulletWorld.bodies.size);
            Log.info("PhysicsSystem","PhysicsComponent loaded");
        }
    }

    public void debugDraw(Camera camera){
        bulletWorld.render(camera);
    }

    protected void createFloor(float width, float height, float depth) {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder meshBuilder = modelBuilder.part("floor", GL20.GL_TRIANGLES, VertexAttribute.Position().usage |VertexAttribute.Normal().usage | VertexAttribute.TexCoords(0).usage, new Material());

        BoxShapeBuilder.build(meshBuilder, width, height, depth);
        btBoxShape btBoxShape = new btBoxShape(new Vector3(width/2f, height/2f, depth/2f));
        Model floor = modelBuilder.end();

        ModelInstance floorInstance = new ModelInstance(floor);
        floorInstance.transform.trn(0, -0.5f, 0f);

        btRigidBody.btRigidBodyConstructionInfo info = new btRigidBody.btRigidBodyConstructionInfo(0, null, btBoxShape, Vector3.Zero);
        btRigidBody body = new btRigidBody(info);

        body.setWorldTransform(floorInstance.transform);
        bulletWorld.addBody(body);
    }

    @Command(description = "Create random physics objects")
    public static void createObjects() {
        // Create some random shapes
        for (int i = -6; i < 6; i+=2) {
            for (int j = -6; j < 6; j+=2) {
                ModelBuilder modelBuilder = new ModelBuilder();
                modelBuilder.begin();
                Material material = new Material();
                material.set(ColorAttribute.createDiffuse(getRandomColor()));
                MeshPartBuilder builder = modelBuilder.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material);

                btCollisionShape shape;

                int random = MathUtils.random(1, 4);
                switch (random) {
                    case 1:
                        BoxShapeBuilder.build(builder, 0, 0, 0, 1f, 1f, 1f);
                        shape = new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f));
                        break;
                    case 2:
                        ConeShapeBuilder.build(builder, 1, 1, 1, 8);
                        shape = new btConeShape(0.5f, 1f);
                        break;
                    case 3:
                        SphereShapeBuilder.build(builder, 1, 1, 1, 8, 8);
                        shape = new btSphereShape(0.5f);
                        break;
                    case 4:
                    default:
                        CylinderShapeBuilder.build(builder, 1, 1, 1, 8);
                        shape = new btCylinderShape(new Vector3(0.5f, 0.5f, 0.5f));
                        break;
                }

                ModelInstance box = new ModelInstance(modelBuilder.end());
                box.transform.setToTranslation(i, MathUtils.random(10, 20), j);
                box.transform.rotate(new Quaternion(Vector3.Z, MathUtils.random(0f, 270f)));

                float mass = 1f;

                Vector3 localInertia = new Vector3();
                shape.calculateLocalInertia(mass, localInertia);

                btRigidBody.btRigidBodyConstructionInfo info = new btRigidBody.btRigidBodyConstructionInfo(mass, null, shape, localInertia);
                btRigidBody body = new btRigidBody(info);

                MotionState motionState = new MotionState(box.transform);
                body.setMotionState(motionState);
                Editor.i().sceneContext.sceneRenderer.renderables.add(box);
                bulletWorld.addBody(body);

            }
        }
    }

}
