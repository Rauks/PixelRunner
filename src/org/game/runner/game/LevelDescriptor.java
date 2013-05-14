/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game;

import android.util.Log;
import java.util.LinkedList;
import java.util.List;
import org.andengine.util.debug.Debug;
import org.game.runner.game.element.BackgroundElement;
import org.game.runner.game.element.LevelElement;

/**
 *
 * @author Karl
 */
public abstract class LevelDescriptor {
    public static final int MAX_BACKGROUND_ELEMENTS = 5;
    
    private List<BackgroundElement> backgrounds = new LinkedList<BackgroundElement>();
    
    public List<BackgroundElement> getBackgrounds(){
        return this.backgrounds;
    }
    public void addBackgroundElement(BackgroundElement background){
        if(this.backgrounds.size() >= MAX_BACKGROUND_ELEMENTS){
            Debug.e("PixelRunner", "Background limit reached in level descriptor.");
        }
        else{
            this.backgrounds.add(background);
        }
    }        
            
    public abstract void start();
    public abstract LevelElement getNext();
    public abstract boolean hasNext();
    public abstract String getMusic();
}
