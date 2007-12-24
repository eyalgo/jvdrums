package ui.swing.actions;

import java.awt.event.ActionEvent;

import ui.MainFrame;
import ui.panels.AboutPanel;

@SuppressWarnings("serial")
public final class AboutAction extends BaseAction {
    private final MainFrame mainFrame;

    public AboutAction(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        config.get("about").read(this);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        AboutPanel.showInDialog(mainFrame);
    }
}
