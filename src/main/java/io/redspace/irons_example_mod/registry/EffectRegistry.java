package io.redspace.irons_example_mod.registry;

import io.redspace.irons_example_mod.IronsExampleMod;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.spells.holy.HasteSpell;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import static io.redspace.ironsspellbooks.api.registry.AttributeRegistry.NATURE_SPELL_POWER;

public class EffectRegistry {
    public static float getDamageBoost(int pAmpLevel) {
        return (0.5F + pAmpLevel / 2);
    }
    public static final DeferredRegister<MobEffect> MOB_EFFECT_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, IronsExampleMod.MODID);
    public static final RegistryObject<MobEffect> FORESTS_BLESSING;
    static{
        FORESTS_BLESSING = MOB_EFFECT_DEFERRED_REGISTER.register("hastened", () -> {
            return (new MagicMobEffect(MobEffectCategory.BENEFICIAL, 14270531)).addAttributeModifier(Attributes.MOVEMENT_SPEED)
        ;});
    }
    public static void register(IEventBus modEventBus) {
        MOB_EFFECT_DEFERRED_REGISTER.register(modEventBus);

    }
}