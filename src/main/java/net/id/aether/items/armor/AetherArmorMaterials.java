package net.id.aether.items.armor;

import net.id.aether.items.AetherItems;
import net.id.aether.items.utils.IngredientUtil;
import net.id.aether.mixin.item.ArmorMaterialsAccessor;
import net.id.aether.util.AetherSoundEvents;
import net.minecraft.item.ArmorMaterial;

@SuppressWarnings("unused")
public class AetherArmorMaterials {
    public static final ArmorMaterial ZANITE = ArmorMaterialsAccessor.callInit("AETHER_ZANITE", -1,
            "aether_zanite", 15, new int[]{2, 5, 6, 2}, 9, AetherSoundEvents.ITEM_ARMOR_EQUIP_ZANITE,
            0f, 0f, IngredientUtil.itemIngredient(AetherItems.ZANITE_GEM));

    public static final ArmorMaterial GRAVITITE = ArmorMaterialsAccessor.callInit("AETHER_GRAVITITE", -1,
            "aether_gravitite", 33, new int[]{3, 6, 8, 3}, 10, AetherSoundEvents.ITEM_ARMOR_EQUIP_GRAVITITE,
            2f, 0f, IngredientUtil.itemIngredient(AetherItems.GRAVITITE_GEM));

    public static final ArmorMaterial NEPTUNE = ArmorMaterialsAccessor.callInit("AETHER_NEPTUNE", -1,
            "aether_neptune", 35, new int[]{4, 6, 8, 4}, 8, AetherSoundEvents.ITEM_ARMOR_EQUIP_NEPTUNE,
            1.5f, 0f, IngredientUtil.EMPTY);

    public static final ArmorMaterial PHOENIX = ArmorMaterialsAccessor.callInit("AETHER_GRAVITITE", -1,
            "aether_phoenix", 35, new int[]{4, 6, 8, 4}, 10, AetherSoundEvents.ITEM_ARMOR_EQUIP_PHOENIX,
            1.5F, 0f, IngredientUtil.EMPTY);

    public static final ArmorMaterial OBSIDIAN = ArmorMaterialsAccessor.callInit("AETHER_GRAVITITE", -1,
            "aether_obsidian", 38, new int[]{4, 8, 10, 4}, 6, AetherSoundEvents.ITEM_ARMOR_EQUIP_OBSIDIAN,
            2f, 0.1f, IngredientUtil.EMPTY);

    public static final ArmorMaterial VALKYRIE = ArmorMaterialsAccessor.callInit("AETHER_GRAVITITE", -1,
            "aether_valkyrie", 35, new int[]{4, 8, 10, 4}, 10, AetherSoundEvents.ITEM_ARMOR_EQUIP_VALKYRIE,
            1.5f, 0.1f, IngredientUtil.EMPTY);

    public static final ArmorMaterial SENTRY = ArmorMaterialsAccessor.callInit("AETHER_GRAVITITE", -1,
            "aether_sentry", 35, new int[]{4, 8, 10, 4}, 10, AetherSoundEvents.ITEM_ARMOR_EQUIP_SENTRY,
            1.5f, 0f, IngredientUtil.EMPTY);
}