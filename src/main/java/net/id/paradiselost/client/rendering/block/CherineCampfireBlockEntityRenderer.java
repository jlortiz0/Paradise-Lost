package net.id.paradiselost.client.rendering.block;

import net.id.paradiselost.blocks.blockentity.CherineCampfireBlockEntity;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import org.joml.Vector3f;

public class CherineCampfireBlockEntityRenderer implements BlockEntityRenderer<CherineCampfireBlockEntity> {

    public CherineCampfireBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }
    
    @Override
    public void render(CherineCampfireBlockEntity campfireBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        Direction direction = campfireBlockEntity.getCachedState().get(CampfireBlock.FACING);
        DefaultedList<ItemStack> defaultedList = campfireBlockEntity.getItemsBeingCooked();
        int k = (int) campfireBlockEntity.getPos().asLong();
        
        for (int l = 0; l < defaultedList.size(); ++l) {
            ItemStack itemStack = defaultedList.get(l);
            if (itemStack != ItemStack.EMPTY) {
                matrixStack.push();
                matrixStack.translate(0.5D, 0.44921875D, 0.5D);
                Direction direction2 = Direction.fromHorizontal((l + direction.getHorizontal()) % 4);
                float g = -direction2.asRotation();
                matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(g));
                matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
                matrixStack.translate(-0.3125D, -0.3125D, 0.0D);
                matrixStack.scale(0.375F, 0.375F, 0.375F);
                MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformation.Mode.FIXED, i, j, matrixStack, vertexConsumerProvider, k + l);
                matrixStack.pop();
            }
        }
        
    }
}
