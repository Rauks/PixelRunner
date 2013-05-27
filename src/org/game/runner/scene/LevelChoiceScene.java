/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import org.game.runner.GameActivity;
import org.game.runner.scene.base.BaseScrollMenuScene;
import org.game.runner.scene.base.element.Page;
import org.game.runner.scene.base.element.PageElement;
import org.game.runner.manager.SceneManager;
import org.game.runner.manager.SceneManager.SceneType;
import org.andengine.util.debug.Debug;

/**
 *
 * @author Karl
 */
public class LevelChoiceScene extends BaseScrollMenuScene{
    private static float PADDING = 100;
    
    private Page tutorials;
    private Page mountains;
    private Page desert;
    private Page city;
    private Page forest;
    private Page hills;
    
    public LevelChoiceScene(){
        super(GameActivity.CAMERA_WIDTH, GameActivity.CAMERA_HEIGHT);
        
        this.registerScrollScenePageListener(new IOnScrollListener() {
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
        
        this.tutorials = new Page(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, 0, 6, this.vbom);
        this.tutorials.setTitle("TUTORIALS");
        this.tutorials.setProgress(11);
        this.tutorials.refreshLocks();
        
        this.mountains = new Page(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, 1, 12, this.vbom);
        this.mountains.setTitle("MOUNTAINS");
        this.mountains.setProgress(5);
        this.mountains.refreshLocks();
        
        this.desert = new Page(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, 2, 12, this.vbom);
        this.desert.setTitle("DESERT");
        this.desert.setProgress(7);
        this.desert.refreshLocks();
        
        this.city = new Page(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, 3, 12, this.vbom);
        this.city.setTitle("CITY");
        this.city.refreshLocks();
        
        this.forest = new Page(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, 4, 12, this.vbom);
        this.forest.setTitle("FOREST");
        this.forest.refreshLocks();
        
        this.hills = new Page(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, 5, 12, this.vbom);
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

    @Override
    public void onElementAction(PageElement element) {
        this.activity.vibrate(30);
        Debug.d("Element selected : " + element.getId() + " @ page " + this.getCurrentPage().getWorldId());
    }
}
