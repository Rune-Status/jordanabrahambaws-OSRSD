package com.jordan.osrs.util;

import java.nio.ByteBuffer;

public class ByteBufferUtils {
	
	public static int getUSmart(ByteBuffer buffer) {
		if ((buffer.get(buffer.position()) & 0xff) < 128) {
			return buffer.get() & 0xff;
		}
		int shortValue = buffer.getShort() & 0xFFFF;
		return shortValue - 32769;
	}
	
	public static int getShortSmart(ByteBuffer buffer) {
		if ((buffer.get(buffer.position()) & 0xff) < 128) {
			return (buffer.get() & 0xff) - 64;
		}
		int shortValue = buffer.getShort() & 0xFFFF;
		return shortValue - 49152;
	}

}
