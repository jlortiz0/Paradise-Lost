package net.id.aether.items.food;

import net.id.aether.items.AetherItems;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ValkyrieMilkItem extends Item {
    public ValkyrieMilkItem(Settings settings) {
        super(settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        super.finishUsing(stack, world, user);
        return stack.isEmpty() ? new ItemStack(AetherItems.QUICKSOIL_VIAL) : stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText("item.the_aether.valkyrie_milk.description1"));
        for (int i = 0; i < 2; i++) {
            tooltip.add(new TranslatableText("item.the_aether.valkyrie_milk.description2"));
        }
        tooltip.add(new TranslatableText("item.the_aether.valkyrie_milk.description3"));
    }
}
