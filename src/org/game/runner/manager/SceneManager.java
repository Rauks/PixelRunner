/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.manager;

import org.andengine.engine.Engine;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;
import org.game.runner.GameActivity;
import org.game.runner.base.BaseScene;
import org.game.runner.scene.MainMenuScene;
import org.game.runner.scene.SplashEndScene;
import org.game.runner.scene.SplashScene;

/**
 *
 * @author Karl
 */
public class SceneManager {
    public enum SceneType{
        SCENE_CREDITS,
        SCENE_SPLASH,
        SCENE_MENU,
        SCENE_GAME,
        SCENE_LOADING,
        SCENE_SPLASH_END}
    
    private static final SceneManager INSTANCE = new SceneManager();
    
    private BaseScene creditsScene;
    private BaseScene splashScene;
    private BaseScene splashEndScene;
    private BaseScene mainMenuScene;
    private BaseScene gameScene;
    private BaseScene loadingScene;
    
    private Engine engine = ResourcesManager.getInstance().engine;
    private GameActivity activity = ResourcesManager.getInstance().activity;
    private BaseScene currentScene;
    
    
    public void setScene(BaseScene scene){
        this.currentScene = scene;
        this.engine.setScene(scene);
    }
    
    public void setScene(SceneType sceneType){
        switch (sceneType){
            case SCENE_MENU:
                setScene(this.mainMenuScene);
                break;
            case SCENE_GAME:
                setScene(this.gameScene);
                break;
            case SCENE_SPLASH:
                setScene(this.splashScene);
                break;
            case SCENE_SPLASH_END:
                setScene(this.splashEndScene);
                break;
            case SCENE_LOADING:
                setScene(this.loadingScene);
                break;
            case SCENE_CREDITS:
                setScene(this.creditsScene);
                break;
            default:
                break;
        }
    }
    
    public SceneType getCurrentSceneType(){
        return this.currentScene.getSceneType();
    }
    
    public BaseScene getCurrentScene(){
        return this.currentScene;
    }
    
    public void createMainMenuScene() {
        ResourcesManager.getInstance().loadMainMenuResources();
        this.mainMenuScene = new MainMenuScene();
        this.setScene(this.mainMenuScene);
    }
    
    public void disposeMainMenuScene(){
        ResourcesManager.getInstance().unloadMainMenuResources();
        this.mainMenuScene.disposeScene();
        this.mainMenuScene = null;
    }
    
    public void createSplashEndScene() {
        ResourcesManager.getInstance().loadSplashEndResources();
        this.splashEndScene = new SplashEndScene();
        this.setScene(this.splashEndScene);
    }
    
    public void disposeSplashEndScene(){
        ResourcesManager.getInstance().unloadSplashEndResources();
        this.splashEndScene.disposeScene();
        this.splashEndScene = null;
    }
    
    public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {
        ResourcesManager.getInstance().loadSplashResources();
        this.splashScene = new SplashScene();
        this.currentScene = this.splashScene;
        pOnCreateSceneCallback.onCreateSceneFinished(this.splashScene);
    }
    
    public void disposeSplashScene(){
        ResourcesManager.getInstance().unloadSplashResources();
        this.splashScene.disposeScene();
        this.splashScene = null;
    }
    
    public static SceneManager getInstance(){
        return INSTANCE;
    }
}
