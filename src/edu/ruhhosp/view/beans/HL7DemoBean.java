package edu.ruhhosp.view.beans;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v23.message.SIU_S12;
import ca.uhn.hl7v2.model.v23.segment.MSH;
import ca.uhn.hl7v2.model.v23.segment.SCH;
import ca.uhn.hl7v2.parser.Parser;
import edu.ruhhosp.hl7server.HL7Server;
import edu.ruhhosp.logging.Log;

@ManagedBean(name="hl7DemoBean")
@RequestScoped
public class HL7DemoBean
{
	private String destinationHost = "10.249.9.19";
	private String destinationPort = "18081";
	private String rawMessageToSend = "MSH|^~\\&|PORTAL|PORTAL|CLOVERLEAF|CLOVERLEAF|20141230094337||SIU^S12|20191230094337|P|2.3\n"+
		"SCH||32160009||||Requested by clinic^Test again|707765^^^Family Medicine Follow-up||15|minute(s)|^^15^20150101103000^20150101104500\n"+
		"PID|1||5119902||ScriptPro^Test^Eleven|||||||||||||||||||||||||Active\n"+
		"PV1||O|638501^^F^^^^Outpatient||||||Attending Physician|||||||||O|195941940|\n"+
		"RGS|1||638501\n"+
		"AIL|1||638501^^F^^^^Outpatient|Appointment||20150101103000|15|minute(s)||||Scheduled\n"+
		"AIP|1||699615|||20150101103000|0|minute(s)|15|minute(s)\n";
	private String patientMrn;
	private String familyName, givenName, middleInitialOrName;
	private String responseToRawMessage;
	private String responseToGeneratedMessage;
	private String appointmentStartTime;

	public String getAppointmentStartTime()
	{
		return appointmentStartTime;
	}

	public void setAppointmentStartTime(String appointmentStartTime)
	{
		this.appointmentStartTime = appointmentStartTime;
	}

	public String getResponseToRawMessage()
	{
		return responseToRawMessage;
	}

	public void setResponseToRawMessage(String responseToRawMessage)
	{
		this.responseToRawMessage = responseToRawMessage;
	}

	public String getResponseToGeneratedMessage()
	{
		return responseToGeneratedMessage;
	}

	public void setResponseToGeneratedMessage(String responseToGeneratedMessage)
	{
		this.responseToGeneratedMessage = responseToGeneratedMessage;
	}

	public String getFamilyName()
	{
		return familyName;
	}

	public void setFamilyName(String familyName)
	{
		this.familyName = familyName;
	}

	public String getGivenName()
	{
		return givenName;
	}

	public void setGivenName(String givenName)
	{
		this.givenName = givenName;
	}

	public String getMiddleInitialOrName()
	{
		return middleInitialOrName;
	}

	public void setMiddleInitialOrName(String middleInitialOrName)
	{
		this.middleInitialOrName = middleInitialOrName;
	}

	public String getPatientMrn()
	{
		return patientMrn;
	}

	public void setPatientMrn(String patientMrn)
	{
		this.patientMrn = patientMrn;
	}

	public String getDestinationHost()
	{
		return destinationHost;
	}

	public void setDestinationHost(String destinationHost)
	{
		this.destinationHost = destinationHost;
	}

	public String getDestinationPort()
	{
		return destinationPort;
	}

	public void setDestinationPort(String destinationPort)
	{
		this.destinationPort = destinationPort;
	}

	public String getRawMessageToSend()
	{
		return rawMessageToSend;
	}

	public void setRawMessageToSend(String rawMessageToSend)
	{
		this.rawMessageToSend = rawMessageToSend;
	}

	public void sendRawMessage()
	{
		Log.out.info("Sending message\n\n" + rawMessageToSend);
		HL7Server server = HL7Server.getInstance();
		rawMessageToSend = rawMessageToSend.replace("\n", "\r\n");

		Message message = HL7Server.getInstance().toMessage(rawMessageToSend);
		Log.out.info("After parsing, we have: " + message);
		Message response = server.sendMessage(server.toMessage(rawMessageToSend), destinationHost, destinationPort);
		responseToRawMessage = server.toString(response);
		responseToGeneratedMessage = null;
	}
	
	public void generateAndSendMessage()
	{
		try
		{
			Log.out.info("Date: " + appointmentStartTime);
			Log.out.info("Generating and Sending message");
			Message message = generateMessage();
			Log.out.info("After parsing, we have: " + message);
			HL7Server server = HL7Server.getInstance();
			Message response = server.sendMessage(message, destinationHost, destinationPort);
			responseToGeneratedMessage = server.toString(response);
			responseToRawMessage = null;
		}
		catch (Exception e)
		{
			Log.out.error("Unable to generate and send message", e);
		}
	}

	private Message generateMessage()
	{
		try
		{
			SimpleDateFormat appointmentformatter = new SimpleDateFormat("MM/dd/yyyy");
			// Date appointmentDate = formatter.parse(appointmentStartTime);

			Date appointmentStartTimeDate = appointmentformatter.parse(appointmentStartTime);
			appointmentStartTimeDate.setMonth(appointmentStartTimeDate.getMonth()+1);
			int appointmentDurationMinutes = 15;
			Date appointmentEndTime = new Date(appointmentStartTimeDate.getTime());
			appointmentEndTime.setMinutes(appointmentEndTime.getMinutes()+appointmentDurationMinutes);
			String appointmentFillerID = (new StringBuffer(Long.toString(System.currentTimeMillis()))).reverse().substring(0, 8);
			String visitID = (new StringBuffer(Long.toString(System.currentTimeMillis()))).reverse().substring(0, 9);
			String pointOfCare = "638501";
			String personnelResourceID = "699615";

			SIU_S12 sius12 = new SIU_S12();
			sius12.initQuickstart("SIU", "S12", "P");

			// -----------------------------------------------------------
			// Populate the MSH Segment
			MSH mshSegment = sius12.getMSH();
			mshSegment.getSendingApplication().getNamespaceID().setValue("PORTAL");
			mshSegment.getMsh4_SendingFacility().getNamespaceID().setValue("PORTAL");
			mshSegment.getMsh5_ReceivingApplication().getNamespaceID().setValue("CLOVERLEAF");
			mshSegment.getMsh6_ReceivingFacility().getNamespaceID().setValue("CLOVERLEAF");

			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = formatter.format(new Date());
			mshSegment.getMsh7_DateTimeOfMessage().getTimeOfAnEvent().setValue(time);
			mshSegment.getMsh10_MessageControlID().setValue(time.replace("201", "301"));
			mshSegment.getMsh11_ProcessingID().getPt1_ProcessingID().setValue("T"); // Force Testing until instructed otherwise

			// mshSegment.getSequenceNumber().setValue("123");
			
			// -----------------------------------------------------------
			// Populate the SCH Segment
			SCH schSegment = sius12.getSCH();
			schSegment.getFillerAppointmentID().getEntityIdentifier().setValue(appointmentFillerID);
			schSegment.getEventReason().getIdentifier().setValue("Requested by clinic");
			schSegment.getEventReason().getText().setValue("Test again");
			schSegment.getAppointmentReason().getIdentifier().setValue("707765");
			schSegment.getAppointmentReason().getAlternateIdentifier().setValue("Family Medicine Follow-up");
			schSegment.getAppointmentDuration().setValue(""+appointmentDurationMinutes);
			schSegment.getAppointmentDurationUnits().getIdentifier().setValue("minute(s)");
			schSegment.getAppointmentTimingQuantity(0).getDuration().setValue(""+appointmentDurationMinutes);
			schSegment.getAppointmentTimingQuantity(0).getStartDateTime().getTimeOfAnEvent().setValue(formatter.format(appointmentStartTimeDate));
			schSegment.getAppointmentTimingQuantity(0).getEndDateTime().getTimeOfAnEvent().setValue(formatter.format(appointmentEndTime));
			//System.out.println( "Length: " + schSegment.getAppointmentTimingQuantity().length);

			// -----------------------------------------------------------
			// Populate the PID Segment
			sius12.getPATIENT(0).getPID().getPid1_SetIDPatientID().setValue("1");
			sius12.getPATIENT(0).getPID().getPid3_PatientIDInternalID(0).getID().setValue(patientMrn);
			sius12.getPATIENT(0).getPID().getPid5_PatientName(0).getFamilyName().setValue(familyName);
			sius12.getPATIENT(0).getPID().getPid5_PatientName(0).getGivenName().setValue(givenName);
			sius12.getPATIENT(0).getPID().getPid5_PatientName(0).getMiddleInitialOrName().setValue(middleInitialOrName);
			sius12.getPATIENT(0).getPID().getPid30_PatientDeathIndicator().setValue("Active");

			// -----------------------------------------------------------
			// Populate the PV1 Segment
			// sius12.getPATIENT(0).getPV1().getPv11_SetIDPatientVisit().setValue("1");
			sius12.getPATIENT(0).getPV1().getPatientClass().setValue("O");
			sius12.getPATIENT(0).getPV1().getAssignedPatientLocation().getPl1_PointOfCare().setValue(pointOfCare);
			sius12.getPATIENT(0).getPV1().getAssignedPatientLocation().getPl3_Bed().setValue("F");
			sius12.getPATIENT(0).getPV1().getAssignedPatientLocation().getPl7_Building().setValue("Outpatient");
			sius12.getPATIENT(0).getPV1().getConsultingDoctor(0).getXcn1_IDNumber().setValue("Attending Physician");
			sius12.getPATIENT(0).getPV1().getPatientType().setValue("O");
			sius12.getPATIENT(0).getPV1().getVisitNumber().getID().setValue(visitID);
			// sius12.getPATIENT(0).getPV1().getFinancialClass(0).getFc1_FinancialClass().setValue(" ");

			// -----------------------------------------------------------
			// Populate the RGS Segment
			sius12.getRESOURCES(0).getRGS().getRgs1_SetIDRGS().setValue("1");
			sius12.getRESOURCES(0).getRGS().getRgs3_ResourceGroupID().getCe1_Identifier().setValue(pointOfCare);
			
			// -----------------------------------------------------------
			// Populate the AIL Segment
			sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getSetIDAIL().setValue("1");
			sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getAil3_LocationResourceID().getPl1_PointOfCare().setValue(pointOfCare);
			sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getAil3_LocationResourceID().getPl3_Bed().setValue("F");
			sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getAil3_LocationResourceID().getPl7_Building().setValue("Outpatient");
			sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getAil4_LocationType().getCe1_Identifier().setValue("Appointment");
			sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getAil6_StartDateTime().getTimeOfAnEvent().setValue(formatter.format(appointmentStartTimeDate));
			sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getAil7_StartDateTimeOffset().setValue(""+appointmentDurationMinutes); // Is this right???
			sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getAil8_StartDateTimeOffsetUnits().getIdentifier().setValue("minute(s)");
			sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getAil12_FillerStatusCode().getIdentifier().setValue("Scheduled");
			//sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getAil4
			//sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getAil4

			// -----------------------------------------------------------
			// Populate the AIP Segment
			sius12.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getSetIDAIP().setValue("1");
			sius12.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getAip3_PersonnelResourceID().getXcn1_IDNumber().setValue(personnelResourceID);
			sius12.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getAip6_StartDateTime().getTimeOfAnEvent().setValue(formatter.format(appointmentStartTimeDate));
			sius12.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getAip7_StartDateTimeOffset().setValue("0");
			sius12.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getAip8_StartDateTimeOffsetUnits().getIdentifier().setValue("minute(s)");
			sius12.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getAip9_Duration().setValue(""+appointmentDurationMinutes);
			sius12.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getAip10_DurationUnits().getIdentifier().setValue("minute(s)");
			
			return sius12;
		}
		catch (Exception e)
		{
			Log.out.error("Unable to generate message", e);
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static void testMessage()
	{
		try
		{
			Date appointmentStartTime = new Date();
			appointmentStartTime.setMonth(appointmentStartTime.getMonth()+1);
			int appointmentDurationMinutes = 15;
			Date appointmentEndTime = new Date(appointmentStartTime.getTime());
			appointmentEndTime.setMinutes(appointmentEndTime.getMinutes()+appointmentDurationMinutes);
			String appointmentFillerID = (new StringBuffer(Long.toString(System.currentTimeMillis()))).reverse().substring(0, 8);
			String visitID = (new StringBuffer(Long.toString(System.currentTimeMillis()))).reverse().substring(0, 9);
			String pointOfCare = "638501";
			String personnelResourceID = "699615";

			SIU_S12 sius12 = new SIU_S12();
			sius12.initQuickstart("SIU", "S12", "P");

			// -----------------------------------------------------------
			// Populate the MSH Segment
			MSH mshSegment = sius12.getMSH();
			mshSegment.getSendingApplication().getNamespaceID().setValue("PORTAL");
			mshSegment.getMsh4_SendingFacility().getNamespaceID().setValue("PORTAL");
			mshSegment.getMsh5_ReceivingApplication().getNamespaceID().setValue("CLOVERLEAF");
			mshSegment.getMsh6_ReceivingFacility().getNamespaceID().setValue("CLOVERLEAF");

			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = formatter.format(new Date());
			mshSegment.getMsh7_DateTimeOfMessage().getTimeOfAnEvent().setValue(time);
			mshSegment.getMsh10_MessageControlID().setValue(time.replace("201", "301"));
			mshSegment.getMsh11_ProcessingID().getPt1_ProcessingID().setValue("T"); // Force Testing until instructed otherwise

			// mshSegment.getSequenceNumber().setValue("123");
			
			// -----------------------------------------------------------
			// Populate the SCH Segment
			SCH schSegment = sius12.getSCH();
			schSegment.getFillerAppointmentID().getEntityIdentifier().setValue(appointmentFillerID);
			schSegment.getEventReason().getIdentifier().setValue("Requested by clinic");
			schSegment.getEventReason().getText().setValue("Test again");
			schSegment.getAppointmentReason().getIdentifier().setValue("707765");
			schSegment.getAppointmentReason().getAlternateIdentifier().setValue("Family Medicine Follow-up");
			schSegment.getAppointmentDuration().setValue(""+appointmentDurationMinutes);
			schSegment.getAppointmentDurationUnits().getIdentifier().setValue("minute(s)");
			schSegment.getAppointmentTimingQuantity(0).getDuration().setValue(""+appointmentDurationMinutes);
			schSegment.getAppointmentTimingQuantity(0).getStartDateTime().getTimeOfAnEvent().setValue(formatter.format(appointmentStartTime));
			schSegment.getAppointmentTimingQuantity(0).getEndDateTime().getTimeOfAnEvent().setValue(formatter.format(appointmentEndTime));
			//System.out.println( "Length: " + schSegment.getAppointmentTimingQuantity().length);

			// -----------------------------------------------------------
			// Populate the PID Segment
			sius12.getPATIENT(0).getPID().getPid1_SetIDPatientID().setValue("1");
			sius12.getPATIENT(0).getPID().getPid3_PatientIDInternalID(0).getID().setValue("5119902");
			sius12.getPATIENT(0).getPID().getPid5_PatientName(0).getFamilyName().setValue("ScriptPro");
			sius12.getPATIENT(0).getPID().getPid5_PatientName(0).getGivenName().setValue("Test");
			sius12.getPATIENT(0).getPID().getPid5_PatientName(0).getMiddleInitialOrName().setValue("Eleven");
			sius12.getPATIENT(0).getPID().getPid30_PatientDeathIndicator().setValue("Active");

			// -----------------------------------------------------------
			// Populate the PV1 Segment
			// sius12.getPATIENT(0).getPV1().getPv11_SetIDPatientVisit().setValue("1");
			sius12.getPATIENT(0).getPV1().getPatientClass().setValue("O");
			sius12.getPATIENT(0).getPV1().getAssignedPatientLocation().getPl1_PointOfCare().setValue(pointOfCare);
			sius12.getPATIENT(0).getPV1().getAssignedPatientLocation().getPl3_Bed().setValue("F");
			sius12.getPATIENT(0).getPV1().getAssignedPatientLocation().getPl7_Building().setValue("Outpatient");
			sius12.getPATIENT(0).getPV1().getConsultingDoctor(0).getXcn1_IDNumber().setValue("Attending Physician");
			sius12.getPATIENT(0).getPV1().getPatientType().setValue("O");
			sius12.getPATIENT(0).getPV1().getVisitNumber().getID().setValue(visitID);
			// sius12.getPATIENT(0).getPV1().getFinancialClass(0).getFc1_FinancialClass().setValue(" ");

			// -----------------------------------------------------------
			// Populate the RGS Segment
			sius12.getRESOURCES(0).getRGS().getRgs1_SetIDRGS().setValue("1");
			sius12.getRESOURCES(0).getRGS().getRgs3_ResourceGroupID().getCe1_Identifier().setValue(pointOfCare);
			
			// -----------------------------------------------------------
			// Populate the AIL Segment
			sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getSetIDAIL().setValue("1");
			sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getAil3_LocationResourceID().getPl1_PointOfCare().setValue(pointOfCare);
			sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getAil3_LocationResourceID().getPl3_Bed().setValue("F");
			sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getAil3_LocationResourceID().getPl7_Building().setValue("Outpatient");
			sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getAil4_LocationType().getCe1_Identifier().setValue("Appointment");
			sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getAil6_StartDateTime().getTimeOfAnEvent().setValue(formatter.format(appointmentStartTime));
			sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getAil7_StartDateTimeOffset().setValue(""+appointmentDurationMinutes); // Is this right???
			sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getAil8_StartDateTimeOffsetUnits().getIdentifier().setValue("minute(s)");
			sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getAil12_FillerStatusCode().getIdentifier().setValue("Scheduled");
			//sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getAil4
			//sius12.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getAil4

			// -----------------------------------------------------------
			// Populate the AIP Segment
			sius12.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getSetIDAIP().setValue("1");
			sius12.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getAip3_PersonnelResourceID().getXcn1_IDNumber().setValue(personnelResourceID);
			sius12.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getAip6_StartDateTime().getTimeOfAnEvent().setValue(formatter.format(appointmentStartTime));
			sius12.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getAip7_StartDateTimeOffset().setValue("0");
			sius12.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getAip8_StartDateTimeOffsetUnits().getIdentifier().setValue("minute(s)");
			sius12.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getAip9_Duration().setValue(""+appointmentDurationMinutes);
			sius12.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getAip10_DurationUnits().getIdentifier().setValue("minute(s)");
			// Populate the PID Segment
			//SIU_S12_PATIENT sius12Patient = sius12.getPATIENT();
			//sius12Patient.getPID().getPatientName().getFamilyName().setValue("Doe");
			//pid.getPatientName(0).getGivenName().setValue("John");
			// pid.getPatientIdentifierList(0).getID().setValue("123456");
			
			HapiContext context = new DefaultHapiContext();
			Parser parser = context.getPipeParser();
			String encodedMessage = parser.encode(sius12);
			System.out.println("Printing ER7 Encoded Message:");
			System.out.println(encodedMessage);
		}
		catch (Exception e)
		{
			Log.out.error("Exception", e);
		}
	}

	public static void testParse()
	{
		try
		{
			FileReader reader = new FileReader("C:/workspaces/glassfish/HL7Demo/testMessage.txt");
			int c = 0;
			StringBuffer buff = new StringBuffer();
			while ((c = reader.read()) != -1)
			{
				buff.append((char) c);
			}
			Log.out.info("Read message:\n\n" + buff);
			
			HL7Server.getInstance().initialize(15101, false, 15102, false);
			
			Message message = HL7Server.getInstance().toMessage(buff.toString());
			Log.out.info(message.getVersion());
		}
		catch (Exception e)
		{
			Log.out.error("Exception", e);
		}
	}

	public static void main(String args[])
	{
		// testMessage();
		testParse();
	}
}
