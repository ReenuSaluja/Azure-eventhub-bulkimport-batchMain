package com.microsoft.azure.eventhubs.samples.Basic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.ConnectionStringBuilder;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
//import com.microsoft.azure.eventhubs.EventHubException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class SendBatch {

	public static void main(String[] args) throws Exception, ExecutionException, InterruptedException, IOException {

		final String namespaceName = "";
		final String eventHubName = "";
		final String sasKeyName = "";
		final String sasKey = "";

		final ConnectionStringBuilder connStr = new ConnectionStringBuilder(namespaceName, eventHubName, sasKeyName,
				sasKey);
		final Gson gson = new GsonBuilder().create();
		final EventHubClient sender = EventHubClient.createFromConnectionStringSync(connStr.toString());
		try {
			while (true) {
				final LinkedList<EventData> events = new LinkedList<>();
				for (int count = 1; count <= 1000; count++) {
					String s = ": = " + count;
					byte[] payloadBytes = s.getBytes("UTF-8");
					EventData sendEvent = new EventData(payloadBytes);
					events.add(sendEvent);

				}
				System.out.println(events.size());
				sender.sendSync(events, "zero");
				sender.sendSync(events, "one");

				System.out.println(String.format("Sent Batch... Size: %s", events.size()));
			}
		} finally {
			sender.closeSync();
		}
	}

	/**
	 * actual application-payload, ex: a telemetry event
	 */
	static final class PayloadEvent {
		PayloadEvent(final int seed) {
			this.id = "telemetryEvent1-critical-eventid-2345" + seed;
			this.strProperty = "I am a mock telemetry event from JavaClient.";
			this.longProperty = seed * new Random().nextInt(seed);
			this.intProperty = seed * new Random().nextInt(seed);
		}

		final public String id;
		final public String strProperty;
		final public long longProperty;
		final public int intProperty;
	}

}