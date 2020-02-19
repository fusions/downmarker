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

import com.watermark_apps.downmarker.language.Grammar;
import com.watermark_apps.downmarker.language.InlineCompiler;
import com.watermark_apps.downmarker.language.Lexer;
import com.watermark_apps.downmarker.language.Options;
import com.watermark_apps.downmarker.language.Parser;

public class ProcessorFactory {
    private final Lexer lexer;
    private final Parser parser;

    public ProcessorFactory(Lexer lexer, Parser parser) {
        this.lexer = lexer;
        this.parser = parser;
    }

    public Processor create(Options options) {
        if (options.gfm) {
            lexer.setRules(Grammar.BLOCK_RULES_GFM);
            parser.setInlineCompiler(new InlineCompiler(Grammar.INLINE_RULES_GFM, parser.getRenderer()));
        } else {
            lexer.setRules(Grammar.BLOCK_RULES_NORMAL);
            parser.setInlineCompiler(new InlineCompiler(Grammar.INLINE_RULES_NORMAL, parser.getRenderer()));
        }

        return new Processor(lexer, parser);
    }
}
