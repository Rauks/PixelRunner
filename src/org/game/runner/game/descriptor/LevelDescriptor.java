/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.descriptor;

import java.util.LinkedList;
import java.util.List;
import org.andengine.util.debug.Debug;
import org.game.runner.game.descriptor.utils.BackgroundPack;
import org.game.runner.game.descriptor.utils.Layer;
import org.game.runner.game.element.background.BackgroundElement;
import org.game.runner.game.element.level.LevelElement;

/**
 *
 * @author Karl
 */
public abstract class LevelDescriptor {
    public static final int MAX_BACKGROUND_ELEMENTS = 5;
    public static final int LAYER_HIGH = 30;
    public static final int LAYERS_MAX = 10;
    public static final int PLATFORM_LOW_LAYER = 3;
    
    private final int worldId;
    private final int levelId;
    
    private LinkedList<BackgroundElement> backgroundElements = new LinkedList<BackgroundElement>();
    
    public LevelDescriptor(){
        this(BackgroundPack.getRamdomBackgroundPack());
    }
    
    public LevelDescriptor(BackgroundPack backgroundPack){
        this(0, 0, backgroundPack);
    }
    public LevelDescriptor(int worldId, int levelId){
        this(worldId, levelId, BackgroundPack.getBackgroundPack(worldId, levelId));
    }
    public LevelDescriptor(int worldId, int levelId, BackgroundPack backgroundPack){
        for(Layer layer : backgroundPack.getLayers()){
            if(this.backgroundElements.size() >= MAX_BACKGROUND_ELEMENTS){
                Debug.e("Background limit reached in level descriptor.");
            }
            else{
                this.backgroundElements.add(new BackgroundElement(layer.x, layer.y, layer.resName, layer.speed));
            }
        }
        this.worldId = worldId;
        this.levelId = levelId;
    }

    public int getWorldId() {
        return worldId;
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
