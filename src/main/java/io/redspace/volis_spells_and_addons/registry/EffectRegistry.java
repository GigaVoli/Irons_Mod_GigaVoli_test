package io.redspace.volis_spells_and_addons.registry;

import io.redspace.ironsspellbooks.effect.EvasionEffect;
import io.redspace.volis_spells_and_addons.VolisSpellsAndAddons;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.volis_spells_and_addons.effects.BloodBarrierEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import io.redspace.volis_spells_and_addons.effects.NaturesBlessingEffect;

public class EffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECT_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, VolisSpellsAndAddons.MODID);
    public static final RegistryObject<MobEffect> FORESTS_BLESSING;
    public static final RegistryObject<MobEffect> BLOOD_BARRIER;
    static{
        FORESTS_BLESSING = MOB_EFFECT_DEFERRED_REGISTER.register("natures_blessing", () -> {
            return NaturesBlessingEffect.FORESTS_BLESSING;
        });
        BLOOD_BARRIER = MOB_EFFECT_DEFERRED_REGISTER.register("blood_barrier", () -> {
            return new BloodBarrierEffect(MobEffectCategory.BENEFICIAL, 10423267);
        });
    }
    public static void register(IEventBus modEventBus) {
        MOB_EFFECT_DEFERRED_REGISTER.register(modEventBus);

    }
}