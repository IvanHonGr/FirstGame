package com.home.slaughter.bucket.bodies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.entity.shape.IAreaShape;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

public class WormMouth{

    final public static String LEFT = "left";
    final public static String RIGHT = "right";

    public static Body createWormMouthBody(final PhysicsWorld pPhysicsWorld, final IAreaShape pAreaShape, final BodyDef.BodyType pBodyType, final FixtureDef pFixtureDef, String side) {
        final float width = pAreaShape.getWidth();
        final float height = pAreaShape.getHeight();

        final float x1 = 46f/ PIXEL_TO_METER_RATIO_DEFAULT;
        final float y1 = -30f/ PIXEL_TO_METER_RATIO_DEFAULT;
        final float x2 = 59f/ PIXEL_TO_METER_RATIO_DEFAULT;
        final float y2 = -30f/ PIXEL_TO_METER_RATIO_DEFAULT;
        final float x3 = 72f/ PIXEL_TO_METER_RATIO_DEFAULT;
        final float y3 = 44f/ PIXEL_TO_METER_RATIO_DEFAULT;
        final float x4 = 59f/ PIXEL_TO_METER_RATIO_DEFAULT;
        final float y4 = 44f/ PIXEL_TO_METER_RATIO_DEFAULT;

        final Vector2[] verticesRight = {
            new Vector2(x1, y1),
            new Vector2(x2, y2),
            new Vector2(x3, y3),
            new Vector2(x4, y4)
        };

        final Vector2[] verticesLeft = {
            new Vector2(-x1, y1),
            new Vector2(-x4, y4),
            new Vector2(-x3, y3),
            new Vector2(-x2, y2)
        };
        if (RIGHT.equals(side)) {
            return PhysicsFactory.createPolygonBody(pPhysicsWorld, pAreaShape, verticesRight, pBodyType, pFixtureDef);
        } else
            return PhysicsFactory.createPolygonBody(pPhysicsWorld, pAreaShape, verticesLeft, pBodyType, pFixtureDef);
    }
}
