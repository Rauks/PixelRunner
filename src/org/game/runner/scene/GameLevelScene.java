/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;
import org.game.runner.base.BaseScene;
import org.game.runner.game.LevelDescriptor;
import org.game.runner.game.element.LevelElement;
import org.game.runner.manager.AudioManager;
import org.game.runner.manager.SceneManager;
import org.game.runner.manager.SceneManager.SceneType;

/**
 *
 * @author Karl
 */
public class GameLevelScene extends BaseScene implements IOnSceneTouchListener{
    private PhysicsWorld physicWorld;
    private Body groundBody;
    private Body testBody;
    private TimerHandler levelReaderHandler;
    private ITimerCallback levelReaderAction;
    private LevelDescriptor level;
    
    public GameLevelScene(LevelDescriptor level){
        this.level = level;
    }
    
    @Override
    public void createScene() {
        this.setBackground(new Background(Color.BLACK));
        
        this.physicWorld = new FixedStepPhysicsWorld(30, new Vector2(0, -20), false);
        this.registerUpdateHandler(this.physicWorld);
        
        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0);
        
        Rectangle ground = new Rectangle(400, 50, 1000, 10, this.vbom);
	this.groundBody = PhysicsFactory.createBoxBody(this.physicWorld, ground, BodyDef.BodyType.StaticBody, wallFixtureDef);
        
        Sprite test = new Sprite(400, 200, this.resourcesManager.testGamePlayer, this.vbom);
	this.testBody = PhysicsFactory.createBoxBody(this.physicWorld, test, BodyDef.BodyType.DynamicBody, wallFixtureDef);
        test.setUserData(this.testBody);
        
        attachChild(ground);
        attachChild(test);
        
        this.physicWorld.registerPhysicsConnector(new PhysicsConnector(test, this.testBody, true, false));
        
        this.levelReaderAction = new ITimerCallback(){                      
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler){
                if(!GameLevelScene.this.level.hasNext()){
                    //End of level
                    GameLevelScene.this.unregisterUpdateHandler(GameLevelScene.this.levelReaderHandler);
                }
                else{
                    LevelElement next = GameLevelScene.this.level.getNext();
                    switch(next.getType()){
                        case TRAP_JUMP:
                            Sprite trap = new Sprite(800, 75, GameLevelScene.this.resourcesManager.testGamePlayer, GameLevelScene.this.vbom);
                            Body trapBody = PhysicsFactory.createBoxBody(GameLevelScene.this.physicWorld, trap, BodyDef.BodyType.KinematicBody, wallFixtureDef);
                            trap.setUserData(trapBody);
                            GameLevelScene.this.physicWorld.registerPhysicsConnector(new PhysicsConnector(trap, trapBody, true, false));
                            attachChild(trap);
                            trapBody.setLinearVelocity(new Vector2(-5, 0));
                            break;
                    }
                }
            }
        };
        
        this.setOnSceneTouchListener(this);
    }
    
    public void start(){
        this.level.start();
        this.registerUpdateHandler(this.levelReaderHandler = new TimerHandler(5, true, this.levelReaderAction));
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
    public SceneType getSceneType() {
        return SceneType.SCENE_GAME_LEVEL;
    }

    @Override
    public void disposeScene() {
        this.detachSelf();
        this.dispose();
    }

    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionDown()){
            this.testBody.setLinearVelocity(new Vector2(0, 10));
        }
        return false;
    }
    
}
