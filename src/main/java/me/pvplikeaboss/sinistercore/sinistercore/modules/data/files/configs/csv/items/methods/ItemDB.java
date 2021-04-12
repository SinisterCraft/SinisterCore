package me.pvplikeaboss.sinistercore.sinistercore.modules.data.files.configs.csv.items.methods;

import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.typeutils.MaterialUtil;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.typeutils.NumberUtil;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.typeutils.StringUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

public interface ItemDB {
    default ItemStack get(final String name, final int quantity) throws Exception {
        final ItemStack stack = get(name);
        stack.setAmount(quantity);
        return stack;
    }

    ItemStack get(final String name) throws Exception;

    default String names(final ItemStack item) {
        List<String> nameList = nameList(item);

        if (nameList.size() > 15) {
            nameList = nameList.subList(0, 14);
        }
        return StringUtil.joinList(", ", nameList);
    }

    List<String> nameList(ItemStack item);

    String name(ItemStack item);

    List<ItemStack> getMatching(PlayerObject user, String[] args) throws Exception;

    String serialize(ItemStack is);

    Collection<String> listNames();

    @Deprecated
    default Material getFromLegacyId(final int id) {
        return getFromLegacy(id, (byte) 0);
    }

    @Deprecated
    int getLegacyId(Material material) throws Exception;

    default Material getFromLegacy(final String item) {
        final String[] split = item.split(":");

        if (!NumberUtil.isInt(split[0])) return null;

        final int id = Integer.parseInt(split[0]);
        byte damage = 0;

        if (split.length > 1 && NumberUtil.isInt(split[1])) {
            damage = Byte.parseByte(split[1]);
        }

        return getFromLegacy(id, damage);
    }

    default Material getFromLegacy(final int id, final byte damage) {
        return MaterialUtil.convertFromLegacy(id, damage);
    }

    boolean isReady();
}
