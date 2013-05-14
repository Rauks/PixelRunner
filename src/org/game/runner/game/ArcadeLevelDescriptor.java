/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game;

import org.game.runner.game.element.BackgroundElement;
import org.game.runner.game.element.LevelElement;
import org.game.runner.game.element.LevelElement.LevelElementType;

/**
 *
 * @author Karl
 */
public class ArcadeLevelDescriptor extends LevelDescriptor{
    public ArcadeLevelDescriptor(){
        this.addBackgroundElement(new BackgroundElement(0, 240, "clouds_2", 10));
        this.addBackgroundElement(new BackgroundElement(0, 220, "clouds_1", 15));
        this.addBackgroundElement(new BackgroundElement(0, 50, "mountain_2", 20));
        this.addBackgroundElement(new BackgroundElement(0, 50, "mountain_1", 25));
    }
    
    @Override
    public LevelElement getNext() {
        return new LevelElement(LevelElementType.TRAP_JUMP);
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public void start() {
        
    }

    @Override
    public String getMusic() {
        return "arcade.xm";
    }
}
