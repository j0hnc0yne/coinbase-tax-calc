/**
 * 
 */
package crypto.coinbase;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import crypto.coinbase.vo.FinalSale;
import crypto.coinbase.vo.Order;
import crypto.coinbase.vo.OrderType;

/**
 * @author jpcoyne
 *
 */
@Component
public class GainLossCalculator {
	
	public List<FinalSale> calcGainLoss(List<Order> orders) {
		List<FinalSale> saleList = new ArrayList<>();
		
		
		List<Order> sellOrders =
		orders
		.stream()
		.filter(e->OrderType.SELL.equals(e.getOrderType()))
		.collect(Collectors.toList() );
		

		List<Order> buyOrders =
				orders
				.stream()
				.filter(e->OrderType.BUY.equals(e.getOrderType()))
				.collect(Collectors.toList() );

		System.out.println("TOTAL BUY ORDERS: " + buyOrders.size());
		System.out.println("TOTAL SELL ORDERS: " + sellOrders.size());
		
		
		for(Order sellOrder : sellOrders) {
			if(sellOrder.getUnit() == null) {
				System.out.println("Unit is null for sell: " + sellOrder.toString());
				continue;
			}
			BigDecimal amountRemaining = null;
			BigDecimal cryptoSold = sellOrder.getCryptoAmount();
			for(Order buyOrder : buyOrders) {
				if(buyOrder.getUnit() == null) {
					System.out.println("Unit is null for buy: " + buyOrder.toString());
					continue;
				}
				
				if(!sellOrder.getUnit().equals(buyOrder.getUnit()) || buyOrder.isSoldOut()) {
					continue;
				}
				if(amountRemaining !=null) {
					BigDecimal amountToSell = amountRemaining.negate();
					amountRemaining = buyOrder.sellCrypto(amountToSell);
				} else {
					amountRemaining = buyOrder.sellCrypto(cryptoSold);
				}
				
				if(amountRemaining.compareTo(BigDecimal.ZERO) < 0) {
					// more left to sell - create sale order for this lot though
					BigDecimal amountSold = cryptoSold.add(amountRemaining);
					saleList.add( createSale(sellOrder, buyOrder, amountSold) );
					// reduce the amount 'sold' for next buy order
					cryptoSold = cryptoSold.subtract(amountSold);
				}else {
					// all sold
					saleList.add( createSale(sellOrder, buyOrder, cryptoSold) );
					break;
				}

			}
		}
		
		return saleList;
	}

	private FinalSale createSale(Order sellOrder, Order buyOrder, BigDecimal amountSold) {
		double percentage = amountSold.doubleValue() / buyOrder.getCryptoAmount().doubleValue();
		double costBasis = percentage * (buyOrder.getUsdAmount().doubleValue() + buyOrder.getFee().doubleValue());
		
		double proceedsPercent = amountSold.doubleValue() / sellOrder.getCryptoAmount().doubleValue();
		double proceeds = proceedsPercent * (sellOrder.getUsdAmount().doubleValue() - sellOrder.getFee().doubleValue());
		
		FinalSale finalSale = new FinalSale();
		finalSale.setAssetName(sellOrder.getUnit());
		finalSale.setCostBasis(formattedDollarAmount(costBasis));
		finalSale.setProceeds(formattedDollarAmount(proceeds));
		finalSale.setReceivedDate(dateToStr(buyOrder.getDate()));
		finalSale.setSoldDate(dateToStr(sellOrder.getDate()));
		return finalSale;
	}
	
	private String formattedDollarAmount(double amt) {
		return String.format("%.2f", amt); 
	}
	
	private String dateToStr(Date date) {
		return new SimpleDateFormat("MM/dd/yy").format(date);
	}
	
}
