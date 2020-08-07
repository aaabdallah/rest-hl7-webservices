package edu.ruhhosp.rws.icis.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import edu.ruhhosp.rws.icis.data.Appointment;

@Path("/appointments")
public interface AppointmentResource
{
	@POST
	@Consumes("application/json")
	public Response createAppointment(Appointment appointment);
	
	@GET
	@Path("{id}")
	@Produces("application/json")
	public Appointment findAppointment(@PathParam("id") int id);
	
	@PUT
	@Path("{id}")
	@Consumes("application/json")
	public Response modifyAppointment(@PathParam("id") int id, Appointment modifiedAppointment);
	
	@PUT
	@Path("{id}/cancel")
	public Response cancelAppointment(@PathParam("id") int id);
}
