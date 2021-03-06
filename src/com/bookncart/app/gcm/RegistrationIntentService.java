/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bookncart.app.gcm;

import java.io.IOException;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.bookncart.app.preferences.ZPreferences;
import com.bookncart.app.utils.CommonLib;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

public class RegistrationIntentService extends IntentService {

	private static final String TAG = "RegIntentService";
	private static final String[] TOPICS = { "global" };

	public RegistrationIntentService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			// [START register_for_gcm]
			// Initially this call goes out to the network to retrieve the
			// token, subsequent calls
			// are local.
			// [START get_token]
			InstanceID instanceID = InstanceID.getInstance(this);
			String token = instanceID.getToken(CommonLib.GCM_SENDER_ID,
					GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

			// TODO: Implement this method to send any registration to your
			// app's servers.
			sendRegistrationToServer(token);

			// Subscribe to topic channels
			subscribeTopics(token);

			ZPreferences.setIsGcmRegistered(this, true);
		} catch (Exception e) {
			ZPreferences.setIsGcmRegistered(this, false);
		}
		Intent registrationComplete = new Intent(CommonLib.GCM_TAG);
		LocalBroadcastManager.getInstance(this).sendBroadcast(
				registrationComplete);
	}

	private void sendRegistrationToServer(String token) {
		ZPreferences.setGcmToken(this, token);
	}

	// [START subscribe_topics]
	private void subscribeTopics(String token) throws IOException {
		GcmPubSub pubSub = GcmPubSub.getInstance(this);
		for (String topic : TOPICS) {
			pubSub.subscribe(token, "/topics/" + topic, null);
		}
	}
	// [END subscribe_topics]
}
