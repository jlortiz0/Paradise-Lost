package net.id.aether.world.feature.configured_features;

import net.id.aether.world.feature.AetherFeatures;
import net.id.aether.world.feature.configs.BoulderFeatureConfig;
import net.id.aether.world.feature.configs.GroundcoverFeatureConfig;
import net.id.aether.world.feature.configs.QuicksoilConfig;
import net.id.aether.world.feature.placed_features.AetherPlacedFeatures;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Properties;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.BlockFilterPlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

import java.util.List;

import static net.id.aether.blocks.AetherBlocks.*;

public class AetherMiscConfiguredFeatures extends AetherConfiguredFeatures{
    /*
    Highlands
     */
    // Default
    public static final RegistryEntry<ConfiguredFeature<QuicksoilConfig, ?>> QUICKSOIL = register("quicksoil", AetherFeatures.QUICKSOIL, Configs.QUICKSOIL_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<SpringFeatureConfig, ?>> WATER_SPRING = register("water_spring", Feature.SPRING_FEATURE, Configs.WATER_SPRING_CONFIG);

    public static final RegistryEntry<ConfiguredFeature<BoulderFeatureConfig, ?>> GENERIC_BOULDER = register("generic_boulder", AetherFeatures.BOULDER, Configs.GENERIC_BOULDER_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<BoulderFeatureConfig, ?>> PLAINS_BOULDER = register("plains_boulder", AetherFeatures.BOULDER, Configs.PLAINS_BOULDER_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<BoulderFeatureConfig, ?>> THICKET_BOULDER = register("thicket_boulder", AetherFeatures.BOULDER, Configs.THICKET_BOULDER_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<BoulderFeatureConfig, ?>> GOLDEN_BOULDER = register("golden_boulder", AetherFeatures.BOULDER, Configs.GOLDEN_BOULDER_CONFIG);

    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> ORE_AMBROSIUM = register("ore_ambrosium", Feature.ORE, Configs.ore(AMBROSIUM_ORE, 14));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> ORE_GRAVITITE = register("ore_gravitite", Feature.ORE, Configs.ore(GRAVITITE_ORE, 6));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> ORE_ZANITE = register("ore_zanite", Feature.ORE, Configs.ore(ZANITE_ORE, 9));
    // Plato
    // Shield
    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> SHIELD_ROCKS = register("shield_rocks", Feature.RANDOM_PATCH, Configs.SHIELD_ROCKS_CONFIG);

    public static final RegistryEntry<ConfiguredFeature<DeltaFeatureConfig, ?>> SHIELD_PONDS = register("shield_pond", AetherFeatures.AETHER_DELTA_FEATURE, new DeltaFeatureConfig(Blocks.WATER.getDefaultState(), COBBLED_HOLYSTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM).with(Properties.WATERLOGGED, true), UniformIntProvider.create(2, 7), UniformIntProvider.create(1, 2)));

    public static final RegistryEntry<ConfiguredFeature<GroundcoverFeatureConfig, ?>> SHIELD_STONE = register("shield_stone", AetherFeatures.GROUNDCOVER_FEATURE, new GroundcoverFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(HOLYSTONE.getDefaultState(), 7).add(COBBLED_HOLYSTONE.getDefaultState(), 5).add(MOSSY_HOLYSTONE.getDefaultState(), 2).build()), UniformIntProvider.create(1, 4), UniformIntProvider.create(0, 0)));
    public static final RegistryEntry<ConfiguredFeature<GroundcoverFeatureConfig, ?>> SHIELD_PODZOL = register("shield_podzol", AetherFeatures.GROUNDCOVER_FEATURE, new GroundcoverFeatureConfig(BlockStateProvider.of(AETHER_FROZEN_GRASS), UniformIntProvider.create(2, 3), UniformIntProvider.create(0, 0)));
    // Tundra
    public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> TUNDRA_SPIRES = register("tundra_spires", AetherFeatures.ICESTONE_SPIRE_FEATURE, FeatureConfig.DEFAULT);

    public static final RegistryEntry<ConfiguredFeature<DeltaFeatureConfig, ?>> TUNDRA_PONDS = register("tundra_pond", AetherFeatures.AETHER_DELTA_FEATURE, new DeltaFeatureConfig(Blocks.ICE.getDefaultState(), Blocks.PACKED_ICE.getDefaultState(), UniformIntProvider.create(4, 9), UniformIntProvider.create(0, 1)));
    public static final RegistryEntry<ConfiguredFeature<DeltaFeatureConfig, ?>> TUNDRA_SNOW = register("tundra_snow", AetherFeatures.AETHER_DELTA_FEATURE, new DeltaFeatureConfig(Blocks.POWDER_SNOW.getDefaultState(), Blocks.SNOW_BLOCK.getDefaultState(), UniformIntProvider.create(3, 8), UniformIntProvider.create(0, 1)));

    public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> FREEZE_AETHER_TOP_LAYER = register("freeze_aether_top_layer", AetherFeatures.FREEZE_AETHER_TOP_LAYER_FEATURE_FEATURE, FeatureConfig.DEFAULT);

    // TODO 1.7 uncomment public static final RegistryEntry<ConfiguredFeature<DynamicConfiguration, ?>> AETHER_LAKE = register("aether_lake", AetherFeatures.AETHER_LAKE, new DynamicConfiguration(Blocks.WATER.getDefaultState(), Optional.of("normal")));

    private static class Configs extends AetherConfiguredFeatures.Configs{
        private static final QuicksoilConfig QUICKSOIL_CONFIG = new QuicksoilConfig();
        private static final SpringFeatureConfig WATER_SPRING_CONFIG = new SpringFeatureConfig(Fluids.WATER.getDefaultState(), true, 4, 1, RegistryEntryList.of(Block::getRegistryEntry, HOLYSTONE));

        private static final RandomPatchFeatureConfig SHIELD_ROCKS_CONFIG = new RandomPatchFeatureConfig(48, 9, 3, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(
                DataPool.<BlockState>builder()
                        .add(COBBLED_HOLYSTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM), 10)
                        .add(COBBLED_HOLYSTONE.getDefaultState(), 4)
                        .build())
        ), BlockFilterPlacementModifier.of(AetherPlacedFeatures.IN_OR_ON_GROUND)));

        private static BoulderFeatureConfig boulder(BlockStateProvider provider, int tries, IntProvider size){
            return new BoulderFeatureConfig(BlockStateProvider.of(COBBLED_HOLYSTONE), ConstantIntProvider.create(tries), size);
        }
        private static BoulderFeatureConfig boulder(Block block, int tries, IntProvider size){
            return boulder(BlockStateProvider.of(block), tries, size);
        }

        private static final BoulderFeatureConfig GENERIC_BOULDER_CONFIG = boulder(new WeightedBlockStateProvider(
                DataPool.<BlockState>builder()
                        .add(MOSSY_HOLYSTONE.getDefaultState(), 1)
                        .add(COBBLED_HOLYSTONE.getDefaultState(), 3)
        ), 4, UniformIntProvider.create(3, 6));

        private static final BoulderFeatureConfig PLAINS_BOULDER_CONFIG = boulder(COBBLED_HOLYSTONE, 3, UniformIntProvider.create(3, 5));
        private static final BoulderFeatureConfig THICKET_BOULDER_CONFIG = boulder(new WeightedBlockStateProvider(
                DataPool.<BlockState>builder()
                        .add(MOSSY_HOLYSTONE.getDefaultState(), 4)
                        .add(COBBLED_HOLYSTONE.getDefaultState(), 1)
        ), 6, UniformIntProvider.create(2, 5));

        private static final BoulderFeatureConfig GOLDEN_BOULDER_CONFIG = boulder(new WeightedBlockStateProvider(
                DataPool.<BlockState>builder()
                        .add(GOLDEN_MOSSY_HOLYSTONE.getDefaultState(), 4)
                        .add(COBBLED_HOLYSTONE.getDefaultState(), 1)
        ), 4, UniformIntProvider.create(3, 5));

        private static OreFeatureConfig ore(Block ore, int size){
            return new OreFeatureConfig(List.of(OreFeatureConfig.createTarget(new BlockMatchRuleTest(HOLYSTONE), ore.getDefaultState())), size);
        }
    }
    public static void init(){}
}
