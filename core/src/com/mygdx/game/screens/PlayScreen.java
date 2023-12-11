package com.mygdx.game.screens;

import static com.mygdx.game.utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.App;
import com.mygdx.game.Bucket;
import com.mygdx.game.Droplet;
import com.mygdx.game.MyData;
import com.mygdx.game.utils.Constants;


import java.util.ArrayList;
import java.util.Iterator;


public class PlayScreen implements Screen {
    private final App app;

    Texture dropImage;
    Texture bucketImage;
    Sound dropSound;
    Music rainMusic;
    OrthographicCamera camera;
    //Rectangle bucket;
    ArrayList<Droplet> raindrops;
    long lastDropTime;
    int dropsGathered;
    private World world;
    //private Body player;
    Bucket player;
    private Box2DDebugRenderer b2dr;

    public PlayScreen(final App app) {
        this.app = app;

        // load the images for the droplet and the bucket, 64x64 pixels each
        //dropImage = new Texture(Gdx.files.internal("droplet.png"));
        //bucketImage = new Texture(Gdx.files.internal("bucket.png"));

        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        rainMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        world = new World(new Vector2(0,-9.8f),false);
        b2dr = new Box2DDebugRenderer();
        player = new Bucket(world);
        player.x = 800 / 2 ;
        player.y = 20+64/2; //

        //player = createBox(800 / 2,40,64,64,true);
        //player.setUserData(bucketSprite);
        // create a Rectangle to logically represent the bucket
        //bucket = new Rectangle();
        //bucket.x = 800 / 2 - 64 / 2; // center the bucket horizontally
        //bucket.y = 20; // bottom left corner of the bucket is 20 pixels above
        // the bottom screen edge
        //bucket.width = 64;
        //bucket.height = 64;
        //player.setUserData(bucket);

        // create the raindrops array and spawn the first raindrop
        raindrops = new ArrayList<>();
        spawnRaindrop();

    }

    private void spawnRaindrop() {
        Droplet raindrop = new Droplet(world,MathUtils.random(0, 800 - 64),480);
        //raindrop.x = MathUtils.random(0, 800 - 64);
        //raindrop.y = 480;
        //System.out.println(raindrop.x + " " +raindrop.y+" ");

        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to clear are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        //ScreenUtils.clear(0, 0, 0.2f, 1);
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        app.batch.setProjectionMatrix(camera.combined);
        /*
        if(player.b2body.getLinearVelocity().x <= 2)
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
*/
        // begin a new batch and draw the bucket and
        // all drops
       // world.getBodies(raindrops);
        for (Iterator<Droplet> iterator = raindrops.iterator(); iterator.hasNext(); ) {
            Droplet raindrop = iterator.next();
            MyData dat = (MyData) raindrop.b2body.getUserData();
            if(dat.isDed()){
                dropsGathered++;
                dropSound.play();
                raindrop.dispose();
                iterator.remove();
            }
            if(raindrop.getPosition().y < 0){
                System.out.println("Out of bounds "+ raindrop.getPosition().y);
                raindrop.dispose();
                iterator.remove();
            }
        }/*
        for(Droplet raindrop : raindrops){
            if(raindrop.getPosition().y < 60){
                System.out.println("Out of bounds "+ raindrop.getPosition().y);
                raindrop.dispose();
                raindrops.remove(raindrop);
            }
        }*/
        app.batch.begin();
        player.render(app.batch);
        app.font.draw(app.batch, "Drops Collected: " + dropsGathered, 0, 480);
        //app.batch.draw(bucketImage, bucket.x, bucket.y, bucket.width, bucket.height);
        for (Droplet raindrop : raindrops) {

            raindrop.render(app.batch);
        }
        app.batch.end();

        world.step(1/60f,6,2);

        b2dr.render(world, camera.combined);


        // process user input

        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            Vector2 touchPos2 = new Vector2();

            touchPos2.set(Gdx.input.getX(), 40);
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            //camera.viewportWidth / Gdx.graphics.getWidth()
            touchPos2.set(camera.viewportWidth / Gdx.graphics.getWidth() * Gdx.input.getX(), 60 );
            camera.unproject(touchPos);
            /*
            if(player.getPosition().x > Gdx.input.getX() ){
                player.setLinearVelocity(-1000*PPM , 0);
            }else if (player.getPosition().x < Gdx.input.getX()){
                player.setLinearVelocity(1000*PPM , 0);
            }*/
            //player.setTransform(touchPos2,0);
            //bucket.x = touchPos.x - 64 / 2;
            player.x = (int) touchPos.x;
            player.y = 20+64/2;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            player.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            player.x += 200 * Gdx.graphics.getDeltaTime();

        // make sure the bucket stays within the screen bounds
        if (player.x < 0)
            player.x = 0;
        if (player.x > 800 - 64)
            player.x = 800 - 64;

        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
            spawnRaindrop();

        // move the raindrops, remove any that are beneath the bottom edge of
        // the screen or that hit the bucket. In the later case we increase the
        // value our drops counter and add a sound effect.
        /*
        Iterator<Rectangle> iter = raindrops.iterator();
        while (iter.hasNext()) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.y + 64 < 0)
                iter.remove();
            if (raindrop.overlaps(bucket)) {
                dropsGathered++;
                dropSound.play();
                iter.remove();
            }
        }*/
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
        rainMusic.play();
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body ah = null;
                System.out.println(contact.getFixtureA().getUserData());
                System.out.println(contact.getFixtureA().getBody());
                if(contact.getFixtureA() != null && contact.getFixtureB() != null){
                    MyData dataA = (MyData) contact.getFixtureA().getUserData();
                    MyData dataB = (MyData) contact.getFixtureB().getUserData();
                    if (dataA.getWat().equals("bucket") && dataB.getWat().equals("droplet")){
                        System.out.println("die");
                        dataB.setDed(true);
                    }
                }

                    //Body bodyA = contact.getFixtureA().getBody();
                    //Body bodyB = contact.getFixtureB().getBody();
                    //dropsGathered++;
                    //dropSound.play();
                    //dataB.setDed(true);
                    //contact.getFixtureB().getBody().setUserData("die");
                    //System.out.println("die");
                //}
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        dropImage.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
        b2dr.dispose();
        world.dispose();
    }
    private Body createBox(int x, int y, int widht, int height, boolean isKinematic){
        Body pBody;
        BodyDef def = new BodyDef();
        if(isKinematic)
            def.type = BodyDef.BodyType.KinematicBody;
        else
            def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x,y);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(widht/2, height/2);

        pBody.createFixture(shape, 1.0f);
        shape.dispose();


        return pBody;
    }
}