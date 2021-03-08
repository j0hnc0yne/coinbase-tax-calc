package crypto.coinbase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoinbaseTaxCalcApplication implements CommandLineRunner {

	@Autowired
	private TransactionProcessor transactionProcessor;
	
	public static void main(String[] args) {
		SpringApplication.run(CoinbaseTaxCalcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		transactionProcessor.process();
		System.exit(0);
	}

}
