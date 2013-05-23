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

    public enum Layer{
        CLOUDS_1(0, 220, "clouds_1"),
        CLOUDS_2(0, 240, "clouds_2"),
        CITY_1(0, 0, "city_1"),
        CITY_2(0, 0, "city_2"),
        DESERT_1(0, 0, "desert_1"),
        DESERT_2(0, 0, "desert_2"),
        MOUNTAIN_1(0, 0, "mountain_1"),
        MOUNTAIN_2(0, 0, "mountain_2"),
        FOREST_1(0, 0, "forest_1"),
        FOREST_2(0, 0, "forest_2"),
        HILL_1(0, 0, "hill_1"),
        HILL_2(0, 0, "hill_2");

        public float x;
        public float y;
        public String resName;
        public float speed;

        private Layer(float x, float y, String resName) {
            this.x = x;
            this.y = y;
            this.resName = resName;
            this.speed = 0;
        }

        public Layer withSpeed(float speed){
            this.speed = speed;
            return this;
        }
    }
    private Layer[] backgrounds;

    private BackgroundPack(Layer... backgrounds){
        this.backgrounds = backgrounds;
    }

    public Layer[] getBackgrounds() {
        return backgrounds;
    }
}
