package pku.zy.iword.utils;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParserFactory;

public class XMLParser {
    public SAXParserFactory factory = null;
    public XMLReader reader = null;

    public XMLParser() {
        try {
            factory = SAXParserFactory.newInstance();
            reader = factory.newSAXParser().getXMLReader();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseJinShanXml(DefaultHandler content, InputSource inSourse) {
        if (inSourse == null) return;
        try {
            reader.setContentHandler(content);
            reader.parse(inSourse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
