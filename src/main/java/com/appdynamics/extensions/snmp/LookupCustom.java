/*
 *   Copyright 2018. AppDynamics LLC and its affiliates.
 *   All Rights Reserved.
 *   This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 *   The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */




package com.appdynamics.extensions.snmp;

import java.util.HashMap;

/**
 * Creates a lookup or each attribute of SNMP and assigns an OID value
 */
public class LookupCustom
{
    private static String baseOID = "1.3.6.1.4.1.791.4.4.1";
    private static HashMap<String, String> map = new HashMap<String, String>();
    private static String[] names = {
            "agent",
            "host",
            "agentType",
            "domain",
            "metric",
            "threshold",
            "currentValue",
            "drillDownLink",
            "date"
    };

    private static HashMap<String,String> oidMap = new HashMap<String, String>();

    public LookupCustom()
    {
        oidMap.put("agent", "1.3.6.1.4.1.791.4.4.8");
        oidMap.put("host", "1.3.6.1.4.1.791.4.4.6");
        oidMap.put("agentType", "1.3.6.1.4.1.791.4.4.7");
        oidMap.put("domain", "1.3.6.1.4.1.791.4.4.5");
        oidMap.put("metric", "1.3.6.1.4.1.791.4.4.24");
        oidMap.put("threshold", "1.3.6.1.4.1.791.4.4.12");
        oidMap.put("currentValue", "1.3.6.1.4.1.791.4.4.10");
        oidMap.put("drillDownLink", "1.3.6.1.4.1.791.4.4.11");
        oidMap.put("date", "1.3.6.1.4.1.791.4.4.1");

        map = oidMap;

    }


/*
    public LookupIntroscope()
    {
        if(map.size() == 0)
        {
            int idx = 1;

            for (String name : names)
            {
                map.put(name, baseOID + idx++);
            }
        }
    }
*/
    public String getOID(String name)
    {
        String val = map.get(name);
        return val;
    }
}

