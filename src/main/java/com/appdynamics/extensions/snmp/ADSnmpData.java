

/*
 *   Copyright 2018. AppDynamics LLC and its affiliates.
 *   All Rights Reserved.
 *   This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 *   The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package com.appdynamics.extensions.snmp;

/**
 * SNMP Data Object
 */
public class ADSnmpData
{
    String application;
    String triggeredBy;
    String nodes;
    String txns = "";
    String machines;
    String tiers;
    String eventTime;
    String severity;
    String type;
    String subtype;
    String summary;
    String link;
    String tag;
    String eventType;
    String ipAddresses;
    String incidentId;
    String accountId;
    String threshold;
    String currentValue;

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public String getTxns() {
        return txns;
    }

    public void setTxns(String txns) {
        this.txns = txns;
    }

    public String getMachines() {
        return machines;
    }

    public void setMachines(String machines) {
        this.machines = machines;
    }

    public String getTiers() {
        return tiers;
    }

    public void setTiers(String tiers) {
        this.tiers = tiers;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getIpAddresses() {
        return ipAddresses;
    }

    public void setIpAddresses(String ipAddresses) {
        this.ipAddresses = ipAddresses;
    }

    public String getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(String incidentId) {
        this.incidentId = incidentId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "ADSnmpData{" +
                "application='" + application + '\'' +
                ", triggeredBy='" + triggeredBy + '\'' +
                ", nodes='" + nodes + '\'' +
                ", txns='" + txns + '\'' +
                ", machines='" + machines + '\'' +
                ", tiers='" + tiers + '\'' +
                ", eventTime='" + eventTime + '\'' +
                ", severity='" + severity + '\'' +
                ", type='" + type + '\'' +
                ", subtype='" + subtype + '\'' +
                ", summary='" + summary + '\'' +
                ", link='" + link + '\'' +
                ", tag='" + tag + '\'' +
                ", eventType='" + eventType + '\'' +
                ", ipAddresses='" + ipAddresses + '\'' +
                ", incidentId='" + incidentId + '\'' +
                ", accountId='" + accountId + '\'' +
                '}';
    }
}
