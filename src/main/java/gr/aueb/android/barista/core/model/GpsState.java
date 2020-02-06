package gr.aueb.android.barista.core.model;

import java.util.Hashtable;
import java.util.Map;

import gr.aueb.android.barista.core.executor.CommandClient;
import gr.aueb.android.barista.utilities.BaristaCommandPrefixes;
import gr.aueb.android.barista.utilities.BaristaLogger;

public class GpsState extends AbstractAdbCommand {

    private boolean enabled;
    private static final Map<String, Boolean> possibleStatusDescription;

    static{
        possibleStatusDescription = new Hashtable<>();
        possibleStatusDescription.put("ENABLED", true);
        possibleStatusDescription.put("DISABLED", false);
    }

    public GpsState() {

    }

    public GpsState(String sessionToken, boolean enabled) {
        super(sessionToken);
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getCommandString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(BaristaCommandPrefixes.GPS_STATUS).append(" ").append(((enabled)? "+gps" : "-gps"));
        String command = buffer.toString();
        return command;
    }

    @Override
    public boolean isCompleted(CommandClient client){
        SvcWifiStatus wifiStatus = new SvcWifiStatus(this.getSessionToken());
        client.executeCommand(wifiStatus);
        Boolean result = possibleStatusDescription.get(wifiStatus.getStatus());
        if(result == enabled ){
            BaristaLogger.print("Completed");
            return true;
        }

        BaristaLogger.print("not Completed");
        return false;
    }
}