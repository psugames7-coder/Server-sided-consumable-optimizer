package com.variantseat;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public final class EatTiming {

    private EatTiming() {}

    private static final int FAST_PING = 80;
    private static final int MAX_CUT = 10;
    private static final int MAX_GRACE = 12;

    private static int ping(ServerPlayerEntity p) {
        return p.networkHandler == null ? 0 : Math.max(0, p.networkHandler.getLatency());
    }

    private static boolean consumable(ItemStack stack) {
        return stack != null && !stack.isEmpty() && stack.contains(DataComponentTypes.CONSUMABLE);
    }

    public static boolean canFinish(ServerPlayerEntity p, ItemStack stack, int timeLeft) {
        if (!consumable(stack) || timeLeft <= 0) return false;
        int grace = Math.min(MAX_GRACE, (int) Math.ceil(ping(p) / 50.0) + 2);
        return timeLeft <= grace;
    }

    public static int startTime(ServerPlayerEntity p, ItemStack stack, int timeLeft) {
        if (!consumable(stack)) return timeLeft;
        int ping = ping(p);
        if (ping < FAST_PING) return timeLeft;
        int cut = Math.min(MAX_CUT, (int) Math.round(ping / 50.0));
        return Math.max(1, timeLeft - cut);
    }
}
