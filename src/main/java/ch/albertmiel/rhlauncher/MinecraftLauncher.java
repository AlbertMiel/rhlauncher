package ch.albertmiel.rhlauncher;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MinecraftLauncher {

    static String command = "";

    public static int launch(String vanillaVersion, String api, String apiVersion) {

        
        Updater.CheckUpdate();

        if(!Properties.getProperty("is-connected").equals("true")) {
            Logger.log("User is not connected - Please try to log in first XD");
        }

        try {
            new ProcessBuilder("cmd", "/c", "start").start();
        }
        catch(Exception e) {
            Logger.log(e.getMessage(), false);
        }

        String sep = Utils.sep;
        String versionFilePath = "";
        String versionFolder = Properties.getProperty("game-directory") + sep + "versions";
        if(api != "minecraft" || apiVersion != vanillaVersion) {
            versionFolder += sep + vanillaVersion + "-" + api + "-" + apiVersion;
            versionFilePath = versionFolder + sep + vanillaVersion + "-" + api + "-" + apiVersion + ".json"; // 1.19.2-forge-43.3.0.json
        }
        else {
            versionFolder += sep + vanillaVersion;
            versionFilePath = versionFolder + sep + vanillaVersion + ".json";
        }
        String gameDirectory = Properties.getProperty("game-directory");
        String librariesDirectory = gameDirectory + sep + "libraries";
        //String vanillaVersion = version;   //1.19.2
        String assetIndex = "1.19";
        String javaRuntime = Utils.javaRuntime;

        try {
            //.json version file reading
            Gson gson = new Gson();
            Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> versionData = gson.fromJson(new FileReader(versionFilePath), mapType);
            
            List<String> arguments = new ArrayList<>();
            arguments.add("-Xmx16384m");
            //HMCL's args
            /*arguments.add("-Xmx8192m" +
            " -Dfile.encoding=windows-1252" +
            " -Dstdout.encoding=windows-1252" + 
            " -Dstderr.encoding=windows-1252"+
            " -Djava.rmi.server.useCodebaseOnly=true" +
            " -Dcom.sun.jndi.rmi.object.trustURLCodebase=false" +
            " -Dcom.sun.jndi.cosnaming.object.trustURLCodebase=false" +
            " -Dlog4j2.formatMsgNoLookups=true" +
            " -Dlog4j.configurationFile=${game_directory}\\versions\\1.19.2\\log4j2.xml" +
            " -Dminecraft.client.jar=${game_directory}\\versions\\1.19.2\\1.19.2.jar" +
            " -XX:+UnlockExperimentalVMOptions" +
            " -XX:+UseG1GC" +
            " -XX:G1NewSizePercent=20" +
            " -XX:G1ReservePercent=20" +
            " -XX:MaxGCPauseMillis=50" +
            " -XX:G1HeapRegionSize=32m" +
            " -XX:-UseAdaptiveSizePolicy" +
            " -XX:-OmitStackTraceInFastThrow" +
            " -XX:-DontCompileHugeMethods" +
            " -Dfml.ignoreInvalidMinecraftCertificates=true" +
            " -Dfml.ignorePatchDiscrepancies=true" +
            " -XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump" +
            " -Djava.library.path=${game_directory}\\versions\\1.19.2\\natives-windows-x86_64" +
            " -Dminecraft.launcher.brand=RH-LAUNCHER" +
            " -Dminecraft.launcher.version=v0.4.1");*/

            //Libraries
            String libraries = "";

            arguments.add("-cp");
            for (Map<String, Object> lib : (List<Map<String, Object>>) versionData.get("libraries")) {
                Map<String, Object> artifact = (Map<String, Object>) lib.get("downloads");
                artifact = (Map<String, Object>) artifact.get("artifact");
                libraries += librariesDirectory + sep + (String) artifact.get("path") + ";";
            }
            libraries += "${library_directory}\\com\\mojang\\logging\\1.0.0\\logging-1.0.0.jar;${library_directory}\\com\\mojang\\blocklist\\1.0.10\\blocklist-1.0.10.jar;${library_directory}\\com\\mojang\\patchy\\2.2.10\\patchy-2.2.10.jar;${library_directory}\\com\\github\\oshi\\oshi-core\\5.8.5\\oshi-core-5.8.5.jar;${library_directory}\\n" + //
                    "et\\java\\dev\\jna\\jna\\5.10.0\\jna-5.10.0.jar;${library_directory}\\net\\java\\dev\\jna\\jna-platform\\5.10.0\\jna-platform-5.10.0.jar;${library_directory}\\org\\slf4j\\slf4j-api\\1.8.0-beta4\\slf4j-api-1.8.0-beta4.jar;${library_directory}\\org\\apache\\logging\\log4j\\log4j-slf4j18-impl\\2.17.0\\log4j-slf4j18-impl-2.17.0.jar;${library_directory}\\com\\ibm\\icu\\icu4j\\70.1\\icu4j-70.1.jar;${library_directory}\\com\\mojang\\javabridge\\1.2.24\\javabridge-1.2.24.jar;${library_directory}\\n" + //
                    "et\\sf\\jopt-simple\\jopt-simple\\5.0.4\\jopt-simple-5.0.4.jar;${library_directory}\\io\\netty\\netty-common\\4.1.77.Final\\netty-common-4.1.77.Final.jar;${library_directory}\\io\\netty\\netty-buffer\\4.1.77.Final\\netty-buffer-4.1.77.Final.jar;${library_directory}\\io\\netty\\netty-codec\\4.1.77.Final\\netty-codec-4.1.77.Final.jar;${library_directory}\\io\\n" + //
                    "etty\\netty-handler\\4.1.77.Final\\netty-handler-4.1.77.Final.jar;${library_directory}\\io\\netty\\netty-resolver\\4.1.77.Final\\netty-resolver-4.1.77.Final.jar;${library_directory}\\io\\n" + //
                    "etty\\netty-transport\\4.1.77.Final\\netty-transport-4.1.77.Final.jar;${library_directory}\\io\\netty\\netty-transport-native-unix-common\\4.1.77.Final\\netty-transport-native-unix-common-4.1.77.Final.jar;${library_directory}\\io\\netty\\netty-transport-classes-epoll\\4.1.77.Final\\netty-transport-classes-epoll-4.1.77.Final.jar;${library_directory}\\com\\google\\guava\\failureaccess\\1.0.1\\failureaccess-1.0.1.jar;${library_directory}\\com\\google\\guava\\guava\\31.0.1-jre\\guava-31.0.1-jre.jar;${library_directory}\\org\\apache\\commons\\commons-lang3\\3.12.0\\commons-lang3-3.12.0.jar;${library_directory}\\commons-io\\commons-io\\2.11.0\\commons-io-2.11.0.jar;${library_directory}\\commons-codec\\commons-codec\\1.15\\commons-codec-1.15.jar;${library_directory}\\com\\mojang\\brigadier\\1.0.18\\brigadier-1.0.18.jar;${library_directory}\\com\\mojang\\datafixerupper\\5.0.28\\datafixerupper-5.0.28.jar;${library_directory}\\com\\google\\code\\gson\\gson\\2.8.9\\gson-2.8.9.jar;${library_directory}\\com\\mojang\\authlib\\3.11.49\\authlib-3.11.49.jar;${library_directory}\\org\\apache\\commons\\commons-compress\\1.21\\commons-compress-1.21.jar;${library_directory}\\org\\apache\\httpcomponents\\httpclient\\4.5.13\\httpclient-4.5.13.jar;${library_directory}\\commons-logging\\commons-logging\\1.2\\commons-logging-1.2.jar;${library_directory}\\org\\apache\\httpcomponents\\httpcore\\4.4.14\\httpcore-4.4.14.jar;${library_directory}\\it\\unimi\\dsi\\fastutil\\8.5.6\\fastutil-8.5.6.jar;${library_directory}\\org\\apache\\logging\\log4j\\log4j-api\\2.17.0\\log4j-api-2.17.0.jar;${library_directory}\\org\\apache\\logging\\log4j\\log4j-core\\2.17.0\\log4j-core-2.17.0.jar;${library_directory}\\org\\lwjgl\\lwjgl\\3.3.1\\lwjgl-3.3.1.jar;${library_directory}\\org\\lwjgl\\lwjgl\\3.3.1\\lwjgl-3.3.1-natives-windows.jar;${library_directory}\\org\\lwjgl\\lwjgl\\3.3.1\\lwjgl-3.3.1-natives-windows-x86.jar;${library_directory}\\org\\lwjgl\\lwjgl-jemalloc\\3.3.1\\lwjgl-jemalloc-3.3.1.jar;${library_directory}\\org\\lwjgl\\lwjgl-jemalloc\\3.3.1\\lwjgl-jemalloc-3.3.1-natives-windows.jar;${library_directory}\\org\\lwjgl\\lwjgl-jemalloc\\3.3.1\\lwjgl-jemalloc-3.3.1-natives-windows-x86.jar;${library_directory}\\org\\lwjgl\\lwjgl-openal\\3.3.1\\lwjgl-openal-3.3.1.jar;${library_directory}\\org\\lwjgl\\lwjgl-openal\\3.3.1\\lwjgl-openal-3.3.1-natives-windows.jar;${library_directory}\\org\\lwjgl\\lwjgl-openal\\3.3.1\\lwjgl-openal-3.3.1-natives-windows-x86.jar;${library_directory}\\org\\lwjgl\\lwjgl-opengl\\3.3.1\\lwjgl-opengl-3.3.1.jar;${library_directory}\\org\\lwjgl\\lwjgl-opengl\\3.3.1\\lwjgl-opengl-3.3.1-natives-windows.jar;${library_directory}\\org\\lwjgl\\lwjgl-opengl\\3.3.1\\lwjgl-opengl-3.3.1-natives-windows-x86.jar;${library_directory}\\org\\lwjgl\\lwjgl-glfw\\3.3.1\\lwjgl-glfw-3.3.1.jar;${library_directory}\\org\\lwjgl\\lwjgl-glfw\\3.3.1\\lwjgl-glfw-3.3.1-natives-windows.jar;${library_directory}\\org\\lwjgl\\lwjgl-glfw\\3.3.1\\lwjgl-glfw-3.3.1-natives-windows-x86.jar;${library_directory}\\org\\lwjgl\\lwjgl-stb\\3.3.1\\lwjgl-stb-3.3.1.jar;${library_directory}\\org\\lwjgl\\lwjgl-stb\\3.3.1\\lwjgl-stb-3.3.1-natives-windows.jar;${library_directory}\\org\\lwjgl\\lwjgl-stb\\3.3.1\\lwjgl-stb-3.3.1-natives-windows-x86.jar;${library_directory}\\org\\lwjgl\\lwjgl-tinyfd\\3.3.1\\lwjgl-tinyfd-3.3.1.jar;${library_directory}\\org\\lwjgl\\lwjgl-tinyfd\\3.3.1\\lwjgl-tinyfd-3.3.1-natives-windows.jar;${library_directory}\\org\\lwjgl\\lwjgl-tinyfd\\3.3.1\\lwjgl-tinyfd-3.3.1-natives-windows-x86.jar;${library_directory}\\com\\mojang\\text2speech\\1.16.7\\text2speech-1.16.7.jar;${library_directory}\\org\\lwjgl\\lwjgl-glfw\\3.3.1\\lwjgl-glfw-3.3.1-natives-windows-arm64.jar;${library_directory}\\org\\lwjgl\\lwjgl-jemalloc\\3.3.1\\lwjgl-jemalloc-3.3.1-natives-windows-arm64.jar;${library_directory}\\org\\lwjgl\\lwjgl-openal\\3.3.1\\lwjgl-openal-3.3.1-natives-windows-arm64.jar;${library_directory}\\org\\lwjgl\\lwjgl-opengl\\3.3.1\\lwjgl-opengl-3.3.1-natives-windows-arm64.jar;${library_directory}\\org\\lwjgl\\lwjgl-stb\\3.3.1\\lwjgl-stb-3.3.1-natives-windows-arm64.jar;${library_directory}\\org\\lwjgl\\lwjgl-tinyfd\\3.3.1\\lwjgl-tinyfd-3.3.1-natives-windows-arm64.jar;${library_directory}\\org\\lwjgl\\lwjgl\\3.3.1\\lwjgl-3.3.1-natives-windows-arm64.jar;";
            libraries += gameDirectory + sep + "versions" + sep + vanillaVersion + sep + vanillaVersion + ".jar";
            arguments.add(libraries);

            //JVM Arguments
            List<String> rawJvmArgs = (List<String>) ((Map<String, Object>) versionData.get("arguments")).get("jvm");
            for (String arg : rawJvmArgs) {
                arg = arg.replace("/", "${slash}");
                arguments.add(arg);
            }
            arguments.add((String) versionData.get("mainClass"));
            
            
            
            //Version & Dir Arguments
            arguments.add("--version");
            arguments.add(vanillaVersion);
            arguments.add("--gameDir");
            arguments.add(gameDirectory);
            arguments.add("--assetsDir");
            arguments.add(gameDirectory + sep + "assets");
            arguments.add("--assetIndex");
            arguments.add(assetIndex);

            //Auth Arguments
            arguments.add("--username");
            arguments.add(Properties.getProperty("pseudo"));
            arguments.add("--uuid");
            arguments.add(Properties.getProperty("id"));
            arguments.add("--accessToken");
            arguments.add(Properties.getProperty("ms-access-token"));
            arguments.add("--clientId");
            arguments.add(Properties.getProperty("client-id"));
            arguments.add("--xuid");
            arguments.add(Properties.getProperty("xuid"));
            arguments.add("--userType");
            arguments.add("msa");
            
            //GAME Arguments
            List<String> rawGameArgs = (List<String>) ((Map<String, Object>) versionData.get("arguments")).get("game");
            for (String arg : rawGameArgs) {
                arguments.add(arg);
            }
            
            // Construire la commande finale
            //List<String> arguments2 = new ArrayList<>();
            command += javaRuntime;
            for (String arg : arguments) {
                arg = arg.replace("${version_name}", vanillaVersion)
                    .replace("${library_directory}", librariesDirectory)
                    .replace("${game_directory}", gameDirectory)
                    .replace("${classpath_separator}", ";");
                if(sep == "\\") {arg = arg.replace("/", sep);}
                else if(sep == "/") {arg = arg.replace("\\", sep);}
                arg = arg.replace("${slash}", "/");

                command += " " + arg;
                //arguments2.add(arg);
            }
            //String[] arguments3 = arguments2.toArray(new String[arguments2.size()]);
            Logger.log("Launching Minecraft with command: \"" + command + "\"");

            /*List<String> command = new ArrayList<>();
            command.add("C:\\runtime\\Java\\jdk-17-win\\bin\\javaw.exe");
            command.addAll(arguments);
            Logger.log("" + command);*/
            

            //Lancer Minecraft
            //Écrire fichier batch
            String cmdFileName = ".minecraft/minecraftLaunch.cmd";
            File cmdFile = new File(cmdFileName);
            if(!cmdFile.exists()) {cmdFile.createNewFile();}
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(cmdFileName));
            writer.write(MinecraftLauncher.command);
            writer.close();

            // Lancer Minecraft
            ProcessBuilder processBuilder = new ProcessBuilder(cmdFileName);
            //processBuilder.inheritIO();
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String outputReadline;
            while((outputReadline = outputReader.readLine()) != null) {
                System.out.println(outputReadline);
            }

            process.waitFor();
            return 0;
            
        } 
        catch(Exception e) {
            Logger.log(e.getMessage() + e.getStackTrace(), false);
            return 1;
        }
    }
}