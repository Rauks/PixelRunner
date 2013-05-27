/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene.base;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.game.runner.GameActivity;
import org.game.runner.manager.AudioManager;
import org.game.runner.manager.ResourcesManager;
import org.game.runner.manager.SceneManager.SceneType;

/**
 *
 * @author Karl
 */
public abstract class BaseScene extends Scene{
    protected Engine engine;
    protected GameActivity activity;
    protected ResourcesManager resourcesManager;
    protected AudioManager audioManager;
    protected VertexBufferObjectManager vbom;
    protected Camera camera;
    
    public BaseScene(){
        this.resourcesManager = ResourcesManager.getInstance();
        this.audioManager = AudioManager.getInstance();
        this.engine = resourcesManager.engine;
        this.activity = resourcesManager.activity;
        this.vbom = resourcesManager.vbom;
        this.camera = resourcesManager.camera;
        createScene();
    }
    
    public abstract void createScene();
    
    public abstract void onBackKeyPressed();
    public abstract void onPause();
    public abstract void onResume();
    
    public abstract SceneType getSceneType();
    
    public abstract void disposeScene();
}
