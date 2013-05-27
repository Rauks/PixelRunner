/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.descriptor;

import java.util.Iterator;
import org.andengine.util.adt.list.SmartList;
import org.game.runner.game.element.level.BonusJump;
import org.game.runner.game.element.level.BonusLife;
import org.game.runner.game.element.level.BonusSlow;
import org.game.runner.game.element.level.BonusSpeed;
import org.game.runner.game.element.level.LevelElement;
import org.game.runner.game.element.level.Platform;
import org.game.runner.game.element.level.Rocket;
import org.game.runner.game.element.level.Trap;
import org.game.runner.game.element.level.Wall;

/**
 *
 * @author Karl
 */
public class ScriptedLevelDescriptor extends LevelDescriptor{
    private class Element{
        public int id;
        public int layer;

        public Element(int id, int layer) {
            this.id = id;
            this.layer = layer;
        }
    }
    
    private SmartList<SmartList<Element>> script;
    private Iterator<SmartList<Element>> scriptIterator;
    
    public ScriptedLevelDescriptor(int worldId, int levelId){
        super(null);
        this.script = new SmartList<SmartList<Element>>();
        //Script building
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getSpawnTime() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getSpawnSpeed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
