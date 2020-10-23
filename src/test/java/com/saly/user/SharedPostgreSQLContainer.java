package com.saly.user;

import org.testcontainers.containers.PostgreSQLContainer;

public class SharedPostgreSQLContainer extends PostgreSQLContainer<SharedPostgreSQLContainer> {
    private static final String IMAGE_VERSION = "postgres:12.0";
    private static SharedPostgreSQLContainer container;

    private SharedPostgreSQLContainer() {
        super(IMAGE_VERSION);
    }

    public static SharedPostgreSQLContainer getInstance() {
        if (container == null) {
            container = new SharedPostgreSQLContainer()
                    .withDatabaseName("test-db")
                    .withUsername("root")
                    .withPassword("root");
        }

        return container;
    }


    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}
