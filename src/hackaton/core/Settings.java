package hackaton.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import hackaton.reporting.Report;

public class Settings {
	private String pathToBaselineResponses;
	private String pathToNewResponses;
	private Report report;
	public Settings() {
		loadConfigurationFile();
		this.report = new Report(getPathToNewResponses(), getPathToBaselineResponses());
		report.createReport();
	}
	
	private void loadConfigurationFile() {
		FileReader frd;
		BufferedReader brd;
		String row;
		try {
			frd = new FileReader(getDesktopPath() + "\\hackatonConfFile.txt");
			brd = new BufferedReader(frd);
			
			while ((row = brd.readLine()) != null) {
				if(row.contains("baselineResponses")) {
					setPathToBaselineResponses(row.substring(row.indexOf(":") + 1));
				} else if (row.contains("newResponses")) {
					setPathToNewResponses(row.substring(row.indexOf(":") + 1));
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public String getPathToBaselineResponses() {
		return pathToBaselineResponses;
	}
	
	public String getPathToNewResponses() {
		return pathToNewResponses;
	}
	
	public void setPathToBaselineResponses(String pathToBaselineResponses) {
		this.pathToBaselineResponses = pathToBaselineResponses;
	}
	
	public void setPathToNewResponses(String pathToNewResponses) {
		this.pathToNewResponses = pathToNewResponses;
	}
	
	private String getDesktopPath() {
		return System.getProperty("user.home") + "\\Desktop";
	}
}
