/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.base;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;

/**
 *
 * @author Karl
 */
public abstract class BaseSplashScene extends BaseScene{
    private Sprite headphones;
    private Text advice;
    
    @Override
    public void createScene() {
        this.setBackground(new Background(Color.BLACK));
        this.headphones = new Sprite(170, 60, this.resourcesManager.splashHeadphones, this.vbom);
        this.headphones.setScale(4);
        this.advice = new Text(434, 65, resourcesManager.fontPixel_34, "FOR MAXIMUM AWESOME,\nHEADPHONES RECOMMENDED.", new TextOptions(HorizontalAlign.CENTER), vbom);
        attachChild(this.headphones);
        attachChild(this.advice);
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
    public void disposeScene() {
        this.headphones.detachSelf();
        this.headphones.dispose();
        this.advice.detachSelf();
        this.advice.dispose();
    }
}
