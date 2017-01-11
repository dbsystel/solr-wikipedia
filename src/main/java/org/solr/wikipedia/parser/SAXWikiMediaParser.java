package org.solr.wikipedia.parser;

import org.solr.wikipedia.handler.PageHandler;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.Reader;

import de.tudarmstadt.ukp.jwktl.api.entry.WikiString;
/**
 * SAX-based implementation of WikiMediaXMLParser.
 *
 * @author bryanbende
 */
public class SAXWikiMediaParser<T> implements WikiMediaXMLParser<T> {

    @Override
    public void parse(Reader reader, PageHandler<T> handler) {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        try {
            SAXParser saxParser = spf.newSAXParser();

            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(new PageContentHandler(handler));
            xmlReader.parse(new InputSource(reader));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ContentHandler that wraps a PageHandler.
     */
    private class PageContentHandler extends DefaultHandler {

        private PageHandler<T> handler;

        private PageElement currElement;

        private StringBuilder buffer;

        public PageContentHandler(PageHandler<T> handler) {
            this.handler = handler;
            this.buffer = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            try {
                currElement = PageElement.valueOf(localName);
                switch(currElement) {
                    case page:
                        handler.startPage();
                        break;
                    case revision:
                        handler.startRevision();
                        break;
                    default:
                        break;
                }
            } catch(IllegalArgumentException e) {
                currElement = null;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            try {
                currElement = PageElement.valueOf(localName);
                switch(currElement) {
                    case page:
                        handler.endPage();
                        break;
                    case revision:
                        handler.endRevision();
                        break;
                    case title:
                        String title = buffer.toString();
			WikiString wTitle = new WikiString(title.trim());
                        handler.title(wTitle.getPlainText());
                        break;
                    case text:
                        String text = buffer.toString();
			WikiString wText = new WikiString(text.trim());
                        handler.text(wText.getPlainText());
                        break;
                    case timestamp:
                        String timestamp = buffer.toString();
                        handler.timestamp(timestamp);
                        break;
                    default:
                        break;
                }
            } catch(IllegalArgumentException e) {
                currElement = null;
            }

            currElement = null;
            buffer = new StringBuilder();
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (currElement == null) {
                return;
            }

            for(int i=start; i < (start+length); i++) {
                if (ch[i] != '\n') {
                    buffer.append(ch[i]);
                }
            }
        }
    }
}
