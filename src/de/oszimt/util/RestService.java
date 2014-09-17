package de.oszimt.util;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;



public class RestService {
	
	Client client = null;
	WebTarget target = null;
	
	JSONObject js = null;
	
	public RestService(){
		client = ClientBuilder.newClient();
		target = client.target("http://zip.elevenbasetwo.com/v2/DE");
		
		//Eine Abfrage wird direkt bei der Erzeugung des Objektes getaetigt, da dadurch meiner Meinung die Verbindung aufgebaut wird und
		//andere Abfragen schneller von statten gehen
		WebTarget resourceTarget = target.path("15711");
	    Invocation.Builder request = resourceTarget.request(MediaType.TEXT_PLAIN);
	    request.get();
	}
	
	/**
	 * Schickt eine Anfrage an einen Rest-Webservice
	 * 
	 * @param path
	 * @return
	 */
	public String getTown(String path){
	    WebTarget resourceTarget = target.path(path);
	    Invocation.Builder request = resourceTarget.request(MediaType.TEXT_PLAIN);
	    Response response = request.get();
	    
	    String json = response.readEntity(String.class);
	    
	    String town = extractTown(json);
	    
	    return town != null ? town : "";			
	}
	
	/**
	 * Durchsucht einen JSON String nach dem Schluessel 'city' und gibt den entsprechenden Key zurueck
	 * 
	 * @param jsonString
	 * @return den Key, oder null wenn nichts gefunden wurde
	 */
	private String extractTown(String jsonString){
		String town = null;
		if(jsonString.equals("{}"))
			return town;
		try {
			js = new JSONObject(jsonString);
			town = js.get("city").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return town;
	}

}
