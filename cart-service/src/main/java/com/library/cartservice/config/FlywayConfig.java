package com.library.cartservice.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    //@Bean
    //public CommandLineRunner migrateDatabase(DataSource dataSource) {
        //return args -> {
            //Flyway flyway = Flyway.configure()
                    //.dataSource(dataSource)
                    //.locations("classpath:db/migration")
                    //.load();

            //flyway.migrate();
            //System.out.println("Flyway ejecutado correctamente");
        //};
    //}
}