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

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import kits.TdKit;
import managers.TDManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import ui.MainFrame;
import ui.lists.OutputKitsList;

/**
 * @author egolan
 */
@SuppressWarnings("serial")
public final class SaveAction extends FileAction implements ListDataListener {
    private final OutputKitsList outputKitsList;
    
    public SaveAction(MainFrame mainFrame, OutputKitsList outputKitsList) {
        this(mainFrame, outputKitsList, true);
    }

    public SaveAction(MainFrame mainFrame, OutputKitsList outputKitsList, boolean withIcon) {
        super(mainFrame, "save", withIcon);
        this.outputKitsList = outputKitsList;
        this.outputKitsList.getModel().addListDataListener(this);
        setEnabledByKits();
    }

    @Override
    protected void handleAction(File file) throws InvalidMidiDataException, IOException {
        final TdKit[] kitsInList = outputKitsList.getKits();
        if (!FilenameUtils.isExtension(file.getName(), "syx")) {
            String name = file.getAbsolutePath() + ".syx";
            file = new File(name);
        }
        final SysexMessage messageFromManager = TDManager.kitsToSysexMessage(kitsInList);
        FileUtils.writeByteArrayToFile(file, messageFromManager.getMessage());
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
        setEnabledByKits();
    }

    private void setEnabledByKits() {
        setEnabled(outputKitsList.numberOfKits() > 0);
    }

    @Override
    public void intervalAdded(ListDataEvent e) {
    // Unimplemented
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
    // Unimplemented
    }
}
