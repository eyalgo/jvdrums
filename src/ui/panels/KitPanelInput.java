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

package ui.panels;

import ui.MainFrame;
import ui.lists.InputKitsList;
import ui.lists.KitsList;
import ui.swing.actions.BrowseAction;
import ui.swing.actions.ClearListAction;
import ui.swing.actions.LoadFromModuleAction;
import ui.swing.actions.MoveRight;

/**
 * @author egolan
 */
public final class KitPanelInput extends KitsPanel {
    private static final long serialVersionUID = 3267475173137048327L;
    private final InputKitsList inputKitsList;
    public KitPanelInput(MainFrame parentFrame, KitsPanel outputPanel) {
        super(parentFrame);
        inputKitsList = new InputKitsList(outputPanel.getKitList());
        setListInView(inputKitsList);
        addToButtonBar(new BrowseAction(getParentFrame(), inputKitsList));
        addToButtonBar(new LoadFromModuleAction());
        addToButtonBar(new ClearListAction(inputKitsList));
//        addToButtonBar(new MoveRight(inputKitsList));
    }

    @Override
    public KitsList getKitList() {
        return inputKitsList;
    }
}
