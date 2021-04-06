package me.pvplikeaboss.sinistercore.sinistercore.utilites.misc;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class Items {
    public static boolean compareItemStack(ItemStack item1, ItemStack item2) {
        Material item1_mat = null;
        Material item2_mat = null;
        int item1_amount = -1;
        int item2_amount = -1;
        ItemMeta item1_meta = null;
        ItemMeta item2_meta = null;
        boolean ret = false;


        if(item1 == null && item2 == null) {// both items are null - true
            ret = true;
            return ret;
        }

        if(item1 == null || item2 == null) {// one of the items are null - false
            return ret;
        }

        item1_mat = item1.getType();
        item2_mat = item2.getType();

        if(item1_mat != item2_mat) {// not same materials - false
            return ret;
        }

        item1_amount = item1.getAmount();
        item2_amount = item2.getAmount();

        if(item1_amount != item2_amount) {// 2 different amounts - false
            return ret;
        }

        if((item1.hasItemMeta() == true) && (item2.hasItemMeta() == true)) {
            item1_meta = item1.getItemMeta();
            item2_meta = item2.getItemMeta();
            if ((item1_meta.hasDisplayName() == true) && (item2_meta.hasDisplayName() == true)) {
                if (item1_meta.getDisplayName().equals(item2_meta.getDisplayName()) == false) {// not same display name - false
                    return ret;
                }
            }
            if((item1_meta.hasEnchants() == true) && (item2_meta.hasEnchants() == true)) {
                if(item1_meta.getEnchants().equals(item2_meta.getEnchants()) == false) {
                    return ret;
                }
            }
            if((item1_meta.hasLore() == true) && (item2_meta.hasLore() == true)) {
                if(item1_meta.getLore().equals(item2_meta.getLore()) == false) {
                    return ret;
                }
            }
        }

        ret = true;
        return ret;
    }

    public static ItemStack createItemStack(String name, int amount, Material material, ArrayList<String> enchants) {
        ItemStack pItem = new ItemStack(material, amount);
        HashMap<Enchantment, Integer> enchantmentIntegerMap = new HashMap<Enchantment, Integer>();
        if(enchants != null && !enchants.isEmpty()) {
            for (String s : enchants) {
                String[] ss = s.split(":");
                enchantmentIntegerMap.put(Enchantment.getByName(ss[0]), Integer.parseInt(ss[1]));
            }
            pItem.addEnchantments(enchantmentIntegerMap);
        }
        ItemMeta pItemMeta = pItem.getItemMeta();
        if(name != null) {
            pItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }
        pItem.setItemMeta(pItemMeta);
        return pItem;
    }
}
