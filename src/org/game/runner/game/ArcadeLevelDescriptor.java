/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game;

import java.util.Random;
import org.game.runner.game.element.BackgroundElement;
import org.game.runner.game.element.LevelElement;

/**
 *
 * @author Karl
 */
public class ArcadeLevelDescriptor extends LevelDescriptor{
    public ArcadeLevelDescriptor(){
        this.addBackgroundElement(new BackgroundElement(0, 190, "clouds_2", 20));
        this.addBackgroundElement(new BackgroundElement(0, 170, "clouds_1", 25));
        this.addBackgroundElement(new BackgroundElement(0, 0, "mountain_2", 30));
        this.addBackgroundElement(new BackgroundElement(0, 0, "mountain_1", 35));
    }
    
    private Random ranGen = new Random();
    
    @Override
    public LevelElement getNext() {
        switch(this.ranGen.nextInt(5)){
            case 0:
                return LevelElement.TRAP_JUMP;
            case 1:
                return LevelElement.BONUS_JUMP;
            case 2:
                return LevelElement.BONUS_LIFE;
            case 3:
                return LevelElement.BONUS_SLOW;
            case 4:
                return LevelElement.BONUS_SPEED;
            default:
                return LevelElement.TRAP_JUMP;
        }
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public void init() {
        
    }

    @Override
    public String getMusic() {
        return "arcade.xm";
    }

    @Override
    public float getSpawnTime() {
        return 3;
    }

    @Override
    public float getSpawnSpeed() {
        return 500;
    }
}
