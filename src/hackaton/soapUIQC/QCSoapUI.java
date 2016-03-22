package hackaton.soapUIQC;

import com.mercury.qualitycenter.otaclient.ClassFactory;
import com.mercury.qualitycenter.otaclient.ITDConnection2;

public class QCSoapUI {
	private QCSoapUI qcSoapUI;
	private ITDConnection2 qcConn;
	//create connection
	//update tc + stps + result
	// create new TC in QC
	public QCSoapUI() {
		this.qcSoapUI = new QCSoapUI();
	}
	
	//create connection
    private void createConnectionToQC() {
    	this.qcConn = ClassFactory.createTDConnection();


    	qcConn.initConnectionEx("http://tlvalm01:8080/qcbin");

        System.out.println("Was connection to HP ALM QC successfull ? : " + qcConn.connected());

        qcConn.login("lukasm", "8kBGPwC9.");

        System.out.println ("Was login to HP ALM QC successfull ? : " + qcConn.loggedIn());

        qcConn.connect("ACTIMIZE", "ALM_ACTIMIZE_CDD");

        System.out.println("Was Project in HP ALM successfully connected ? :  " + qcConn.projectConnected());
    }
    
    //zbytok doladu dakoli, nemau b ti problem zos tm ..zobrati TC a podla togo jak skincilo, dirvati to
    
    public ITDConnection2 getQcConn() {
		return qcConn;
	}
    
    public QCSoapUI getQcSoapUI() {
		return qcSoapUI;
	}
    
    
}
