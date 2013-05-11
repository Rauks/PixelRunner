package org.game.runner;

import android.os.Bundle;
import android.view.KeyEvent;
import java.io.IOException;
import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;
import org.game.runner.manager.AudioManager;
import org.game.runner.manager.ResourcesManager;
import org.game.runner.manager.SceneManager;

public class GameActivity extends BaseGameActivity{
    
    private Camera camera;
    private ResourcesManager resourcesManager;
    private SceneManager sceneManager;

    @Override
    public EngineOptions onCreateEngineOptions() {
        this.camera = new Camera(0, 0, 800, 480);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(800, 480), this.camera);
        engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        return engineOptions;
    }
    
    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions) {
        return new LimitedFPSEngine(pEngineOptions, 60);
    }
    
    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException {
        ResourcesManager.prepareManager(this.mEngine, this, this.camera, getVertexBufferObjectManager());
        this.resourcesManager = ResourcesManager.getInstance();
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
        SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
        
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.isGameLoaded()){
            AudioManager.getInstance().stop();
            System.exit(0);    
        } 
    }
    
    @Override
    public synchronized void onPauseGame() {
        super.onPauseGame();
        if (this.isGameLoaded()){
            SceneManager.getInstance().getCurrentScene().onPause();
        }
    }
    
    @Override
    public synchronized void onResumeGame() {
        super.onResumeGame();
        if (this.isGameLoaded()){
            SceneManager.getInstance().getCurrentScene().onResume();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (this.isGameLoaded()){
            SceneManager.getInstance().getCurrentScene().onPause();
        }
    }
    
    @Override
    protected synchronized void onResume() {
        super.onResume();
        if (this.isGameLoaded()){
            SceneManager.getInstance().getCurrentScene().onResume();
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK){
            SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
        }
        return false; 
    }
}
