package net.id.aether.world.feature.structure.sliderdungeon;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public final class SliderDungeonGenerator{
    private static final int UNIT_SCALE_FACTOR = 9;
    
    private static final int DUNGEON_SIZE = 8;
    
    // This margin is on purpose.
    private final PlacedPiece[] layout = new PlacedPiece[(DUNGEON_SIZE + 2) * (DUNGEON_SIZE + 2)];
    private final Random random;
    
    SliderDungeonGenerator(Random random){
        this.random = random;
    }
    
    public void generate(StructureManager manager, BlockPos pos, List<StructurePiece> pieces){
        placePiece(Piece.START, 0, 0, 0);
        placePieceRandomly(Piece.BOSS);
        capEmptyConnections();
        
        addPieces(manager, pos.toImmutable(), pieces);
    }
    
    private void capEmptyConnections(){
        for(int z = 0; z < DUNGEON_SIZE; z++){
            for(int x = 0; x < DUNGEON_SIZE; x++){
                var piece = getPiece(z, 0, x);
                if(piece != null){
                    for(var connection : piece.piece().connections){
                        int capX = x + connection.getOffsetX();
                        int capZ = z + connection.getOffsetZ();
                        if(getMarginPiece(capX, 0, capZ) == null){
                            placeMarginPiece(Piece.END, findConnection(Piece.END, connection.getOpposite()), capX, 0, capZ);
                        }
                    }
                }
            }
        }
    }
    
    private BlockRotation findConnection(Piece piece, Direction... requiredConnections){
        return findConnection(piece, Set.of(requiredConnections));
    }
    
    //TODO Figure out a better way to do this.
    private BlockRotation findConnection(Piece piece, Set<Direction> requiredConnections){
        if(requiredConnections.isEmpty()){
            return BlockRotation.NONE;
        }
        
        var pieceConnections = piece.connections;
        for(BlockRotation rotation : BlockRotation.values()){
            var rotatedConnections = pieceConnections.stream()
                .map(rotation::rotate)
                .collect(Collectors.toUnmodifiableSet());
            if(requiredConnections.containsAll(rotatedConnections)){
                return rotation;
            }
        }
        
        var builder = new StringBuilder("Failed to find a rotation to connect ")
            .append(piece.name())
            .append(" to [");
        for(Direction dir : requiredConnections){
            builder.append(dir.name()).append(", ");
        }
        builder.setLength(builder.length() - 2);
        builder.append(']');
        throw new RuntimeException(builder.toString());
    }
    
    private void addPieces(StructureManager manager, BlockPos origin, List<StructurePiece> pieces){
        for(int z = -1; z < DUNGEON_SIZE + 1; z++){
            for(int x = -1; x < DUNGEON_SIZE + 1; x++){
                var piece = getMarginPiece(x, 0, z);
                if(piece != null){
                    // pieces.add(new SliderDungeonStartPiece(manager, SliderDungeonFeature.START, pos));
                    var pos = origin.add((z - DUNGEON_SIZE / 2) * DUNGEON_SIZE, 0, (x - DUNGEON_SIZE / 2) * DUNGEON_SIZE);
                    pieces.add(piece.createStructurePiece(manager, pos));
                }
            }
        }
    }
    
    private void placePiece(Piece piece, int x, int y, int z){
        placePiece(piece, BlockRotation.NONE, x, y, z);
    }
    
    private void placePiece(Piece piece, BlockRotation rotation, int x, int y, int z){
        if(y != 0){
            throw new IllegalArgumentException("No support for multiple layers at this time");
        }
        if(x < 0 || x > DUNGEON_SIZE){
            throw new IllegalArgumentException("X was out of bounds: " + x);
        }
        if(z < 0 || z > DUNGEON_SIZE){
            throw new IllegalArgumentException("Z was out of bounds: " + z);
        }
        x++;
        z++;
        layout[x + z * (DUNGEON_SIZE + 2)] = new PlacedPiece(piece, rotation);
    }
    
    private void placeMarginPiece(Piece piece, BlockRotation rotation, int x, int y, int z){
        if(y != 0){
            throw new IllegalArgumentException("No support for multiple layers at this time");
        }
        if(x < -1 || x > DUNGEON_SIZE + 1){
            throw new IllegalArgumentException("X was out of bounds: " + x);
        }
        if(z < -1 || z > DUNGEON_SIZE + 1){
            throw new IllegalArgumentException("Z was out of bounds: " + z);
        }
        x++;
        z++;
        layout[x + z * (DUNGEON_SIZE + 2)] = new PlacedPiece(piece, rotation);
    }
    
    private PlacedPiece getPiece(int x, int y, int z){
        if(y != 0){
            throw new IllegalArgumentException("No support for multiple layers at this time");
        }
        if(x < 0 || x > DUNGEON_SIZE){
            throw new IllegalArgumentException("X was out of bounds: " + x);
        }
        if(z < 0 || z > DUNGEON_SIZE){
            throw new IllegalArgumentException("Z was out of bounds: " + z);
        }
        x++;
        z++;
        return layout[x + z * (DUNGEON_SIZE + 2)];
    }
    
    private PlacedPiece getMarginPiece(int x, int y, int z){
        if(y != 0){
            throw new IllegalArgumentException("No support for multiple layers at this time");
        }
        if(x < -1 || x > DUNGEON_SIZE + 1){
            throw new IllegalArgumentException("X was out of bounds: " + x);
        }
        if(z < -1 || z > DUNGEON_SIZE + 1){
            throw new IllegalArgumentException("Z was out of bounds: " + z);
        }
        x++;
        z++;
        return layout[x + z * (DUNGEON_SIZE + 2)];
    }
    
    private void placePieceRandomly(Piece piece){
        int x;
        int z;
        do{
            x = random.nextInt(DUNGEON_SIZE);
            z = random.nextInt(DUNGEON_SIZE);
        }while(layout[x + z * DUNGEON_SIZE] != null);
        placePiece(piece, BlockRotation.random(random), x, 0, z);
    }
    
    private static final Direction[] HORIZONTAL = new Direction[]{
        Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST
    };
    
    private record PlacedPiece(
        Piece piece,
        BlockRotation rotation
    ){
        PlacedPiece(Piece piece){
            this(piece, BlockRotation.NONE);
        }
    
        public StructurePiece createStructurePiece(StructureManager manager, BlockPos pos){
            return piece().factory.create(manager, pos, rotation());
        }
    
        public void forEachConnection(Consumer<Direction> action){
            piece().connections.forEach(action);
        }
    }
    
    @FunctionalInterface
    private interface PieceFactory{
        StructurePiece create(StructureManager manager, BlockPos pos, BlockRotation rotation);
    }
    
    private enum Piece{
        START(SliderDungeonStartPiece::new, 9, 8, 9, HORIZONTAL),
        BOSS(SliderDungeonBossPiece::new, 27, 27, 27, HORIZONTAL),
        END(SliderDungeonEndPiece::new, 5, 5, 5, Direction.WEST),
        ;
    
        private final PieceFactory factory;
        private final int sizeX;
        private final int sizeY;
        private final int sizeZ;
        private final Set<Direction> connections;
        
        Piece(PieceFactory factory, int sizeX, int sizeY, int sizeZ, Direction... connections){
            this.factory = factory;
            
            this.sizeX = unitSize(sizeX);
            this.sizeY = unitSize(sizeY);
            this.sizeZ = unitSize(sizeZ);
            
            this.connections = Set.of(connections);
        }
    
        private static int unitSize(int size){
            return (int)Math.ceil(size / (double)UNIT_SCALE_FACTOR);
        }
    }
}
