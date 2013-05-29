/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.scene;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import net.kirauks.pixelrunner.GameActivity;
import org.game.runner.R;
import net.kirauks.pixelrunner.scene.base.BaseSplashScene;
import net.kirauks.pixelrunner.manager.AudioManager;
import net.kirauks.pixelrunner.manager.ResourcesManager;
import net.kirauks.pixelrunner.manager.SceneManager;
import net.kirauks.pixelrunner.manager.SceneManager.SceneType;

/**
 *
 * @author Karl
 */
public class SplashEndScene extends BaseSplashScene implements IOnSceneTouchListener{
    private Text title;
    
    @Override
    public void createScene() {
        super.createScene();
        this.title = new Text(GameActivity.CAMERA_WIDTH/2, GameActivity.CAMERA_HEIGHT/2, resourcesManager.fontPixel_60, this.activity.getString(R.string.splash_touch), vbom);
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
            ResourcesManager.getInstance().unloadSplashResources();
            SceneManager.getInstance().disposeSplashScene();
            AudioManager.getInstance().play("mfx/main/", "menu.xm");
        }
    return false;
    }
    
}
