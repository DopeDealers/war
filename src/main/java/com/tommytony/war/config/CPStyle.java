package com.tommytony.war.config;

public enum CPStyle {

	ANALOG("Analog"),
	DIGITAL("Digital"),
	BOTH("Both");
	private final String displayName;

	CPStyle(String displayName) {
		this.displayName = displayName;
	}

	public static CPStyle getFromString(String string) {
		for (CPStyle cpStyle : CPStyle.values()) {
			if (string.toLowerCase().equals(cpStyle.toString())) {
				return cpStyle;
			}
		}

		return CPStyle.DIGITAL;
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}

	public String getDisplayName() {
		return displayName;
	}
}
