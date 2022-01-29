package com.rohan;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.state.HostInfo;

public class ApplicationState {
	private KafkaStreams streams;
	private HostInfo hostPortInfo;

	public KafkaStreams getStreams() {
		return streams;
	}

	public void setStreams(KafkaStreams streams) {
		this.streams = streams;
	}

	public HostInfo getHostPortInfo() {
		return hostPortInfo;
	}

	public void setHostPortInfo(HostInfo hostPortInfo) {
		this.hostPortInfo = hostPortInfo;
	}
}
