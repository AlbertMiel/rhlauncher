package ch.albertmiel.rhlauncher;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
//import com.dropbox.core.v2.files.FileMetadata;
//import com.dropbox.core.v2.files.ListFolderResult;
//import com.dropbox.core.v2.files.Metadata;

public class Updater {
    static String latestVersionFileName;
    static String latestVersionKey;
    static String latestVersion;
    static String modFolderPath;
    static String installedVersion;
    static OutputStream latestVersionFileOutput;
    static String latestVersionPath;
    static String dropboxToken;
    static DbxRequestConfig config;
    static DbxClientV2 client;
    static Boolean isVersionFileDownloaded;
    private static void initVars() {
        dropboxToken = Properties.getProperty("dropbox-token");
        latestVersionFileName = Properties.getProperty("dropbox-latest-version-file");
        latestVersionPath = "/rhodium/" + latestVersionFileName;
        latestVersionKey = Properties.getProperty("dropbox-latest-version-key");
        try {
            latestVersionFileOutput = new FileOutputStream(latestVersionFileName);
            config = DbxRequestConfig.newBuilder("dropbox").build();
            client = new DbxClientV2(config, dropboxToken);
        } catch(Exception e) {
            Logger.log(e.getMessage(), false);
        }
        modFolderPath = Properties.getProperty("game-directory") + "/mods";
        installedVersion = Properties.getProperty("installed-version");
        latestVersion = null;
    }


    static void CheckUpdate() {
        initVars();

        //Try downloading version file to see if the dbx token is up-to-date
        try {latestVersion = getLatestVersion(client, latestVersionPath);} catch(Exception e) {Logger.log(e.getMessage(), false);}

        if(latestVersion == null) { //If the dbx token is invalid
            Logger.log("Error detected! Your dropbox token is invalid - generating new token...");

            final String dropboxAppKey = "mhy3ljwad29e62d";
            final String dropboxAppSecret = "4iolkr8pswomc4y";
            final String dropboxAuthString = dropboxAppKey + ":" + dropboxAppSecret;
            //final String dropboxAccessCode = "qrGRW5cyQqsAAAAAAAAAKGZml7hPu-D62VjkOVjL-UQ";
            final String dropboxRefreshToken = "5NMU84Obd2YAAAAAAAAAAawe189k3FhZcXWmgUW544p19ZTHYMHP9eEqx6Qh8ygV";
            //String getRefreshTokenCommand = "curl https://api.dropbox.com/oauth2/token -d code=" + dropboxAccessCode + " -d grant_type=authorization_code -u " + dropboxAuthString;
            String getTokenCommand = "curl https://api.dropbox.com/oauth2/token -d grant_type=refresh_token -d refresh_token=" + dropboxRefreshToken + " -u " + dropboxAuthString;

            try {
                //Execute command
                String commandResult = "";
                Process process = Runtime.getRuntime().exec(getTokenCommand);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String readline;
                    while ((readline = reader.readLine()) != null) {
                    commandResult += readline;
                }
                process.waitFor();
                
                //modify dropboxToken
                String[] commandResults = commandResult.split(", ");
                for (String arg : commandResults) {
                    if(!arg.replace("access_token", "").equals(arg)) {
                        dropboxToken = arg;
                        break;
                    }
                }
                dropboxToken = dropboxToken.split(": ")[1].replace("\"", "");
                Properties.setProperty("dropbox-token", dropboxToken);
                client = new DbxClientV2(config, dropboxToken);
                
                latestVersion = getLatestVersion(client, latestVersionPath);

                
            } catch(Exception e) {
                Logger.log(e.getMessage(), false);
            }

        }
        else {
            Logger.log("Your dropbox token is up-to-date!");
        }


        //Download new modpack if latest version does not match the installed version
        try {


            if(installedVersion.equals(latestVersion)) {
                Logger.log("Latest version already installed!");
            }
            else {
                downloadMods(client, "modpack-" + latestVersion + ".zip");
            }

        }
        catch(Exception e) {
            Logger.log(e.getMessage(), false);
        }
        
    }

    public static String getLatestVersion(DbxClientV2 client, String versionFilePath) throws DbxException, IOException {
        Logger.log("Checking installed version...");
        try {
            
            File latestVersionFile = new File(latestVersionFileName);
            
            //download file
            Logger.log("Downloading Version File...");
            client.files().downloadBuilder(versionFilePath).download(latestVersionFileOutput);
            Logger.log("Version File downloaded successfully!");

            //get the latest version String
            String latestVersion = Properties.getProperty(latestVersionFileName, latestVersionKey);

            //remove file
            latestVersionFile.delete();

            return latestVersion;
        }
        catch(Exception e) {
            Logger.log(e.getMessage(), false);
            return null;
        }
    }

    public static void downloadMods(DbxClientV2 client, String zipfileDropboxName) {
        String sep = "\\";
        String zipfileDropboxPath = "/rhodium/" + zipfileDropboxName;
        String zipfileDestPath = modFolderPath + "/releases/modpack-".replace("/", sep) + latestVersion + ".zip";
        try {
            
            File zipfile = new File(zipfileDestPath);
            //download zipfile
            if(!zipfile.exists()) {
                Logger.log("Downloading mods " + latestVersion + " (please wait)");
                OutputStream zipfileOutput = new FileOutputStream(zipfileDestPath);
                client.files().downloadBuilder(zipfileDropboxPath).download(zipfileOutput);
                Logger.log("Mods zipfile downloaded successfully!");
            }
            else {
                Logger.log("The Mods zipfile already exists! (is it your mistake?)");
            }

            //remove old modpack
            Logger.log("Removing old modpack " + installedVersion);
            File modFolder = new File(modFolderPath);
            for (File file : modFolder.listFiles()) {
                if(!file.isDirectory()) {
                    file.delete();
                }
            }

            //extract zip from zipfileName to modFolder
            Logger.log("Extracting new modpack " + latestVersion);
            ZipInputStream zipfileInput = new ZipInputStream(new FileInputStream(zipfileDestPath)); // + sep + zipfileDropboxName.replace(".zip", "")));
            ZipEntry child;
            String childDestPath;
            while((child = zipfileInput.getNextEntry()) != null) {
                //extractFile(zipfileInput, modFolder, fileInZipfile.getName());
                childDestPath = modFolderPath + sep + child.getName();
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

            //Changing variables
            Properties.setProperty("installed-version", latestVersion);
            installedVersion = latestVersion;

            Logger.log("The last version of the Rh-modpack (" + latestVersion + ") is installed in your .minecraft/mods folder");
            
        }
        catch(Exception e) {
            Logger.log(e.getMessage(), false);
        }
    }

    public static void download() {}

    public static void extract() {}

}
