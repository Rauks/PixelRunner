/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.game.descriptor.utils;

import java.util.Random;

/**
 *
 * @author Karl
 */
public enum BackgroundPack{
    MOUNTAIN(Layer.CLOUDS_2.withSpeed(20), Layer.CLOUDS_1.withSpeed(25), Layer.MOUNTAIN_2.withSpeed(30), Layer.MOUNTAIN_1.withSpeed(35)), 
    FOREST(Layer.CLOUDS_2.withSpeed(20), Layer.CLOUDS_1.withSpeed(25), Layer.FOREST_2.withSpeed(30), Layer.FOREST_1.withSpeed(35)), 
    CITY(Layer.CLOUDS_2.withSpeed(20), Layer.CLOUDS_1.withSpeed(25), Layer.CITY_2.withSpeed(30), Layer.CITY_1.withSpeed(35)), 
    DESERT(Layer.CLOUDS_2.withSpeed(20), Layer.CLOUDS_1.withSpeed(25), Layer.DESERT_2.withSpeed(30), Layer.DESERT_1.withSpeed(35)), 
    HILLS(Layer.CLOUDS_2.withSpeed(20), Layer.CLOUDS_1.withSpeed(25), Layer.HILL_2.withSpeed(30), Layer.HILL_1.withSpeed(35)), 
    SWEETS(Layer.SWEET_4.withSpeed(20), Layer.SWEET_3.withSpeed(25), Layer.SWEET_2.withSpeed(30), Layer.SWEET_1.withSpeed(35)), 
    FORTRESS(Layer.CLOUDS_2.withSpeed(20), Layer.FORTRESS_2.withSpeed(25), Layer.FORTRESS_1.withSpeed(30), Layer.FOREST_1.withSpeed(35)),
    SPACE(Layer.STARS_2.withSpeed(20), Layer.STARS_1.withSpeed(25), Layer.PLANETS_2.withSpeed(30), Layer.PLANETS_1.withSpeed(35)),
    TRAINING_JUMP(Layer.JUMP_1.withSpeed(25), Layer.JUMP_2.withSpeed(30)), 
    TRAINING_ROLL(Layer.ROLL_1.withSpeed(25), Layer.ROLL_2.withSpeed(30)), 
    TRAINING_DOUBLEJUMP(Layer.DOUBLEJUMP_1.withSpeed(25), Layer.DOUBLEJUMP_2.withSpeed(30)), 
    TRAINING_PLATFORMS(Layer.JUMP_1.withSpeed(25), Layer.JUMP_2.withSpeed(30));

    private Layer[] backgrounds;
    
    private static Random RANDGEN = new Random();

    private BackgroundPack(Layer... backgrounds){
        this.backgrounds = backgrounds;
    }

    public Layer[] getLayers() {
        return backgrounds;
    }
    
    public static BackgroundPack getBackgroundPack(World world, int levelId){
        switch(world){
            case TRAINING:
                switch(levelId){
                    case 1:
                        return BackgroundPack.TRAINING_JUMP;
                    case 2:
                        return BackgroundPack.TRAINING_ROLL;
                    case 3:
                        return BackgroundPack.TRAINING_DOUBLEJUMP;
                    default:
                    case 4:
                        return BackgroundPack.TRAINING_PLATFORMS;
                }
            case MOUNTAINS:
                return BackgroundPack.MOUNTAIN;
            case DESERT:
                return BackgroundPack.DESERT;
            case CITY:
                return BackgroundPack.CITY;
            case FOREST:
                return BackgroundPack.FOREST;
            case HILLS:
                return BackgroundPack.HILLS;
            case SWEETS:
                return BackgroundPack.SWEETS;
            case FORTRESS:
                return BackgroundPack.FORTRESS;
            default:
            case SPACE:
                return BackgroundPack.SPACE;
        }
    }
    
    public static BackgroundPack getRamdomBackgroundPack(){
        switch(RANDGEN.nextInt(8)){
            case 0:
                return BackgroundPack.MOUNTAIN;
            case 1:
                return BackgroundPack.DESERT;
            case 2:
                return BackgroundPack.CITY;
            case 3:
                return BackgroundPack.FOREST;
            case 4:
                return BackgroundPack.HILLS;
            case 5:
                return BackgroundPack.SWEETS;
            case 6:
                return BackgroundPack.FORTRESS;
            default:
            case 7:
                return BackgroundPack.SPACE;
        }
    }
}
