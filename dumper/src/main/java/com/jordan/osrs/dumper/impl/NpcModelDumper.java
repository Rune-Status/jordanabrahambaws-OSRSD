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
import com.friz.cache.type.npcs.NpcType;
import com.jordan.osrs.Settings;
import com.jordan.osrs.dumper.Dumper;

public class NpcModelDumper extends Dumper {

	private final ConcurrentHashMap<Integer, int[]> models;

	public NpcModelDumper() {
		super(Settings.NPC_MODELS_PATH, Settings.MODELS_INDEX);
		models = new ConcurrentHashMap<>();
	}

	@Override
	public void run(Cache cache) throws IOException {
		Container container = Container.decode(cache.getStore().read(255, Settings.CONFIG_INDEX));
		ReferenceTable tablel = ReferenceTable.decode(container.getData());

		Entry entry = tablel.getEntry(Settings.NPC_ARCHIVE);
		Archive archive = Archive.decode(cache.read(Settings.CONFIG_INDEX, Settings.NPC_ARCHIVE).getData(), entry.size());

		NpcType[] npcs = new NpcType[entry.capacity()];
		for (int id = 0; id < entry.capacity(); id++) {
			ChildEntry child = entry.getEntry(id);
			if (child == null)
				continue;

			ByteBuffer buffer = archive.getEntry(child.index());
			NpcType type = new NpcType(id);
			type.decode(buffer);
			npcs[id] = type;
		}
		
		for (int id = 0; id <= npcs.length - 1; id++) {
			if (id > npcs.length || npcs[id].getModels() == null) {
				continue;
			}
			models.put(id, npcs[id].getModels());
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
