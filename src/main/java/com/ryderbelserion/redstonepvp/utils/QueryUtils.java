package com.ryderbelserion.redstonepvp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class QueryUtils {

    public static List<String> getStatements(final InputStream inputStream) throws IOException {
        final List<String> queries = new LinkedList<>();

        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("--") || line.startsWith("#")) {
                    continue;
                }

                builder.append(line);

                // check for end of declaration
                if (line.endsWith(";")) {
                    builder.deleteCharAt(builder.length() - 1);

                    String result = builder.toString().trim();

                    if (!result.isEmpty()) {
                        queries.add(result);
                    }

                    // reset
                    builder = new StringBuilder();
                }
            }
        }

        return queries;
    }
}