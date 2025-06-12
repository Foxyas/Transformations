package net.foxyas.transformations.mixin.client.cmrs;

import net.foxyas.transformations.client.cmrs.CMRS;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer <T extends Entity, S extends EntityRenderState> {

    @Inject(at = @At("RETURN"), method = "createRenderState(Lnet/minecraft/world/entity/Entity;F)Lnet/minecraft/client/renderer/entity/state/EntityRenderState;")
    private void onCreateState(T entity, float partialTick, CallbackInfoReturnable<S> cir){
        if(!(cir.getReturnValue() instanceof LivingEntityRenderState state) || !(entity instanceof LivingEntity living)) return;

        state.setRenderData(CMRS.LIVING, living);
    }
}
