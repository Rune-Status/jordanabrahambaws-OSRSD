/*
 * MultiServer - Multiple Server Communication Application
 * Copyright (C) 2015 Kyle Fricilone
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.friz.cache.utility.crypto;

import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * An implementation of the RSA algorithm.
 * @author Graham
 * @author `Discardedx2
 */
public final class Rsa {

	/**
	 * Encrypts/decrypts the specified buffer with the key and modulus.
	 * @param buffer The input buffer.
	 * @param modulus The modulus.
	 * @param key The key.
	 * @return The output buffer.
	 */
	public static ByteBuffer crypt(ByteBuffer buffer, BigInteger modulus, BigInteger key) {
		byte[] bytes = new byte[buffer.limit()];
		buffer.get(bytes);

		BigInteger in = new BigInteger(bytes);
		BigInteger out = in.modPow(key, modulus);

		return ByteBuffer.wrap(out.toByteArray());
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private Rsa() {
		
	}

}
