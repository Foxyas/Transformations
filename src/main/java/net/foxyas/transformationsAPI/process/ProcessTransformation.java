package net.foxyas.transformationsAPI.process;

import net.foxyas.transformationsAPI.entity.playerExtension.IPlayerDataExtension;
import net.foxyas.transformationsAPI.init.ModBuildInTransformations;
import net.foxyas.transformationsAPI.init.TransformationsInit;
import net.foxyas.transformationsAPI.transformations.Transformation;
import net.foxyas.transformationsAPI.transformations.TransformationInstance;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

public class ProcessTransformation {

    public static void TransformPlayer(Player player, Transformation transformation) {
        if (player instanceof IPlayerDataExtension iPlayerDataExtension) {
            iPlayerDataExtension.setCurrentTransformation(transformation);
        }
    }

    @Nullable
    public static TransformationInstance getPlayerTransform(Player player) {
        if (player instanceof IPlayerDataExtension iPlayerDataExtension) {
            return iPlayerDataExtension.getCurrentTransformation();
        }
        return null;
    }


    @Mod.EventBusSubscriber
    public static class EventDebug {

        @SubscribeEvent
        public static void eat(final LivingEntityUseItemEvent.Finish event) {
            if (!(event.getEntity() instanceof Player player))
                return;

            ItemStack itemStack = event.getItem();
            if (itemStack.get(DataComponents.FOOD) != null) {
                if (!TransformationsInit.FIRE_TRANSFORMATION.isPresent()) {
                    System.out.println("Player eat: " + itemStack.getItem().getDescriptionId());
                }
                if (itemStack.is(Items.APPLE)){
                    TransformPlayer(player, TransformationsInit.ICE_TRANSFORMATION.get());
                    return;
                }
                TransformPlayer(player, TransformationsInit.FIRE_TRANSFORMATION.get());
            }
        }
    }
}
