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
public class SuccessDatabase extends Database{
    public static final String SUCCESS_DB_NAME = "success";
    
    public SuccessDatabase(Context context){
        super(context, SUCCESS_DB_NAME);
    }
    
    public boolean getSuccessStatus(String success){
        return super.getBoolean(success, false);
    }
    
    public void unlockSuccess(String success){
        super.setBoolean(success, true);
    }
}
