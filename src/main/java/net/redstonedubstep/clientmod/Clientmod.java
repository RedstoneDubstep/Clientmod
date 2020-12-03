package net.redstonedubstep.clientmod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod(Clientmod.MODID)
@EventBusSubscriber(modid=Clientmod.MODID, bus= Bus.MOD)
public class Clientmod {

	public static final String MODID = "clientmod";

	public Clientmod() {
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
	}
}
