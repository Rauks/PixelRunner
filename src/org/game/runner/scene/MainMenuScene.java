/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;
import org.game.runner.base.BaseScene;
import org.game.runner.manager.AudioManager;
import org.game.runner.manager.SceneManager;

/**
 *
 * @author Karl
 */
public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener {
    private AutoParallaxBackground autoParallaxBackground;
    private Sprite parallaxLayer1;
    private Sprite parallaxLayer2;
    private Sprite parallaxLayer3;
    private Sprite parallaxLayer4;
    
    private MenuScene menuChildScene;
    private final int MENUID_PLAY = 0;
    private final int MENUID_OPTIONS = 1;
    private final int MENUID_CREDITS = 2;

    @Override
    public void createScene() {
        this.createBackground();
        this.createMenuChildScene();
        attachChild(new Text(this.camera.getWidth()/2, this.camera.getHeight()/2 + 150, resourcesManager.fontPixel_100, "PIXEL RUNNER", vbom));
        this.audioManager.play("mfx/", "menu.xm");
    }
    
    private void createBackground(){
        this.autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
        this.setBackground(autoParallaxBackground);
        this.parallaxLayer1 = new Sprite(0, 0, this.resourcesManager.mainMenuParallaxLayer1, this.vbom);
        this.parallaxLayer2 = new Sprite(0, 0, this.resourcesManager.mainMenuParallaxLayer2, this.vbom);
        this.parallaxLayer3 = new Sprite(0, 0, this.resourcesManager.mainMenuParallaxLayer3, this.vbom);
        this.parallaxLayer4 = new Sprite(0, 0, this.resourcesManager.mainMenuParallaxLayer4, this.vbom);
        this.parallaxLayer1.setOffsetCenter(0, 0);
        this.parallaxLayer2.setOffsetCenter(0, 0);
        this.parallaxLayer3.setOffsetCenter(0, 0);
        this.parallaxLayer4.setOffsetCenter(0, 0);
        this.autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-10.0f, this.parallaxLayer1));
        this.autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-15.0f, this.parallaxLayer2));
        this.autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-20.0f, this.parallaxLayer3));
        this.autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-25.0f, this.parallaxLayer4));
    }
    
    private void createMenuChildScene(){
        this.menuChildScene = new MenuScene(this.camera);
        this.menuChildScene.setPosition(0, 0);
        
        final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new TextMenuItem(MENUID_PLAY, this.resourcesManager.fontPixel_60, "PLAY", vbom), 1.4f, 1);
        final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new TextMenuItem(MENUID_OPTIONS, this.resourcesManager.fontPixel_60, "OPTIONS", vbom), 1.4f, 1);
        final IMenuItem creditsMenuItem = new ScaleMenuItemDecorator(new TextMenuItem(MENUID_CREDITS, this.resourcesManager.fontPixel_60, "CREDITS", vbom), 1.4f, 1);

        this.menuChildScene.addMenuItem(playMenuItem);
        this.menuChildScene.addMenuItem(optionsMenuItem);
        this.menuChildScene.addMenuItem(creditsMenuItem);

        this.menuChildScene.buildAnimations();
        this.menuChildScene.setBackgroundEnabled(false);

        playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY() - 10);
        optionsMenuItem.setPosition(optionsMenuItem.getX(), optionsMenuItem.getY() - 40);
        creditsMenuItem.setPosition(creditsMenuItem.getX(), creditsMenuItem.getY() - 70);

        this.menuChildScene.setOnMenuItemClickListener(this);

        setChildScene(menuChildScene);
    }
    
    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
        switch(pMenuItem.getID()){
            case MENUID_PLAY:
                return true;
            case MENUID_OPTIONS:
                return true;
            case MENUID_CREDITS:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackKeyPressed() {
        AudioManager.getInstance().stop();
        System.exit(0);    
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
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_MENU;
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
