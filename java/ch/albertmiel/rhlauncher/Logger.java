package ch.albertmiel.rhlauncher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    static void log(String text, String infoType) {
        try {
            //Message creation
            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMMyyyy"));
            String prefix = "[RH LAUNCHER]";
            String typeOfInfo = "[" + infoType + "]";
            String message = "[" + time + "] " + prefix + " " + typeOfInfo + ": " + text;

            //Message in console
            System.out.println(message);

            //Message in log file
            message = "[" + date + " " + time + "] " + prefix + " " + typeOfInfo + ": " + text;

            String fileWithExtention = Properties.getProperty("log-file");

            File file = new File(fileWithExtention); 
            if(!file.exists()) {file.createNewFile();}

            BufferedReader reader = new BufferedReader(new FileReader(fileWithExtention));
            String line;
            String logText = "";
            while((line = reader.readLine()) != null) {logText += line + "\n";}

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileWithExtention));
            writer.write(logText + message);
            writer.close();
            reader.close();
        }
        catch(Exception e) {
            Logger.log(e.getMessage(), false);
        }

    }

    static void log(String text) {
        log(text, true);
    }

    static void log(String text, Boolean info) {
        if(info) {
            log(text, "INFO");
        }
        else {
            log(text, "ERROR");
        }
    }
}
