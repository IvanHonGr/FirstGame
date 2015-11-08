package com.home.slaughter.bucket;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.home.slaughter.bucket.bodies.WormMouth;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

public class GameActivity extends SimpleBaseGameActivity implements IAccelerationListener, IOnSceneTouchListener {

    private static final int CAMERA_WIDTH = 720;
    private static final int CAMERA_HEIGHT = 480;
    private static final int WORM_MUSTAGE_WIDTH = 35;
    private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);

    private ITextureRegion mWormTextureRegion;
    private ITextureRegion mTriangleTextureRegion;
    private TiledTextureRegion mCircleFaceTextureRegion;
    private PhysicsWorld mPhysicsWorld;

    final Scene mScene = new Scene();
    Line line;
    Sprite worm;


    @Override
    public EngineOptions onCreateEngineOptions() {
        Toast.makeText(this, "Touch the screen to add objects.", Toast.LENGTH_LONG).show();

        final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
    }

    @Override
    public void onCreateResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        BitmapTextureAtlas mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 512, 256, TextureOptions.BILINEAR);
        mCircleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, this, "happy_smile.png", 0, 0, 2, 1); // 64x32
        mWormTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, this, "worm_b_small.png", 64, 0); //200x88
        mTriangleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, this, "ufo_0_small.png", 264, 0); //188x77
        mBitmapTextureAtlas.load();
    }

    @Override
    public Scene onCreateScene() {
        mEngine.registerUpdateHandler(new FPSLogger());

        mScene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
        mScene.setOnSceneTouchListener(this);

        mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
        worm = new Sprite(getTextureCenterX(mWormTextureRegion), CAMERA_HEIGHT - 2 - mWormTextureRegion.getHeight(), mWormTextureRegion, this.getVertexBufferObjectManager());
        Body wormLeftPart = WormMouth.createWormMouthBody(mPhysicsWorld, worm, BodyType.StaticBody, PhysicsFactory.createFixtureDef(100, 0, 0.5f), WormMouth.LEFT);
        Body wormRightPart = WormMouth.createWormMouthBody(mPhysicsWorld, worm, BodyType.StaticBody, PhysicsFactory.createFixtureDef(100, 0, 0.5f), WormMouth.RIGHT);
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(worm, wormLeftPart, true, true));
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(worm, wormRightPart, true, true));

        final Sprite triangle = new Sprite(getTextureCenterX(mTriangleTextureRegion), 0, mTriangleTextureRegion, this.getVertexBufferObjectManager());
        line = new Line(getTextureCenterX(mWormTextureRegion) + WORM_MUSTAGE_WIDTH, CAMERA_HEIGHT - 5, (CAMERA_WIDTH + mWormTextureRegion.getWidth())/2 - WORM_MUSTAGE_WIDTH, CAMERA_HEIGHT - 5, getVertexBufferObjectManager());

        final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
        final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, vertexBufferObjectManager);
        final Rectangle roof = new Rectangle(0, 0, CAMERA_WIDTH, 2, vertexBufferObjectManager);
        final Rectangle left = new Rectangle(0, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);
        final Rectangle right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);

        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
        PhysicsFactory.createBoxBody(mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);

        mScene.attachChild(ground);
        mScene.attachChild(roof);
        mScene.attachChild(left);
        mScene.attachChild(right);

        mScene.registerUpdateHandler(mPhysicsWorld);
        mScene.attachChild(worm);
        mScene.attachChild(triangle);



//ToDo change to TimerHandler
        mScene.registerUpdateHandler(new IUpdateHandler() {
            float timePassed = 0;
            @Override
            public void reset() {
                Log.e("Reset", "it is time");
            }

            @Override
            public void onUpdate(final float pSecondsElapsed) {
                timePassed += pSecondsElapsed;
                if (timePassed > 0.1) {
                    addFace((CAMERA_WIDTH - triangle.getWidth())/2 + (int) (Math.random() * (triangle.getWidth() - 32)), triangle.getHeight());
                    timePassed = 0;
                }

            }
        });

        return mScene;
    }

    private float getTextureCenterX (ITextureRegion textureRegion) {
        return (CAMERA_WIDTH - textureRegion.getWidth())/2;
    }

    @Override
    public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
        if(mPhysicsWorld != null) {
            if(pSceneTouchEvent.isActionDown()) {
                this.addFace(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
                return true;
            }
        }
        return false;
    }

    @Override
    public void onAccelerationAccuracyChanged(final AccelerationData pAccelerationData) {

    }

    @Override
    public void onAccelerationChanged(final AccelerationData pAccelerationData) {
        final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX(), pAccelerationData.getY());
        Vector2Pool.recycle(gravity);
    }

    @Override
    public void onResumeGame() {
        super.onResumeGame();

        this.enableAccelerationSensor(this);
    }

    @Override
    public void onPauseGame() {
        super.onPauseGame();

        this.disableAccelerationSensor();
    }

    private void addFace(final float pX, final float pY) {

        final AnimatedSprite face;

        face = new AnimatedSprite(pX, pY, mCircleFaceTextureRegion, getVertexBufferObjectManager());
        Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, face, BodyType.DynamicBody, FIXTURE_DEF);

        face.animate(200);

        mScene.attachChild(face);
        mScene.detachChild(worm);
        mScene.attachChild(worm);
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(face, body, true, true));
        mScene.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void reset() {
            }

            @Override
            public void onUpdate(final float pSecondsElapsed) {
                if (line.collidesWith(face)) {
                    removeFace(face);
                    mScene.unregisterUpdateHandler(this);
                }
            }
        });
    }

    private void removeFace(final AnimatedSprite face) {
        final PhysicsConnector facePhysicsConnector = mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(face);
        mPhysicsWorld.unregisterPhysicsConnector(facePhysicsConnector);
        mPhysicsWorld.destroyBody(facePhysicsConnector.getBody());

        mScene.unregisterTouchArea(face);
        mScene.detachChild(face);
        face.detachChildren();
        face.detachSelf();
        System.gc();
    }

    private static Body createHexagonBody(final PhysicsWorld pPhysicsWorld, final IAreaShape pAreaShape, final BodyType pBodyType, final FixtureDef pFixtureDef) {
		/* Remember that the vertices are relative to the center-coordinates of the Shape. */
        final float halfWidth = pAreaShape.getWidthScaled() * 0.5f / PIXEL_TO_METER_RATIO_DEFAULT;
        final float halfHeight = pAreaShape.getHeightScaled() * 0.5f / PIXEL_TO_METER_RATIO_DEFAULT;

		/* The top and bottom vertex of the hexagon are on the bottom and top of hexagon-sprite. */
        final float top = -halfHeight;
        final float bottom = halfHeight;

        final float centerX = 0;

		/* The left and right vertices of the heaxgon are not on the edge of the hexagon-sprite, so we need to inset them a little. */
        final float left = -halfWidth + 2.5f / PIXEL_TO_METER_RATIO_DEFAULT;
        final float right = halfWidth - 2.5f / PIXEL_TO_METER_RATIO_DEFAULT;
        final float higher = top + 8.25f / PIXEL_TO_METER_RATIO_DEFAULT;
        final float lower = bottom - 8.25f / PIXEL_TO_METER_RATIO_DEFAULT;

        final Vector2[] vertices = {
                new Vector2(centerX, top),
                new Vector2(right, higher),
                new Vector2(right, lower),
                new Vector2(centerX, bottom),
                new Vector2(left, lower),
                new Vector2(left, higher)
        };

        return PhysicsFactory.createPolygonBody(pPhysicsWorld, pAreaShape, vertices, pBodyType, pFixtureDef);
    }



}
