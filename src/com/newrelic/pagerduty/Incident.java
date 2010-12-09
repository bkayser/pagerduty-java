package com.newrelic.pagerduty;

import org.json.simple.JSONObject;

/**
 * Represents a PageDuty incident.  Create an instance and use it in in the trigger
 * as well as acknowledge and resolve messages.
 * 
 * The incident takes details but as near as I can tell this information isn't recorded
 * by PagerDuty right now.
 * 
 * @author Bill Kayser
 *
 */
public class Incident {
  private String incidentKey;
  private String description;
  private JSONObject details = new JSONObject();

  public Incident(String description, String incidentKey) {
    this.description = description;
    this.incidentKey = incidentKey;
  }
  
  public Incident(String description) {
    this(description, null);
  }
  
  public void setDetails(JSONObject details) {
    this.details = details;
  }

  public String getIncidentKey() {
    return incidentKey;
  }

  /**
   * Set the incident key to any string you want, or leave
   * it null to assume a unique id provided by the server.
   * 
   * @param incidentKey
   */
  public void setIncidentKey(String incidentKey) {
    this.incidentKey = incidentKey;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public JSONObject getDetails() {
    return details.isEmpty() ? null : details;
  }

  /**
   * Append the given key and value to the detail object.
   * @param key a label
   * @param info should be a string or a JSON subclass
   */
  @SuppressWarnings("unchecked")
  public void addDetail(String key, Object info) {
    details.put(key, info);
  }
}
