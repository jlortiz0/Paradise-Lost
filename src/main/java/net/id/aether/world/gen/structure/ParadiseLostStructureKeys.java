package net.id.aether.world.gen.structure;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.structure.Structure;

import static net.id.aether.Aether.locate;

public interface ParadiseLostStructureKeys {
    RegistryKey<Structure> SLIDER_LABYRINTH = of("slider_labyrinth");

    private static RegistryKey<Structure> of(String id) {
        return RegistryKey.of(Registry.STRUCTURE_KEY, locate(id));
    }
}
