package net.foxyas.transformationsAPI.process;

import net.foxyas.transformationsAPI.entity.playerExtension.IPlayerDataExtension;
import net.foxyas.transformationsAPI.init.ModBuildInTransformations;
import net.foxyas.transformationsAPI.transformations.Transformation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ProcessTransformation {

    public static void TransformPlayer(Player player, Transformation transformation) {
        if (player instanceof IPlayerDataExtension iPlayerDataExtension) {
            iPlayerDataExtension.setCurrentTransformation(transformation);
        }
    }


    @Mod.EventBusSubscriber
    public static class EventDebug {

        @SubscribeEvent
        public static void eat(final LivingEntityUseItemEvent.Finish event) {
            if (!(event.getEntity() instanceof Player player))
                return;

            ItemStack itemStack = event.getItem();
            if (itemStack.is(Items.COD)) {

                if (itemStack.getItem() == Items.ROTTEN_FLESH) {
                    System.out.println("Player eat: " + itemStack.getItem().getDescriptionId());
                    TransformPlayer(player, ModBuildInTransformations.FORM_DEV.get());
                }
            }
        }
    }
}
