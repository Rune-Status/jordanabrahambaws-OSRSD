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
import com.friz.cache.type.items.ItemType;
import com.jordan.osrs.Settings;
import com.jordan.osrs.dumper.Dumper;

public class ItemListDumper extends Dumper {

	public ItemListDumper() {
		super(Settings.LISTS_PATH, Settings.CONFIG_INDEX);
	}

	@Override
	public void run(Cache cache) throws IOException {
		Container container = Container.decode(cache.getStore().read(255, index));
		ReferenceTable table = ReferenceTable.decode(container.getData());

		Entry entry = table.getEntry(Settings.ITEM_ARCHIVE);
		Archive archive = Archive.decode(cache.read(index, Settings.ITEM_ARCHIVE).getData(), entry.size());

		ItemType[] items = new ItemType[entry.capacity()];
		for (int id = 0; id < entry.capacity(); id++) {
			ChildEntry child = entry.getEntry(id);
			if (child == null)
				continue;

			ByteBuffer buffer = archive.getEntry(child.index());
			ItemType type = new ItemType(id);
			type.decode(buffer);
			items[id] = type;
		}
		
		File file = new File(dumpPath, "items.txt");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			Arrays.stream(items).filter(Objects::nonNull).forEach((ItemType t) -> {
				TypePrinter.print(t, writer);
			});
			writer.flush();
		}
	}

}
