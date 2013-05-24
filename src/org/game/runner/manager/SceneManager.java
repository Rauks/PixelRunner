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
import org.game.runner.game.descriptor.LevelDescriptor;
import org.game.runner.scene.ArcadeGameLevelScene;
import org.game.runner.scene.CreditScene;
import org.game.runner.scene.GameLevelScene;
import org.game.runner.scene.LevelChoiceScene;
import org.game.runner.scene.LoadingScene;
import org.game.runner.scene.MainMenuScene;
import org.game.runner.scene.SplashEndScene;

/**
 *
 * @author Karl
 */
public class SceneManager {

    public enum SceneType{
        SCENE_SPLASH,
        SCENE_SPLASH_END,
        SCENE_MENU,
        SCENE_LEVEL_CHOICE,
        SCENE_GAME_ARCADE,
        SCENE_LOADING,
        SCENE_CREDITS}
    
    private static final SceneManager INSTANCE = new SceneManager();
    
    private BaseScene creditsScene;
    private BaseScene splashEndScene;
    private BaseScene mainMenuScene;
    private BaseScene levelChoiceScene;
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
        this.setScene(this.loadingScene);
        ResourcesManager.getInstance().unloadMenuResources();
        this.disposeMainMenuScene();
        System.gc();
        this.engine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler){
                SceneManager.this.engine.unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadGameResources(level);
                SceneManager.this.createGameLevelScene(type, level);
                SceneManager.this.engine.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() {
                    @Override
                    public void onTimePassed(final TimerHandler pTimerHandler){
                        SceneManager.this.engine.unregisterUpdateHandler(pTimerHandler);
                        if(SceneManager.getInstance().getCurrentScene() instanceof GameLevelScene){
                            ((GameLevelScene)SceneManager.getInstance().getCurrentScene()).start();
                        }
                    }
                }));
            }
        }));
    }
    
    public void unloadGameLevelScene(){
        this.setScene(this.loadingScene);
        ResourcesManager.getInstance().unloadGameResources();
        this.disposeGameLevelScene();
        System.gc();
        this.engine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler){
                SceneManager.this.engine.unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadMenuResources();
                SceneManager.this.createMainMenuScene();
                AudioManager.getInstance().play("mfx/", "menu.xm");
            }
        }));
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
        this.activity.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                SceneManager.this.gameLevelScene.disposeScene();
                SceneManager.this.gameLevelScene = null;
            }
        });
    }
    
    public void createMainMenuScene() {
        this.mainMenuScene = new MainMenuScene();
        this.setScene(this.mainMenuScene);
    }
    
    public void disposeMainMenuScene(){
        this.activity.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                SceneManager.this.mainMenuScene.disposeScene();
                SceneManager.this.mainMenuScene = null;
            }
        });
    }

    public void createCreditsScene() {
        this.creditsScene = new CreditScene();
        this.setScene(this.creditsScene);
    }

    public void disposeCreditsScene() {
        this.activity.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                SceneManager.this.creditsScene.disposeScene();
                SceneManager.this.creditsScene = null;
            }
        });
    }
    
    public void createLevelChoiceScene() {
        this.levelChoiceScene = new LevelChoiceScene();
        this.setScene(this.levelChoiceScene);
    }
    public void disposeLevelChoiceScene() {
        this.activity.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                SceneManager.this.levelChoiceScene.disposeScene();
                SceneManager.this.levelChoiceScene = null;
            }
        });
    }
    
    public void createSplashScene() {
        ResourcesManager.getInstance().loadSplashResources();
        ResourcesManager.getInstance().loadMenuResources();
        this.splashEndScene = new SplashEndScene();
        this.setScene(this.splashEndScene);
    }
    
    public void disposeSplashScene(){
        this.activity.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                SceneManager.this.splashEndScene.disposeScene();
                SceneManager.this.splashEndScene = null;
            }
        });
    }
    
    public void createLoadingScene(OnCreateSceneCallback pOnCreateSceneCallback) {
        ResourcesManager.getInstance().loadFonts();
        this.loadingScene = new LoadingScene();
        this.setScene(this.loadingScene);
        pOnCreateSceneCallback.onCreateSceneFinished(this.loadingScene);
    }
    
    public static SceneManager getInstance(){
        return INSTANCE;
    }
}
