package gr.aueb.android.barista.core.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gr.aueb.android.barista.core.executor.CommandClient;
import gr.aueb.android.barista.utilities.BaristaCommandPrefixes;
import gr.aueb.android.barista.utilities.BaristaLogger;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

@JsonTypeName("Monkey")
public class Monkey extends AbstractAdbCommand {

    private String command;

    private boolean completed;

    private int seed, count, throttle;
    private String apk;

    public Monkey() {

    }

    public Monkey(String sessionToken, String command) {
        super(sessionToken);
        this.command = command;
        this.completed = false;
    }

    public Monkey(String sessionToken, int seed, int count, int throttle, String apk) {
        super(sessionToken);
        this.seed = seed;
        this.count = count;
        this.throttle = throttle;
        this.apk = apk;
    }

    @Override
    public String getCommandString() {
        if (this.command != null) {
            return this.command;
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(BaristaCommandPrefixes.MONKEY).append(" ").append("-p ").append(this.apk);
        buffer.append(" -c android.intent.category.LAUNCHER -c android.intent.category.MONKEY");
        buffer.append(" ").append("-s ").append(this.seed);
        buffer.append(" ").append("-v");
        buffer.append(" ").append("--throttle ").append(this.throttle).append(" ").append(this.count);
        buffer.append(" --pct-appswitch 0");
        buffer.append(" --pct-syskeys 0");
        buffer.append(" --pct-appswitch 0");
        buffer.append(" --pct-majornav 0");
        String command = buffer.toString();
        return command;
    }

    @Override
    public void parseResult(Stream<String> resultLines) {
        resultLines.filter(new Predicate<String>() {
            @Override
            public boolean test(String line) {
                return line.contains("Monkey aborted due to error") || line.contains("Monkey finished") || line.contains("CRASHED");
            }
        }).forEach(new Consumer<String>() {
            @Override
            public void accept(String line) {
                completed = true;
            }
        });
    }

    @Override
    public boolean isCompleted(CommandClient client) {
        BaristaLogger.print("Completed");
        return this.completed;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getThrottle() {
        return throttle;
    }

    public void setThrottle(int throttle) {
        this.throttle = throttle;
    }

    public String getApk() {
        return apk;
    }

    public void setApk(String apk) {
        this.apk = apk;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
