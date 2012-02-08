package ca.usask.cs.srlab.simcad.token;

public class TokenBuildStrategy {

	private boolean tokenFrequencyNormalization;
	private int tokenFrequencyNormalizationThreshold;
	private int tokenFrequencyNormalizationValueOverThreshold;

	public boolean isTokenFrequencyNormalizationOn() {
		return tokenFrequencyNormalization;
	}

	public int isTokenFrequencyNormalizationThreshold() {
		return tokenFrequencyNormalizationThreshold;
	}

	public int isTokenFrequencyNormalizationValueOverThreshold() {
		return tokenFrequencyNormalizationValueOverThreshold;
	}

}
