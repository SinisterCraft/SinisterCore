package me.pvplikeaboss.sinistercore.sinistercore.utilites.misc;

import me.pvplikeaboss.sinistercore.sinistercore.SinisterCore;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Pattern;

public class Misc implements AbstractMisc {
    private transient SinisterCore plugin = null;

    private static final String MESSAGES = "messages";
    private static final Pattern NODOUBLEMARK = Pattern.compile("''");
    private static final ResourceBundle NULL_BUNDLE = new ResourceBundle() {
        public Enumeration<String> getKeys() {
            return null;
        }

        protected Object handleGetObject(final String key) {
            return null;
        }
    };
    private static Misc instance;
    private final transient Locale defaultLocale = Locale.getDefault();
    private transient ResourceBundle defaultBundle;
    private transient Locale currentLocale = defaultLocale;
    private transient ResourceBundle customBundle;
    private transient ResourceBundle localeBundle;
    private transient Map<String, MessageFormat> messageFormatCache = new HashMap<>();

    public void load(SinisterCore p) {
        this.plugin = p;
        //defaultBundle = ResourceBundle.getBundle(MESSAGES, Locale.ENGLISH, new UTF8PropertiesControl());
        //localeBundle = defaultBundle;
        //customBundle = NULL_BUNDLE;
    }

    public void unload() {
        plugin = null;
    }

    public static String tl(final String string, final Object... objects) {
        if (instance == null) {
            return "";
        }
        if (objects.length == 0) {
            return NODOUBLEMARK.matcher(instance.translate(string)).replaceAll("'");
        } else {
            return instance.format(string, objects);
        }
    }

    public static String capitalCase(final String input) {
        return input == null || input.length() == 0 ? input : input.toUpperCase(Locale.ENGLISH).charAt(0) + input.toLowerCase(Locale.ENGLISH).substring(1);
    }

    public void onEnable() {
        instance = this;
    }

    public void onDisable() {
        instance = null;
    }

    @Override
    public Locale getCurrentLocale() {
        return currentLocale;
    }

    private String translate(final String string) {
        try {
            try {
                return string;
            } catch (final MissingResourceException ex) {
                return string;
            }
        } catch (final MissingResourceException ex) {
            return string;
        }
    }

    public String format(final String string, final Object... objects) {
        String format = translate(string);
        MessageFormat messageFormat = messageFormatCache.get(format);
        if (messageFormat == null) {
            try {
                messageFormat = new MessageFormat(format);
            } catch (final IllegalArgumentException e) {
                format = format.replaceAll("\\{(\\D*?)\\}", "\\[$1\\]");
                messageFormat = new MessageFormat(format);
            }
            messageFormatCache.put(format, messageFormat);
        }
        return messageFormat.format(objects).replace(' ', ' '); // replace nbsp with a space
    }

    public void updateLocale(final String loc) {
        if (loc != null && !loc.isEmpty()) {
            final String[] parts = loc.split("[_\\.]");
            if (parts.length == 1) {
                currentLocale = new Locale(parts[0]);
            }
            if (parts.length == 2) {
                currentLocale = new Locale(parts[0], parts[1]);
            }
            if (parts.length == 3) {
                currentLocale = new Locale(parts[0], parts[1], parts[2]);
            }
        }
        ResourceBundle.clearCache();
        messageFormatCache = new HashMap<>();

        try {
            localeBundle = ResourceBundle.getBundle(MESSAGES, currentLocale, new UTF8PropertiesControl());
        } catch (final MissingResourceException ex) {
            localeBundle = NULL_BUNDLE;
        }

        try {
            customBundle = ResourceBundle.getBundle(MESSAGES, currentLocale, new FileResClassLoader(Misc.class.getClassLoader(), plugin), new UTF8PropertiesControl());
        } catch (final MissingResourceException ex) {
            customBundle = NULL_BUNDLE;
        }
    }

    /**
     * Attempts to load properties files from the plugin directory before falling back to the jar.
     */
    private class FileResClassLoader extends ClassLoader {
        private final transient File dataFolder;

        FileResClassLoader(final ClassLoader classLoader, final SinisterCore ess) {
            super(classLoader);
            this.dataFolder = plugin.getDataFolder();
        }

        @Override
        public URL getResource(final String string) {
            final File file = new File(dataFolder, string);
            if (file.exists()) {
                try {
                    return file.toURI().toURL();
                } catch (final MalformedURLException ignored) {
                }
            }
            return null;
        }

        @Override
        public InputStream getResourceAsStream(final String string) {
            final File file = new File(dataFolder, string);
            if (file.exists()) {
                try {
                    return new FileInputStream(file);
                } catch (final FileNotFoundException ignored) {
                }
            }
            return null;
        }
    }

    private static class UTF8PropertiesControl extends ResourceBundle.Control {
        public ResourceBundle newBundle(final String baseName, final Locale locale, final String format, final ClassLoader loader, final boolean reload) throws IOException {
            final String resourceName = toResourceName(toBundleName(baseName, locale), "properties");
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload) {
                final URL url = loader.getResource(resourceName);
                if (url != null) {
                    final URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try {
                    // use UTF-8 here, this is the important bit
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
                } finally {
                    stream.close();
                }
            }
            return bundle;
        }
    }
}
