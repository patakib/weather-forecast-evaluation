package com.none.weather.utils;

import com.none.weather.history.models.entities.History;
import java.sql.Timestamp;
import java.util.Calendar;

public class Utils {


  public String addDaysForRequest(History history, int days) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(history.getDateTime());
    cal.add(Calendar.DATE, days);
    Timestamp ts = new Timestamp(cal.getTime().getTime());
    return ts.toLocalDateTime().toLocalDate().toString();
  }

  public String addDaysForRequest(Timestamp timestamp, int days) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(timestamp);
    cal.add(Calendar.DATE, days);
    Timestamp ts = new Timestamp(cal.getTime().getTime());
    return ts.toLocalDateTime().toLocalDate().toString();
  }
  public Timestamp addDays(Timestamp timestamp, int days) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(timestamp);
    cal.add(Calendar.DATE, days);
    return new Timestamp(cal.getTime().getTime());

  }
}
