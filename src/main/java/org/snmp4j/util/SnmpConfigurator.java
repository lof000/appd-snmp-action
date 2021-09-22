/*
 *   Copyright 2018. AppDynamics LLC and its affiliates.
 *   All Rights Reserved.
 *   This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 *   The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package org.snmp4j.util;

import org.snmp4j.Snmp;
import java.util.Map;
import org.snmp4j.Target;
import java.util.Properties;
import org.snmp4j.mp.MPv3;
import org.snmp4j.smi.OctetString;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.CommunityTarget;
import org.snmp4j.SecureTarget;
import org.snmp4j.UserTarget;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.OID;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.AuthSHA;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.PrivAES128;
import org.snmp4j.security.PrivAES192;
import org.snmp4j.security.PrivAES256;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.PDUv1;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.security.SecurityModel;

/**
 * The <code>SnmpConfigurator</code> class configures a {@link org.snmp4j.Snmp} instance
 * with settings taken from a <code>Map</code> conforming to the format returned
 * by {@link org.snmp4j.util.ArgumentParser#parse(String[] args)}.
 * In addition, a {@link org.snmp4j.util.PDUFactory} and {@link org.snmp4j.Target} can be created using
 * settings from a <code>Map</code> too.
 *
 * @author Frank Fock
 * @version 1.10
 * @since 1.10
 */
public class SnmpConfigurator {

  public static final String O_VERSION = "v";
  public static final String P_VERSION = "org.snmp4j.arg.version";
  public static final String F_VERSION = "s<1|2c|3>";

  public static final String O_LOCAL_ENGINE_ID = "l";
  public static final String P_LOCAL_ENGINE_ID = "org.snmp4j.arg.localEngineID";
  public static final String F_LOCAL_ENGINE_ID = "o<\\n\\n[:\\n\\n]*>";

  public static final String O_AUTHORITATIVE_ENGINE_ID = "e";
  public static final String P_AUTHORITATIVE_ENGINE_ID = "org.snmp4j.arg.authoritativeEngineID";
  public static final String F_AUTHORITATIVE_ENGINE_ID = "o<\\n\\n[:\\n\\n]*>";

  public static final String O_COMMUNITY = "c";
  public static final String P_COMMUNITY = "org.snmp4j.arg.community";
  public static final String F_COMMUNITY = "s{=public}";

  public static final String O_CONTEXT_NAME = "n";
  public static final String P_CONTEXT_NAME = "org.snmp4j.arg.contextName";
  public static final String F_CONTEXT_NAME = "s{=}";

  public static final String O_CONTEXT_ENGINE_ID = "E";
  public static final String P_CONTEXT_ENGINE_ID = "org.snmp4j.arg.contextEngineID";
  public static final String F_CONTEXT_ENGINE_ID = "o<\\n\\n[:\\n\\n]*>";

  public static final String O_SECURITY_NAME = "u";
  public static final String P_SECURITY_NAME = "org.snmp4j.arg.securityName";
  public static final String F_SECURITY_NAME = "s";

  public static final String O_RETRIES = "r";
  public static final String P_RETRIES = "org.snmp4j.arg.retries";
  public static final String F_RETRIES = "i";

  public static final String O_TIMEOUT = "t";
  public static final String P_TIMEOUT = "org.snmp4j.arg.timeout";
  public static final String F_TIMEOUT = "l";

  public static final String O_ADDRESS = "address";
  public static final String P_ADDRESS = "org.snmp4j.arg.address";
  public static final String F_ADDRESS = "s<(udp|tcp):.*[/[0-9]+]?>";

  public static final String O_AUTH_PASSPHRASE = "A";
  public static final String P_AUTH_PASSPHRASE = "org.snmp4j.arg.authPassphrase";
  public static final String F_AUTH_PASSPHRASE = "s<.*>";

  public static final String O_PRIV_PASSPHRASE = "Y";
  public static final String P_PRIV_PASSPHRASE = "org.snmp4j.arg.privPassphrase";
  public static final String F_PRIV_PASSPHRASE = "s<.*>";

  public static final String O_AUTH_PROTOCOL = "a";
  public static final String P_AUTH_PROTOCOL = "org.snmp4j.arg.authProtocol";
  public static final String F_AUTH_PROTOCOL = "s<(MD5|SHA)>";

  public static final String O_PRIV_PROTOCOL = "y";
  public static final String P_PRIV_PROTOCOL = "org.snmp4j.arg.privProtocol";
  public static final String F_PRIV_PROTOCOL = "s<(DES|3DES|AES|AES128|AES192|AES256)>";

  public static final String O_OPERATION = "o";
  public static final String P_OPERATION = "org.snmp4j.arg.operation";
  public static final String F_OPERATION = "s<(?i)(GET|GETNEXT|GETBULK|TRAP|NOTIFICATION|SET|INFORM)>";

  public static final String O_MAX_REPETITIONS = "Cr";
  public static final String P_MAX_REPETITIONS = "org.snmp4j.arg.maxRepetitions";
  public static final String F_MAX_REPETITIONS = "i{=10}";

  public static final String O_NON_REPEATERS = "Cn";
  public static final String P_NON_REPEATERS = "org.snmp4j.arg.nonRepeaters";
  public static final String F_NON_REPEATERS = "i{=0}";

  public static final String O_TRAPV1_AGENT_ADDR = "Ta";
  public static final String P_TRAPV1_AGENT_ADDR = "org.snmp4j.arg.trapv1.agentAddr";
  public static final String F_TRAPV1_AGENT_ADDR = "s{=0.0.0.0}<\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}>";

  public static final String O_TRAP_OID = "To";
  public static final String P_TRAP_OID = "org.snmp4j.arg.trap.trapOID";
  public static final String F_TRAP_OID = "s{=1.3.6.1.6.3.1.1.5.1}<([a-zA-Z\\-0-9]*:)?[0-9a-zA-Z\\-\\.]*>";

  public static final String O_TRAP_UPTIME = "Tu";
  public static final String P_TRAP_UPTIME = "org.snmp4j.arg.trap.trapUpTime";
  public static final String F_TRAP_UPTIME = "l{=0}";

  public static final String O_TRAPV1_ENTERPRISE = "Te";
  public static final String P_TRAPV1_ENTERPRISE = "org.snmp4j.arg.trap.trapv1.enterprise";
  public static final String F_TRAPV1_ENTERPRISE = "s{=0.0}<([a-zA-Z\\-0-9]*:)?[0-9a-zA-Z\\-\\.]*>";

  public static final String O_TRAPV1_SPECIFIC_ID = "Ts";
  public static final String P_TRAPV1_SPECIFIC_ID = "org.snmp4j.arg.trap.trapv1.specificID";
  public static final String F_TRAPV1_SPECIFIC_ID = "i{=0}";

  public static final String O_TRAPV1_GENERIC_ID = "Tg";
  public static final String P_TRAPV1_GENERIC_ID = "org.snmp4j.arg.trap.trapv1.genericID";
  public static final String F_TRAPV1_GENERIC_ID = "i{=0}";

  public static final String O_BOOT_COUNTER = "bc";
  public static final String P_BOOT_COUNTER = "org.snmp4j.arg.bootCounter";
  public static final String F_BOOT_COUNTER = "i{=0}";

  public static final String O_SEC_LEVEL = "sl";
  public static final String P_SEC_LEVEL = "org.snmp4j.arg.securityLevel";
  public static final String F_SEC_LEVEL = "i";


  private String oVersion;
  private String oLocalEngineID;
  private String oRetries;
  private String oTimeout;
  private String oAddress;
  private String oCommunity;
  private String oAuthoritativeEngineID;
  private String oSecurityName;
  private String oAuthPassphrase;
  private String oAuthProtocol;
  private String oPrivPassphrase;
  private String oPrivProtocol;
  private String oOperation;
  private String oMaxRepetitions;
  private String oNonRepeaters;
  private String oBootCounter;
  private String oContextName;
  private String oContextEngineID;
  private String oSecLevel;
  private String oAgentAddr;
  private String oTrapOID;
  private String oTrapSysUpTime;
  private String oGenericID;
  private String oSpecificID;
  private String oEnterprise;

  public SnmpConfigurator() {
    this(new Properties());
  }

  public SnmpConfigurator(Properties props) {
    oVersion = props.getProperty(P_VERSION, O_VERSION);
    oLocalEngineID = props.getProperty(P_LOCAL_ENGINE_ID, O_LOCAL_ENGINE_ID);
    oRetries = props.getProperty(P_RETRIES, O_RETRIES);
    oTimeout = props.getProperty(P_TIMEOUT, O_TIMEOUT);
    oAddress = props.getProperty(P_ADDRESS, O_ADDRESS);
    oCommunity = props.getProperty(P_COMMUNITY, O_COMMUNITY);
    oSecurityName = props.getProperty(P_SECURITY_NAME, O_SECURITY_NAME);
    oAuthoritativeEngineID = props.getProperty(P_AUTHORITATIVE_ENGINE_ID, O_AUTHORITATIVE_ENGINE_ID);
    oAuthPassphrase = props.getProperty(P_AUTH_PASSPHRASE, O_AUTH_PASSPHRASE);
    oAuthProtocol = props.getProperty(P_AUTH_PROTOCOL, O_AUTH_PROTOCOL);
    oPrivPassphrase = props.getProperty(P_PRIV_PASSPHRASE, O_PRIV_PASSPHRASE);
    oPrivProtocol = props.getProperty(P_PRIV_PROTOCOL, O_PRIV_PROTOCOL);
    oOperation = props.getProperty(P_OPERATION, O_OPERATION);
    oMaxRepetitions = props.getProperty(P_MAX_REPETITIONS, O_MAX_REPETITIONS);
    oNonRepeaters = props.getProperty(P_NON_REPEATERS, O_NON_REPEATERS);
    oBootCounter = props.getProperty(P_BOOT_COUNTER, O_BOOT_COUNTER);
    oContextName = props.getProperty(P_CONTEXT_NAME, O_CONTEXT_NAME);
    oContextEngineID = props.getProperty(P_CONTEXT_ENGINE_ID, O_CONTEXT_ENGINE_ID);
    oSecLevel = props.getProperty(P_SEC_LEVEL, O_SEC_LEVEL);
    oAgentAddr = props.getProperty(P_TRAPV1_AGENT_ADDR, O_TRAPV1_AGENT_ADDR);
    oTrapOID = props.getProperty(P_TRAP_OID, O_TRAP_OID);
    oTrapSysUpTime = props.getProperty(P_TRAP_UPTIME, O_TRAP_UPTIME);
    oSpecificID = props.getProperty(P_TRAPV1_SPECIFIC_ID, O_TRAPV1_SPECIFIC_ID);
    oGenericID = props.getProperty(P_TRAPV1_GENERIC_ID, O_TRAPV1_GENERIC_ID);
    oEnterprise = props.getProperty(P_TRAPV1_ENTERPRISE, O_TRAPV1_ENTERPRISE);
  }

  public void configure(Snmp snmp, Map settings) {
    if (snmp.getUSM() != null) {
      int engineBoots = 0;
      Integer bc =
          (Integer) ArgumentParser.getValue(settings, oBootCounter, 0);
      if (bc != null) {
        engineBoots = bc.intValue();
        snmp.getUSM().setEngineBoots(engineBoots);
      }
      int engineTime = 0;
      OctetString localEngineID =
          createOctetString((String)
                            ArgumentParser.getValue(settings, oLocalEngineID, 0),
                            null);
      if (localEngineID == null) {
        if (snmp.getLocalEngineID() == null) {
          snmp.setLocalEngine(MPv3.createLocalEngineID(), engineBoots,
                              engineTime);
        }
      }
      else {
        snmp.setLocalEngine(localEngineID.getValue(), engineBoots, engineTime);
      }
      String sn = (String)
          ArgumentParser.getValue(settings, oSecurityName, 0);
      if (sn != null) {
        String authPP =
            (String) ArgumentParser.getValue(settings, oAuthPassphrase, 0);
        String privPP =
            (String) ArgumentParser.getValue(settings, oPrivPassphrase, 0);
        OID authProtocol = null;
        String authP =
            (String) ArgumentParser.getValue(settings, oAuthProtocol, 0);
        String privP =
            (String) ArgumentParser.getValue(settings, oPrivProtocol, 0);
        OID privProtocol = null;
        if ("MD5".equals(authP)) {
          authProtocol = AuthMD5.ID;
        }
        else if ("SHA".equals(authP)) {
          authProtocol = AuthSHA.ID;
        }
        if ("DES".equals(privP)) {
          privProtocol = PrivDES.ID;
        }
        else if ("3DES".equals(privP)) {
          privProtocol = Priv3DES.ID;
        }
        else if ("AES".equals(privP) || "AES128".equals(privP)) {
          privProtocol = PrivAES128.ID;
        }
        else if ("AES192".equals(privP)) {
          privProtocol = PrivAES192.ID;
        }
        else if ("AES256".equals(privP)) {
          privProtocol = PrivAES256.ID;
        }
        OctetString un = createOctetString(sn, null);
        snmp.getUSM().addUser(un, new UsmUser(un,
                                              authProtocol,
                                              createOctetString(authPP, null),
                                              privProtocol,
                                              createOctetString(privPP, null)));
      }
    }
  }

  public PDUFactory getPDUFactory(Map settings) {
    return new InnerPDUFactory(settings);
  }

  public Target getTarget(Map settings) {
    String version =
        (String) ArgumentParser.getValue(settings, oVersion, 0);
    OctetString community =
        createOctetString((String)
                          ArgumentParser.getValue(settings, oCommunity, 0),
                          "public");
    Target t;
    if ("1".equals(version)) {
      t = new CommunityTarget();
      t.setVersion(SnmpConstants.version1);
      ((CommunityTarget)t).setCommunity(community);
    }
    else if ("2c".equals(version)) {
      t = new CommunityTarget();
      t.setVersion(SnmpConstants.version2c);
      ((CommunityTarget)t).setCommunity(community);
    }
    else {
      UserTarget ut = new UserTarget();
      t = ut;
      String ae = (String)
          ArgumentParser.getValue(settings, oAuthoritativeEngineID, 0);
      if (ae != null) {
        ut.setAuthoritativeEngineID(createOctetString(ae, null).getValue());
      }
      ut.setSecurityModel(USM.SECURITY_MODEL_USM);
      String sn = (String)
          ArgumentParser.getValue(settings, oSecurityName, 0);
      if (sn != null) {
        ut.setSecurityName(createOctetString(sn, null));
      }
      Integer secLevel =
          (Integer) ArgumentParser.getValue(settings, oSecLevel, 0);
      if (secLevel == null) {
        if (settings.containsKey(oPrivPassphrase)) {
          ut.setSecurityLevel(SecurityLevel.AUTH_PRIV);
        }
        else if (settings.containsKey(oAuthPassphrase)) {
          ut.setSecurityLevel(SecurityLevel.AUTH_NOPRIV);
        }
        else {
          ut.setSecurityLevel(SecurityLevel.NOAUTH_NOPRIV);
        }
      }
      else {
        ut.setSecurityLevel(secLevel.intValue());
      }
    }
    int retries = 0;
    Number r = (Number) ArgumentParser.getValue(settings, oRetries, 0);
    if (r != null) {
      retries = r.intValue();
    }
    t.setRetries(retries);
    long timeout = 5000;
    Number to = (Number) ArgumentParser.getValue(settings, oTimeout, 0);
    if (to != null) {
      timeout = to.longValue();
    }
    t.setTimeout(timeout);
    String addr = (String) ArgumentParser.getValue(settings, oAddress, 0);
    if (addr != null) {
      if (addr.indexOf('/') <= 0) {
        addr += "/161";
      }
      t.setAddress(GenericAddress.parse(addr));
    }
    return t;
  }

  /**
   * Create an OctetString from a String value.
   * @param s
   *    the string value to convert. If it starts with <code>0x</code> its
   *    trailing value will be interpreted as a hex string with colon (:)
   *    separator.
   * @param defaultString
   *    an optional default if <code>s</code> is <code>null</code>.
   * @return
   *    an OctetString or <code>null</code> if <code>s</code> is
   *    <code>null</code>.
   * @since 1.10.2
   */
  public static OctetString createOctetString(String s, String defaultString) {
    OctetString octetString = null;
    if (s == null) {
      s = defaultString;
    }
    if ((s != null) && s.startsWith("0x")) {
      octetString = OctetString.fromHexString(s.substring(2), ':');
    }
    else if (s != null) {
      octetString = new OctetString(s);
    }
    return octetString;
  }

  public class InnerPDUFactory implements PDUFactory {

    private Map settings;

    public InnerPDUFactory(Map settings) {
      this.settings = settings;
    }

    public PDU createPDU(Target target) {
      String pduType = (String) ArgumentParser.getValue(settings, oOperation, 0);
      if (pduType == null) {
        pduType = "GET";
      }
      pduType = pduType.toUpperCase();
      int type = PDU.getTypeFromString(pduType);
      PDU pdu = DefaultPDUFactory.createPDU(target, type);
      if ((type == PDU.V1TRAP)  && (!(pdu instanceof PDUv1))) {
        throw new RuntimeException("V1TRAP can only be sent using SNMPv1");
      }
      if (type == PDU.GETBULK) {
        Integer maxRep = getMaxRepetitions();
        if (maxRep != null) {
          pdu.setMaxRepetitions(maxRep.intValue());
        }
        Integer nonRepeaters = getNonRepeaters();
        if (nonRepeaters != null) {
          pdu.setNonRepeaters(nonRepeaters.intValue());
        }
      }
      else if ((type == PDU.TRAP) || (type == PDU.INFORM)) {
        Number tu = (Number) ArgumentParser.getValue(settings, oTrapSysUpTime, 0);
        if (tu != null) {
          pdu.add(new VariableBinding(SnmpConstants.sysUpTime,
                                      new TimeTicks(tu.longValue())));
        }
        String to = (String) ArgumentParser.getValue(settings, oTrapOID, 0);
        if (to != null) {
          pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(to)));
        }
      }
      else if (type == PDU.V1TRAP) {
        PDUv1 pduV1 = (PDUv1)pdu;
        String aa =
            (String) ArgumentParser.getValue(settings, oAgentAddr, 0);
        if (aa != null) {
          pduV1.setAgentAddress(new IpAddress(aa));
        }
        Integer gid = (Integer) ArgumentParser.getValue(settings, oGenericID, 0);
        if (gid != null) {
          pduV1.setGenericTrap(gid.intValue());
        }
        Integer sid = (Integer) ArgumentParser.getValue(settings, oSpecificID, 0);
        if (sid != null) {
          pduV1.setSpecificTrap(sid.intValue());
        }
        String e =
            (String) ArgumentParser.getValue(settings, oEnterprise, 0);
        if (e != null) {
          pduV1.setEnterprise(new OID(e));
        }
      }
      if (pdu instanceof ScopedPDU) {
        ScopedPDU scoped = (ScopedPDU)pdu;
        String cEngineID =
            (String) ArgumentParser.getValue(settings, oContextEngineID, 0);
        if (cEngineID != null) {
          scoped.setContextEngineID(createOctetString(cEngineID, null));
        }
        String cn =
            (String) ArgumentParser.getValue(settings, oContextName, 0);
        if (cn != null) {
          scoped.setContextName(createOctetString(cn, null));
        }
      }
      return pdu;
    }

    public Integer getMaxRepetitions() {
      Integer maxRep =
          (Integer) ArgumentParser.getValue(settings, oMaxRepetitions, 0);
      return maxRep;
    }

    public Integer getNonRepeaters() {
      return (Integer) ArgumentParser.getValue(settings, oNonRepeaters, 0);
    }
  }
}
