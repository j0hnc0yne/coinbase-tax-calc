package crypto.coinbase.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class OrderTest {

	@Test
	void testOrder_Longs() {
		Order order = new Order();
		order.setCryptoAmount(BigDecimal.valueOf(5L));
		
		assertFalse(order.isSoldOut(), "Order sold out");
		
		BigDecimal amountLeft = order.sellCrypto(BigDecimal.valueOf(1L));
		assertEquals(BigDecimal.valueOf(4L), amountLeft, "Amount left after 1 sell");
		
		amountLeft = order.sellCrypto(BigDecimal.valueOf(2L));

		assertEquals(BigDecimal.valueOf(2L), amountLeft, "Amount left after 2 sells");
		
		amountLeft = order.sellCrypto(BigDecimal.valueOf(3L));

		assertEquals(BigDecimal.valueOf(-1L), amountLeft, "Amount left after 3 sells");

		assertTrue(order.isSoldOut(), "Order sold out");

		
		amountLeft = order.sellCrypto(BigDecimal.valueOf(2L));
		assertEquals(BigDecimal.valueOf(-2L), amountLeft, "Amount left after 4 sells");

	}
}
