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
import net.kirauks.pixelrunner.manager.SceneManager;
import net.kirauks.pixelrunner.manager.SceneManager.SceneType;
import net.kirauks.pixelrunner.scene.base.BaseListMenuScene;
import net.kirauks.pixelrunner.scene.base.element.ListElement;
import net.kirauks.pixelrunner.scene.base.element.XmAudioListElement;
import net.kirauks.pixelrunner.scene.base.utils.comparator.AlphanumComparator;

/**
 *
 * @author Karl
 */
public class BonusChoiceScene extends BaseListMenuScene{
    public BonusChoiceScene(){
        super();
        try {
            List<String> fileNames = Arrays.asList(this.activity.getAssets().list("mfx/game"));
            Collections.sort(fileNames, new AlphanumComparator());
            for(final String name : fileNames){    
                ListElement file = new XmAudioListElement(name.substring(0, name.length() - 3), name, this.vbom);
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
        BonusChoiceScene.this.audioManager.stop();
        BonusChoiceScene.this.audioManager.play("mfx/game/", ((XmAudioListElement)element).getXmFileName());
    }
}
