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
import net.kirauks.pixelrunner.R;
import net.kirauks.pixelrunner.game.Player;
import net.kirauks.pixelrunner.manager.AudioManager;
import net.kirauks.pixelrunner.manager.ResourcesManager;
import net.kirauks.pixelrunner.manager.SceneManager;
import net.kirauks.pixelrunner.scene.base.BaseListMenuScene;
import net.kirauks.pixelrunner.scene.base.element.ListElement;
import net.kirauks.pixelrunner.scene.base.element.ScrollPage;
import net.kirauks.pixelrunner.scene.base.element.XmAudioListElement;
import net.kirauks.pixelrunner.scene.base.utils.comparator.AlphanumComparator;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.align.HorizontalAlign;

/**
 *
 * @author Karl
 */
public class BonusJukeboxScene extends BaseListMenuScene{
    private final AnimatedSprite playerDance;
    private Text nowPlaying;
    private Text playing;
    private Sprite top;
    private Sprite bottom;
    
    public BonusJukeboxScene(){
        super();
        try {
            List<String> fileNames = Arrays.asList(this.activity.getAssets().list("mfx"));
            Collections.sort(fileNames, new AlphanumComparator());
            for(final String name : fileNames){    
                ListElement file = new XmAudioListElement(name.substring(0, name.length() - 3), name, this.vbom);
                this.addListElement(file);
            }
        } catch (IOException ex) {
            Logger.getLogger(BonusChoiceScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setListX(430);
        
        this.playerDance = new AnimatedSprite(190, 280, this.resourcesManager.player, this.vbom);
        this.playerDance.animate(Player.PLAYER_ANIMATE_DANCE, Player.PLAYER_ANIMATE_DANCE_FRAMES);
        this.playerDance.setCullingEnabled(true);
        this.playerDance.setScale(4);
        this.attachChild(this.playerDance);
        
        this.nowPlaying = new Text(190, 110, this.resourcesManager.fontPixel_34, this.activity.getString(R.string.jukebox_playing), this.vbom);
        this.nowPlaying.setVisible(false);
        this.attachChild(this.nowPlaying);
        this.playing = new Text(190, 80, this.resourcesManager.fontPixel_60, "0123456789", this.vbom);
        this.playing.setVisible(false);
        this.attachChild(this.playing);
        
        this.top = new Sprite(760, 450, ResourcesManager.getInstance().lvlLeft, this.vbom);
        this.top.setScale(6f);
        this.top.setRotation(90f);
        this.attachChild(this.top);
        this.bottom = new Sprite(760, 30, ResourcesManager.getInstance().lvlRight, this.vbom);
        this.bottom.setScale(6f);
        this.bottom.setRotation(90f);
        this.attachChild(this.bottom);
        this.onListMove(0, 0, 1);
    }

    @Override
    protected void onListMove(float newPos, float minPos, float maxPos) {
        if(newPos >= maxPos){
            this.bottom.setVisible(false);
        }
        else{
            this.bottom.setVisible(true);
        }
        if(newPos <= minPos){
            this.top.setVisible(false);
        }
        else{
            this.top.setVisible(true);
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
        this.audioManager.stop();
        this.audioManager.play("mfx/", "menu.xm");
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
        this.nowPlaying.setVisible(true);
        this.playing.setVisible(true);
        this.playing.setText(element.getName());
        this.audioManager.play("mfx/", ((XmAudioListElement)element).getXmFileName());
    }
    
    @Override
    public void disposeScene() {
        super.disposeScene();
        this.playerDance.detachSelf();
        this.playerDance.dispose();
        this.nowPlaying.detachSelf();
        this.nowPlaying.dispose();
        this.playing.detachSelf();
        this.playing.dispose();
        this.detachSelf();
        this.dispose();
    }
}