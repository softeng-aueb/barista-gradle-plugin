/**
 * Author: Tsiskomichelis Stelios
 * Created On: 9/4/2019
 * Project: barista-plugin
 * <p>
 * ClassName: BaristaADBCommandPrefix
 * Role:
 * Description:
 */
package gr.aueb.android.barista.utilities;

public class BaristaCommandPrefixes {

    public static final String DUMPSYS_BATTERY_LEVEL = "shell dumpsys battery set level "; // 0 |1
    public static final String DUMPSYS_CHARGE_STATUS = "shell dumpsys battery set ac "; // 0 - 100
    public static final String DUMPSYS_BATTERY_STATUS = "shell dumpsys battery";
    public static final String GEO_FIX = "geo fix"; // telnet
    public static final String PM_GRANT = "shell pm grant"; // permission name
    public static final String PM_REVOKE = "shell pm revoke"; // permission name
    public static final String SVC_DATA = "shell svc data"; // enable | disable
    public static final String SVC_WIFI = "shell svc wifi"; //enable | disable
    public static final String WM_DENSITY = "shell wm density"; // integer
    public static final String WM_SIZE = "shell wm size"; // integerXinteger
    public static final String WM_SIZE_RESET = "shell wm size reset"; // -
    public static final String WM_DENSITY_RESET = "shell wm density reset"; // -
    public static final String AUTH = "auth"; // telnet
    public static final String SET_ORIENTATION = "shell content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:";
    public static final String GET_ORIENTATION = "shell content query --uri content://settings/system --projection name:value --where 'name=\\\"user_rotation\\\"'";

}
