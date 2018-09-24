package eventViewer.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

@Configuration
public class ConfDAO {
	
	@Profile("default")
	@Bean
	@Scope(value="prototype")
	@Lazy(value=true)
	SettingDAO getOmniDao(String host, int port, String ncoms, String username, String password) {
		return new OMNIbus(host, port, ncoms, username, password);
	}

	@Profile("dev")
	@Bean
	SettingDAO getH2Dao(
			@Value("local") String host,
			@Value("0") int port,
			@Value("H2S") String ncoms,
			@Value("SA") String username,
			@Value("") String password) {
		return new H2MemDB();
	}
}
