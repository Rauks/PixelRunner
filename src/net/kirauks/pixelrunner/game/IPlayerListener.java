/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.game;

/**
 *
 * @author Karl
 */
public interface IPlayerListener {
    public void onJump();
    public void onRoll();
    public void onRollBackJump();
    public void onBonus();
}
