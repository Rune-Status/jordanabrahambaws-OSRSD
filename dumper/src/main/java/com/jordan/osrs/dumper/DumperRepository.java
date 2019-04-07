package com.jordan.osrs.dumper;

import java.util.concurrent.ConcurrentHashMap;

import com.jordan.osrs.dumper.impl.InventoryModelDumper;
import com.jordan.osrs.dumper.impl.ItemListDumper;
import com.jordan.osrs.dumper.impl.ModelDumper;
import com.jordan.osrs.dumper.impl.NpcListDumper;
import com.jordan.osrs.dumper.impl.NpcModelDumper;
import com.jordan.osrs.dumper.impl.ObjectListDumper;
import com.jordan.osrs.dumper.impl.ObjectModelDumper;
import com.jordan.osrs.dumper.impl.SequenceDumper;
import com.jordan.osrs.dumper.impl.SoundEffectDumper;
import com.jordan.osrs.dumper.impl.SpriteDumper;
import com.jordan.osrs.dumper.impl.TextureDumper;

public class DumperRepository {
	
	private static final ConcurrentHashMap<Class<?>, Dumper> DUMPERS = new ConcurrentHashMap<>();
	
	public static ConcurrentHashMap<Class<?>, Dumper> getDumpers() {
		return DUMPERS;
	}
	
	public static void register(Class<?> clazz) {
        try {
        	DUMPERS.put(clazz, (Dumper) clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
	
	static {
		register(SequenceDumper.class);
		register(SoundEffectDumper.class);
		register(ObjectModelDumper.class);
		register(InventoryModelDumper.class);
		register(ObjectListDumper.class);
		register(ItemListDumper.class);
		register(NpcListDumper.class);
		register(TextureDumper.class);
		register(NpcModelDumper.class);
		register(ModelDumper.class);
		register(SpriteDumper.class);
	}

}
