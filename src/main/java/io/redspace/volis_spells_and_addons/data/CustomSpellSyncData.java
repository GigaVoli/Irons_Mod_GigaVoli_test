package io.redspace.volis_spells_and_addons.data;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.LearnedSpellData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.gui.overlays.SpellSelection;
import io.redspace.ironsspellbooks.network.ClientboundSyncEntityData;
import io.redspace.ironsspellbooks.network.ClientboundSyncPlayerData;
import io.redspace.ironsspellbooks.player.SpinAttackType;
import io.redspace.ironsspellbooks.setup.Messages;
import io.redspace.volis_spells_and_addons.packets.CustomPacketManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class CustomSpellSyncData extends SyncedSpellData {
    public static final long BLOOD_BARRIER = 256L;
    private int hitsRemaining;
    private @Nullable LivingEntity livingEntity;
    private boolean isCasting;
    private String castingSpellId;
    private int castingSpellLevel;
    private long syncedEffectFlags;
    private long localEffectFlags;
    private float heartStopAccumulatedDamage;
    private int evasionHitsRemaining;
    private SpinAttackType spinAttackType;
    private LearnedSpellData learnedSpellData;
    private SpellSelection spellSelection;
    private String castingEquipmentSlot;
    public static final EntityDataSerializer<CustomSpellSyncData> SYNCED_SPELL_DATA = new EntityDataSerializer.ForValueType<CustomSpellSyncData>() {
        public void write(FriendlyByteBuf buffer, CustomSpellSyncData data) {
            buffer.writeInt(data.hitsRemaining);
            data.learnedSpellData.writeToBuffer(buffer);
            data.spellSelection.writeToBuffer(buffer);
        }

        public void write(FriendlyByteBuf pBuffer, SyncedSpellData pValue) {

        }

        public CustomSpellSyncData read(FriendlyByteBuf buffer) {
            CustomSpellSyncData data = new CustomSpellSyncData(buffer.readInt());
            data.isCasting = buffer.readBoolean();
            data.hitsRemaining = buffer.readInt();
            data.learnedSpellData.readFromBuffer(buffer);
            data.spellSelection.readFromBuffer(buffer);
            return data;
        }
    };

    public CustomSpellSyncData(int serverPlayerId) {
        super(serverPlayerId);
        this.hitsRemaining = 0;
        this.learnedSpellData = new LearnedSpellData();
        this.spellSelection = new SpellSelection();
    }

    public CustomSpellSyncData(LivingEntity livingEntity) {
        this(livingEntity == null ? -1 : livingEntity.getId());
        this.livingEntity = livingEntity;
    }

    public void saveNBTData(CompoundTag compound) {
        compound.putInt("hitsRemaining", this.hitsRemaining);
        this.learnedSpellData.saveToNBT(compound);
        compound.put("spellSelection", this.spellSelection.serializeNBT());
    }

    public void loadNBTData(CompoundTag compound) {
        this.hitsRemaining = compound.getInt("hitsRemaining");
    }

    public boolean hasEffect(long effectFlags) {
        return (this.syncedEffectFlags & effectFlags) == effectFlags;
    }

    public String getCastingEquipmentSlot() {
        return this.castingEquipmentSlot;
    }

    public boolean hasLocalEffect(long effectFlags) {
        return (this.localEffectFlags & effectFlags) == effectFlags;
    }

    public void addLocalEffect(long effectFlags) {
        this.localEffectFlags |= effectFlags;
    }

    public void removeLocalEffect(long effectFlags) {
        this.localEffectFlags &= ~effectFlags;
    }

    public SpellSelection getSpellSelection() {
        return this.spellSelection;
    }

    public void setSpellSelection(SpellSelection spellSelection) {
        this.spellSelection = spellSelection;
        this.doSync();
    }

    public void learnSpell(AbstractSpell spell) {
        this.learnedSpellData.learnedSpells.add(spell.getSpellResource());
        this.doSync();
    }

    public void forgetAllSpells() {
        this.learnedSpellData.learnedSpells.clear();
        this.doSync();
    }

    public boolean isSpellLearned(AbstractSpell spell) {
        return !spell.needsLearning() || this.learnedSpellData.learnedSpells.contains(spell.getSpellResource());
    }

    public int getHitsRemaining() {
        return this.hitsRemaining;
    }

    public void subtractHit() {
        --this.hitsRemaining;
        this.doSync();
    }

    public void setHitsRemaining(int hitsRemaining) {
        this.hitsRemaining = hitsRemaining;
        this.doSync();
    }

    public void addEffects(long effectFlags) {
        this.syncedEffectFlags |= effectFlags;
        this.doSync();
    }

    public void removeEffects(long effectFlags) {
        this.syncedEffectFlags &= ~effectFlags;
        this.doSync();
    }

    public void doSync() {
        LivingEntity var3 = this.livingEntity;
        if (var3 instanceof ServerPlayer serverPlayer) {
            Messages.sendToPlayer(new CustomPacketManager(this), serverPlayer);
            Messages.sendToPlayersTrackingEntity(new CustomPacketManager(this), serverPlayer);
        } else {
            var3 = this.livingEntity;
            if (var3 instanceof IMagicEntity abstractSpellCastingMob) {
                Messages.sendToPlayersTrackingEntity(new ClientboundSyncEntityData(this, abstractSpellCastingMob), this.livingEntity);
            }
        }

    }

    public void syncToPlayer(ServerPlayer serverPlayer) {
        Messages.sendToPlayer(new CustomPacketManager(this), serverPlayer);
    }

    public boolean isCasting() {
        return this.isCasting;
    }

    public String getCastingSpellId() {
        return this.castingSpellId;
    }

    public int getCastingSpellLevel() {
        return this.castingSpellLevel;
    }

    protected SyncedSpellData clone() {
        return new SyncedSpellData(this.livingEntity);
    }

    public String toString() {
        return String.format("isCasting:%s, spellID:%s, spellLevel:%d, effectFlags:%d", this.isCasting, this.castingSpellId, this.castingSpellLevel, this.syncedEffectFlags);
    }

    public CustomSpellSyncData getPersistentData() {
        CustomSpellSyncData persistentData = new CustomSpellSyncData(this.livingEntity);
        persistentData.learnedSpellData.learnedSpells.addAll(this.learnedSpellData.learnedSpells);
        return persistentData;
    }
}