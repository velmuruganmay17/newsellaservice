package camel.sellacast.rest;

import java.util.ResourceBundle;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import camel.sellacast.Track;

@Path("/json/metallica")
public class JSONService {

	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Track getTrackInJSON() {

		ResourceBundle resourceBundle = ResourceBundle.getBundle("SellaCast");
		String mongodb_uri = resourceBundle.getString("mongodb_uri");
		int mongodb_port = Integer.parseInt(resourceBundle.getString("mongodb_port"));
		String mongodb_name = resourceBundle.getString("mongodb_name");
		 
		System.out.println("mongodb_uri >> "+mongodb_uri);
		System.out.println("mongodb_port >> "+mongodb_port);
		System.out.println("mongodb_name >> "+mongodb_name);
		Track track = new Track();
		track.setTitle("Enter Sandman");
		track.setSinger("Metallica");

		return track;

	}

	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createTrackInJSON(Track track) {

		String result = "Track saved : " + track;
		return Response.status(201).entity(result).build();
		
	}
	
}