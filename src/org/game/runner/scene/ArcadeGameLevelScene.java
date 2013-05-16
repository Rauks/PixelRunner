/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLES20;
import android.util.FloatMath;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.GravityParticleInitializer;
import org.andengine.entity.particle.initializer.IParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.IParticleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.game.runner.game.LevelDescriptor;
import org.game.runner.game.player.Trail;
import org.game.runner.manager.AudioManager;
import org.game.runner.manager.SceneManager;
import org.game.runner.manager.SceneManager.SceneType;

/**
 *
 * @author Karl
 */
public class ArcadeGameLevelScene extends GameLevelScene{
    private static final String HIGHSCORE_DB_NAME = "highscore";
    private static final String HIGHSCORE_LABEL = "score";
    
    private long score = 0;
    private boolean countScore = false;
    private Text scoreText;
    
    private SharedPreferences scoreDb;
    private SharedPreferences.Editor scoreDbEditor;
    private long highScore;
    private Text highScoreText;
    private boolean isHigtscoring = false;
    
    public ArcadeGameLevelScene(LevelDescriptor level){
        super(level);
        
        AudioManager.getInstance().prepare("mfx/", "arcade_win.xm");
        
        this.scoreDb = this.activity.getSharedPreferences(HIGHSCORE_DB_NAME, Context.MODE_PRIVATE);
        this.scoreDbEditor = this.scoreDb.edit();
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
                    AudioManager.getInstance().play("mfx/", "arcade_win.xm");
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
    }
    
    private boolean saveHighScore() {
        this.scoreDbEditor.putLong(HIGHSCORE_LABEL, this.highScore);
        return this.scoreDbEditor.commit();
    }
    private long loadHighScore() {
            return this.scoreDb.getLong(HIGHSCORE_LABEL, 0);
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
        this.countScore = true;
    }

    @Override
    protected void onStartBegin() {
        this.playerTrail.setColorMode(Trail.ColorMode.WHITE);
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
}
