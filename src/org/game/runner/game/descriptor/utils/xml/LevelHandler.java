/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.descriptor.utils.xml;

import org.andengine.util.adt.list.SmartList;
import org.game.runner.game.descriptor.ScriptedLevelDescriptor;
import org.xml.sax.SAXException;

/**
 *
 * @author Karl
 */
public class LevelHandler extends TagHandler{
    private static final String TAG_ROOT = "lvl";
    private static final String TAG_ROOT_SPAWNTIME = "lvl";
    private static final String TAG_SCRIPTLINE = "e";
    private static final String TAG_ELEMENT = "o";
    private static final String TAG_ELEMENT_TYPE = "t";
    private static final String TAG_ELEMENT_LAYER = "l";
    
    public class Element{
        public int id;
        public int layer;

        public Element(int id, int layer) {
            this.id = id;
            this.layer = layer;
        }
        
        @Override
        public String toString(){
            return this.id + "(" + this.layer + ")";
        }
    }
    
    private float spawnTime = 1f;
    private SmartList<SmartList<Element>> script = new SmartList<SmartList<Element>>();
    private SmartList<Element> scriptLine;
    
    public LevelHandler() {
        super(TAG_ROOT);
        
        this.addChildTagHandler(this.getScriptLineHandler());
    }
    
    public SmartList<SmartList<Element>> getScript(){
        return this.script;
    }
    public float getSpawnTime(){
        return this.spawnTime;
    }

    @Override
    public void handleStartTag() throws SAXException {
        super.handleStartTag();
        //this.spawnTime = Float.parseFloat()this.getStringAttribute(TAG_ROOT_SPAWNTIME);
    }

    @Override
    public void handleEndTag() throws SAXException {
        super.handleEndTag();
    }

    private TagHandler getScriptLineHandler() {
        TagHandler elementHandler = new TagHandler(TAG_SCRIPTLINE) {
            @Override
            public void handleStartTag() throws SAXException {
                super.handleStartTag();
                LevelHandler.this.scriptLine = new SmartList<Element>();
                
                this.addChildTagHandler(LevelHandler.this.getElementHandler());
            }

            @Override
            public void handleEndTag() throws SAXException {
                super.handleEndTag();
                LevelHandler.this.script.add(LevelHandler.this.scriptLine);
                LevelHandler.this.scriptLine = null;
            }
                };
                return elementHandler;
            }
    
    private TagHandler getElementHandler() {
        TagHandler elementHandler = new TagHandler(TAG_ELEMENT) {
            @Override
            public void handleStartTag() throws SAXException {
                Element e = new Element(this.getIntegerAttribute(TAG_ELEMENT_TYPE), this.getIntegerAttribute(TAG_ELEMENT_LAYER));
                LevelHandler.this.scriptLine.add(e);
            }
        };
        return elementHandler;
    }
}
