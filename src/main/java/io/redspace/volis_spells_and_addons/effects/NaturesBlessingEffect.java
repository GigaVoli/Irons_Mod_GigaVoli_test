package io.redspace.volis_spells_and_addons.effects;

import io.redspace.volis_spells_and_addons.VolisSpellsAndAddons;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NaturesBlessingEffect extends MagicMobEffect {
    public static MagicMobEffect FORESTS_BLESSING;
    public static final float SPEED_PER_LEVEL = 1.1f;
    public static final float KNOCKBACK_RESISTANCE_PER_LEVEL = 10f;
    public static final float MAX_HEALTH_PER_LEVEL = 1.1f;
    static{
        FORESTS_BLESSING = new MagicMobEffect(MobEffectCategory.BENEFICIAL, 14270531);
        FORESTS_BLESSING.addAttributeModifier(Attributes.MOVEMENT_SPEED, "39db1030-8df6-4329-b18f-fafc9d7f5b82", (SPEED_PER_LEVEL), AttributeModifier.Operation.MULTIPLY_TOTAL);
        FORESTS_BLESSING.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, "3db1030-8df6-4329-b18f-fac9d7f5b82", (KNOCKBACK_RESISTANCE_PER_LEVEL), AttributeModifier.Operation.ADDITION);
        FORESTS_BLESSING.addAttributeModifier(Attributes.MAX_HEALTH, "3db1030-8df6-9-b18f-fac9d7f5b82", (MAX_HEALTH_PER_LEVEL), AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public NaturesBlessingEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public static float getPercentageSpeed(int spellLevel, LivingEntity caster)
    {
        return spellLevel * SPEED_PER_LEVEL * 10;
    }

    public static float getPercentageKBR(int spellLevel, LivingEntity caster)
    {
        return spellLevel * KNOCKBACK_RESISTANCE_PER_LEVEL * 2;
    }

    public static float getPercentageMaxHealth(int spellLevel, LivingEntity caster)
    {
        return spellLevel * MAX_HEALTH_PER_LEVEL * 10;
    }
}
