package com.variantseat.mixin;

import com.variantseat.EatTiming;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class UseItemMixin {

    @Unique private static boolean variantseat$finishing;
    @Unique private boolean variantseat$wasUsing;

    @Unique
    private void variantseat$finish() {
        variantseat$finishing = true;
        try {
            ((LivingEntityAccess) this).variantseat$consumeItem();
        } finally {
            variantseat$finishing = false;
        }
    }

    @Unique
    private boolean variantseat$canFinish(ServerPlayerEntity p) {
        return p.isUsingItem() && EatTiming.canFinish(p, p.getActiveItem(), ((LivingEntityAccess) this).variantseat$getItemUseTimeLeft());
    }

    @Inject(method = "setCurrentHand(Lnet/minecraft/util/Hand;)V", at = @At("HEAD"))
    private void variantseat$beforeUse(Hand hand, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        variantseat$wasUsing = self.isUsingItem();
        if (variantseat$finishing || !(self instanceof ServerPlayerEntity p)) return;
        try {
            if (p.getActiveHand() == hand && variantseat$canFinish(p)) {
                variantseat$finish();
                variantseat$wasUsing = p.isUsingItem();
            }
        } catch (Throwable ignored) {
        }
    }

    @Inject(method = "setCurrentHand(Lnet/minecraft/util/Hand;)V", at = @At("TAIL"))
    private void variantseat$afterUse(Hand hand, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (variantseat$wasUsing || !(self instanceof ServerPlayerEntity p) || !p.isUsingItem()) return;
        try {
            LivingEntityAccess access = (LivingEntityAccess) this;
            access.variantseat$setItemUseTimeLeft(EatTiming.startTime(p, p.getActiveItem(), access.variantseat$getItemUseTimeLeft()));
        } catch (Throwable ignored) {
        }
    }

    @Inject(method = "clearActiveItem()V", at = @At("HEAD"))
    private void variantseat$clear(CallbackInfo ci) {
        if (variantseat$finishing || !((Object) this instanceof ServerPlayerEntity p)) return;
        try {
            if (variantseat$canFinish(p)) variantseat$finish();
        } catch (Throwable ignored) {
        }
    }
}
