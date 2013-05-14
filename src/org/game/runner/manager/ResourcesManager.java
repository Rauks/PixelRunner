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
import org.andengine.opengl.texture.region.ITiledTextureRegion;
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
    private BitmapTextureAtlas testTextureAtlas;
    public ITextureRegion test;
    
    private BitmapTextureAtlas gameTextureAtlas;
    public ITiledTextureRegion player;
    
    private BitmapTextureAtlas menuTextureAtlas;
    public ITextureRegion mainMenuParallaxLayer1;
    public ITextureRegion mainMenuParallaxLayer2;
    public ITextureRegion mainMenuParallaxLayer3;
    public ITextureRegion mainMenuParallaxLayer4;
    
    private BitmapTextureAtlas splashTextureAtlas;
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
        this.menuTextureAtlas = new BitmapTextureAtlas(this.activity.getTextureManager(), 960, 960);
        this.mainMenuParallaxLayer1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.menuTextureAtlas, this.activity, "menu_bg_1.png", 0, 0);
        this.mainMenuParallaxLayer2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.menuTextureAtlas, this.activity, "menu_bg_2.png", 480, 0);
        this.mainMenuParallaxLayer3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.menuTextureAtlas, this.activity, "menu_bg_3.png", 0, 480);
        this.mainMenuParallaxLayer4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.menuTextureAtlas, this.activity, "menu_bg_4.png", 480, 480);
        this.menuTextureAtlas.load();
        AudioManager.getInstance().prepare("mfx/", "menu.xm");
    }
    
    public void unloadMenuResources(){
        this.menuTextureAtlas.unload();
        mainMenuParallaxLayer1 = null;
        mainMenuParallaxLayer2 = null;
        mainMenuParallaxLayer3 = null;
        mainMenuParallaxLayer4 = null;
    }

    public void loadSplashResources() {
        this.loadFonts();
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        this.splashTextureAtlas = new BitmapTextureAtlas(this.activity.getTextureManager(), 960, 960);
        this.splashHeadphones = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.splashTextureAtlas, this.activity, "headphones.png", 0, 0);
        this.splashTextureAtlas.load();
    }

    public void unloadSplashResources() {
        this.splashTextureAtlas.unload();
        this.splashHeadphones = null;
    }

    public void loadGameResources(LevelDescriptor level) {
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        this.testTextureAtlas = new BitmapTextureAtlas(this.activity.getTextureManager(), 40, 40);
        this.test = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.testTextureAtlas, this.activity, "test_body.png", 0, 0);
        this.testTextureAtlas.load();
        
        this.gameTextureAtlas = new BitmapTextureAtlas(this.activity.getTextureManager(), 1024, 1024);
        this.player = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.gameTextureAtlas, this.activity, "player.png", 0, 0, 4, 4);
        this.gameTextureAtlas.load();
        AudioManager.getInstance().prepare("mfx/", level.getMusic());
    }

    public void unloadGameResources() {
        this.testTextureAtlas.unload();
        this.test = null;
        this.gameTextureAtlas.unload();
        this.player = null;
    }
    
    public static ResourcesManager getInstance(){
        return INSTANCE;
    }
}
