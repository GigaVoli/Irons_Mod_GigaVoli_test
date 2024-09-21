package io.redspace.volis_spells_and_addons.registry;

import io.redspace.volis_spells_and_addons.VolisSpellsAndAddons;
import io.redspace.volis_spells_and_addons.spells.BloodBarrierSpell;
import io.redspace.volis_spells_and_addons.spells.SuperHealSpell;
import io.redspace.volis_spells_and_addons.spells.NaturesBlessingSpell;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ExampleSpellRegistry {
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, VolisSpellsAndAddons.MODID);

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    public static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    public static final RegistryObject<AbstractSpell> SUPER_HEAL_SPELL = registerSpell(new SuperHealSpell());
    public static final RegistryObject<AbstractSpell> NATURES_BLESSING_SPELL = registerSpell(new NaturesBlessingSpell());
    public static final RegistryObject<AbstractSpell> BLOOD_BARRIER_SPELL = registerSpell(new BloodBarrierSpell());

}