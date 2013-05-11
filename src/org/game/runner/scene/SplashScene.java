/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.adt.color.Color;
import org.game.runner.base.BaseScene;
import org.game.runner.manager.SceneManager.SceneType;

/**
 *
 * @author Karl
 */
public class SplashScene extends BaseScene{
    private AutoParallaxBackground autoParallaxBackground;
    private Sprite parallaxLayer1;
    private Sprite parallaxLayer2;
    private Sprite parallaxLayer3;
    private Sprite parallaxLayer4;
    
    
    @Override
    public void createScene() {
        this.createBackground();
        this.audioManager.play("mfx/", "menu.xm");
    }
    
    private void createBackground(){
        this.autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 10);
        this.autoParallaxBackground.setColor(Color.BLACK);
        this.parallaxLayer1 = new Sprite(0, this.camera.getHeight()/2, this.resourcesManager.splashParallaxLayer1, this.vbom);
        this.parallaxLayer2 = new Sprite(0, this.camera.getHeight()/2, this.resourcesManager.splashParallaxLayer2, this.vbom);
        this.parallaxLayer3 = new Sprite(0, this.camera.getHeight()/2, this.resourcesManager.splashParallaxLayer3, this.vbom);
        this.parallaxLayer4 = new Sprite(0, this.camera.getHeight()/2, this.resourcesManager.splashParallaxLayer4, this.vbom);
        this.autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-10.0f, this.parallaxLayer1));
        this.autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-15.0f, this.parallaxLayer2));
        this.autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-20.0f, this.parallaxLayer3));
        this.autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-25.0f, this.parallaxLayer4));
        this.setBackground(autoParallaxBackground);
    }

    @Override
    public void onBackKeyPressed() {
        
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
        return SceneType.SCENE_SPLASH;
    }

    @Override
    public void disposeScene() {
        this.parallaxLayer1.detachSelf();
        this.parallaxLayer1.dispose();
        this.parallaxLayer2.detachSelf();
        this.parallaxLayer2.dispose();
        this.parallaxLayer3.detachSelf();
        this.parallaxLayer3.dispose();
        this.parallaxLayer4.detachSelf();
        this.parallaxLayer4.dispose();
        this.detachSelf();
        this.dispose();
    }
    
}
