/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.u2p.core.nsd;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.net.nsd.NsdManager;
import android.util.Log;
import android.widget.Toast;

public class NsdHelper {

    Context mContext;

    NsdManager mNsdManager;
    NsdManager.ResolveListener mResolveListener;
    NsdManager.DiscoveryListener mDiscoveryListener;
    NsdManager.RegistrationListener mRegistrationListener;
	private boolean discovering, registered,connected;

    public static final String SERVICE_TYPE = "_http._tcp.";

    public static final String TAG = "NsdHelper";
    public String mServiceName = "U2P";

    NsdServiceInfo mService;

    public NsdHelper(Context context) {
        mContext = context;
        mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        setDiscovering(false);
        setRegistered(false);
        setConnected(false);
    }

	public void initializeDiscoveryListener() {
        mDiscoveryListener = new NsdManager.DiscoveryListener() {
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
                setDiscovering(true);
            }

            public void onServiceFound(NsdServiceInfo service) {
                Log.d(TAG, "Service discovery success" + service);
                if (!service.getServiceType().equals(SERVICE_TYPE)) {
                    Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
                } else if (service.getServiceName().equals(mServiceName)) {
                    Log.d(TAG, "Same machine: " + mServiceName);
                } else if (service.getServiceName().contains(mServiceName)){
                    Toast.makeText(mContext, "Service "+service.getServiceName()+" found!!!",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Service: " + service.getServiceName() + " found!!!");
                    mNsdManager.resolveService(service, mResolveListener);
                }
            }

            public void onServiceLost(NsdServiceInfo service) {
                Log.e(TAG, "service lost" + service);
                Toast.makeText(mContext, "Service lost",Toast.LENGTH_SHORT).show();
                if (mService == service) {
                    mService = null;
                }
            }
            
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
                setDiscovering(false);
            }

            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }
        };
    }

    public void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {

            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Toast.makeText(mContext, "Resolve failed",Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Resolve failed" + errorCode);
            }

            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.d(TAG, "Resolve Succeeded. " + serviceInfo);
                Toast.makeText(mContext, "Resolve succeeded",Toast.LENGTH_SHORT).show();
                if (serviceInfo.getServiceName().equals(mServiceName)) {
                    Log.d(TAG, "Same IP.");
                    return;
                }
                mService = serviceInfo;
                setConnected(true);
            }
        };
    }

    public void initializeRegistrationListener() {
        mRegistrationListener = new NsdManager.RegistrationListener() {

            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                mServiceName = NsdServiceInfo.getServiceName();
                Toast.makeText(mContext, "Service registered: "+mServiceName,Toast.LENGTH_SHORT).show();
                Log.d(TAG,"Service registered: "+mServiceName);
                setRegistered(true);
            }
            
            public void onRegistrationFailed(NsdServiceInfo arg0, int arg1) {
            	Toast.makeText(mContext, "Service registration failed",Toast.LENGTH_SHORT).show();
            	Log.e(TAG,"Service registration failed");
            }

            public void onServiceUnregistered(NsdServiceInfo arg0) {
            	Toast.makeText(mContext, "Service unregistered",Toast.LENGTH_SHORT).show();
            	Log.d(TAG,"Service unregistered");
            	setRegistered(false);
            }
            
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            	Toast.makeText(mContext, "Service registration failed",Toast.LENGTH_SHORT).show();
            	Log.e(TAG,"Service registration failed");
            }
            
        };
    }

    public void registerService(int port) {
        NsdServiceInfo serviceInfo  = new NsdServiceInfo();
        serviceInfo.setPort(port);
        serviceInfo.setServiceName(mServiceName);
        serviceInfo.setServiceType(SERVICE_TYPE);
        
        mNsdManager.registerService(
                serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
        
    }

    public void discoverServices() {
        mNsdManager.discoverServices(
                SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }
    
    public void stopDiscovery() {
        mNsdManager.stopServiceDiscovery(mDiscoveryListener);
    }

    public NsdServiceInfo getChosenServiceInfo() {
        return mService;
    }
    
    public void tearDown() {
        mNsdManager.unregisterService(mRegistrationListener);
    }

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public boolean isDiscovering() {
		return discovering;
	}

	public void setDiscovering(boolean discovering) {
		this.discovering = discovering;
	}
    public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}
}
