/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.manager.db;

import android.content.Context;
import net.kirauks.pixelrunner.manager.db.SuccessDatabase.Success;
import net.kirauks.pixelrunner.scene.ArcadeGameScene;

/**
 *
 * @author Karl
 */
public class ArcadeScoreDatabase extends Database{
    protected static final String HIGHSCORE_DB_NAME = "highscore";
    protected static final String HIGHSCORE_LABEL = "score";
    
    public ArcadeScoreDatabase(Context context){
        super(context, HIGHSCORE_DB_NAME);
    }
    
    public long get(){
        return super.getLong(HIGHSCORE_LABEL, 0);
    }
    
    public void set(long value){
        super.setLong(HIGHSCORE_LABEL, value);
        SuccessDatabase successDb = new SuccessDatabase(this.getContext());
        float score = value * ArcadeGameScene.SCORE_CORRECTION_FACTOR;
        if(score >= 1000){
            successDb.unlockSuccess(Success.ARCADE_BRONZE);
        }
        if(score >= 5000){
            successDb.unlockSuccess(Success.ARCADE_SILVER);
        }
        if(score >= 10000){
            successDb.unlockSuccess(Success.ARCADE_GOLD);
        }
        if(score >= 25000){
            successDb.unlockSuccess(Success.ARCADE_CRAZY);
        }
        if(score >= 50000){
            successDb.unlockSuccess(Success.ARCADE_MORDOR);
        }
        if(score >= 100000){
            successDb.unlockSuccess(Success.ARCADE_WTF);
        }
    }
}
