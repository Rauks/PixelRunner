/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.element.level;

import org.andengine.entity.IEntity;
import org.game.runner.game.player.Player;

/**
 *
 * @author Karl
 */
public abstract class LevelElementCollisionCallback {
    Player player;
    
    public LevelElementCollisionCallback(Player player){
        this.player = player;
    }
    
    public boolean playerCollideWith(IEntity entity){
        return this.player.collidesWith(entity);
    }
    
    public Player getPlayer(){
        return this.player;
    }
    
    public abstract void onPlayerCollided();
}
