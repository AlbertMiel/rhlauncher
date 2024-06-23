package ch.albertmiel.rhlauncher;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class MSLogin extends Thread {
    public void run() {
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
                    Logger.log(error.toString(), false);
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
}
