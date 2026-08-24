package org.jeecg.modules.geo.publish;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Resolves the Wechatsync CLI executable for the current environment.
 */
public final class WechatsyncCliPathResolver {

    private WechatsyncCliPathResolver() {
    }

    public static String resolve() {
        String env = System.getenv("WECHATSYNC_CLI_PATH");
        if (env != null && !env.isBlank()) {
            return env;
        }

        String os = System.getProperty("os.name", "").toLowerCase();
        if (os.contains("win")) {
            String appData = System.getenv("APPDATA");
            if (appData != null && !appData.isBlank()) {
                String candidate = Paths.get(appData, "npm", "wechatsync.cmd").toString();
                if (Files.exists(Path.of(candidate))) {
                    return candidate;
                }
            }

            String programFiles = System.getenv("ProgramFiles");
            if (programFiles != null && !programFiles.isBlank()) {
                String candidate = Paths.get(programFiles, "nodejs", "wechatsync.cmd").toString();
                if (Files.exists(Path.of(candidate))) {
                    return candidate;
                }
            }
        }

        return "wechatsync";
    }
}
