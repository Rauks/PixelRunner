/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.scene;

import android.util.Log;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.kirauks.pixelrunner.manager.SceneManager;
import net.kirauks.pixelrunner.manager.SceneManager.SceneType;
import net.kirauks.pixelrunner.scene.base.BaseListMenuScene;
import net.kirauks.pixelrunner.scene.base.element.ListElement;

/**
 *
 * @author Karl
 */
public class BonusChoiceScene extends BaseListMenuScene{
    public BonusChoiceScene(){
        super();
        try {
            String[] fileNames = this.activity.getAssets().list("mfx/game");
            for(final String name : fileNames){    
                ListElement file = new ListElement(name, this.vbom);
                this.addListElement(file);
            }
        } catch (IOException ex) {
            Logger.getLogger(BonusChoiceScene.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onPause() {
        /* Override auto audio pause to continue playback on phone lock */
    }

    @Override
    public void onResume() {
        /* Override auto audio pause to continue playback on phone lock */
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

    @Override
    public void onElementAction(ListElement element) {
        Log.d("MENU", "TOUCH");
        BonusChoiceScene.this.audioManager.stop();
        BonusChoiceScene.this.audioManager.play("mfx/game/", element.getName());
    }
}
