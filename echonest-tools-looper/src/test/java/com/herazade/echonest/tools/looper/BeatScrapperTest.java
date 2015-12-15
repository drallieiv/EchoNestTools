package com.herazade.echonest.tools.looper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.echonest.api.v4.TimedEvent;
import com.echonest.api.v4.TrackAnalysis;
import com.herazade.echonest.tools.looper.jump.JumpMap;

public class BeatScrapperTest {

	// Logger
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void testGetJumpMap() throws FileNotFoundException, IOException, ParseException, URISyntaxException, ScrapperException {

		// Read dumped Json
		JSONParser parser = new JSONParser();
		File analysisJsonFile = new File(this.getClass().getResource("/test-data.json").toURI());
		JSONObject analysisMap = (JSONObject) parser.parse(new FileReader(analysisJsonFile));
		TrackAnalysis analysis = new TrackAnalysis(analysisMap);

		BeatScrapper scrapper = new BeatScrapper(analysis);
		scrapper.setBackWardOnly(true);

		int nbJumps = 10;
		JumpMap jumps = scrapper.scrapForJumps(nbJumps);
		
		JumpMap filteredJumps = jumps.filterEmpty();

		logger.debug("Jumps found : {}", filteredJumps);
	}

}
