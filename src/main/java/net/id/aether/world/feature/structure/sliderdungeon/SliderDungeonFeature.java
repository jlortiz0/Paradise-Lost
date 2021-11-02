package net.id.aether.world.feature.structure.sliderdungeon;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.*;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import static net.id.aether.Aether.locate;

public final class SliderDungeonFeature extends StructureFeature<DefaultFeatureConfig>{
    static final Identifier BOSS = locate("slider_dungeon/boss");
    static final Identifier CORNER = locate("slider_dungeon/corner");
    static final Identifier END = locate("slider_dungeon/end");
    static final Identifier HALLWAY = locate("slider_dungeon/hallway");
    static final Identifier STAIR_BOTTOM = locate("slider_dungeon/stair_bottom");
    static final Identifier STAIR_SEGMENT = locate("slider_dungeon/stair_segment");
    static final Identifier STAIR_TOP = locate("slider_dungeon/stair_top");
    static final Identifier START = locate("slider_dungeon/start");
    
    static final StructureFeature<DefaultFeatureConfig> DUNGEON = new SliderDungeonFeature(DefaultFeatureConfig.CODEC);
    static final ConfiguredStructureFeature<DefaultFeatureConfig, ?> CONFIGURED_DUNGEON = DUNGEON.configure(DefaultFeatureConfig.INSTANCE);
    
    static final StructurePieceType PIECE_BOSS = SliderDungeonBossPiece::new;
    static final StructurePieceType PIECE_CORNER = SliderDungeonCornerPiece::new;
    static final StructurePieceType PIECE_END = SliderDungeonEndPiece::new;
    static final StructurePieceType PIECE_HALLWAY = SliderDungeonHallwayPiece::new;
    static final StructurePieceType PIECE_STAIR_BOTTOM = SliderDungeonStairBottomPiece::new;
    static final StructurePieceType PIECE_STAIR_SEGMENT = SliderDungeonStairSegmentPiece::new;
    static final StructurePieceType PIECE_STAIR_TOP = SliderDungeonStairTopPiece::new;
    static final StructurePieceType PIECE_START = SliderDungeonStartPiece::new;
    
    public SliderDungeonFeature(Codec<DefaultFeatureConfig> codec){
        super(codec);
    }
    
    public static void register(){
        Registry.register(Registry.STRUCTURE_PIECE, BOSS, PIECE_BOSS);
        Registry.register(Registry.STRUCTURE_PIECE, CORNER, PIECE_CORNER);
        Registry.register(Registry.STRUCTURE_PIECE, END, PIECE_END);
        Registry.register(Registry.STRUCTURE_PIECE, HALLWAY, PIECE_HALLWAY);
        Registry.register(Registry.STRUCTURE_PIECE, STAIR_BOTTOM, PIECE_STAIR_BOTTOM);
        Registry.register(Registry.STRUCTURE_PIECE, STAIR_SEGMENT, PIECE_STAIR_SEGMENT);
        Registry.register(Registry.STRUCTURE_PIECE, STAIR_TOP, PIECE_STAIR_TOP);
        Registry.register(Registry.STRUCTURE_PIECE, START, PIECE_START);
        
        FabricStructureBuilder.create(locate("slider_dungeon"), DUNGEON)
            .step(GenerationStep.Feature.SURFACE_STRUCTURES)
            .defaultConfig(32, 8, 12345)
            .adjustsSurface()
            .superflatFeature(CONFIGURED_DUNGEON)
            .register();
        
        var configured = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, locate("slider_dungeon"));
        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, configured.getValue(), CONFIGURED_DUNGEON);
    
        BiomeModifications.addStructure(BiomeSelectors.all(), configured);
    }
    
    @Override
    public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory(){
        return Start::new;
    }
    
    public static final class Start extends StructureStart<DefaultFeatureConfig>{
        public Start(StructureFeature<DefaultFeatureConfig> feature, ChunkPos pos, int references, long seed){
            super(feature, pos, references, seed);
        }
    
        @Override
        public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator, StructureManager manager, ChunkPos chunkPos, Biome biome, DefaultFeatureConfig config, HeightLimitView world){
            int x = chunkPos.x << 4;
            int z = chunkPos.z << 4;
            int y = chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG, world);
            
            y = 128;
            
            var blockPos = new BlockPos(x, y, z);
            new SliderDungeonGenerator(random).generate(manager, blockPos, children);
            setBoundingBoxFromChildren();
        }
    }
}
