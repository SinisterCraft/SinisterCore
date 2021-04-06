package me.pvplikeaboss.sinistercore.sinistercore.utilites.misc;

import java.util.Locale;

public interface AbstractMisc {
    /**
     * Gets the current locale setting
     *
     * @return the current locale, if not set it will return the default locale
     */
    Locale getCurrentLocale();
}
