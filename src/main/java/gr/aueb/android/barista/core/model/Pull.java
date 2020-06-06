package gr.aueb.android.barista.core.model;

import gr.aueb.android.barista.core.executor.CommandClient;
import gr.aueb.android.barista.utilities.BaristaCommandPrefixes;

public class Pull extends AbstractAdbCommand {

    private String filename;

    public Pull(String token, String filename) {
        super(token);
        this.filename = filename;
    }

    @Override
    public String getCommandString() {
        return BaristaCommandPrefixes.PULL + " " + filename;
    }

    @Override
    public boolean isCompleted(CommandClient client) {
        return true;
    }
}
