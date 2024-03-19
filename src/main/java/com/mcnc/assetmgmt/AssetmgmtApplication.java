package com.mcnc.assetmgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AssetmgmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssetmgmtApplication.class, args);
	}
}
