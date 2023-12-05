package com.mygdx.game.screens;

import static com.badlogic.gdx.math.Intersector.overlaps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.App;
import com.mygdx.game.actors.Bucket;
import com.mygdx.game.actors.Droplet;

import java.util.ArrayList;
import java.util.Iterator;


public class PlayScreen implements Screen {
    private final App app;
    private Stage stage;
    //Texture dropImage;
    //Texture bucketImage;
    Sound dropSound;
    Music rainMusic;
    //Rectangle bucket;
    Bucket bucket;
    ArrayList<Droplet> raindrops;
    long lastDropTime;
    int dropsGathered;

    public PlayScreen(final App app){
        this.app = app;
        this.stage = new Stage(new ScreenViewport());
        bucket = new Bucket();
        bucket.setPosition(stage.getWidth()/2,40);


        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        rainMusic.setLooping(true);

        // create the raindrops array and spawn the first raindrop
        raindrops = new ArrayList<Droplet>();
        spawnRaindrop();
    }
    private void spawnRaindrop() {
        Droplet drop = new Droplet();
        drop.setPosition(MathUtils.random(0, stage.getWidth() - 64), stage.getHeight()-64);
        System.out.println(MathUtils.random(0, stage.getWidth() - 64) + ", " + (stage.getHeight() - 64));
        raindrops.add(drop);
        stage.addActor(drop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        rainMusic.play();
        bucket.addListener(new DragListener()
        {
            public void drag(InputEvent event, float x, float y, int pointer)
            {
                bucket.moveBy(x - bucket.getWidth() / 2, 0);
            }
        });
        stage.addActor(bucket);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (bucket.getX() < 0)
            bucket.setX(0);
        if (bucket.getX() > stage.getWidth() - 64)
            bucket.setX(stage.getWidth()-64);

        if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
            spawnRaindrop();

/*
        for (Droplet raindrop : raindrops) {


            raindrop.setY(raindrop.getY()-200 * Gdx.graphics.getDeltaTime());// -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.getY() + 64 < 0)
                raindrop.remove();
                raindrops.remove(raindrop);

            if(Intersector.overlaps(raindrop.getSprite().getBoundingRectangle(),bucket.getSprite().getBoundingRectangle())){
                dropsGathered++;
                dropSound.play();
                raindrop.remove();
                raindrops.remove(raindrop);
            }
            //stage.addActor(raindrop);
        }

 */



        Iterator<Droplet> iter = raindrops.iterator();
        while (iter.hasNext()) {
            Droplet raindrop = iter.next();
            raindrop.setY(raindrop.getY()-200 * Gdx.graphics.getDeltaTime());// -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.getY() + 64 < 0){
                raindrop.addAction(Actions.removeActor());
                iter.remove();
                //raindrop.getSprite().getTexture().dispose();
            }



            if(Intersector.overlaps(raindrop.getSprite().getBoundingRectangle(),bucket.getSprite().getBoundingRectangle())){
                dropsGathered++;
                dropSound.play();
                //raindrop.rem
                raindrop.addAction(Actions.removeActor());
                iter.remove();
            }
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
        dropSound.dispose();
        rainMusic.dispose();
    }
}
