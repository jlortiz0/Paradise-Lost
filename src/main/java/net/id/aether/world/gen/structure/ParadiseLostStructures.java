package net.id.aether.world.gen.structure;

import net.id.aether.world.gen.structure.generator.OrangeRuinGenerator;
import net.id.aether.world.gen.structure.generator.ParadiseLostStructurePools;
import net.id.aether.world.gen.structure.generator.SkyrootTowerGenerator;
import net.id.aether.world.gen.structure.generator.SliderLabyrinthGenerator;
import net.id.aether.world.gen.structure.generator.WellGenerator;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.tag.BiomeTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.heightprovider.ConstantHeightProvider;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Map;

import static net.id.aether.Aether.locate;

public class ParadiseLostStructures {
    public static final TagKey<Structure> WELL_KEY = tagKey("well");
    public static final StructureType<WellStructure> WELL = ()-> WellStructure.CODEC;
    public static final StructurePieceType WELL_PIECE = WellGenerator.Piece::new;
    
    public static final TagKey<Structure> SKYROOT_TOWER_KEY = tagKey("skyroot_tower");
    public static final StructureType<SkyrootTowerStructure> SKYROOT_TOWER = ()-> SkyrootTowerStructure.CODEC;
    public static final StructurePieceType SKYROOT_TOWER_PIECE = SkyrootTowerGenerator.Piece::new;
    
    public static final TagKey<Structure> ORANGE_RUIN_KEY = tagKey("orange_ruin");
    public static final StructureType<OrangeRuinStructure> ORANGE_RUIN = ()-> OrangeRuinStructure.CODEC;
    public static final StructurePieceType ORANGE_RUIN_PIECE = OrangeRuinGenerator.Piece::new;

    public static final JigsawStructure SLIDER_LABYRINTH = new JigsawStructure(
            new Structure.Config(
                    BuiltinRegistries.BIOME.getOrCreateEntryList(BiomeTags.IS_OVERWORLD),
                    Map.of(),
                    GenerationStep.Feature.SURFACE_STRUCTURES,
                    StructureTerrainAdaptation.NONE),
            SliderLabyrinthGenerator.STRUCTURE_POOLS,
            Integer.MAX_VALUE,
            ConstantHeightProvider.create(YOffset.fixed(0)),
            false,
            Heightmap.Type.WORLD_SURFACE_WG);
    
    private static TagKey<Structure> tagKey(String name) {
        return TagKey.of(Registry.STRUCTURE_KEY, locate(name));
    }
    
    public static void init() {
        register(WELL_KEY, WELL, WELL_PIECE);
        register(SKYROOT_TOWER_KEY, SKYROOT_TOWER, SKYROOT_TOWER_PIECE);
        register(ORANGE_RUIN_KEY, ORANGE_RUIN, ORANGE_RUIN_PIECE);
        BuiltinRegistries.add(BuiltinRegistries.STRUCTURE, ParadiseLostStructureKeys.SLIDER_LABYRINTH, SLIDER_LABYRINTH);
        ParadiseLostStructurePools.init();
        ParadiseLostStructureSets.init();
    }
    
    private static <T extends Structure> void register(TagKey<? extends T> name, StructureType<? extends T> type, StructurePieceType pieceType) {
        var id = name.id();
        Registry.register(Registry.STRUCTURE_TYPE, id, type);
        Registry.register(Registry.STRUCTURE_PIECE, id, pieceType);
    }
}
