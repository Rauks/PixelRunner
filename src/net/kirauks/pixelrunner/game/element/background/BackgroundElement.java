/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.game.element.background;

/**
 *
 * @author Karl
 */
public class BackgroundElement {
    public static final int MAX_WIDTH = 250;
    public static final int MAX_HEIGHT = 120;
    
    public final float x;
    public final float y;
    public final float speed;
    private String resourceName;

    public BackgroundElement(float x, float y, String resourceName, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return this.resourceName;
    }
}
