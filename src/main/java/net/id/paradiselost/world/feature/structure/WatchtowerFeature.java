package net.id.paradiselost.world.feature.structure;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.world.feature.structure.generator.WatchtowerGenerator;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Optional;

import static net.minecraft.world.gen.structure.Structure.createCodec;

public class WatchtowerFeature extends Structure {
    public static final Codec<WatchtowerFeature> CODEC = createCodec(WatchtowerFeature::new);

    private static final int X_OFFSET = 4;
    private static final int Z_OFFSET = 4;

    public WatchtowerFeature(Structure.Config config) {
        super(config);
    }

    private static void addPieces(StructurePiecesCollector collector, Structure.Context context) {
        StructureTemplate structure = context.structureTemplateManager().getTemplateOrBlank(ParadiseLost.locate("watchtower"));
        BlockRotation blockRotation = BlockRotation.NONE;
        ChunkPos pos = context.chunkPos();
        BlockPos pivot = new BlockPos(structure.getSize().getX() / 2, 0, structure.getSize().getZ() / 2);
        BlockBox boundingBox = structure.calculateBoundingBox(pos.getStartPos(), blockRotation, pivot, BlockMirror.NONE);
        BlockPos center = boundingBox.getCenter();
        int y = context.chunkGenerator().getHeight(pos.getStartPos().getX() - X_OFFSET, pos.getStartPos().getZ() - Z_OFFSET, Heightmap.Type.WORLD_SURFACE_WG, context.world(), context.noiseConfig());
        if (y < 0) { // DON'T PLACE ON THE BOTTOM OF THE WORLD
            return;
        }
        BlockPos newPos = new BlockPos(pos.getStartPos().getX() - X_OFFSET, y, pos.getStartPos().getZ() - Z_OFFSET);
        WatchtowerGenerator.addPieces(context.structureTemplateManager(), collector, BlockRotation.NONE, newPos);
    }

    @Override
    public Optional<Structure.StructurePosition> getStructurePosition(Structure.Context context) {
        context.random().nextDouble();
        ChunkPos chunkPos = context.chunkPos();
        BlockPos blockPos = new BlockPos(chunkPos.getCenterX(), 50, chunkPos.getStartZ());
        StructurePiecesCollector structurePiecesCollector = new StructurePiecesCollector();
        addPieces(structurePiecesCollector, context);
        return Optional.of(new Structure.StructurePosition(blockPos, Either.right(structurePiecesCollector)));
    }

    @Override
    public StructureType<?> getType() {
        return ParadiseLostStructureFeatures.WATCHTOWER;
    }
}
