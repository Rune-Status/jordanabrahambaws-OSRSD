package com.jordan.osrs.dumper;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.friz.cache.Cache;
import com.friz.cache.Container;

public abstract class Dumper {
	
	protected final String dumpPath;
	
	protected final int index;
	
	public Dumper(String dumpPath, int index) {
		this.dumpPath = dumpPath;
		this.index = index;
	}
	
	public boolean checkPath() {
		File path = new File(dumpPath);

		if (!path.exists()) {
			return path.mkdir();
		}
		return path.exists();
	}
	
	protected void dumpModel(Cache cache, int id, int... models) throws IOException {
		if (models == null || models.length < 0)
			return;

		for (int model : models) {
			Container container = cache.read(index, model);
			byte[] bytes = new byte[container.getData().limit()];
			container.getData().get(bytes);

			File file = new File(dumpPath, id + "_" + model + ".dat");

			DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
			dos.write(bytes);
			dos.close();
		}
	}
	
	public abstract void run(Cache cache) throws IOException;

	public String getDumpPath() {
		return dumpPath;
	}

	public int getCacheIndex() {
		return index;
	}

}
