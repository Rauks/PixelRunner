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
import net.kirauks.pixelrunner.game.Player;
import net.kirauks.pixelrunner.manager.AudioManager;
import net.kirauks.pixelrunner.manager.SceneManager;
import net.kirauks.pixelrunner.scene.base.BaseListMenuScene;
import net.kirauks.pixelrunner.scene.base.element.ListElement;
import net.kirauks.pixelrunner.scene.base.element.XmAudioListElement;
import net.kirauks.pixelrunner.scene.base.utils.comparator.AlphanumComparator;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.AnimatedSprite;

/**
 *
 * @author Karl
 */
public class BonusJukeboxScene extends BaseListMenuScene{
    private final AnimatedSprite playerDance;
    
    public BonusJukeboxScene(){
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
        this.setListX(0);
        
        this.playerDance = new AnimatedSprite(200, 280, this.resourcesManager.player, this.vbom);
        this.playerDance.animate(Player.PLAYER_ANIMATE_DANCE, Player.PLAYER_ANIMATE_DANCE_FRAMES);
        this.playerDance.setCullingEnabled(true);
        this.playerDance.setScale(4);
        this.attachChild(this.playerDance);
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
        this.audioManager.stop();
        this.audioManager.play("mfx/main/", "menu.xm");
        SceneManager.getInstance().createBonusChoiceScene();
        SceneManager.getInstance().disposeBonusJukeboxScene();
    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_BONUS_JUKEBOX;
    }

    @Override
    public void onElementAction(ListElement element) {
        this.audioManager.stop();
        this.audioManager.play("mfx/game/", ((XmAudioListElement)element).getXmFileName());
    }
    
    @Override
    public void disposeScene() {
        super.disposeScene();
        this.playerDance.detachSelf();
        this.playerDance.dispose();
        this.detachSelf();
        this.dispose();
    }
}