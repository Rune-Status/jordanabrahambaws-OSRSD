package com.jordan.osrs.dumper.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import com.friz.cache.Cache;
import com.friz.cache.Container;
import com.friz.cache.ReferenceTable;
import com.jordan.osrs.Settings;
import com.jordan.osrs.dumper.Dumper;
import com.jordan.osrs.sound.SoundEffect;

public class SoundEffectDumper extends Dumper {

	public SoundEffectDumper() {
		super(Settings.SOUND_EFFECTS_PATH, Settings.SOUND_EFFECTS_INDEX);
	}

	@Override
	public void run(Cache cache) throws IOException {
		ReferenceTable table = ReferenceTable.decode(Container.decode(cache.getStore().read(255, index)).getData());
		for (int i = 0; i < table.capacity(); i++) {
			if (table.getEntry(i) == null)
				continue;

			Container container = cache.read(index, i);
			SoundEffect effect = new SoundEffect(container.getData());
			
			byte[] data = effect.mix();
			AudioFormat audioFormat = new AudioFormat(22050, 8, 1, true, false);
			AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(data), audioFormat, data.length);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, bos);
            data = bos.toByteArray();
            
            File file = new File(dumpPath, i + ".wav");

			DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
			dos.write(data);
			dos.close();
		}
	}

}
