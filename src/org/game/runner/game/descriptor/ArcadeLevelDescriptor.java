/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.descriptor;

import java.util.Random;
import org.game.runner.game.descriptor.utils.BackgroundPack;
import org.game.runner.game.descriptor.utils.BackgroundPack.Layer;
import org.game.runner.game.element.background.BackgroundElement;
import org.game.runner.game.element.level.BonusJump;
import org.game.runner.game.element.level.BonusLife;
import org.game.runner.game.element.level.BonusSlow;
import org.game.runner.game.element.level.BonusSpeed;
import org.game.runner.game.element.level.LevelElement;
import org.game.runner.game.element.level.Platform;
import org.game.runner.game.element.level.Rocket;
import org.game.runner.game.element.level.Trap;
import org.game.runner.game.element.level.Wall;

/**
 *
 * @author Karl
 */
public class ArcadeLevelDescriptor extends LevelDescriptor{
    private enum PrevState{
        BONUS,
        TRAP,
        PLATFORM
    }
    
    public ArcadeLevelDescriptor(){
        super(BackgroundPack.values()[new Random().nextInt(BackgroundPack.values().length)]);
    }
    
    private Random ranGen = new Random();
    private PrevState prevState;
    private int platLayer;
    
    @Override
    public LevelElement[] getNext() {
        int rand;
        switch(this.prevState){
            case BONUS: //WAS BONUS
                rand = ranGen.nextInt(100);
                if(rand < 80){ // Rocket @ 80%
                    this.prevState = PrevState.TRAP;
                    return new LevelElement[]{this.getTrap()};
                }
                else{ // Platform @ 20%
                    this.prevState = PrevState.PLATFORM;
                    this.platLayer = 2;
                    return new LevelElement[]{new Platform(this.platLayer)};
                }
            case TRAP: //WAS TRAP
                rand = ranGen.nextInt(100);
                if(rand < 5){ // Bonus @ layer 3 @ 5%
                    this.prevState = PrevState.BONUS;
                    return new LevelElement[]{this.getBonus(3)};
                }
                else if(rand >=5 && rand < 80){ // Rocket @ 75%
                    this.prevState = PrevState.TRAP;
                    return new LevelElement[]{this.getTrap()};
                }
                else{ // Platform @ 20%
                    this.prevState = PrevState.PLATFORM;
                    this.platLayer = 2;
                    return new LevelElement[]{new Platform(this.platLayer)};
                }
            default: //WAS PLATFORM
            case PLATFORM:
                rand = ranGen.nextInt(100);
                int deviation = (int)(((float)this.platLayer / (float)LevelDescriptor.LAYERS_MAX) * 50f); // 0% @ layer 0, 70% @ layer max
                if(rand < (100 - deviation)){ // Platform @ 100% ~ 50%
                    this.prevState = PrevState.PLATFORM;
                    rand = ranGen.nextInt(100);
                    if(rand < 90 && this.platLayer != LevelDescriptor.LAYERS_MAX){ // Plateform @ layer +1 @ 90%
                        this.platLayer++;
                    }
                    if(this.platLayer > 5){
                        return new LevelElement[]{new Platform(this.platLayer), this.getTrap()};
                    }
                    else if(this.platLayer > 3){
                        return new LevelElement[]{new Platform(this.platLayer), this.getSmallTrap()};
                    }
                    else{
                        return new LevelElement[]{new Platform(this.platLayer)};
                    }
                }
                else{ // Bonus or trap @ 0% ~ 50%
                    rand = ranGen.nextInt(100);
                    if(rand < 70){ // Bonus @ layer +3 @ 70%
                        this.prevState = PrevState.BONUS;
                        return new LevelElement[]{this.getBonus(this.platLayer + 3)};
                    }
                    else{ // Rocket @ layer 0 @ 30%
                        this.prevState = PrevState.TRAP;
                        return new LevelElement[]{this.getTrap()};
                    }
                }
        }
    }
    private LevelElement getBonus(int layer){
        switch(this.ranGen.nextInt(4)){
            case 0:
                return new BonusJump(layer);
            case 1:
                return new BonusLife(layer);
            case 2:
                return new BonusSlow(layer);
            default:
            case 3:
                return new BonusSpeed(layer);
        }
    }
    private LevelElement getTrap(){
        switch(this.ranGen.nextInt(3)){
            case 0:
                return new Rocket(0);
            case 1:
                return new Wall(0);
            default:
            case 2:
                return new Trap(0);
        }
    }
    private LevelElement getSmallTrap(){
        switch(this.ranGen.nextInt(2)){
            case 0:
                return new Rocket(0);
            default:
            case 1:
                return new Trap(0);
        }
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public void init() {
        this.prevState = PrevState.TRAP;
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
