/*
 * Copyright (c) 2023 KeksGauner.
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 *  associated documentation files (the "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 *  INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 *  PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 *  OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.datenente.cyberente.utils.config;

import java.io.FileNotFoundException;

/**
 * Interface representing a configuration handler.
 * Provides methods to load, reload, and save configuration data.
 */
public interface Config {

    /**
     * Loads the content of the configuration.
     * This method should be implemented to initialize or refresh the configuration data.
     */
    void loadContent();

    /**
     * Loads the configuration from a persistent storage.
     *
     * @throws FileNotFoundException if the configuration file is not found.
     */
    void load() throws FileNotFoundException;

    /**
     * Reloads the configuration, typically by re-reading the configuration file.
     */
    void reload();

    /**
     * Saves the current state of the configuration to a persistent storage.
     */
    void save();
}
