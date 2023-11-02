package org.ulpgc.cleaner.stopwords;

import java.util.Set;

public interface StopwordProvider {
    Set<String> getStopwords();
}
