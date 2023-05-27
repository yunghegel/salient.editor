package utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author Marcus Brummer
 * @version 05-12-2015
 */
public class TextureUtils {

    public static Texture loadMipmapTexture(FileHandle fileHandle, boolean tilable) {
        Texture texture = new Texture(fileHandle, true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);

        if (tilable) {
            texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        }

        return texture;
    }

    // public static Texture load(FileHandle fileHandle) {
    // Texture texture = new Texture(fileHandle, false);
    // return texture;
    // }

}