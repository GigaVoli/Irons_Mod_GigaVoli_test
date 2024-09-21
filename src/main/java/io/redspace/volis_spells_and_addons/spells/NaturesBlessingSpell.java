package io.redspace.volis_spells_and_addons.spells;

import io.redspace.volis_spells_and_addons.VolisSpellsAndAddons;
import io.redspace.volis_spells_and_addons.effects.NaturesBlessingEffect;
import io.redspace.volis_spells_and_addons.registry.EffectRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.network.spell.ClientboundFortifyAreaParticles;
import io.redspace.ironsspellbooks.setup.Messages;
import io.redspace.ironsspellbooks.spells.TargetAreaCastData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.Nullable;
import net.minecraft.ChatFormatting;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class NaturesBlessingSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(VolisSpellsAndAddons.MODID, "natures_blessing");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.effect_length", Utils.timeFromTicks(getSpellPower(spellLevel, caster) * 1200, 1)),
                Component.translatable("attribute.modifier.plus.1", Utils.stringTruncation(NaturesBlessingEffect.getPercentageSpeed(spellLevel, caster), 0), Component.translatable("attribute.name.generic.movement_speed")),
                Component.translatable("attribute.modifier.plus.1", Utils.stringTruncation(NaturesBlessingEffect.getPercentageKBR(spellLevel, caster), 0), Component.translatable("attribute.name.generic.knockback_resistance")),
                Component.translatable("attribute.modifier.plus.1", Utils.stringTruncation(NaturesBlessingEffect.getPercentageMaxHealth(spellLevel, caster), 0), Component.translatable("attribute.name.generic.max_health"))
        );
    }

    public NaturesBlessingSpell() {
        this.manaCostPerLevel = 30;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 40;
        this.baseManaCost = 30;
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
        level.getEntitiesOfClass(Player.class, new AABB(entity.position().subtract(16.0, 16.0, 16.0), entity.position().add(16.0, 16.0, 16.0))).forEach((target) -> {
            if (entity.distanceTo(target) <= 16.0F) {
                target.addEffect(new MobEffectInstance((MobEffect) EffectRegistry.FORESTS_BLESSING.get(), 2400, (int) this.getSpellPower(spellLevel, entity) - 1, false, false, true));
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
}