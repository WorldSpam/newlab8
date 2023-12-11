package com.mygdx.game;

import static com.mygdx.game.utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Bucket {
    public int x,y;
    public Vector3 position;
    public World world;
    public com.badlogic.gdx.physics.box2d.Body b2body;
    Sprite sprite = new Sprite(new Texture(Gdx.files.internal("bucket.png")));
    MyData data;
    public Bucket(World world){
        this.world = world;
        data = new MyData("bucket");
        defineSprite();
        //setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        //setTouchable(Touchable.enabled);

    }
    public void defineSprite(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(x ,y /*x + 64 / PPM , y + 64 / PPM */);
        bdef.type = BodyDef.BodyType.KinematicBody;
        b2body = world.createBody(bdef);
        b2body.setUserData(data);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(64/2, 64/2);


        fdef.shape = shape;
        //fdef.isSensor = true;
        b2body.createFixture(fdef);
        b2body.getFixtureList().get(0).setUserData(data);


    }
    public void render(SpriteBatch batch) {
        // First we position and rotate the sprite correctly
        int posX = (int) (b2body.getPosition().x * PPM);
        int posY = (int) (b2body.getPosition().y * PPM);
        float rotation = (float) Math.toDegrees(b2body.getAngle());
        sprite.setPosition(b2body.getPosition().x - sprite.getWidth()/2, y- sprite.getHeight()/2);
        //System.out.println(b2body.getPosition().x + " " + sprite.getX()+" ");
        sprite.setRotation(rotation);

        // Then we simply draw it as a normal sprite.
        sprite.draw(batch);
        b2body.setTransform(x ,y ,0);
    }

    public float getX() {
        return position.x;
    }
    public float getY() {
        return position.y;
    }
    public Vector3 getPosition() {
        return position;
    }

    public float getWidth() {
        return 64 / PPM;
    }

    public float getHeight() {
        return 64 / PPM;
    }
}
