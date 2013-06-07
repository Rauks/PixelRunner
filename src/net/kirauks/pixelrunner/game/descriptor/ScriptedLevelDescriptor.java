/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.game.descriptor;

import android.content.Context;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.andengine.util.adt.list.SmartList;
import net.kirauks.pixelrunner.game.descriptor.utils.World;
import net.kirauks.pixelrunner.game.descriptor.utils.xml.LevelHandler;
import net.kirauks.pixelrunner.game.descriptor.utils.xml.LevelHandler.Element;
import net.kirauks.pixelrunner.game.descriptor.utils.xml.MinimizeXMLParser;
import net.kirauks.pixelrunner.game.element.level.BonusJump;
import net.kirauks.pixelrunner.game.element.level.BonusLife;
import net.kirauks.pixelrunner.game.element.level.BonusSlow;
import net.kirauks.pixelrunner.game.element.level.BonusSpeed;
import net.kirauks.pixelrunner.game.element.level.BonusSwap;
import net.kirauks.pixelrunner.game.element.level.LevelElement;
import net.kirauks.pixelrunner.game.element.level.Platform;
import net.kirauks.pixelrunner.game.element.level.Rocket;
import net.kirauks.pixelrunner.game.element.level.Trap;
import net.kirauks.pixelrunner.game.element.level.Wall;

/**
 *
 * @author Karl
 */
public class ScriptedLevelDescriptor extends LevelDescriptor{
    private final float spawnTime;
    
    private SmartList<SmartList<Element>> script;
    private Iterator<SmartList<Element>> scriptIterator;
    
    public ScriptedLevelDescriptor(World world, int levelId, final Context pContext){
        super(world, levelId);
        
        LevelHandler levelHandler = new LevelHandler();
        try {
            MinimizeXMLParser parser = new MinimizeXMLParser();
            parser.setElementHandler(levelHandler);
            parser.parse(new InputStreamReader(pContext.getAssets().open("xml/game/" + this.getWorld() + "-" + this.getLevelId() + ".xml")));
        } catch (Exception ex) {
            Logger.getLogger(ScriptedLevelDescriptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.spawnTime = levelHandler.getSpawnTime();
        this.script = levelHandler.getScript();
    }
    
    private LevelElement build(int id, int layer){
        switch(id){
            case 1:
                return new BonusJump(layer);
            case 2:
                return new BonusLife(layer);
            case 3:
                return new BonusSlow(layer);
            case 4:
                return new BonusSpeed(layer);
            case 5:
                return new Platform(layer);
            case 6:
                return new Rocket(layer);
            case 7:
                return new Trap(layer);
            case 8:
                return new Wall(layer);
            default:
            case 9:
                return new BonusSwap(layer);
        }
    }
    private LevelElement[] buildArray(SmartList<Element> elements){
        SmartList<LevelElement> build = new SmartList<LevelElement>();
        for(Element element : elements){
            build.add(this.build(element.id, element.layer));
        }
        return build.toArray(new LevelElement[build.size()]);
    }
    
    @Override
    public void init() {
        this.scriptIterator = this.script.iterator();
    }

    @Override
    public LevelElement[] getNext() {
        return this.buildArray(this.scriptIterator.next());
    }

    @Override
    public boolean hasNext() {
        return this.scriptIterator.hasNext();
    }

    @Override
    public String getMusic() {
        return this.getWorld() + "-" + this.getLevelId() + ".xm";
    }

    @Override
    public float getSpawnTime() {
        return this.spawnTime;
    }

    @Override
    public float getSpawnSpeed() {
        return 500;
    }
    
}
