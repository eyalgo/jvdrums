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
import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JFileChooser;

import ui.MainFrame;
import ui.swing.SyxFileChooser;
import bias.Configuration;
import exceptions.VdrumException;

/**
 * @author egolan
 */
public abstract class FileAction extends BaseAction {
    private static Configuration configuration = Configuration.getRoot().get(FileAction.class);
    private final MainFrame mainFrame;
    String buttonStr = "";
    
    protected FileAction(MainFrame mainFrame, String name, boolean withIcon) {
        this.mainFrame = mainFrame;
        configuration.get(name).read(this);
        if (!withIcon) {
            setSmallIcon(null);
        }
    }

    @Override
    public final void actionPerformed(ActionEvent e) {
        final JFileChooser fc = new SyxFileChooser();
        int option = fc.showDialog(mainFrame, buttonStr);
        if (JFileChooser.APPROVE_OPTION == option) {
            final File file = fc.getSelectedFile();
            try {
                handleAction(file);
            }
            catch (IOException e1) {
                mainFrame.showErrorDialog(e1.getMessage(), e1.getMessage());
                e1.printStackTrace();
            }
            catch (InvalidMidiDataException e1) {
                mainFrame.showErrorDialog(e1.getMessage(), e1.getMessage());
                e1.printStackTrace();
            }
            catch (VdrumException e2) {
                mainFrame.showErrorDialog(e2);
                e2.printStackTrace();
            }
            catch (Exception e9) {
                mainFrame.showErrorDialog(e9.getMessage(), e9.getMessage());
            }
            catch (Error er) {
                mainFrame.showErrorDialog("Fatal Error", er.getMessage());
            }
        }
    }

    protected abstract void handleAction(File file) throws IOException,
            InvalidMidiDataException, VdrumException;
}
