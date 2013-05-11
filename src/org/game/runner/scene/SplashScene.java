/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;
import org.game.runner.base.BaseScene;
import org.game.runner.manager.SceneManager.SceneType;

/**
 *
 * @author Karl
 */
public class SplashScene extends BaseScene{
    @Override
    public void createScene() {
        this.setBackground(new Background(Color.BLACK));
        attachChild(new Text(this.camera.getWidth()/2, this.camera.getHeight()/2, resourcesManager.fontPixel_60, "LOADING...", vbom));
    }

    @Override
    public void onBackKeyPressed() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_SPLASH;
    }

    @Override
    public void disposeScene() {
        this.detachSelf();
        this.dispose();
    }
    
}
