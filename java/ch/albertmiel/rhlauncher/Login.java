package ch.albertmiel.rhlauncher;

import java.util.concurrent.CompletableFuture;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class Login {

    public static void MSLogin() {
        Logger.log("MS Login connection");
        try {
            MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
            CompletableFuture<MicrosoftAuthResult> future = authenticator.loginWithAsyncWebview().whenComplete((response, error) -> {
                if(error == null) {
                    Properties.setProperty("pseudo", response.getProfile().getName());
                    Properties.setProperty("id", response.getProfile().getId());
                    Properties.setProperty("ms-access-token", response.getAccessToken());
                    Properties.setProperty("ms-refresh-token", response.getRefreshToken());
                    Properties.setProperty("client-id", response.getClientId());
                    Properties.setProperty("xuid", response.getXuid());
                    Properties.setProperty("is-crack-mode", "false");

                    Properties.setProperty("is-connected", "true");
                    Properties.setProperty("day-of-connection", LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMMyyyy")));

                    Logger.log("Hello " + Properties.getProperty("pseudo") + "!");
                }
                else if(error != null) {
                    System.out.println(error.toString());
                    Platform.runLater(()-> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur");
                        alert.setContentText(error.getMessage());
                        alert.show();
                    });
                    return;
                }
            });

            future.join();
        }
        catch(Exception e) {
            Logger.log(e.getMessage(), false);
        }
    }

    public static void CrackLogin(String pseudo) {
        Properties.setProperty("pseudo", pseudo);
        Properties.setProperty("id", UUID.randomUUID().toString());
        Properties.setProperty("ms-access-token", "42fd85713af04fd7ac146df4d3bd631d");
        Properties.setProperty("ms-refresh-token", "${refresh_token}");
        Properties.setProperty("client-id", "${clientid}");
        Properties.setProperty("xuid", "${auth_xuid}");
        Properties.setProperty("is-crack-mode", "true");

        Properties.setProperty("is-connected", "true");

        Logger.log("Hello " + Properties.getProperty("pseudo") + "!");
    }
    public static void CrackLogin(String pseudo, String uuid) {
        Properties.setProperty("pseudo", pseudo);
        Properties.setProperty("id", uuid);
        Properties.setProperty("ms-access-token", "42fd85713af04fd7ac146df4d3bd631d");
        Properties.setProperty("ms-refresh-token", "${refresh_token}");
        Properties.setProperty("client-id", "${clientid}");
        Properties.setProperty("xuid", "${auth_xuid}");
        Properties.setProperty("is-crack-mode", "true");

        Properties.setProperty("is-connected", "true");

        Logger.log("Hello " + Properties.getProperty("pseudo") + "!");
    }

    public static void logout() {
        String pseudo = Properties.getProperty("pseudo");
        Properties.setProperty("pseudo", "");
        Properties.setProperty("id", "");
        Properties.setProperty("ms-access-token", "");
        Properties.setProperty("ms-refresh-token", "");
        Properties.setProperty("client-id", "");
        Properties.setProperty("xuid", "");
        Properties.setProperty("is-crack-mode", "");

        Properties.setProperty("is-connected", "false");

        String cmdFileName = ".minecraft/minecraftLaunch.cmd";
        File cmdFile = new File(cmdFileName);
        if(cmdFile.exists()) {cmdFile.delete();}


        Logger.log("Goodbye " + pseudo);
        
    }

    public static void updateAccessToken() {
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        Logger.log("Updating your Access Token...");
        try {
            MicrosoftAuthResult response = authenticator.loginWithRefreshToken(Properties.getProperty("ms-refresh-token"));
            Properties.setProperty("ms-access-token", response.getAccessToken());
            Properties.setProperty("day-of-connection", LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMMyyyy")));
            Logger.log("Your Access Token is now up-to-date!");
        }
        catch(Exception e) {Logger.log(e.getMessage(), false);}
    }

}
