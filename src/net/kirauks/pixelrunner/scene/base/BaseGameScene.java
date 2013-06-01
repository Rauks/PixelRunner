/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.scene.base;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.IModifier;
import net.kirauks.pixelrunner.GameActivity;
import net.kirauks.pixelrunner.game.IPlayerListener;
import net.kirauks.pixelrunner.scene.base.BaseScene;
import net.kirauks.pixelrunner.game.descriptor.LevelDescriptor;
import net.kirauks.pixelrunner.game.element.background.BackgroundElement;
import net.kirauks.pixelrunner.game.element.level.LevelElement;
import net.kirauks.pixelrunner.game.Player;
import net.kirauks.pixelrunner.game.Trail;
import net.kirauks.pixelrunner.manager.AudioManager;
import net.kirauks.pixelrunner.manager.SceneManager;
import net.kirauks.pixelrunner.scene.base.utils.ease.EaseBroadcast;

/**
 *
 * @author Karl
 */
public abstract class BaseGameScene extends BaseScene implements IOnSceneTouchListener{
    private final float RIGHT_SPAWN = 900;
    private final float PLAYER_X = 250;
    private final float GROUND_LEVEL = 50;
    private final float GROUND_WIDTH = 1000;
    private final float GROUND_THICKNESS = LevelElement.PLATFORM_THICKNESS;
    private final float BROADCAST_LEVEL = 240;
    private final float BROADCAST_LEFT = -100;
    private final float BROADCAST_RIGHT = 1000;
    
    private boolean isPaused = false;
    private boolean isStarted = false;
    private boolean isWin = false;
    
    //HUD
    protected HUD hud;
    //HUD Broadcast
    private Text chrono3;
    private Text chrono2;
    private Text chrono1;
    private Text chronoStart;
    //HUD - Pause
    private Text pause;
    //HUD - Win
    private Text win;
    //Level
    protected LevelDescriptor level;
    private TimerHandler levelReaderHandler;
    private ITimerCallback levelReaderAction;
    private TimerHandler levelWinHandler;
    //Background
    private AutoParallaxBackground autoParallaxBackground;
    private List<Sprite> backgroundParallaxLayers = new LinkedList<Sprite>();
    private float parallaxFactor = 1f;
    //Graphics
    protected Player player;
    protected Trail playerTrail;
    private Rectangle ground;
    //Physic
    protected PhysicsWorld physicWorld;
    private Body groundBody;
    //Elements memory
    private ConcurrentLinkedQueue<Shape> levelElements = new ConcurrentLinkedQueue<Shape>();
    
    public BaseGameScene(LevelDescriptor level){
        this.level = level;
        
        this.createBackground();
        this.createLevelSpwaner();
    }
    
    @Override
    public void createScene() {
        this.initPhysics();
        
        this.createPlayer();
        this.createGround();
        this.createHUD();
        
        this.setOnSceneTouchListener(this);
    }
    public void initPhysics(){
        this.physicWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -50), false);
        this.physicWorld.setContactListener(new ContactListener(){
            @Override
            public void beginContact(Contact contact){
                final Fixture xA = contact.getFixtureA();
                final Fixture xB = contact.getFixtureB();
                
                //Player contacts
                if (xA.getBody().getUserData().equals("player") && xB.getBody().getUserData().equals("ground")
                 || xB.getBody().getUserData().equals("player") && xA.getBody().getUserData().equals("ground")){
                    BaseGameScene.this.player.resetMovements();
                }
                if(xA.getBody().getUserData().equals("player") && xB.getBody().getUserData() instanceof LevelElement){
                    LevelElement element = (LevelElement)xB.getBody().getUserData();
                    if(element.isPlatform()){
                        float playerY = xA.getBody().getPosition().y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT - BaseGameScene.this.player.getHeight() / 2;
                        float elementY = xB.getBody().getPosition().y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT + LevelElement.PLATFORM_THICKNESS / 2;
                        
                        if (playerY >= elementY && xA.getBody().getLinearVelocity().y < 0.5) {
                            element.doPlayerAction(BaseGameScene.this.player);
                            BaseGameScene.this.player.resetMovements();
                        }
                    }
                }
                if(xB.getBody().getUserData().equals("player") && xA.getBody().getUserData() instanceof LevelElement){
                    LevelElement element = (LevelElement)xB.getBody().getUserData();
                    if(element.isPlatform()){
                        float playerY = xB.getBody().getPosition().y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT - BaseGameScene.this.player.getHeight() / 2;
                        float elementY = xA.getBody().getPosition().y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT + LevelElement.PLATFORM_THICKNESS / 2;
                        
                        if (playerY >= elementY && xB.getBody().getLinearVelocity().y < 0.5) {
                            element.doPlayerAction(BaseGameScene.this.player);
                            BaseGameScene.this.player.resetMovements();
                        }
                    }
                }
            }
            @Override
            public void endContact(Contact contact){
            }
            @Override
            public void preSolve(Contact contact, Manifold oldManifold){
                final Fixture xA = contact.getFixtureA();
                final Fixture xB = contact.getFixtureB();
                
                if(xA.getBody().getUserData().equals("player") && xB.getBody().getUserData() instanceof LevelElement){
                    LevelElement element = (LevelElement)xB.getBody().getUserData();
                    if(element.isPlatform()){
                        float playerY = xA.getBody().getPosition().y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT - BaseGameScene.this.player.getHeight() / 2;
                        float elementY = xB.getBody().getPosition().y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT + LevelElement.PLATFORM_THICKNESS / 2;
                        
                        if (playerY < elementY) {
                            contact.setEnabled(false);
                        }
                    }
                    else{
                        contact.setEnabled(false);
                        element.doPlayerAction(BaseGameScene.this.player);
                    }
                    
                }
                if(xB.getBody().getUserData().equals("player") && xA.getBody().getUserData() instanceof LevelElement){
                    LevelElement element = (LevelElement)xA.getBody().getUserData();
                    if(element.isPlatform()){
                        float playerY = xB.getBody().getPosition().y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT - BaseGameScene.this.player.getHeight() / 2;
                        float elementY = xA.getBody().getPosition().y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT + LevelElement.PLATFORM_THICKNESS / 2;

                        if (playerY < elementY) {
                            contact.setEnabled(false);
                        }
                    }
                    else{
                        contact.setEnabled(false);
                        element.doPlayerAction(BaseGameScene.this.player);
                    }
                }
            }
            @Override
            public void postSolve(Contact contact, ContactImpulse impulse){
            }
        });
        
        this.registerUpdateHandler(this.physicWorld);
        
        /*
        DebugRenderer debug = new DebugRenderer(this.physicWorld, this.vbom);
        this.attachChild(debug);
        */
        
    }
    private void createBackground(){
        this.autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5){
            @Override
            public void onUpdate(final float pSecondsElapsed) {
		super.onUpdate(pSecondsElapsed * BaseGameScene.this.parallaxFactor);
            }
        };
        this.setBackground(this.autoParallaxBackground);
        for(BackgroundElement layer : this.level.getBackgroundsElements()){
            Sprite layerSprite = new Sprite(layer.x, GROUND_LEVEL + layer.y, this.resourcesManager.gameParallaxLayers.get(layer.getResourceName()), this.vbom);
            layerSprite.setColor(new Color(0.4f, 0.4f, 0.4f));
            this.backgroundParallaxLayers.add(layerSprite);
            layerSprite.setOffsetCenter(-1.5f, -1.5f);
            layerSprite.setScale(4f);
            this.autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-layer.speed, layerSprite));
        }
    }
    private void createPlayer(){
        this.player = new Player(PLAYER_X, GROUND_LEVEL + GROUND_THICKNESS/2 + 32, this.resourcesManager.player, this.vbom, this.physicWorld) {            
            @Override
            protected void onUpdateColor(){
                super.onUpdateColor();
                if(BaseGameScene.this.playerTrail != null){
                    BaseGameScene.this.playerTrail.setColor(this.getColor());
                }
            }
        };
        this.player.getBody().setUserData("player");
        this.player.registerPlayerListener(new IPlayerListener() {
            @Override
            public void onJump() {
                BaseGameScene.this.activity.vibrate(30);
            }
            @Override
            public void onRoll() {
                BaseGameScene.this.activity.vibrate(30);
            }
            @Override
            public void onRollBackJump() {
                BaseGameScene.this.player.reset();
                BaseGameScene.this.restart();
                BaseGameScene.this.activity.vibrate(new long[]{100, 50, 100, 50, 100, 50, 100, 50, 100, 50, 100, 50, 100});
            }
            @Override
            public void onBonus() {
                BaseGameScene.this.activity.vibrate(30);
            }
        });
        
        this.attachChild(this.player);
        this.playerTrail = new Trail(36, 0, 0, 64, -340, -300, -2, 2, 25, 30, 50, Trail.ColorMode.NORMAL, this.resourcesManager.trail, this.vbom);
        this.playerTrail.bind(this.player);
        this.attachChild(this.playerTrail);
        this.playerTrail.hide();
        this.playerTrail.setZIndex(this.player.getZIndex() - 1);
        this.sortChildren();
        
	Body retention = PhysicsFactory.createBoxBody(this.physicWorld, PLAYER_X - this.player.getWidth()/2, 250, 1, 400, BodyDef.BodyType.StaticBody, PhysicsFactory.createFixtureDef(0, 0, 0));
        retention.setUserData("retention");
    }
    private void createGround(){
        this.ground = new Rectangle(400, GROUND_LEVEL, GROUND_WIDTH, GROUND_THICKNESS, this.vbom);
        this.ground.setColor(LevelElement.COLOR_DEFAULT);
	this.groundBody = PhysicsFactory.createBoxBody(this.physicWorld, this.ground, BodyDef.BodyType.StaticBody, PhysicsFactory.createFixtureDef(0, 0, 0));
        this.groundBody.setUserData("ground");
        this.attachChild(this.ground);
    }
    protected abstract void onWin();
    private void createLevelSpwaner(){
        //Level elements
        this.levelWinHandler = new TimerHandler(3f, new ITimerCallback(){
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                BaseGameScene.this.unregisterUpdateHandler(pTimerHandler);

                BaseGameScene.this.isWin = true;
                BaseGameScene.this.onWin();

                Sprite player = BaseGameScene.this.player;
                final PhysicsConnector physicsConnector = BaseGameScene.this.physicWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(player);
                physicsConnector.getBody().applyForce(135, 0, 0, 0);
                BaseGameScene.this.parallaxFactor = 2f;

                BaseGameScene.this.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback(){
                    @Override
                    public void onTimePassed(final TimerHandler pTimerHandler){
                        BaseGameScene.this.unregisterUpdateHandler(pTimerHandler);
                        physicsConnector.getBody().applyForce(-135, 0, 0, 0);
                    }
                }));

                BaseGameScene.this.win.setVisible(true);
                AudioManager.getInstance().stop();
                AudioManager.getInstance().play("mfx/", "win.xm");
                BaseGameScene.this.playerTrail.setColorMode(Trail.ColorMode.MULTICOLOR);
            }
        });

        this.levelReaderAction = new ITimerCallback(){                      
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler){
                if(!BaseGameScene.this.level.hasNext()){
                    BaseGameScene.this.unregisterUpdateHandler(pTimerHandler);
                    BaseGameScene.this.registerUpdateHandler(BaseGameScene.this.levelWinHandler);
                }
                else{
                    //Level elements spawn
                    final float baseY = GROUND_LEVEL + GROUND_THICKNESS/2;
                    for(final LevelElement lvlElement : BaseGameScene.this.level.getNext()){
                    lvlElement.build(RIGHT_SPAWN, baseY, BaseGameScene.this.vbom, BaseGameScene.this.player, BaseGameScene.this.physicWorld);
                        BaseGameScene.this.attachChild(lvlElement.getBuildedShape());
                        lvlElement.getBuildedShape().setZIndex(BaseGameScene.this.player.getZIndex() - 2);
                        BaseGameScene.this.sortChildren();
                        BaseGameScene.this.levelElements.add(lvlElement.getBuildedShape());
                        lvlElement.getBuildedBody().setUserData(lvlElement);
                        lvlElement.getBuildedBody().setLinearVelocity(new Vector2(-15, 0));
                        BaseGameScene.this.registerUpdateHandler(new TimerHandler(6f, new ITimerCallback(){
                            @Override
                            public void onTimePassed(final TimerHandler pTimerHandler){
                                BaseGameScene.this.unregisterUpdateHandler(pTimerHandler);
                                BaseGameScene.this.disposeLevelElement(lvlElement.getBuildedShape());
                            }
                        }));
                        if(!BaseGameScene.this.level.hasNext()){
                            BaseGameScene.this.unregisterUpdateHandler(pTimerHandler);
                            BaseGameScene.this.registerUpdateHandler(BaseGameScene.this.levelWinHandler);
                        }
                    }
                }
            }
        };
    }
    private void createHUD(){
        this.hud = new HUD();
        this.camera.setHUD(this.hud);  
        
        //Broadcast messages
        this.chrono3 = new Text(0, 0, resourcesManager.fontPixel_200, "3", vbom);
        this.chrono2 = new Text(0, 0, resourcesManager.fontPixel_200, "2", vbom);
        this.chrono1 = new Text(0, 0, resourcesManager.fontPixel_200, "1", vbom);
        this.chronoStart = new Text(0, 0, resourcesManager.fontPixel_200, "GO!", vbom);
        this.chrono3.setVisible(false);
        this.chrono2.setVisible(false);
        this.chrono1.setVisible(false);
        this.chronoStart.setVisible(false);
        this.hud.attachChild(this.chrono3);
        this.hud.attachChild(this.chrono2);
        this.hud.attachChild(this.chrono1);
        this.hud.attachChild(this.chronoStart);
        
        //Pause message
        this.pause = new Text(GameActivity.CAMERA_WIDTH/2, GameActivity.CAMERA_HEIGHT/2, resourcesManager.fontPixel_200, "PAUSE", vbom);
        this.pause.setVisible(false);
        this.hud.attachChild(this.pause);
        
        //win message
        this.win = new Text(GameActivity.CAMERA_WIDTH/2, GameActivity.CAMERA_HEIGHT/2 + 130, resourcesManager.fontPixel_200, "EPIC", new TextOptions(HorizontalAlign.CENTER), vbom);
        this.win.setVisible(false);
        Text winSub = new Text(this.win.getWidth()/2, -25f, resourcesManager.fontPixel_200, "WIN!", new TextOptions(HorizontalAlign.CENTER), vbom);
        this.win.attachChild(winSub);
        this.hud.attachChild(this.win);
    }
    
    private void restart(){
        this.isStarted = false;
        this.onRestartBegin();
        this.unregisterUpdateHandler(this.levelWinHandler);
        this.unregisterUpdateHandler(this.levelReaderHandler);
        AudioManager.getInstance().stop();
        this.player.resetBonus();
        this.playerTrail.hide();
        this.parallaxFactor = -10f;
        this.disposeLevelElements();
        this.engine.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback(){
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler){
                BaseGameScene.this.engine.unregisterUpdateHandler(pTimerHandler);
                BaseGameScene.this.parallaxFactor = 1f;
                BaseGameScene.this.onRestartEnd();
                BaseGameScene.this.start();
            }
        }));
    }
    protected abstract void onRestartBegin();
    protected abstract void onRestartEnd();
    public void start(){
        if(!BaseGameScene.this.isPaused){
            this.level.init();
            this.onStartBegin();

            this.broadcast(this.chrono3, new IEntityModifierListener() {
                @Override
                public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
                }

                @Override
                public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                     BaseGameScene.this.broadcast(BaseGameScene.this.chrono2, new IEntityModifierListener() {
                        @Override
                        public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
                        }

                        @Override
                        public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                            BaseGameScene.this.broadcast(BaseGameScene.this.chrono1, new IEntityModifierListener() {
                                @Override
                                public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
                                }

                                @Override
                                public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                                    BaseGameScene.this.broadcast(BaseGameScene.this.chronoStart, new IEntityModifierListener() {
                                        @Override
                                        public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
                                            AudioManager.getInstance().play("mfx/", BaseGameScene.this.level.getMusic());
                                            BaseGameScene.this.playerTrail.show();
                                            BaseGameScene.this.registerUpdateHandler(BaseGameScene.this.levelReaderHandler = new TimerHandler(BaseGameScene.this.level.getSpawnTime(), true, BaseGameScene.this.levelReaderAction));
                                            BaseGameScene.this.onStartEnd();
                                            BaseGameScene.this.isStarted = true;
                                        }

                                        @Override
                                        public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                                        }
                                    }); 
                                }
                            });
                        }
                    });
                }
            });
        }
    }
    protected abstract void onStartBegin();
    protected abstract void onStartEnd();
    
    private void broadcast(final IEntity entity, final IEntityModifierListener listener){
        entity.registerEntityModifier(new MoveModifier(0.5f, BROADCAST_RIGHT, BROADCAST_LEVEL, BROADCAST_LEFT, BROADCAST_LEVEL, new IEntityModifierListener() {
            @Override
            public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
                entity.setVisible(true);
                listener.onModifierStarted(pModifier, pItem);
            }
            
            @Override
            public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                entity.setVisible(false);
                listener.onModifierFinished(pModifier, pItem);
            }
        }, EaseBroadcast.getInstance()));
    }

    @Override
    public void onBackKeyPressed() {
        AudioManager.getInstance().stop();
        SceneManager.getInstance().unloadGameLevelScene();
    }
    @Override
    public void onPause() {
        if(!this.isWin){
            this.pause();
        }
        else{
            this.audioManager.pause();
        }
    }
    public void pause(){
        this.isPaused = true;
        this.chrono1.clearEntityModifiers();
        this.chrono2.clearEntityModifiers();
        this.chrono3.clearEntityModifiers();
        this.chronoStart.clearEntityModifiers();
        this.chrono1.setVisible(false);
        this.chrono2.setVisible(false);
        this.chrono3.setVisible(false);
        this.chronoStart.setVisible(false);
        this.setIgnoreUpdate(true);
        this.pause.setVisible(true);
        this.audioManager.pause();
    }
    public void resume(){
        this.isPaused = false;
        if(this.isStarted){
            this.broadcast(this.chrono3, new IEntityModifierListener() {
                @Override
                public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
                    BaseGameScene.this.pause.setVisible(false);
                }

                @Override
                public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                     BaseGameScene.this.broadcast(BaseGameScene.this.chrono2, new IEntityModifierListener() {
                        @Override
                        public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
                        }

                        @Override
                        public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                            BaseGameScene.this.broadcast(BaseGameScene.this.chrono1, new IEntityModifierListener() {
                                @Override
                                public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
                                }

                                @Override
                                public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                                    BaseGameScene.this.setIgnoreUpdate(false);
                                    BaseGameScene.this.audioManager.resume();
                                }
                            });
                        }
                    });
                }
            });
        }
        else{
            this.pause.setVisible(false);
            this.setIgnoreUpdate(false);
            this.audioManager.resume();
            this.start();
        }
    }
    @Override
    public void onResume() {
        if(this.isWin){
            this.audioManager.resume();
        }
    }
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed * this.player.getSpeed());
    }
    
    @Override
    public void disposeScene() {
        this.clearUpdateHandlers();
        this.destroyPhysicsWorld();
        this.disposeLevelElements();
        
        this.camera.setHUD(null);
        this.chrono3.detachSelf();
        this.chrono3.dispose();
        this.chrono2.detachSelf();
        this.chrono2.dispose();
        this.chrono1.detachSelf();
        this.chrono1.dispose();
        this.pause.detachSelf();
        this.pause.dispose();
        this.win.detachSelf();
        this.win.dispose();
        
        this.chronoStart.detachSelf();
        this.chronoStart.dispose();
        this.ground.detachSelf();
        this.ground.dispose();
        this.playerTrail.detachSelf();
        this.playerTrail.dispose();
        this.player.detachSelf();
        this.player.dispose();
        for(Sprite layer : this.backgroundParallaxLayers){
            layer.detachSelf();
            layer.dispose();
        }
        this.detachSelf();
        this.dispose();
    }
    private void destroyPhysicsWorld(){
        this.engine.runOnUpdateThread(new Runnable(){
            public void run(){
                PhysicsWorld world = BaseGameScene.this.physicWorld;
                Iterator<Body> localIterator = BaseGameScene.this.physicWorld.getBodies();
                while (true){
                    if (!localIterator.hasNext()){
                        world.clearForces();
                        world.clearPhysicsConnectors();
                        world.reset();
                        world.dispose();
                        System.gc();
                        return;
                    }
                    try{
                        final Body localBody = (Body) localIterator.next();
                        world.destroyBody(localBody);
                    }
                    catch (Exception localException){
                        Debug.e(localException);
                    }
                }
            }
        });
    }
    private void disposeLevelElement(final Shape element){
        this.levelElements.remove(element);
        final PhysicsConnector physicsConnector = this.physicWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(element);
        this.engine.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                if (physicsConnector != null){
                     Body body = physicsConnector.getBody();
                     BaseGameScene.this.physicWorld.unregisterPhysicsConnector(physicsConnector);
                     body.setActive(false);
                     BaseGameScene.this.physicWorld.destroyBody(body);
                }
                element.detachSelf();
            }
        });
    }
    private void disposeLevelElements(){
        for(Shape element : BaseGameScene.this.levelElements){
            this.disposeLevelElement(element);
        }
    }

    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionDown()){
            if(this.isPaused){
                this.resume();
            }
            else if(this.isWin){
                this.onBackKeyPressed();
            }
            else{
                if(pSceneTouchEvent.getY() >= 240){
                    //Jump
                    this.player.jump();
                }
                else if(pSceneTouchEvent.getY() < 240){
                    //Roll
                    this.player.roll();
                }
            }
        }
        return false;
    }
}
