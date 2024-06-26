package me.pvplikeaboss.sinistercore.sinistercore.utilites.misc;

import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class Enchantments {
    public static final Map<String, Enchantment> ENCHANTMENTS = new HashMap<>();
    public static final Map<String, Enchantment> ALIASENCHANTMENTS = new HashMap<>();

    static {
        ENCHANTMENTS.put("alldamage", Enchantment.DAMAGE_ALL);
        ALIASENCHANTMENTS.put("alldmg", Enchantment.DAMAGE_ALL);
        ALIASENCHANTMENTS.put("sharpness", Enchantment.DAMAGE_ALL);
        ALIASENCHANTMENTS.put("sharp", Enchantment.DAMAGE_ALL);
        ALIASENCHANTMENTS.put("dal", Enchantment.DAMAGE_ALL);

        ENCHANTMENTS.put("baneofarthropods", Enchantment.DAMAGE_ARTHROPODS);
        ALIASENCHANTMENTS.put("ardmg", Enchantment.DAMAGE_ARTHROPODS);
        ALIASENCHANTMENTS.put("baneofarthropod", Enchantment.DAMAGE_ARTHROPODS);
        ALIASENCHANTMENTS.put("arthropod", Enchantment.DAMAGE_ARTHROPODS);
        ALIASENCHANTMENTS.put("dar", Enchantment.DAMAGE_ARTHROPODS);

        ENCHANTMENTS.put("smite", Enchantment.DAMAGE_UNDEAD);
        ALIASENCHANTMENTS.put("undeaddamage", Enchantment.DAMAGE_UNDEAD);
        ALIASENCHANTMENTS.put("du", Enchantment.DAMAGE_UNDEAD);

        ENCHANTMENTS.put("efficiency", Enchantment.DIG_SPEED);
        ALIASENCHANTMENTS.put("digspeed", Enchantment.DIG_SPEED);
        ALIASENCHANTMENTS.put("minespeed", Enchantment.DIG_SPEED);
        ALIASENCHANTMENTS.put("cutspeed", Enchantment.DIG_SPEED);
        ALIASENCHANTMENTS.put("ds", Enchantment.DIG_SPEED);
        ALIASENCHANTMENTS.put("eff", Enchantment.DIG_SPEED);

        ENCHANTMENTS.put("unbreaking", Enchantment.DURABILITY);
        ALIASENCHANTMENTS.put("durability", Enchantment.DURABILITY);
        ALIASENCHANTMENTS.put("dura", Enchantment.DURABILITY);
        ALIASENCHANTMENTS.put("d", Enchantment.DURABILITY);

        ENCHANTMENTS.put("thorns", Enchantment.THORNS);
        ALIASENCHANTMENTS.put("highcrit", Enchantment.THORNS);
        ALIASENCHANTMENTS.put("thorn", Enchantment.THORNS);
        ALIASENCHANTMENTS.put("highercrit", Enchantment.THORNS);
        ALIASENCHANTMENTS.put("t", Enchantment.THORNS);

        ENCHANTMENTS.put("fireaspect", Enchantment.FIRE_ASPECT);
        ALIASENCHANTMENTS.put("fire", Enchantment.FIRE_ASPECT);
        ALIASENCHANTMENTS.put("meleefire", Enchantment.FIRE_ASPECT);
        ALIASENCHANTMENTS.put("meleeflame", Enchantment.FIRE_ASPECT);
        ALIASENCHANTMENTS.put("fa", Enchantment.FIRE_ASPECT);

        ENCHANTMENTS.put("knockback", Enchantment.KNOCKBACK);
        ALIASENCHANTMENTS.put("kback", Enchantment.KNOCKBACK);
        ALIASENCHANTMENTS.put("kb", Enchantment.KNOCKBACK);
        ALIASENCHANTMENTS.put("k", Enchantment.KNOCKBACK);

        ENCHANTMENTS.put("fortune", Enchantment.LOOT_BONUS_BLOCKS);
        ALIASENCHANTMENTS.put("blockslootbonus", Enchantment.LOOT_BONUS_BLOCKS);
        ALIASENCHANTMENTS.put("fort", Enchantment.LOOT_BONUS_BLOCKS);
        ALIASENCHANTMENTS.put("lbb", Enchantment.LOOT_BONUS_BLOCKS);

        ENCHANTMENTS.put("looting", Enchantment.LOOT_BONUS_MOBS);
        ALIASENCHANTMENTS.put("mobloot", Enchantment.LOOT_BONUS_MOBS);
        ALIASENCHANTMENTS.put("loot", Enchantment.LOOT_BONUS_MOBS);
        ALIASENCHANTMENTS.put("lbm", Enchantment.LOOT_BONUS_MOBS);
        ALIASENCHANTMENTS.put("mobslootbonus", Enchantment.LOOT_BONUS_MOBS);

        ENCHANTMENTS.put("respiration", Enchantment.OXYGEN);
        ALIASENCHANTMENTS.put("oxygen", Enchantment.OXYGEN);
        ALIASENCHANTMENTS.put("breathing", Enchantment.OXYGEN);
        ALIASENCHANTMENTS.put("breath", Enchantment.OXYGEN);
        ALIASENCHANTMENTS.put("o", Enchantment.OXYGEN);

        ENCHANTMENTS.put("protection", Enchantment.PROTECTION_ENVIRONMENTAL);
        ALIASENCHANTMENTS.put("prot", Enchantment.PROTECTION_ENVIRONMENTAL);
        ALIASENCHANTMENTS.put("protect", Enchantment.PROTECTION_ENVIRONMENTAL);
        ALIASENCHANTMENTS.put("p", Enchantment.PROTECTION_ENVIRONMENTAL);

        ENCHANTMENTS.put("blastprotect", Enchantment.PROTECTION_EXPLOSIONS);
        ALIASENCHANTMENTS.put("explosionsprotection", Enchantment.PROTECTION_EXPLOSIONS);
        ALIASENCHANTMENTS.put("explosionprotection", Enchantment.PROTECTION_EXPLOSIONS);
        ALIASENCHANTMENTS.put("expprot", Enchantment.PROTECTION_EXPLOSIONS);
        ALIASENCHANTMENTS.put("blastprotection", Enchantment.PROTECTION_EXPLOSIONS);
        ALIASENCHANTMENTS.put("bprotection", Enchantment.PROTECTION_EXPLOSIONS);
        ALIASENCHANTMENTS.put("bprotect", Enchantment.PROTECTION_EXPLOSIONS);
        ALIASENCHANTMENTS.put("pe", Enchantment.PROTECTION_EXPLOSIONS);

        ENCHANTMENTS.put("featherfall", Enchantment.PROTECTION_FALL);
        ALIASENCHANTMENTS.put("fallprotection", Enchantment.PROTECTION_FALL);
        ALIASENCHANTMENTS.put("fallprot", Enchantment.PROTECTION_FALL);
        ALIASENCHANTMENTS.put("featherfalling", Enchantment.PROTECTION_FALL);
        ALIASENCHANTMENTS.put("pfa", Enchantment.PROTECTION_FALL);

        ENCHANTMENTS.put("fireprotect", Enchantment.PROTECTION_FIRE);
        ALIASENCHANTMENTS.put("fireprotection", Enchantment.PROTECTION_FIRE);
        ALIASENCHANTMENTS.put("flameprotection", Enchantment.PROTECTION_FIRE);
        ALIASENCHANTMENTS.put("flameprotect", Enchantment.PROTECTION_FIRE);
        ALIASENCHANTMENTS.put("fireprot", Enchantment.PROTECTION_FIRE);
        ALIASENCHANTMENTS.put("flameprot", Enchantment.PROTECTION_FIRE);
        ALIASENCHANTMENTS.put("pf", Enchantment.PROTECTION_FIRE);

        ENCHANTMENTS.put("projectileprotection", Enchantment.PROTECTION_PROJECTILE);
        ALIASENCHANTMENTS.put("projprot", Enchantment.PROTECTION_PROJECTILE);
        ALIASENCHANTMENTS.put("pp", Enchantment.PROTECTION_PROJECTILE);

        ENCHANTMENTS.put("silktouch", Enchantment.SILK_TOUCH);
        ALIASENCHANTMENTS.put("softtouch", Enchantment.SILK_TOUCH);
        ALIASENCHANTMENTS.put("st", Enchantment.SILK_TOUCH);

        ENCHANTMENTS.put("aquaaffinity", Enchantment.WATER_WORKER);
        ALIASENCHANTMENTS.put("waterworker", Enchantment.WATER_WORKER);
        ALIASENCHANTMENTS.put("watermine", Enchantment.WATER_WORKER);
        ALIASENCHANTMENTS.put("ww", Enchantment.WATER_WORKER);

        ENCHANTMENTS.put("flame", Enchantment.ARROW_FIRE);
        ALIASENCHANTMENTS.put("firearrow", Enchantment.ARROW_FIRE);
        ALIASENCHANTMENTS.put("flamearrow", Enchantment.ARROW_FIRE);
        ALIASENCHANTMENTS.put("af", Enchantment.ARROW_FIRE);

        ENCHANTMENTS.put("power", Enchantment.ARROW_DAMAGE);
        ALIASENCHANTMENTS.put("arrowdamage", Enchantment.ARROW_DAMAGE);
        ALIASENCHANTMENTS.put("arrowpower", Enchantment.ARROW_DAMAGE);
        ALIASENCHANTMENTS.put("ad", Enchantment.ARROW_DAMAGE);

        ENCHANTMENTS.put("punch", Enchantment.ARROW_KNOCKBACK);
        ALIASENCHANTMENTS.put("arrowknockback", Enchantment.ARROW_KNOCKBACK);
        ALIASENCHANTMENTS.put("arrowkb", Enchantment.ARROW_KNOCKBACK);
        ALIASENCHANTMENTS.put("arrowpunch", Enchantment.ARROW_KNOCKBACK);
        ALIASENCHANTMENTS.put("ak", Enchantment.ARROW_KNOCKBACK);

        ENCHANTMENTS.put("infinity", Enchantment.ARROW_INFINITE);
        ALIASENCHANTMENTS.put("infinitearrows", Enchantment.ARROW_INFINITE);
        ALIASENCHANTMENTS.put("infarrows", Enchantment.ARROW_INFINITE);
        ALIASENCHANTMENTS.put("infinite", Enchantment.ARROW_INFINITE);
        ALIASENCHANTMENTS.put("unlimited", Enchantment.ARROW_INFINITE);
        ALIASENCHANTMENTS.put("unlimitedarrows", Enchantment.ARROW_INFINITE);
        ALIASENCHANTMENTS.put("ai", Enchantment.ARROW_INFINITE);

        ENCHANTMENTS.put("luck", Enchantment.LUCK);
        ALIASENCHANTMENTS.put("luckofsea", Enchantment.LUCK);
        ALIASENCHANTMENTS.put("luckofseas", Enchantment.LUCK);
        ALIASENCHANTMENTS.put("rodluck", Enchantment.LUCK);

        ENCHANTMENTS.put("lure", Enchantment.LURE);
        ALIASENCHANTMENTS.put("rodlure", Enchantment.LURE);

        // 1.8
        try {
            final Enchantment depthStrider = Enchantment.getByName("DEPTH_STRIDER");
            if (depthStrider != null) {
                ENCHANTMENTS.put("depthstrider", depthStrider);
                ALIASENCHANTMENTS.put("depth", depthStrider);
                ALIASENCHANTMENTS.put("strider", depthStrider);
            }
        } catch (final IllegalArgumentException ignored) {
        }
    }

    private Enchantments() {
    }

    public static Enchantment getByName(final String name) {
        Enchantment enchantment = null;
        if (enchantment == null) {
            enchantment = Enchantment.getByName(name.toUpperCase());
        }
        if (enchantment == null) {
            enchantment = Enchantment.getByName(name.toLowerCase());
        }
        if (enchantment == null) {
            enchantment = Enchantment.getByName(name);
        }
        if (enchantment == null) {
            enchantment = ENCHANTMENTS.get(name.toLowerCase(Locale.ENGLISH));
        }
        if (enchantment == null) {
            enchantment = ALIASENCHANTMENTS.get(name.toLowerCase(Locale.ENGLISH));
        }
        return enchantment;
    }

    public static Set<Map.Entry<String, Enchantment>> entrySet() {
        return ENCHANTMENTS.entrySet();
    }

    public static Set<String> keySet() {
        return ENCHANTMENTS.keySet();
    }

    public static void registerEnchantment(String name, Enchantment enchantment) {
        if (ENCHANTMENTS.containsKey(name) || ALIASENCHANTMENTS.containsKey(name)) {
            return;
        }
        ENCHANTMENTS.put(name, enchantment);
    }

    public static void registerAlias(String name, Enchantment enchantment) {
        if (ENCHANTMENTS.containsKey(name) || ALIASENCHANTMENTS.containsKey(name) || !ENCHANTMENTS.containsValue(enchantment)) {
            return;
        }
        ALIASENCHANTMENTS.put(name, enchantment);
    }
}