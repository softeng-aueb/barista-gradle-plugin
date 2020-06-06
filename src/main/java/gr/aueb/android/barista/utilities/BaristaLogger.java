/**
 * Author: Tsiskomichelis Stelios
 * Created On: 21/1/2019
 * Project: barista-plugin
 * <p>
 * ClassName: BaristaLoger
 * Role:  Prints Barista log messages when testing
 * Description:
 */
package gr.aueb.android.barista.utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BaristaLogger {

    private static final String path = "crashLog/";

    /**
     *  Prints a String at the console with the prifix '[BARISTA-PLUGIN]'
     *
     * @param str
     */
    public static void print(String str){
        System.out.println("[BARISTA-PLUGIN] "+str);
    }

    public static void printList(List<String> list) {
        if (list != null) list.forEach(BaristaLogger::print);
    }

    public static void writeCrashLog(List<String> list) {
        if (list != null) {
            String time = new SimpleDateFormat("yyyy-MM-dd HH-mm").format(new Date());
            String name = time + "-barista-fuzzer-crash.log";

            new File(path).mkdirs();

            try {
                FileWriter writer = new FileWriter(path + name);

                list.forEach(i -> {
                    try {
                        writer.write(i + System.lineSeparator());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
