/*
 * This file is part of the dSploit.
 *
 * Copyleft of Simone Margaritelli aka evilsocket <evilsocket@gmail.com>
 *
 * dSploit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * dSploit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with dSploit.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.evilsocket.dsploit.wifi;

import java.util.Comparator;
import java.util.List;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkManager 
{	
	public static void cleanPreviousConfiguration( WifiManager wifiMgr, WifiInfo info ) {
		WifiConfiguration config;
		do{
			config = getWifiConfiguration( wifiMgr, info );
			if ( config!= null) 
				wifiMgr.removeNetwork(config.networkId);
		} while ( config != null );
	}
	
	public static int getMaxPriority(WifiManager wifiManager) {
		List<WifiConfiguration> configurations = wifiManager.getConfiguredNetworks();
		int pri = 0;
		for( WifiConfiguration config : configurations) {
			if (config.priority > pri) {
				pri = config.priority;
			}
		}
		return pri;
	}

	public static void sortByPriority(List<WifiConfiguration> configurations) {
		java.util.Collections.sort(configurations,
			new Comparator<WifiConfiguration>() {
				public int compare(WifiConfiguration object1, WifiConfiguration object2) {
					return object1.priority - object2.priority;
				}
			}
		);
	}

	public static int shiftPriorityAndSave(final WifiManager wifiMgr) {
		final List<WifiConfiguration> configurations = wifiMgr
				.getConfiguredNetworks();
		sortByPriority(configurations);
		final int size = configurations.size();
		for (int i = 0; i < size; i++) {
			final WifiConfiguration config = configurations.get(i);
			config.priority = i;
			wifiMgr.updateNetwork(config);
		}
		wifiMgr.saveConfiguration();
		return size;
	}

	public static WifiConfiguration getWifiConfiguration( WifiManager wifiMgr, WifiInfo info ) {
		final String ssid = info.getSSID();
		if (ssid.length() == 0) {
			return null;
		}

		String bssid = info.getBSSID();
		List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();

		for (final WifiConfiguration config : configurations) {
			if (config.SSID == null || !ssid.equals(config.SSID)) {
				continue;
			}
			if (config.BSSID == null || bssid == null
					|| bssid.equals(config.BSSID)) {
				return config;
			}
		}
		return null;
	}
	
	public static WifiConfiguration getWifiConfiguration( WifiManager wifiMgr, WifiConfiguration configToFind ) {
		final String ssid = configToFind.SSID;
		if (ssid.length() == 0) {
			return null;
		}

		String bssid = configToFind.BSSID;
		List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();

		for (final WifiConfiguration config : configurations) {
			if (config.SSID == null || !ssid.equals(config.SSID)) {
				continue;
			}
			if (config.BSSID == null || bssid == null
					|| bssid.equals(config.BSSID)) {
				return config;
			}
		}
		return null;
	}
}
