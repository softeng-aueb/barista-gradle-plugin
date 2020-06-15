package gr.aueb.android.barista.fuzzer;

import gr.aueb.android.barista.core.model.Command;
import gr.aueb.android.barista.core.model.Monkey;
import gr.aueb.android.barista.utilities.BaristaCommandPrefixes;

import java.util.ArrayList;
import java.util.List;

public class MonkeyEventGenerator implements EventGenerator{

    private int seed, count, throttle;
    private String apk, token;
    private Monkey monkey;

    public MonkeyEventGenerator(int seed, int count, int throttle, String apk, String token) {
        this.seed = seed;
        this.count = count;
        this.throttle = throttle;
        this.apk = apk;
        this.token = token;
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

    private String createMonkeyCommand() {
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

    public List<Command> generate() {
        Monkey monkey = new Monkey(this.token, this.createMonkeyCommand());
        monkey.setApk(this.apk);
        monkey.setSeed(this.seed);
        monkey.setThrottle(this.throttle);
        monkey.setCount(this.count);
        List<Command> toExecute = new ArrayList<>();
        toExecute.add(monkey);
        return toExecute;
    }
}
