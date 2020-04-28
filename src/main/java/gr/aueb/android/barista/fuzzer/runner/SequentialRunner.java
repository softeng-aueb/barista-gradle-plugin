package gr.aueb.android.barista.fuzzer.runner;

import gr.aueb.android.barista.core.executor.CommandExecutor;
import gr.aueb.android.barista.core.executor.CommandExecutorFactory;
import gr.aueb.android.barista.core.executor.CommandExecutorImpl;
import gr.aueb.android.barista.core.model.Command;
import gr.aueb.android.barista.core.model.LogcatCrash;
import gr.aueb.android.barista.utilities.BaristaLogger;

import java.util.List;

public class SequentialRunner implements Runner {

    private List<Command> commands;
    private CommandExecutor executor;
    private LogcatCrash crashReporter;

    public SequentialRunner(List<Command> commands, LogcatCrash crashReporter) {
        this.commands = commands;
        this.executor = (CommandExecutorImpl) CommandExecutorFactory.getCommandExecutor();
        this.crashReporter = crashReporter;
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
    }


}