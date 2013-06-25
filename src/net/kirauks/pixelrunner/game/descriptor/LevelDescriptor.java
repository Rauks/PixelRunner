/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.game.descriptor;

import java.util.LinkedList;
import java.util.List;
import org.andengine.util.debug.Debug;
import net.kirauks.pixelrunner.game.descriptor.utils.BackgroundPack;
import net.kirauks.pixelrunner.game.descriptor.utils.Layer;
import net.kirauks.pixelrunner.game.descriptor.utils.World;
import net.kirauks.pixelrunner.game.element.background.BackgroundElement;
import net.kirauks.pixelrunner.game.element.level.LevelElement;

/**
 *
 * @author Karl
 */
public abstract class LevelDescriptor {
    public static final int MAX_BACKGROUND_ELEMENTS = 4;
    public static final int LAYER_HIGH = 30;
    public static final int LAYERS_MAX = 10;
    public static final int PLATFORM_LOW_LAYER = 3;
    
    private final World world;
    private final int levelId;
    
    private LinkedList<BackgroundElement> backgroundElements = new LinkedList<BackgroundElement>();
    
    public LevelDescriptor(){
        this(BackgroundPack.getRamdomBackgroundPack());
    }
    
    public LevelDescriptor(BackgroundPack backgroundPack){
        this(World.ARCADE, 0, backgroundPack);
    }
    public LevelDescriptor(World world, int levelId){
        this(world, levelId, BackgroundPack.getBackgroundPack(world, levelId));
    }
    public LevelDescriptor(World world, int levelId, BackgroundPack backgroundPack){
        for(Layer layer : backgroundPack.getLayers()){
            if(this.backgroundElements.size() >= MAX_BACKGROUND_ELEMENTS){
                Debug.e("Background limit reached in level descriptor.");
            }
            else{
                this.backgroundElements.add(new BackgroundElement(layer.x, layer.y, layer.resName, layer.speed));
            }
        }
        this.world = world;
        this.levelId = levelId;
    }

    public World getWorld() {
        return world;
    }

    public int getLevelId() {
        return levelId;
    }
    
    public List<BackgroundElement> getBackgroundsElements(){
        return this.backgroundElements;
    }    
            
    public abstract void init();
    public abstract LevelElement[] getNext();
    public abstract boolean hasNext();
    public abstract String getMusic();
    public abstract float getSpawnTime();
    public abstract float getSpawnSpeed();
}
