package me.pvplikeaboss.sinistercore.sinistercore.modules.files.configs.csv.items.methods;

import me.pvplikeaboss.sinistercore.sinistercore.utilites.typeutils.PluginKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public interface IItemDB extends ItemDB {
    void registerResolver(Plugin plugin, String name, ItemResolver resolver) throws Exception;

    void unregisterResolver(Plugin plugin, String name) throws Exception;

    boolean isResolverPresent(Plugin plugin, String name);

    Map<PluginKey, ItemResolver> getResolvers();

    Map<PluginKey, ItemResolver> getResolvers(Plugin plugin);

    ItemResolver getResolver(Plugin plugin, String name);

    ItemStack get(String name, boolean useResolvers) throws Exception;

    @Deprecated
    String serialize(ItemStack itemStack, boolean useResolvers);

    @FunctionalInterface
    interface ItemResolver extends Function<String, ItemStack> {

        @Override
        ItemStack apply(String name);

        default Collection<String> getNames() {
            return null;
        }

        default String serialize(final ItemStack stack) {
            return null;
        }
    }

}
