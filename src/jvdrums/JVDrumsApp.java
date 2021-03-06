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

import java.awt.EventQueue;

import ui.MainFrame;
import bias.Configuration;
import bias.store.PropertiesStore;
import bias.store.ResourceBundlesStore;

/**
 * @author egolan
 */
public final class JVDrumsApp {
    private static Configuration config = Configuration.getRoot().get(JVDrumsApp.class);

    public static void main(String args[]) {
        /* Collection<Option> options = */initConfiguration();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Info().log();
                createAndShowGui();
            }
        });
    }
    
    private static void createAndShowGui() {
        final MainFrame mainFrame = new MainFrame();
        config.read(mainFrame);
        try {
            mainFrame.initFrame();
            mainFrame.setVisible(true);
        }
        catch (Error e) {
            e.printStackTrace();
            mainFrame.showErrorDialog(e.getLocalizedMessage(), "Fatal Error");
            System.exit(-1);
        }
        catch (Exception e) {
            e.printStackTrace();
            mainFrame.showErrorDialog(e.getLocalizedMessage(), "Fatal Exception");
            System.exit(-2);
        }
    }

    /**
     * Initialize the configuration.
     * 
     * @return the command line options of the configuration
     */
    private static void initConfiguration() {
        Configuration configuration = Configuration.getRoot();
        configuration.addStore(new PropertiesStore(JVDrumsApp.class, "app.properties"));
        configuration.addStore(new ResourceBundlesStore("jvdrums"));
    }
}
