/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.manager.db;

import android.content.Context;

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
    
    public int get(int worldId){
        return super.getInt(PROGRESS_LABEL + worldId, 1);
    }
    
    public void set(int worldId, int value){
        super.setInt(PROGRESS_LABEL + worldId, value);
    }
}
