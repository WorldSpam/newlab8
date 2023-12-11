package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.App;

public class MenuScreen implements Screen {
    private final App app;
    private Stage stage;
    private Skin skin;
    private TextButton buttonStart;

    public MenuScreen(final App app){
        this.app = app;
        stage = new Stage(new ScreenViewport());
        //Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));

    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        buttonStart = new TextButton("Start game", skin);
        buttonStart.getLabel().setFontScale(5, 5);
        //stage.addActor(buttonStart);
        buttonStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("button");
                app.setScreen( app.playScreen );

            }
        });

        Table root = new Table();
        root.setFillParent(true);
        root.add (buttonStart).minWidth(100).minHeight(50).prefWidth(500).prefHeight(70);
        stage.addActor(root);
        //buttonStart.setPosition(stage.getWidth() /2, stage.getHeight()/2, Align.center);
        //table.row();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
