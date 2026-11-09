package com.carrental.app.carmanagement.domainlayer;

// Some constants are only used through JSON requests and the database, not in Java code,
// so IntelliJ's "never used" warning doesn't apply.
@SuppressWarnings("unused")
public enum CarStatus {
    AVAILABLE,
    RENTED,
    MAINTENANCE
}
