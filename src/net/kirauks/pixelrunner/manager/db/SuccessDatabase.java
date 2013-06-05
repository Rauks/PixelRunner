/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.manager.db;

import android.content.Context;
import net.kirauks.pixelrunner.R;

/**
 *
 * @author Karl
 */
public class SuccessDatabase extends Database{
    public static final String SUCCESS_DB_NAME = "success";
    
    public enum Success{
        TUTORIALS("tutorials", R.string.success_tutorials, R.string.success_tutorials_sub),
        MOUNTAINS("mountains", R.string.success_mountains, R.string.success_mountains_sub),
        DESERT("desert", R.string.success_desert, R.string.success_desert_sub),
        CITY("city", R.string.success_city, R.string.success_city_sub),
        FOREST("forest", R.string.success_forest, R.string.success_forest_sub),
        HILLS("hills", R.string.success_hills, R.string.success_hills_sub),
        SWEETS("sweets", R.string.success_sweets, R.string.success_sweets_sub),
        ENDGAME("endgame", R.string.success_endgame, R.string.success_endgame_sub),
        JUKEBOX("jukebox", R.string.success_jukebox, R.string.success_jukebox_sub),
        ARCADE_BRONZE("arcade_bronze", R.string.success_arcade_bronze, R.string.success_arcade_bronze_sub),
        ARCADE_SILVER("arcade_silver", R.string.success_arcade_silver, R.string.success_arcade_silver_sub),
        ARCADE_GOLD("arcade_gold", R.string.success_arcade_gold, R.string.success_arcade_gold_sub),
        ARCADE_CRAZY("arcade_crasy", R.string.success_arcade_crazy, R.string.success_arcade_crazy_sub),
        ARCADE_MORDOR("arcade_mordor", R.string.success_arcade_mordor, R.string.success_arcade_mordor_sub),
        ARCADE_WTF("arcade_wtf", R.string.success_arcade_wtf, R.string.success_arcade_wtf_sub),
        ARCADE_ROLL("arcade_roll", R.string.success_arcade_roll, R.string.success_arcade_roll_sub),
        ARCADE_BONUS("arcade_bonus", R.string.success_arcade_bonus, R.string.success_arcade_bonus_sub),
        ARCADE_ROLL_AND_BONUS("arcade_roll_and_bonus", R.string.success_arcade_roll_and_bonus, R.string.success_arcade_roll_and_bonus_sub),
        NYAN("nyan", R.string.success_nyan, R.string.success_nyan_sub);
        
        private String label;
        private int titleResId;
        private int subTitleResId;
        
        private Success(String label, int titleId, int subTitleResId){
            this.label = label;
            this.titleResId = titleId;
            this.subTitleResId = subTitleResId;
        }
        
        private String getLabel(){
            return this.label;
        }
        
        public int getTitleResId(){
            return this.titleResId;
        }
        public int getSubTitleResId(){
            return this.subTitleResId;
        }
    }
    
    public SuccessDatabase(Context context){
        super(context, SUCCESS_DB_NAME);
    }
    
    public boolean getSuccessStatus(Success success){
        return super.getBoolean(success.getLabel(), false);
    }
    
    public void unlockSuccess(Success success){
        super.setBoolean(success.getLabel(), true);
    }
    
    public void lockSuccess(Success success){
        super.setBoolean(success.getLabel(), false);
    }
}
