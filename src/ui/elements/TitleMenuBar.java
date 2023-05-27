package ui.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.*;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

public class TitleMenuBar extends MenuBar {

    Menu editMenu;
    Menu fileMenu;
    Menu viewMenu;
    Menu helpMenu;

    VisTextButton maximizeButton;
    VisTextButton exitButton;
    VisTextButton minimizeButton;

    Texture icon;
    TextureRegionDrawable iconDrawable;
    VisImage iconImage;

    VisTable menuBarButtons;

    Texture spriteSheet;
    public AnimatedSprite animatedSprite;
    Animation<TextureRegion> animation;

    public TitleMenuBar() {
        super();
        spriteSheet = new Texture(Gdx.files.internal("images/sheet.png"));
        //dimension 2080x104 with 20 images
        //104x104 per image
        Array<TextureRegion> regions = new Array<TextureRegion>();
        for(int i = 0; i < 20; i++){
            regions.add(new TextureRegion(spriteSheet, i*104, 0, 104, 104));
        }

        animation = new Animation<TextureRegion>(0.1f, regions);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        animation.setFrameDuration(.3f);

        animatedSprite = new AnimatedSprite(animation);



        icon = new Texture(Gdx.files.internal("button_icons/translate_icon.png"));
        iconDrawable = new TextureRegionDrawable(icon);

        SpriteDrawable spriteDrawable = new SpriteDrawable(animatedSprite);
        iconImage = new VisImage(spriteDrawable);

        getTable().setColor(0.5f, 0.5f, 0.5f, 1f);
        getTable().add(new VisLabel("default.scene - salient","small-font-normal","gray")).pad(3,100,3,5);

        fileMenu = new Menu("File");
        editMenu = new Menu("Edit");
        viewMenu = new Menu("View");
        helpMenu = new Menu("Help");


        getTable().padTop(3);

        menuBarButtons = new VisTable();
        menuBarButtons.align(Align.right);
        exitButton = new VisTextButton("  X  ","menu-bar");
        minimizeButton = new VisTextButton("  _  ","menu-bar");
        maximizeButton = new VisTextButton("  []  ","menu-bar");
        menuBarButtons.add(minimizeButton).pad(3).right().size(20,20);
        menuBarButtons.add(exitButton).pad(3).right().size(20,20);

        minimizeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Lwjgl3Graphics g = (Lwjgl3Graphics) Gdx.graphics;
                g.getWindow().iconifyWindow();
            }
        });
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
                System.exit(0);
            }
        });
//        getTable().getCells().get(0).setActor(iconImage);
        Table items = (Table) getTable().getChild(0);
        items.pad(0);
        Table imageTable = new Table();
        imageTable.padTop(3).padBottom(3);

        VisLabel title = new VisLabel("Salient","big-font-normal","white");
        imageTable.add(iconImage).pad(5,0,5,8).pad(3).size(15,15);
        imageTable.add(title).pad(0,5,0,5).pad(3);
        items.add(imageTable).padRight(5).padLeft(0);
        addMenu(fileMenu);
        addMenu(editMenu);
        addMenu(viewMenu);
        addMenu(helpMenu);
        items.getCells().iterator().forEachRemaining(cell -> {
            cell.padLeft(7).padLeft(7);
        });
        items.getCells().first().padLeft(2);

        getTable().add(menuBarButtons).expandX().fillX().setActorHeight(5);

    }

    public void animateIcon(){
        animatedSprite.update();
        animatedSprite.play();

    }
}
