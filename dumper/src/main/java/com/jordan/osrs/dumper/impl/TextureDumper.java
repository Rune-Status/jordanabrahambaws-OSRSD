package com.jordan.osrs.dumper.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import com.friz.cache.Archive;
import com.friz.cache.Cache;
import com.friz.cache.Container;
import com.friz.cache.ReferenceTable;
import com.friz.cache.ReferenceTable.ChildEntry;
import com.friz.cache.ReferenceTable.Entry;
import com.friz.cache.sprite.Sprite;
import com.friz.cache.sprite.Texture;
import com.jordan.osrs.Settings;
import com.jordan.osrs.dumper.Dumper;

public class TextureDumper extends Dumper {

	public TextureDumper() {
		super(Settings.TEXTURES_PATH, Settings.TEXTURES_INDEX);
	}

	@Override
	public void run(Cache cache) throws IOException {
		Container container = Container.decode(cache.getStore().read(255, index));
		ReferenceTable table = ReferenceTable.decode(container.getData());
		
		Entry entry = table.getEntry(0);
		Archive archive = Archive.decode(cache.read(index, 0).getData(), entry.size());

		int[] ids = new int[entry.capacity()];
		for (int id = 0; id < entry.capacity(); id++) {
			ChildEntry child = entry.getEntry(id);
			if (child == null)
				continue;

			ByteBuffer buffer = archive.getEntry(child.index());
			Texture texture = Texture.decode(buffer);
			ids[id] = texture.getIds(0);
		}
		
		container = Container.decode(cache.getStore().read(255, Settings.SPRITES_INDEX));
		table = ReferenceTable.decode(container.getData());
		for (int id = 0; id < entry.capacity(); id++) {
			int file = ids[id];

			Entry e = table.getEntry(file);
			if (e == null)
				continue;

			Container containers = cache.read(Settings.SPRITES_INDEX, file);
			Sprite sprite = Sprite.decode(containers.getData());
			
			for (int frame = 0; frame < sprite.size(); frame++) {
				File f = new File(dumpPath, id + ".png");
				BufferedImage image = sprite.getFrame(frame);

				ImageIO.write(image, "png", f);
			}
		}
	}

}
