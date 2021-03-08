package crypto.coinbase.vo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@ToString
@Slf4j
public class Order implements Comparable<Order> {

	private Date date;
	private BigDecimal usdAmount;
	private BigDecimal cryptoAmount;
	private BigDecimal fee;
	private Unit unit;
	private OrderType orderType;

	private BigDecimal soldAmount;

	public Order() {
		soldAmount = BigDecimal.valueOf(0L);
		fee = BigDecimal.ZERO;
		usdAmount = BigDecimal.ZERO;
		cryptoAmount = BigDecimal.ZERO;
	}

	public boolean isSoldOut() {
		return soldAmount.compareTo(cryptoAmount) >= 0;
	}
	
	public void addUsd(BigDecimal usdAmtToAdd) {
		if(!usdAmount.equals(BigDecimal.ZERO)) {
			log.debug("Adding usd {} to order with usd: {}", usdAmtToAdd, usdAmount);
		}
		usdAmount = usdAmount.add(usdAmtToAdd);
	}

	public void addCrypto(BigDecimal cryptoAmountToAdd) {
		if(!cryptoAmount.equals(BigDecimal.ZERO)) {
			log.debug("Adding crypto {} to order with crypto: {}", cryptoAmountToAdd, cryptoAmount);
		}
		cryptoAmount = cryptoAmount.add(cryptoAmountToAdd);
	}

	/**
	 * Returns amount that is left to sell.
	 * 
	 * A negative value indicates there is that much value still unsold
	 * 
	 * @param amountToSell
	 * @return
	 */
	public BigDecimal sellCrypto(BigDecimal amountToSell) {
		if (OrderType.SELL.equals(orderType)) {
			throw new RuntimeException("Cant sell towards a sell");
		}
		if (soldAmount.compareTo(cryptoAmount) >= 0) {
			// already sold all
			return amountToSell.negate();
		}
		soldAmount = soldAmount.add(amountToSell);

		BigDecimal amountLeft = cryptoAmount.subtract(soldAmount);

		if (amountLeft.compareTo(BigDecimal.ZERO) < 0) {
			soldAmount = cryptoAmount;
		}

		return amountLeft;
	}

	@Override
	public int compareTo(Order o) {
		if (o != null && o.getDate() != null && this.getDate() != null) {
			return this.getDate().compareTo(o.getDate());
		}
		return 0;
	}

}
