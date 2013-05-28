/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import org.game.runner.game.descriptor.LevelDescriptor;
import org.game.runner.manager.SceneManager.SceneType;
import org.game.runner.scene.base.BaseGameScene;

/**
 *
 * @author Karl
 */
public class LevelGameScene extends BaseGameScene{
    public LevelGameScene(LevelDescriptor level){
        super(level);
    }
    
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
        
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_GAME_LEVEL;
    }
}
