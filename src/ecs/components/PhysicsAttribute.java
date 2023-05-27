package ecs.components;

public class PhysicsAttribute extends Attribute{
    public static final int DYNAMIC = 1;
    public static final int STATIC = 2;
    public static final int KINEMATIC = 4;
    public static final int GHOST_OBJECT = 8;
    public static final int DEBUG_DRAW_ENABLED = 16;
    public static final int DISABLE_DEACTIVATION = 32;

    protected static long Mask = DYNAMIC | STATIC | KINEMATIC | GHOST_OBJECT | DEBUG_DRAW_ENABLED | DISABLE_DEACTIVATION;

    public PhysicsAttribute(int flags) {
        super(flags);
    }

    public static PhysicsAttribute create(int flags){
        return new PhysicsAttribute(flags);
    }
}
