package me.pvplikeaboss.sinistercore.sinistercore.modules.API.vault;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Messages;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;

public class VaultAPI {

    private static SinisterCore plugin = null;
    private static EconomyImplementer ecoImplementer = null;
    private static Plugin vaultPlugin = null;
    private static Economy economy = null;
    private static Messages utilMsgs = null;

    public static void load(SinisterCore p) {
        if(plugin == null) {
            plugin = p;
        }

        if(ecoImplementer == null) {
            ecoImplementer = (EconomyImplementer) Instances.getInstance(Instances.InstanceType.Economy, -1);
        }

        if(utilMsgs == null) {
            utilMsgs = (Messages) Instances.getInstance(Instances.InstanceType.Utilities, 2);
        }

        if(vaultPlugin == null) {
            if ((vaultPlugin = plugin.getServer().getPluginManager().getPlugin("Vault")) == null) {
                utilMsgs.logErrorMessage("Vault not found");
                return;
            }
        }

        if(economy == null) {
            if (!hookEconomy()) {
                utilMsgs.logErrorMessage("Vault not found");
            }
        }
    }

    public static void unload() {
        unhookEconomy();
    }

    private static boolean hookEconomy() {
        plugin.getServer().getServicesManager().register(Economy.class, new EconomyImplementer(plugin), (Plugin)plugin, ServicePriority.Highest);
        economy = (Economy)new EconomyImplementer(plugin);
        return true;
    }

    private static void unhookEconomy() {
        plugin.getServer().getServicesManager().unregister(net.milkbowl.vault.economy.Economy.class, economy);
    }

    public static Economy getEconomy() {
        return economy;
    }
}
