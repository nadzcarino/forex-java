package com.forex.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
			System.out.println("API Server not available");
		}

		return rateList;
	}

	public List<Rate> getDefaultData() {
		JSONParser jsonParser = new JSONParser();
		List<Rate> rateList = new ArrayList<Rate>();
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("default_data.json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

			JSONObject object = (JSONObject) jsonParser.parse(reader);

			JSONArray dataArray = (JSONArray) object.get("default_data");
			for (Object datum : dataArray) {
				JSONObject obj = (JSONObject) datum;
				String decimalRate;
				Rate rate = new Rate();
				JSONObject objRate = (JSONObject) obj.get("rate");

				rate.setBase(obj.get("base").toString());
				rate.setTarget(obj.get("target").toString());
				rate.setTimeStamp((long) objRate.get("timestamp"));

				if (objRate.get("rate") instanceof Long) {
					decimalRate = ForexConstants.DECIMAL_FORMAT.format((double) (long) objRate.get("rate"));
				} else {
					decimalRate = ForexConstants.DECIMAL_FORMAT.format(objRate.get("rate"));
				}
				rate.setRate(Double.parseDouble(decimalRate));

				rateList.add(rate);

			}

		} catch (IOException | ParseException e) {
			// System.out.println(e);
		}
		return rateList;
	}
}
