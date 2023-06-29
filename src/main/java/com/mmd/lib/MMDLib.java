package com.mmd.lib;

import com.google.common.base.Joiner;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MMDLib.MODID)
public class MMDLib
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "mmdlib";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public List<ResourceLocation> buildFileList(IModFileInfo info) {
        Path root = info.getFile().getSecureJar().getPath("assets", "mmdlib-data");
        try {
            return Files.walk(root).map(root::relativize)//path -> root.relativize(path))
                    .filter(path -> path.getNameCount() <= 64) // logical bounds checking
                    .filter(path -> path.toString().toLowerCase(Locale.ROOT).endsWith(".json"))
                    .map(path -> Joiner.on('/').join(path))
                    .map(path -> path.substring(0, path.length() - 5)) // strip the extension
                    .map(path -> new ResourceLocation(info.getMods().get(0).getModId(), path))
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            if (!(ex instanceof NoSuchFileException)) {
                LOGGER.error("Exception trying to iterate files", ex);
            }
            return Collections.emptyList();
        }
    }

    public String loadFile(ResourceLocation fileLoc) {
        ModFile mf = (ModFile)ModList.get().getModFileById(fileLoc.getNamespace()).getFile();
        String base = String.format("assets/mmdlib-data/%s.json", fileLoc.getPath());
        Path p = mf.getSecureJar().getPath(base);
        StringBuilder buffer = new StringBuilder();
        try(InputStream is = p.toUri().toURL().openStream();
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr)) {
            br.lines().forEach(l -> buffer.append(String.format("\n%s",l)));
        } catch (IOException e) {
            LOGGER.error("Exception reading data: ", e);
            return "";
        }
        return buffer.toString().strip();
    }
    public MMDLib()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        List<ResourceLocation> foundFiles = new LinkedList<>();
        ModList.get().getModFiles().stream().map(this::buildFileList).forEach(foundFiles::addAll);

        foundFiles.parallelStream().map(this::loadFile).forEach(data -> LOGGER.info("loaded: {}", data));
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
        }
    }
}
