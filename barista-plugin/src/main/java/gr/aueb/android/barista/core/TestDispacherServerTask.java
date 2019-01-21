/**
 * Author: Tsiskomichelis Stelios
 * Created On: 17/10/2018
 * Project: barista-plugin
 * <p>
 * ClassName: TestDispacherServerTask
 * Role: Implements the task that deploys the adb test dispacher
 * Description:
 */
package gr.aueb.android.barista.core;

import org.gradle.api.DefaultTask;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import gr.aueb.android.barista.server.HttpServerManager;

/**
 * todo NOT USED
 */
public class TestDispacherServerTask extends DefaultTask {
    private final String DEFAULT_SERVER_URL = "http://localhost:8080";

    private String serverURL = DEFAULT_SERVER_URL;

    @TaskAction
    public void deployServer(){

        getProject().getLogger().log(LogLevel.LIFECYCLE,this.getPath());
        //getProject().getLogger().log(LogLevel.);
        System.out.println("Deploying Server at: "+HttpServerManager.getBaseUri());
        HttpServerManager.startServer();
    }

    @TaskAction
    public void stopServer(){
        System.out.println("Shuting down Server at: "+HttpServerManager.getBaseUri());
        HttpServerManager.stopServer();
    }


}
