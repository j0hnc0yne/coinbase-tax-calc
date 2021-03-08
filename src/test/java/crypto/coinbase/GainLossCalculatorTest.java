/**
 * 
 */
package crypto.coinbase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import crypto.coinbase.vo.FinalSale;
import crypto.coinbase.vo.Order;
import crypto.coinbase.vo.OrderType;
import crypto.coinbase.vo.Unit;

/**
 * @author jpcoyne
 *
 */
class GainLossCalculatorTest {

	GainLossCalculator gainLossCalculatorTest;

	@BeforeEach
	void setup() {
		gainLossCalculatorTest = new GainLossCalculator();
	}

	Order createOrder(double cryptoAmt, double usdAmount, double fee, Unit unit, OrderType ot, String date) throws Exception{
		Order order = new Order();
		order.setCryptoAmount(BigDecimal.valueOf(cryptoAmt));
		order.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(date));
		order.setFee(new BigDecimal(fee));
		order.setOrderType(ot);
		order.setUnit(unit);
		order.setUsdAmount(BigDecimal.valueOf(usdAmount));
		return order;
	}
	
	@Test
	void testWithSplit() throws Exception{
		List<Order> orders = new ArrayList<>();
		orders.add(createOrder(0.1, 1999.0, 1.0, Unit.BTC, OrderType.BUY, "2020-01-02"));
		orders.add(createOrder(0.2, 5994.0, 6.0, Unit.BTC, OrderType.BUY, "2020-03-02"));
		orders.add(createOrder(0.2, 8008.0, 8.0, Unit.BTC, OrderType.SELL, "2020-01-02"));

		List<FinalSale> output = gainLossCalculatorTest.calcGainLoss(orders);
		
		assertEquals(2, output.size(), "Size of Final Sale List");
		
		assertEquals("2000.00", output.get(0).getCostBasis(), "Cost basis sale 1");
		assertEquals("4000.00", output.get(0).getProceeds(), "Proceeds sale 1");
		assertEquals(Unit.BTC, output.get(0).getAssetName());
		
		assertEquals("3000.00", output.get(1).getCostBasis(), "Cost basis sale 2");
		assertEquals("4000.00", output.get(1).getProceeds(), "Proceeds sale 2");
		assertEquals(Unit.BTC, output.get(1).getAssetName());

		
	}

}
