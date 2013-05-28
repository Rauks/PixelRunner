/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.manager.db;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 * @author Karl
 */
public abstract class Database {
    private SharedPreferences db;
    private SharedPreferences.Editor dbEditor;
    
    public Database(Context context, String databaseName){
        this.db = context.getSharedPreferences(databaseName, Context.MODE_PRIVATE);
        this.dbEditor = this.db.edit();
    }
    
    protected long getLong(String label, long defaultValue){
        return this.db.getLong(label, defaultValue);
    }
    
    protected void setLong(String label, long value){
        this.dbEditor.putLong(label, value);
        this.dbEditor.commit();
    }
    
    protected int getInt(String label, int defaultValue){
        return this.db.getInt(label, defaultValue);
    }
    
    protected void setInt(String label, int value){
        this.dbEditor.putInt(label, value);
        this.dbEditor.commit();
    }
}
