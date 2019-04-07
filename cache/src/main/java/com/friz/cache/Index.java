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
package com.friz.cache;

import com.friz.cache.utility.ByteBufferUtils;

import java.nio.ByteBuffer;


/**
 * An {@link Index} points to a file inside a {@link FileStore}.
 * @author Graham
 * @author `Discardedx2
 */
public final class Index {

	/**
	 * The size of an index, in bytes.
	 */
	public static final int SIZE = 6;

	/**
	 * Decodes the specified {@link ByteBuffer} into an {@link Index} object.
	 * @param buf The buffer.
	 * @return The index.
	 */
	public static Index decode(ByteBuffer buf) {
		if (buf.remaining() != SIZE)
			throw new IllegalArgumentException();

		int size = ByteBufferUtils.getTriByte(buf);
		int sector = ByteBufferUtils.getTriByte(buf);
		return new Index(size, sector);
	}

	/**
	 * The size of the file in bytes.
	 */
	private final int size;

	/**
	 * The number of the first sector that contains the file.
	 */
	private final int sector;

	/**
	 * Creates a new index.
	 * @param size The size of the file in bytes.
	 * @param sector The number of the first sector that contains the file.
	 */
	public Index(int size, int sector) {
		this.size = size;
		this.sector = sector;
	}

	/**
	 * Encodes this index into a byte buffer.
	 * @return The buffer.
	 */
	public ByteBuffer encode() {
		ByteBuffer buf = ByteBuffer.allocate(Index.SIZE);
		ByteBufferUtils.putTriByte(buf, size);
		ByteBufferUtils.putTriByte(buf, sector);
		return (ByteBuffer) buf.flip();
	}

	/**
	 * Gets the number of the first sector that contains the file.
	 * @return The number of the first sector that contains the file.
	 */
	public int getSector() {
		return sector;
	}

	/**
	 * Gets the size of the file.
	 * @return The size of the file in bytes.
	 */
	public int getSize() {
		return size;
	}

}