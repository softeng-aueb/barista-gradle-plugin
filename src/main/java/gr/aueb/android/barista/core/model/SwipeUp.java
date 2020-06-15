package gr.aueb.android.barista.core.model;

import gr.aueb.android.barista.core.executor.CommandClient;
import gr.aueb.android.barista.utilities.BaristaCommandPrefixes;

public class SwipeUp extends AbstractAdbCommand {

    public SwipeUp(String token) {
        super(token);
    }

    @Override
    public String getCommandString() {
        return BaristaCommandPrefixes.SWIPE_UP;
    }

    @Override
    public boolean isCompleted(CommandClient client) {
        return true;
    }
}
