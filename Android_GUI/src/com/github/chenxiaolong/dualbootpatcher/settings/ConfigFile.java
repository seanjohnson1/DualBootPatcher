/*
 * Copyright (C) 2014  Xiao-Long Chen <chenxiaolong@cxl.epac.to>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.chenxiaolong.dualbootpatcher.settings;

import com.github.chenxiaolong.dualbootpatcher.RomUtils;
import com.github.chenxiaolong.dualbootpatcher.RootFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public abstract class ConfigFile {
    private static final String CONF_VERSION = "jsonversion";

    private String mConfigFilename;
    private boolean mLoaded;
    private boolean mChanged;

    protected int mVersion;

    protected ConfigFile(String filename) {
        mConfigFilename = filename;
        load();
    }

    protected String getConfigFile() {
        // Avoid root for this (not using FileUtils.isExistsDirectory())
        File d = new File(RomUtils.RAW_DATA);

        if (d.exists() && d.isDirectory()) {
            return RomUtils.RAW_DATA + File.separator + mConfigFilename;
        } else {
            return RomUtils.DATA + File.separator + mConfigFilename;
        }
    }

    private void load() {
        // Only load once
        if (!mLoaded) {
            mLoaded = true;
            mVersion = 0;

            String contents = new RootFile(getConfigFile()).getContents();
            if (contents != null) {
                try {
                    JSONObject root = new JSONObject(contents);
                    mVersion = root.getInt(CONF_VERSION);

                    onLoadedVersion(mVersion, root);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected JSONObject getEmptyVersionedRoot(int version) {
        try {
            JSONObject root = new JSONObject();
            root.put(CONF_VERSION, 1);
            return root;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void setChanged(boolean changed) {
        mChanged = changed;
    }

    public final void save() {
        if (mChanged) {
            onSaveVersion(mVersion);

            mChanged = false;
        }
    }

    protected final void delete() {
        new RootFile(getConfigFile()).delete();
    }

    protected final void write(int version, String contents) {
        mVersion = version;
        new RootFile(getConfigFile()).writeContents(contents);
    }

    protected abstract void onLoadedVersion(int version, JSONObject root);

    protected abstract void onSaveVersion(int version);
}
