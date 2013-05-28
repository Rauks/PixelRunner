/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import android.content.Context;
import android.content.SharedPreferences;
import org.game.runner.game.descriptor.LevelDescriptor;
import org.game.runner.manager.SceneManager.SceneType;
import org.game.runner.manager.db.ArcadeScoreDatabase;
import org.game.runner.manager.db.ProgressDatabase;
import org.game.runner.scene.base.BaseGameScene;

/**
 *
 * @author Karl
 */
public class LevelGameScene extends BaseGameScene{
    private ProgressDatabase progressDb;
    
    public LevelGameScene(LevelDescriptor level){
        super(level);
        
        this.progressDb = new ProgressDatabase(this.activity);
    }
    
    @Override
    protected void onRestartBegin() {
        
    }

    @Override
    protected void onRestartEnd() {
        
    }

    @Override
    protected void onStartBegin() {
        
    }

    @Override
    protected void onStartEnd() {
        
    }

    @Override
    protected void onWin() {
        if(this.progressDb.get(this.level.getWorld()) == this.level.getLevelId()){
            this.progressDb.set(this.level.getWorld(), this.level.getLevelId() + 1);
        }
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_GAME_LEVEL;
    }
}
