package backend.asset;


import java.util.ArrayList;
import java.util.List;

public class ValidFileDrop {

    List<String> gltf= new ArrayList<>();
    List<String> obj= new ArrayList<>();
    List<String> png= new ArrayList<>();
    FileDrop fileDrop;

    public ValidFileDrop(FileDrop fileDrop) {
        this.fileDrop = fileDrop;
    }

    public void sortFiles(String[] strings) {
        for (String string : strings) {
            if (string.endsWith(".gltf")) {
                gltf.add(string);
            } else if (string.endsWith(".obj")) {
                obj.add(string);
            } else if (string.endsWith(".png")) {
                png.add(string);
            } else {
                System.out.println("File type not supported: " + string);
            }
        }

    }

}
