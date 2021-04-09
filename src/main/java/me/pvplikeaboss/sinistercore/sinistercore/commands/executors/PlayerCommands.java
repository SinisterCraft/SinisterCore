package me.pvplikeaboss.sinistercore.sinistercore.commands.executors;

import me.pvplikeaboss.sinistercore.sinistercore.Instances;
import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;
import me.pvplikeaboss.sinistercore.sinistercore.commands.util.CommandContext;
import me.pvplikeaboss.sinistercore.sinistercore.modules.files.configs.csv.items.methods.AbstractItemDB;
import me.pvplikeaboss.sinistercore.sinistercore.modules.files.configs.csv.items.methods.ItemDB;
import me.pvplikeaboss.sinistercore.sinistercore.objects.PlayerObject;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Enchantments;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.FakeAnvil;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.misc.Messages;
import me.pvplikeaboss.sinistercore.sinistercore.utilites.serverutils.PlayerUtils;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class PlayerCommands {
    private static SinisterCore plugin;
    private Messages utilMsgs;
    private PlayerUtils utilPlayers;
    private AbstractItemDB cfgItemDB;

    public boolean onCommand(String name, CommandContext context) {
        plugin = context.getPlugin();
        utilMsgs = (Messages) Instances.getInstance(Instances.InstanceType.Utilities, 2);
        utilPlayers = (PlayerUtils) Instances.getInstance(Instances.InstanceType.Utilities, 3);
        cfgItemDB = (AbstractItemDB) Instances.getInstance(Instances.InstanceType.Config, 4);

        PlayerObject sender = null;
        if(context.isPlayer()) {
            sender = plugin.getPlayer(context.getPlayer().getUniqueId());
        }

        if(name.equalsIgnoreCase("hat")) {
            if(sender != null) {
                ItemStack itemStack = sender.getPlayer().getItemInHand();
                if(itemStack == null || itemStack.getType() == Material.AIR) {
                    utilMsgs.errorMessage(sender, "&7Nothing in hand");
                    return true;
                }
                sender.getPlayer().getInventory().setHelmet(itemStack);
                utilMsgs.infoMessage(sender, "&7Added hat sucessfully");
                return true;
            }
            utilMsgs.logErrorMessage("only player can execute this command");
            return true;
        } else if(name.equalsIgnoreCase("more")) {
            if(sender != null) {
                ItemStack itemStack = sender.getPlayer().getItemInHand();
                if(itemStack == null || itemStack.getType() == Material.AIR) {
                    utilMsgs.errorMessage(sender, "&7Nothing in hand");
                    return true;
                }
                if(itemStack.getAmount() == itemStack.getMaxStackSize()) {
                    utilMsgs.errorMessage(sender, "&7Already max stack size!");
                    return true;
                }
                itemStack.setAmount(itemStack.getMaxStackSize());
                utilMsgs.infoMessage(sender, "&7Set max stack");
                return true;
            }
            utilMsgs.logErrorMessage("only player can execute this command");
            return true;
        } else if(name.equalsIgnoreCase("pweather")) {
            if(sender != null) {
                if(context.getArgs().size() == 1) {
                    String weatherType = context.argAt(0);
                    if(weatherType.equalsIgnoreCase("clear") || weatherType.equalsIgnoreCase("none") || weatherType.equalsIgnoreCase("off")) {
                        sender.getPlayer().setPlayerWeather(WeatherType.CLEAR);
                        utilMsgs.infoMessage(sender, "&7Turning off weather on local player");
                        return true;
                    } else if(weatherType.equalsIgnoreCase("rain") || weatherType.equalsIgnoreCase("on")) {
                        sender.getPlayer().setPlayerWeather(WeatherType.DOWNFALL);
                        utilMsgs.infoMessage(sender, "&7Turning on downfall on local player");
                        return true;
                    }// else {// no need can just fallback to usage prints
                }
                utilMsgs.infoMessage(sender, "&7Usage: &7/pweather clear");
                utilMsgs.infoMessage(sender, "&7Usage: &7/pweather rain");
                return true;
            }
            utilMsgs.logErrorMessage("only player can execute this command");
            return true;
        } else if(name.equalsIgnoreCase("ptime")) {
            if(sender != null) {
                if(context.getArgs().size() == 1) {
                    String weatherType = context.argAt(0);
                    if(weatherType.equalsIgnoreCase("day")) {
                        sender.getPlayer().setPlayerTime(0, false);
                        utilMsgs.infoMessage(sender, "&7Set time day on local player");
                        return true;
                    } else if(weatherType.equalsIgnoreCase("night")) {
                        sender.getPlayer().setPlayerTime(14000, false);
                        utilMsgs.infoMessage(sender, "&7Set time night on local player");
                        return true;
                    }// else {// no need can just fallback to usage prints
                }
                utilMsgs.infoMessage(sender, "&7Usage: &7/ptime day");
                utilMsgs.infoMessage(sender, "&7Usage: &7/ptime night");
                return true;
            }
            utilMsgs.logErrorMessage("only player can execute this command");
            return true;
        } else if(name.equalsIgnoreCase("invsee")) {
            if(sender != null) {
                if(context.getArgs().size() == 1) {
                    String targetPlayerName = null;
                    PlayerObject targetPlayer = null;
                    if((targetPlayerName = utilPlayers.playerExists(context.argAt(0))) == null) {
                        utilMsgs.errorMessage(sender, "&7Player not exists!");
                        return true;
                    }

                    targetPlayer = plugin.getPlayer(targetPlayerName);

                    if(!targetPlayer.isPlayerOnline) {
                        utilMsgs.errorMessage(sender, "&7Player is offline!");
                        return true;
                    }

                    Inventory targetPlayerInv = targetPlayer.getPlayer().getInventory();
                    sender.getPlayer().openInventory(targetPlayerInv);
                    utilMsgs.infoMessage(sender, "&7Opening player's inventory");
                    return true;
                }
                utilMsgs.infoMessage(sender, "&7Usage: &7/invsee <player>");
                return true;
            }
            utilMsgs.logErrorMessage("only player can execute this command");
            return true;
        } else if(name.equalsIgnoreCase("enchant")) {
            if(sender != null) {
                if(context.getArgs().size() == 2) {
                    String enchantmentStr = context.argAt(0);
                    int enchantmentLevel = -1;

                    try {
                        enchantmentLevel = Integer.parseInt(context.argAt(1));
                    } catch(NumberFormatException e) {
                        utilMsgs.errorMessage(sender, "&7Invalid enchantment level! (parse error!)");
                        return true;
                    }

                    if(enchantmentLevel > 50) {
                        utilMsgs.errorMessage(sender, "&7Invalid enchantment level! (too high!)");
                        return true;
                    }

                    Enchantment enchantment = Enchantments.getByName(enchantmentStr);
                    if(enchantment == null) {
                        utilMsgs.errorMessage(sender, "&7Invalid enchantment");
                        StringBuilder enchantmentListBuilder = new StringBuilder();
                        enchantmentListBuilder.append("&7List of available enchantments: ");
                        int enchantmentListSize = Enchantments.keySet().size();
                        int x = 0;
                        for(String ench : Enchantments.keySet()) {
                            enchantmentListBuilder.append("&6"+ench);
                            if(x < enchantmentListSize-1) {
                                enchantmentListBuilder.append("&7, ");
                            }
                            x++;
                        }
                        utilMsgs.infoMessage(sender, enchantmentListBuilder.toString());
                        return true;
                    }
                    ItemStack item = sender.getPlayer().getItemInHand();
                    if(item == null || item.getType() == Material.AIR) {
                        utilMsgs.errorMessage(sender, "&7Nothing in hand");
                        return true;
                    }
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.addEnchant(enchantment, enchantmentLevel, true);
                    item.setItemMeta(itemMeta);
                    utilMsgs.infoMessage(sender, "&7Successfully added enchantment &6"+enchantmentStr+" &7to current item");
                    return true;
                }
                utilMsgs.infoMessage(sender, "&7Usage: &7/enchant <enchantment name> <enchantment level>");
                utilMsgs.infoMessage(sender, "&7Usage: &7/enchant sharp 5");
                return true;
            }
            utilMsgs.logErrorMessage("only player can execute this command");
            return true;
        } else if(name.equalsIgnoreCase("workbench") || name.equalsIgnoreCase("wb")) {
            if(sender != null) {
                sender.getPlayer().openWorkbench(null, true);
                utilMsgs.infoMessage(sender, "&7Opening Workbench GUI");
                return true;
            }
            utilMsgs.logErrorMessage("only player can execute this command");
            return true;
        } else if(name.equalsIgnoreCase("anvil")) {
            if(sender != null) {
                EntityPlayer entityPlayer = sender.getCraftPlayer().getHandle();
                FakeAnvil fakeAnvil = new FakeAnvil(entityPlayer);
                int containerId = entityPlayer.nextContainerCounter();
                entityPlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerId, "minecraft:anvil", new ChatMessage("Repairing", new Object[]{}), 0));
                entityPlayer.activeContainer = fakeAnvil;
                entityPlayer.activeContainer.windowId = containerId;
                entityPlayer.activeContainer.addSlotListener(entityPlayer);
                entityPlayer.activeContainer = fakeAnvil;
                entityPlayer.activeContainer.windowId = containerId;
                Inventory inv = fakeAnvil.getBukkitView().getTopInventory();
                utilMsgs.infoMessage(sender, "&7Opening Anvil GUI");
                return true;
            }
            utilMsgs.logErrorMessage("only player can execute this command");
            return true;
        } else if(name.equalsIgnoreCase("give") || name.equalsIgnoreCase("i")) {
            boolean hasOtherPerm = false;

            if(sender != null && context.getArgs().size() == 2) {// /give item amount
                ItemStack itemStack = null;
                int amount = 0;

                try {
                    itemStack = cfgItemDB.get(context.argAt(0));
                } catch (Exception e) {
                    utilMsgs.errorMessage(sender, "&7Invalid item name provided");
                    return true;
                }

                try {
                    amount = Integer.parseInt(context.argAt(1));
                } catch (NumberFormatException e) {
                    utilMsgs.errorMessage(sender, "&7Invalid item amount provided");
                    return true;
                }

                itemStack.setAmount(amount);

                sender.getPlayer().getInventory().addItem(itemStack);
                sender.getPlayer().updateInventory();

                utilMsgs.infoMessage(sender, "&7Given &6x" + amount + "&7 of item &6" + context.argAt(0) + "&7!");

                return true;
            } else if(context.getArgs().size() == 3) {// /give player item amount
                String targetName = context.argAt(0);
                ItemStack itemStack = null;
                int amount = 0;

                try {
                    itemStack = cfgItemDB.get(context.argAt(1));
                } catch (Exception e) {
                    if(sender != null) {
                        utilMsgs.errorMessage(sender, "&7Invalid item name provided");
                        return true;
                    }
                    utilMsgs.logErrorMessage("Invalid item name provided");
                    return true;
                }

                try {
                    amount = Integer.parseInt(context.argAt(2));
                } catch (NumberFormatException e) {
                    if(sender != null) {
                        utilMsgs.errorMessage(sender, "&7Invalid item amount provided");
                        return true;
                    }
                    utilMsgs.logErrorMessage("Invalid item amount provided");
                    return true;
                }

                itemStack.setAmount(amount);

                String newName = null;
                if ((newName = utilPlayers.playerExists(targetName)) == null) {
                    if(sender != null) {
                        utilMsgs.errorMessage(sender, "&7Player not exists!");
                        return true;
                    }
                    utilMsgs.logErrorMessage("&7Player not exists!");
                    return true;
                }

                PlayerObject targetPlayer = plugin.getPlayer(newName);

                if (targetPlayer.isPlayerOnline) {
                    targetPlayer.getPlayer().getInventory().addItem(itemStack);
                    utilMsgs.infoMessage(targetPlayer, "&7Given &6x" + amount + "&7 of item &6" + context.argAt(1) + "&7!");
                    if(sender != null) {
                        utilMsgs.infoMessage(sender, "&7Given &6x" + amount + "&7 of item &6" + context.argAt(1) + "&7 to player &6" + targetPlayer.playerName + "&7!");
                        return true;
                    }
                    utilMsgs.logErrorMessage("Given x" + amount + " of item " + context.argAt(1) + " to player " + targetPlayer.playerName + "!");
                    return true;
                }
                if(sender != null) {
                    utilMsgs.errorMessage(sender, "&7Player is offline!");
                    return true;
                }
                utilMsgs.logErrorMessage("Player is offline!");
                return true;
            }

            if(sender != null) {
                utilMsgs.infoMessage(sender, "&7Usage: /i <item> <amount>");
                utilMsgs.infoMessage(sender, "&7Usage: /i <player> <item> <amount>");
                return true;
            }
            utilMsgs.logErrorMessage("Usage: /i <player> <item> <amount>");
            return true;
        } else if(name.equalsIgnoreCase("enderchest") || name.equalsIgnoreCase("ec")) {
            if(sender != null) {
                sender.getPlayer().openInventory(sender.getCraftPlayer().getEnderChest());
                utilMsgs.infoMessage(sender, "&7Opening Enderchest GUI");
            }
            utilMsgs.logErrorMessage("only player can execute this command");
            return true;
        } else if(name.equalsIgnoreCase("sudo")) {
            if(context.getArgs().size() >= 2) {
                PlayerObject targPlayer = null;
                String targPlayerName = null;

                if((targPlayerName = utilPlayers.playerExists(context.argAt(0))) == null) {
                    if (sender != null) {
                        utilMsgs.errorMessage(sender, "&7Player not exists!");
                        return true;
                    }
                    utilMsgs.logErrorMessage("&7Player not exists!");
                    return true;
                }

                if(utilPlayers.playerOnline(targPlayerName) == null) {
                    if(sender != null) {
                        utilMsgs.errorMessage(sender, "&7Player is offline!");
                        return true;
                    }
                    utilMsgs.logErrorMessage("&7Player is offline!");
                    return true;
                }

                targPlayer = plugin.getPlayer(targPlayerName);

                StringBuilder commandBuilder = new StringBuilder();
                int x;
                for(x = 1; x < context.getArgs().size(); x++) {
                    commandBuilder.append(context.argAt(x));
                    if(x == context.getArgs().size()-1) {
                        continue;
                    }
                    commandBuilder.append(" ");
                }

                targPlayer.getPlayer().performCommand(commandBuilder.toString());
                if(sender != null) {
                    utilMsgs.infoMessage(sender, "&7Forced user &6" + targPlayerName + " &7to run command &6" + commandBuilder.toString() + "&7!");
                    return true;
                }
                utilMsgs.logInfoMessage("Forced user " + targPlayerName + " to run command " + commandBuilder.toString() + "!");
                return true;
            }
            if(sender != null) {
                utilMsgs.infoMessage(sender, "&7Usage: /sudo <player> <command>");
                return true;
            }
            utilMsgs.logErrorMessage("Usage: /sudo <player> <command>");
            return true;
        } else if(name.equalsIgnoreCase("vanish") || name.equalsIgnoreCase("v")) {
            if(sender != null) {
                if(context.getArgs().size() == 0) {
                    if(sender.getIsVanish()) {
                        sender.setIsVanish(false);
                        utilMsgs.infoMessage(sender, "&7Vanish mode disabled!");
                        return true;
                    }
                    sender.setIsVanish(true);
                    utilMsgs.infoMessage(sender, "&7Vanish mode enabled!");
                    return true;
                } else if((context.getArgs().size() == 1) && (sender.getPlayer().hasPermission("sinistercore.vanish.other"))) {
                    String targetPlayerName = null;
                    if((targetPlayerName = utilPlayers.playerExists(context.argAt(0))) == null) {
                        utilMsgs.errorMessage(sender, "&7Player not exists!");
                        return true;
                    }

                    if(utilPlayers.playerOnline(targetPlayerName) == null) {
                        utilMsgs.errorMessage(sender, "&7Player is offline!");
                        return true;
                    }

                    PlayerObject targetPlayer = plugin.getPlayer(targetPlayerName);
                    if(targetPlayer.getIsVanish()) {
                        targetPlayer.setIsVanish(false);
                        utilMsgs.infoMessage(sender, "&7Vanish mode disabled!");
                        utilMsgs.infoMessage(targetPlayer, "&7Vanish mode disabled!");
                        return true;
                    }
                    targetPlayer.setIsVanish(true);
                    utilMsgs.infoMessage(sender, "&7Vanish mode enabled!");
                    utilMsgs.infoMessage(targetPlayer, "&7Vanish mode enabled!");
                    return true;
                }
                utilMsgs.infoMessage(sender, "&7Usage: vanish");
                return true;
            }
            utilMsgs.logErrorMessage("only player can execute this command");
            return true;
        } else if(name.equalsIgnoreCase("god") || name.equalsIgnoreCase("godmode")) {
            if(context.getArgs().size() == 0 && sender != null) {// self cmd; player instance

                //toggle off
                if(sender.getIsGodMode()) {
                    sender.setIsGodMode(false);
                    utilMsgs.infoMessage(sender, "&7GodMode disabled!");
                    return true;
                }
                //toggle on
                sender.setIsGodMode(true);
                utilMsgs.infoMessage(sender, "&7GodMode enabled!");
                return true;

            } else if(context.getArgs().size() == 0) {// self cmd; console instance

                //console cant have godmode as it is not a player
                utilMsgs.logInfoMessage("Console isn't aloud to heal its self!");
                utilMsgs.logInfoMessage("Usage: /godmode <player>");
                return true;

            } else if (
                    (
                            context.getArgs().size() == 1
                    ) && (
                            (context.isConsole()) ||
                                    (context.isPlayer() && sender.getPlayer().hasPermission("sinistercore.godmode.other"))
                    )
            ) {

                PlayerObject targetPlayer = null;// must check username
                String targetPlayerName;

                if((targetPlayerName = utilPlayers.playerExists(context.argAt(0))) == null) {// determine if we can retrieve playerObject
                    if(sender != null) {
                        utilMsgs.errorMessage(sender, "&7Player not exists!");
                        return true;
                    }
                    utilMsgs.logErrorMessage("Player not exists!");
                    return true;
                }

                if((targetPlayer = plugin.getPlayer(targetPlayerName)) == null) {// retrieve target PlayerObject from plugin
                    if(sender != null) {
                        utilMsgs.errorMessage(sender, "&7PlayerObject is null!");
                        return true;
                    }
                    utilMsgs.logErrorMessage("PlayerObject is null!");
                    return true;
                }

                if(targetPlayer.getIsPlayerOnline() == false) {
                    if(sender != null) {
                        utilMsgs.errorMessage(sender, "&7Player is offline!");
                        return true;
                    }
                    utilMsgs.logErrorMessage("Player is offline!");
                    return true;
                }

                //toggle off
                if(targetPlayer.getIsGodMode()) {
                    targetPlayer.setIsGodMode(false);
                    utilMsgs.infoMessage(targetPlayer, "&7GodMode disabled!");
                    if(sender != null) {
                        utilMsgs.infoMessage(sender, "&7GodMode disabled on user &6" + targetPlayer.playerName + "&7!");
                        return true;
                    }
                    utilMsgs.logInfoMessage("Godmode disabled on user "+targetPlayerName+"!");
                    return true;
                }
                //toggle on
                targetPlayer.setIsGodMode(true);
                utilMsgs.infoMessage(targetPlayer, "&7GodMode enabled!");
                if(sender != null) {
                    utilMsgs.infoMessage(sender, "&7GodMode disabled on user &6" + targetPlayer.playerName + "&7!");
                    return true;
                }
                utilMsgs.logInfoMessage("Godmode disabled on user "+targetPlayerName+"!");
                return true;

            }

            if(sender != null) {
                utilMsgs.infoMessage(sender, "&7Usage: /godmode");
                if(sender.getPlayer().hasPermission("sinistercore.godmode.other")) {
                    utilMsgs.infoMessage(sender, "&7Usage: /godmode <player>");
                }
                return true;
            }
            utilMsgs.logInfoMessage("Usage: /godmode <player>");
            return true;
        } else if(name.equalsIgnoreCase("clearinventory") || name.equalsIgnoreCase("clear") || name.equalsIgnoreCase("ci")) {
            boolean hasPermOther = false;

            if(sender == null || sender.getPlayer().hasPermission("sinistercore.clearinventory.other")) {
                hasPermOther = true;
            }

            if(context.getArgs().size() == 0) {
                if(sender != null) {
                    sender.getPlayer().getInventory().clear();
                    utilMsgs.infoMessage(sender, "&7Cleared Inventory!");
                    return true;
                }
                utilMsgs.logErrorMessage("Only player can use command!");
                return true;
            } else if(hasPermOther && context.getArgs().size() == 1) {
                PlayerObject targetPlayer = null;// must check username
                String targetPlayerName;

                if((targetPlayerName = utilPlayers.playerExists(context.argAt(0))) == null) {// determine if we can retrieve playerObject
                    if(sender != null) {
                        utilMsgs.errorMessage(sender, "&7Player not exists!");
                        return true;
                    }
                    utilMsgs.logErrorMessage("Player not exists!");
                    return true;
                }

                if((targetPlayer = plugin.getPlayer(targetPlayerName)) == null) {// retrieve target PlayerObject from plugin
                    if(sender != null) {
                        utilMsgs.errorMessage(sender, "&7PlayerObject is null!");
                        return true;
                    }
                    utilMsgs.logErrorMessage("PlayerObject is null!");
                    return true;
                }

                if(targetPlayer.getIsPlayerOnline() == false) {
                    if(sender != null) {
                        utilMsgs.errorMessage(sender, "&7Player is offline!");
                        return true;
                    }
                    utilMsgs.logErrorMessage("Player is offline!");
                    return true;
                }

                targetPlayer.getPlayer().getInventory().clear();

                utilMsgs.infoMessage(targetPlayer, "&7Cleared Inventory!");
                if(sender != null) {
                    utilMsgs.infoMessage(sender, "&7Cleared Player &6"+targetPlayerName+"'s &7Inventory!");
                    return true;
                }
                utilMsgs.logInfoMessage("Cleared Player "+targetPlayerName+"'s Inventory!");
                return true;
            } else if(!hasPermOther) {
                if(sender != null) {
                    utilMsgs.infoMessage(sender, "&7No perms to clear other's inventory!");
                    return true;
                }
                utilMsgs.logInfoMessage("No perms to clear other's inventory!");
                return true;
            }

            if(sender != null) {
                utilMsgs.infoMessage(sender,"&7Usage: /ci");
                utilMsgs.infoMessage(sender, "&7Usage: /ci <player>");
                return true;
            }
            utilMsgs.logInfoMessage("Usage: /ci <player>");
            return true;
        } else if(name.equalsIgnoreCase("rename") || name.equalsIgnoreCase("setname")) {
            if(sender != null) {
                if (context.getArgs().size() > 0) {
                    ItemStack item = sender.getPlayer().getItemInHand();
                    if (item == null || item.getType() == Material.AIR) {
                        utilMsgs.errorMessage(sender, "&7Nothing in hand");
                        return true;
                    }

                    StringBuilder stringNameBuilder = new StringBuilder();
                    int x;
                    for (x = 0; x < context.getArgs().size(); x++) {
                        stringNameBuilder.append(context.argAt(x));
                        if (x == context.getArgs().size() - 1) {
                            break;
                        }
                        stringNameBuilder.append(" ");
                    }

                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', stringNameBuilder.toString()));
                    item.setItemMeta(itemMeta);

                    utilMsgs.infoMessage(sender, "&7Successfully renamed item in hand to &6" + stringNameBuilder.toString() + "&7!");
                    return true;
                }
                utilMsgs.infoMessage(sender, "&7Usage: /rename <name>");
                return true;
            }
            utilMsgs.logErrorMessage("Only player can use command!");
            return true;
        } else if(name.equalsIgnoreCase("relore") || name.equalsIgnoreCase("setlore")) {
            if(sender != null) {
                if (context.getArgs().size() > 0) {
                    ItemStack item = sender.getPlayer().getItemInHand();
                    if (item == null || item.getType() == Material.AIR) {
                        utilMsgs.errorMessage(sender, "&7Nothing in hand");
                        return true;
                    }

                    ArrayList<String> lores = new ArrayList();

                    int x;
                    for (x = 0; x < context.getArgs().size(); x++) {
                        lores.add(ChatColor.translateAlternateColorCodes('&', context.argAt(x)));
                    }

                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setLore(lores);
                    item.setItemMeta(itemMeta);

                    utilMsgs.infoMessage(sender, "&7Successfully relored item in hand to: ");

                    for (x = 0; x < lores.size(); x++) {
                        utilMsgs.infoMessage(sender, " &9> &8[&b" + x + "&8]&6: &8'&c" + lores.get(x) + "&8' &9<");
                    }
                    return true;
                }
                utilMsgs.infoMessage(sender, "&7Usage: /relore <line1> [line2] [line3] [...]");
                return true;
            }
            utilMsgs.logErrorMessage("Only player can use command!");
            return true;
        } else if(name.equalsIgnoreCase("nick") || name.equalsIgnoreCase("nickname")) {
            if(sender != null && context.getArgs().size() == 1) {// player nicknamed self
                if(sender.getPlayerDisplayName() == null) {
                    //missing permissions to set supplied nickname
                    utilMsgs.errorMessage(sender, "&7No permissions for supplied username");
                    return true;
                }
                sender.setPlayerDisplayName(context.argAt(0));
                utilMsgs.infoMessage(sender, "&7Successfully set player nickname!");
                return true;
            } else if(context.getArgs().size() == 2) {
                if(sender == null || sender.getPlayer().hasPermission("sinistercore.nickname.other"))
                {
                    PlayerObject targetPlayer = null;// must check username
                    String targetPlayerName;

                    if((targetPlayerName = utilPlayers.playerExists(context.argAt(0))) == null) {// determine if we can retrieve playerObject
                        if(sender != null) {
                            utilMsgs.errorMessage(sender, "&7Player not exists!");
                            return true;
                        }
                        utilMsgs.logErrorMessage("Player not exists!");
                        return true;
                    }

                    if((targetPlayer = plugin.getPlayer(targetPlayerName)) == null) {// retrieve target PlayerObject from plugin
                        if(sender != null) {
                            utilMsgs.errorMessage(sender, "&7PlayerObject is null!");
                            return true;
                        }
                        utilMsgs.logErrorMessage("PlayerObject is null!");
                        return true;
                    }

                    sender.setPlayerDisplayName(context.argAt(1));
                    if(sender != null) {
                        utilMsgs.infoMessage(sender, "&7Successfully set player &6"+targetPlayerName+"'s &7nickname!");
                        return true;
                    }
                    utilMsgs.logErrorMessage("Successfully set player "+targetPlayerName+"'s nickname!");
                    return true;
                }
                // if sender doesnt have permission fall back to print available commands
            }

            if(sender != null) {
                utilMsgs.infoMessage(sender, "&7Usage: /nick <nickname>");
                if(sender.getPlayer().hasPermission("sinistercore.nickname.other")) {
                    utilMsgs.infoMessage(sender, "&7Usage: /nick <player> <nickname>");
                }
                return true;
            }
            utilMsgs.logInfoMessage("&9Usage: /nick <player> <nickname>");
            return true;
        } else if(name.equalsIgnoreCase("repair") || name.equalsIgnoreCase("fix")) {
            if(sender != null) {
                sender.getPlayer().getItemInHand().setDurability((short)0);
                utilMsgs.infoMessage(sender, "&7Item in hand has been repaired!");
                return true;
            }
            utilMsgs.logErrorMessage("Only player can use command!");
            return true;
        } else if(name.equalsIgnoreCase("feed")) {
            if(context.getArgs().size() == 1) {
                if(sender == null || sender.getPlayer().hasPermission("sinistercore.feed.other")) {
                    String pName;
                    if((pName = utilPlayers.playerExists(context.argAt(0))) == null) {
                        if(sender == null) {
                            utilMsgs.logErrorMessage("Player not exists!");
                            return true;
                        }

                        utilMsgs.errorMessage(sender, "&7Player not exists!");
                        return true;
                    }

                    if(utilPlayers.playerOnline(pName) == null) {
                        if(sender == null) {
                            utilMsgs.logErrorMessage("Player not online!");
                            return true;
                        }

                        utilMsgs.errorMessage(sender, "&7Player not online!");
                        return true;
                    }

                    PlayerObject targetPlayer = plugin.getPlayer(pName);
                    targetPlayer.getPlayer().setFoodLevel(20);

                    if(sender == null) {
                        utilMsgs.infoMessage(targetPlayer, "&7You're Hunger Has Been Satisfied By &6Console");
                        utilMsgs.logInfoMessage("Feed player "+pName);
                        return true;
                    }
                    utilMsgs.infoMessage(targetPlayer, "&7You're Hunger Has Been Satisfied By &6"+sender.playerName);
                    utilMsgs.infoMessage(sender, "&7Feed player &6"+pName);
                    return true;
                }
                utilMsgs.errorMessage(sender, "&7Not Enough Permissions");
                return true;
            }
            if(sender != null) {
                sender.getPlayer().setFoodLevel(20);
                utilMsgs.infoMessage(sender, "&7You're Hunger Has Been Satisfied");
                return true;
            }
            utilMsgs.logErrorMessage("Usage: /feed <player>");
            return true;
        } else if(name.equalsIgnoreCase("heal")) {
            if(context.getArgs().size() >= 1) {
                if(sender == null || sender.getPlayer().hasPermission("sinistercore.heal.other")) {
                    String pName;

                    if((pName = utilPlayers.playerExists(context.argAt(0))) == null) {
                        if(sender == null) {
                            utilMsgs.logErrorMessage("Player not exists!");
                            return true;
                        }

                        utilMsgs.errorMessage(sender, "&7Player not exists!");
                        return true;
                    }

                    if(utilPlayers.playerOnline(pName) == null) {
                        if(sender == null) {
                            utilMsgs.logErrorMessage("Player not online!");
                            return true;
                        }

                        utilMsgs.errorMessage(sender, "&7Player not online!");
                        return true;
                    }

                    PlayerObject targetPlayer = plugin.getPlayer(pName);
                    targetPlayer.getPlayer().setHealth(20);

                    if(sender == null) {
                        utilMsgs.infoMessage(targetPlayer, "&7You're Health Has Been Restored By &6Console");
                        utilMsgs.logInfoMessage("Healed sender "+pName);
                        return true;
                    }
                    utilMsgs.infoMessage(targetPlayer, "&7You're Health Has Been Restored By &6"+sender.playerName);
                    utilMsgs.infoMessage(sender, "&7Healed sender &6"+pName);
                    return true;
                }
                utilMsgs.errorMessage(sender, "&7Not Enough Permissions");
                return true;
            }

            if(sender != null) {
                sender.getPlayer().setHealth(20);
                utilMsgs.infoMessage(sender, "&7You're Health Has Been Restored");
                return true;
            }
            utilMsgs.logErrorMessage("Usage: /heal <player>");
            return true;
        } else if(name.equalsIgnoreCase("gmc")) {
            if(context.getArgs().size() >= 1) {
                if(sender == null || sender.getPlayer().hasPermission("sinistercore.gamemode.other")) {
                    String pName;

                    if((pName = utilPlayers.playerExists(context.argAt(0))) == null) {
                        if(sender == null) {
                            utilMsgs.logErrorMessage("Player not exists!");
                            return true;
                        }

                        utilMsgs.errorMessage(sender, "&7Player not exists!");
                        return true;
                    }

                    if(utilPlayers.playerOnline(pName) == null) {
                        if(sender == null) {
                            utilMsgs.logErrorMessage("Player not online!");
                            return true;
                        }

                        utilMsgs.errorMessage(sender, "&7Player not online!");
                        return true;
                    }

                    PlayerObject targetPlayer = plugin.getPlayer(pName);
                    targetPlayer.getPlayer().setGameMode(GameMode.CREATIVE);

                    if(sender == null) {
                        utilMsgs.infoMessage(targetPlayer, "&7You've been put into creative By &6Console");
                        utilMsgs.logInfoMessage("Put sender "+pName+ " into creative");
                        return true;
                    }

                    utilMsgs.infoMessage(targetPlayer, "&7You've been put into creative By &6"+sender.playerName);
                    utilMsgs.infoMessage(sender, "&7Put sender &6"+pName+ " &7into creative");
                    return true;
                }
                utilMsgs.errorMessage(sender, "&7Not Enough Permissions");
                return true;
            }

            if(sender != null) {
                sender.getPlayer().setGameMode(GameMode.CREATIVE);
                utilMsgs.infoMessage(sender, "&7You've been put in creative gamemode");
                return true;
            }
            utilMsgs.logErrorMessage("Usage: /gmc <player>");
            return true;
        } else if(name.equalsIgnoreCase("gms")) {
            if(context.getArgs().size() >= 1) {
                if(sender == null || sender.getPlayer().hasPermission("sinistercore.gamemode.other")) {
                    String pName;
                    if((pName = utilPlayers.playerExists(context.argAt(0))) == null) {
                        if(sender == null) {
                            utilMsgs.logErrorMessage("Player not exists!");
                            return true;
                        }

                        utilMsgs.errorMessage(sender, "&7Player not exists!");
                        return true;
                    }

                    if(utilPlayers.playerOnline(pName) == null) {
                        if(sender == null) {
                            utilMsgs.logErrorMessage("Player not online!");
                            return true;
                        }

                        utilMsgs.errorMessage(sender, "&7Player not online!");
                        return true;
                    }

                    PlayerObject targetPlayer = plugin.getPlayer(pName);
                    targetPlayer.getPlayer().setGameMode(GameMode.SURVIVAL);

                    if(sender == null) {
                        utilMsgs.infoMessage(targetPlayer, "&7You've been put into survival By &6Console");
                        utilMsgs.logInfoMessage("Put sender " + pName + " into survival");
                        return true;
                    }

                    utilMsgs.infoMessage(targetPlayer, "&7You've been put into survival By &6"+sender.playerName);
                    utilMsgs.infoMessage(sender, "&7Put player &6"+pName+ " &7into survival");
                    return true;
                }
                utilMsgs.errorMessage(sender, "&4Not Enough Permissions");
                return true;
            }

            if(sender != null) {
                sender.getPlayer().setGameMode(GameMode.SURVIVAL);
                utilMsgs.infoMessage(sender, "&9You've been put in survival gamemode");
                return true;
            }

            utilMsgs.logErrorMessage("Usage: /gms <player>");
            return true;
        } else if(name.equalsIgnoreCase("fly")) {
            if(context.getArgs().size() == 1) {
                if(sender == null || sender.getPlayer().hasPermission("sinistercore.fly.other")) {
                    String pName;
                    if((pName = utilPlayers.playerExists(context.argAt(0))) == null) {
                        if(sender == null) {
                            utilMsgs.logErrorMessage("Player not exists!");
                            return true;
                        }
                        utilMsgs.errorMessage(sender, "&7Player not exists!");
                        return true;
                    }

                    if(utilPlayers.playerOnline(pName) == null) {
                        if (sender == null) {
                            utilMsgs.logErrorMessage("Player not online!");
                            return true;
                        }
                        utilMsgs.errorMessage(sender, "&7Player not online!");
                        return true;
                    }

                    PlayerObject targetPlayer = plugin.getPlayer(pName);

                    if(targetPlayer.getPlayer().getAllowFlight() == true) {
                        targetPlayer.getPlayer().setAllowFlight(false);
                        targetPlayer.getPlayer().setFlying(false);
                        if (sender == null) {
                            utilMsgs.infoMessage(targetPlayer, "&7You've been taken out of fly mode By &6Console");
                            utilMsgs.logInfoMessage("Taken player " + pName + " out of fly mode");
                            return true;
                        }
                        utilMsgs.infoMessage(targetPlayer, "&7You've been taken out of fly mode By &6" + sender.playerName);
                        utilMsgs.infoMessage(sender, "&7Taken player &6" + pName + " &7out of fly mode");
                        return true;
                    }
                    targetPlayer.getPlayer().setAllowFlight(true);
                    targetPlayer.getPlayer().setFlying(true);
                    if (sender == null) {
                        utilMsgs.infoMessage(targetPlayer, "&7You've been put into fly mode By &6Console");
                        utilMsgs.logInfoMessage("Put player " + pName + " into fly mode");
                        return true;
                    }
                    utilMsgs.infoMessage(targetPlayer, "&7You've been put into fly mode By &6" + sender.playerName);
                    utilMsgs.infoMessage(sender, "&7Put player &6" + pName + " &7into fly mode");
                    return true;
                }
                utilMsgs.errorMessage(sender, "&7Not Enough Permissions");
                return true;
            }

            if(sender != null) {
                if (sender.getPlayer().getAllowFlight() == true) {
                    sender.getPlayer().setAllowFlight(false);
                    sender.getPlayer().setFlying(false);
                    utilMsgs.infoMessage(sender, "&7You've been taken out of fly mode");
                    return true;
                }
                sender.getPlayer().setAllowFlight(true);
                sender.getPlayer().setFlying(true);
                utilMsgs.infoMessage(sender, "&7You've been put in fly mode");
                return true;
            }
            utilMsgs.logErrorMessage("Usage: /fly <player>");
            return true;
        } else if(name.equalsIgnoreCase("back")) {
            if(sender != null) {
                Location loc = sender.getLastPlayerDeathLocation();
                if(loc != null) {
                    sender.teleportPlayer(loc, true);
                    return true;
                }
                utilMsgs.errorMessage(sender, "&7No death found to teleport to!");
                return true;
            }
            utilMsgs.logErrorMessage("only player can execute this command");
            return true;
        }
        return true;
    }
}
