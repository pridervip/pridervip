package com.pridervip.gate.utils;

import java.io.StringReader;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLUtils {
	
	public static String getNodeValue(String xml) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(new StringReader(xml));
		Element root = document.getRootElement();
		return root.getText();
	}
}
