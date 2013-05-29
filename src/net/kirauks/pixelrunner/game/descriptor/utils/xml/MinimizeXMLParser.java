/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.game.descriptor.utils.xml;

import java.io.Reader;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Allows the parsing of an XML Document using {@link TagHandler}'s.
 *
 * @author Adam Goodchild
 */
public class MinimizeXMLParser extends DefaultHandler {

    private SAXParser mSaxParser = null;
    private XMLReader mXMLReader = null;
    private TagHandler mTagHandler = null;

    public MinimizeXMLParser() throws ParserConfigurationException, SAXException, FactoryConfigurationError {
        mSaxParser = SAXParserFactory.newInstance().newSAXParser();
        mXMLReader = mSaxParser.getXMLReader();
        mXMLReader.setContentHandler(this);
    }

    /**
     * Parses an XML Document from a Reader
     *
     * @param reader
     * @throws Exception
     */
    public void parse(Reader reader) throws Exception {
        mXMLReader.parse(new InputSource(reader));
    }

    /**
     * Parses an XML Document from an InputSource
     *
     * @param source
     * @throws Exception
     */
    public void parse(InputSource source) throws Exception {
        mXMLReader.parse(source);
    }

    public void setElementHandler(TagHandler handler) {
        mTagHandler = handler;
    }

    @Override
    public void startDocument() throws SAXException {
        mTagHandler.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        mTagHandler.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        mTagHandler.startTag(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        mTagHandler.endTag(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        mTagHandler.characters(ch, start, length);
    }
}
