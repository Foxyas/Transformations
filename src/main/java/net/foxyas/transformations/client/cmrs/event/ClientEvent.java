package net.foxyas.transformations.client.cmrs.event;

import net.foxyas.transformations.client.cmrs.CustomModelManager;
import net.foxyas.transformations.client.cmrs.Keybindings;
import net.foxyas.transformations.client.cmrs.gui.screen.ModelManagerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientEvent {

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.Key event){
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if(player == null) return;

        if(Keybindings.MODEL_MANAGER.isDown() && minecraft.screen == null){
            minecraft.setScreen(new ModelManagerScreen());
        }
    }

    @SubscribeEvent
    public static void onClone(ClientPlayerNetworkEvent.Clone event){
        LocalPlayer old = event.getOldPlayer();
        if(old.getRemovalReason() == null || old.getRemovalReason() == Entity.RemovalReason.CHANGED_DIMENSION) return;
        CustomModelManager.getInstance().playerDied(old);
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event){
        LocalPlayer player = event.getPlayer();
        if(player != null) CustomModelManager.getInstance().playerDied(player);
    }
}
