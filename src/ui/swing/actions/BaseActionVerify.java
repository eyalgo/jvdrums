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

import javax.swing.JOptionPane;

import ui.MainFrame;

import bias.Configuration;

/**
 * @author egolan
 */
public abstract class BaseActionVerify extends BaseAction {
    private static Configuration configuration = Configuration.getRoot().get(
            BaseActionVerify.class);
    final MainFrame mainFrame;
    String confirmMessage = "";
    String confirmTitle = "";
    String areYouSureMessage = "";

    BaseActionVerify(MainFrame mainFrame, String name) {
        this.mainFrame = mainFrame;
        configuration.read(this);
        configuration.get(name).read(this);

    }

    @Override
    public final void actionPerformed(ActionEvent e) {
        int result = JOptionPane.showConfirmDialog(mainFrame, confirmMessage(),
                confirmTitle(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            verifyOk();
        }
    }

    abstract void verifyOk();

    final String confirmMessage() {
        final StringBuilder strBuilder = new StringBuilder(confirmMessage);
        strBuilder.append(System.getProperty("line.separator")).append(areYouSureMessage);
        return strBuilder.toString();
    }

    final String confirmTitle() {
        return confirmTitle;
    }
}
