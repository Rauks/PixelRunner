/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.element;

/**
 *
 * @author Karl
 */
public class LevelElement {
    public enum LevelElementType{
        TRAP_JUMP;
    }
    
    private LevelElementType type;
    
    public LevelElement(LevelElementType type){
        this.type = type;
    }
    
    public LevelElementType getType(){
        return this.type;
    }
}
