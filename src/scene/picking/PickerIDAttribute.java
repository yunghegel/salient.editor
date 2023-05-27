package scene.picking;

import com.badlogic.gdx.graphics.g3d.Attribute;

/**
 * @author Marcus Brummer
 * @version 20-02-2016
 */
public class PickerIDAttribute extends Attribute {

    public final static String Alias = "goID";
    public final static long Type = register(Alias);

    public int r = 255;
    public int g = 255;
    public int b = 255;

    public final static boolean is(final long mask) {
        return (mask & Type) == mask;
    }

    public PickerIDAttribute() {
        super(Type);
    }

    public PickerIDAttribute(PickerIDAttribute other) {
        super(Type);
    }

    @Override
    public PickerIDAttribute copy() {
        return new PickerIDAttribute(this);
    }

    @Override
    public int hashCode() {
        return r + g * 255 + b * 255 * 255;
    }

    @Override
    public int compareTo(Attribute o) {
        return -1; // FIXME implement comparing
    }

    @Override
    public String toString() {
        return "GameObjectIdAttribute{" + "r=" + r + ", g=" + g + ", b=" + b + '}';
    }
}