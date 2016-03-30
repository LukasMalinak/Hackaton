package hackaton.soapUIQC;

import org.omg.CORBA.Environment;

import com.mercury.qualitycenter.otaclient.ClassFactory;
import com.mercury.qualitycenter.otaclient.IBaseFactory;
import com.mercury.qualitycenter.otaclient.IBaseField;
import com.mercury.qualitycenter.otaclient.IList;
import com.mercury.qualitycenter.otaclient.IRun;
import com.mercury.qualitycenter.otaclient.IRunFactory;
import com.mercury.qualitycenter.otaclient.IStep;
import com.mercury.qualitycenter.otaclient.ISysTreeNode;
import com.mercury.qualitycenter.otaclient.ITDConnection2;
import com.mercury.qualitycenter.otaclient.ITSTest;
import com.mercury.qualitycenter.otaclient.ITest;
import com.mercury.qualitycenter.otaclient.ITestFactory;
import com.mercury.qualitycenter.otaclient.ITestSet;
import com.mercury.qualitycenter.otaclient.ITestSetFactory;
import com.mercury.qualitycenter.otaclient.ITestSetTreeManager;
import com.mercury.qualitycenter.otaclient.ITreeManager;
import com4j.Com4jObject;

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
    
    //*****************************************************************************************
    //*	Name		    : fAddTest
    //*	Description	    : Add Test in Test Plan of Quality Center
    //*	Input Params	:
    //*	Return Values	: Test Object
    //*****************************************************************************************

    public Object fAddTest(String sTestName) {
        try {
            ITDConnection2 QCconn = getQcConn();
            Object[] sTestParam = new Object[4];
            if (QCconn.projectConnected() == true) {
                ITestFactory sTestFactory = (QCconn.testFactory()).queryInterface(ITestFactory.class);
                ITreeManager sTreeManager = (QCconn.treeManager()).queryInterface(ITreeManager.class);
                ISysTreeNode sSysTreeNode = (sTreeManager.nodeByPath("QC_TEST_PLAN" + "QC_PACKAGE")).queryInterface(ISysTreeNode.class);
                String sTestFactoryFilter = "select TS_TEST_ID from TEST where TS_NAME = '" + sTestName + "' and TS_SUBJECT = " + sSysTreeNode.nodeID();
                IList iListTestCase = sTestFactory.newList(sTestFactoryFilter);
                if (iListTestCase.count() == 0) {
                    sTestParam[0] = sTestName;
                    sTestParam[1] = "QUICKTEST_TEST";
                    sTestParam[2] = "lukasm";
                    sTestParam[3] = sSysTreeNode.nodeID();
                    IBaseField sBaseField = (sTestFactory.addItem(sTestParam)).queryInterface(IBaseField.class);
                    sBaseField.post();
                    return sBaseField.id();
                } else {
                    Com4jObject comObj = (Com4jObject) iListTestCase.item(1);
                    ITest sTest = comObj.queryInterface(ITest.class);
                    return sTest.id();
                }
            } else {
                System.out.println("QC Connection Failed");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //	//*****************************************************************************************
//	//*	Name		    : fUpdateTestStatQC
//	//*	Description	    : Update Test Case Status in Test Set of Quality Center
//	//*	Input Params	:
//	//*	Return Values	: Boolean
//	//*****************************************************************************************
    public boolean fUpdateTestStatQC(String sTestName, Object sTestid, String sStatus, String sTestSteps, String sOsBrowserVer) {
        try {
            ITDConnection2 QCconn = getQcConn();
            if (QCconn.projectConnected() == true) {
                ITestSetFactory sTestSetFactory = (QCconn.testSetFactory()).queryInterface(ITestSetFactory.class);
                ITestSetTreeManager sTestSetTreeManager = (QCconn.testSetTreeManager()).queryInterface(ITestSetTreeManager.class);
                ISysTreeNode sSysTreeNodeTestSet = (sTestSetTreeManager.nodeByPath("QC_TESTSET_FOLDER")).queryInterface(ISysTreeNode.class);

                String sTestSetFactoryFilter = "select CY_CYCLE_ID from CYCLE where CY_CYCLE = '" + "QC_TESTSET" + "' and CY_FOLDER_ID = " + sSysTreeNodeTestSet.nodeID();
                IList iListTestSets = sTestSetFactory.newList(sTestSetFactoryFilter);

                if (iListTestSets.count() == 0) {
                    System.out.println("Test Set does not exist");
                    return false;
                } else {
                    Com4jObject comObjTestSet = (Com4jObject) iListTestSets.item(1);
                    ITestSet sTestSet = comObjTestSet.queryInterface(ITestSet.class);
                    Com4jObject comObjTSTestSet = (Com4jObject) sTestSet.tsTestFactory();
                    IBaseFactory sBaseFactory = (IBaseFactory) comObjTSTestSet.queryInterface(IBaseFactory.class);

                    String strTestSetTestFactoryFilter = "SELECT *  FROM TESTCYCL WHERE tc_test_id = " + sTestid + " and TC_cycle_id = " + sTestSet.id();
                    IList iListTsTest = sBaseFactory.newList(strTestSetTestFactoryFilter);
                    ITSTest sTSTest;
                    if (iListTsTest.count() == 0) {
                        sTSTest = (sBaseFactory.addItem(sTestid)).queryInterface(ITSTest.class);
                        sTSTest.status("No Run");
                        sTSTest.post();
                    } else {
                        Com4jObject comObjTSTest = (Com4jObject) iListTsTest.item(1);
                        sTSTest = (ITSTest) comObjTSTest.queryInterface(ITSTest.class);
                    }
                    IRunFactory sRunFactory = (sTSTest.runFactory()).queryInterface(IRunFactory.class);
                    IRun sRun = (sRunFactory.addItem(sTSTest.id())).queryInterface(IRun.class);


                    //24-Feb-2014 - Dina - update fields: RN_USER_01=Desktop/Mobile, RN_USER_02=Browser Version <MSIE 10.x>, RN_OS_NAME = OS name <Windows 7>, <iOS 6.1.4>
                    //String OsBrowserVer[] = sOsBrowserVer.split(";");
                    //sRun.field("RN_USER_01", OsBrowserVer[0]);
                    //sRun.field("RN_USER_02", OsBrowserVer[1]);
                    //sRun.field("RN_OS_NAME", OsBrowserVer[2]);
                    sRun.status(sStatus);
                    //sRunid = sRun.id();
                    sRun.post();


                    String[] arrTestSteps = sTestSteps.split("\\<NS\\>");

                    for (int i = 0; i < arrTestSteps.length; i++) {
                        String[] arrStep = arrTestSteps[i].split("\\<ND\\>");
                        if (arrStep.length == 4) {
                            IBaseFactory sTestStepFactory = (sRun.stepFactory()).queryInterface(IBaseFactory.class);
                            IStep sTestStep = (sTestStepFactory.addItem(sTestName)).queryInterface(IStep.class);
                            sTestStep.field("ST_STEP_NAME", arrStep[0]);
                            sTestStep.field("ST_DESCRIPTION", arrStep[0]);
                            sTestStep.field("ST_EXPECTED", arrStep[1]);
                            sTestStep.field("ST_ACTUAL", arrStep[2]);
                            if (arrStep[3].equals("Pass")) {
                                sTestStep.status("Passed");
                            } else if (arrStep[3].equals("Fail")) {
                                sTestStep.status("Failed");
                            } else {
                                sTestStep.status("Passed");
                            }
                            sTestStep.post();
                        } else {
                            System.out.println("fUpdateTestStatQC: Step data should have 4 parameters. " + arrTestSteps[i]);
                        }
                    }
                }
            } else {
                System.out.println("QC Connection Failed");
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("Exception occured in fUpdateTestStatQC");
            e.printStackTrace();
            return false;
        }
    }
    
    
}
