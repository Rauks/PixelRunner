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
import org.game.runner.game.LevelDescriptor;

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
    public Font fontPixel_34;
    public Font fontPixel_60;
    public Font fontPixel_100;
    public Font fontPixel_200;
    
    //Textures & Regions
    private BitmapTextureAtlas testGamePlayerTexture;
    public ITextureRegion testGamePlayer;
    private BitmapTextureAtlas testGameParallaxBackgroundTexture;
    public ITextureRegion testGameParallaxLayer1;
    public ITextureRegion testGameParallaxLayer2;
    public ITextureRegion testGameParallaxLayer3;
    public ITextureRegion testGameParallaxLayer4;
    
    private BitmapTextureAtlas mainMenuAutoParallaxBackgroundTexture;
    public ITextureRegion mainMenuParallaxLayer1;
    public ITextureRegion mainMenuParallaxLayer2;
    public ITextureRegion mainMenuParallaxLayer3;
    public ITextureRegion mainMenuParallaxLayer4;
    
    private BitmapTextureAtlas splashHeadphonesTexture;
    public ITextureRegion splashHeadphones;
    
    public static void prepareManager(Engine engine, GameActivity activity, Camera camera, VertexBufferObjectManager vbom){
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }
    
    private void loadFonts(){
        FontFactory.setAssetBasePath("font/");
        final ITexture fontPixel34Texture = new BitmapTextureAtlas(this.activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.fontPixel_34 = FontFactory.createStrokeFromAsset(this.activity.getFontManager(), fontPixel34Texture, this.activity.getAssets(), "pixel.ttf", 34, false, Color.WHITE, 1, Color.BLACK);
        this.fontPixel_34.load();
        final ITexture fontPixel60Texture = new BitmapTextureAtlas(this.activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.fontPixel_60 = FontFactory.createStrokeFromAsset(this.activity.getFontManager(), fontPixel60Texture, this.activity.getAssets(), "pixel.ttf", 60, false, Color.WHITE, 2, Color.BLACK);
        this.fontPixel_60.load();
        final ITexture fontPixel100Texture = new BitmapTextureAtlas(this.activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.fontPixel_100 = FontFactory.createStrokeFromAsset(this.activity.getFontManager(), fontPixel100Texture, this.activity.getAssets(), "pixel.ttf", 100, false, Color.WHITE, 2, Color.BLACK);
        this.fontPixel_100.load();
        final ITexture fontPixel200Texture = new BitmapTextureAtlas(this.activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.fontPixel_200 = FontFactory.createStrokeFromAsset(this.activity.getFontManager(), fontPixel200Texture, this.activity.getAssets(), "pixel.ttf", 200, false, Color.WHITE, 2, Color.BLACK);
        this.fontPixel_200.load();
    }
    
    public void loadMenuResources(){
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        this.mainMenuAutoParallaxBackgroundTexture = new BitmapTextureAtlas(this.activity.getTextureManager(), 960, 960);
        this.mainMenuParallaxLayer1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mainMenuAutoParallaxBackgroundTexture, this.activity, "menu_bg_1.png", 0, 0);
        this.mainMenuParallaxLayer2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mainMenuAutoParallaxBackgroundTexture, this.activity, "menu_bg_2.png", 480, 0);
        this.mainMenuParallaxLayer3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mainMenuAutoParallaxBackgroundTexture, this.activity, "menu_bg_3.png", 0, 480);
        this.mainMenuParallaxLayer4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mainMenuAutoParallaxBackgroundTexture, this.activity, "menu_bg_4.png", 480, 480);
        this.mainMenuAutoParallaxBackgroundTexture.load();
        AudioManager.getInstance().prepare("mfx/", "menu.xm");
    }
    
    public void unloadMenuResources(){
        this.mainMenuAutoParallaxBackgroundTexture.unload();
        mainMenuParallaxLayer1 = null;
        mainMenuParallaxLayer2 = null;
        mainMenuParallaxLayer3 = null;
        mainMenuParallaxLayer4 = null;
    }

    public void loadSplashResources() {
        this.loadFonts();
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        this.splashHeadphonesTexture = new BitmapTextureAtlas(this.activity.getTextureManager(), 960, 960);
        this.splashHeadphones = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.splashHeadphonesTexture, this.activity, "headphones.png", 0, 0);
        this.splashHeadphonesTexture.load();
    }

    public void unloadSplashResources() {
        this.splashHeadphonesTexture.unload();
        this.splashHeadphones = null;
    }

    public void loadGameResources(LevelDescriptor level) {
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        this.testGamePlayerTexture = new BitmapTextureAtlas(this.activity.getTextureManager(), 40, 40);
        this.testGamePlayer = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.testGamePlayerTexture, this.activity, "test_body.png", 0, 0);
        this.testGamePlayerTexture.load();
        AudioManager.getInstance().prepare("mfx/", level.getMusic());
    }

    public void unloadGameResources() {
        this.testGamePlayerTexture.unload();
        this.testGamePlayer = null;
    }
    
    public static ResourcesManager getInstance(){
        return INSTANCE;
    }
}
