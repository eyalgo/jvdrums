package ui.swing.actions;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import bias.Configuration;

public abstract class BaseAction extends AbstractAction {
    static Configuration config = Configuration.getRoot().get(BaseAction.class);
    public BaseAction () {
    }
    public void setName(String name) {
        putValue(Action.NAME, name);
    }

    public String getName() {
        return (String) getValue(Action.NAME);
    }

    public void setShortDescription(String description) {
        putValue(Action.SHORT_DESCRIPTION, description);
    }

    public String getShortDescription() {
        return (String) getValue(Action.SHORT_DESCRIPTION);
    }

    public void setSmallIcon(Icon icon) {
        putValue(Action.SMALL_ICON, icon);
    }

    public Icon getSmallIcon() {
        return (Icon) getValue(Action.SMALL_ICON);
    }
}
