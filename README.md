PagerDuty Java API Helper
======================================

Utility classes for managing incidents using the PagerDuty REST API in a 
java application.

Building
======================================

    ant jar

Add pagerduty.jar and json_simple-1.1.jar to your classpath

The project contains metafiles for use in Eclipse.  The build file doesn't 
run unit tests but they are easily run in Eclipse.  Note that to run
the unit tests you need to connect to the PagerDuty server with a license
key.

Using
======================================

Refer to the [javadocs](http://bkayser.github.com/pagerduty-java) for documentation.

Create an Incident object and use a PagerDutyAPI instance to trigger, acknowledge
and resolve incidents from your application.

See the PagerDutyAPITest class for examples.

Bill Kayser, December 2010

