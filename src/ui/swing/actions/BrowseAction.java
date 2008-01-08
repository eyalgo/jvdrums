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

import kits.TdKit;
import managers.TDManager;

import org.apache.commons.io.FileUtils;

import ui.MainFrame;
import ui.panels.KitsPanel;
import exceptions.VdrumException;

/**
 * @author egolan
 */
@SuppressWarnings("serial")
public final class BrowseAction extends FileAction {
    private final KitsPanel kitsPanel;

    public BrowseAction(MainFrame mainFrame, KitsPanel kitsPanel) {
        this(mainFrame, kitsPanel, true);
    }

    public BrowseAction(MainFrame mainFrame, KitsPanel kitsPanel, boolean withIcon) {
        super(mainFrame, "browse", withIcon);
        this.kitsPanel = kitsPanel;
        config.get("browse").read(this);
        if (!withIcon) {
            setSmallIcon(null);
        }
    }

    @Override
    protected boolean handleAction(final File file) throws IOException, InvalidMidiDataException,
            VdrumException {
        byte[] bytes = FileUtils.readFileToByteArray(file);
        TdKit[] kits = TDManager.bytesToKits(bytes);
        for (TdKit kit : kits) {
            if (kit != null) {
                kitsPanel.addKit(kit);
            }
        }
        return true;
    }
}
