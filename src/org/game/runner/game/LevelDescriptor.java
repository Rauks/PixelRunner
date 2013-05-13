/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game;

import org.game.runner.game.element.LevelElement;

/**
 *
 * @author Karl
 */
public abstract class LevelDescriptor {
    public abstract void start();
    public abstract LevelElement getNext();
    public abstract boolean hasNext();
}
