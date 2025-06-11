package net.foxyas.transformationsAPI.mixins.entities.players;

import net.foxyas.transformationsAPI.entity.playerExtension.IPlayerDataExtension;
import net.foxyas.transformationsAPI.transformations.Transformation;
import net.foxyas.transformationsAPI.transformations.TransformationInstance;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Unique
    private Player getSelf() {
        return (Player) (Object) this;
    }

}