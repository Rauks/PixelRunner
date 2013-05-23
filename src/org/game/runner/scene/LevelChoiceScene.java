/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import org.game.runner.base.BaseMenuScene;
import org.game.runner.manager.SceneManager;
import org.game.runner.manager.SceneManager.SceneType;

/**
 *
 * @author Karl
 */
public class LevelChoiceScene extends BaseMenuScene{

    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().createMainMenuScene();
        SceneManager.getInstance().disposeLevelChoiceScene();
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_LEVEL_CHOICE;
    }
    
}
