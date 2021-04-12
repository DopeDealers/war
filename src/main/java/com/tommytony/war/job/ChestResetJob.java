package com.tommytony.war.job;

import com.tommytony.war.volume.Volume;

public class ChestResetJob implements Runnable {

	private final Volume volume;

	public ChestResetJob(Volume volume) {
		this.volume = volume;
	}

	public void run() {
		this.volume.resetChests();
	}

}
