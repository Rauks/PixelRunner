/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.descriptor;

import java.util.Random;
import org.game.runner.game.element.background.BackgroundElement;
import org.game.runner.game.element.level.BonusJump;
import org.game.runner.game.element.level.BonusLife;
import org.game.runner.game.element.level.BonusSlow;
import org.game.runner.game.element.level.BonusSpeed;
import org.game.runner.game.element.level.LevelElement;
import org.game.runner.game.element.level.Platform;
import org.game.runner.game.element.level.Trap;

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
        switch(this.ranGen.nextInt(10)){
            case 0:
                return new BonusJump(0);
            case 1:
                return new BonusLife(0);
            case 2:
                return new BonusSlow(0);
            case 3:
                return new BonusSpeed(0);
            case 4:
            case 5:
            case 6:
                return new Platform(1);
            default:
            case 7:
            case 8:
            case 9:
                return new Trap(0);
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
        return 1;
    }

    @Override
    public float getSpawnSpeed() {
        return 500;
    }
}
