// Copyright 2012 Square, Inc.
package com.tripadvisor;

import java.util.Date;

public class WeekDescriptor {
  private final int weekInYear;
  private final int year;
  private final int month;
  private final Date date;
  private String label;

  public WeekDescriptor(int weekInYear, int year, Date date, String label) {
    this.weekInYear = weekInYear;
    this.year = year;
    this.date = date;
    this.label = label;
    //noinspection deprecation
    this.month = date.getMonth();
  }

  public int getWeekInYear() {
    return weekInYear;
  }

  public int getYear() {
    return year;
  }

  public int getMonth() {
      return month;
  }

  public Date getDate() {
    return date;
  }

    @Override public String toString() {
    return "WeekDescriptor{"
        + "label='"
        + label
        + '\''
        + ", weekInYear="
        + weekInYear
        + ", year="
        + year
        + '}';
  }
}
