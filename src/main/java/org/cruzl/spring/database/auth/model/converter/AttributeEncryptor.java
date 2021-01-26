package org.cruzl.spring.database.auth.model.converter;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;

import org.cruzl.spring.database.auth.SecurityProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("spring.datasource.security")
public class AttributeEncryptor implements AttributeConverter<String, String> {

//	private @Setter @Getter String algorithm = "AES";
//	private @Setter @Getter String secretKey;

	private final Key key;
	private final Cipher cipher;

	public AttributeEncryptor(SecurityProperties securityProperties) throws Exception {
		this.key = new SecretKeySpec(securityProperties.getSecretKey().getBytes(), securityProperties.getAlgorithm());
		this.cipher = Cipher.getInstance(securityProperties.getAlgorithm());
	}

	@Override
	public String convertToDatabaseColumn(String attribute) {
		try {
			this.cipher.init(Cipher.ENCRYPT_MODE, this.key);
			return Base64.getEncoder().encodeToString(this.cipher.doFinal(attribute.getBytes()));
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		try {
			this.cipher.init(Cipher.DECRYPT_MODE, this.key);
			return new String(this.cipher.doFinal(Base64.getDecoder().decode(dbData)));
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
}