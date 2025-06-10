package net.foxyas.transformationsAPI.entity.playerExtension;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.foxyas.transformationsAPI.entity.playerExtension.IPlayerDataExtension.loadTransformationData;

@Mod.EventBusSubscriber
public class EventHandle {
    @SubscribeEvent
    public static void eventInject(PlayerEvent.PlayerLoggedInEvent event) {
        loadTransformationData(event.getEntity(), event.getEntity().saveWithoutId(new CompoundTag()));
    }
}