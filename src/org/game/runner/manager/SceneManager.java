/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.manager;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;
import org.andengine.util.debug.Debug;
import org.game.runner.GameActivity;
import org.game.runner.base.BaseScene;
import org.game.runner.game.LevelDescriptor;
import org.game.runner.scene.ArcadeGameLevelScene;
import org.game.runner.scene.CreditScene;
import org.game.runner.scene.GameLevelScene;
import org.game.runner.scene.LoadingScene;
import org.game.runner.scene.MainMenuScene;
import org.game.runner.scene.SplashEndScene;
import org.game.runner.scene.SplashScene;

/**
 *
 * @author Karl
 */
public class SceneManager {
    public enum SceneType{
        SCENE_SPLASH,
        SCENE_SPLASH_END,
        SCENE_MENU,
        SCENE_GAME_ARCADE,
        SCENE_LOADING,
        SCENE_CREDITS}
    
    private static final SceneManager INSTANCE = new SceneManager();
    
    private BaseScene creditsScene;
    private BaseScene splashScene;
    private BaseScene splashEndScene;
    private BaseScene mainMenuScene;
    private BaseScene gameLevelScene;
    private BaseScene loadingScene;
    
    private Engine engine = ResourcesManager.getInstance().engine;
    private GameActivity activity = ResourcesManager.getInstance().activity;
    private BaseScene currentScene;
    
    
    public void setScene(BaseScene scene){
        this.currentScene = scene;
        this.engine.setScene(scene);
    }
    
    public SceneType getCurrentSceneType(){
        return this.currentScene.getSceneType();
    }
    
    public BaseScene getCurrentScene(){
        return this.currentScene;
    }
    
    public void loadGameLevelScene(final SceneType type, final LevelDescriptor level){
        this.createLoadingScene();
        ResourcesManager.getInstance().unloadMenuResources();
        this.disposeMainMenuScene();
        this.engine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                SceneManager.this.engine.unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadGameResources(level);
                SceneManager.this.createGameLevelScene(type, level);
                SceneManager.this.disposeLoadingScene();
                ((GameLevelScene)SceneManager.getInstance().getCurrentScene()).start();
            }
        }));
    }
    
    public void unloadGameLevelScene(){
        this.createLoadingScene();
        ResourcesManager.getInstance().unloadGameResources();
        this.disposeGameLevelScene();
        this.engine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                SceneManager.this.engine.unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadMenuResources();
                SceneManager.this.createMainMenuScene();
                AudioManager.getInstance().play("mfx/", "menu.xm");
                SceneManager.this.disposeLoadingScene();
            }
        }));
    }
    
    private void createLoadingScene() {
        this.loadingScene = new LoadingScene();
        setScene(this.loadingScene);
    }
    
    private void disposeLoadingScene(){
        this.loadingScene.disposeScene();
        this.loadingScene = null;
    }
    
    private void createGameLevelScene(SceneType type, LevelDescriptor level) {
        switch(type){
            case SCENE_GAME_ARCADE:
                this.gameLevelScene = new ArcadeGameLevelScene(level);
                break;
            default:
                Debug.e("PixelRunner", "Not a gaming scene flag in scene manager loading.");
                return;       
        }        
        setScene(this.gameLevelScene);
    }
    
    private void disposeGameLevelScene(){
        this.gameLevelScene.disposeScene();
        this.gameLevelScene = null;
    }
    
    public void createMainMenuScene() {
        this.mainMenuScene = new MainMenuScene();
        this.setScene(this.mainMenuScene);
    }
    
    public void disposeMainMenuScene(){
        this.mainMenuScene.disposeScene();
        this.mainMenuScene = null;
    }

    public void createCreditsScene() {
        this.creditsScene = new CreditScene();
        this.setScene(this.creditsScene);
    }

    public void disposeCreditsScene() {
        this.creditsScene.disposeScene();
        this.creditsScene = null;
    }
    
    public void createSplashEndScene() {
        this.splashEndScene = new SplashEndScene();
        this.setScene(this.splashEndScene);
    }
    
    public void disposeSplashEndScene(){
        this.splashEndScene.disposeScene();
        this.splashEndScene = null;
    }
    
    public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {
        this.splashScene = new SplashScene();
        this.currentScene = this.splashScene;
        pOnCreateSceneCallback.onCreateSceneFinished(this.splashScene);
    }
    
    public void disposeSplashScene(){
        this.splashScene.disposeScene();
        this.splashScene = null;
    }
    
    public static SceneManager getInstance(){
        return INSTANCE;
    }
}
