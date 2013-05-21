/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

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
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.IModifier;
import org.game.runner.base.BaseScene;
import org.game.runner.game.descriptor.LevelDescriptor;
import org.game.runner.game.element.background.BackgroundElement;
import org.game.runner.game.element.level.LevelElement;
import org.game.runner.game.player.Player;
import org.game.runner.game.player.Trail;
import org.game.runner.manager.AudioManager;
import org.game.runner.manager.SceneManager;
import org.game.runner.utils.ease.EaseBroadcast;

/**
 *
 * @author Karl
 */
public abstract class GameLevelScene extends BaseScene implements IOnSceneTouchListener{
    private final float RIGHT_SPAWN = 850;
    private final float PLAYER_X = 250;
    private final float GROUND_LEVEL = 50;
    private final float GROUND_WIDTH = 1000;
    private final float GROUND_THICKNESS = LevelElement.PLATFORM_THICKNESS;
    private final float BROADCAST_LEVEL = 240;
    private final float BROADCAST_LEFT = -100;
    private final float BROADCAST_RIGHT = 1000;
    
    //HUD
    protected HUD hud;
    //HUD Broadcast
    private Text chrono3;
    private Text chrono2;
    private Text chrono1;
    private Text chronoStart;
    //Level
    protected LevelDescriptor level;
    private TimerHandler levelReaderHandler;
    private ITimerCallback levelReaderAction;
    private TimerHandler bonusPickHandler;
    private ITimerCallback bonusPickAction;
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
    
    public GameLevelScene(LevelDescriptor level){
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
                    GameLevelScene.this.player.resetMovements();
                }
                if(xA.getBody().getUserData().equals("player") && xB.getBody().getUserData() instanceof LevelElement){
                    LevelElement element = (LevelElement)xB.getBody().getUserData();
                    if(element.isPlatform()){
                        float playerY = xA.getBody().getPosition().y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT - GameLevelScene.this.player.getHeight() / 2;
                        float elementY = xB.getBody().getPosition().y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT + LevelElement.PLATFORM_THICKNESS / 2;
                        
                        if (playerY >= elementY && xA.getBody().getLinearVelocity().y < 0.5) {
                            element.getBuildedShape().registerEntityModifier(new ColorModifier(.3f, element.getBuildedShape().getColor(), LevelElement.COLOR_DEFAULT));
                            GameLevelScene.this.player.resetMovements();
                        }
                    }
                    else{
                        xB.getBody().setActive(false);
                        GameLevelScene.this.disposeLevelElement(element.getBuildedShape());
                        element.doPlayerAction(GameLevelScene.this.player);
                    }
                }
                if(xB.getBody().getUserData().equals("player") && xA.getBody().getUserData() instanceof LevelElement){
                    LevelElement element = (LevelElement)xB.getBody().getUserData();
                    if(element.isPlatform()){
                        float playerY = xB.getBody().getPosition().y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT - GameLevelScene.this.player.getHeight() / 2;
                        float elementY = xA.getBody().getPosition().y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT + LevelElement.PLATFORM_THICKNESS / 2;
                        
                        if (playerY >= elementY && xB.getBody().getLinearVelocity().y < 0.5) {
                            element.getBuildedShape().registerEntityModifier(new ColorModifier(.3f, element.getBuildedShape().getColor(), LevelElement.COLOR_DEFAULT));
                            GameLevelScene.this.player.resetMovements();
                        }
                    }
                    else{
                        xA.getBody().setActive(false);
                        GameLevelScene.this.disposeLevelElement(element.getBuildedShape());
                        element.doPlayerAction(GameLevelScene.this.player);
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
                        float playerY = xA.getBody().getPosition().y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT - GameLevelScene.this.player.getHeight() / 2;
                        float elementY = xB.getBody().getPosition().y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT + LevelElement.PLATFORM_THICKNESS / 2;
                        
                        if (playerY < elementY) {
                            contact.setEnabled(false);
                        }
                    }
                    
                }
                if(xB.getBody().getUserData().equals("player") && xA.getBody().getUserData() instanceof LevelElement){
                    LevelElement element = (LevelElement)xA.getBody().getUserData();
                    if(element.isPlatform()){
                        float playerY = xB.getBody().getPosition().y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT - GameLevelScene.this.player.getHeight() / 2;
                        float elementY = xA.getBody().getPosition().y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT + LevelElement.PLATFORM_THICKNESS / 2;

                        if (playerY < elementY) {
                            contact.setEnabled(false);
                        }
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
		super.onUpdate(pSecondsElapsed * GameLevelScene.this.parallaxFactor);
            }
        };
        this.setBackground(this.autoParallaxBackground);
        for(BackgroundElement layer : this.level.getBackgrounds()){
            Sprite layerSprite = new Sprite(layer.x, GROUND_LEVEL + layer.y, this.resourcesManager.gameParallaxLayers.get(layer.getResourceName()), this.vbom);
            layerSprite.setColor(new Color(0.4f, 0.4f, 0.4f));
            this.backgroundParallaxLayers.add(layerSprite);
            layerSprite.setOffsetCenter(0, 0);
            this.autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-layer.speed, layerSprite));
        }
    }
    private void createPlayer(){
        this.player = new Player(PLAYER_X, GROUND_LEVEL + GROUND_THICKNESS/2 + 32, this.resourcesManager.player, this.vbom, this.physicWorld) {
            @Override
            public void onSpeedChange(float speed) {
                GameLevelScene.this.activity.vibrate(30);
            }
            @Override
            protected void onJumpModeChange() {
                GameLevelScene.this.activity.vibrate(30);
            }
            @Override
            protected void onGetLife() {
                GameLevelScene.this.activity.vibrate(30);
            }
            @Override
            public void onJump() {
                GameLevelScene.this.activity.vibrate(30);
            }
            @Override
            public void onRoll() {
                GameLevelScene.this.activity.vibrate(30);
            }
            @Override
            public void onRollBackJump() {
                this.reset();
                GameLevelScene.this.restart();
                GameLevelScene.this.activity.vibrate(new long[]{100, 50, 100, 50, 100, 50, 100, 50, 100, 50, 100, 50, 100});
            }
            @Override
            protected void onBonus() {
                this.registerUpdateHandler(GameLevelScene.this.bonusPickHandler = new TimerHandler(8, GameLevelScene.this.bonusPickAction));
            }

            @Override
            protected void onBonusReset() {
                this.clearUpdateHandlers();
                this.clearEntityModifiers();
            }
        };
        this.player.getBody().setUserData("player");
        this.bonusPickAction = new ITimerCallback(){                      
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                GameLevelScene.this.player.registerEntityModifier(new SequenceEntityModifier(new IEntityModifierListener() {
                        @Override
                        public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) {}
                        @Override
                        public void onModifierFinished(final IModifier<IEntity> pEntityModifier, final IEntity pEntity) {
                            GameLevelScene.this.player.endBonus();
                        }
                    },
                    new ColorModifier(0.4f, GameLevelScene.this.player.getColor(), Color.WHITE),
                    new ColorModifier(0.4f, Color.WHITE, GameLevelScene.this.player.getColor()),
                    new ColorModifier(0.3f, GameLevelScene.this.player.getColor(), Color.WHITE),
                    new ColorModifier(0.3f, Color.WHITE, GameLevelScene.this.player.getColor()),
                    new ColorModifier(0.2f, GameLevelScene.this.player.getColor(), Color.WHITE),
                    new ColorModifier(0.2f, Color.WHITE, GameLevelScene.this.player.getColor()),
                    new ColorModifier(0.1f, GameLevelScene.this.player.getColor(), Color.WHITE),
                    new ColorModifier(0.1f, Color.WHITE, GameLevelScene.this.player.getColor()),
                    new ColorModifier(0.1f, GameLevelScene.this.player.getColor(), Color.WHITE),
                    new ColorModifier(0.1f, Color.WHITE, GameLevelScene.this.player.getColor()),
                    new ColorModifier(0.1f, GameLevelScene.this.player.getColor(), Color.WHITE),
                    new ColorModifier(0.1f, Color.WHITE, GameLevelScene.this.player.getColor()),
                    new ColorModifier(0.1f, GameLevelScene.this.player.getColor(), Color.WHITE),
                    new ColorModifier(0.1f, Color.WHITE, GameLevelScene.this.player.getColor()))
                 );
            }
        };
        this.attachChild(this.player);
        this.playerTrail = new Trail(32, 0, 0, 64, -320, -280, -2, 2, 15, 20, 40, Trail.ColorMode.NORMAL, this.player, this.resourcesManager.trail, this.vbom);
        this.playerTrail.hide();
        
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
    private void createLevelSpwaner(){
        //Level elements
        this.levelReaderAction = new ITimerCallback(){                      
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler){
                if(!GameLevelScene.this.level.hasNext()){
                    //End of level
                    GameLevelScene.this.unregisterUpdateHandler(GameLevelScene.this.levelReaderHandler);
                }
                else{
                    //Level elements spawn
                    final float baseY = GROUND_LEVEL + GROUND_THICKNESS/2;
                    final LevelElement lvlElement = GameLevelScene.this.level.getNext();
                    lvlElement.build(RIGHT_SPAWN, baseY, GameLevelScene.this.vbom, GameLevelScene.this.player, GameLevelScene.this.physicWorld);
                    GameLevelScene.this.attachChild(lvlElement.getBuildedShape());
                    lvlElement.getBuildedShape().setZIndex(GameLevelScene.this.player.getZIndex() - 2);
                    GameLevelScene.this.sortChildren();
                    GameLevelScene.this.levelElements.add(lvlElement.getBuildedShape());
                    lvlElement.getBuildedBody().setUserData(lvlElement);
                    lvlElement.getBuildedBody().setLinearVelocity(new Vector2(-15, 0));
                    GameLevelScene.this.engine.registerUpdateHandler(new TimerHandler(6f, new ITimerCallback(){
                        @Override
                        public void onTimePassed(final TimerHandler pTimerHandler){
                            GameLevelScene.this.disposeLevelElement(lvlElement.getBuildedShape());
                        }
                    }));
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
        this.chronoStart = new Text(0, 0, resourcesManager.fontPixel_200, "GO !", vbom);
        this.chrono3.setVisible(false);
        this.chrono2.setVisible(false);
        this.chrono1.setVisible(false);
        this.chronoStart.setVisible(false);
        this.hud.attachChild(this.chrono3);
        this.hud.attachChild(this.chrono2);
        this.hud.attachChild(this.chrono1);
        this.hud.attachChild(this.chronoStart);
    }
    
    private void restart(){
        this.onRestartBegin();
        this.unregisterUpdateHandler(this.levelReaderHandler);
        AudioManager.getInstance().stop();
        this.player.resetBonus();
        this.player.setAlpha(1f);
        this.player.clearUpdateHandlers();
        this.player.clearEntityModifiers();
        this.playerTrail.hide();
        this.parallaxFactor = -10f;
        this.disposeLevelElements();
        this.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback(){
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler){
                GameLevelScene.this.parallaxFactor = 1f;
                GameLevelScene.this.onRestartEnd();
                GameLevelScene.this.start();
            }
        }));
    }
    protected abstract void onRestartBegin();
    protected abstract void onRestartEnd();
    public void start(){
        this.level.init();
        this.onStartBegin();
        
        this.broadcast(this.chrono3, new IEntityModifierListener() {
            @Override
            public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
            }
            
            @Override
            public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                 GameLevelScene.this.broadcast(GameLevelScene.this.chrono2, new IEntityModifierListener() {
                    @Override
                    public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
                    }
                    
                    @Override
                    public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                        GameLevelScene.this.broadcast(GameLevelScene.this.chrono1, new IEntityModifierListener() {
                            @Override
                            public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
                            }
                            
                            @Override
                            public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                                GameLevelScene.this.broadcast(GameLevelScene.this.chronoStart, new IEntityModifierListener() {
                                    @Override
                                    public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
                                        AudioManager.getInstance().play("mfx/", GameLevelScene.this.level.getMusic());
                                        GameLevelScene.this.playerTrail.show();
                                        GameLevelScene.this.registerUpdateHandler(GameLevelScene.this.levelReaderHandler = new TimerHandler(GameLevelScene.this.level.getSpawnTime(), true, GameLevelScene.this.levelReaderAction));
                                        GameLevelScene.this.onStartEnd();
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
        this.audioManager.pause();
    }
    @Override
    public void onResume() {
        this.audioManager.resume();
    }
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed * this.player.getSpeed());
    }
    
    @Override
    public void disposeScene() {
        this.destroyPhysicsWorld();
        this.disposeLevelElements();
        
        this.camera.setHUD(null);
        this.chrono3.detachSelf();
        this.chrono3.dispose();
        this.chrono2.detachSelf();
        this.chrono2.dispose();
        this.chrono1.detachSelf();
        this.chrono1.dispose();
        
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
                PhysicsWorld world = GameLevelScene.this.physicWorld;
                Iterator<Body> localIterator = GameLevelScene.this.physicWorld.getBodies();
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
            public void run() 
            {
                if (physicsConnector != null){
                     Body body = physicsConnector.getBody();
                     GameLevelScene.this.physicWorld.unregisterPhysicsConnector(physicsConnector);
                     body.setActive(false);
                     GameLevelScene.this.physicWorld.destroyBody(body);
                }
                element.detachSelf();
            }
        });
    }
    private void disposeLevelElements(){
        for(Shape element : GameLevelScene.this.levelElements){
            this.disposeLevelElement(element);
        }
    }

    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionDown()){
            if(pSceneTouchEvent.getY() > 250){
                //Jump
                this.player.jump();
            }
            else if(pSceneTouchEvent.getY() < 230){
                //Roll
                this.player.roll();
            }
        }
        return false;
    }
}
