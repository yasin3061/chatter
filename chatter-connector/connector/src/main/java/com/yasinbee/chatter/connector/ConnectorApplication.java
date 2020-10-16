package com.yasinbee.chatter.connector;

import com.yasinbee.chatter.connector.config.ChatterConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ChatterConfig.class)
public class ConnectorApplication implements CommandLineRunner {

    private final AppLauncher launcher;

    public ConnectorApplication(AppLauncher launcher) {
        this.launcher = launcher;
    }

    public static void main(String[] args) {
        SpringApplication.run(ConnectorApplication.class, args);
    }

    @Override
    public void run(String... args) {
        launcher.launch();
    }
}
