/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import org.game.runner.GameActivity;
import org.game.runner.base.BaseScrollMenuScene;
import org.game.runner.base.element.ScrollMenuPage;
import org.game.runner.manager.SceneManager;
import org.game.runner.manager.SceneManager.SceneType;

/**
 *
 * @author Karl
 */
public class LevelChoiceScene extends BaseScrollMenuScene{
    private static float PADDING = 100;
    
    public LevelChoiceScene(){
        super(GameActivity.CAMERA_WIDTH, GameActivity.CAMERA_HEIGHT);
        
        this.registerScrollScenePageListener(new IOnScrollScenePageListener() {
            @Override
            public void onMoveToPageStarted(int oldPageNumber, int newPageNumber) {
                if(oldPageNumber < newPageNumber){
                    LevelChoiceScene.this.setParallaxFactor(5f);
                }
                else if(oldPageNumber > newPageNumber){
                    LevelChoiceScene.this.setParallaxFactor(-5f);
                }
            }
            @Override
            public void onMoveToPageFinished(int oldPageNumber, int newPageNumber) {
                LevelChoiceScene.this.setParallaxFactor(1f);
            }
        });
        
        this.addPage(new ScrollMenuPage(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, this.vbom));
        this.addPage(new ScrollMenuPage(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, this.vbom));
        this.addPage(new ScrollMenuPage(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, this.vbom));
        this.addPage(new ScrollMenuPage(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, this.vbom));
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
