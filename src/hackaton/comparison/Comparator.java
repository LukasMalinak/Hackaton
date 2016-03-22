package hackaton.comparison;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Comparator {

	private Comparator comparator;
	
	public Comparator() {
		this.comparator = new Comparator();
	}
	
	private String processRow(String baselineXmlFileRow, String newXmlFileRow) {
		String result = "";
		String baselineRow = baselineXmlFileRow;
		String newRow = newXmlFileRow;
		
		
		return result;
	}
	//treba zrobiti tak, ze kid je calj riadok rozdielnj, tak svihne do reportu &nbsp
	public void processXmlFiles(String pathToBaselineXmlFile, String pathToNewXmlFIle) {
		String baseRow;
		String newRow;
		int baseRowCount = this.baselineRowCount(pathToBaselineXmlFile);
		int newRowCount = this.newRowCount(pathToNewXmlFIle);
		
		try {
		BufferedReader brfBase = new BufferedReader(new FileReader(new File(pathToBaselineXmlFile)));
		BufferedReader brfNew = new BufferedReader(new FileReader(new File(pathToNewXmlFIle)));
		
		if (baseRowCount == newRowCount) {
			while ((baseRow = brfBase.readLine()) != null) {
				newRow = brfNew.readLine();
				this.processRow(baseRow, newRow);
			}
		} else {
			while ((baseRow = brfBase.readLine()) != null && (newRow = brfNew.readLine()) != null) {
				if (this.wholeRowIsDifferent(baseRow, newRow) == true) {
					if (baseRowCount < newRowCount) {
						//enter empty row in base part
					} else {
						//enter empty row in new report
					}
				} else if (this.hasWordsInCommon(baseRow, newRow)) {
					this.processRow(baseRow, newRow);
				}
			}
			
		}
		
		brfBase.close();
		brfNew.close();
		} catch (IOException ioexp) {
			ioexp.printStackTrace();
		}
	}
	
	private boolean hasWordsInCommon (String baseRow, String newRow) {
		boolean result = false;
		String base = baseRow.trim();
		List<String> baseWords = new ArrayList<String>();
		int beginIndex = 0;
		int endIndex = 0;	
		for(int i = 0; i < base.length(); i++) {
			Character baseChar = base.charAt(i);
			beginIndex = endIndex;
			if(Character.isUpperCase(baseChar) || Character.isSpaceChar(baseChar) || baseChar == '_' || baseChar == '=' || baseChar == '/' || baseChar == ':' || baseChar == '\"' || baseChar == '.') {
				endIndex = i;
				char beginChar = base.charAt(beginIndex);
				if (Character.isSpaceChar(beginChar) || beginChar == '<' || beginChar == '_' || beginChar == '=' || beginChar == '/' || beginChar == ':' || beginChar == '\"' || beginChar == '.') {
					beginIndex++;
				}
				String word = base.substring(beginIndex, endIndex);
				baseWords.add(word);
			} 
			
		}
		if (beginIndex < base.length()) {
			baseWords.add(base.substring(beginIndex, base.length()));
		}
          //tu ide druhj string

		String newResponse = newRow.trim();
		List<String> newWords = new ArrayList<String>();
		int begin = 0;
		int end = 0;	
		for(int i = 0; i < newResponse.length(); i++) {
			Character baseChar = newResponse.charAt(i);
			begin = end;
			if(Character.isUpperCase(baseChar) || Character.isSpaceChar(baseChar) || baseChar == '_' || baseChar == '=' || baseChar == '/' || baseChar == ':' || baseChar == '\"' || baseChar == '.') {
				end = i;
				char beginChar = newResponse.charAt(begin);
				if (Character.isSpaceChar(beginChar) || beginChar == '<' || beginChar == '_' || beginChar == '=' || beginChar == '/' || beginChar == ':' || beginChar == '\"' || beginChar == '.') {
					begin++;
				}
				String word = newResponse.substring(begin, end);
				newWords.add(word);
			} 
			
		}
		if (begin < newResponse.length()) {
			newWords.add(newResponse.substring(begin, newResponse.length()));
		}
		
		for (int i = 0; i < baseWords.size(); i++) {
			for (int j = 0; j < newWords.size(); j++) {
				if (baseWords.get(i).equals(newWords.get(j))) {
					result = true;
				}
			}
		}
		
		return result;
	}
	
	private boolean wholeRowIsDifferent (String baseRow, String newRow) {
		boolean result = false;
		
		for (int i = 0; i < baseRow.length(); i++) {
			for (int j = 0; j < newRow.length(); j++) {
				char baseChar = baseRow.charAt(i);
				char newChar = newRow.charAt(j);
				
				if (baseChar != newChar) {
					result = true;
				} else {
					result = false;
				}
			}
		}
		
		return result;
	}
	
	private int baselineRowCount (String baselineFilePath) {
		int result = 0;
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(new File(baselineFilePath)));
			while (bfr.readLine() != null) {
				result++;
			}
			bfr.close();
		} catch (IOException ioexp) {
			ioexp.printStackTrace();
		}
		
		return result;
	}
	
	private int newRowCount (String newFilePath) {
		int result = 0;
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(new File(newFilePath)));
			while (bfr.readLine() != null) {
				result++;
			}
			bfr.close();
		} catch (IOException ioexp) {
			ioexp.printStackTrace();
		}
		
		return result;
	}
}
