

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
public class ADSnmpDataIntroscope
{

    String agent;
    String host;
    String agentType;
    String domain;
    String metric;
    String threshold;
    String currentValue;
    String drillDownLink;
    String date;

    public String getAgent() {
        return agent;
    }



    public void setAgent(String agent) {
        this.agent = agent;
    }



    public String getHost() {
        return host;
    }



    public void setHost(String host) {
        this.host = host;
    }



    public String getAgentType() {
        return agentType;
    }



    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }



    public String getDomain() {
        return domain;
    }



    public void setDomain(String domain) {
        this.domain = domain;
    }



    public String getMetric() {
        return metric;
    }



    public void setMetric(String metric) {
        this.metric = metric;
    }



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



    public String getDrillDownLink() {
        return drillDownLink;
    }



    public void setDrillDownLink(String drillDownLink) {
        this.drillDownLink = drillDownLink;
    }



    public String getDate() {
        return date;
    }



    public void setDate(String date) {
        this.date = date;
    }

  

    @Override
    public String toString() {
        return "ADSnmpData{" +
                "agent='" + agent + '\'' +
                ", host='" + host + '\'' +
                ", agentType='" + agentType + '\'' +
                ", domain='" + domain + '\'' +
                ", metric='" + metric + '\'' +
                ", threshold='" + threshold + '\'' +
                ", currentValue='" + currentValue + '\'' +
                ", drillDownLink='" + drillDownLink + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
