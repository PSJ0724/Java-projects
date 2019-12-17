package project;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import javax.xml.xpath.*;

import java.io.*;
import java.util.*;

public class XmlSoup {
	
	public static Map[] maps;

	
	public static void xmlParse() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
		factory.setNamespaceAware(true);
		
		DocumentBuilder builder;
		Document doc = null;
		try {
			String xml = RssCroller.readXmlFromUrl("http://www.kma.go.kr/wid/queryDFS.jsp?gridx="+ RssCroller.gx +"&gridy=" + RssCroller.gy);
			InputSource is = new InputSource(new StringReader(xml));
			
			builder = factory.newDocumentBuilder();
			doc = builder.parse(is);
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			XPathExpression expr = xpath.compile("//body/data");
			
			NodeList nodelist = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			maps = new  Map[nodelist.getLength()];
			
			
			for(int i = 0; i< nodelist.getLength(); i++) {
				NodeList child = nodelist.item(i).getChildNodes();
				maps[i] = new HashMap<String, String>();
				
				for(int j = 0; j<child.getLength(); j++) {
					Node node = child.item(j);
					if(node.getNodeType() == Node.ELEMENT_NODE) {
						maps[i].put(node.getNodeName(), node.getTextContent());
						//xml의 각 요소를 자료형에 담는다. 
					}
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
