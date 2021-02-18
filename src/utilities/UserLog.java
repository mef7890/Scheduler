package utilities;

import controller.main.MainController;

import java.io.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class UserLog {

    public static void updateLog()  {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd @ H:mm:ss");
        String userTimestampUTC = TimeConverter.toUTC(MainController.actionTimestamp).format(formatter);
        String userTimestampUserLocal = MainController.actionTimestamp.format(formatter);

        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(new File("log.txt"), true));
            // append = true
            writer.append("User: " + MainController.currentUser + "\t\t\t " + userTimestampUTC + " UTC \n");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void actionLog(String action)  {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd @ H:mm:ss");
        String userTimestampUTC = TimeConverter.toUTC(MainController.actionTimestamp).format(formatter);
        String userTimestampUserLocal = MainController.actionTimestamp.format(formatter);

        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(new File("action_log.txt"), true));
            writer.append("User: " + MainController.currentUser + "\t\t\t\t " +
                    "Action: " + action + "\t\t\t\t On: " + userTimestampUserLocal + " " + ZoneId.systemDefault().toString() +
                    "\t\t" + userTimestampUTC + " UTC " +
                    "\n");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
