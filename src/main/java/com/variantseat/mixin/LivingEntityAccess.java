package com.variantseat.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccess {

    @Accessor("itemUseTimeLeft")
    int variantseat$getItemUseTimeLeft();

    @Accessor("itemUseTimeLeft")
    void variantseat$setItemUseTimeLeft(int value);

    @Invoker("consumeItem")
    void variantseat$consumeItem();
}
