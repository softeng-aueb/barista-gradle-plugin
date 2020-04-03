package gr.aueb.android.barista.core.context.model;

import gr.aueb.android.barista.core.executor.CommandExecutorFactory;
import gr.aueb.android.barista.core.executor.CommandExecutorImpl;
import gr.aueb.android.barista.core.model.BatteryCharge;
import gr.aueb.android.barista.core.model.BatteryLevel;
import gr.aueb.android.barista.core.model.BatteryStatus;
import gr.aueb.android.barista.core.model.Command;
import gr.aueb.android.barista.utilities.BaristaLogger;

import java.util.Random;

public class BatteryDrainModel implements ContextModel {

    private Command batteryCharge;
    private Command batteryLevel;
    private Command batteryStatus;
    private String token;

    public BatteryDrainModel(String token) {
        this.token = token;
    }

    /*@Override
    public void execute() {
        new Thread(() -> {
            CommandExecutorImpl executor = (CommandExecutorImpl) CommandExecutorFactory.getCommandExecutor();
            while (!this.stop) {
                this.batteryStatus = new BatteryStatus(this.token);
                executeCommand(this.batteryStatus, executor);

                if (((BatteryStatus) this.batteryStatus).getLevel() < 10) {
                    this.batteryCharge = new BatteryCharge(this.token, true);
                    executeCommand(this.batteryCharge, executor);
                    this.batteryLevel = new BatteryLevel(this.token, 100);
                    executeCommand(this.batteryLevel, executor);
                    this.batteryCharge = new BatteryCharge(this.token, false);
                    executeCommand(this.batteryCharge, executor);
                }
                if (((BatteryStatus) this.batteryStatus).getLevel() == 100) {
                    this.batteryCharge = new BatteryCharge(this.token, false);
                    executeCommand(this.batteryCharge, executor);
                }

                if (!((BatteryStatus) this.batteryStatus).getChargingStatus()) {
                    this.batteryStatus = new BatteryStatus(token);
                    executeCommand(this.batteryStatus, executor);
                    int currentLevel = ((BatteryStatus) this.batteryStatus).getLevel();
                    int finalLevel = currentLevel - this.generateRandomInt(0, 4);

                    this.batteryLevel = new BatteryLevel(this.token,finalLevel);
                    executeCommand(this.batteryLevel, executor);
                }
                try {
                    Thread.sleep(this.generateRandomInt(15, 45)*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }*/

    private void executeCommand(Command cmd, CommandExecutorImpl executor) {
        executor.executeCommand(cmd);
        BaristaLogger.print("Executing battery command");
    }

    @Override
    public Command next(long elapsedTimeMillis) {
        CommandExecutorImpl executor = (CommandExecutorImpl) CommandExecutorFactory.getCommandExecutor();
        this.batteryStatus = new BatteryStatus(this.token);
        executeCommand(this.batteryStatus, executor);

        if (((BatteryStatus) this.batteryStatus).getLevel() < 10 && !((BatteryStatus) this.batteryStatus).getChargingStatus()) {
            return new BatteryCharge(this.token, true);
        }

        if (((BatteryStatus) this.batteryStatus).getLevel() == 100 && ((BatteryStatus) this.batteryStatus).getChargingStatus()) {
            return new BatteryCharge(this.token, false);
        }

        if (((BatteryStatus) this.batteryStatus).getChargingStatus()) {
            int currentLevel = ((BatteryStatus) this.batteryStatus).getLevel();
            int finalLevel = currentLevel - this.generateRandomInt(0, 4);

            return new BatteryLevel(this.token,finalLevel);
        }

        return new BatteryStatus(this.token);
    }

    private int generateRandomInt(int min, int max) {
        Random random = new Random();

        int number = random.nextInt((max - min) + 1) + min;

        return number;
    }
}