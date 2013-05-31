/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.manager;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;
import org.andengine.util.debug.Debug;
import net.kirauks.pixelrunner.GameActivity;
import net.kirauks.pixelrunner.scene.base.BaseScene;
import net.kirauks.pixelrunner.game.descriptor.LevelDescriptor;
import net.kirauks.pixelrunner.scene.ArcadeGameScene;
import net.kirauks.pixelrunner.scene.BonusChoiceScene;
import net.kirauks.pixelrunner.scene.BonusJukeboxScene;
import net.kirauks.pixelrunner.scene.CreditScene;
import net.kirauks.pixelrunner.scene.base.BaseGameScene;
import net.kirauks.pixelrunner.scene.LevelChoiceScene;
import net.kirauks.pixelrunner.scene.LevelGameScene;
import net.kirauks.pixelrunner.scene.LoadingScene;
import net.kirauks.pixelrunner.scene.LoadingScene.LoadingListener;
import net.kirauks.pixelrunner.scene.MainMenuScene;
import net.kirauks.pixelrunner.scene.SplashEndScene;

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
        SCENE_BONUS_CHOICE,
        SCENE_BONUS_JUKEBOX,
        SCENE_BONUS_SUCCESS,
        SCENE_GAME_ARCADE,
        SCENE_GAME_LEVEL,
        SCENE_LOADING,
        SCENE_CREDITS}
    
    private static final SceneManager INSTANCE = new SceneManager();
    
    private BaseScene creditsScene;
    private BaseScene splashEndScene;
    private BaseScene mainMenuScene;
    private BaseScene levelChoiceScene;
    private BaseScene bonusChoiceScene;
    private BaseScene bonusJukeboxScene;
    private BaseScene gameLevelScene;
    
    private LoadingScene loadingScene;
    
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
        this.disposeMenuBeforeGameScene(type);
        System.gc();
        this.engine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler){
                SceneManager.this.engine.unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadGameResources(level);
                SceneManager.this.createGameScene(type, level);
                SceneManager.this.loadingScene.setLoadingListener(new LoadingListener(){
                    @Override
                    public void onPause() {
                        if(SceneManager.getInstance().getCurrentScene() instanceof BaseGameScene){
                            ((BaseGameScene)SceneManager.getInstance().getCurrentScene()).pause();
                        }
                    }
                    @Override
                    public void onResume() {
                        if(SceneManager.getInstance().getCurrentScene() instanceof BaseGameScene){
                        }
                    }
                });
                SceneManager.this.engine.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() {
                    @Override
                    public void onTimePassed(final TimerHandler pTimerHandler){
                        SceneManager.this.engine.unregisterUpdateHandler(pTimerHandler);
                        if(SceneManager.getInstance().getCurrentScene() instanceof BaseGameScene){
                            ((BaseGameScene)SceneManager.getInstance().getCurrentScene()).start();
                        }
                        SceneManager.this.loadingScene.removeLoadingListener();
                    }
                }));
            }
        }));
    }
    
    public void unloadGameLevelScene(){
        final SceneType wasType = this.getCurrentSceneType();
        this.setScene(this.loadingScene);
        ResourcesManager.getInstance().unloadGameResources();
        this.disposeGameScene();
        System.gc();
        this.engine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler){
                SceneManager.this.engine.unregisterUpdateHandler(pTimerHandler);
                ResourcesManager.getInstance().loadMenuResources();
                SceneManager.this.createMenuAfterGameScene(wasType);
                AudioManager.getInstance().play("mfx/main/", "menu.xm");
            }
        }));
    }
    
    private void createMenuAfterGameScene(final SceneType type){
        switch(type){
            case SCENE_GAME_ARCADE:
                this.createMainMenuScene();
                break;
            case SCENE_GAME_LEVEL:
                this.createLevelChoiceScene();
                break;
            default:
                Debug.e("PixelRunner", "Not a gaming scene flag in scene manager loading.");
        }    
    }
    private void disposeMenuBeforeGameScene(final SceneType type){
        switch(type){
            case SCENE_GAME_ARCADE:
                this.disposeMainMenuScene();
                break;
            case SCENE_GAME_LEVEL:
                this.disposeLevelChoiceScene();
                break;
            default:
                Debug.e("PixelRunner", "Not a gaming scene flag in scene manager loading.");
        }        
    }
    private void createGameScene(final SceneType type, final LevelDescriptor level) {
        switch(type){
            case SCENE_GAME_ARCADE:
                this.gameLevelScene = new ArcadeGameScene(level);
                break;
            case SCENE_GAME_LEVEL:
                this.gameLevelScene = new LevelGameScene(level);
                break;
            default:
                Debug.e("PixelRunner", "Not a gaming scene flag in scene manager loading.");
                return;       
        }        
        setScene(this.gameLevelScene);
    }
    
    private void disposeGameScene(){
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

    public void createBonusChoiceScene() {
        this.bonusChoiceScene = new BonusChoiceScene();
        this.setScene(this.bonusChoiceScene);
    }

    public void disposeBonusChoiceScene() {
        this.activity.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                SceneManager.this.bonusChoiceScene.disposeScene();
                SceneManager.this.bonusChoiceScene = null;
            }
        });
    }
    
    
    public void createBonusJukeboxScene() {
        this.bonusJukeboxScene = new BonusJukeboxScene();
        this.setScene(this.bonusJukeboxScene);
    }
    
    public void disposeBonusJukeboxScene(){
        this.activity.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                SceneManager.this.bonusJukeboxScene.disposeScene();
                SceneManager.this.bonusJukeboxScene = null;
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
