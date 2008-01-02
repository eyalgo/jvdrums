/*
 * Copyright (c) 2007 by Eyal Golan
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jvdrums;

import java.util.Vector;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * @author egolan
 */
public final class UserPreferences {
    private Preferences userPrefs;
    private static UserPreferences instance;

    private UserPreferences() {
        userPrefs = Preferences.userNodeForPackage(UserPreferences.class);
    }

    public static UserPreferences getInstance() {
        if (instance == null) {
            instance = new UserPreferences();
        }

        return instance;
    }
    
    public void putInt(String key, int value) {
        userPrefs.putInt(key, value);
    }
    
    public int getInt(String key, int def) {
        return userPrefs.getInt(key, def);
    }

    public void put(String key, String value) {
        userPrefs.put(key, value);
    }

    public String get(String key, String def) {
        return userPrefs.get(key, def);
    }

    public Vector<String> showPrefs() {
        Vector<String> v = new Vector<String>();
        try {
            String keys[] = userPrefs.keys();
            for (String key : keys) {
                v.add(key + " " + userPrefs.get(key, ""));
            }
        }
        catch (BackingStoreException e) {
            e.printStackTrace();
            return null;
        }
        return v;
    }

    private void clearPreferences() {
        try {
            userPrefs.clear();
        }
        catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }
    
    public static void main (String[] args) {
        getInstance().clearPreferences();
    }
}
