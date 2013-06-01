/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.scene;

import net.kirauks.pixelrunner.R;
import net.kirauks.pixelrunner.manager.SceneManager;
import net.kirauks.pixelrunner.manager.SceneManager.SceneType;
import net.kirauks.pixelrunner.scene.base.BaseListMenuScene;
import net.kirauks.pixelrunner.scene.base.element.ListElement;
import net.kirauks.pixelrunner.scene.base.element.SuccessListElement;

/**
 *
 * @author Karl
 */
public class BonusSuccessScene extends BaseListMenuScene{
    public BonusSuccessScene(){
        this.addSuccess(R.string.success_tutorials, R.string.success_tutorials_sub);
        this.addSuccess(R.string.success_mountains, R.string.success_mountains_sub);
        this.addSuccess(R.string.success_desert, R.string.success_desert_sub);
        this.addSuccess(R.string.success_city, R.string.success_city_sub);
        this.addSuccess(R.string.success_forest, R.string.success_forest_sub);
        this.addSuccess(R.string.success_hills, R.string.success_hills_sub);
        this.addSuccess(R.string.success_sweets, R.string.success_sweets_sub);
        this.addSuccess(R.string.success_endgame, R.string.success_endgame_sub);
        this.addSuccess(R.string.success_jukebox, R.string.success_jukebox_sub);
        this.addSuccess(R.string.success_arcade_bronze, R.string.success_arcade_bronze_sub);
        this.addSuccess(R.string.success_arcade_silver, R.string.success_arcade_silver_sub);
        this.addSuccess(R.string.success_arcade_gold, R.string.success_arcade_gold_sub);
        this.addSuccess(R.string.success_arcade_crazy, R.string.success_arcade_crazy_sub);
        this.addSuccess(R.string.success_arcade_mordor, R.string.success_arcade_mordor_sub);
        this.addSuccess(R.string.success_arcade_wtf, R.string.success_arcade_wtf_sub);
        this.addSuccess(R.string.success_arcade_roll, R.string.success_arcade_roll_sub);
        this.addSuccess(R.string.success_nyan, R.string.success_nyan_sub);
    }
    
    private void addSuccess(int redIdName, int resIdSub){
        this.addListElement(new SuccessListElement(this.activity.getString(redIdName), this.activity.getString(resIdSub), this.vbom));
    }

    @Override
    protected void onListMove(float newPos, float minPos, float maxPos) {
        
    }

    @Override
    public void onElementAction(ListElement element) {
        
    }

    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().createBonusChoiceScene();
        SceneManager.getInstance().disposeBonusSuccessScene();
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_BONUS_SUCCESS;
    }
    
}
