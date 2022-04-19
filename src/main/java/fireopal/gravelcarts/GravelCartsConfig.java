package fireopal.gravelcarts;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;

public class GravelCartsConfig {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    //Config Default Values

    public String CONFIG_VERSION_DO_NOT_TOUCH_PLS = GravelCarts.VERSION.toString();
    
    public int maxGravelLayers = 2;

    //~~~~~~~~


    public static GravelCartsConfig init() {
        GravelCartsConfig config = null;

        try {
            Path configPath = Paths.get("", "config", GravelCarts.MODID + ".json");

            if (Files.exists(configPath)) {
                config = gson.fromJson(
                    new FileReader(configPath.toFile()),
                    GravelCartsConfig.class
                );

                if (!config.CONFIG_VERSION_DO_NOT_TOUCH_PLS.equals(GravelCarts.VERSION.toString())) {
                    config.CONFIG_VERSION_DO_NOT_TOUCH_PLS = GravelCarts.VERSION.toString();

                    BufferedWriter writer = new BufferedWriter(
                        new FileWriter(configPath.toFile())
                    );

                    writer.write(gson.toJson(config));
                    writer.close();
                }

            } else {
                config = new GravelCartsConfig();
                Paths.get("", "config").toFile().mkdirs();

                BufferedWriter writer = new BufferedWriter(
                    new FileWriter(configPath.toFile())
                );

                writer.write(gson.toJson(config));
                writer.close();
            }


        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return config;
    }
}
