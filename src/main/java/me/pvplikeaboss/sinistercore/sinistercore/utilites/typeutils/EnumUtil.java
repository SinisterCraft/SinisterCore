package me.pvplikeaboss.sinistercore.sinistercore.utilites.typeutils;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.Set;

public final class EnumUtil {

    private EnumUtil() {
    }

    public static <T extends Enum<T>> T valueOf(final Class<T> enumClass, final String... names) {
        for (final String name : names) {
            try {
                final Field enumField = enumClass.getDeclaredField(name);

                if (enumField.isEnumConstant()) {
                    return (T) enumField.get(null);
                }
            } catch (final NoSuchFieldException | IllegalAccessException ignored) {
            }
        }

        return null;
    }

    public static <T extends Enum<T>> Set<T> getAllMatching(final Class<T> enumClass, final String... names) {
        final Set<T> set = EnumSet.noneOf(enumClass);

        for (final String name : names) {
            try {
                final Field enumField = enumClass.getDeclaredField(name);

                if (enumField.isEnumConstant()) {
                    set.add((T) enumField.get(null));
                }
            } catch (final NoSuchFieldException | IllegalAccessException ignored) {
            }
        }

        return set;
    }

    public static Material getMaterial(final String... names) {
        return valueOf(Material.class, names);
    }

    public static Statistic getStatistic(final String... names) {
        return valueOf(Statistic.class, names);
    }

    public static EntityType getEntityType(final String... names) {
        return valueOf(EntityType.class, names);
    }
}
