package com.jordan.osrs.dumper.impl;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

import com.friz.cache.Archive;
import com.friz.cache.Cache;
import com.friz.cache.Container;
import com.friz.cache.FileStore;
import com.friz.cache.ReferenceTable;
import com.friz.cache.ReferenceTable.ChildEntry;
import com.friz.cache.ReferenceTable.Entry;
import com.friz.cache.type.TypePrinter;
import com.friz.cache.type.sequence.SequenceType;
import com.jordan.osrs.Settings;
import com.jordan.osrs.dumper.Dumper;

public class SequenceDumper extends Dumper {

	public SequenceDumper() {
		super(Settings.LISTS_PATH, Settings.CONFIG_INDEX);
	}

	@Override
	public void run(Cache cache) throws IOException {
		Container container = Container.decode(cache.getStore().read(255, index));
		ReferenceTable table = ReferenceTable.decode(container.getData());

		Entry entry = table.getEntry(Settings.SEQUENCE_ARCHIVE);
		Archive archive = Archive.decode(cache.read(index, Settings.SEQUENCE_ARCHIVE).getData(), entry.size());

		SequenceType[] sequences = new SequenceType[entry.capacity()];
		// for (int id = 0; id < entry.capacity(); id++) {
		int id = 2981;
		ChildEntry child = entry.getEntry(id);
//		if (child == null)
//			continue;

		ByteBuffer buffer = archive.getEntry(child.index());
//		
//		Cache cache2 = new Cache(FileStore.open(System.getProperty("user.home") + "/Documents/592/"));
//		container = Container.decode(cache2.getStore().read(255, 20));
//		table = ReferenceTable.decode(container.getData());
		
//		cache2.write(20, 12928, 0, buffer);
//		cache2.close();
		
		

//		File file = new File("./repository/", "2981.dat");
//		DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
//		dos.write(buffer.array());
//		dos.close();
		 SequenceType type = new SequenceType(id);
		 type.decode(buffer);
		 if (id == 2981) {
			 System.out.println(Arrays.toString(type.getFrameIDs()));
			 System.out.println(Arrays.toString(type.getFrameLengths()));
			 System.out.println(Arrays.toString(type.getAnIntArray2126()));
		 }
		 sequences[id] = type;
	}
	//
	// File file = new File(dumpPath, "sequence.txt");
	// try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	// Arrays.stream(sequences).filter(Objects::nonNull).forEach((SequenceType t) -> {
	// TypePrinter.print(t, writer);
	// });
	// writer.flush();
	// }
}
