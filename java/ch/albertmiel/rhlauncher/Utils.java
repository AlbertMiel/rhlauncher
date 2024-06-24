package ch.albertmiel.rhlauncher;

import org.apache.commons.lang3.SystemUtils;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class Utils {
    static String sep;
    static String javaRuntime;
    static public int ram = Integer.valueOf(Properties.getProperty("ram"));

    static void downloadFile(String filePath, String destPath) {
        try {
            OutputStream fileOutputStream = new FileOutputStream(destPath);
            DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox").build();
            DbxClientV2 client = new DbxClientV2(config, Properties.getProperty("dropbox-token"));
            //download file
            Logger.log("Downloading File " + filePath + "...");
            client.files().downloadBuilder(filePath).download(fileOutputStream);
            Logger.log("File " + filePath + " downloaded successfully!");
        }
        catch(Exception e) {
            Logger.log(e.getMessage(), false);
        }
    }

    /*static void unzipFile(String zipFilePath, String zipDestPath) {
        try {
            //extract zip from zipfileName to modFolder
            Logger.log("Extracting zipfile");
            ZipInputStream zipfileInput = new ZipInputStream(new FileInputStream(zipFilePath)); // + sep + zipfileDropboxName.replace(".zip", "")));
            ZipEntry child;
            String childDestPath;
            while((child = zipfileInput.getNextEntry()) != null) {
                //extractFile(zipfileInput, modFolder, fileInZipfile.getName());
                childDestPath = zipDestPath + sep + child.getName();
                try(OutputStream childOutput = new FileOutputStream(childDestPath)) {
                    byte[] buffer = new byte[4096];
                    int len;
                    while ((len = zipfileInput.read(buffer)) > 0) {
                        childOutput.write(buffer, 0, len);
                    }
                } catch (Exception e) {
                    Logger.log(e.getMessage(), false);
                }

                zipfileInput.closeEntry();
            }
            zipfileInput.close();
        }
        catch(Exception e) {
            Logger.log(e.getMessage());
        }
    }*/

    static void setOsDifferences() {
        sep = "/";
        File releasesFolder = new File(".minecraft/mods/releases");
        if(!releasesFolder.exists()) {releasesFolder.mkdirs();}
        if(SystemUtils.IS_OS_WINDOWS) {
            sep = "\\";
            javaRuntime = Properties.getProperty("runtime-folder") + "\\jdk-17-win\\bin\\javaw.exe";
            //if(!new File(".runtime\\jdk-17-win.zip").exists()) {downloadFile("/rhodium/jdk-17-win.zip", ".runtime\\jdk-17-win.zip");}
            //if(!new File(".runtime\\jdk-17-win").exists()) {unzipFile(".runtime\\jdk-17-win.zip", ".runtime");}
        }
        else if(SystemUtils.IS_OS_MAC) {
            javaRuntime = Properties.getProperty("runtime-folder") + "/jdk-17-macos/Contents/Home/bin/java";
        }
        else if(SystemUtils.IS_OS_LINUX) {
            javaRuntime = Properties.getProperty("runtime-folder") + "/jdk-17-linux/bin/java";            
        }
    }
    static String getOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win")) {
            return "windows";
        }
        else if(os.contains("mac")) {
            return "macos";
        }
        else if(os.contains("nux") || os.contains("nix")) {
            return "linux";
        }
        else {
            return "unkown";
        }
    }
    
    public static void changeRAM(int ram2) {
        Utils.ram = ram2;
        Properties.setProperty("ram", String.valueOf(ram2));
    }

    static void mainMenu() {
        Scanner scanner12 = new Scanner(System.in);
        System.out.println("Please choose an option : \n" 
        + "1: Log in with a MS account \n"
        + "2: Log in with a cracked account, random UUID \n"
        + "3: Log in with a cracked account and a custom UUID \n"
        + "12: Launch Rhodium!\n"
        + "5: Log out and delete your private infos\n");
        switch(scanner12.nextLine()) {
        //switch("4") {
            case "1":
                Login.MSLogin();
                mainMenu();
                break;
            case "2":
                Logger.log("Please choose a pseudo: ");
                Login.CrackLogin(scanner12.next());
                mainMenu();
                break;
            case "3":
                Logger.log("Please choose a pseudo: ");
                String pseudo = scanner12.nextLine();
                Logger.log("And a UUID: ");
                String uuid = scanner12.nextLine();
                Login.CrackLogin(pseudo, uuid);
                mainMenu();
                break;
            case "12":
                MinecraftLauncher.launch("1.19.2", "forge", "43.3.0");
                break;
            case "5":
                Login.logout();
                break;
            default:
                Logger.log("Invalid choice - Exiting...");
                break;
        }
        scanner12.close();
    }
}
