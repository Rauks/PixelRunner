/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene.base.utils.ease;

import org.andengine.util.modifier.ease.IEaseFunction;

/**
 *
 * @author Karl
 */
public class EaseBroadcast implements IEaseFunction{
    private static EaseBroadcast INSTANCE;
    private EaseBroadcast(){
    }

    public static EaseBroadcast getInstance() {
            if (INSTANCE == null) {
                    INSTANCE = new EaseBroadcast();
            }
            return INSTANCE;
    }
    
    @Override
    public float getPercentage(float pSecondsElapsed, float pDuration) {
        final float t = pSecondsElapsed / pDuration;
        final float ts = t * t;
	final float tc = ts * t;
	return (float)(-8.5*tc*ts + 24.5*ts*ts + -22*tc + 6*ts + t);
	//return (float)(-4*tc*ts + 10.5*ts*ts + -6*tc + -2*ts + 2.5*t);
    }
}
