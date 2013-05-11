/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.manager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.game.runner.GameActivity;

/**
 *
 * @author Karl
 */
public class ResourcesManager {
    private static final ResourcesManager INSTANCE = new ResourcesManager();
    
    public Engine engine;
    public GameActivity activity;
    public Camera camera;
    public VertexBufferObjectManager vbom;
    
    //Textures & Regions
    private BitmapTextureAtlas splashAutoParallaxBackgroundTexture;
    public ITextureRegion splashParallaxLayer1;
    public ITextureRegion splashParallaxLayer2;
    public ITextureRegion splashParallaxLayer3;
    public ITextureRegion splashParallaxLayer4;
    
    public static void prepareManager(Engine engine, GameActivity activity, Camera camera, VertexBufferObjectManager vbom){
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }
    
    public void loadSplashScreen(){
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        this.splashAutoParallaxBackgroundTexture = new BitmapTextureAtlas(this.activity.getTextureManager(), 400, 480);
        this.splashParallaxLayer1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.splashAutoParallaxBackgroundTexture, this.activity, "menu_bg_1.png", 0, 0);
        this.splashParallaxLayer2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.splashAutoParallaxBackgroundTexture, this.activity, "menu_bg_2.png", 100, 0);
        this.splashParallaxLayer3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.splashAutoParallaxBackgroundTexture, this.activity, "menu_bg_3.png", 200, 0);
        this.splashParallaxLayer4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.splashAutoParallaxBackgroundTexture, this.activity, "menu_bg_4.png", 300, 0);
        this.splashAutoParallaxBackgroundTexture.load();
    }
    
    public void unloadSplashScreen(){
        this.splashAutoParallaxBackgroundTexture.unload();
        splashParallaxLayer1 = null;
        splashParallaxLayer2 = null;
        splashParallaxLayer3 = null;
        splashParallaxLayer4 = null;
    }
    
    public static ResourcesManager getInstance(){
        return INSTANCE;
    }
}
