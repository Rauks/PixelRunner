/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.scene;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.kirauks.pixelrunner.GameActivity;
import net.kirauks.pixelrunner.R;
import net.kirauks.pixelrunner.game.descriptor.ArcadeLevelDescriptor;
import net.kirauks.pixelrunner.manager.AudioManager;
import net.kirauks.pixelrunner.manager.SceneManager;
import net.kirauks.pixelrunner.manager.SceneManager.SceneType;
import net.kirauks.pixelrunner.scene.base.BaseListMenuScene;
import net.kirauks.pixelrunner.scene.base.BaseMenuScene;
import net.kirauks.pixelrunner.scene.base.element.ListElement;
import net.kirauks.pixelrunner.scene.base.element.XmAudioListElement;
import net.kirauks.pixelrunner.scene.base.utils.comparator.AlphanumComparator;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.text.Text;

/**
 *
 * @author Karl
 */
public class BonusChoiceScene extends BaseMenuScene implements MenuScene.IOnMenuItemClickListener {
    private MenuScene menuChildScene;
    private final int MENUID_JUKEBOX = 0;
    private final int MENUID_ACHIEVEMENTS = 1;

    @Override
    public void createScene() {
        super.createScene();
        this.createMenuChildScene();
        attachChild(new Text(GameActivity.CAMERA_WIDTH/2, GameActivity.CAMERA_HEIGHT/2 + 170, resourcesManager.fontPixel_100, this.activity.getString(R.string.menu_bonus), vbom));
    }
    
    private void createMenuChildScene(){
        this.menuChildScene = new MenuScene(this.camera);
        this.menuChildScene.setPosition(0, 0);
        
        final IMenuItem jukeMenuItem = new ScaleMenuItemDecorator(new TextMenuItem(MENUID_JUKEBOX, this.resourcesManager.fontPixel_60, this.activity.getString(R.string.menu_bonus_jukebox), vbom), 1.4f, 1);
        final IMenuItem successMenuItem = new ScaleMenuItemDecorator(new TextMenuItem(MENUID_ACHIEVEMENTS, this.resourcesManager.fontPixel_60, this.activity.getString(R.string.menu_bonus_success), vbom), 1.4f, 1);

        this.menuChildScene.addMenuItem(jukeMenuItem);
        this.menuChildScene.addMenuItem(successMenuItem);

        this.menuChildScene.buildAnimations();
        this.menuChildScene.setBackgroundEnabled(false);

        jukeMenuItem.setPosition(jukeMenuItem.getX(), jukeMenuItem.getY() - 35);
        successMenuItem.setPosition(successMenuItem.getX(), successMenuItem.getY() - 55);

        this.menuChildScene.setOnMenuItemClickListener(this);

        setChildScene(menuChildScene);
    }
    
    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
        switch(pMenuItem.getID()){
            case MENUID_JUKEBOX:
                SceneManager.getInstance().createBonusJukeboxScene();
                AudioManager.getInstance().stop();
                SceneManager.getInstance().disposeBonusChoiceScene();
                return true;
            case MENUID_ACHIEVEMENTS:
                
                return true;
            default:
                return false;
        }
    }
    
    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().createMainMenuScene();
        SceneManager.getInstance().disposeBonusChoiceScene();
    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_BONUS_CHOICE;
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