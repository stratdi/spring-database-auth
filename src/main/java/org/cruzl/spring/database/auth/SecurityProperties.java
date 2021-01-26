package org.cruzl.spring.database.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("spring.datasource.security")
public class SecurityProperties {

	private String algorithm = "AES";
	private String secretKey;

}
