/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.manager.db;

import android.content.Context;
import net.kirauks.pixelrunner.game.descriptor.utils.World;

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
    }
}
