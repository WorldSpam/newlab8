package com.mygdx.game;

import static com.mygdx.game.utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Droplet {
    private int x,y;
    //public Vector2 position;
    public World world;
    public com.badlogic.gdx.physics.box2d.Body b2body;
    public Sprite sprite = new Sprite(new Texture(Gdx.files.internal("droplet.png")));
    MyData data;
    public Droplet(World world,int x,int y){
        this.world = world;
        this.x = x;
        this.y=y;
        data = new MyData("droplet");
        defineSprite();
        //position = new Vector2(b2body.getPosition().x,b2body.getPosition().y);
        //setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        //setTouchable(Touchable.enabled);

    }
    public void defineSprite(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(x ,y /*x + 64 / PPM , y + 64 / PPM */);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.gravityScale = 5f;
        b2body = world.createBody(bdef);
        b2body.setUserData(data);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(64/2, 64/2);

        fdef.shape = shape;
        b2body.createFixture(fdef);
        b2body.getFixtureList().get(0).setUserData(data);

        //System.out.println(b2body.getUserData());
        //b2body.setUserData(sprite);

    }
    public void render(SpriteBatch batch) {
        // First we position and rotate the sprite correctly
        int posX = (int) (b2body.getPosition().x * PPM);
        int posY = (int) (b2body.getPosition().y * PPM);
        float rotation = (float) Math.toDegrees(b2body.getAngle());
        sprite.setPosition(b2body.getPosition().x - sprite.getWidth()/2, b2body.getPosition().y - sprite.getHeight()/2);
        //System.out.println(b2body.getPosition().x + " " + sprite.getX()+" ");
        sprite.setRotation(rotation);

        // Then we simply draw it as a normal sprite.
        sprite.draw(batch);
        //b2body.setTransform(x ,y ,0);
    }
    public void dispose(){
        sprite.getTexture().dispose();
        world.destroyBody(b2body);
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public Vector2 getPosition() {
        return b2body.getPosition();
    }

    public float getWidth() {
        return 64 / PPM;
    }

    public float getHeight() {
        return 64 / PPM;
    }

}
