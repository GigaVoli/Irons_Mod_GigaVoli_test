package io.redspace.irons_example_mod.spells;

import io.redspace.irons_example_mod.IronsExampleMod;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class SuperHealSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(IronsExampleMod.MODID, "super_heal");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_example_mod.healing", Utils.stringTruncation(getSpellPower(spellLevel, caster), 1))
        );
    }

    public SuperHealSpell() {
        this.manaCostPerLevel = 20;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 100;
        this.baseManaCost = 10;
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(20)
            .build();

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.CONTINUOUS;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.empty();
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {

        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        if (playerMagicData != null && (playerMagicData.getCastDurationRemaining() + 1) % 20 == 0) {
            entity.heal(getSpellPower(spellLevel, entity)*entity.getMaxHealth()*0.05f);
            int count = 16;
            float radius = 1.25f;
            for (int i = 0; i < count; i++) {
                double x, z;
                double theta = Math.toRadians(360 / count) * i;
                x = Math.cos(theta) * radius;
                z = Math.sin(theta) * radius;
                MagicManager.spawnParticles(level, ParticleTypes.HEART, entity.position().x + x, entity.position().y, entity.position().z + z, 1, 0, 0, 0, 0.1, false);
            }
        }
    }


    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.SELF_CAST_ANIMATION;
    }
}
