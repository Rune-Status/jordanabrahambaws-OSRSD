package com.jordan.osrs.dumper.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

import com.friz.cache.Archive;
import com.friz.cache.Cache;
import com.friz.cache.Container;
import com.friz.cache.ReferenceTable;
import com.friz.cache.ReferenceTable.ChildEntry;
import com.friz.cache.ReferenceTable.Entry;
import com.friz.cache.type.TypePrinter;
import com.friz.cache.type.objects.ObjectType;
import com.jordan.osrs.Settings;
import com.jordan.osrs.dumper.Dumper;

public class ObjectListDumper extends Dumper {

	public ObjectListDumper() {
		super(Settings.LISTS_PATH, Settings.CONFIG_INDEX);
	}

	@Override
	public void run(Cache cache) throws IOException {
		Container container = Container.decode(cache.getStore().read(255, index));
		ReferenceTable table = ReferenceTable.decode(container.getData());

		Entry entry = table.getEntry(Settings.OBJECT_ARCHIVE);
		Archive archive = Archive.decode(cache.read(index, Settings.OBJECT_ARCHIVE).getData(), entry.size());

		ObjectType[] npcs = new ObjectType[entry.capacity()];
		for (int id = 0; id < entry.capacity(); id++) {
			ChildEntry child = entry.getEntry(id);
			if (child == null)
				continue;

			ByteBuffer buffer = archive.getEntry(child.index());
			ObjectType type = new ObjectType(id);
			type.decode(buffer);
			npcs[id] = type;
		}
		
		File file = new File(dumpPath, "objects.txt");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			Arrays.stream(npcs).filter(Objects::nonNull).forEach((ObjectType t) -> {
				TypePrinter.print(t, writer);
			});
			writer.flush();
		}
	}

}
