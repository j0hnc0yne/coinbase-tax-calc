/**
 * 
 */
package crypto.coinbase;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author jpcoyne
 *
 */
@Component
@Slf4j
public class CoinDeskClient {
	static final String URL = "https://api.coindesk.com/v1/bpi/historical/close.json?start=DATESTR&end=DATESTR";

	public BigDecimal getBtcPrice(Date date) {

		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
		String url = URL.replaceAll("DATESTR", dateStr);

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(url).build();

		String respBody = null;
		try (Response response = client.newCall(request).execute()) {
			respBody = response.body().string();
		} catch (IOException e) {
			log.error("Unable to call CoinDesk API successfully", e);
			throw new RuntimeException("unable to contact API to determine price");
		}

		log.debug("Response from Coindesk API {}", respBody);

		Map map = new Gson().fromJson(respBody, Map.class);
		Map prices = (Map) map.get("bpi");

		double price = (Double) prices.get(dateStr);
		log.info("Got BTC Price for {} -> ${}", dateStr, price);

		String priceStr = "" + price;
		// force to string to avoid double issue
		return new BigDecimal(priceStr);
	}
}
