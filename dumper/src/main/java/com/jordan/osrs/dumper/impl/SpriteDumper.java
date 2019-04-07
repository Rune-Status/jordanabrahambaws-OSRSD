package com.jordan.osrs.dumper.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.friz.cache.Cache;
import com.friz.cache.Container;
import com.friz.cache.ReferenceTable;
import com.friz.cache.sprite.Sprite;
import com.jordan.osrs.Settings;
import com.jordan.osrs.dumper.Dumper;

public class SpriteDumper extends Dumper {

	public SpriteDumper() {
		super(Settings.SPRITES_PATH, Settings.SPRITES_INDEX);
	}

	@Override
	public void run(Cache cache) throws IOException {
		ReferenceTable table = ReferenceTable.decode(Container.decode(cache.getStore().read(255, index)).getData());
		for (int i = 0; i < table.capacity(); i++) {
			if (table.getEntry(i) == null) {
				continue;
			}
			
			Container container = cache.read(index, i);
			Sprite sprite = Sprite.decode(container.getData());
			
			for (int frame = 0; frame < sprite.size(); frame++) {
				File file = new File(dumpPath, i + "_" + frame + ".png");
				BufferedImage image = sprite.getFrame(frame);

				ImageIO.write(image, "png", file);
			}
		}
	}

}
