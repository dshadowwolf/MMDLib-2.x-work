package com.mmd.lib.common.types;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class MMDMaterial {
    private final Map<ResourceLocation, Item> items = new ConcurrentHashMap<ResourceLocation, Item>();
    private final Map<ResourceLocation, Block> blocks = new ConcurrentHashMap<ResourceLocation, Block>();

    private final Set<ResourceLocation> allowedBlocks = new ConcurrentSkipListSet<ResourceLocation>();
    private final Set<ResourceLocation> allowedItems = new ConcurrentSkipListSet<ResourceLocation>();

    public MMDMaterial() {
    }

    public void load() {
        // TODO: Load <resourceLocation> as `material` from `assets/mmdlib-data`

    }
}
