package hackaton.reporting;

import hackaton.reporting.html.Body;
import hackaton.reporting.html.Footer;
import hackaton.reporting.html.Header;

public class Report {
	private String pathToNewResponses;
	private String pathToBaselineResponses;	
	private Footer footer;
	private Header header;
	private Body body;
	
	public Report(String pathToNewResponses, String pathToBaselineResponses) {
		this.pathToNewResponses = pathToNewResponses;
		this.pathToBaselineResponses = pathToBaselineResponses;
	}

	//tu bude vskladanj novj report
	public void createReport() {
		this.footer = new Footer();
		this.header = new Header();
		this.body = new Body();
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
}
