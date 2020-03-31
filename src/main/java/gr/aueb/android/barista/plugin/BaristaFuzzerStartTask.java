package gr.aueb.android.barista.plugin;

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension;
import gr.aueb.android.barista.fuzzer.FuzzScheduler;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;

public class BaristaFuzzerStartTask extends DefaultTask {

    private int eventsPerEpoch, throttle, epochs;
    private String apk;

    public static final String NAME = "startBaristaFuzzer";

    @Option(option = "events", description = "Events per epoch")
    public void setEventsPerEpoch(String eventsPerEpoch) {
        this.eventsPerEpoch = Integer.parseInt(eventsPerEpoch);
    }

    @Input
    public int getEventsPerEpoch() {
        return eventsPerEpoch;
    }

    @Option(option = "epochs", description = "Number of epochs")
    public void setEpochs(String epochs) {
        this.epochs = Integer.parseInt(epochs);
    }

    @Input
    public int getEpochs() {
        return epochs;
    }

    @Option(option = "throttle", description = "Throttle of testing")
    public void setThrottle(String throttle) {
        this.throttle = Integer.parseInt(throttle);
    }

    @Input
    public int getThrottle() {
        return throttle;
    }

    @TaskAction
    public void action() {
        BaseAppModuleExtension android = (BaseAppModuleExtension) this.getProject().getExtensions().findByName("android");
        this.apk = android.getDefaultConfig().getApplicationId();

        FuzzScheduler fuzzer = new FuzzScheduler(this.epochs, this.throttle, this.eventsPerEpoch, this.apk);
        fuzzer.initialize(true, false);
        fuzzer.start();
    }
}
