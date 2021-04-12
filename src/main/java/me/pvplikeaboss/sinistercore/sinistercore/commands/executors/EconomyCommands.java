package me.pvplikeaboss.sinistercore.sinistercore.commands.executors;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.CommandContext;
import me.pvplikeaboss.sinistercore.sinistercore.modules.economy.EconomyEntry;
import me.pvplikeaboss.sinistercore.sinistercore.modules.data.files.configs.yml.EconConfig;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Messages;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils.PlayerUtils;
import net.milkbowl.vault.economy.Economy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EconomyCommands {
    private static SinisterCore plugin;
    private Messages utilMsgs;
    private PlayerUtils utilPlayers;
    private Economy ecoImplementer;
    private EconConfig cfgEcon;

    public boolean onCommand(String name, CommandContext context) {
        plugin = context.getPlugin();
        utilMsgs = (Messages) Instances.getInstance(Instances.InstanceType.Utilities, 2);
        utilPlayers = (PlayerUtils) Instances.getInstance(Instances.InstanceType.Utilities, 3);
        ecoImplementer = (Economy) Instances.getInstance(Instances.InstanceType.Economy, -1);
        cfgEcon = (EconConfig) Instances.getInstance(Instances.InstanceType.Config, 3);
        
        List<String> args = context.getArgs();

        PlayerObject sender = null;
        if(context.isPlayer()) {
            sender = plugin.getPlayer(context.getPlayer().getUniqueId());
        }

        if(name.equalsIgnoreCase("baltop")) {
            List<EconomyEntry> entries = new ArrayList<>();
            if (cfgEcon.getConfig().isSet("econ")) {
                for (String sUUID : cfgEcon.getConfig().getConfigurationSection("econ").getKeys(false)) {
                    PlayerObject p = plugin.getPlayer(UUID.fromString(sUUID));
                    if(p == null) {
                        continue;
                    }
                    entries.add(new EconomyEntry(p.playerUUID, p.playerDisplayName, BigDecimal.valueOf(cfgEcon.getConfig().getDouble("econ." + sUUID))));
                }

                entries.sort((entry1, entry2) -> entry2.getBalance().compareTo(entry1.getBalance()));

                int total_pages;
                if(entries.size() <= 10)
                {
                    total_pages = 1;
                } else {
                    double tmp = Double.valueOf(entries.size()/10);
                    if(tmp % 1 != 0) {// we have a remainder so we need another page
                        total_pages = (int) (tmp+1);
                    } else {// whole number of pages
                        total_pages = (int)tmp;
                    }
                }

                if(args.size() == 1) {
                    int page;
                    try {
                        page = Integer.parseInt(args.get(0));
                    } catch(NumberFormatException e) {
                        if(sender != null) {
                            utilMsgs.errorMessage(sender, "&7Invalid page number");
                            return true;
                        }
                        utilMsgs.logErrorMessage("&7Invalid page number");
                        return true;
                    }

                    if(page > total_pages) {
                        if(sender != null) {
                            utilMsgs.errorMessage(sender, "&7Invalid page number (1-"+total_pages+")");
                            return true;
                        }
                        utilMsgs.logErrorMessage("&7Invalid page number (1-"+total_pages+")");
                        return true;
                    }

                    int start = page*10;
                    int end = (page*10)+10;
                    if(end > entries.size()) {
                        end = entries.size();
                    }

                    if(sender != null) {
                        utilMsgs.infoMessage(sender, "&7SinisterCraft Balance Top &8(&6"+page+"&7/&6"+total_pages+"&8)");
                    } else {
                        utilMsgs.logInfoMessage("&7SinisterCraft Balance Top &8(&6"+page+"&7/&6"+total_pages+"&8)");
                    }

                    for(int x = start; x < end; x++) {
                        if(sender != null) {
                            utilMsgs.infoMessage(sender, "&6"+entries.get(x).getPlayerName()+"&7: &6"+entries.get(x).getBalance());
                            continue;
                        }
                        utilMsgs.logInfoMessage("&6"+entries.get(x).getPlayerName()+"&7: &6"+entries.get(x).getBalance());
                        continue;
                    }
                    return true;
                } else {
                    int display;
                    if(entries.size() > 9) {
                        display = 10;
                    } else {
                        display = entries.size();
                    }

                    if(sender != null) {
                        utilMsgs.infoMessage(sender, "&7SinisterCraft Balance Top &8(&61&7/&6"+total_pages+"&8)");
                    } else {
                        utilMsgs.logInfoMessage("&7SinisterCraft Balance Top &8(&61&7/&6"+total_pages+"&8)");
                    }

                    for(int x = 0; x < display; x++) {
                        if(sender != null) {
                            utilMsgs.infoMessage(sender, "&6"+entries.get(x).getPlayerName()+"&7: &6"+entries.get(x).getBalance());
                            continue;
                        }
                        utilMsgs.logInfoMessage("&6"+entries.get(x).getPlayerName()+"&7: &6"+entries.get(x).getBalance());
                        continue;
                    }
                    return true;
                }
            } else {
                if(sender != null) {
                    utilMsgs.errorMessage(sender,"&7Error retrieving player data");
                    return true;
                }
                utilMsgs.logErrorMessage("&7Error retrieving player data");
                return true;
            }
        } else if (name.equalsIgnoreCase("bal") || name.equalsIgnoreCase("balance") || name.equalsIgnoreCase("money")) {
            if (args.size() == 0) {
                if (sender != null) {
                    double balance = 0;

                    balance = ecoImplementer.getBalance(context.getSender().getName());

                    utilMsgs.infoMessage(sender, "&7Your balance is &6$"+balance+"&7!");
                    return true;
                }
                utilMsgs.logErrorMessage("&7Usage: /bal <name>");
                return true;
            } else if(args.size() == 1) {
                String pname = args.get(0);
                String newName = null;
                if((newName = utilPlayers.playerExists(pname)) != null) {
                    double balance = 0;
                    if (utilPlayers.playerOnline(pname) != null) {
                        balance = ecoImplementer.getBalance(newName);
                    } else {
                        balance = ecoImplementer.getBalance(plugin.getServer().getOfflinePlayer(newName));
                    }
                    if (sender != null) {
                        utilMsgs.infoMessage(sender, "&7Player &6" + newName + "'s &7balance is &6$" + balance + "&7!");
                    } else {
                        utilMsgs.logInfoMessage("&7Player &6" + newName + "'s &7balance is &6$" + balance + "&7!");
                    }
                    return true;
                }
                if(sender != null) {
                    utilMsgs.errorMessage(sender, "&7Player Doesn't Exists!");
                } else {
                    utilMsgs.logErrorMessage("&7Player Doesn't Exists!");
                }
                return true;
            }
            if(sender != null) {
                utilMsgs.errorMessage(sender, "&7Usage: /bal");
                utilMsgs.errorMessage(sender, "&7Usage: /bal <name>");
            } else {
                utilMsgs.logErrorMessage("&7Usage: /bal <name>");
            }
            return true;
        } else if(name.equalsIgnoreCase("pay")) {
            if(context.isPlayer()) {
                if (args.size() == 2) {
                    String pname = args.get(0);
                    double amount = 0;

                    try {
                        amount = Double.parseDouble(args.get(1));
                    } catch(NumberFormatException e) {
                        utilMsgs.errorMessage(sender, "&7Invalid amount! Use a number (1050.20)");
                        return true;
                    }

                    String newName = null;
                    if((newName = utilPlayers.playerExists(pname)) != null) {
                        PlayerObject targetPlayer = plugin.getPlayer(newName);
                        if(ecoImplementer.getBalance(sender.playerName) < amount) {// check if player has enough money
                            utilMsgs.errorMessage(sender, "&7You do not have enough funds!");
                            return true;
                        }

                        ecoImplementer.withdrawPlayer(sender.getPlayer(), amount);

                        if (utilPlayers.playerOnline(pname) != null) {
                            ecoImplementer.depositPlayer(targetPlayer.playerName, amount);
                            utilMsgs.infoMessage(targetPlayer, "&7Player &6"+sender.playerName+"&7 has sent you &6$"+amount+"&7!");
                        } else {
                            ecoImplementer.depositPlayer(targetPlayer.getOfflinePlayer(), amount);
                        }

                        utilMsgs.infoMessage(sender, "&7Sent player &6" + newName + " &7money in the amount of &6$" + amount + "&7!");
                        return true;
                    }
                    utilMsgs.errorMessage(sender, "&7Player Doesn't Exists!");
                    return true;
                }
                utilMsgs.errorMessage(sender, "&7Usage: /pay <name> <ammount>");
                return true;
            }
            utilMsgs.logErrorMessage("&aConsole Cant Run This Command!");
            return true;
        } else if(name.equalsIgnoreCase("eco")) {
            if(args.size() > 0) {
                String subcmd = args.get(0);
                if(subcmd.equalsIgnoreCase("deposit")) {
                    if(args.size() == 3) {
                        String playername = args.get(1);
                        double amount;
                        try {
                            amount = Double.parseDouble(args.get(2));
                        } catch(NumberFormatException e) {
                            if(sender != null) {
                                utilMsgs.errorMessage(sender, "&7Invalid amount");
                                utilMsgs.errorMessage(sender, "&7Usage: /eco deposit <name> <amount>");
                            } else {
                                utilMsgs.logErrorMessage("Invalid amount");
                                utilMsgs.logErrorMessage("&7Usage: /eco deposit <name> <amount>");
                            }
                            return true;
                        }

                        String newName;
                        if((newName = utilPlayers.playerExists(playername)) != null) {
                            PlayerObject targetPlayer = plugin.getPlayer(newName);
                            if(utilPlayers.playerOnline(newName) != null) {
                                ecoImplementer.depositPlayer(targetPlayer.playerName, amount);
                            } else {
                                ecoImplementer.depositPlayer(targetPlayer.getOfflinePlayer(), amount);
                            }

                            if(sender != null) {
                                utilMsgs.infoMessage(sender, "&7Deposited &6$"+amount+" &7into &6"+newName+"'s &7account!");
                            } else {
                                utilMsgs.logInfoMessage("&7Deposited &6$"+amount+" &7into &6"+newName+"'s &7account!");
                            }
                            return true;
                        }

                        if(sender != null) {
                            utilMsgs.errorMessage(sender, "&7Player doesn't exist!");
                        } else {
                            utilMsgs.logErrorMessage("Player doesn't exist!");
                        }
                        return true;
                    }
                    if(sender != null) {
                        utilMsgs.errorMessage(sender, "&7Usage: /eco deposit <name> <amount>");
                    } else {
                        utilMsgs.logErrorMessage("&7Usage: /eco deposit <name> <amount>");
                    }
                    return true;
                } else if(subcmd.equalsIgnoreCase("withdraw")) {
                    if(args.size() == 3) {
                        String playername = args.get(1);
                        double amount;
                        try {
                            amount = Double.parseDouble(args.get(2));
                        } catch(NumberFormatException e) {
                            if(sender != null) {
                                utilMsgs.errorMessage(sender, "Invalid amount");
                                utilMsgs.errorMessage(sender, "&7Usage: /eco withdraw <name> <amount>");
                            } else {
                                utilMsgs.logErrorMessage("Invalid amount");
                                utilMsgs.logErrorMessage("&7Usage: /eco withdraw <name> <amount>");
                            }
                            return true;
                        }
                        String newName;
                        if((newName = utilPlayers.playerExists(playername)) != null) {
                            PlayerObject targetPlayer = plugin.getPlayer(newName);
                            if(utilPlayers.playerOnline(newName) != null) {
                                ecoImplementer.withdrawPlayer(targetPlayer.playerName, amount);
                            } else {
                                ecoImplementer.withdrawPlayer(targetPlayer.getOfflinePlayer(), amount);
                            }

                            if(sender != null) {
                                utilMsgs.infoMessage(sender, "&7Withdrawn &6$"+amount+" &7from &6"+newName+"'s &7account!");
                            } else {
                                utilMsgs.logInfoMessage("&7Withdrawn &6$"+amount+" &7from &6"+newName+"'s &7account!");
                            }
                            return true;
                        }
                        if(sender != null) {
                            utilMsgs.errorMessage(sender, "&7Player doesn't exist!");
                        } else {
                            utilMsgs.logErrorMessage("Player doesn't exist!");
                        }
                        return true;
                    }
                    if(sender != null) {
                        utilMsgs.errorMessage(sender, "&7Usage: /eco withdraw <name> <amount>");
                    } else {
                        utilMsgs.logErrorMessage("&7Usage: /eco withdraw <name> <amount>");
                    }
                    return true;
                } else if(subcmd.equalsIgnoreCase("set")) {
                    if(args.size() == 3) {
                        String playername = args.get(1);
                        double amount;
                        try {
                            amount = Double.parseDouble(args.get(2));
                        } catch(NumberFormatException e) {
                            if(sender != null) {
                                utilMsgs.errorMessage(sender, "&7Invalid amount");
                                utilMsgs.errorMessage(sender, "&7Usage: /eco set <name> <amount>");
                            } else {
                                utilMsgs.logErrorMessage("&7Invalid amount");
                                utilMsgs.logErrorMessage("&7Usage: /eco set <name> <amount>");
                            }
                            return true;
                        }
                        String newName;
                        if((newName = utilPlayers.playerExists(playername)) != null) {
                            PlayerObject targetPlayer = plugin.getPlayer(newName);
                            if(utilPlayers.playerOnline(newName) != null) {
                                double balance = ecoImplementer.getBalance(targetPlayer.playerName);
                                ecoImplementer.withdrawPlayer(targetPlayer.playerName, balance);
                                ecoImplementer.depositPlayer(targetPlayer.playerName, amount);
                            } else {
                                double balance = ecoImplementer.getBalance(targetPlayer.getOfflinePlayer());
                                ecoImplementer.withdrawPlayer(targetPlayer.getOfflinePlayer(), balance);
                                ecoImplementer.depositPlayer(targetPlayer.getOfflinePlayer(), amount);
                            }

                            if(sender != null) {
                                utilMsgs.infoMessage(sender, "&7Set &6$"+amount+" &7as &6"+newName+"'s &7balance!");
                            } else {
                                utilMsgs.logInfoMessage("&7Set &6$"+amount+" &7as &6"+newName+"'s &7balance!");
                            }
                            return true;
                        }
                        if(sender != null) {
                            utilMsgs.errorMessage(sender, "&7Player doesn't exist!");
                        } else {
                            utilMsgs.logErrorMessage("&7Player doesn't exist!");
                        }
                        return true;
                    }
                    if(sender != null) {
                        utilMsgs.errorMessage(sender, "&7Usage: /eco set <name> <amount>");
                    } else {
                        utilMsgs.logErrorMessage("&7Usage: /eco set <name> <amount>");
                    }
                    return true;
                }
                // fall through to below
            }
            if(sender != null) {
                utilMsgs.errorMessage(sender, "&7Usage: /eco set <name> <amount>");
                utilMsgs.errorMessage(sender, "&7Usage: /eco withdraw <name> <amount>");
                utilMsgs.errorMessage(sender, "&7Usage: /eco deposit <name> <amount>");
            } else {
                utilMsgs.logErrorMessage("&7Usage: /eco set <name> <amount>");
                utilMsgs.logErrorMessage("&7Usage: /eco withdraw <name> <amount>");
                utilMsgs.logErrorMessage("&7Usage: /eco deposit <name> <amount>");
            }
            return true;
        } else {
            return false;
        }
    }
}
