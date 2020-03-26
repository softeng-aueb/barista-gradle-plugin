package gr.aueb.android.barista.core.model;

import gr.aueb.android.barista.emulator.EmulatorManager;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.stream.Stream;

import static org.junit.Assert.*;


public class SvcWifiStatusTest {

    @Test
    public void parseResultIfConnected() {

        SvcWifiStatus wifiStatus = new SvcWifiStatus(null);
        String testString = "rec[14]: time=06-20 20:03:41.080 processed=DefaultState org=HandshakeState dest=CompletedState what=147462(0x24006)\n" +
                " rec[15]: time=06-20 20:05:38.636 processed=DefaultState org=CompletedState dest=UninitializedState what=131183(0x2006f)\n" +
                " rec[16]: time=06-20 20:05:42.298 processed=DefaultState org=UninitializedState dest=HandshakeState what=147462(0x24006)\n" +
                " rec[17]: time=06-20 20:05:42.330 processed=DefaultState org=HandshakeState dest=CompletedState what=147462(0x24006)\n" +
                " rec[18]: time=06-20 20:06:07.752 processed=DefaultState org=CompletedState dest=UninitializedState what=131183(0x2006f)\n" +
                " rec[19]: time=06-20 20:06:11.409 processed=DefaultState org=UninitializedState dest=HandshakeState what=147462(0x24006)\n" +
                " rec[20]: time=06-20 20:06:11.442 processed=DefaultState org=HandshakeState dest=CompletedState what=147462(0x24006)\n" +
                "curState=CompletedState\n" +
                "mAuthFailureInSupplicantBroadcast false\n" +
                "mAuthFailureReason 0\n" +
                "mNetworksDisabledDuringConnect false\n" +
                "\n" +
                "mLinkProperties {InterfaceName: wlan0 LinkAddresses: [fe80::ff:fe44:5566/64,192.168.232.2/21,fec0::ff:fe44:5566/64,fec0::90dd:3c93:5a1c:84e3/64,]  Routes: [fe80::/64 -> :: wlan0,::/0 -> fe80::ff:fe00:100 wlan0,fec0::/64 -> :: wlan0,192.168.232.0/21 -> 0.0.0.0 wlan0,0.0.0.0/0 -> 192.168.232.1 wlan0,] DnsAddresses: [10.0.2.3,] UsePrivateDns: false PrivateDnsServerName: null Domains: null MTU: 0 TcpBufferSizes: 524288,1048576,2097152,262144,524288,1048576}\n" +
                "mWifiInfo SSID: AndroidWifi, BSSID: 02:00:00:00:01:00, MAC: 02:00:00:44:55:66, Supplicant state: COMPLETED, RSSI: -50, Link speed: 58Mbps, Frequency: 2447MHz, Net ID: 0, Metered hint: false, score: 60\n" +
                "mDhcpResults IP address 192.168.232.2/21 Gateway 192.168.232.1  DNS servers: [ 10.0.2.3 ] Domains  DHCP server /192.168.232.1 Vendor info null lease 1476526080 seconds\n" +
                "mNetworkInfo [type: WIFI[], state: CONNECTED/CONNECTED, reason: (unspecified), extra: (none), failover: false, available: true, roaming: false]\n" +
                "mLastSignalLevel 4\n" +
                "mLastBssid 02:00:00:00:01:00\n" +
                "mLastNetworkId 0\n" +
                "mOperationalMode 1\n" +
                "mUserWantsSuspendOpt true\n" +
                "mSuspendOptNeedsDisabled 0\n" +
                "CountryCode sent to driver: US\n" +
                "mConnectionReqCount 3\n" +
                "mUntrustedReqCount 0\n" +
                "Wlan Wake Reasons: totalCmdEventWake 5 totalDriverFwLocalWake -206245696 totalRxDataWake -206245696 rxUnicast 4 rxMulticast 64 rxBroadcast -206557060 icmp -195338852 icmp6 -206466556 icmp6Ra -972025774 icmp6Na -195711691 icmp6Ns 246 ipv4RxMulticast -195338852 ipv6Multicast 8 otherRxMulticast -7588504\n" +
                "\n" +
                "Dump of WifiConfigManager" ;

        new BufferedReader(new StringReader(testString))
                .lines().forEach(line->wifiStatus.parseResult(Stream.of(line)));

        System.out.println(wifiStatus.getStatus());
    }

    @Test
    public void parseResultIfDisconnected() {

        SvcWifiStatus wifiStatus = new SvcWifiStatus(null);
        String testString = "rec[13]: time=06-20 20:03:41.047 processed=DefaultState org=UninitializedState dest=HandshakeState what=147462(0x24006)\n" +
                " rec[14]: time=06-20 20:03:41.080 processed=DefaultState org=HandshakeState dest=CompletedState what=147462(0x24006)\n" +
                " rec[15]: time=06-20 20:05:38.636 processed=DefaultState org=CompletedState dest=UninitializedState what=131183(0x2006f)\n" +
                " rec[16]: time=06-20 20:05:42.298 processed=DefaultState org=UninitializedState dest=HandshakeState what=147462(0x24006)\n" +
                " rec[17]: time=06-20 20:05:42.330 processed=DefaultState org=HandshakeState dest=CompletedState what=147462(0x24006)\n" +
                " rec[18]: time=06-20 20:06:07.752 processed=DefaultState org=CompletedState dest=UninitializedState what=131183(0x2006f)\n" +
                " rec[19]: time=06-20 20:06:11.409 processed=DefaultState org=UninitializedState dest=HandshakeState what=147462(0x24006)\n" +
                " rec[20]: time=06-20 20:06:11.442 processed=DefaultState org=HandshakeState dest=CompletedState what=147462(0x24006)\n" +
                "curState=CompletedState\n" +
                "mAuthFailureInSupplicantBroadcast false\n" +
                "mAuthFailureReason 0\n" +
                "mNetworksDisabledDuringConnect false\n" +
                "\n" +
                "mLinkProperties {InterfaceName: wlan0 LinkAddresses: [192.168.232.2/21,]  Routes: [192.168.232.0/21 -> 0.0.0.0 wlan0,0.0.0.0/0 -> 192.168.232.1 wlan0,] DnsAddresses: [10.0.2.3,] UsePrivateDns: false PrivateDnsServerName: null Domains: null MTU: 0 TcpBufferSizes: 524288,1048576,2097152,262144,524288,1048576}\n" +
                "mWifiInfo SSID: <unknown ssid>, BSSID: <none>, MAC: 02:00:00:44:55:66, Supplicant state: DISCONNECTED, RSSI: -127, Link speed: -1Mbps, Frequency: -1MHz, Net ID: -1, Metered hint: false, score: 0\n" +
                "mDhcpResults IP address Gateway  DNS servers: [ ] Domains  DHCP server /192.168.232.1 Vendor info null lease 0 seconds\n" +
                "mNetworkInfo [type: WIFI[], state: DISCONNECTED/DISCONNECTED, reason: (unspecified), extra: (none), failover: false, available: false, roaming: false]\n" +
                "mLastSignalLevel 4\n" +
                "mLastBssid null\n" +
                "mLastNetworkId -1\n" +
                "mOperationalMode 4\n" +
                "mUserWantsSuspendOpt true\n" +
                "mSuspendOptNeedsDisabled 0\n" +
                "CountryCode sent to driver: US\n" +
                "mConnectionReqCount 3\n" +
                "mUntrustedReqCount 0\n" +
                "Wlan Wake Reasons:null\n" +
                "\n" +
                "Dump of WifiConfigManager\n" +
                "WifiConfigManager - Log Begin ----\n" +
                "2019-06-20T19:34:06.967 - clearInternalData: Clearing all internal data\n" +
                "2019-06-20T19:34:12.182 - setNetworkSelectionStatus: configKey=\"AndroidWifi\"NONE networkStatus=NETWORK_SELECTION_ENABLED disableReason=NETWORK_SELECTION_ENABLE at=time=06-20 19:34:12.182\n" +
                "2019-06-20T19:36:04.914 - setNetworkSelectionStatus: configKey=\"AndroidWifi\"NONE networkStatus=NETWORK_SELECTION_ENABLED disableReason=NETWORK_SELECTION_ENABLE at=time=06-20 19:36:04.914\n" +
                "2019-06-20T19:58:20.810 - setNetworkSelectionStatus: configKey=\"AndroidWifi\"NONE networkStatus=NETWORK_SELECTION_ENABLED disableReason=NETWORK_SELECTION_ENABLE at=time=06-20 19:58:20.809\n" +
                "2019-06-20T20:00:40.530 - setNetworkSelectionStatus: configKey=\"AndroidWifi\"NONE networkStatus=NETWORK_SELECTION_ENABLED disableReason=NETWORK_SELECTION_ENABLE at=time=06-20 20:00:40.529\n" +
                "2019-06-20T20:03:41.520 - setNetworkSelectionStatus: configKey=\"AndroidWifi\"NONE networkStatus=NETWORK_SELECTION_ENABLED disableReason=NETWORK_SELECTION_ENABLE at=time=06-20 20:03:41.520\n" +
                "2019-06-20T20:05:42.804 - setNetworkSelectionStatus: configKey=\"AndroidWifi\"NONE networkStatus=NETWORK_SELECTION_ENABLED disableReason=NETWORK_SELECTION_ENABLE at=time=06-20 20:05:42.803\n" +
                "2019-06-20T20:06:11.904 - setNetworkSelectionStatus: configKey=\"AndroidWifi\"NONE networkStatus=NETWORK_SELECTION_ENABLED disableReason=NETWORK_SELECTION_ENABLE at=time=06-20 20:06:11.904\n" +
                "WifiConfigManager - Log End ----\n" +
                "WifiConfigManager - Configured networks Begin ----\n" +
                "ID: 0 SSID: \"AndroidWifi\" PROVIDER-NAME: null BSSID: null FQDN: null PRIO: 0 HIDDEN: false\n" +
                " NetworkSelectionStatus NETWORK_SELECTION_ENABLED\n" +
                " hasEverConnected: true\n" +
                " numAssociation 9\n" +
                " validatedInternetAccess\n" +
                " KeyMgmt: NONE Protocols: WPA RSN\n" +
                " AuthAlgorithms: OPEN\n" +
                " PairwiseCiphers: TKIP CCMP\n" +
                " GroupCiphers: WEP40 WEP104 TKIP CCMP\n" +
                " PSK: " ;

        new BufferedReader(new StringReader(testString))
                .lines().forEach(line->wifiStatus.parseResult(Stream.of(line)));

        System.out.println(wifiStatus.getStatus());
    }

}