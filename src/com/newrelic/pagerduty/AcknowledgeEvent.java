package com.newrelic.pagerduty;

class AcknowledgeEvent extends PagerDutyEvent {
  AcknowledgeEvent(String serviceKey, Incident incident) {
    super(serviceKey, "acknowledge");
    setDescription(incident.getDescription());
    setIncidentKey(incident.getIncidentKey());
    setDetails(incident.getDetails());
  }

}
