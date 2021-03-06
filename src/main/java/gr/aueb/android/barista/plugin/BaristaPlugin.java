/**
 * Author: Tsiskomichelis Stelios
 * Created On: 17/10/2018
 * Project: barista-plugin
 * <p>
 * ClassName: TestDispacherServerTask
 * Role: Implements the task that deploys the adb test dispacher
 * Description:
 */
package gr.aueb.android.barista.plugin;

import com.android.build.gradle.api.ApplicationVariant;
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension;
import gr.aueb.android.barista.core.BaristaConfigurationExtension;
import gr.aueb.android.barista.core.emulator.EmulatorManager;
import gr.aueb.android.barista.server.HttpServerManager;
import gr.aueb.android.barista.utilities.BaristaLogger;
import org.gradle.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public class BaristaPlugin implements Plugin<Project> {

    public static final String BARISTA_PLUGIN_SETTINGS_SECTION = "baristaSettings";
    public static final String BARISTA_PORT_BUILD_CONFIG = "BARISTA_PORT";
    public static final String BARISTA_HOST_BUILD_CONFIG = "BARISTA_HOST";

    // the android task that runs the android instrumented tests of the project
    private String CONNECTED_ANDROID_TEST = "connectedDebugAndroidTest";
    private String INSTALL = "install";
    
    private final String ANDROID_EXTENSION_NAME = "android";
    private BaseAppModuleExtension androidExtension;

    private BaristaConfigurationExtension config;

    private Project project;

    private void registerStartServerTask(Task targetTask){
        project.getTasks().create(BaristaServerStartTask.NAME, BaristaServerStartTask.class);
        Task myCustomTask = project.getTasks().getByName(BaristaServerStartTask.NAME);
        targetTask.dependsOn(myCustomTask);

        myCustomTask.setGroup("Barista");
        myCustomTask.setDescription("Start barista server");
    }

    private void addAndroidBuildConfigField(String type, String name, String value){
        String configValue = value;
        if (type.equals("String")){
            configValue = "\"" + value + "\"";
        }
        androidExtension.getDefaultConfig().buildConfigField(type, name, configValue);
    }

    private void registerBaristaFuzzerStartTask() {

        for (String i : getVariants()) {

            project.getTasks().create(BaristaFuzzerStartTask.NAME + i, BaristaFuzzerStartTask.class);
            Task myCustomTask = project.getTasks().getByName(BaristaFuzzerStartTask.NAME +  i);

            Task targetTask = project.getTasks().getByName(INSTALL + i);

            myCustomTask.dependsOn(targetTask);

            project.getTasks().getByName(BaristaFuzzerStartTask.NAME + i).setGroup("Barista");
            project.getTasks().getByName(BaristaFuzzerStartTask.NAME + i).setDescription("Spawns a monkey on the variant " + i);
        }

    }

    private void registerMonkeyTask() {

        for (String i : getVariants()) {

            project.getTasks().create(MonkeyStartTask.NAME + i, MonkeyStartTask.class);
            Task myCustomTask = project.getTasks().getByName(MonkeyStartTask.NAME +  i);

            Task targetTask = project.getTasks().getByName(INSTALL + i);

            myCustomTask.dependsOn(targetTask);

            project.getTasks().getByName(MonkeyStartTask.NAME + i).setGroup("Barista");
            project.getTasks().getByName(MonkeyStartTask.NAME + i).setDescription("Spawns a monkey on the variant " + i);
        }

    }

    private List<String> getVariants() {
        DomainObjectSet<ApplicationVariant> variants = getAndroidExtension().getApplicationVariants();
        List<String> variantNames = new ArrayList<>();

        for (ApplicationVariant var: variants) {
            if (var.getName().toLowerCase().contains("release")) continue;

            variantNames.add(var.getName().substring(0, 1).toUpperCase() + var.getName().substring(1));
        }

        return variantNames;
    }

    /**
     *  Function that is called by gradle in order to intergrate the plugin inside the building process of the project
     *  The plugin firsts checks that the target project is an Android Project and that the gradle is about to run
     *  integration tests. Then it hooks its functionality at specific points in order to start and stop the server at the
     *  proper time.
     *
     * @param project
     */
    public void apply(Project project){

        this.project = project;
        // load the configuration extension
        project.getExtensions().create(BARISTA_PLUGIN_SETTINGS_SECTION, BaristaConfigurationExtension.class);

        // check if target project is an android project
        if(isAndroidProject()){

            BaristaLogger.print("Android project identified");

            project.afterEvaluate(new Action<Project>() {

                @Override
                public void execute(Project project) {

                    androidExtension = getAndroidExtension();

                    config = project.getExtensions().findByType(BaristaConfigurationExtension.class);

                    BaristaLogger.print("Configuration <host>: " + config.getHost());
                    BaristaLogger.print("Configuration <port>: " + config.getPort());
                    addAndroidBuildConfigField("Integer", BARISTA_PORT_BUILD_CONFIG, config.getPort().toString());
                    addAndroidBuildConfigField("String", BARISTA_HOST_BUILD_CONFIG, config.getHost());

                    EmulatorManager.setPackageName(getApplicationID());

                    // Hook start server task
                    Task connectedDebugAndroidTest = project.getTasks().findByPath(CONNECTED_ANDROID_TEST);
                    registerStartServerTask(connectedDebugAndroidTest);

                    registerMonkeyTask();
                    registerBaristaFuzzerStartTask();

                }
            });

        } else{    // If the target project is NOT an android project, do nothing
            BaristaLogger.print("Aborting, not an Android project.");
        }
    }

    /**
     * Function that determins if target project is an android project based on the imported plugins
     *
     * @return true if is android project, false if not
     */
    private boolean isAndroidProject() {

        final boolean isAndroidLibrary = this.project.getPlugins().hasPlugin("com.android.library");
        final boolean isAndroidApp = this.project.getPlugins().hasPlugin("com.android.application");
        final boolean isAndroidTest = this.project.getPlugins().hasPlugin("com.android.test");
        final boolean isAndroidFeature = this.project.getPlugins().hasPlugin("com.android.feature");
        final boolean isAndroidInstantApp = this.project.getPlugins().hasPlugin("com.android.instantapp");

        return isAndroidLibrary || isAndroidApp || isAndroidTest || isAndroidFeature || isAndroidInstantApp;

    }


    // Jersey server should be automaticaly shut down when connectedAndroidTest task is completed
    // todo There are two ways to run an  instrumented test. Must find how to close when using 2
    //  1.  from command lind with 'gradle connectedAndroidTest'
    //  2.  with adb using '$ adb shell am instrument -w <test_package_name>/<runner_class>'
    // Curently the plugin shuts down the server if the first way is used.
    /**
     *
     */
    private void hookServerStopTask(){

        BaristaLogger.print("Hooking task for stopping server");

        Task targetTask = project.getTasks().findByPath(CONNECTED_ANDROID_TEST);

        targetTask.doLast("shutDownServer",new Action<Task>() {

            @Override
            public void execute(Task task) {
                BaristaLogger.print("Closing Server");
                HttpServerManager.getInstance().forceKillServer();
            }
        });

    }

    private String getApplicationID(){
        BaseAppModuleExtension androidExtension= getAndroidExtension();
        return androidExtension.getDefaultConfig().getApplicationId();
    }

    /**
     * Function that returns the android extension of the android project.
     * This will expose the configurations given by the developer inside the android gradle.build block
     * @return
     */
    private BaseAppModuleExtension getAndroidExtension(){

        Object o = project.getExtensions().findByName(this.ANDROID_EXTENSION_NAME);
        BaristaLogger.print("Loading android module extension: "+o.getClass().getName());
        if(o instanceof com.android.build.gradle.internal.dsl.BaseAppModuleExtension){
            return (BaseAppModuleExtension) project.getExtensions().findByName(this.ANDROID_EXTENSION_NAME);
        }

        return null;
    }

    /**
     * todo DEBUG-ONLY
     */
    private void scanProject(){
        int projectNum = project.getAllprojects().size();
        BaristaLogger.print("Number of Projects: "+projectNum);
    }

}
