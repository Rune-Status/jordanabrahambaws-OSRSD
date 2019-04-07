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
import com.jordan.osrs.Settings;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public final class CacheDefragmenter {

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		try (FileStore in = FileStore.open(Settings.CACHE_PATH)) {
			try (FileStore out = FileStore.create(Settings.CACHE_PATH + "-tmp/", in.getTypeCount())) {
				for (int type = 0; type < in.getTypeCount(); type++) {

					ByteBuffer buf = in.read(255, type);
					buf.mark();
					out.write(255, type, buf);
					buf.reset();

					ReferenceTable rt = ReferenceTable.decode(Container.decode(buf).getData());
					for (int file = 0; file < rt.capacity(); file++) {
						if (rt.getEntry(file) == null) {
							System.out.println(type + ", " + file + ", " + in.read(type, file).remaining());
							continue;
						}

						out.write(type, file, in.read(type, file));
					}
				}
				out.close();
			}
			in.close();
		}
		System.out.println("Took: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start));
	}

}
