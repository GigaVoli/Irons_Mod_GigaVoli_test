package io.redspace.volis_spells_and_addons.packets;


import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.network.ClientboundSyncPlayerData;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import java.util.function.Supplier;

import io.redspace.volis_spells_and_addons.data.CustomSpellSyncData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class CustomPacketManager extends ClientboundSyncPlayerData {
    CustomSpellSyncData syncedSpellData;

    public CustomPacketManager(FriendlyByteBuf buf) {
        super(buf);
    }
    public CustomPacketManager(CustomSpellSyncData playerSyncedData) {
        super(playerSyncedData);
    }

    public void toBytes(FriendlyByteBuf buf) {
        CustomSpellSyncData.SYNCED_SPELL_DATA.write(buf, this.syncedSpellData);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context)supplier.get();
        ctx.enqueueWork(() -> {
            ClientMagicData.handlePlayerSyncedData(this.syncedSpellData);
        });
        return true;
    }
}