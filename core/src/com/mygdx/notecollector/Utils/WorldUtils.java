package com.mygdx.notecollector.Utils;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by bill on 15/12/2015.
 */
public class WorldUtils {

    public static World CreateWorld(){
        return new World(Constants.WORLD_GRAVITY,true);
    }

    public static Body CreateCollector(World world){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Constants.CollectorStartX, Constants.CollectorStartY);
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.APP_WIDTH/ 2, Constants.APP_HEIGHT /2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        body.createFixture(fixtureDef);
        body.resetMassData();
       // CollectorUserData userData = new CollectorUserData(Constants.CollectorWidth,Constants.CollectorHeight);
        //body.setUserData(userData);
        shape.dispose();
        return body;
    }

    public static Body CreateSquareNotes(World world){
       // SquareNoteType squareNoteType = SquareNoteType.SquarwNoteGrey;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Constants.SquareNoteStartX, Constants.SquareNoteStartY);
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.SquareNoteWidth/ 2, Constants.SquareNoteHeight /2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        body.createFixture(fixtureDef);
        body.resetMassData();
       // SquareNotesUserData userData = new SquareNotesUserData(Constants.SquareNoteWidth, Constants.SquareNoteHeight);
       // body.setUserData(userData);
        shape.dispose();
        return body;
    }

    public static Body CreatePianoKeyboard(World world){
        // SquareNoteType squareNoteType = SquareNoteType.SquarwNoteGrey;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(36f, 56f);
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(5f, 5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        body.createFixture(fixtureDef);
        body.resetMassData();
       // PianoUserData userData = new PianoUserData(728f,56f);
        //body.setUserData(userData);
        shape.dispose();
        return body;
    }


}
