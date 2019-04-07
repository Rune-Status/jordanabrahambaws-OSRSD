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

package com.friz.cache.tools;

import com.friz.cache.Container;
import com.friz.cache.FileStore;
import com.friz.cache.ReferenceTable;
import com.friz.cache.ReferenceTable.Entry;
import com.jordan.osrs.Settings;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;

public final class CacheAggregator {

	public static void main(String[] args) throws IOException {
		FileStore otherStore = FileStore.open(Settings.CACHE_PATH);
		FileStore store = FileStore.open(Settings.CACHE_PATH);

		for (int type = 0; type < store.getFileCount(255); type++) {
			ReferenceTable otherTable = ReferenceTable.decode(Container.decode(otherStore.read(255, type)).getData());
			ReferenceTable table = ReferenceTable.decode(Container.decode(store.read(255, type)).getData());
			for (int file = 0; file < table.capacity(); file++) {
				Entry entry = table.getEntry(file);
				if (entry == null)
					continue;

				if (isRepackingRequired(store, entry, type, file)) {
					Entry otherEntry = otherTable.getEntry(file);
					if (entry.getVersion() == otherEntry.getVersion() && entry.getCrc() == otherEntry.getCrc()) {
						store.write(type, file, otherStore.read(type, file));
					}
				}
			}
		}
	}

	private static boolean isRepackingRequired(FileStore store, Entry entry, int type, int file) {
		ByteBuffer buffer;
		try {
			buffer = store.read(type, file);
		} catch (IOException ex) {
			return true;
		}

		if (buffer.capacity() <= 2) {
			return true;
		}

		byte[] bytes = new byte[buffer.limit() - 2]; // last two bytes are the version and shouldn't be included
		buffer.position(0);
		buffer.get(bytes, 0, bytes.length);

		CRC32 crc = new CRC32();
		crc.update(bytes, 0, bytes.length);

		if ((int) crc.getValue() != entry.getCrc()) {
			return true;
		}

		buffer.position(buffer.limit() - 2);
		return (buffer.getShort() & 0xFFFF) != entry.getVersion();

	}

}
