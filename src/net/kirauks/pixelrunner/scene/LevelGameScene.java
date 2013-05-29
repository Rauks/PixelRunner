/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.scene;

import net.kirauks.pixelrunner.game.descriptor.LevelDescriptor;
import net.kirauks.pixelrunner.manager.SceneManager.SceneType;
import net.kirauks.pixelrunner.manager.db.ProgressDatabase;
import net.kirauks.pixelrunner.scene.base.BaseGameScene;

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
