/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.game.descriptor.utils;

import net.kirauks.pixelrunner.GameActivity;
import net.kirauks.pixelrunner.scene.base.BaseGameScene;

/**
 *
 * @author Karl
 */
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
    FORTRESS_1(0, 4, "fortress_1"),
    FORTRESS_2(0, 4, "fortress_2"),
    HILL_1(0, 0, "hill_1"),
    HILL_2(0, 0, "hill_2"),
    SWEET_1(0, 0, "sweet_1"),
    SWEET_2(0, 0, "sweet_2"),
    SWEET_3(0, 280, "sweet_3"),
    SWEET_4(0, 170, "sweet_4"),
    STARS_1(0, 0, "stars_1"),
    STARS_2(0, 0, "stars_2"),
    PLANETS_1(0, 164, "planets_1"),
    PLANETS_2(0, 250, "planets_2"),
    JUMP_1(0, 200, "training_1"),
    JUMP_2(0, 220, "training_2"),
    ROLL_1(0, 10, "training_3"),
    ROLL_2(0, 26, "training_4"),
    DOUBLEJUMP_1(0, 200, "training_5"),
    DOUBLEJUMP_2(0, 220, "training_6"),
    DASH(0, 186, "dash");

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
