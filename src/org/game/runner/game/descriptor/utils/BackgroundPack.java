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
    HILL(Layer.CLOUDS_2.withSpeed(20), Layer.CLOUDS_1.withSpeed(25), Layer.HILL_2.withSpeed(30), Layer.HILL_1.withSpeed(35));

    private Layer[] backgrounds;

    private BackgroundPack(Layer... backgrounds){
        this.backgrounds = backgrounds;
    }

    public Layer[] getLayers() {
        return backgrounds;
    }
}
