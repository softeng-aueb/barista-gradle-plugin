/**
 * Author: Tsiskomichelis Stelios
 * Created On: 28/1/2019
 * Project: barista-plugin
 * <p>
 * ClassName: ADBCommandClient
 * Role: Executes ADB commands
 * Description: ADBCommandClient implements the CommandClient interface and it responsible
 *              for executing all the adb commands. In addition it verifies the execution of
 *              any command by using the isComplete() member function of the Command interface.
 *
 */
package gr.aueb.android.barista.core.executor;

import gr.aueb.android.barista.core.model.Command;
import gr.aueb.android.barista.emulator.EmulatorManager;
import gr.aueb.android.barista.utilities.BaristaLogger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ADBCommandClient implements CommandClient {
    /**
     * Prefix String for adb console commands
     */
    private final String ADB_COMMAND = "adb";

    /**
     * Executes and verifies the execution of an ADB command. It uses the ProcessBuilder to create system processes.
     * All the commands executed have the form 'adb -s [device_id] [command] [args]'.
     * To route the command to the apropriate emulator, -s flag is used and the device_id is acquired from the EmulatorManager
     * by using the sessionToken encapsulated in each Command instance.
     *
     * This function is terminated only when the execution of the command is verified or if an exeption occurs during
     * the execution of the process.
     *
     * @param cmd The command to be executed. It is asserted that cmd will always be instance of AbstractAdbCommand
     */
    @Override
    public void executeCommand(Command cmd) {

        // get the sessionID from the command
        String token = cmd.getSessionToken();

        // get the command string to be executed
        String strCommand = cmd.getCommandString();

        // acquire the device id by using the sessionToken
        String deviceId = EmulatorManager.getManager().verifyToken(token);

        // constuct the argument list
        List<String> commandList = new ArrayList<>();
        commandList.add(ADB_COMMAND);
        commandList.add("-s");
        commandList.add(deviceId);

        String[] coreCommand = strCommand.split(" ");
        for (String subCommand: coreCommand) {
            commandList.add(subCommand);
        }

        // print message to the console that a command is about to be executed
        BaristaLogger.print("Executing ADB command: "+strCommand+" for emulator: "+deviceId);

        try {

            // construct a process to execute the command
            ProcessBuilder pb = new ProcessBuilder();
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);
            pb.command(commandList);

            // execute the command and wait for its execution
            Process p = pb.start();
            pb.redirectErrorStream(true);

            // read the output (if any) and pass it tho the result parser of the Command. It is used
            // when the command output is needed.
            BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()));
            /*String line;
            while ((line = output.readLine()) != null)
                System.out.println("tasklist: " + line);*/
            Stream<String> resultStream = output.lines();

            cmd.parseResult(resultStream);
            output.close();

            p.waitFor();

            // wait until the Command verifies its execution
            while (!cmd.isCompleted(this)){
                BaristaLogger.print("Waiting for command completion");
                Thread.sleep(500);
            }

        } catch (IOException e) {
            BaristaLogger.print("Exception occurred: "+e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
