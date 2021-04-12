package me.pvplikeaboss.sinistercore.sinistercore.objects;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.files.configs.yml.KitConfig;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Cooldown;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Items;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Messages;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KitObject {
    private final SinisterCore plugin;
    private final KitConfig cfgKits;
    private final Cooldown utilCooldown;
    private final Messages utilMsgs;
    public String kitName;
    public List<ItemStack> items;
    public int itemsLen;
    public int delay;

    public KitObject(SinisterCore p, String kN) {
        this.plugin = p;
        this.cfgKits = (KitConfig) Instances.getInstance(Instances.InstanceType.Config, 1);
        this.utilCooldown = (Cooldown) Instances.getInstance(Instances.InstanceType.Utilities, 1);
        this.utilMsgs = (Messages) Instances.getInstance(Instances.InstanceType.Utilities, 2);
        this.kitName = kN;
        this.items = new ArrayList<ItemStack>();
    }

    private void getKit() {
        this.delay = this.cfgKits.getConfig().getInt("kits."+this.kitName+".delay");
        this.itemsLen = 0;
        for(String listID : this.cfgKits.getConfig().getConfigurationSection("kits." +this.kitName+".items").getKeys(false)) {
            String name = null;
            if(this.cfgKits.getConfig().isSet("kits." + this.kitName +".items." + listID + ".name")) {
                name = this.cfgKits.getConfig().getString("kits." + this.kitName + ".items." + listID + ".name");
            }
            int amount = this.cfgKits.getConfig().getInt("kits." + this.kitName +".items." + listID + ".amount");
            Material material = Material.valueOf(this.cfgKits.getConfig().getString("kits." + this.kitName + ".items." + listID + ".material"));
            ArrayList<String> enchantments = null;
            if(this.cfgKits.getConfig().isSet("kits."+this.kitName+".items."+listID+".enchantments")) {
                enchantments = (ArrayList<String>) cfgKits.getConfig().getList("kits." + this.kitName + ".items." + listID + ".enchantments");  //Enchantment name
            }
            this.items.add(Items.createItemStack(name, amount, material, enchantments));
            this.itemsLen++;
        }
    }

    private void setKit(Inventory inv, int delay) {
        if(this.kitExists()) {// less just delete the kit
            this.cfgKits.getConfig().set("kits."+this.kitName, null);
        }
        int count = 0;
        this.cfgKits.getConfig().set("kits." + this.kitName + ".delay", delay);
        for(ItemStack item : inv.getContents()) {
            if(item == null || item.getType() == Material.AIR) {
                continue;
            }
            String name = item.getItemMeta().getDisplayName();
            int amount = item.getAmount();
            Material material = item.getType();
            ArrayList<String> enchants = new ArrayList<String>();
            if(item.getItemMeta().hasEnchants()) {
                for (Enchantment enchantment : item.getEnchantments().keySet()) {
                    String s = enchantment.getName();
                    s = s + ":";
                    s = s + item.getEnchantments().get(enchantment);
                    enchants.add(s);
                }
            }
            if(name != null) {
                this.cfgKits.getConfig().set("kits." + this.kitName + ".items." + count + ".name", name);
            }
            this.cfgKits.getConfig().set("kits." + this.kitName + ".items." + count + ".amount", amount);
            this.cfgKits.getConfig().set("kits." + this.kitName + ".items." + count + ".material", material.toString());
            if(item.getItemMeta().hasEnchants()) {
                this.cfgKits.getConfig().set("kits." + this.kitName + ".items." + count + ".enchantments", enchants);
            }
            count++;
        }
        this.cfgKits.saveConfig();
    }

    public boolean giveKit(PlayerObject p, boolean force) {
        this.getKit();

        if(!force) {
            if(p.getPlayer().hasPermission("sinistercore.kit."+this.kitName.toLowerCase()) == false) {
                return false;
            }
        }

        Inventory inv = p.getPlayer().getInventory();
        for (ItemStack item : this.items) {
            inv.addItem(item);
        }
        return true;
    }

    public boolean createKit(PlayerObject p, int delay) {
        Inventory inv = p.getPlayer().getInventory();
        this.setKit(inv, delay);
        return true;
    }

    public boolean deleteKit() {
        if(this.kitExists()) {// less just delete the kit
            this.cfgKits.getConfig().set("kits."+this.kitName, null);
            return true;
        } else {
            return false;
        }
    }

    public boolean kitExists() {
        if(this.cfgKits.getConfig().isSet("kits")) {
            for (String kit : this.cfgKits.getConfig().getConfigurationSection("kits").getKeys(false)) {
                if (kit.equalsIgnoreCase(this.kitName)) {
                    this.kitName = kit;
                    return true;
                }
            }
        }
        return false;
    }

}
