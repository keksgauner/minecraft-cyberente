/*
 * MIT License
 *
 * Copyright (c) 2025 KeksGauner, CyberEnte
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package de.datenente.cyberente.utils.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;

/**
 * Abstract class representing a JSON document configuration.
 *
 * @param <T> The type of the storage object.
 */
@Getter
public abstract class JsonDocument<T> implements Config {

    /** The path delimiter. */
    static final String PATH_DELIMITER = "/";

    /** The logger to use for logging. */
    final Logger pluginLogger;
    /** The folder where the data is stored. */
    final Path dataDirectory;
    /** The class type of the storage object. */
    final Class<T> classOfT;

    /** The name of the configuration file. */
    final String name;
    /** The folder where the configuration file is located. */
    final String folder;

    /** The configuration file. */
    final File file;
    /** The Gson object. */
    final Gson gson =
            new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    /** The storage object. */
    protected T storage;

    /**
     * Constructor for JsonDocument with a specified folder.
     *
     * @param pluginLogger The logger to use for logging.
     * @param dataDirectory The folder where the data is stored.
     * @param classOfT The class type of the storage object.
     * @param name The name of the configuration file.
     * @param folder The folder where the configuration file is located.
     */
    protected JsonDocument(
            final Logger pluginLogger,
            final Path dataDirectory,
            final Class<T> classOfT,
            final String name,
            final String folder) {
        this.pluginLogger = pluginLogger;
        this.dataDirectory = dataDirectory;
        this.classOfT = classOfT;

        this.name = name;
        this.folder = folder + PATH_DELIMITER;

        final String path = this.getDataDirectory() + PATH_DELIMITER + this.getFolder();
        this.file = new File(path, this.getName());

        this.preLoad();
    }

    /**
     * Constructor for JsonDocument without a specified folder.
     *
     * @param pluginLogger The logger to use for logging.
     * @param dataDirectory The folder where the data is stored.
     * @param classOfT The class type of the storage object.
     * @param name The name of the configuration file.
     */
    protected JsonDocument(
            final Logger pluginLogger, final Path dataDirectory, final Class<T> classOfT, final String name) {
        this.pluginLogger = pluginLogger;
        this.dataDirectory = dataDirectory;
        this.classOfT = classOfT;

        this.name = name;
        this.folder = "";

        this.file = new File(this.getDataDirectory().toFile(), this.getName());

        this.preLoad();
    }

    /**
     * Pre-loads the configuration file. If the file does not exist, it creates a new instance of the storage.
     *
     * @throws IllegalStateException if the folder for the config cannot be created.
     */
    void preLoad() throws IllegalStateException {
        if (!this.getFile().getParentFile().exists()
                && (!this.getFile().getParentFile().mkdirs()))
            throw new IllegalStateException("Could not create folder for config");

        try {
            this.load();
        } catch (FileNotFoundException ex) {
            this.getPluginLogger().log(Level.SEVERE, "Could not load configuration file: ", ex);
        }

        this.loadContent();
    }

    /**
     * Loads the configuration file.
     *
     * @throws FileNotFoundException if the configuration file cannot be found.
     */
    @Override
    public void load() throws FileNotFoundException {
        if (this.getFile().exists()) {
            try (final FileReader fileReader = new FileReader(this.getFile())) {
                this.storage = this.getGson().fromJson(fileReader, this.getClassOfT());
            } catch (IOException ex) {
                this.getPluginLogger().log(Level.SEVERE, "Could not load configuration file: ", ex);
            }
            return;
        }

        try {
            this.storage = this.getClassOfT().getDeclaredConstructor().newInstance();
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException ex) {
            this.getPluginLogger().log(Level.SEVERE, "Could not create new instance of storage: ", ex);
        }
        // Save the newly created instance to create the file
        this.save();
    }

    /**
     * Reloads the configuration file.
     */
    @Override
    public void reload() {
        this.storage = null;
        try {
            this.load();
        } catch (FileNotFoundException ex) {
            // NOT MY EX ^^
            this.getPluginLogger().log(Level.SEVERE, "Could not load configuration file: ", ex);
        }
    }

    /**
     * Saves the configuration file.
     */
    @Override
    public void save() {
        try (final FileWriter fileWriter = new FileWriter(this.getFile(), StandardCharsets.UTF_8, false)) {
            this.getGson().toJson(this.getStorage(), fileWriter);
        } catch (IOException ex) {
            this.getPluginLogger().log(Level.SEVERE, "Could not save configuration file: ", ex);
        }
    }

    /**
     * Deletes the configuration file.
     */
    @Override
    public boolean delete() {
        if (this.getFile().exists()) {
            return this.getFile().delete();
        }
        return false;
    }
}
