/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.descriptor.utils.xml;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * An {@link TagHandler} is a superclass for all XML Tags. An {@link TagHandler}
 * catches events within an XML Document by name and parses them.
 *
 * You can override the methods beginning with "handle" to perform actions you
 * would like to do whilst parsing.
 *
 * @author Adam Goodchild
 */
public class TagHandler {

    private List<TagHandler> mSubTagHandlerList = null;
    protected boolean mIsTagActive = false;
    private String mTagName = null;
    private Attributes mTagAttributes = null;
    private StringBuffer mCharacterDataBuffer = new StringBuffer();
    private int mTagDepth = 0;
    private int mCurrentTagHandler = -1;

    public TagHandler(String name) {
        mTagName = name;
        mSubTagHandlerList = new ArrayList<TagHandler>();
    }

    final public String getTagName() {
        return mTagName;
    }

    final public boolean getIsTagActive() {
        return mIsTagActive;
    }

    final public Attributes getTagAttributes() {
        return mTagAttributes;
    }

    final public String getCharacterData() {
        return mCharacterDataBuffer.toString();
    }

    public void addChildTagHandler(TagHandler handler) {
        mSubTagHandlerList.add(handler);
    }

    /**
     * Called when the Document is started
     *
     * @throws SAXException
     */
    public void handleStartDocument() throws SAXException {
    }

    /**
     * Called when the Document is ended
     *
     * @throws SAXException
     */
    public void handleEndDocument() throws SAXException {
    }

    /**
     * Called at the start of the Tag
     *
     * @throws SAXException
     */
    public void handleStartTag() throws SAXException {
    }

    /**
     * Called at the end of the Tag
     *
     * @throws SAXException
     */
    public void handleEndTag() throws SAXException {
    }

    /**
     * Called if the Tag has any Character Data (Content)
     *
     * @param data
     * @throws SAXException
     */
    public void handleCharacterData(String data) throws SAXException {
    }

    final protected void startDocument() throws SAXException {
        handleStartDocument();
    }

    final protected void endDocument() throws SAXException {
        handleEndDocument();
    }

    final protected boolean startTag(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (!mIsTagActive) {
            if (mTagName.equals(localName)) {
                mIsTagActive = true;
                mTagDepth = 0;
                mTagAttributes = attributes;
                mCharacterDataBuffer.setLength(0);
                handleStartTag();
                return true;
            }
        } else {
            mTagDepth++;
            if (mCurrentTagHandler >= 0) {
                return mSubTagHandlerList.get(mCurrentTagHandler).startTag(uri, localName, qName, attributes);
            }
            for (int i = 0; i < mSubTagHandlerList.size(); i++) {
                if (mSubTagHandlerList.get(i).startTag(uri, localName, qName, attributes)) {
                    mCurrentTagHandler = i;
                    return true;
                }
            }
        }
        return false;
    }

    final protected boolean endTag(String uri, String localName, String qName) throws SAXException {
        if (mIsTagActive) {
            if (mTagDepth > 0) {
                mTagDepth--;
            }
            if (mCurrentTagHandler >= 0) {
                if (mSubTagHandlerList.get(mCurrentTagHandler).endTag(uri, localName, qName)) {
                    mCurrentTagHandler = -1;
                }
            } else {
                if (mTagName.equals(localName)) {
                    if (getCharacterData().length() > 0) {
                        handleCharacterData(getCharacterData());
                    }
                    handleEndTag();
                    mIsTagActive = false;
                    return true;
                }
            }
        }
        return false;
    }

    final protected void characters(char[] ch, int start, int length) throws SAXException {
        if (mIsTagActive) {
            if (mCurrentTagHandler >= 0) {
                mSubTagHandlerList.get(mCurrentTagHandler).characters(ch, start, length);
            } else {
                mCharacterDataBuffer.append(ch, start, length);
            }
        }
    }

    /**
     * Returns a String Attribute of the specified name
     *
     * @param name Attribute Name
     * @return String Attribute
     */
    final protected String getStringAttribute(String name) {
        int index = getTagAttributes().getIndex("", name);
        if (index >= 0) {
            return getTagAttributes().getValue(index);
        }
        return null;
    }

    /**
     * Returns an Integer Attribute of the specified name
     *
     * @param name Attribute Name
     * @return Integer Attribute
     */
    final protected Integer getIntegerAttribute(String name) {
        int index = getTagAttributes().getIndex("", name);
        if (index >= 0) {
            Integer result = null;
            try {
                result = Integer.parseInt(getTagAttributes().getValue(index));
            } catch (NumberFormatException e) {
            }
            return result;
        }
        return null;
    }

    /**
     * Returns an Boolean Attribute of the specified name
     *
     * @param name Attribute Name
     * @return Boolean Attribute
     */
    final protected Boolean getBooleanAttribute(String name) {
        int index = getTagAttributes().getIndex("", name);
        if (index >= 0) {
            return Boolean.parseBoolean(getTagAttributes().getValue(index));
        }
        return null;
    }
}
