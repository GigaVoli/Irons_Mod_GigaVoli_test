package io.redspace.irons_example_mod.spells;

import io.redspace.irons_example_mod.IronsExampleMod;
import io.redspace.irons_example_mod.registry.EffectRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.network.spell.ClientboundAborptionParticles;
import io.redspace.ironsspellbooks.network.spell.ClientboundFortifyAreaParticles;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.setup.Messages;
import io.redspace.ironsspellbooks.spells.TargetAreaCastData;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.BeaconBlock;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.ChatFormatting;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class NaturesBlessingSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(IronsExampleMod.MODID, "natures_blessing");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_example_mod.healing", Utils.stringTruncation(getSpellPower(spellLevel, caster), 1))
        );
    }

    public NaturesBlessingSpell() {
        this.manaCostPerLevel = 20;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 100;
        this.baseManaCost = 10;
    }

    public enum MobEffectCategory {
        BENEFICIAL(ChatFormatting.BLUE),
        HARMFUL(ChatFormatting.RED),
        NEUTRAL(ChatFormatting.BLUE);

        private final ChatFormatting tooltipFormatting;

        private MobEffectCategory(ChatFormatting pTooltipFormatting) {
            this.tooltipFormatting = pTooltipFormatting;
        }

        public ChatFormatting getTooltipFormatting() {
            return this.tooltipFormatting;
        }
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SchoolRegistry.NATURE_RESOURCE)
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
        return CastType.LONG;
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
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        System.out.println("Hi");
        level.getEntitiesOfClass(LivingEntity.class, new AABB(entity.position().subtract(16.0, 16.0, 16.0), entity.position().add(16.0, 16.0, 16.0))).forEach((target) -> {
            if (entity.distanceTo(target) <= 16.0F) {
                System.out.println("Hi");
                target.addEffect(new MobEffectInstance((MobEffect) EffectRegistry.FORESTS_BLESSING.get(), 2400, (int) this.getSpellPower(spellLevel, entity) - 1, false, false, true));
                System.out.println("Hi");
            }

        });
        Messages.sendToPlayersTrackingEntity(new ClientboundFortifyAreaParticles(entity.position()), entity, true);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void onServerPreCast(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        super.onServerPreCast(level, spellLevel, entity, playerMagicData);
        if (playerMagicData != null) {
            TargetedAreaEntity targetedAreaEntity = TargetedAreaEntity.createTargetAreaEntity(level, entity.position(), 16.0F, 16239960, entity);
            playerMagicData.setAdditionalCastData(new TargetAreaCastData(entity.position(), targetedAreaEntity));
        }
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.SELF_CAST_ANIMATION;
    }
}