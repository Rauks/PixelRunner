/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.manager.db;

import android.content.Context;
import net.kirauks.pixelrunner.game.descriptor.utils.World;
import net.kirauks.pixelrunner.manager.db.SuccessDatabase.Success;

/**
 *
 * @author Karl
 */
public class ProgressDatabase extends Database{
    public static final String PROGRESS_DB_NAME = "progress";
    public static final String PROGRESS_LABEL = "world-";
    
    public ProgressDatabase(Context context){
        super(context, PROGRESS_DB_NAME);
    }
    
    public int get(World world){
        return super.getInt(PROGRESS_LABEL + world, 1);
    }
    
    public void set(World world, int value){
        super.setInt(PROGRESS_LABEL + world, value);
        SuccessDatabase successDb = new SuccessDatabase(this.getContext());
        switch(world){
            case TRAINING:
                successDb.unlockSuccess(Success.TUTORIALS);
                break;
            case MOUNTAINS:
                if(value > 12){
                    successDb.unlockSuccess(Success.MOUNTAINS);
                }
                break;
            case DESERT:
                if(value > 12){
                    successDb.unlockSuccess(Success.DESERT);
                }
                break;
            case FOREST:
                if(value > 12){
                    successDb.unlockSuccess(Success.FOREST);
                }
                break;
            case CITY:
                if(value > 12){
                    successDb.unlockSuccess(Success.CITY);
                }
                break;
            case HILLS:
                if(value > 12){
                    successDb.unlockSuccess(Success.HILLS);
                }
                break;
            case SWEETS:
                if(value > 12){
                    successDb.unlockSuccess(Success.SWEETS);
                }
                break;
            case ISLANDS:
                if(value > 12){
                    successDb.unlockSuccess(Success.ISLANDS);
                }
                break;
            case FORTRESS:
                if(value > 12){
                    successDb.unlockSuccess(Success.FORTRESS);
                }
                break;
            case SPACE:
                if(value > 12){
                    successDb.unlockSuccess(Success.SPACE);
                }
                break;
        }
        if(successDb.getSuccessStatus(Success.MOUNTAINS) && successDb.getSuccessStatus(Success.DESERT)
                && successDb.getSuccessStatus(Success.FOREST) && successDb.getSuccessStatus(Success.CITY)
                && successDb.getSuccessStatus(Success.HILLS) && successDb.getSuccessStatus(Success.SWEETS)
                && successDb.getSuccessStatus(Success.ISLANDS) && successDb.getSuccessStatus(Success.FORTRESS)
                && successDb.getSuccessStatus(Success.SPACE)){
            successDb.unlockSuccess(Success.ENDGAME);
        }
    }
}
