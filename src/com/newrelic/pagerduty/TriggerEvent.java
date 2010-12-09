package com.newrelic.pagerduty;

class TriggerEvent extends PagerDutyEvent {

  TriggerEvent(String serviceKey, Incident incident) {
    super(serviceKey, "trigger");
    setDescription(incident.getDescription());
    setDetails(incident.getDetails());
    setIncidentKey(incident.getIncidentKey());
  }

}
