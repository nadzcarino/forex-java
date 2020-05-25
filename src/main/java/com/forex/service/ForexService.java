package com.forex.service;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.forex.model.Rate;
import com.forex.utils.ForexConstants;

@Service
public class ForexService {

	@Autowired
	private RestTemplate restTemplate;

	public List<Rate> getForexData() {

		String url = ForexConstants.BASE_URL + ForexConstants.validPairs;		
		JSONParser parser = new JSONParser();
		List<Rate> rateList = new ArrayList<Rate>();

		try {

			String result = this.restTemplate.getForObject(url, String.class);
			JSONObject object = (JSONObject) parser.parse(result);
			Object[] keys = ((JSONObject) object.get("rates")).keySet().toArray();
			Object[] values = ((JSONObject) object.get("rates")).values().toArray();

			for (int i = 0; i < keys.length; i++) {
				Rate rate = new Rate();
				String decimalRate;
				Object[] arr = (((JSONObject) values[i]).values()).toArray();
				rate.setBase(keys[i].toString().substring(0, 3));
				rate.setTarget(keys[i].toString().substring(3, 6));
				rate.setTimeStamp((long) arr[1]);

				if (arr[0] instanceof Long) {
					decimalRate = ForexConstants.DECIMAL_FORMAT.format((double) (long) arr[0]);
				} else {
					decimalRate = ForexConstants.DECIMAL_FORMAT.format(arr[0]);
				}
				rate.setRate(Double.parseDouble(decimalRate));

				rateList.add(rate);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return rateList;
	}
	
}
