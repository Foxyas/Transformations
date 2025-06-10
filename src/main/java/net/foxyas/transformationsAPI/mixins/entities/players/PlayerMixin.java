package net.foxyas.transformationsAPI.mixins.entities.players;

import net.foxyas.transformationsAPI.entity.playerExtension.IPlayerDataExtension;
import net.foxyas.transformationsAPI.transformations.Transformation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin implements IPlayerDataExtension {

    @Unique
    public Transformation transformations$transformation = null;

    @Override
    public @Nullable Transformation getCurrentTransformation() {
        return transformations$transformation;
    }

    @Override
    public void setCurrentTransformation(@Nullable Transformation transformation) {
        this.transformations$transformation = transformation;
    }

    @Unique
    private Player getSelf(){
        return (Player) (Object) this;
    }


    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void injectSaveData(CompoundTag tag, CallbackInfo ci) {
        IPlayerDataExtension.saveTransformationData(getSelf(), tag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void loadSaveData(CompoundTag tag, CallbackInfo ci) {
        IPlayerDataExtension.loadTransformationData(getSelf(), tag);
    }
}
