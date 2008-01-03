/*
 * jOrgan - Java Virtual Organ
 * Copyright (C) 2003 Sven Meier
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package jvdrums;

import java.util.Vector;

import bias.Configuration;

public class Info {
    private static Configuration config = Configuration.getRoot().get(Info.class);
    private String version = "";

    public Info() {
        config.read(this);
    }

    public String getVersion() {
        return version;
    }

    public void log() {
        JVDrumsLogger.getLogger().info("JVDrums Started");
        JVDrumsLogger.getLogger().info("JVDrums -" + version + "-");
        JVDrumsLogger.getLogger().info(appendProperty("os.arch"));
        JVDrumsLogger.getLogger().info(appendProperty("os.name"));
        JVDrumsLogger.getLogger().info(appendProperty("os.version"));
        JVDrumsLogger.getLogger().info(appendProperty("java.home"));
        JVDrumsLogger.getLogger().info(appendProperty("java.version"));
        JVDrumsLogger.getLogger().info(appendProperty("java.runtime.name"));
        JVDrumsLogger.getLogger().info(appendProperty("java.runtime.version"));
        JVDrumsLogger.getLogger().info(appendProperty("user.dir"));
        JVDrumsLogger.getLogger().info(appendProperty("user.home"));
        JVDrumsLogger.getLogger().info(appendProperty("user.country"));
        JVDrumsLogger.getLogger().info(appendProperty("user.language"));
        JVDrumsLogger.getLogger().info(appendProperty("user.name"));
        Vector<String> prefs = UserPreferences.getInstance().showPrefs();
        for (String pref : prefs) {
            JVDrumsLogger.getLogger().info("UserPreferences: " + pref);
        }
    }

    private String appendProperty(String key) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(key);
        buffer.append(" = ");
        buffer.append(System.getProperty(key));
        return buffer.toString();
    }
}