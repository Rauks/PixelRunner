/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import org.andengine.entity.text.Text;
import org.game.runner.base.BaseSplashScene;
import org.game.runner.manager.SceneManager.SceneType;

/**
 *
 * @author Karl
 */
public class SplashScene extends BaseSplashScene{
    private Text title;
    
    @Override
    public void createScene() {
        super.createScene();
        this.title = new Text(this.camera.getWidth()/2, this.camera.getHeight()/2, resourcesManager.fontPixel_60, "LOADING...", vbom);
        attachChild(this.title);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_SPLASH;
    }

    @Override
    public void disposeScene() {
        super.disposeScene();
        this.title.detachSelf();
        this.title.dispose();
        this.detachSelf();
        this.dispose();
    }
    
}
