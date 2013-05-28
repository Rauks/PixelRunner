/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.descriptor.utils;

/**
 *
 * @author Karl
 */
public enum BackgroundPack{
    MOUNTAIN(Layer.CLOUDS_2.withSpeed(20), Layer.CLOUDS_1.withSpeed(25), Layer.MOUNTAIN_2.withSpeed(30), Layer.MOUNTAIN_1.withSpeed(35)), 
    FOREST(Layer.CLOUDS_2.withSpeed(20), Layer.CLOUDS_1.withSpeed(25), Layer.FOREST_2.withSpeed(30), Layer.FOREST_1.withSpeed(35)), 
    CITY(Layer.CLOUDS_2.withSpeed(20), Layer.CLOUDS_1.withSpeed(25), Layer.CITY_2.withSpeed(30), Layer.CITY_1.withSpeed(35)), 
    DESERT(Layer.CLOUDS_2.withSpeed(20), Layer.CLOUDS_1.withSpeed(25), Layer.DESERT_2.withSpeed(30), Layer.DESERT_1.withSpeed(35)), 
    HILL(Layer.CLOUDS_2.withSpeed(20), Layer.CLOUDS_1.withSpeed(25), Layer.HILL_2.withSpeed(30), Layer.HILL_1.withSpeed(35)), 
    TRAINING_JUMP(Layer.JUMP_1.withSpeed(25), Layer.JUMP_2.withSpeed(30)), 
    TRAINING_ROLL(Layer.ROLL_1.withSpeed(25), Layer.ROLL_2.withSpeed(30)), 
    TRAINING_DOUBLEJUMP(Layer.DOUBLEJUMP_1.withSpeed(25), Layer.DOUBLEJUMP_2.withSpeed(30)), 
    TRAINING_PLATFORMS(Layer.JUMP_1.withSpeed(25), Layer.JUMP_2.withSpeed(30));

    private Layer[] backgrounds;

    private BackgroundPack(Layer... backgrounds){
        this.backgrounds = backgrounds;
    }

    public Layer[] getLayers() {
        return backgrounds;
    }
    
    public static BackgroundPack getBackgroundPack(int worldId, int levelId){
        switch(worldId){
            case 0:
                switch(levelId){
                    case 0:
                        return BackgroundPack.TRAINING_JUMP;
                    case 1:
                        return BackgroundPack.TRAINING_ROLL;
                    case 2:
                        return BackgroundPack.TRAINING_DOUBLEJUMP;
                    default:
                    case 3:
                        return BackgroundPack.TRAINING_PLATFORMS;
                }
            case 1:
                return BackgroundPack.MOUNTAIN;
            case 2:
                return BackgroundPack.DESERT;
            case 3:
                return BackgroundPack.CITY;
            case 4:
                return BackgroundPack.FOREST;
            default:
            case 5:
                //return BackgroundPack.SWEETS;
                return null;
        }
    }
}
