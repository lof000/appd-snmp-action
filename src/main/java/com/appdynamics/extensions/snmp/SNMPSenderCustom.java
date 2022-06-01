/*
 *   Copyright 2018. AppDynamics LLC and its affiliates.
 *   All Rights Reserved.
 *   This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 *   The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package com.appdynamics.extensions.snmp;


import com.appdynamics.extensions.snmp.config.*;
import org.apache.log4j.Logger;
import org.snmp4j.*;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.appdynamics.extensions.snmp.CommonUtils.getSysUptime;

public class SNMPSenderCustom {

    private static Logger logger = Logger.getLogger(SNMPSender.class);

    public static final int SNMP_V1 = 1;
    public static final int SNMP_V2 = 2;
    public static final int SNMP_V3 = 3;

    public static String NO_AUTH_NO_PRIV = "1";
    public static String AUTH_NO_PRIV = "2";
    public static String AUTH_PRIV = "3";



    public void sendTrap(Configuration config, ADSnmpDataCustom snmpData,String trapOid) {

        ArrayList<String> exceptions = new ArrayList<String>();
        //sending SNMP traps to all registered receivers
        for(Receiver receiver : config.getReceivers()){
            logger.info("SNMP version " + config.getSnmpVersion());
            logger.info("Sending trap to " + receiver.getHost() + ":" + receiver.getPort());
            try {
                if (config.getSnmpVersion() == SNMP_V2) {
                    //sendV2Trap(receiver.getHost(), Integer.toString(receiver.getPort()), config.getCommunity(), config.getSenderHost(), snmpData,trapOid);
                    logger.info("mandando trap V1");
                    sendV1Trap(receiver.getHost(), Integer.toString(receiver.getPort()), config.getCommunity(), config.getSenderHost(), snmpData,trapOid);
                    
                }else{
                    logger.error("THIS GUY ONLY SUPPORTS SNMP_V2");
                    exceptions.add("THIS GUY ONLY SUPPORTS SNMP_V2");
                }
            } catch (IOException e) {
                logger.error("Something unforeseen has happened.",e);
                exceptions.add("Exception while sending trap to " + receiver.getHost() + ":" + receiver.getPort()+ e);
            } catch (IllegalAccessException e) {
                logger.error("Illegal access",e);
                exceptions.add("Exception while sending trap to " + receiver.getHost() + ":" + receiver.getPort()+ e);
            }
        }
        if(exceptions.size() > 0){
            throw new SNMPTrapException(exceptions.toString());
        }
    }

       /**
     * Sends v2 Traps
     * @param 	host 						Host to send trap to
     * @param 	port						Port location to send trap to
     * @param 	community					Community (Default: PUBLIC)
     * @param 	trapHost					Host of the source sending the trap
     * @param 	snmpData					Trap Data
     * @throws 	java.io.IOException					Failed to send trap exception
     * @throws 	IllegalArgumentException 	Failed to access snmp trap variables
     * @throws 	IllegalAccessException 		Failed to access snmp trap variables
     */
    @SuppressWarnings("rawtypes")
    private void sendV2Trap(String host, String port, String community, String trapHost, ADSnmpDataCustom snmpData,String trapOid)
            throws IOException, IllegalArgumentException, IllegalAccessException
    {
        LookupCustom lookUp = new LookupCustom();
        TransportMapping transport = new DefaultUdpTransportMapping();
        transport.listen();

        CommunityTarget comTarget = new CommunityTarget();
        comTarget.setCommunity(new OctetString(community));
        comTarget.setVersion(SnmpConstants.version2c);
        comTarget.setAddress(new UdpAddress(host + '/' + port));
        comTarget.setRetries(2);
        comTarget.setTimeout(5000);
        
        TimeTicks sysUpTime = getTimeTicks();

        PDU pdu = new PDU();
        pdu.add(new VariableBinding(SnmpConstants.sysUpTime,  sysUpTime));
        pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(trapOid)));
        pdu.add(new VariableBinding(SnmpConstants.snmpTrapAddress, new IpAddress(trapHost)));

        for (Field field : snmpData.getClass().getDeclaredFields())
        {
            
            if(field.get(snmpData) != null) {

                try {
                    Object snmpVal = new OctetString(field.get(snmpData).toString());

                    if (!(snmpVal.equals(" ") || snmpVal.equals(""))) {
                        pdu.add(new VariableBinding(new OID(lookUp.getOID(field.getName())), new OctetString(snmpVal.toString())));
                    }
                } catch (Throwable ex) {
                    logger.error("Error reading snmp data field:", ex);
                }
            }
        }

        pdu.setType(PDU.NOTIFICATION);
        Snmp snmp = new Snmp(transport);
        logger.info("Enviando trap v2....");
        snmp.send(pdu, comTarget);
        snmp.close();
    }

    private TimeTicks getTimeTicks() {
        long upTimeInMs = getSysUptime();
        return CommonUtils.getTimeTicks(upTimeInMs);
    }


    /**
     * Sends v1 Traps
     * @param 	host 						Host to send trap to
     * @param 	port						Port location to send trap to
     * @param 	community					Community (Default: PUBLIC)
     * @param 	trapHost					Host of the source sending the trap
     * @param 	snmpData					Trap Data
     * @throws java.io.IOException                    Failed to send trap exception
     * @throws 	IllegalArgumentException 	Failed to access snmp trap variables
     * @throws 	IllegalAccessException 		Failed to access snmp trap variables
     */
    @SuppressWarnings("rawtypes")
    private void sendV1Trap(String host, String port, String community, String trapHost, ADSnmpDataCustom snmpData,String trapOid)
            throws IOException, IllegalArgumentException, IllegalAccessException
    {
        
        LookupCustom lookUp = new LookupCustom();

        TransportMapping transport = new DefaultUdpTransportMapping();
        transport.listen();

        CommunityTarget comTarget = new CommunityTarget();
        comTarget.setCommunity(new OctetString(community));
        comTarget.setVersion(SnmpConstants.version1);
        comTarget.setAddress(new UdpAddress(host + "/" + port));
        comTarget.setRetries(2);
        comTarget.setTimeout(5000);

        TimeTicks sysUpTime = getTimeTicks();

        PDUv1 pdu = new PDUv1();
        pdu.setType(PDU.V1TRAP);
        //pdu.setEnterprise(new OID(trapOid));
        pdu.setEnterprise(new OID("1.3.6.1.4.1.791"));
        pdu.setGenericTrap(PDUv1.ENTERPRISE_SPECIFIC);
        pdu.setSpecificTrap(1);
        pdu.setAgentAddress(new IpAddress(trapHost));

        pdu.add(new VariableBinding(SnmpConstants.sysUpTime,  sysUpTime));
        pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(trapOid)));
        pdu.add(new VariableBinding(SnmpConstants.snmpTrapAddress, new IpAddress(trapHost)));

        for (Field field : snmpData.getClass().getDeclaredFields())
        {
            if(field.get(snmpData) != null) {
                try {
                    Object snmpVal = new OctetString(field.get(snmpData).toString());

                    if (!(snmpVal.equals(" ") || snmpVal.equals(""))) {
                        pdu.add(new VariableBinding(new OID(lookUp.getOID(field.getName())), new OctetString(snmpVal.toString())));
                    }
                } catch (Throwable ex) {
                    logger.error("Error reading snmp data field:", ex);
                }
            }
        }
        logger.info("Enviando trap v1....");
        Snmp snmp = new Snmp(transport);
        snmp.send(pdu, comTarget);
        snmp.close();
        
    }

}
