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
 * 
 * 
 * This class was taken from an example in
 * http://www.devx.com/getHelpOn/10MinuteSolution/20423
 * 
 */

package ui.utils;

import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

/**
 * @author egolan
 */
public final class ImageBundle extends ResourceBundle {
//    private static final Vector<String> KEYS;
    private static Hashtable<String, ImageIcon> TABLE;

    static {
//        KEYS = new Vector<String>();
        // __KEYS.addElement("language");
        TABLE = new Hashtable<String, ImageIcon>();
    }

    public ImageBundle() {

    }

    @Override
    public Enumeration<String> getKeys() {
        return TABLE.keys();
//        return KEYS.elements();
    }

    @Override
    protected ImageIcon handleGetObject(String key) {
        return loadImage(key);
    }

    private ImageIcon loadImage(final String key) {
        final String imageName = key;
        ImageIcon icon = (ImageIcon) TABLE.get(imageName);
        if (icon != null) {
            return icon;
        }
        URL url = ImageBundle.class.getResource("images/"+ imageName);// + File.separator 
        if (url == null) {
            System.err.println("url for " + imageName + " is null");
            return null;
        }
        icon = new ImageIcon(url);
        TABLE.put(imageName, icon);
        return icon;
    }
}
