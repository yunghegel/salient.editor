package shaders;

public class Shaders {

    private static Shaders instance;

    private PickerShader pickerShader;
    private WireframeShader wireframeShader;

    private Shaders() {
        pickerShader = new PickerShader();
        pickerShader.init();
        wireframeShader = new WireframeShader();
        wireframeShader.init();
    }



    public static Shaders i() {
        if (instance == null) {
            instance = new Shaders();
        }
        return instance;
    }

    public PickerShader getPickerShader() {
        return pickerShader;
    }

    public WireframeShader getWireframeShader() {
        return wireframeShader;
    }
}
