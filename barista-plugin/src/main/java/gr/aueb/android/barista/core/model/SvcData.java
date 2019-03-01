/**
 * Author: Tsiskomichelis Stelios
 * Created On: 1/3/2019
 * Project: barista-plugin
 * <p>
 * ClassName: SvcData
 * Role:
 * Description:
 */
package gr.aueb.android.barista.core.model;


public class SvcData extends AbstractAdbCommand {
    private final String SVC_DATA = "shell svc data";
    private boolean enabled;

    public SvcData() {
    }

    public SvcData(String sessionToken, boolean enabled) {
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
        buffer.append(SVC_DATA).append(" ").append(((enabled)? "enable" : "disable"));
        String command = buffer.toString();
        return command;
    }
}
