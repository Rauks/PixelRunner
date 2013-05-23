/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.descriptor;

import java.util.LinkedList;
import java.util.List;
import org.andengine.util.debug.Debug;
import org.game.runner.game.descriptor.utils.BackgroundPack;
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
    
    private LinkedList<BackgroundElement> backgroundElements = new LinkedList<BackgroundElement>();
    
    public LevelDescriptor(BackgroundPack backgroundPack){
        for(BackgroundPack.Layer layer : backgroundPack.getLayers()){
            if(this.backgroundElements.size() >= MAX_BACKGROUND_ELEMENTS){
                Debug.e("PixelRunner", "Background limit reached in level descriptor.");
            }
            else{
                this.backgroundElements.add(new BackgroundElement(layer.x, layer.y, layer.resName, layer.speed));
            }
        }
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
