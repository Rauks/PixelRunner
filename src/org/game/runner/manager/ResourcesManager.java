/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.manager;

import android.graphics.Color;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
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
    
    //Fonts
    public Font fontPixel_60;
    public Font fontPixel_100;
    
    //Textures & Regions
    private BitmapTextureAtlas mainMenuAutoParallaxBackgroundTexture;
    public ITextureRegion mainMenuParallaxLayer1;
    public ITextureRegion mainMenuParallaxLayer2;
    public ITextureRegion mainMenuParallaxLayer3;
    public ITextureRegion mainMenuParallaxLayer4;
    
    public static void prepareManager(Engine engine, GameActivity activity, Camera camera, VertexBufferObjectManager vbom){
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }
    
    public void loadForAll(){
        this.loadFonts();
    }
    
    private void loadFonts(){
        FontFactory.setAssetBasePath("font/");
        final ITexture fontPixel60Texture = new BitmapTextureAtlas(this.activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.fontPixel_60 = FontFactory.createStrokeFromAsset(this.activity.getFontManager(), fontPixel60Texture, this.activity.getAssets(), "pixel.ttf", 60, false, Color.WHITE, 2, Color.BLACK);
        this.fontPixel_60.load();
        final ITexture fontPixel100Texture = new BitmapTextureAtlas(this.activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.fontPixel_100 = FontFactory.createStrokeFromAsset(this.activity.getFontManager(), fontPixel100Texture, this.activity.getAssets(), "pixel.ttf", 100, false, Color.WHITE, 2, Color.BLACK);
        this.fontPixel_100.load();
    }
    
    public void loadMainMenuResources(){
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        this.mainMenuAutoParallaxBackgroundTexture = new BitmapTextureAtlas(this.activity.getTextureManager(), 960, 960);
        this.mainMenuParallaxLayer1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mainMenuAutoParallaxBackgroundTexture, this.activity, "menu_bg_1.png", 0, 0);
        this.mainMenuParallaxLayer2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mainMenuAutoParallaxBackgroundTexture, this.activity, "menu_bg_2.png", 480, 0);
        this.mainMenuParallaxLayer3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mainMenuAutoParallaxBackgroundTexture, this.activity, "menu_bg_3.png", 0, 480);
        this.mainMenuParallaxLayer4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mainMenuAutoParallaxBackgroundTexture, this.activity, "menu_bg_4.png", 480, 480);
        this.mainMenuAutoParallaxBackgroundTexture.load();
    }
    
    public void unloadMainMenuResources(){
        this.mainMenuAutoParallaxBackgroundTexture.unload();
        mainMenuParallaxLayer1 = null;
        mainMenuParallaxLayer2 = null;
        mainMenuParallaxLayer3 = null;
        mainMenuParallaxLayer4 = null;
    }

    public void loadSplashResources() {
    }

    public void unloadSplashResources() {
    }
    
    public static ResourcesManager getInstance(){
        return INSTANCE;
    }
}
