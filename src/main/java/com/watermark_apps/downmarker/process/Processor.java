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

package com.watermark_apps.downmarker.process;

import com.watermark_apps.downmarker.language.Lexer;
import com.watermark_apps.downmarker.language.Parser;
import com.watermark_apps.downmarker.language.token.IToken;

import java.util.Stack;

public class Processor {
    private final Lexer lexer;
    private final Parser parser;

    public Processor(Lexer lexer, Parser parser) {
        this.lexer = lexer;
        this.parser = parser;
    }

    public String process(String markdownText) {
        Stack<IToken> tokens = lexer.lex(markdownText);

        return parser.parse(tokens);
    }
}
