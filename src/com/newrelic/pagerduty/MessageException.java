package com.newrelic.pagerduty;


public class MessageException extends Exception {

  public MessageException(String string, Throwable e) {
    super(string, e);
  }

  public MessageException(String string) {
    super(string);
  }
  private static final long serialVersionUID = 1L;

}
