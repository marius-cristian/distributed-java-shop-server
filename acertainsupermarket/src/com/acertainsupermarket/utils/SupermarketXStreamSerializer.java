package com.acertainsupermarket.utils;

import java.io.IOException;

import com.acertainsupermarket.interfaces.SupermarketSerializer;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;


public class SupermarketXStreamSerializer implements SupermarketSerializer {
	/** The XML stream. */
	private final XStream xmlStream = new XStream(new StaxDriver());
	
	
	@Override
	public byte[] serialize(Object object) throws IOException {
		String xml = xmlStream.toXML(object);
		return xml.getBytes();

	}

	@Override
	public Object deserialize(byte[] bytes) throws IOException {
		String xml = new String(bytes);
		return xmlStream.fromXML(xml);
	}

}
