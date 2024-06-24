package ch.albertmiel.rhlauncher;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import ch.albertmiel.rhlauncher.ui.MainScreen;

public class Main {

    public static void main(String[] args) {
        //launch(args);
        Logger.log("\n"+
         "   ___________     ___     ___     ___________     __________      ___     ___     ___     ___     ___     ___ \n"
        +" /|           |  /|   |  /|   |  /|           |  /|          \\   /|   |  /|   |  /|   |  /|   \\   /   |  /|   |\n"
        +"| |    ___    | | |   | | |   | | |    ___    | | |    ___    \\ | |   | | |   | | |   | | |    \\_/    | | |   |\n"
        +"| |   |   |   | | |   | | |   | | |   |_ /|   | | |   |_ /\\   | | |   | | |   | | |   | | |           | | |   |\n"
        +"| |   |___|   | | |   |___|   | | |   | | |   | | |   | | |   | | |   | | |   | | |   | | |           | | |   |\n"
        +"| |           | | |           | | |   | | |   | | |   | | |   | | |   | | |   | | |   | | |   |\\_/|   | | |   |\n"
        +"| |         __| | |    ___    | | |   | | |   | | |   | | |   | | |   | | |   | | |   | | |   | | |   | | |   |\n"
        +"| |   |\\   \\ /  | |   |   |   | | |   | | |   | | |   | | |   | | |   | | |   | | |   | | |   | | |   | | |___|\n"
        +"| |   | \\   \\   | |   |   |   | | |   |___|   | | |   |___/   | | |   | | |   |___|   | | |   | | |   | |/ ___ \n"
        +"| |   |\\ \\   \\  | |   |   |   | | |           | | |           / | |   | | |           | | |   | | |   |  /|   |\n"
        +"| |___| \\ \\___\\ | |___|   |___| | |___________| | |__________/  | |___| | |___________| | |___| | |___| | |___|\n"
        +"|/___/   \\/___/ |/___/   /___/  |/___________/  |/__________/   |/___/  |/___________/  |/___/  |/___/  |/___/ \n\n"
        +"RH Launcher Started!");
        Utils.setOsDifferences();
        if(Properties.getProperty("is-connected").equals("true")) {
            if(!Properties.getProperty("day-of-connection").equals(LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMMyyyy"))) && Properties.getProperty("is-crack-mode").equals("false")) {
                Login.updateAccessToken();
            }
        }
        else {}
        MainScreen.launch(MainScreen.class, args);
        //Utils.mainMenu();
        Logger.log("The launcher is now safe to close. See you!\n\n\n\n");
    }
}
