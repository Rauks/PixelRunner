/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.scene;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.text.Text;
import net.kirauks.pixelrunner.GameActivity;
import net.kirauks.pixelrunner.R;
import net.kirauks.pixelrunner.scene.base.BaseMenuScene;
import net.kirauks.pixelrunner.game.descriptor.ArcadeLevelDescriptor;
import net.kirauks.pixelrunner.manager.AudioManager;
import net.kirauks.pixelrunner.manager.SceneManager;
import net.kirauks.pixelrunner.manager.SceneManager.SceneType;

/**
 *
 * @author Karl
 */
public class MainMenuScene extends BaseMenuScene implements MenuScene.IOnMenuItemClickListener {
    private MenuScene menuChildScene;
    private final int MENUID_PLAY = 0;
    private final int MENUID_ARCADE = 1;
    private final int MENUID_BONUS = 2;
    private final int MENUID_CREDITS = 3;

    @Override
    public void createScene() {
        super.createScene();
        this.createMenuChildScene();
        attachChild(new Text(GameActivity.CAMERA_WIDTH/2, GameActivity.CAMERA_HEIGHT/2 + 170, resourcesManager.fontPixel_100, this.activity.getString(R.string.app_name), vbom));
    }
    
    private void createMenuChildScene(){
        this.menuChildScene = new MenuScene(this.camera);
        this.menuChildScene.setPosition(0, 0);
        
        final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new TextMenuItem(MENUID_PLAY, this.resourcesManager.fontPixel_60, this.activity.getString(R.string.menu_play), vbom), 1.4f, 1);
        final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new TextMenuItem(MENUID_ARCADE, this.resourcesManager.fontPixel_60, this.activity.getString(R.string.menu_arcade), vbom), 1.4f, 1);
        final IMenuItem bonusMenuItem = new ScaleMenuItemDecorator(new TextMenuItem(MENUID_BONUS, this.resourcesManager.fontPixel_60, this.activity.getString(R.string.menu_bonus), vbom), 1.4f, 1);
        final IMenuItem creditsMenuItem = new ScaleMenuItemDecorator(new TextMenuItem(MENUID_CREDITS, this.resourcesManager.fontPixel_60, this.activity.getString(R.string.menu_credits), vbom), 1.4f, 1);

        this.menuChildScene.addMenuItem(playMenuItem);
        this.menuChildScene.addMenuItem(optionsMenuItem);
        this.menuChildScene.addMenuItem(bonusMenuItem);
        this.menuChildScene.addMenuItem(creditsMenuItem);

        this.menuChildScene.buildAnimations();
        this.menuChildScene.setBackgroundEnabled(false);

        playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY() -15);
        optionsMenuItem.setPosition(optionsMenuItem.getX(), optionsMenuItem.getY() - 35);
        bonusMenuItem.setPosition(bonusMenuItem.getX(), bonusMenuItem.getY() - 55);
        creditsMenuItem.setPosition(creditsMenuItem.getX(), creditsMenuItem.getY() - 75);

        this.menuChildScene.setOnMenuItemClickListener(this);

        setChildScene(menuChildScene);
    }
    
    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
        switch(pMenuItem.getID()){
            case MENUID_PLAY:
                SceneManager.getInstance().createLevelChoiceScene();
                SceneManager.getInstance().disposeMainMenuScene();
                return true;
            case MENUID_ARCADE:
                AudioManager.getInstance().stop();
                SceneManager.getInstance().loadGameLevelScene(SceneType.SCENE_GAME_ARCADE, new ArcadeLevelDescriptor());
                return true;
            case MENUID_BONUS:
                AudioManager.getInstance().stop();
                SceneManager.getInstance().loadBonusChoiceScene();
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
