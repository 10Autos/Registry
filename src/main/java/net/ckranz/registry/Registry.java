package net.ckranz.registry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * A class to easily manipulate the Windows registry
 */
public class Registry {
    /**
     * The H_KEY for the user
     */
    public static final String USER = "HKEY_CURRENT_USER\\";

    /**
     * The H_KEY for the system
     */
    public static final String SYSTEM = "HKEY_LOCAL_MACHINE\\";

    /**
     * This key is associated with the default value of the node
     */
    public static final String DEFAULT = "";


    /**
     * @param path The path to the node
     * @param key The key of the node
     * @param value The value of the key
     */
    public static void write(String path, String key, String value) {
        try {
            String command = "reg add \"" + path + "\" " + getKey(key) + " /d \"" + value + "\" /f";
            executeCommand(command);
        } catch(IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param path The path to the node
     * @param key The key of the node
     * @return The value of the key
     */
    public static String read(String path, String key) {
        try {
            String command = "reg query \"" + path + "\" " +  getKey(key);
            Process process = executeCommand(command);
            process.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            String result = null;
            for(int i = 0; i < 3; i++) {
                result = br.readLine();
            }
            if(result != null) {
                String[] parts = br.readLine().trim().split("\\s+");
                return parts[parts.length - 1];
            } else {
                throw new MissingKeyException("The key \"" + key + "\" doesn't exist");
            }
        } catch(IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param path The path to the node
     * @param key The key of the node
     */
    public static void deleteKey(String path, String key) {
        try {
            String command = "reg delete \"" + path + "\" " + getKey(key) + " /f";
            executeCommand(command);
        } catch(IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param path The path to the node
     */
    public static void deleteNode(String path) {
        try {
            String command = "reg delete \"" + path + "\" /f";
            executeCommand(command);
        } catch(IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private static String getKey(String key) {
        if(!key.equals(DEFAULT)) {
            return "/v \"" + key + "\"";
        } else {
            return "/ve";
        }
    }


    private static Process executeCommand(String command) throws IOException, InterruptedException {
        Process process = new ProcessBuilder("cmd", "/c", command).start();
        process.waitFor();
        return process;
    }
}