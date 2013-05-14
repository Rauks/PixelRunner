/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.text.Text;
import org.game.runner.base.BaseMenuScene;
import org.game.runner.game.ArcadeLevelDescriptor;
import org.game.runner.manager.AudioManager;
import org.game.runner.manager.SceneManager;

/**
 *
 * @author Karl
 */
public class MainMenuScene extends BaseMenuScene implements MenuScene.IOnMenuItemClickListener {
    private MenuScene menuChildScene;
    private final int MENUID_PLAY = 0;
    private final int MENUID_ARCADE = 1;
    private final int MENUID_CREDITS = 2;

    @Override
    public void createScene() {
        super.createScene();
        this.createMenuChildScene();
        attachChild(new Text(this.camera.getWidth()/2, this.camera.getHeight()/2 + 150, resourcesManager.fontPixel_100, "PIXEL RUNNER", vbom));
    }
    
    private void createMenuChildScene(){
        this.menuChildScene = new MenuScene(this.camera);
        this.menuChildScene.setPosition(0, 0);
        
        final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new TextMenuItem(MENUID_PLAY, this.resourcesManager.fontPixel_60, "PLAY", vbom), 1.4f, 1);
        final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new TextMenuItem(MENUID_ARCADE, this.resourcesManager.fontPixel_60, "ARCADE", vbom), 1.4f, 1);
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
            case MENUID_ARCADE:
                AudioManager.getInstance().stop();
                SceneManager.getInstance().loadGameLevelScene(new ArcadeLevelDescriptor());
                return true;
            case MENUID_CREDITS:
                SceneManager.getInstance().createCreditsScene();
                SceneManager.getInstance().disposeMainMenuScene();
                return true;
            default:
                return false;
        }
    }
    
    @Override
    public void onBackKeyPressed() {
        this.audioManager.stop();
        System.exit(0);    
    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_MENU;
    }

    @Override
    public void disposeScene() {
        super.disposeScene();
        this.menuChildScene.detachSelf();
        this.menuChildScene.dispose();
        this.detachSelf();
        this.dispose();
    }
}
