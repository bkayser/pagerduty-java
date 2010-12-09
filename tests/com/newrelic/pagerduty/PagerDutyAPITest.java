package com.newrelic.pagerduty;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

/**
 * To run these tests, use a valid service key. This will create incidents in
 * that service.
 * 
 * @author Bill Kayser
 * 
 */
public class PagerDutyAPITest {

  private PagerDutyAPI api;

  @Before
  public void setupAPI() {
    String serviceKey = System.getProperty("APIKEY", "5d5a0b30e550012d488912313d009e57");
    api = new PagerDutyAPI(serviceKey);
    try {
      api.verify();
    } catch (MessageException e) {
      throw new RuntimeException("The API service key does not appear to be valid.  Assign a valid key to the system property APIKEY and try again", e);
    }
  }

  @Test
  public void testIncidentTrigger() throws MessageException {
    Incident testIncident = new Incident("An resolved incident with a fixed attribute and new incident key");
    api.trigger(testIncident);
    testIncident.addDetail("Fixed", new Date().toString());
    Response result = api.resolve(testIncident);
    assertNull(result.getWarnings());
    assertEquals("success", result.getStatus());
  }

  @Test
  public void testTriggerDetailedIncident() throws MessageException {
    Incident testIncident = new Incident("A detailed open test incident", "test00");
    testIncident.addDetail("WHY", "because it happened");
    testIncident.addDetail("How often", "3");
    api.trigger(testIncident);
  }

  @Test
  public void testIncidentReraised() throws Exception {
    Incident testIncident = new Incident("A re-raised open incident", "test01");
    api.trigger(testIncident);
    api.acknowledge(testIncident);
    api.resolve(testIncident);
    api.trigger(testIncident);
  }

  @Test
  public void testIncidentAcknowledge() throws Exception {
    Incident testIncident = new Incident("An acknowledged incident", "test02");
    api.trigger(testIncident);
    api.acknowledge(testIncident);
  }

  @Test
  public void testBadResolvedMessage() throws Exception {
    Incident testIncident = new Incident("An unresolved test incident", "test03");
    api.trigger(testIncident);
    testIncident.setIncidentKey("bogus");
    Response resolve = api.resolve(testIncident);
    assertEquals(null, resolve.getErrors());
    assertFalse(resolve.successful());
    assertEquals("no incident found matching incident_key bogus", resolve.getWarnings());
    assertEquals("Event was dropped", resolve.getMessage());
    assertEquals("success", resolve.getStatus());
  }

  @Test
  public void testVerify() throws Exception {
    api.verify();
  }

  @Test(expected = MessageException.class)
  public void testVerifyFailure() throws Exception {
    // now use an invalid key
    api = new PagerDutyAPI("5d5a0b30e550012d488912313d009e57");
    api.verify();
  }
}
