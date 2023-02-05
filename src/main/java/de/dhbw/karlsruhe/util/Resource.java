package de.dhbw.karlsruhe.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Resource {

  public String getResourceAsString(String fileName) throws IOException {
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    InputStream inputStream = classloader.getResourceAsStream(fileName);
    InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    BufferedReader reader = new BufferedReader(streamReader);

    StringBuilder contentAsJson = new StringBuilder();

    for (String line; (line = reader.readLine()) != null; ) {
      contentAsJson.append(line);
    }

    return contentAsJson.toString();
  }

}
