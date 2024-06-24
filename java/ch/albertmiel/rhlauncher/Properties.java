package ch.albertmiel.rhlauncher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Properties {

    static void setProperty(String fileName, String key, String text) {
        try {
            String fileWithExtention = fileName;
            File file = new File(fileWithExtention); 
            if(!file.exists()) {
                file.createNewFile();
            }

            BufferedReader reader = new BufferedReader(new FileReader(fileWithExtention));
            String fileText = "";
            Boolean fileWrited = false;

            String line = reader.readLine();
            fileText += line;
            String actualKey;
            while((line = reader.readLine()) != null) {
                actualKey = line.split(": ")[0];
                if(actualKey.equals(key)) {
                    fileText += "\n" + key + ": " + text + ";";
                    fileWrited = true;
                }
                else {
                    fileText += "\n" + line;
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileWithExtention));
            if(!fileWrited) {fileText += "\n" + key + ": " + text + ";";}
            writer.write(fileText);
            writer.close();
            reader.close();
        }
        catch (IOException e) {
            Logger.log(e.getMessage(), false);
        }
    }

    static void setProperty(String key, String text) {
        setProperty("settings.rhlauncherdata", key, text);
    }

    static String getProperty(String fileName, String key) {
        String fileWithExtention = fileName;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileWithExtention));
            String line;
            String actualKey;
            String lineToReturn = null;
            while((line = reader.readLine()) != null) {
                actualKey = line.split(": ")[0];
                if(actualKey.equals(key)) {
                    lineToReturn = line;
                    break;
                }
            }
            reader.close();
            if(!lineToReturn.equals(null)) {return lineToReturn.split(": ")[1].split(";")[0];}
        }
        catch(IOException e) {
            Logger.log(e.getMessage(), false);
        }
        Logger.log("The property \"" + key + "\" dosen't exists", false);
        return null;
    }

    static String getProperty(String key) {
        return getProperty("settings.rhlauncherdata", key);
    }

}
