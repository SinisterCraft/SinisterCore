package me.pvplikeaboss.sinistercore.sinistercore.modules.economy;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.files.configs.yml.EconConfig;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils.PlayerUtils;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public class EconomyImplementer extends AbstractEconomy {
    private SinisterCore plugin;
    private EconomyData econData;
    private PlayerUtils playerUtils;
    public EconomyImplementer(SinisterCore p) {
        this.plugin = p;
        this.econData = new EconomyData(this.plugin);
        this.playerUtils = (PlayerUtils) Instances.getInstance(Instances.InstanceType.Utilities, 3);
    }

    public boolean isEnabled() {
        return true;
    }

    
    public String getName() {
        return "SinisterEcon";
    }

    
    public boolean hasBankSupport() {
        return false;
    }

    
    public int fractionalDigits() {
        return 2;
    }


    public String format(double v) {
        return "$"+v;
    }

    
    public String currencyNamePlural() {
        return "Dollars";
    }

    
    public String currencyNameSingular() {
        return "Dollar";
    }

    
    public boolean hasAccount(String s) {
        return false;
    }

    
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    
    public boolean hasAccount(String s, String s1) {
        return false;
    }

    
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }

    
    public double getBalance(String s) {
        String pName = playerUtils.playerExists(s);
        if(pName != null) {
            if(plugin.getServer().getPlayer(pName) == null) {
                return getBalance(plugin.getServer().getOfflinePlayer(pName));
            } else {
                UUID playerUUID = plugin.getServer().getPlayer(pName).getUniqueId();
                return econData.getBalance(playerUUID);
            }
        }
        return -1;
    }

    
    public double getBalance(OfflinePlayer offlinePlayer) {
        return econData.getBalance(offlinePlayer.getUniqueId());
    }

    
    public double getBalance(String s, String s1) {
        return 0;
    }

    
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        return 0;
    }

    
    public boolean has(String s, double v) {
        return false;
    }

    
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        return false;
    }

    
    public boolean has(String s, String s1, double v) {
        return false;
    }

    
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return false;
    }

    
    public EconomyResponse withdrawPlayer(String s, double v) {
        if(v >= 0) {
            String pName = playerUtils.playerExists(s);
            if(pName != null) {
                if(plugin.getServer().getPlayer(pName) == null) {
                    withdrawPlayer(plugin.getServer().getOfflinePlayer(pName), v);
                    return new EconomyResponse(v, getBalance(s), EconomyResponse.ResponseType.SUCCESS, "Success(offline)");
                }
                double balance = getBalance(s);
                if(balance >= v) {
                    econData.setBalance(plugin.getServer().getPlayer(pName).getUniqueId(), balance-v);
                    return new EconomyResponse(v, getBalance(s), EconomyResponse.ResponseType.SUCCESS, "Success");
                }
                return new EconomyResponse(v, balance, EconomyResponse.ResponseType.FAILURE, "Player does not have enough funds");
            }
            return new EconomyResponse(v, -1, EconomyResponse.ResponseType.FAILURE, "Player not exists?");
        }
        return new EconomyResponse(v, getBalance(s), EconomyResponse.ResponseType.FAILURE, "Withdrawing Negative Amounts");
    }

    
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        if(v >= 0) {
            if (offlinePlayer != null) {
                double balance = getBalance(offlinePlayer);
                if(balance >= v) {
                    econData.setBalance(offlinePlayer.getUniqueId(), balance-v);
                    return new EconomyResponse(v, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, "Success");
                }
                return new EconomyResponse(v, getBalance(offlinePlayer), EconomyResponse.ResponseType.FAILURE, "Player does not have enough funds");
            }
            return new EconomyResponse(v, -1, EconomyResponse.ResponseType.FAILURE, "offlinePlayer is null?");
        }
        return new EconomyResponse(v, getBalance(offlinePlayer), EconomyResponse.ResponseType.FAILURE, "Withdrawing Negative Amounts");
    }

    
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return null;
    }

    
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return null;
    }

    
    public EconomyResponse depositPlayer(String s, double v) {
        if(v >= 0) {
            String pName = playerUtils.playerExists(s);
            if(pName != null) {
                if(plugin.getServer().getPlayer(pName) == null) {
                    depositPlayer(plugin.getServer().getOfflinePlayer(pName), v);
                    return new EconomyResponse(v, getBalance(s), EconomyResponse.ResponseType.SUCCESS, "Success(offline)");
                }
                double balance = getBalance(s);
                econData.setBalance(plugin.getServer().getPlayer(pName).getUniqueId(), balance + v);
                return new EconomyResponse(v, getBalance(s), EconomyResponse.ResponseType.SUCCESS, "Success");
            }
            return new EconomyResponse(v, -1, EconomyResponse.ResponseType.FAILURE, "Player not exists");
        }
        return new EconomyResponse(v, getBalance(s), EconomyResponse.ResponseType.FAILURE, "Depositing Negative Amounts");
    }

    
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        if(v >= 0) {
            if (offlinePlayer != null) {
                double balance = getBalance(offlinePlayer);
                econData.setBalance(offlinePlayer.getUniqueId(), balance+v);
                return new EconomyResponse(v, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, "Success");
            }
            return new EconomyResponse(v, -1, EconomyResponse.ResponseType.FAILURE, "offlinePlayer is null?");
        }
        return new EconomyResponse(v, getBalance(offlinePlayer), EconomyResponse.ResponseType.FAILURE, "Depositing Negative Amounts");
    }

    
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        return null;
    }

    
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return null;
    }

    
    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    
    public EconomyResponse deleteBank(String s) {
        return null;
    }

    
    public EconomyResponse bankBalance(String s) {
        return null;
    }

    
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    
    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    
    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    
    public List<String> getBanks() {
        return null;
    }

    
    public boolean createPlayerAccount(String s) {
        return false;
    }

    
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    
    public boolean createPlayerAccount(String s, String s1) {
        return false;
    }

    
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }
}
