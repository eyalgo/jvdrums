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

package ui.swing.actions;

import java.awt.event.ActionEvent;

import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;

import jvdrums.UserPreferences;
import ui.MainFrame;
import ui.utils.WindowUtilities;

/**
 * @author egolan
 */
@SuppressWarnings("serial")
public final class LookAndFeelAction extends BaseAction {
    private final String lookAndFeel;
    private final MainFrame mainFrame;

    public LookAndFeelAction(MainFrame mainFrame, String lookAndFeel) {
        this.lookAndFeel = lookAndFeel;
        this.mainFrame = mainFrame;
        try {
            Class<?> lnfClass = Class.forName(lookAndFeel);
            LookAndFeel lookAndFeelClass = (LookAndFeel) lnfClass.newInstance();
            setName(lookAndFeelClass.getName());
            setShortDescription(lookAndFeelClass.getDescription());
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Runnable runnable = new Runnable() {
            public void run() {
                WindowUtilities.setLookAndFeel(lookAndFeel);
                SwingUtilities.updateComponentTreeUI(mainFrame);
                mainFrame.pack();
                UserPreferences.getInstance().put("lookAndFeel", lookAndFeel);
            }
        };
        SwingUtilities.invokeLater(runnable);
    }
}
