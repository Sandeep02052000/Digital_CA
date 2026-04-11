package org.tax.mitra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication(scanBasePackages = "org.tax.mitra")
public class TaxMitraApplication {
    public static void main(String... args)
    {
        SpringApplication.run(TaxMitraApplication.class , args);
    }
}