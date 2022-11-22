package com.acertainsupermarket.utils;

import java.io.IOException;

import com.acertainsupermarket.business.ImmutableItem;
import com.acertainsupermarket.interfaces.SupermarketSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;

public class SupermarketKryoSerializer implements SupermarketSerializer {
	/** The binary stream. */
	private final Kryo binaryStream;

	/**
	 * Instantiates a new {@link BookStoreKryoSerializer}.
	 */
	public SupermarketKryoSerializer() {
		binaryStream = new Kryo();
		binaryStream.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
		binaryStream.register(ImmutableItem.class);
	}
	
	@Override
	public byte[] serialize(Object object) throws IOException {
		try (ByteArrayOutputStream outStream = new ByteArrayOutputStream(); Output out = new Output(outStream)) {
			binaryStream.writeClassAndObject(out, object);
			out.flush();
			return outStream.toByteArray();
		}
	}

	@Override
	public Object deserialize(byte[] bytes) throws IOException {
		try (InputStream inStream = new ByteArrayInputStream(bytes); Input in = new Input(inStream)) {
			return binaryStream.readClassAndObject(in);
		}
	}

}
