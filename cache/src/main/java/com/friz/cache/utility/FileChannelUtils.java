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
package com.friz.cache.utility;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Contains {@link FileChannel}-related utility methods.
 * @author Graham
 * @author `Discardedx2
 */
public final class FileChannelUtils {

	/**
	 * Reads as much as possible from the channel into the buffer.
	 * @param channel The channel.
	 * @param buffer The buffer.
	 * @param ptr The initial position in the channel.
	 * @throws IOException if an I/O error occurs.
	 */
	public static void readFully(FileChannel channel, ByteBuffer buffer, long ptr) throws IOException {
		while (buffer.remaining() > 0) {
			long read = channel.read(buffer, ptr);
			if (read < -1) {
				throw new EOFException();
			} else {
				ptr += read;
			}
		}
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private FileChannelUtils() {
		
	}

}
