/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.element;

import org.andengine.util.adt.color.Color;

/**
 *
 * @author Karl
 */
public enum LevelElement {
    TRAP_JUMP, BONUS_SPEED(Color.RED), BONUS_SLOW(Color.YELLOW), BONUS_LIFE(Color.GREEN), BONUS_JUMP(Color.BLUE);
        
    private Color color;
    
    private LevelElement(Color color){
        this.color = color;
    }
    private LevelElement(){
        this(Color.WHITE);
    }
    
    public Color getColor(){
        return this.color;
    }
    
}
