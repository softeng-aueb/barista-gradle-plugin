package gr.aueb.android.barista.fuzzer;

import gr.aueb.android.barista.core.context.EnumTypes;
import gr.aueb.android.barista.core.context.ContextModelFactory;
import gr.aueb.android.barista.core.context.RandomWalkModel;
import gr.aueb.android.barista.core.executor.CommandExecutor;
import gr.aueb.android.barista.core.executor.CommandExecutorFactory;
import gr.aueb.android.barista.core.executor.CommandExecutorImpl;
import gr.aueb.android.barista.core.model.*;
import gr.aueb.android.barista.core.emulator.EmulatorManager;
import gr.aueb.android.barista.fuzzer.runner.ParallelRunner;
import gr.aueb.android.barista.fuzzer.runner.Runner;
import gr.aueb.android.barista.fuzzer.runner.SequentialRunner;
import gr.aueb.android.barista.utilities.CommandExporter;
import gr.aueb.android.barista.utilities.PropertiesReader;
import groovy.sql.Sql;

import java.util.ArrayList;
import java.util.List;

public class FuzzScheduler {

    private Runner runner;

    private MonkeyEventGenerator monkey;
    private ContextEventGenerator context;
    private CommandExecutor executor;
    private ContextModelFactory ctxFactory;
    private PropertiesReader reader;
    private LogcatCrash crashReporter;

    private List<Command> commandsToExecute;
    private List<EventGenerator> eventGenerators;
    private List<Command> monkeyCommands;
    private List<Command> contextCommands;

    private int epochs, throttle, batchSize;
    private String apk, token, conf, input;

    public FuzzScheduler(int epochs, int throttle, int batchSize, String apk, String conf, String input) {
        this.epochs = epochs;
        this.throttle = throttle;
        this.apk = apk;
        this.batchSize = batchSize;
        this.conf = conf;
        this.input = input;

        this.commandsToExecute = new ArrayList<>();
        this.eventGenerators = new ArrayList<>();
        this.monkeyCommands = new ArrayList<>();
        this.contextCommands = new ArrayList<>();
    }

    public void initialize(boolean context, boolean parallel) {
        this.reader = new PropertiesReader(this.conf);
        this.reader.loadProperties();

        this.executor = (CommandExecutorImpl) CommandExecutorFactory.getCommandExecutor();
        this.token = EmulatorManager.getManager().getTokenMap().keySet().iterator().next();
        if (context) this.initializeContext();
        this.initializeMonkey();

        this.executor.executeCommand(new LogcatCrashClear(token));
        this.crashReporter = new LogcatCrash(token, apk);
//        Instant start = Instant.now();

        AppSwitch appSwitch = new AppSwitch(token);
        SwipeUp swipeUp = new SwipeUp(token);
        Pull pull = new Pull(token, "/sdcard/coverage.exec");

        if (this.input == null) {
            for (int i = 0; i < this.epochs; i++) {
                for (EventGenerator eg: this.eventGenerators) {
                    List<Command> commands = eg.generate();
                    this.commandsToExecute.addAll(commands);
                }
            }

            CommandExporter.export(this.commandsToExecute);

            for (Command cmd : this.commandsToExecute) {
                if (cmd instanceof Monkey) {
                    this.monkeyCommands.add(cmd);
                }
                else {
                    this.contextCommands.add(cmd);
                }
            }
        }
        else {
            CommandImporter importer = new CommandImporter(this.input, this.token);
            this.commandsToExecute.addAll(importer.getCommandList());
        }

        List<Command> emulatorReset = resetEmulator(this.token);

        if (parallel) {
            this.runner = new ParallelRunner(this.monkeyCommands,
                                            (ContextEventGenerator) this.eventGenerators.get(this.eventGenerators.indexOf(this.context)),
                                            this.crashReporter,
                                            appSwitch, swipeUp, pull, emulatorReset);
        }
        else {
            this.runner = new SequentialRunner(this.commandsToExecute, this.crashReporter, appSwitch, swipeUp, pull, emulatorReset);
        }
    }

    private void initializeMonkey() {
        this.eventGenerators.add(new MonkeyEventGenerator(this.reader.getMonkeySeed(), this.batchSize, this.throttle, this.apk, this.token));
    }

    private void initializeContext() {
        this.ctxFactory = new ContextModelFactory(this.token);

        this.context = new ContextEventGenerator();

        if (this.reader.getModel("fuzzer.poorConnectivity")) {
            this.context.register(this.ctxFactory.getConnectivityModel(EnumTypes.ConnectivityType.POOR));
        }

        if (this.reader.getModel("fuzzer.randomConnectivity")) {
            this.context.register(this.ctxFactory.getConnectivityModel(EnumTypes.ConnectivityType.RANDOM));
        }

        if (this.reader.getModel("fuzzer.fuzzGPS")) {
            this.context.register(this.ctxFactory.getConnectivityModel(EnumTypes.ConnectivityType.GPS));
        }

        if (this.reader.getModel("fuzzer.walkModel")) {
            RandomWalkModel walkModel = (RandomWalkModel) this.ctxFactory.getNavigationModel(EnumTypes.NavigationType.RANDOM_WALK);
            walkModel.setLat(this.reader.getWalkLatitude());
            walkModel.setLongt(this.reader.getWalkLongitude());

            this.context.register(walkModel);
        }

        if (this.reader.getModel("fuzzer.randomMovement")) {
            this.context.register(this.ctxFactory.getNavigationModel(EnumTypes.NavigationType.FUZZ));
        }

        if (this.reader.getModel("fuzzer.batteryDrain")) {
            this.context.register(this.ctxFactory.getBatteryDrainModel());
        }

        this.eventGenerators.add(this.context);
    }

    private List<Command> resetEmulator(String token) {
        List<Command> emulatorReset = new ArrayList<>();

        Command wifiOn = new SvcWifi(token, true);
        Command dataOn = new SvcData(token, true);
        Command gpsOn = new GpsState(token, true);
        Command batteryCharging = new BatteryCharge(token, false);
        Command battery100 = new BatteryLevel(token, 100);
        Command rmCoverage = new Rm(token, "sdcard/coverage.exec");

        emulatorReset.add(wifiOn);
        emulatorReset.add(dataOn);
        emulatorReset.add(gpsOn);
        emulatorReset.add(batteryCharging);
        emulatorReset.add(battery100);
        emulatorReset.add(rmCoverage);

        return emulatorReset;
    }

    public void start() {
        this.runner.start();
    }

}
