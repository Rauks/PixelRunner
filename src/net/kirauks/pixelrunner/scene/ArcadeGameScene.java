/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.scene;

import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.adt.align.HorizontalAlign;
import net.kirauks.pixelrunner.game.descriptor.LevelDescriptor;
import net.kirauks.pixelrunner.game.Trail;
import net.kirauks.pixelrunner.manager.AudioManager;
import net.kirauks.pixelrunner.manager.SceneManager.SceneType;
import net.kirauks.pixelrunner.manager.db.ArcadeScoreDatabase;
import net.kirauks.pixelrunner.scene.base.BaseGameScene;

/**
 *
 * @author Karl
 */
public class ArcadeGameScene extends BaseGameScene{
    private long score = 0;
    private boolean countScore = false;
    private Text scoreText;
    
    private ArcadeScoreDatabase scoreDb;
    
    private long highScore;
    private Text highScoreText;
    private boolean isHigtscoring = false;
    
    public ArcadeGameScene(LevelDescriptor level){
        super(level);
        
        AudioManager.getInstance().prepare("mfx/game/", "arcade-win.xm");
        
        this.scoreDb = new ArcadeScoreDatabase(this.activity);
        this.createScoreOnHud();
    }
    
    private void createScoreOnHud(){
        this.highScore = this.loadHighScore();
        
        this.scoreText = new Text(20, 415, resourcesManager.fontPixel_60, "0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
        this.scoreText.setAnchorCenter(0, 0);
        this.scoreText.setText("0");
        this.hud.attachChild(this.scoreText);
        this.highScoreText = new Text(20, 415, resourcesManager.fontPixel_60_gray, "0123456789/", new TextOptions(HorizontalAlign.LEFT), vbom);
        this.highScoreText.setAnchorCenter(0, 0);
        this.highScoreText.setText("/" + String.valueOf(this.highScore/5));
        this.highScoreText.setPosition(this.scoreText.getWidth(), 0);
        this.scoreText.attachChild(this.highScoreText);
    }
    
    private void addScore(int score){
        if(this.countScore){
            this.score += score;
            this.scoreText.setText(String.valueOf(this.score/5));
            this.highScoreText.setPosition(this.scoreText.getWidth(), 0);
            if(this.highScore < this.score){
                if(!this.isHigtscoring){
                    this.isHigtscoring = true;
                    AudioManager.getInstance().stop();
                    AudioManager.getInstance().play("mfx/game/", "arcade-win.xm");
                    this.playerTrail.setColorMode(Trail.ColorMode.MULTICOLOR);
                }
                this.highScore = this.score;
                this.highScoreText.setText("/" + String.valueOf(this.highScore/5));
            }
        }
    }
    private void resetScore(){
        this.isHigtscoring = false;
        this.score = 0;
        this.scoreText.setText("0");
        this.highScoreText.setPosition(this.scoreText.getWidth(), 0);
        this.highScoreText.setText("/" + String.valueOf(this.highScore/5));
    }
    
    private void saveHighScore() {
        this.scoreDb.set(this.highScore);
    }
    private long loadHighScore() {
        return this.scoreDb.get();
    }
    
    @Override
    public void onBackKeyPressed() {
        this.saveHighScore();
        super.onBackKeyPressed();
    }

    @Override
    public void onPause() {
        this.saveHighScore();
        super.onPause();
    }
    
    
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        this.addScore((int)(pSecondsElapsed*100));
    }
    
    @Override
    public void disposeScene() {
        this.scoreText.detachSelf();
        this.scoreText.dispose();
        this.highScoreText.detachSelf();
        this.highScoreText.dispose();
        super.disposeScene();
    }

    @Override
    protected void onRestartBegin() {
        this.countScore = false;
        this.saveHighScore();
    }

    @Override
    protected void onRestartEnd() {
    }

    @Override
    protected void onStartBegin() {
        this.playerTrail.setColorMode(Trail.ColorMode.NORMAL);
        this.resetScore();
    }

    @Override
    protected void onStartEnd() {
        this.countScore = true;
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_GAME_ARCADE;
    }

    @Override
    protected void onWin() {
        /* Never called in arcade */
    }
}
