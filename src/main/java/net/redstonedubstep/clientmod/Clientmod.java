package net.redstonedubstep.clientmod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;

@Mod(Clientmod.MODID)
@EventBusSubscriber(modid=Clientmod.MODID, bus= Bus.MOD)
public class Clientmod {

	public static final String MODID = "clientmod";

	public Clientmod() {
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, ()->Pair.of(()->"Placeholder string", (remoteversionstring,networkbool)->networkbool));
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
	}
}
