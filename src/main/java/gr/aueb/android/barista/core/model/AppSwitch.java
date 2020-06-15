package gr.aueb.android.barista.core.model;

import gr.aueb.android.barista.core.executor.CommandClient;
import gr.aueb.android.barista.utilities.BaristaCommandPrefixes;

public class AppSwitch extends AbstractAdbCommand {

    public AppSwitch(String token) {
        super(token);
    }

    @Override
    public String getCommandString() {
        return BaristaCommandPrefixes.APP_SWITCH;
    }

    @Override
    public boolean isCompleted(CommandClient client) {
        return true;
    }
}
