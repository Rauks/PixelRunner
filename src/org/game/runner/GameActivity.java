package org.game.runner;

import android.view.KeyEvent;
import java.io.IOException;
import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.ui.activity.BaseGameActivity;
import org.game.runner.manager.AudioManager;
import org.game.runner.manager.ResourcesManager;
import org.game.runner.manager.SceneManager;
import org.andengine.util.debug.Debug;

public class GameActivity extends BaseGameActivity{
    public static float CAMERA_WIDTH = 800;
    public static float CAMERA_HEIGHT = 480;
    
    private android.media.AudioManager phoneAudioManager;
    private Camera camera;
    private ResourcesManager resourcesManager;
    private SceneManager sceneManager;
    private boolean isEnginePaused = false;

    @Override
    public EngineOptions onCreateEngineOptions() {
        this.camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
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
        SceneManager.getInstance().createLoadingScene(pOnCreateSceneCallback);
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
        this.mEngine.enableVibrator(this);
        this.mEngine.registerUpdateHandler(new FPSLogger());
        this.phoneAudioManager = (android.media.AudioManager)getSystemService(AUDIO_SERVICE);
        this.mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback(){
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler){
                GameActivity.this.mEngine.unregisterUpdateHandler(pTimerHandler);
                SceneManager.getInstance().createSplashScene();
            }
        }));
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
        this.isEnginePaused = true;
    }
 
    @Override
    public void onResumeGame() {
        super.onResumeGame();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK){
            SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
        }
        return false; 
    }
    
    @Override
    public void onWindowFocusChanged(final boolean pHasWindowFocus) {
        super.onWindowFocusChanged(pHasWindowFocus);
        if (pHasWindowFocus && this.isEnginePaused) {
            this.isEnginePaused = false;
            if (this.isGameLoaded()){
                SceneManager.getInstance().getCurrentScene().onResume();
            }
        }
    }
    
    public void vibrate(long pMilliseconds){
        if(!this.isMute()){
            this.mEngine.vibrate(pMilliseconds);
        }
    }
    public void vibrate(long[] pMillisecondsPattern){
        if(!this.isMute()){
            this.mEngine.vibrate(pMillisecondsPattern, -1);
        }
    }
    
    public boolean isMute(){
        return this.phoneAudioManager.getStreamVolume(android.media.AudioManager.STREAM_MUSIC) == 0;
    }
}
