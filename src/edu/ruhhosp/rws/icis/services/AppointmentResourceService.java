package edu.ruhhosp.rws.icis.services;

import java.net.URI;
import java.util.Iterator;
import java.util.Set;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import edu.ruhhosp.logging.Log;
import edu.ruhhosp.rws.icis.data.Appointment;
import edu.ruhhosp.rws.icis.resources.AppointmentResource;

@Stateless
@LocalBean
public class AppointmentResourceService implements AppointmentResource
{
	@PersistenceContext(unitName = "InhouseApps")
	EntityManager em;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Response createAppointment(Appointment appointment)
	{
		// convert input to appointment object
		Log.out.debug("Creating appointment " + appointment.getMrn());

		// int id = MockDataStore.getInstance().createObject(appointment);
		try
		{
			if (validate(appointment))
			{
				em.persist(appointment);
				em.flush();
			}
			return Response.created(URI.create("/" + appointment.getId())).build();
		}
		catch (Exception e)
		{
			Log.out.error("exception", e);
		}
		throw new WebApplicationException(Response.Status.BAD_REQUEST);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Appointment findAppointment(int id)
	{
		// Appointment appointment = (Appointment) MockDataStore.getInstance().readObject(id);
		Appointment appointment = em.find(Appointment.class, id);

		if (appointment == null) throw new WebApplicationException(Response.Status.NOT_FOUND);

		return appointment;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Response modifyAppointment(int id, Appointment modifiedAppointment)
	{
		Appointment oldAppointment = findAppointment(id);
		if (oldAppointment == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
		oldAppointment.load(modifiedAppointment);

		if (validate(oldAppointment))
		{
			em.merge(oldAppointment);
			em.flush();
		}

		return Response.ok().build();
	}

	@Override
	public Response cancelAppointment(int id)
	{
		Appointment oldAppointment = findAppointment(id);
		if (oldAppointment == null) throw new WebApplicationException(Response.Status.NOT_FOUND);

		oldAppointment.setStatus(Appointment.STATUS_CANCELLED);
		
		em.merge(oldAppointment);
		em.flush();

		return Response.ok().build();
	}
	
	private boolean validate(Appointment appointment)
	{
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Appointment>> constraintViolations = validator.validate(appointment);
		if (constraintViolations.size() > 0)
		{
			Iterator<ConstraintViolation<Appointment>> iterator = constraintViolations.iterator();
			while (iterator.hasNext())
			{
				ConstraintViolation<Appointment> cv = iterator.next();
				Log.out.debug(cv.getRootBeanClass().getName() + "." + cv.getPropertyPath() + " " + cv.getMessage());
			}
			return false;
		}
		
		return true;
	}
}
