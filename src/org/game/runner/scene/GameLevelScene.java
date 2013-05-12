/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
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
    
    @Override
    public void createScene() {
        this.setBackground(new Background(Color.BLACK));
        
        this.physicWorld = new FixedStepPhysicsWorld(30, new Vector2(0, -20), false);
        this.registerUpdateHandler(this.physicWorld);
        
        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0);
        
        Rectangle ground = new Rectangle(400, 50, 800, 10, this.vbom);
	this.groundBody = PhysicsFactory.createBoxBody(this.physicWorld, ground, BodyDef.BodyType.StaticBody, wallFixtureDef);
        
        Sprite test = new Sprite(400, 200, this.resourcesManager.testGamePlayer, this.vbom);
	this.testBody = PhysicsFactory.createBoxBody(this.physicWorld, test, BodyDef.BodyType.DynamicBody, wallFixtureDef);
        test.setUserData(this.testBody);
        
        attachChild(ground);
        
        attachChild(test);
        this.physicWorld.registerPhysicsConnector(new PhysicsConnector(test, this.testBody, true, false));
        
        this.setOnSceneTouchListener(this);
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
