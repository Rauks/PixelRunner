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
public class InfosDatabase extends Database{
    public enum Info{
        LEVEL_CHOICE_PAGE("level-choice-page", 0);
        
        private String label;
        private int defaultValue;
        
        private Info(String label, int defaultValue){
            this.label = label;
            this.defaultValue = defaultValue;
        }

        public String getLabel() {
            return label;
        }

        public int getDefaultValue() {
            return defaultValue;
        }
    }
    
    protected static final String INFOS_DB_NAME = "states";
    protected static final String INFOS_LABEL = "level-choice-page";
    
    public InfosDatabase(Context context){
        super(context, INFOS_DB_NAME);
    }
    
    public int getInfo(Info info){
        return super.getInt(info.getLabel(), info.getDefaultValue());
    }
    
    public void setInfo(Info info, int value){
        super.setInt(info.getLabel(), value);
    }
}
