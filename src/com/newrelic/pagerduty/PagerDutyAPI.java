package com.newrelic.pagerduty;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.simple.parser.ParseException;

/**
 * Utility for managing incidents using PagerDuty service.
 * 
 * @author Bill Kayser
 */
public class PagerDutyAPI {

  final URL            serviceURL;
  private final String serviceKey;

  /**
   * Create the api with the specified base URL.
   * 
   * @param serviceKey
   * @param baseURL
   */
  public PagerDutyAPI(String serviceKey, URL baseURL) {
    this.serviceKey = serviceKey;
    this.serviceURL = baseURL;
  }

  /**
   * Create an API facade based on the URL
   * https://events.pagerduty.com/generic/2010-04-15/create_event.json
   * @param serviceKey the API key obtained from the PagerDuty service definition.
   */
  public PagerDutyAPI(String serviceKey) {
    this.serviceKey = serviceKey;
    try {
      serviceURL = new URL("https://events.pagerduty.com/generic/2010-04-15/create_event.json");
    } catch (MalformedURLException e) {
      // This is a programmer error if it occurs
      throw new RuntimeException("Bad PagerDuty API URL", e);
    }
  }

  /**
   * Trigger the incident. If the incident doesn't have an incidentKey then one
   * is obtained from the service and assigned to the incident instance.
   * 
   * @param incident
   * @return a Response object which may have warnings.
   * @throws MessageException if there is any problem with the message at all.
   */
  public Response trigger(Incident incident) throws MessageException {
    return sendEvent(incident, new TriggerEvent(serviceKey, incident));
  }

  /**
   * Acknowledge the incident.  Use the same incident passed to the trigger call.
   * 
   * @param incident
   * @return the Response object
   * @throws MessageException if there is any problem with the message
   */
  public Response acknowledge(Incident incident) throws MessageException {
    return sendEvent(incident, new AcknowledgeEvent(serviceKey, incident));
  }

  /**
   * Set the issue to resolved.  Use the same incident passed to the trigger call.
   * 
   * @param incident
   * @return the Response object
   * @throws MessageException if there is any problem with the message
   */
  public Response resolve(Incident incident) throws MessageException {
    return sendEvent(incident, new ResolveEvent(serviceKey, incident));
  }
  
  /** Send a message to the service to verify the serviceKey and serviceURL 
   * @throws MessageException if there is a problem with the service.  This will
   * be a 400 response if the service key is no good. 
   * */
  public void verify() throws MessageException {
    Incident bogusIncident = new Incident("Nonexistent incident", "nonexistent key");
    acknowledge(bogusIncident);
  }

  /*
   * Send the event.  Unfortunately since we're using the J2SE http client
   * we can't dig out the message details when there is a 400 response.  
   */
  private Response sendEvent(Incident incident, PagerDutyEvent event) throws MessageException {
    URL url = null;
    try {
      url = new URL(serviceURL, "create_event.json");
      HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
      openConnection.setRequestMethod("POST");
      openConnection.setDoOutput(true);
      OutputStream outputStream = openConnection.getOutputStream();
      PrintStream out = new PrintStream(outputStream);
      out.print(event.encode());
      out.close();
      InputStreamReader in = new InputStreamReader(openConnection.getInputStream());
      Response response = new Response(in);
      if (response.getIncidentKey() != null)
        incident.setIncidentKey(response.getIncidentKey());
      return response;
    } catch (FileNotFoundException e) {
      throw new MessageException("Request returned NOT FOUND");
    } catch (MalformedURLException e) {
      throw new MessageException("Error building message", e);
    } catch (ProtocolException e) {
      throw new MessageException("Error building message", e);
    } catch (IOException e) {
      throw new MessageException("Error communicating with PagerDuty server: " + e, e);
    } catch (ParseException e) {
      throw new MessageException("Error parsing response from server: " + e, e);
    }
  }
}
