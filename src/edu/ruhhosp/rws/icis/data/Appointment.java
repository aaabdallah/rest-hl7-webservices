package edu.ruhhosp.rws.icis.data;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(name="ICISWS_APPTS")
public class Appointment extends BaseValueObject implements Serializable
{
	private static final long serialVersionUID = -6135530245621196650L;
	static public int STATUS_ACTIVE = 0;
	static public int STATUS_CANCELLED = 1;
	/*
	 * IF A FIELD IS ADDED TO THIS CLASS, BE SURE TO UPDATE THE "load" METHOD.
	 * IF A FIELD IS ADDED TO THIS CLASS, BE SURE TO UPDATE THE "load" METHOD.
	 */
	
	/* ***********************************************************************/
	/* ***** INSTANCE FIELDS *************************************************/
	@Id
	@GeneratedValue( generator="IcisWSSeq" )
	@SequenceGenerator(name="IcisWSSeq",sequenceName="ICISWS_SEQ",allocationSize=1)
	protected Integer id;	

	@NotNull protected Integer status = STATUS_ACTIVE;

	@NotNull protected String reasonChoice = null; // SIU S12 - SCH - 6.1

	protected String reasonText = null; // SIU S12 - SCH - 6.2

	@NotNull protected String reasonSynCode = null; // SIU S12 - SCH - 7.4 (assume 7.1 can be looked up)

	@NotNull @Min(1) @Max(480)
	protected Integer durationMinutes = null; // SIU S12 - SCH - 10

	@NotNull @Temporal(TemporalType.TIMESTAMP)
	protected Date startDateTime = null; // SIU S12 - SCH - 11.4
	
	@NotNull 
	protected String mrn = null; // SIU S12 - PID - 3
	
	@NotNull
	protected String patientLocation = null; // SIU S12 - PV1 - 3
	
	@NotNull
	protected String personnelResCode = null; // SIU S12 - AIP - 3

	/* ***** INSTANCE METHODS ************************************************/

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getReasonChoice()
	{
		return reasonChoice;
	}

	public void setReasonChoice(String reasonChoice)
	{
		this.reasonChoice = reasonChoice;
	}

	public String getReasonText()
	{
		return reasonText;
	}

	public void setReasonText(String reasonText)
	{
		this.reasonText = reasonText;
	}

	public String getReasonSynCode()
	{
		return reasonSynCode;
	}

	public void setReasonSynCode(String reasonSynCode)
	{
		this.reasonSynCode = reasonSynCode;
	}

	public Integer getDurationMinutes()
	{
		return durationMinutes;
	}

	public void setDurationMinutes(Integer durationMinutes)
	{
		this.durationMinutes = durationMinutes;
	}

	public Date getStartDateTime()
	{
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime)
	{
		this.startDateTime = startDateTime;
	}

	public String getMrn()
	{
		return mrn;
	}

	public void setMrn(String mrn)
	{
		this.mrn = mrn;
	}

	public String getPatientLocation()
	{
		return patientLocation;
	}

	public void setPatientLocation(String patientLocation)
	{
		this.patientLocation = patientLocation;
	}

	public String getPersonnelResCode()
	{
		return personnelResCode;
	}

	public void setPersonnelResCode(String personnelResCode)
	{
		this.personnelResCode = personnelResCode;
	}

	/**
	 * Loads the source's fields into this object.
	 * @param source
	 */
	public void load(Appointment source)
	{
		// DO NOT LOAD THE ID
		status = source.status;
		reasonChoice = source.reasonChoice;
		reasonText = source.reasonText;
		reasonSynCode = source.reasonSynCode;
		durationMinutes = source.durationMinutes;
		startDateTime = source.startDateTime;
		mrn = source.mrn;
		patientLocation = source.patientLocation;
		personnelResCode = source.personnelResCode;
	}
}
