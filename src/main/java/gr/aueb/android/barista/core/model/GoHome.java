package gr.aueb.android.barista.core.model;

import gr.aueb.android.barista.core.executor.CommandClient;
import gr.aueb.android.barista.utilities.BaristaCommandPrefixes;

public class ForceStop extends AbstractAdbCommand {

    private String apk;

    public ForceStop(String token, String apk) {
        super(token);
        this.apk = apk;
    }

    @Override
    public String getCommandString() {
        return BaristaCommandPrefixes.FORCE_STOP;
    }

    @Override
    public boolean isCompleted(CommandClient client) {
        return true;
    }
}
