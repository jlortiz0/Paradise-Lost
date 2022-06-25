package net.id.aether.world.gen.structure;

import net.minecraft.structure.StructureSet;
import net.minecraft.structure.StructureSets;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.gen.chunk.placement.SpreadType;

@SuppressWarnings("unused")
public interface ParadiseLostStructureSets {
    RegistryEntry<StructureSet> SLIDER_LABYRINTHS = StructureSets.register(
            ParadiseLostStructureSetKeys.SLIDER_LABYRINTHS,
            RegistryEntry.of(ParadiseLostStructures.SLIDER_LABYRINTH),
            new RandomSpreadStructurePlacement(32, 8, SpreadType.LINEAR, 12847026));

    static void init() {
    }
}
