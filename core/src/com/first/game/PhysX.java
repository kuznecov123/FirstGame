package com.first.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Iterator;

public class PhysX {
    private final World world;
    private final Box2DDebugRenderer  debugRenderer;

    public PhysX(){
        world = new World(new Vector2(0, -9.81f), true);
        debugRenderer = new Box2DDebugRenderer();
    }
    public void step(){world.step(1/60.0f, 3, 3);}
    public void debugDraw(OrthographicCamera camera){debugRenderer.render(world, camera.combined);}

    public void dispose(){
        world.dispose();
        debugRenderer.dispose();
    }

    public void addObject(MapObjects objects){
        BodyDef def = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape poly_h = new PolygonShape();

        Iterator<MapObject> objectIterator = objects.iterator();
        while(objectIterator.hasNext()){
        MapObject obj = objectIterator.next();

        switch ((String) obj.getProperties().get("type")){
            case "StaticBody":
                def.type = BodyDef.BodyType.StaticBody;
                break;
            case "DynamicBody":
                def.type = BodyDef.BodyType.DynamicBody;
                break;
            case "KinematicBody;":
                def.type = BodyDef.BodyType.KinematicBody;
                break;
            default:

        }
        def.gravityScale = (float) obj.getProperties().get("gravityScale");
            RectangleMapObject rect = (RectangleMapObject) obj;
            def.position.set(new Vector2(rect.getRectangle().x + rect.getRectangle().width/2, rect.getRectangle().y + rect.getRectangle().height/2 ));
            poly_h.setAsBox(rect.getRectangle().width/2, rect.getRectangle().height/2);
            fdef.shape = poly_h;

            fdef.restitution = (float) obj.getProperties().get("restitution");
            fdef.density = (float) obj.getProperties().get("density");
            fdef.friction = (float) obj.getProperties().get("friction");
            String name= (String)  obj.getProperties().get("name");

            world.createBody(def).createFixture(fdef).setUserData(name);


        }

        poly_h.dispose();

    }

}
