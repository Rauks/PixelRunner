/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.scene;

import net.kirauks.pixelrunner.R;
import net.kirauks.pixelrunner.manager.SceneManager;
import net.kirauks.pixelrunner.manager.SceneManager.SceneType;
import net.kirauks.pixelrunner.scene.base.BaseListMenuScene;
import org.andengine.entity.text.Text;

/**
 *
 * @author Karl
 */
public class BonusChoiceScene extends BaseListMenuScene{
    public BonusChoiceScene(){
        this.addElementAtEnd(new Text(0, 0, this.resourcesManager.fontPixel_100, this.activity.getString(R.string.menu_credits), this.vbom));
        this.addElementAtEnd(new Text(0, 0, this.resourcesManager.fontPixel_100, this.activity.getString(R.string.menu_credits), this.vbom));
        this.addElementAtEnd(new Text(0, 0, this.resourcesManager.fontPixel_100, this.activity.getString(R.string.menu_credits), this.vbom));
        this.addElementAtEnd(new Text(0, 0, this.resourcesManager.fontPixel_100, this.activity.getString(R.string.menu_credits), this.vbom));
        this.addElementAtEnd(new Text(0, 0, this.resourcesManager.fontPixel_100, this.activity.getString(R.string.menu_credits), this.vbom));
        this.addElementAtEnd(new Text(0, 0, this.resourcesManager.fontPixel_100, this.activity.getString(R.string.menu_credits), this.vbom));
        this.addElementAtEnd(new Text(0, 0, this.resourcesManager.fontPixel_100, this.activity.getString(R.string.menu_credits), this.vbom));
        this.addElementAtEnd(new Text(0, 0, this.resourcesManager.fontPixel_100, this.activity.getString(R.string.menu_credits), this.vbom));
        this.addElementAtEnd(new Text(0, 0, this.resourcesManager.fontPixel_100, this.activity.getString(R.string.menu_credits), this.vbom));
        this.addElementAtEnd(new Text(0, 0, this.resourcesManager.fontPixel_100, this.activity.getString(R.string.menu_credits), this.vbom));
    }
    
    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().createMainMenuScene();
        SceneManager.getInstance().disposeBonusChoiceScene();
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_BONUS_CHOICE;
    }
}
