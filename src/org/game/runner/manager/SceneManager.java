/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.manager;

import org.andengine.engine.Engine;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;
import org.game.runner.GameActivity;
import org.game.runner.base.BaseScene;
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
        SCENE_LOADING
    }
    
    private static final SceneManager INSTANCE = new SceneManager();
    
    private BaseScene creditsScene;
    private BaseScene splashScene;
    private BaseScene menuScene;
    private BaseScene gameScene;
    private BaseScene loadingScene;
    
    private Engine engine = ResourcesManager.getInstance().engine;
    private GameActivity activity = ResourcesManager.getInstance().activity;
    private SceneType currentSceneType = SceneType.SCENE_SPLASH;
    private BaseScene currentScene;
    
    
    public void setScene(BaseScene scene){
        this.engine.setScene(scene);
        this.currentScene = scene;
        this.currentSceneType = scene.getSceneType();
    }
    
    public SceneType getCurrentSceneType(){
        return this.currentSceneType;
    }
    
    public BaseScene getCurrentScene(){
        return this.currentScene;
    }
    
    public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {
        ResourcesManager.getInstance().loadSplashScreen();
        this.splashScene = new SplashScene();
        this.currentScene = this.splashScene;
        pOnCreateSceneCallback.onCreateSceneFinished(this.splashScene);
    }
    
    public static SceneManager getInstance(){
        return INSTANCE;
    }
}
