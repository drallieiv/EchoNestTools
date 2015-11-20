package com.herazade.echonest.tools.core.project;

import java.io.File;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Track;
import com.echonest.api.v4.TrackAnalysis;
import com.echonest.api.v4.util.Commander;

@Service
public class EntProjectManager {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private EchoNestAPI en;

	public void runAnalysis(EntProject project) throws EchoNestException, IOException {

		Track track = getTrack(project);

		logger.info("Analysis URL : {}", track.getAnalysisURL());

		JSONObject analysisMap = (JSONObject) Commander.fetchURLAsJSON(track.getAnalysisURL());

		String jsonString = analysisMap.toJSONString();

		TrackAnalysis analysis = new TrackAnalysis(analysisMap);

		project.setAnalysis(analysis);

	}

	private Track getTrack(EntProject project) throws IOException, EchoNestException {

		if (!project.isFileSubmitted()) {

			File trackFile = project.getMp3File();

			// Generate Music Hash and send to EchoNest
			Track track = en.getKnownTrack(trackFile);

			if (track != null) {
				logger.info("Track Found : {}", track.getTitle());
			} else {
				logger.info("Upload Music");

				// Send Track to EchoNest
				track = en.uploadTrack(trackFile);
			}

			switch (track.getStatus()) {
			case COMPLETE:
				project.setTrackId(track.getID());
				return track;

			case PENDING:
				logger.warn("Analysis Pending : Try again later");
				return null;

			case ERROR:
			case UNAVAILABLE:
			case UNKNOWN:
				logger.error("Analysis Failed : status {}", track.getStatus());
				throw new EchoNestException(EchoNestException.SUCCESS, "Analysis Failed");
			default:
				return null;
			}

		} else {
			return en.newTrackByID(project.getTrackId());
		}

	}

}
