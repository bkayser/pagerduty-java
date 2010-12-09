package com.newrelic.pagerduty;

import org.json.simple.JSONObject;

abstract class PagerDutyEvent {

  private final String serviceKey;
  private final String type;
  private String incidentKey;
  private String description;
  private JSONObject details;

  PagerDutyEvent(String serviceKey, String type) {
    this.serviceKey = serviceKey;
    this.type = type;
  }
  
  public String getIncidentKey() {
    return incidentKey;
  }

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
    return details;
  }

  public void setDetails(JSONObject details) {
    this.details = details;
  }

  public String getServiceKey() {
    return serviceKey;
  }
  
  @SuppressWarnings("unchecked")
  String encode() {
    JSONObject o = new JSONObject();
    o.put("service_key", serviceKey);
    o.put("event_type", type);
    if (incidentKey != null) o.put("incident_key", incidentKey);
    if (description != null) o.put("description", description);
    if (details != null) o.put("details", details);    
    return o.toJSONString();
  }
  
}
