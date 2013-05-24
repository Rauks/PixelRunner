/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.manager;

import android.graphics.Color;
import java.util.HashMap;
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
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.game.runner.GameActivity;
import org.game.runner.game.descriptor.LevelDescriptor;
import org.game.runner.game.element.background.BackgroundElement;

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
    private BitmapTextureAtlas rocketTextureAtlas;
    public ITiledTextureRegion rocket;
    private BitmapTextureAtlas trapTextureAtlas;
    public TiledTextureRegion trap;
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
    
    //Textures - Nyan
    private BitmapTextureAtlas nyanTextureAtlas;
    public ITiledTextureRegion nyan;
    
    public static void prepareManager(Engine engine, GameActivity activity, Camera camera, VertexBufferObjectManager vbom){
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }
    
    public void loadFonts(){
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
        this.menuTextureAtlas = new BitmapTextureAtlas(this.activity.getTextureManager(), 240, 240);
        this.mainMenuParallaxLayer1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.menuTextureAtlas, this.activity, "menu_bg_1.png", 0, 0);
        this.mainMenuParallaxLayer2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.menuTextureAtlas, this.activity, "menu_bg_2.png", 120, 0);
        this.mainMenuParallaxLayer3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.menuTextureAtlas, this.activity, "menu_bg_3.png", 0, 120);
        this.mainMenuParallaxLayer4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.menuTextureAtlas, this.activity, "menu_bg_4.png", 120, 120);
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
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/splash/");
        this.splashTextureAtlas = new BitmapTextureAtlas(this.activity.getTextureManager(), 16, 16);
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
        
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/elements/");
        this.rocketTextureAtlas = new BitmapTextureAtlas(this.activity.getTextureManager(), 368, 36);
        this.rocket = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.rocketTextureAtlas, this.activity, "rocket.png", 0, 0, 4, 1);
        this.rocketTextureAtlas.load();
        this.trapTextureAtlas = new BitmapTextureAtlas(this.activity.getTextureManager(), 96, 32);
        this.trap = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.trapTextureAtlas, this.activity, "trap.png", 0, 0, 3, 1);
        this.trapTextureAtlas.load();
        
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/backgrounds/");
        this.gameBackgroundTextureAtlas = new BitmapTextureAtlas(this.activity.getTextureManager(), BackgroundElement.MAX_WIDTH, BackgroundElement.MAX_HEIGHT * LevelDescriptor.MAX_BACKGROUND_ELEMENTS);
        int index = 0;
        for(BackgroundElement background : level.getBackgroundsElements()){
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
    
    public void loadNyanResources(){
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
        this.nyanTextureAtlas = new BitmapTextureAtlas(this.activity.getTextureManager(), 198, 84);
        this.nyan = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.nyanTextureAtlas, this.activity, "nyan.png", 0, 0, 3, 2);
        this.nyanTextureAtlas.load();
    }
    
    public void unloadNyanResources(){
        this.nyanTextureAtlas.unload();
        this.nyan = null;
    }
    
    public static ResourcesManager getInstance(){
        return INSTANCE;
    }
}
