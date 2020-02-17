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

public class Options {
    public boolean gfm = true;
    public boolean pedantic = false;
    public boolean headerIds = true;

    public Options gfm(boolean gfm) {
        this.gfm = gfm;

        return this;
    }

    public Options pedantic(boolean pedantic) {
        this.pedantic = pedantic;

        return this;
    }

    public Options headerIds(boolean headerIds) {
        this.headerIds = headerIds;

        return this;
    }
}
