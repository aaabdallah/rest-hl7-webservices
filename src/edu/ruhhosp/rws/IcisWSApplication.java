package edu.ruhhosp.rws;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import edu.ruhhosp.rws.icis.services.AppointmentResourceService;

@ApplicationPath("")
public class IcisWSApplication extends Application
{
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> classes = new HashSet<Class<?>>();

	public IcisWSApplication()
	{
		classes.add(AppointmentResourceService.class);
	}

	// Stateless service instances
	@Override
	public Set<Class<?>> getClasses()
	{
		return classes;
	}

	// Stateful service instances
	@Override
	public Set<Object> getSingletons()
	{
		return singletons;
	}
}
