/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game;

import org.game.runner.game.element.LevelElement;
import org.game.runner.game.element.LevelElement.LevelElementType;

/**
 *
 * @author Karl
 */
public class ArcadeLevelDescriptor extends LevelDescriptor{
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
