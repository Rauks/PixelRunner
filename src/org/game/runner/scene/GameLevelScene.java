/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.game.runner.base.BaseScene;
import org.game.runner.manager.AudioManager;
import org.game.runner.manager.SceneManager;
import org.game.runner.manager.SceneManager.SceneType;

/**
 *
 * @author Karl
 */
public class GameLevelScene extends BaseScene implements IOnSceneTouchListener{

    @Override
    public void createScene() {
        
    }

    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().unloadGameLevelScene();
        AudioManager.getInstance().play("mfx/", "menu.xm");
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
