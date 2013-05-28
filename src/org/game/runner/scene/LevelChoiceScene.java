/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import android.content.SharedPreferences;
import org.game.runner.GameActivity;
import org.game.runner.scene.base.BaseScrollMenuScene;
import org.game.runner.scene.base.element.Page;
import org.game.runner.scene.base.element.PageElement;
import org.game.runner.manager.SceneManager;
import org.game.runner.manager.SceneManager.SceneType;
import org.andengine.util.debug.Debug;
import org.game.runner.R;
import org.game.runner.game.descriptor.ArcadeLevelDescriptor;
import org.game.runner.game.descriptor.LevelDescriptor;
import org.game.runner.game.descriptor.ScriptedLevelDescriptor;
import org.game.runner.game.descriptor.utils.World;
import org.game.runner.manager.AudioManager;
import org.game.runner.manager.db.InfosDatabase;
import org.game.runner.manager.db.InfosDatabase.Info;
import org.game.runner.manager.db.ProgressDatabase;
import org.game.runner.scene.base.element.PageTutorial;

/**
 *
 * @author Karl
 */
public class LevelChoiceScene extends BaseScrollMenuScene{
    private static float PADDING = 100;
    
    private ProgressDatabase progressDb;
    private InfosDatabase infosDb;
    
    private Page tutorials;
    private Page mountains;
    private Page desert;
    private Page city;
    private Page forest;
    private Page hills;
    private Page sweets;
    
    public LevelChoiceScene(){
        super(GameActivity.CAMERA_WIDTH, GameActivity.CAMERA_HEIGHT);
        
        this.progressDb = new ProgressDatabase(this.activity);
        this.progressDb.set(World.TRAINING, 4);
        
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
                LevelChoiceScene.this.infosDb.setInfo(Info.LEVEL_CHOICE_PAGE, newPageNumber);
            }
        });
        
        this.tutorials = new PageTutorial(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, World.TRAINING, this.vbom);
        this.tutorials.setTitle(this.activity.getString(R.string.lvl_tutorials));
        this.tutorials.setProgress(this.progressDb.get(World.TRAINING));
        
        this.mountains = new Page(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, World.MOUNTAINS, 12, this.vbom);
        this.mountains.setTitle(this.activity.getString(R.string.lvl_mountains));
        this.mountains.setProgress(this.progressDb.get(World.MOUNTAINS));
        
        this.desert = new Page(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, World.DESERT, 12, this.vbom);
        this.desert.setTitle(this.activity.getString(R.string.lvl_desert));
        this.desert.setProgress(this.progressDb.get(World.DESERT));
        
        this.city = new Page(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, World.CITY, 12, this.vbom);
        this.city.setTitle(this.activity.getString(R.string.lvl_city));
        this.city.setProgress(this.progressDb.get(World.CITY));
        
        this.forest = new Page(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, World.FOREST, 12, this.vbom);
        this.forest.setTitle(this.activity.getString(R.string.lvl_forest));
        this.forest.setProgress(this.progressDb.get(World.FOREST));
        
        this.hills = new Page(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, World.HILLS, 12, this.vbom);
        this.hills.setTitle(this.activity.getString(R.string.lvl_hills));
        this.hills.setProgress(this.progressDb.get(World.HILLS));
        
        this.sweets = new Page(GameActivity.CAMERA_WIDTH - PADDING, GameActivity.CAMERA_HEIGHT - PADDING, World.SWEETS, 12, this.vbom);
        this.sweets.setTitle(this.activity.getString(R.string.lvl_sweets));
        this.sweets.setProgress(this.progressDb.get(World.SWEETS));
        
        addPages(this.tutorials, this.mountains, this.desert, this.city, this.forest, this.hills, this.sweets);
        
        this.infosDb = new InfosDatabase(this.activity);
        this.moveToPage(this.infosDb.getInfo(Info.LEVEL_CHOICE_PAGE));
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
        if(this.getCurrentPage().getProgress() >= element.getId()){
            this.activity.vibrate(30);
            AudioManager.getInstance().stop();
            SceneManager.getInstance().loadGameLevelScene(SceneType.SCENE_GAME_LEVEL, new ScriptedLevelDescriptor(this.getCurrentPage().getWorld(), element.getId(), this.activity));
        }
        
    }
}
