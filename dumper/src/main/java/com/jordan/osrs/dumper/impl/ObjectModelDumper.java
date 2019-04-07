package com.jordan.osrs.dumper.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

import com.friz.cache.Archive;
import com.friz.cache.Cache;
import com.friz.cache.Container;
import com.friz.cache.ReferenceTable;
import com.friz.cache.ReferenceTable.ChildEntry;
import com.friz.cache.ReferenceTable.Entry;
import com.friz.cache.type.objects.ObjectType;
import com.jordan.osrs.Settings;
import com.jordan.osrs.dumper.Dumper;

public class ObjectModelDumper extends Dumper {

	private final ConcurrentHashMap<Integer, int[]> models;

	public ObjectModelDumper() {
		super(Settings.OBJECT_MODELS_PATH, Settings.MODELS_INDEX);
		models = new ConcurrentHashMap<>();
	}

	@Override
	public void run(Cache cache) throws IOException {
		Container container = Container.decode(cache.getStore().read(255, Settings.CONFIG_INDEX));
		ReferenceTable tablel = ReferenceTable.decode(container.getData());

		Entry entry = tablel.getEntry(Settings.OBJECT_ARCHIVE);
		Archive archive = Archive.decode(cache.read(Settings.CONFIG_INDEX, Settings.OBJECT_ARCHIVE).getData(), entry.size());

		ObjectType[] objects = new ObjectType[entry.capacity()];
		for (int id = 0; id < entry.capacity(); id++) {
			ChildEntry child = entry.getEntry(id);
			if (child == null)
				continue;

			ByteBuffer buffer = archive.getEntry(child.index());
			ObjectType type = new ObjectType(id);
			type.decode(buffer);
			objects[id] = type;
		}
		
		for (int id = 0; id <= objects.length - 1; id++) {
			if (id > objects.length || objects[id].getObjectModels() == null) {
				continue;
			}
			models.put(id, objects[id].getObjectModels());
		}
		
		ReferenceTable table = ReferenceTable.decode(Container.decode(cache.getStore().read(255, index)).getData());
		for (int i = 0; i <= table.capacity(); i++) {
			if (table.getEntry(i) == null)
				continue;
			
			models.forEach((k, v) -> {
				try {
					dumpModel(cache, k, v);
				} catch (IOException e) {
					e.printStackTrace();
				}
				models.remove(k);
			});
		}
	}

}
