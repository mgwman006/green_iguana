package com.maplewood.enums;

import lombok.Getter;

@Getter
public enum WeekDay {
  MON(1),
  TUE(2),
  WED(3),
  THU(4),
  FRI(5);

  private final int order;

  WeekDay(int order) {
    this.order = order;
  }

}
