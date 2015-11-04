package com.home.slaughter.bucket;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
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
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class GameActivity extends SimpleBaseGameActivity implements IAccelerationListener, IOnSceneTouchListener {
    // ===========================================================
    // Constants
    // ===========================================================

    private static final int CAMERA_WIDTH = 720;
    private static final int CAMERA_HEIGHT = 480;

    private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);

    // ===========================================================
    // Fields
    // ===========================================================

    private BitmapTextureAtlas mBitmapTextureAtlas;
    private ITextureRegion mWormTextureRegion;
    private ITextureRegion mTriangleTestureRegion;

    private TiledTextureRegion mCircleFaceTextureRegion;

    private PhysicsWorld mPhysicsWorld;
    private int mFaceCount = 0;

    final Scene mScene = new Scene();

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public EngineOptions onCreateEngineOptions() {
        Toast.makeText(this, "Touch the screen to add objects.", Toast.LENGTH_LONG).show();

        final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
    }

    @Override
    public void onCreateResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 512, 256, TextureOptions.BILINEAR);
        mCircleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, this, "happy_smile.png", 0, 0, 2, 1); // 64x32
        mWormTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, this, "worm.png", 64, 0); //116x135
        mTriangleTestureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, this, "triangle.png", 180, 0); //188x77
        mBitmapTextureAtlas.load();
    }

    @Override
    public Scene onCreateScene() {
        mEngine.registerUpdateHandler(new FPSLogger());

        mScene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
        mScene.setOnSceneTouchListener(this);

        final float centerX = (CAMERA_WIDTH - mCircleFaceTextureRegion.getWidth()) / 2;
        final float centerY = (CAMERA_HEIGHT - mCircleFaceTextureRegion.getHeight()) / 2;

        final Sprite worm = new Sprite(getTextureCenterX(mWormTextureRegion), CAMERA_HEIGHT - 2 - mWormTextureRegion.getHeight(), mWormTextureRegion, this.getVertexBufferObjectManager());
        final Sprite triangle = new Sprite(getTextureCenterX(mTriangleTestureRegion), 0, mTriangleTestureRegion, this.getVertexBufferObjectManager());

        mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
        Body wormBody = PhysicsFactory.createBoxBody(mPhysicsWorld, worm, BodyType.StaticBody, FIXTURE_DEF);
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(worm, wormBody, true, true));

        final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
        final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, vertexBufferObjectManager);
        final Rectangle roof = new Rectangle(0, 0, CAMERA_WIDTH, 2, vertexBufferObjectManager);
        final Rectangle left = new Rectangle(0, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);
        final Rectangle right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);

        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
        PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);

        mScene.attachChild(ground);
        mScene.attachChild(roof);
        mScene.attachChild(left);
        mScene.attachChild(right);

        mScene.registerUpdateHandler(this.mPhysicsWorld);
        mScene.attachChild(worm);
        mScene.attachChild(triangle);

        return mScene;
    }

    private float getTextureCenterX (ITextureRegion textureRegion) {
        return (CAMERA_WIDTH - textureRegion.getWidth())/2;
    }

    @Override
    public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
        if(this.mPhysicsWorld != null) {
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

        face = new AnimatedSprite(pX, pY, this.mCircleFaceTextureRegion, this.getVertexBufferObjectManager());
        Body body = PhysicsFactory.createBoxBody(this.mPhysicsWorld, face, BodyType.DynamicBody, FIXTURE_DEF);

        face.animate(200);

        mScene.attachChild(face);
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(face, body, true, true));
    }

}
