package crypto.coinbase;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import crypto.coinbase.vo.FinalSale;
import crypto.coinbase.vo.Order;

public class Utils {
	public static void createOrdersCSVFile(List<Order> orders) throws IOException {
		String[] HEADERS = { "Date", "Order Type", "Crypto Amount", "Crypto Type", "USD Amount", "Fee" };

		FileWriter out = new FileWriter("orders.csv");
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS))) {
			for (Order order : orders) {
				printer.printRecord(order.getDate(), order.getOrderType(), order.getCryptoAmount(), order.getUnit(),
						order.getUsdAmount(), order.getFee());
			}
		}
	}

	public static void createFinalSaleCSVFile(List<FinalSale> sales) throws IOException {
		String[] HEADERS = { "Asset Name", "Recieved Date", "Cost Basis", "Sold Date", "Proceeds"};

		FileWriter out = new FileWriter("sales.csv");
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS))) {
			for (FinalSale sale : sales) {
				printer.printRecord(sale.getAssetName(), sale.getReceivedDate(), sale.getCostBasis(),
						sale.getSoldDate(), sale.getProceeds());
			}
		}
	}
	
	public static BigDecimal bigD(String strNumber) {
		BigDecimal bigD = new BigDecimal(strNumber);
		bigD.setScale(4, RoundingMode.HALF_DOWN);
		return bigD;
	}
	
	public static BigDecimal bigD(double number) {
		return bigD(Double.toString(number));
	}
	
	public static Date getDateYYYYMMDD(String date) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd").parse(date);
	}

}
