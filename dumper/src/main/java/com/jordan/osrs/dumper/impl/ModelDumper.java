package com.jordan.osrs.dumper.impl;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.friz.cache.Cache;
import com.friz.cache.Container;
import com.friz.cache.ReferenceTable;
import com.jordan.osrs.Settings;
import com.jordan.osrs.dumper.Dumper;

public class ModelDumper extends Dumper {

	public ModelDumper() {
		super(Settings.MODELS_PATH, Settings.MODELS_INDEX);
	}

	@Override
	public void run(Cache cache) throws IOException {
		ReferenceTable table = ReferenceTable.decode(Container.decode(cache.getStore().read(255, index)).getData());
//		for (int i = 0; i < table.capacity(); i++) {
		int i = 28439;
//			if (table.getEntry(i) == null)
//				continue;

			Container container = cache.read(index, i);
			byte[] bytes = new byte[container.getData().limit()];
			container.getData().get(bytes);

			File file = new File(dumpPath, i + ".dat");

			DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
			dos.write(bytes);
			dos.close();
//		}
	}

}
