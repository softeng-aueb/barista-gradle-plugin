package gr.aueb.android.barista.fuzzer.runner;

import gr.aueb.android.barista.core.executor.CommandExecutor;
import gr.aueb.android.barista.core.executor.CommandExecutorFactory;
import gr.aueb.android.barista.core.executor.CommandExecutorImpl;
import gr.aueb.android.barista.core.model.*;
import gr.aueb.android.barista.utilities.BaristaLogger;

import java.util.List;

public class SequentialRunner implements Runner {

    private List<Command> commands;
    private CommandExecutor executor;
    private LogcatCrash crashReporter;
    private AppSwitch appSwitch;
    private SwipeUp swipeUp;
    private Pull pull;

    public SequentialRunner(List<Command> commands, LogcatCrash crashReporter, AppSwitch appSwitch, SwipeUp swipeUp, Pull pull) {
        this.commands = commands;
        this.executor = (CommandExecutorImpl) CommandExecutorFactory.getCommandExecutor();
        this.crashReporter = crashReporter;
        this.appSwitch = appSwitch;
        this.swipeUp = swipeUp;
        this.pull = pull;
    }

    @Override
    public void start() {
        for (Command cmd : this.commands) {
            this.executor.executeCommand(cmd);
            this.executor.executeCommand(this.crashReporter);
            if (this.crashReporter.hasCrashed()) break;
        }

        BaristaLogger.printList(this.crashReporter.getCrashLog());
        BaristaLogger.writeCrashLog(this.crashReporter.getCrashLog());
        this.executor.executeCommand(this.appSwitch);
        this.executor.executeAdbCommand(this.swipeUp);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.executor.executeCommand(this.pull);
    }


}
