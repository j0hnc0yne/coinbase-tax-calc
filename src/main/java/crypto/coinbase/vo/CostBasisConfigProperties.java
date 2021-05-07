package crypto.coinbase.vo;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "basis")
@Data
public class CostBasisConfigProperties {
	private Map<String, String> btc;
	private Map<String, String> xrp;
	private Map<String, String> ltc;
}
