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

package com.watermark_apps.downmarker.language.token;

public class TextToken implements IToken {
    private final String value;

    public TextToken(String value) {
        this.value = value;
    }

    @Override
    public TokenType getType() {
        return TokenType.TEXT;
    }

    @Override
    public String getValue() {
        return value;
    }
}
