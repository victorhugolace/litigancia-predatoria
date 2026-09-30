package br.jus.tjpb.lyno.litigancia.config;

import java.time.Clock;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeConfig {
    @Bean
    public Clock clock() { return Clock.system(ZoneId.of("America/Recife")); }
}
