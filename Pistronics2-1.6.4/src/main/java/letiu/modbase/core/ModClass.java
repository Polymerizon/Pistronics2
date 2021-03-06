package letiu.modbase.core;

import letiu.modbase.blocks.BlockCollector;
import letiu.modbase.blocks.VanillaData;
import letiu.modbase.config.ConfigHandler;
import letiu.modbase.entities.EntityCollector;
import letiu.modbase.events.ArrowEventHandler;
import letiu.modbase.items.ItemCollector;
import letiu.modbase.network.PacketHandler;
import letiu.modbase.proxies.IProxy;
import letiu.modbase.render.TextureMapper;
import letiu.pistronics.config.ConfigData;
import letiu.pistronics.data.BlockData;
import letiu.pistronics.data.EntityData;
import letiu.pistronics.data.GuiHandler;
import letiu.pistronics.data.ItemData;
import letiu.pistronics.data.PacketData;
import letiu.pistronics.reference.ModInformation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import letiu.pistronics.recipes.Recipes;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = ModInformation.ID, name = ModInformation.NAME, version = ModInformation.VERSION)
@NetworkMod(channels = {ModInformation.CHANNEL}, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class ModClass {

	@Instance(ModInformation.ID)
	public static ModClass instance;
	
	@SidedProxy(clientSide = ModBaseInfo.CLIENT_PROXY, serverSide = ModBaseInfo.SERVER_PROXY)
	public static IProxy proxy;
	
	public static CreativeTabs modTap = new ModCreativeTab();
	
	public static ArrowEventHandler arrowEventHandler = new ArrowEventHandler();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		// init block and item data
		BlockData.init();
		ItemData.init();
		
		// config
		ConfigData.init();
		ConfigHandler.init(event.getSuggestedConfigurationFile());
		ConfigData.load();
		
		// modbase init
		VanillaData.init();
		TextureMapper.init();
		BlockCollector.init();
		ItemCollector.init();
		EntityData.init();
		
		// create blocks
		BlockCollector.createBlocks();
		ItemCollector.createItems();
		EntityCollector.registerEntities();
		
		// proxy
		proxy.init();
		proxy.registerRenderers();
		proxy.registerEvents();
		
		Recipes.registerRecipes();
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) {
		LanguageRegistry.instance().addStringLocalization("itemGroup.Pistronics2", "Pistronics 2");
		PacketData.init();
		proxy.registerTileEntities();
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		
		MinecraftForge.EVENT_BUS.register(arrowEventHandler);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}
}