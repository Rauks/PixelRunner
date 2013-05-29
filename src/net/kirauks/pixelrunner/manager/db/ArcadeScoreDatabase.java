/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.manager.db;

import android.content.Context;

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
    }
}
