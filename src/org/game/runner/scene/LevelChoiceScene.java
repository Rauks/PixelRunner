/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import org.andengine.entity.primitive.Rectangle;
import org.game.runner.GameActivity;
import org.game.runner.base.BaseScrollMenuScene;
import org.game.runner.manager.SceneManager;
import org.game.runner.manager.SceneManager.SceneType;

/**
 *
 * @author Karl
 */
public class LevelChoiceScene extends BaseScrollMenuScene{
    public LevelChoiceScene(){
        super(GameActivity.CAMERA_WIDTH, GameActivity.CAMERA_HEIGHT);
        
        this.addPage(new Rectangle(0, 0, 100, 100, this.vbom));
        this.addPage(new Rectangle(0, 0, 100, 100, this.vbom));
        this.addPage(new Rectangle(0, 0, 100, 100, this.vbom));
        this.addPage(new Rectangle(0, 0, 100, 100, this.vbom));
    }

    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().createMainMenuScene();
        SceneManager.getInstance().disposeLevelChoiceScene();
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_LEVEL_CHOICE;
    }
    
    @Override
    public void disposeScene() {
        super.disposeScene();
    }
}
