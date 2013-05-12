/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
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
public class SplashEndScene extends BaseScene implements IOnSceneTouchListener{
    private Sprite headphones;

    @Override
    public void createScene() {
        this.setBackground(new Background(Color.BLACK));
        attachChild(new Text(this.camera.getWidth()/2, this.camera.getHeight()/2, resourcesManager.fontPixel_60, "PRESS TO START !", vbom));
        this.headphones = new Sprite(170, 60, this.resourcesManager.splashHeadphones, this.vbom);
        this.headphones.setScale(4);
        attachChild(this.headphones);
        attachChild(new Text(434, 70, resourcesManager.fontPixel_34, "FOR MAXIMUM AWESOME,", vbom));
        attachChild(new Text(434, 40, resourcesManager.fontPixel_34, "HEADPHONES RECOMMENDED.", vbom));
        
        this.setOnSceneTouchListener(this);
    }

    @Override
    public void onBackKeyPressed() {
        System.exit(0);
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_SPLASH_END;
    }

    @Override
    public void disposeScene() {
        this.headphones.detachSelf();
        this.headphones.dispose();
        this.detachSelf();
        this.dispose();
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionDown()){
            SceneManager.getInstance().createMainMenuScene();
            SceneManager.getInstance().disposeSplashEndScene();
            AudioManager.getInstance().play("mfx/", "menu.xm");
        }
    return false;
    }
    
}
