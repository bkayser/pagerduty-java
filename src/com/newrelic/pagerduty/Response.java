package com.newrelic.pagerduty;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Represents information passed back from the PagerDuty service.
 * 
 * @author Bill Kayser
 *
 */
public class Response {

  private final String warnings;
  private final String errors;
  private final String status;
  private final String message;
  private final String incidentKey;

  Response(Reader in) throws IOException, ParseException {
    JSONObject msg = (JSONObject) new JSONParser().parse(in);
    message = String.valueOf(msg.get("message"));
    status = String.valueOf(msg.get("status"));
    incidentKey = (String) msg.get("incident_key");
    warnings = readMessages(msg, "warnings");
    errors = readMessages(msg, "errors");
  }

  @SuppressWarnings("unchecked")
  private String readMessages(JSONObject msg, String key) {
    if (msg.containsKey(key)) {
      StringBuilder text = new StringBuilder();
      JSONArray warningArray = (JSONArray) msg.get(key);
      for (Iterator<Object> i = warningArray.iterator(); i.hasNext();) {
        text.append(String.valueOf(i.next()));
        if (i.hasNext())
          text.append("; ");
      }
      return text.toString();
    } else {
      return null;
    }
  }

  public boolean successful() {
    return status.equals("success") && warnings == null && errors == null;
  }

  public String getIncidentKey() {
    return incidentKey;
  }

  public String getWarnings() {
    return warnings;
  }

  public String getErrors() {
    return errors;
  }

  public String getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }
}
