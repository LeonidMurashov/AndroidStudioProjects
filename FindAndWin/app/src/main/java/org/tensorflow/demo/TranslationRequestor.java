package org.tensorflow.demo;

/**
 * Interface for translator responses
 */

public interface TranslationRequestor {
	void ReceiveTranslation(String translation, int code);
}
