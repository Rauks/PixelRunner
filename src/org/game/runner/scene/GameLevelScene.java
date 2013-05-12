/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import com.badlogic.gdx.math.Vector2;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
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
    
    @Override
    public void createScene() {
        this.physicWorld = new PhysicsWorld(new Vector2(0, -1), false);
        
        this.setBackground(new Background(Color.BLACK));
    }

    @Override
    public void onBackKeyPressed() {
        AudioManager.getInstance().stop();
        SceneManager.getInstance().unloadGameLevelScene();
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onResume() {
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
        return false;
    }
    
}
