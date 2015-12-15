package com.herazade.echonest.tools.core.project;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.support.AbstractMarshaller;
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

	@Autowired
	private AbstractMarshaller marshaller;

	/**
	 * Write an Echonest Remix Project as File
	 * 
	 * @param project
	 * @param outFile
	 * @return true if save is a success
	 * @throws IOException
	 * @throws TransformerConfigurationException
	 */
	public boolean saveAsFile(EntProject project, File outFile) throws IOException {

		try (FileOutputStream fos = new FileOutputStream(outFile);) {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Result temp = new StreamResult(baos);
			marshaller.marshal(project, temp);

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			Result result = new StreamResult(fos);
			Source tmpSource = new StreamSource(new ByteArrayInputStream(baos.toByteArray()));
			transformer.transform(tmpSource, result);
			
			return true;
		} catch (TransformerFactoryConfigurationError | TransformerException e) {
			logger.error("Error saving file : ", e);
			return false;
		}
	}

	/**
	 * Load an Echonest Remix Project from File
	 * 
	 * @param inFile
	 * @return Ent Project
	 * @throws XmlMappingException
	 * @throws IOException
	 */
	public EntProject loadProject(File inFile) throws XmlMappingException, IOException {
		try (FileInputStream fis = new FileInputStream(inFile);) {
			return (EntProject) marshaller.unmarshal(new StreamSource(fis));
		}
	}

	public TrackAnalysis runAnalysis(EntProject project) throws EchoNestException, IOException {

		Track track = getTrack(project);
		if(track == null){
			return null;
		}
		
		logger.debug("Analysis URL : {}", track.getAnalysisURL());

		JSONObject analysisMap = (JSONObject) Commander.fetchURLAsJSON(track.getAnalysisURL());

		String jsonString = analysisMap.toJSONString();

		TrackAnalysis analysis = new TrackAnalysis(analysisMap);

		project.setAnalysis(analysis);
		
		return analysis;
	}

	private Track getTrack(EntProject project) throws IOException, EchoNestException {

		if (!project.isFileSubmitted()) {

			File trackFile = project.getMp3File();

			// Generate Music Hash and send to EchoNest
			Track track = en.getKnownTrack(trackFile);

			if (track != null) {
				logger.info("Track Found : Title : {}", track.getTitle());
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
