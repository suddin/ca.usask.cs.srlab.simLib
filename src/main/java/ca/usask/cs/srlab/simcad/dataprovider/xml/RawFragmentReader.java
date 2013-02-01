package ca.usask.cs.srlab.simcad.dataprovider.xml;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RawFragmentReader {

	static public Set<RawFragment> readRawFragments(String xmlFileName) {

		final Set<RawFragment> rawFragmentSet = new TreeSet<RawFragment>();
		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {
				boolean found = false;
				RawFragment rawFragment = null;

				public void startElement(String uri, String localName,
						String qName, Attributes attributes)
						throws SAXException {
					if (qName.equalsIgnoreCase("source")) {
						found = true;
					}

					String file = attributes.getValue("file");
					String startLine = attributes.getValue("startline");
					String endLine = attributes.getValue("endline");

					rawFragment = new RawFragment(file, startLine, endLine, null);
				}

				public void characters(char ch[], int start, int length)
						throws SAXException {
					if (found) {
						rawFragment.setContent(new String(ch, start, length).trim());
						rawFragmentSet.add(rawFragment);
						found = false;
					}
				}
			};

			saxParser.parse(xmlFileName, handler);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return rawFragmentSet;
	}
}