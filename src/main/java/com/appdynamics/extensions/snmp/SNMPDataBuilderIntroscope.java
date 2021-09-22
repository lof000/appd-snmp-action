/*
 *   Copyright 2018. AppDynamics LLC and its affiliates.
 *   All Rights Reserved.
 *   This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 *   The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package com.appdynamics.extensions.snmp;


import com.appdynamics.extensions.alerts.customevents.*;
import com.appdynamics.extensions.snmp.api.*;
import com.appdynamics.extensions.snmp.config.Configuration;
import com.appdynamics.extensions.snmp.config.ControllerConfig;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SNMPDataBuilderIntroscope {

    private static final Joiner JOIN_ON_COMMA = Joiner.on(",");
    private Configuration config;
    private IService service = new ServiceImpl();
    private final HttpClientBuilder clientBuilder;
    private final EndpointBuilder endpointBuilder;

    private static Logger logger = Logger.getLogger(SNMPDataBuilder.class);


    SNMPDataBuilderIntroscope(Configuration config) {
        this.config = config;
        ControllerConfig controller = config.getController();
        clientBuilder = new HttpClientBuilder(controller.isUseSsl(), controller.getUserAccount(), controller.getPassword(), controller.getConnectTimeoutInSeconds() * 1000, controller.getSocketTimeoutInSeconds() * 1000);
        endpointBuilder = new EndpointBuilder();
    }


    public ADSnmpDataIntroscope buildFromHealthRuleViolationEvent(HealthRuleViolationEvent violationEvent){

        ADSnmpDataIntroscope snmpData = new ADSnmpDataIntroscope();

        snmpData.setAgent("agent_name");
        snmpData.setHost("host");
        snmpData.setAgentType("AppDynamics_APM");
        snmpData.setDomain(violationEvent.getAppName());
        snmpData.setMetric("metric");
        snmpData.setThreshold("0");
        snmpData.setCurrentValue("0");

        if (config.getController() != null) {
            snmpData.setDrillDownLink(CommonUtils.getAlertUrl(violationEvent));
        }
        snmpData.setDrillDownLink(CommonUtils.getAlertUrl(violationEvent));

        snmpData.setDate(violationEvent.getPvnAlertTime());


        if(isAffectedEntityType(violationEvent, "BUSINESS_TRANSACTION")){
            EvaluationEntity firstEntity =  getFirstEvEntity(violationEvent.getEvaluationEntity(),"APPLICATION");
            if (firstEntity!=null){
                TriggerCondition tCon = getApplicationTriggeredCondition(firstEntity.getTriggeredConditions());
                snmpData.setAgent("APP:"+tCon.getScopeName());
                snmpData.setHost("APP:"+tCon.getScopeName());
                snmpData.setMetric( violationEvent.getAffectedEntityName() + "|" + violationEvent.getHealthRuleName() + "|" + tCon.getConditionName() + "|" + tCon.getOperator() + "|" + tCon.getConditionUnitType());
                snmpData.setThreshold(tCon.getThresholdValue());
                snmpData.setCurrentValue(tCon.getObservedValue());
            }

        }

        if(isAffectedEntityType(violationEvent, "APPLICATION")){
            EvaluationEntity firstEntity =  getFirstEvEntity(violationEvent.getEvaluationEntity(),"APPLICATION");
            if (firstEntity!=null){
                TriggerCondition tCon = getApplicationTriggeredCondition(firstEntity.getTriggeredConditions());
                snmpData.setAgent("APP:"+tCon.getScopeName());
                snmpData.setHost("APP:"+tCon.getScopeName());
                snmpData.setMetric( violationEvent.getHealthRuleName() + "|" + tCon.getConditionName() + "|" + tCon.getOperator() + "|" + tCon.getConditionUnitType());
                snmpData.setThreshold(tCon.getThresholdValue());
                snmpData.setCurrentValue(tCon.getObservedValue());
            }

        }

        if( isAffectedEntityType(violationEvent, "APPLICATION_NODE")  || isAffectedEntityType(violationEvent, "APPLICATION_COMPONENT_NODE")    ){
            EvaluationEntity firstEntity =  getFirstEvEntity(violationEvent.getEvaluationEntity(),"APPLICATION_COMPONENT_NODE");
            if (firstEntity!=null){
                TriggerCondition tCon = getApplicationNodeTriggeredCondition(firstEntity.getTriggeredConditions());
                snmpData.setAgent(tCon.getScopeName());
                snmpData.setHost(tCon.getScopeName());
                snmpData.setMetric( violationEvent.getHealthRuleName() + "|" + tCon.getConditionName() + "|" + tCon.getOperator() + "|" + tCon.getConditionUnitType());
                snmpData.setThreshold(tCon.getThresholdValue());
                snmpData.setCurrentValue(tCon.getObservedValue());
            }
        }



        return snmpData;
    }

    //PEGAR O PRIMEIRO APPLICATION NODE DENTRO DE TRIGGERED CONDITIONS
    public TriggerCondition getApplicationNodeTriggeredCondition(List<TriggerCondition> triggerCons){
        TriggerCondition firstTriggerCon=null;

        List<TriggerCondition> filteredList;

        filteredList = triggerCons.stream()
        .filter(tCon -> tCon.getScopeType().equalsIgnoreCase("APPLICATION_NODE") || tCon.getScopeType().equalsIgnoreCase("APPLICATION_COMPONENT_NODE"))
        .collect(Collectors.toList());

        if (filteredList.size()>0){
            firstTriggerCon = filteredList.get(0);
        }
        return firstTriggerCon;
    }

    //PEGAR O PRIMEIRO APPLICATION NODE DENTRO DE TRIGGERED CONDITIONS
    public TriggerCondition getApplicationTriggeredCondition(List<TriggerCondition> triggerCons){
        TriggerCondition firstTriggerCon=null;

        List<TriggerCondition> filteredList;

        filteredList = triggerCons.stream()
        .filter(tCon -> tCon.getScopeType().equalsIgnoreCase("APPLICATION") )
        .collect(Collectors.toList());

        if (filteredList.size()>0){
            firstTriggerCon = filteredList.get(0);
        }
        return firstTriggerCon;
    }

    //PEGAR O PRIMEIRO EVALUATIONEVENT DO TIPO APPLICATION COMPONENT NODE
    public EvaluationEntity getFirstEvEntity(List<EvaluationEntity> evalEnt,String filter){

        EvaluationEntity firstEntity=null;

        List<EvaluationEntity> filteredList;

        filteredList = evalEnt.stream()
        .filter(evEnt -> evEnt.getType().equalsIgnoreCase(filter))
        .collect(Collectors.toList());

        if (filteredList.size()>0){
            firstEntity = filteredList.get(0);
        }
        return firstEntity;
    }

    private void populateMachineInfo(HealthRuleViolationEvent violationEvent, List<String> affectedNodes, List<String> affectedTiers, ADSnmpData snmpData) {
        logger.debug("Affected Tiers : " + affectedTiers);
        logger.debug("Affected Nodes : " + affectedNodes);
        List<String> machines = Lists.newArrayList();
        List<String> ipAddresses = Lists.newArrayList();

        try {
            List<Node> nodesInAffectedTiers = null;
            if(!affectedTiers.isEmpty()){
                nodesInAffectedTiers  = getAllNodesFromTiers(Integer.parseInt(violationEvent.getAppID()),affectedTiers);
                collectMachineInfo(machines, ipAddresses, nodesInAffectedTiers);
            }
            if(!affectedNodes.isEmpty()){
                for(String affectedNode : affectedNodes){
                    List<Node> nodes = getNodeFromNodeName(Integer.parseInt(violationEvent.getAppID()),affectedNode);
                    collectMachineInfo(machines, ipAddresses, nodes);
                    //extracting tiers from the nodes and setting it..ugly..needs a clean approach.
                    affectedTiers.addAll(collectTierInfo(nodes,affectedTiers));
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("Encoding error",e);
        }
        snmpData.setMachines(JOIN_ON_COMMA.join(machines));
        snmpData.setIpAddresses(JOIN_ON_COMMA.join(ipAddresses));
    }

    private Set<String> collectTierInfo(List<Node> nodes, List<String> affectedTiers) {
        Set<String> setOfAffectedTiers = Sets.newHashSet(affectedTiers);
        for(Node aNode : nodes){
            setOfAffectedTiers.add(aNode.getTierName());
        }
        return setOfAffectedTiers;
    }

    private void collectMachineInfo(List<String> machines, List<String> ipAddresses, List<Node> nodesInAffectedTiers) {
        if(nodesInAffectedTiers != null){
            for(Node aNode : nodesInAffectedTiers){
                machines.add(aNode.getMachineName());
                ipAddresses.addAll(aNode.getIpAddresses());
            }
        }
    }

    private List<Node> getNodeFromNodeName(int appId, String affectedNode) throws UnsupportedEncodingException {
        ControllerConfig controller = config.getController();
        String endpoint = endpointBuilder.getANodeEndpoint(controller,appId,affectedNode);
        List<Node> nodes = service.getNodes(clientBuilder,endpoint);
        return nodes;
    }

    private List<Node> getAllNodesFromTiers(int applicationId,List<String> tiers) throws UnsupportedEncodingException {
        List<Node> nodes = Lists.newArrayList();
        for(String tier:tiers){
            nodes.addAll(getAllNodesInTier(applicationId,tier));
        }
        return nodes;
    }

    private List<Node> getAllNodesInTier(int applicationId,String tier) throws UnsupportedEncodingException {
        ControllerConfig controller = config.getController();
        String endpoint = endpointBuilder.getNodesFromTierEndpoint(controller,applicationId,tier);
        List<Node> nodes = service.getNodes(clientBuilder,endpoint);
        return nodes;
    }


    private String getTiersFromBTApi(HealthRuleViolationEvent violationEvent) {
        ControllerConfig controller = config.getController();
        String endpoint = endpointBuilder.buildBTsEndpoint(controller,Integer.parseInt(violationEvent.getAppID()));
        List<BusinessTransaction> bts = service.getBTs(clientBuilder,endpoint);
        for(BusinessTransaction bt : bts){
            if(bt.getId() == Integer.parseInt(violationEvent.getAffectedEntityID())){
                return bt.getTierName();
            }
        }
        return "";
    }


    ADSnmpData buildFromOtherEvent(OtherEvent otherEvent){
        ADSnmpData snmpData = new ADSnmpData();
        snmpData.setApplication(otherEvent.getAppName());
        snmpData.setTriggeredBy(otherEvent.getEventNotificationName());
        snmpData.setNodes(" ");
        snmpData.setTxns(" ");
        snmpData.setEventTime(otherEvent.getEventNotificationTime());
        snmpData.setSeverity(otherEvent.getSeverity());
        snmpData.setType(getTypes(otherEvent));
        snmpData.setSubtype(" ");
        snmpData.setSummary(getSummary(otherEvent));
        if(config.getController() != null) {
            snmpData.setLink(CommonUtils.getAlertUrl(otherEvent));
        }
        snmpData.setTag(otherEvent.getTag());
        snmpData.setEventType("NON_POLICY_EVENT");
        snmpData.setIncidentId(otherEvent.getEventNotificationId());
        snmpData.setAccountId(CommonUtils.cleanUpAccountInfo(otherEvent.getAccountId()));
        return snmpData;
    }



    private List<String> getNodes(HealthRuleViolationEvent violationEvent) {
        List<String> nodes = Lists.newArrayList();
        if(isAffectedEntityType(violationEvent, "APPLICATION_COMPONENT_NODE")){
            nodes.add(violationEvent.getAffectedEntityName());
        }
        else if(violationEvent.getEvaluationEntity() != null) {
            for (EvaluationEntity evaluationEntity : violationEvent.getEvaluationEntity()) {
                if (evaluationEntity.getType().equalsIgnoreCase("APPLICATION_COMPONENT_NODE")) {
                    nodes.add(evaluationEntity.getName());
                }
            }
        }
        return nodes;
    }

    private boolean isAffectedEntityType(HealthRuleViolationEvent violationEvent, String type) {
        if(type.equalsIgnoreCase(violationEvent.getAffectedEntityType())){
            return true;
        }
        return false;
    }

    private List<String> getBTs(HealthRuleViolationEvent violationEvent) {
        List<String> bts = Lists.newArrayList();
        if(isAffectedEntityType(violationEvent, "BUSINESS_TRANSACTION")){
            bts.add(violationEvent.getAffectedEntityName());
        }
        else if(violationEvent.getEvaluationEntity() != null) {
            for (EvaluationEntity evaluationEntity : violationEvent.getEvaluationEntity()) {
                if (evaluationEntity.getType().equalsIgnoreCase("BUSINESS_TRANSACTION")) {
                    bts.add(evaluationEntity.getName());
                }
            }
        }
        return bts;
    }

    private List<String> getTiers(HealthRuleViolationEvent violationEvent) {
        List<String> tiers = Lists.newArrayList();
        if(isAffectedEntityType(violationEvent, "APPLICATION_COMPONENT")){
            tiers.add(violationEvent.getAffectedEntityName());
        }
        else if(violationEvent.getEvaluationEntity() != null) {
            for (EvaluationEntity evaluationEntity : violationEvent.getEvaluationEntity()) {
                if (evaluationEntity.getType().equalsIgnoreCase("APPLICATION_COMPONENT")) {
                    tiers.add(evaluationEntity.getName());
                }
            }
        }
        //for BTs, when the health rule is configured to be triggered when the condition fails on
        // avergae number of nodes in the tier, the controller doesn't pass tier name but just the application name.
        //In such cases, tier name needs to be pulled from API.
        if(tiers.isEmpty() && isAffectedEntityType(violationEvent,"BUSINESS_TRANSACTION")){
            String btTiers = getTiersFromBTApi(violationEvent);
            if(!Strings.isNullOrEmpty(btTiers)){
                tiers.add(btTiers);
            }
        }
        return tiers;
    }




    private String getSummary(OtherEvent otherEvent) {
        StringBuilder summaries = new StringBuilder("");
        if(otherEvent.getEventSummaries() != null){
            for(EventSummary eventSummary : otherEvent.getEventSummaries()){
                summaries.append(eventSummary.getEventSummaryString()).append(" ");
            }
        }
        return summaries.toString();
    }


    private String getTypes(OtherEvent otherEvent) {
        StringBuilder types = new StringBuilder("");
        if(otherEvent.getEventTypes() != null){
            for(EventType eventType : otherEvent.getEventTypes()){
                types.append(eventType.getEventType()).append(" ");
            }
        }
        return types.toString();
    }




}
