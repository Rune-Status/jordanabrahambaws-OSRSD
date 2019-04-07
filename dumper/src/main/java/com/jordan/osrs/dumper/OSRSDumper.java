package com.jordan.osrs.dumper;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.friz.cache.Cache;
import com.friz.cache.FileStore;
import com.jordan.osrs.Settings;

public final class OSRSDumper {

	public static void main(String[] args) {
		try {
			Cache cache = new Cache(FileStore.open(Settings.CACHE_PATH));

			DumperRepository.getDumpers().forEach((k, v) -> {

				if (v.checkPath()) {
					try {
						System.out.println("Running " + v.getClass().getSimpleName() + ".");
						v.run(cache);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			System.err.println("Finished!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
