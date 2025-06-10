package net.foxyas.transformationsAPI.mixins.entities.players;

import net.foxyas.transformationsAPI.entity.playerExtension.IPlayerDataExtension;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Unique
    private ServerPlayer getSelf(){
        return (ServerPlayer) (Object) this;
    }
}
