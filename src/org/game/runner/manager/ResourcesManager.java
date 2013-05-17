/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.manager;

import android.graphics.Color;
import android.util.Log;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import org.andengine.util.debug.Debug;
import org.game.runner.GameActivity;
import org.game.runner.game.LevelDescriptor;
import org.game.runner.game.element.BackgroundElement;

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
    public Font fontPixel_60_gray;
    public Font fontPixel_100;
    public Font fontPixel_200;
    
    //Texturess - Game
    private BitmapTextureAtlas playerTextureAtlas;
    public ITiledTextureRegion player;
    private BitmapTextureAtlas trailTextureAtlas;
    public ITextureRegion trail;
    private BitmapTextureAtlas gameBackgroundTextureAtlas;
    public Map<String, ITextureRegion> gameParallaxLayers = new HashMap<String, ITextureRegion>();
    
    //Textures - Main
    private BitmapTextureAtlas menuTextureAtlas;
    public ITextureRegion mainMenuParallaxLayer1;
    public ITextureRegion mainMenuParallaxLayer2;
    public ITextureRegion mainMenuParallaxLayer3;
    public ITextureRegion mainMenuParallaxLayer4;
    
    //Textures - Splash
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
        final ITexture fontPixel60GrayTexture = new BitmapTextureAtlas(this.activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.fontPixel_60_gray = FontFactory.createStrokeFromAsset(this.activity.getFontManager(), fontPixel60GrayTexture, this.activity.getAssets(), "pixel.ttf", 60, false, Color.GRAY, 2, Color.BLACK);
        this.fontPixel_60_gray.load();
        final ITexture fontPixel100Texture = new BitmapTextureAtlas(this.activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.fontPixel_100 = FontFactory.createStrokeFromAsset(this.activity.getFontManager(), fontPixel100Texture, this.activity.getAssets(), "pixel.ttf", 100, false, Color.WHITE, 2, Color.BLACK);
        this.fontPixel_100.load();
        final ITexture fontPixel200Texture = new BitmapTextureAtlas(this.activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.fontPixel_200 = FontFactory.createStrokeFromAsset(this.activity.getFontManager(), fontPixel200Texture, this.activity.getAssets(), "pixel.ttf", 200, false, Color.WHITE, 5, Color.BLACK);
        this.fontPixel_200.load();
    }
    
    public void loadMenuResources(){
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/main/");
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
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/splash/");
        this.splashTextureAtlas = new BitmapTextureAtlas(this.activity.getTextureManager(), 960, 960);
        this.splashHeadphones = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.splashTextureAtlas, this.activity, "headphones.png", 0, 0);
        this.splashTextureAtlas.load();
    }

    public void unloadSplashResources() {
        this.splashTextureAtlas.unload();
        this.splashHeadphones = null;
    }

    public void loadGameResources(LevelDescriptor level) {
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
        this.playerTextureAtlas = new BitmapTextureAtlas(this.activity.getTextureManager(), 224, 256);
        this.player = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.playerTextureAtlas, this.activity, "player.png", 0, 0, 4, 4);
        this.playerTextureAtlas.load();
        this.trailTextureAtlas = new BitmapTextureAtlas(this.activity.getTextureManager(), 4, 4);
        this.trail = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.trailTextureAtlas, this.activity, "trail.png", 0, 0);
        this.trailTextureAtlas.load();
        
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/backgrounds/");
        this.gameBackgroundTextureAtlas = new BitmapTextureAtlas(this.activity.getTextureManager(), BackgroundElement.MAX_WIDTH, BackgroundElement.MAX_HEIGHT * LevelDescriptor.MAX_BACKGROUND_ELEMENTS);
        int index = 0;
        for(BackgroundElement background : level.getBackgrounds()){
            if(!this.gameParallaxLayers.containsKey(background.getResourceName())){
                this.gameParallaxLayers.put(background.getResourceName(), BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.gameBackgroundTextureAtlas, this.activity, background.getResourceName() + ".png", 0, index * BackgroundElement.MAX_HEIGHT));
                index++;
            }
        }
        this.gameBackgroundTextureAtlas.load();
        
        AudioManager.getInstance().prepare("mfx/", level.getMusic());
    }

    public void unloadGameResources() {
        this.playerTextureAtlas.unload();
        this.player = null;
        this.trailTextureAtlas.unload();
        this.trail = null;
        this.gameBackgroundTextureAtlas.unload();
        this.gameParallaxLayers.clear();
    }
    
    public static ResourcesManager getInstance(){
        return INSTANCE;
    }
}
