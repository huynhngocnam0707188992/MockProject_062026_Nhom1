package com.eldercare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		loadEnv();
		SpringApplication.run(BackendApplication.class, args);
	}

	private static void loadEnv() {
		String[] paths = {".env", "backend/.env", "../.env"};
		for (String envPath : paths) {
			Path path = Paths.get(envPath);
			if (Files.exists(path)) {
				try {
					List<String> lines = Files.readAllLines(path);
					for (String line : lines) {
						line = line.trim();
						if (line.isEmpty() || line.startsWith("#")) {
							continue;
						}
						int eqIdx = line.indexOf('=');
						if (eqIdx > 0) {
							String key = line.substring(0, eqIdx).trim();
							String value = line.substring(eqIdx + 1).trim();
							if (value.startsWith("\"") && value.endsWith("\"") && value.length() >= 2) {
								value = value.substring(1, value.length() - 1);
							} else if (value.startsWith("'") && value.endsWith("'") && value.length() >= 2) {
								value = value.substring(1, value.length() - 1);
							}
							System.setProperty(key, value);
						}
					}
					System.out.println("Loaded environment variables from " + path.toAbsolutePath());
					return;
				} catch (IOException e) {
					System.err.println("Failed to read environment file: " + path.toAbsolutePath() + " - " + e.getMessage());
				}
			}
		}
		System.out.println("No .env file found. Relying on system environment variables.");
	}

}

