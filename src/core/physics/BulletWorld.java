package core.physics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class BulletWorld implements Disposable {

    public static final Vector3 DEFAULT_GRAVITY = new Vector3(0, -9.8f, 0f);

    /**
     * Stores all btCollisionObjects and provides an interface to
     * perform queries.
     */
    private final btDynamicsWorld dynamicsWorld;

    /**
     * Allows to configure Bullet collision detection
     * stack allocator size, default collision algorithms and persistent manifold pool size
     */
    private final btCollisionConfiguration collisionConfig;

    /**
     * A collision dispatcher iterates over each pair, searches for a matching collision algorithm based on the
     * types of objects involved and executes the collision algorithm computing contact points.
     */
    private final btDispatcher dispatcher;

    /**
     * Broadphase collision detection provides acceleration structure to quickly reject pairs of objects
     * based on axis aligned bounding box (AABB) overlap.
     */
    private final btBroadphaseInterface broadphase;

    private final btConstraintSolver constraintSolver;

    private final DebugDrawer debugDrawer;

    private final float fixedTimeStep = 1 / 60f;

    // Debug drawing ray casts
    private final Vector3 lastRayFrom = new Vector3();
    private final Vector3 lastRayTo = new Vector3();
    private final Vector3 rayColor = new Vector3(1, 0, 1);

    public Array<btRigidBody> bodies = new Array<>();

    public BulletWorld() {
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);

        // General purpose, well optimized broadphase, adapts dynamically to the dimensions of the world.
        broadphase = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);

        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe);

        dynamicsWorld.setDebugDrawer(debugDrawer);
        dynamicsWorld.setGravity(DEFAULT_GRAVITY);
    }

    /**
     * Update core.physics world, should be called every frame
     *
     * @param delta deltaTime since last frame
     */
    public void update(float delta) {
        // performs collision detection and core.physics simulation
        dynamicsWorld.stepSimulation(delta, 5, fixedTimeStep);
    }

    /**
     * Debug draw the core.physics world
     *
     * @param camera camera to render to
     */
    public void render(Camera camera) {
        debugDrawer.begin(camera);
        debugDrawer.drawLine(lastRayFrom, lastRayTo, rayColor);
        dynamicsWorld.debugDrawWorld();
        debugDrawer.end();
    }

    /**
     * Add a rigid body to the core.physics world
     *
     * @param body the body to add
     */
    public void addBody(btRigidBody body) {
        dynamicsWorld.addRigidBody(body);
        bodies.add(body);
    }

    /**
     * Perform a raycast in the core.physics world.
     *
     * @param from     the starting position (origin) of the ray
     * @param to       the end position of the ray
     * @param callback the callback object to use
     */
    public void raycast(Vector3 from, Vector3 to, RayResultCallback callback) {
        lastRayFrom.set(from).sub(0, 5f, 0f);

        dynamicsWorld.rayTest(from, to, callback);

        if (callback.hasHit() && callback instanceof ClosestRayResultCallback) {
            // Use interpolation to determine the hitpoint where the ray hit the object
            // This is what bullet does behind the scenes as well
            lastRayTo.set(from);
            lastRayTo.lerp(to, callback.getClosestHitFraction());
        } else {
            lastRayTo.set(to);
        }
    }

    @Override
    public void dispose() {
        collisionConfig.dispose();
        dispatcher.dispose();
        broadphase.dispose();
        constraintSolver.dispose();
        dynamicsWorld.dispose();
    }
}
