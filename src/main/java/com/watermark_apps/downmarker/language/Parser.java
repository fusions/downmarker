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

package com.watermark_apps.downmarker.language;

import com.watermark_apps.downmarker.language.token.IToken;

import java.util.Stack;

public class Parser {
    private InlineCompiler inlineCompiler;
    private IRenderer renderer;

    public Parser(IRenderer renderer) {
        this.renderer = renderer;
    }

    public void setInlineCompiler(InlineCompiler inlineCompiler) {
        this.inlineCompiler = inlineCompiler;
    }

    public String parse(Stack<IToken> tokens) {
        StringBuilder output = new StringBuilder();
        tokens.forEach(token -> {
            output.append(parseToken(token));
        });

        return output.toString();
    }

    public IRenderer getRenderer() {
        return renderer;
    }

    private String parseToken(IToken token) {
        switch (token.getType()) {
            case SPACE:
                return "";
            case PARAGRAPH:
                return renderer.renderParagraph(inlineCompiler.compile(token.getValue()));
            default:
                return "";
        }
    }
}
