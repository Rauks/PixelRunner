/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.game.runner.base.BaseSplashScene;
import org.game.runner.manager.AudioManager;
import org.game.runner.manager.SceneManager;
import org.game.runner.manager.SceneManager.SceneType;

/**
 *
 * @author Karl
 */
public class SplashEndScene extends BaseSplashScene implements IOnSceneTouchListener{
    private Text title;
    
    @Override
    public void createScene() {
        super.createScene();
        this.title = new Text(this.camera.getWidth()/2, this.camera.getHeight()/2, resourcesManager.fontPixel_60, "PRESS TO START !", vbom);
        attachChild(this.title);
        this.setOnSceneTouchListener(this);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_SPLASH_END;
    }

    @Override
    public void disposeScene() {
        super.disposeScene();
        this.title.detachSelf();
        this.title.dispose();
        this.detachSelf();
        this.dispose();
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionUp()){
            SceneManager.getInstance().createMainMenuScene();
            SceneManager.getInstance().disposeSplashEndScene();
            AudioManager.getInstance().play("mfx/", "menu.xm");
        }
    return false;
    }
    
}
