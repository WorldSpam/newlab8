package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.MenuScreen;
import com.mygdx.game.screens.PlayScreen;

public class App extends Game {
    public static final String TITLE = "Drops";
    public OrthographicCamera camera;
    public SpriteBatch batch;
    public BitmapFont font;
    public AssetManager assets;
    public MenuScreen menuScreen;
    public PlayScreen playScreen;

    @Override
    public void create() {
        assets = new AssetManager();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 400,720);
        batch = new SpriteBatch();
        font = new BitmapFont();

        menuScreen = new MenuScreen(this);
        playScreen = new PlayScreen(this);
        this.setScreen(menuScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        //super.dispose();
        batch.dispose();
        font.dispose();
        assets.dispose();
        menuScreen.dispose();
        playScreen.dispose();

    }
}
