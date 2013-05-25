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
    
    private ScrollMenuPage tutorials;
    private ScrollMenuPage mountains;
    private ScrollMenuPage desert;
    private ScrollMenuPage city;
    private ScrollMenuPage forest;
    private ScrollMenuPage hills;
    
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
        
        this.tutorials = new ScrollMenuPage(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, 6, this.vbom);
        this.tutorials.setTitle("TUTORIALS");
        this.tutorials.setProgress(11);
        this.tutorials.refreshLocks();
        
        this.mountains = new ScrollMenuPage(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, 12, this.vbom);
        this.mountains.setTitle("MOUNTAINS");
        this.mountains.setProgress(5);
        this.mountains.refreshLocks();
        
        this.desert = new ScrollMenuPage(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, 12, this.vbom);
        this.desert.setTitle("DESERT");
        this.desert.setProgress(7);
        this.desert.refreshLocks();
        
        this.city = new ScrollMenuPage(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, 12, this.vbom);
        this.city.setTitle("CITY");
        this.city.refreshLocks();
        
        this.forest = new ScrollMenuPage(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, 12, this.vbom);
        this.forest.setTitle("FOREST");
        this.forest.refreshLocks();
        
        this.hills = new ScrollMenuPage(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, 12, this.vbom);
        this.hills.setTitle("HILLS");
        this.hills.refreshLocks();
        
        addPages(this.tutorials, this.mountains, this.desert, this.city, this.forest, this.hills);
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
