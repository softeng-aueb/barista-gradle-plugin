/**
 * Author: Tsiskomichelis Stelios
 * Created On: 9/4/2019
 * Project: barista-plugin
 * <p>
 * ClassName: BaristaADBCommandPrefix
 * Role: Statically define all the command prefixes used by barista
 * Description: All the commands use those strings in order to create their full string representation
 *              and execute themshelves in the command console.
 */
package gr.aueb.android.barista.utilities;

public class BaristaCommandPrefixes {

    public static final String DUMPSYS_BATTERY_LEVEL = "shell dumpsys battery set level "; // 0 - 100
    public static final String DUMPSYS_CHARGE_STATUS = "shell dumpsys battery set ac "; // 0 |1/
    public static final String DUMPSYS_BATTERY_STATUS = "shell dumpsys battery";
    public static final String GEO_FIX = "geo fix"; // telnet
    public static final String PM_GRANT = "shell pm grant"; // permission name
    public static final String PM_REVOKE = "shell pm revoke"; // permission name
    public static final String SVC_DATA = "shell svc data"; // enable | disable
    public static final String SVC_WIFI = "shell svc wifi"; //enable | disable
    public static final String SVC_WIFI_STATUS = "shell dumpsys wifi";
    public static final String WM_DENSITY = "shell wm density"; // integer
    public static final String WM_SIZE = "shell wm size"; // integerXinteger
    public static final String WM_SIZE_RESET = "shell wm size reset"; // -
    public static final String WM_DENSITY_RESET = "shell wm density reset"; // -
    public static final String AUTH = "auth"; // telnet
    public static final String SET_ORIENTATION = "shell content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:";
    public static final String GET_ORIENTATION = "shell content query --uri content://settings/system --projection name:value --where \"name='user_rotation'\"";
    public static final String GPS_STATUS = "shell settings put secure location_providers_allowed";
    public static final String GPS_STATUS_RES = "shell settings get secure location_providers_allowed";
    public static final String MONKEY = "shell monkey";
    public static final String LOGCAT_CRASH = "logcat --buffer=crash";
    public static final String GO_HOME = "shell input keyevent 3";
    public static final String APP_SWITCH = "shell input keyevent 187";
    public static final String SWIPE_UP = "shell input touchscreen swipe 200 500 200 0";
    public static final String PULL = "pull";
}
