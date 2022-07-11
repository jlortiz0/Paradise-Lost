package net.id.aether.world.gen.structure.generator;

import com.mojang.datafixers.util.Pair;
import net.immortaldevs.jigsort.api.JigsawInfo;
import net.immortaldevs.jigsort.impl.JigsortJigsawBlockEntity.ConflictMode;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.JigsawBlockEntity.Joint;
import net.minecraft.block.enums.JigsawOrientation;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.List;

import static net.id.aether.Aether.locate;
import static net.immortaldevs.jigsort.api.StructurePoolElements.box;
import static net.immortaldevs.jigsort.api.StructurePoolElements.connecting;
import static net.immortaldevs.jigsort.api.StructurePoolElements.single;

public class SliderLabyrinthGenerator {
    private static final Identifier EMPTY = new Identifier("empty");
    private static final JigsawInfo[] DIVIDER_JIGSAWS = new JigsawInfo[76];

    public static final RegistryEntry<StructurePool> STRUCTURE_POOLS = StructurePools.register(new StructurePool(
            locate("labyrinth/base"),
            EMPTY,
            List.of(Pair.of(box(new BlockBox(-75, -22, -75, 75, 8, 75),
                    BlockStateProvider.of(Blocks.AIR),
                    JigsawInfo.of(0, -13, -71, JigsawOrientation.SOUTH_UP)
                            .pool(locate("labyrinth/foyers"))
                            .name(EMPTY)
                            .target(locate("foyer_entrance"))
                            .immediateChance(100)
                            .joint(Joint.ALIGNED)
                            .conflictMode(ConflictMode.FORCE)), 1))));

    public static void init() {
        StructurePools.register(new StructurePool(
                locate("labyrinth/foyers"),
                EMPTY,
                List.of(Pair.of(single(locate("labyrinth/foyers/base")), 1))));

        StructurePools.register(new StructurePool(
                locate("labyrinth/arenas"),
                EMPTY,
                List.of(Pair.of(single(locate("labyrinth/arenas/base")), 1))));

        StructurePools.register(new StructurePool(
                locate("labyrinth/foyer_hallway"),
                EMPTY,
                List.of(Pair.of(single(locate("labyrinth/foyer_hallway")), 1))));

        StructurePools.register(new StructurePool(
                locate("labyrinth/arena_hallway"),
                EMPTY,
                List.of(Pair.of(single(locate("labyrinth/arena_hallway")), 1))));
        StructurePools.register(new StructurePool(
                locate("labyrinth/foyer_corridor"),
                EMPTY,
                List.of(Pair.of(single(locate("labyrinth/foyer_corridor")), 1))));

        StructurePools.register(new StructurePool(
                locate("labyrinth/hallway_end"),
                EMPTY,
                List.of(Pair.of(single(locate("labyrinth/hallway_end")), 1))));

        StructurePools.register(new StructurePool(
                locate("labyrinth/hallways"),
                locate("labyrinth/hallway_end"),
                List.of(Pair.of(single(locate("labyrinth/hallways/base")), 16),
                        Pair.of(single(locate("labyrinth/hallways/living_wall")), 1))));

        StructurePools.register(new StructurePool(
                locate("labyrinth/hallway_walls"),
                EMPTY,
                List.of(Pair.of(single(locate("labyrinth/hallway_walls/horizontal_corridor")), 1),
                        Pair.of(single(locate("labyrinth/hallway_walls/upwards_corridor")), 1),
                        Pair.of(single(locate("labyrinth/hallway_walls/downwards_corridor")), 1),
                        Pair.of(single(locate("labyrinth/hallway_walls/horizontal")), 5),
                        Pair.of(single(locate("labyrinth/hallway_walls/upwards")), 5),
                        Pair.of(single(locate("labyrinth/hallway_walls/downwards")), 5))));

        StructurePools.register(new StructurePool(
                locate("labyrinth/walls"),
                EMPTY,
                List.of(Pair.of(single(locate("labyrinth/walls/horizontal")), 24),
                        Pair.of(single(locate("labyrinth/walls/upwards")), 24),
                        Pair.of(single(locate("labyrinth/walls/downwards")), 24),
                        Pair.of(single(locate("labyrinth/walls/living_wall")), 1))));

        StructurePools.register(new StructurePool(
                locate("labyrinth/corridors"),
                locate("labyrinth/walls"),
                List.of(Pair.of(single(locate("labyrinth/corridors/horizontal")), 1),
                        Pair.of(single(locate("labyrinth/corridors/upwards")), 1),
                        Pair.of(single(locate("labyrinth/corridors/downwards")), 1))));

        StructurePools.register(new StructurePool(
                locate("labyrinth/column"),
                EMPTY,
                List.of(Pair.of(single(locate("labyrinth/column")), 1))));

        StructurePools.register(new StructurePool(
                locate("labyrinth/beam"),
                EMPTY,
                List.of(Pair.of(single(locate("labyrinth/beam")), 1))));

        StructurePools.register(new StructurePool(
                locate("labyrinth/shoulder"),
                EMPTY,
                List.of(Pair.of(single(locate("labyrinth/shoulder")), 1))));

        StructurePools.register(new StructurePool(
                locate("labyrinth/divider"),
                EMPTY,
                List.of(Pair.of(connecting(DIVIDER_JIGSAWS), 1))));
    }

    static {
        DIVIDER_JIGSAWS[0] = JigsawInfo.of(15, 13, 0, JigsawOrientation.NORTH_UP)
                .pool(EMPTY)
                .name(locate("divider"))
                .target(EMPTY)
                .priority(-1)
                .immediateChance(100)
                .conflictMode(ConflictMode.GHOST);

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 4; y++) {
                DIVIDER_JIGSAWS[x * 4 + y + 1] = JigsawInfo.of(x * 6 + 3, y * 6 + 3, 7,
                                JigsawOrientation.SOUTH_UP)
                        .pool(locate("labyrinth/beam"))
                        .name(EMPTY)
                        .target(locate("beam"))
                        .immediateChance(100);
            }
        }

        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 5; y++) {
                DIVIDER_JIGSAWS[x * 5 + y + 21] = JigsawInfo.of(x * 6, y * 6, 7,
                                JigsawOrientation.SOUTH_UP)
                        .pool(locate("labyrinth/column"))
                        .name(EMPTY)
                        .target(locate("column"))
                        .immediateChance(100);
            }
        }

        for (int x = 0; x < 5; x++)
            for (int y = 0; y < 5; y++) {
                DIVIDER_JIGSAWS[x * 5 + y + 51] = JigsawInfo.of(x * 6 + 3, y * 6, 7,
                                JigsawOrientation.SOUTH_UP)
                        .pool(locate("labyrinth/walls"))
                        .name(EMPTY)
                        .target(locate("corridor"))
                        .priority(1)
                        .immediateChance(100);
            }
    }
}
