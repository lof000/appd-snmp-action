package com.appdynamics.extensions;

import com.appdynamics.extensions.snmp.SnmpTrapAlertExtension;

public class GeralEvents {


static String[] EVENT_1 = {
"digibank",
"10",
"Wed Sep 22 02:21:55 UTC 2021",
"1",
"ERROR",
"send_to_spectrum",
"Teste Leandro",
"49",
"1",
"APPLICATION_COMPONENT_NODE",
"creditnode",
"500",
"1",
"APPLICATION_COMPONENT_NODE",
"creditnode",
"500",
"1",
"APPLICATION_COMPONENT_NODE",
"creditnode",
"500",
"Condition 1",
"282",
"GREATER_THAN",
"ABSOLUTE",
"0.0",
"4.0",
"Teste Leandro triggered at Wed Sep 22 02:21:55 UTC 2021. This policy was violated because the following conditions were met for the creditnode Node for the last 1 minute(s):   For Evaluation Entity: creditnode Node - Condition 1 is greater than 0.0. Observed value = 4.0",
"507",
"http://ip-10-97-5-1.us-west-2.compute.internal:8090/controller/#location=APP_INCIDENT_DETAIL_MODAL&incident=",
"POLICY_OPEN_CRITICAL",
"ACCOUNT",
"customer1_f161f981-6f1f-4fa8-a6ad-2235b48bd549"
};

static  String[] EVENT_2 = {
"digibank",
"10",
"Wed Sep 22 02:02:55 UTC 2021",
"1",
"ERROR",
"send_to_spectrum",
"Teste Leandro",
"49",
"1",
"APPLICATION",
"digibank",
"10",
"1",
"APPLICATION",
"digibank",
"10",
"1",
"APPLICATION",
"digibank",
"10",
"Condition 1",
"187",
"GREATER_THAN",
"ABSOLUTE",
"1.0",
"7.0",
"Teste Leandro triggered at Wed Sep 22 02:02:55 UTC 2021. This policy was violated because the following conditions were met for the digibank Application for the last 1 minute(s):   For Evaluation Entity: digibank Application - Condition 1 is greater than 1.0. Observed value = 7.0",
"500",
"http://ip-10-97-5-1.us-west-2.compute.internal:8090/controller/#location=APP_INCIDENT_DETAIL_MODAL&incident=",
"POLICY_CONTINUES_CRITICAL",
"ACCOUNT",
"customer1_f161f981-6f1f-4fa8-a6ad-2235b48bd549"
};


static  String[] EVENT_3 = {
"digibank",
 "10",
 "Wed Sep 22 12:34:55 UTC 2021",
 "1",
 "ERROR",
 "send_to_spectrum",
 "Teste Leandro",
 "49",
 "1",
 "BUSINESS_TRANSACTION",
 "/bank/api/v1",
 "186",
 "1",
 "APPLICATION",
 "digibank",
 "10",
 "1",
 "APPLICATION",
 "digibank",
 "10",
 "Condition 1",
 "283",
 "GREATER_THAN",
 "ABSOLUTE",
 "0.0",
 "2.0",
 "Teste Leandro triggered at Wed Sep 22 12:34:55 UTC 2021. This policy was violated because the following conditions were met for the /bank/api/v1 Business Transaction for the last 1 minute(s):   For Evaluation Entity: digibank Application - Condition 1 is greater than 0.0. Observed value = 2.0",
 "610",
 "http://ip-10-97-5-1.us-west-2.compute.internal:8090/controller/#location=APP_INCIDENT_DETAIL_MODAL&incident=",
 "POLICY_OPEN_CRITICAL",
 "ACCOUNT",
 "customer1_f161f981-6f1f-4fa8-a6ad-2235b48bd549"
};


static  String[] EVENT_4 = {
"S344",
"8",
"Thu Sep 23 10:18:24 BRT 2021",
"1",
"ERROR",
"Trap_Spectrum",
"Teste_trap",
"34",
"2",
"APPLICATION_COMPONENT",
"S344Backend",
"25",
"1",
"APPLICATION_COMPONENT_NODE",
"S1CPTP37-S344Backend",
"320",
"1",
"APPLICATION_COMPONENT_NODE",
"S1CPTP37-S344Backend",
"320",
"Memory Used (%)",
"120",
"GREATER_THAN",
"ABSOLUTE",
"10.0",
"79.0",
"Teste_trap triggered at Thu Sep 23 10:18:24 BRT 2021. This policy was violated because the following conditions were met for he S344Backend Tier for the last 2 minute(s):   For Evaluation Entity: S1CPTP37-S344Backend Node - Memory Used (%) is greater than 10.0. Observed value = 79.0",
"740",
"http://S1APDP01:8090/controller/#location=APP_INCIDENT_DETAIL_MODAL&incident=",
"POLICY_CONTINUES_CRITICAL",
"ACCOUNT",
"customer1_48fc1142-b3d3-4948-9b28-a210b7e6067e"
};


static String[] EVENT_5 = {
"S344",
"8",
"Thu Sep 23 14:24:24 BRT 2021",
"1",
"ERROR",
"Trap_Spectrum",
"Teste_trap",
"34",
"2",
"MACHINE_INSTANCE",
"S1CPTP37",
"305",
"1",
"APPLICATION_COMPONENT_NODE",
"S1CPTP37-java-MA",
"307",
"1",
"APPLICATION_COMPONENT_NODE",
"S1CPTP37-java-MA",
"307",
"Condition 1",
"128",
"GREATER_THAN",
"ABSOLUTE",
"10.0",
"74.0",
"Teste_trap triggered at Thu Sep 23 14:24:24 BRT 2021. This policy was violated because the following conditions were met for the S1CPTP37 Machine Instance for the last 2 minute(s):   For Evaluation Entity: S1CPTP37-java-MA Node - Condition 1 is greater than 10.0. Observed value = 74.0",
"778",
"http://S1APDP01:8090/controller/#location=APP_INCIDENT_DETAIL_MODAL&incident=",
"POLICY_CONTINUES_CRITICAL",
"ACCOUNT",
"customer1_48fc1142-b3d3-4948-9b28-a210b7e6067e"
};

public static void main(String args[]){
    SnmpTrapAlertExtension.main(EVENT_1);
    SnmpTrapAlertExtension.main(EVENT_2);
    SnmpTrapAlertExtension.main(EVENT_3);
    SnmpTrapAlertExtension.main(EVENT_4);
    SnmpTrapAlertExtension.main(EVENT_5);
}
    
}
