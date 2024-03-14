package com.reqserv.requestservice.controller.helpers;

public enum SortOrder {
  ASC("ascending"),
  DESC("descending"),
  NO_SORT("no_sorting");

  private final String displayName;

  SortOrder(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
