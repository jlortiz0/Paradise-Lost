package net.id.paradiselost.devel;

import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.entities.block.SliderEntity;
import net.id.paradiselost.items.ParadiseLostItemGroups;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.ActionResult;

/**
 * Tools for paradise lost development, such as blocks and items
 */
// Package-private to avoid being called in production
class ParadiseLostDevTools {
    /**
     * Spawns a slider
     */
    private static final Item SLIDER_TEST_ITEM = new Item(new Item.Settings().group(ParadiseLostItemGroups.PARADISE_LOST_MISC)) {
        @Override
        public ActionResult useOnBlock(ItemUsageContext context) {
            boolean sneaking = false;
            if (context.getPlayer() != null) {
                sneaking = context.getPlayer().isSneaking();
            }
            SliderEntity slider;
            if (!sneaking) {
                slider = new SliderEntity(context.getWorld(), context.getBlockPos().getX() + 0.5D, context.getBlockPos().getY(), context.getBlockPos().getZ() + 0.5D, context.getPlayerFacing().getOpposite());
                slider.setBlockState(context.getWorld().getBlockState(context.getBlockPos()));
            } else {
                slider = new SliderEntity(context.getWorld(), context.getHitPos().getX(), context.getHitPos().getY(), context.getHitPos().getZ(), context.getPlayerFacing().getOpposite());
                slider.moveTime = 1;
            }
            context.getWorld().spawnEntity(slider);
            return ActionResult.SUCCESS;
        }
    };

    static void init() {
        Registry.register(Registries.ITEM, ParadiseLost.locate("slider_test_item"), SLIDER_TEST_ITEM);
    }

//    static { // TODO: Do we need this to be reimplemented?
//        if (!Devel.isDevel()) {
//            ParadiseLost.LOG.error(LogUtils.FATAL_MARKER, "!!\n!!\n!!\n!!ParadiseLostDevItems called in production environment! Please report this to Paradise Lost developers!");
//            new RuntimeException("").printStackTrace();
//        }
//    }
}
