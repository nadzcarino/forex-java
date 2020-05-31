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

	public List<Rate> getForexData(String base) {

		String url = ForexConstants.BASE_URL + base;
		JSONParser parser = new JSONParser();
		List<Rate> rateList = new ArrayList<Rate>();
		try {
			String result = this.restTemplate.getForObject(url, String.class);
			JSONObject object = (JSONObject) parser.parse(result);
			String date = object.get("date").toString();
			Object[] keys = ((JSONObject) object.get("rates")).keySet().toArray();
			Object[] values = ((JSONObject) object.get("rates")).values().toArray();			
			for (int i = 0; i < keys.length; i++) {
				Rate rate = new Rate();
				rate.setTarget(keys[i].toString());
				rate.setDate(date);
				rate.setRate(Double.parseDouble(ForexConstants.DECIMAL_FORMAT.format(values[i])));
				rateList.add(rate);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return rateList;
	}

}
