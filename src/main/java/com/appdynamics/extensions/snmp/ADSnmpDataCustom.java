

/*
 *   Copyright 2018. AppDynamics LLC and its affiliates.
 *   All Rights Reserved.
 *   This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 *   The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */


/*
//https://docs.appdynamics.com/21.9/en/appdynamics-essentials/alert-and-respond/actions/custom-actions/build-a-custom-action
*/

package com.appdynamics.extensions.snmp;

/**
 * SNMP Data Object
 */
public class ADSnmpDataCustom
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
    String type;
    String policyStatus;


    //POLICY_OPEN_WARNING, POLICY_OPEN_CRITICAL, POLICY_CLOSE_WARNING, POLICY_CLOSE_CRITICAL, POLICY_UPGRADED, POLICY_DOWNGRADED, POLICY_CANCELED_WARNING, 
    //POLICY_CANCELED_CRITICAL, POLICY_CONTINUES_CRITICAL, and POLICY_CONTINUES_WARNING.

    //retornar No item 109, que é OID 1.3.6.1.4.1.791.4.4.16, é necessario chegar a severidade do alarme.  #severidade APM - 1 CLEAR, 2 - CAUTION, 3 - DANGER
    public String getPolicyStatus() {
        return policyStatus;
    }

    public void setPolicyStatus(String policyStatus) {

        if (
            policyStatus.equals("POLICY_OPEN_WARNING") ||
            policyStatus.equals("POLICY_CONTINUES_WARNING") ||
            policyStatus.equals("POLICY_DOWNGRADED")
        ){
            this.policyStatus = "2";
        }

        if (
            policyStatus.equals("POLICY_OPEN_CRITICAL") ||
            policyStatus.equals("POLICY_CONTINUES_CRITICAL") ||
            policyStatus.equals("POLICY_UPGRADED")
        ){
            this.policyStatus = "3";
        }

        if (
            policyStatus.contains("CLOSE") ||
            policyStatus.contains("CANCELED")
        ){
            this.policyStatus = "1";
        }

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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
