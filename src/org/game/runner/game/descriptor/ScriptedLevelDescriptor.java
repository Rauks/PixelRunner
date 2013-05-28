/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.descriptor;

import android.content.Context;
import org.andengine.util.debug.Debug;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.FileUtils;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.game.runner.game.descriptor.utils.BackgroundPack;
import org.game.runner.game.descriptor.utils.xml.LevelHandler;
import org.game.runner.game.descriptor.utils.xml.LevelHandler.Element;
import org.game.runner.game.descriptor.utils.xml.MinimizeXMLParser;
import org.game.runner.game.element.level.BonusJump;
import org.game.runner.game.element.level.BonusLife;
import org.game.runner.game.element.level.BonusSlow;
import org.game.runner.game.element.level.BonusSpeed;
import org.game.runner.game.element.level.LevelElement;
import org.game.runner.game.element.level.Platform;
import org.game.runner.game.element.level.Rocket;
import org.game.runner.game.element.level.Trap;
import org.game.runner.game.element.level.Wall;
import org.xml.sax.SAXException;

/**
 *
 * @author Karl
 */
public class ScriptedLevelDescriptor extends LevelDescriptor{
    private final int worldId;
    private final int levelId;
    private final float spawnTime;
    
    private SmartList<SmartList<Element>> script;
    private Iterator<SmartList<Element>> scriptIterator;
    
    public ScriptedLevelDescriptor(int worldId, int levelId, final Context pContext){
        super(BackgroundPack.getBackgroundPack(worldId, levelId));
        this.worldId = worldId;
        this.levelId = levelId;
        
        LevelHandler levelHandler = new LevelHandler();
        try {
            MinimizeXMLParser parser = new MinimizeXMLParser();
            parser.setElementHandler(levelHandler);
            parser.parse(new InputStreamReader(pContext.getAssets().open("xml/game/" + this.worldId + "-" + this.levelId + ".xml")));
        } catch (Exception ex) {
            Logger.getLogger(ScriptedLevelDescriptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.spawnTime = levelHandler.getSpawnTime();
        this.script = levelHandler.getScript();
        
        Debug.d(this.script.toString());
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
            default:
            case 8:
                return new Wall(layer);
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
        return this.worldId + "-" + this.levelId + ".xm";
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
