package com.forex.service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

	@Autowired
	private JSONParser parser;

	public List<Rate> getOnlineData(String base) {

		String url = ForexConstants.BASE_URL + base;
		List<Rate> rateList = new ArrayList<Rate>();
		try {
			String result = this.restTemplate.getForObject(url, String.class);
			JSONObject object = (JSONObject) this.parser.parse(result);
			this.writeFile(object.toJSONString());
			rateList = this.rateMapper(object);

		} catch (Exception e) {
			System.out.println(e);
		}

		return rateList;
	}

	public void writeFile(String data) {
		FileWriter file = null;
		try {
			file = new FileWriter("default_data.json");
			file.write(data);
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				file.close();
			} catch (IOException e) {
				System.out.println(e);
			}
		}

	}

	public List<Rate> rateMapper(JSONObject object) {
		List<Rate> rateList = new ArrayList<Rate>();
		if (!object.isEmpty()) {
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
		}
		return rateList;
	}

	public List<Rate> getOfflineData() {
		List<Rate> rateList = new ArrayList<Rate>();		
		FileReader reader = null;
		
		try {
			 reader = new FileReader("default_data.json");
			JSONObject object = (JSONObject) this.parser.parse(reader);
			rateList = this.rateMapper(object);

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		} finally {
			try {				
				reader.close();
			} catch (IOException e) {
				System.out.println(e);
			}
		}

		return rateList;
	}

}
