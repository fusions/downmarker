/*
 * Copyright (c) fusions corporation,
 * All rights reserved.
 *
 * This file is part of Downmarker.
 *
 * This program and the accompanying materials are made available under
 * the terms of the MIT License which accompanies this
 * distribution, and is available at https://opensource.org/licenses/MIT
 */

package com.watermark_apps.downmarker.functional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.watermark_apps.downmarker.language.Lexer;
import com.watermark_apps.downmarker.language.Options;
import com.watermark_apps.downmarker.language.Parser;
import com.watermark_apps.downmarker.process.Processor;
import com.watermark_apps.downmarker.process.ProcessorFactory;
import com.watermark_apps.downmarker.renderer.HtmlRenderer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class MarkdownSpecTests {
    private static final List<String> SUPPORTED_SECTIONS = Collections.unmodifiableList(Arrays.asList(
        "Links",
        "Images",
        "Autolinks",
        "[extension] Autolinks"
    ));

    private static Map<String, Options> specGroups = new HashMap<>();

    @BeforeAll
    static void initSpecs() {
        specGroups.put("GFM", new Options().gfm(true).pedantic(false).headerIds(false));
    }

    @TestFactory
    Stream<DynamicNode> specExamples() {
        System.out.println("Hello World!");
        return specGroups.entrySet().stream().map(entry -> {
            String title = entry.getKey();
            Options options = entry.getValue();
            ClassLoader classLoader = getClass().getClassLoader();
            File[] files = new File(classLoader.getResource("spec/" + title.toLowerCase()).getFile()).listFiles();
            assumeTrue(files != null);

            Processor processor = new ProcessorFactory(new Lexer(), new Parser(new HtmlRenderer())).create(options);

            return dynamicContainer(title, Arrays.stream(files).map(file -> {
                int lastIndexOfPeriod = file.getName().lastIndexOf('.');
                String extension = lastIndexOfPeriod == -1 ? "" : file.getName().substring(lastIndexOfPeriod);
                SpecExample[] specExamples = new SpecExample[0];
                switch (extension) {
                    case ".json":
                        try {
                            specExamples = new ObjectMapper().readValue(file, SpecExample[].class);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        break;
                    default:
                        break;
                }

                return dynamicContainer(file.getName(), Arrays.stream(specExamples).map(specExample -> dynamicTest("#" + specExample.example, () -> {
                    assumeTrue(SUPPORTED_SECTIONS.contains(specExample.section), "Section \"" + specExample.section + "\" is not supported. Skipped.");

                    if (specExample.shouldFail) {
                        assertNotEquals(specExample.html.trim(), processor.process(specExample.markdown).trim(), specExample.markdown);

                        return;
                    }

                    assertEquals(specExample.html.trim(), processor.process(specExample.markdown).trim(), specExample.markdown);
                })));
            }));
        });
    }
}
