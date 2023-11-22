/*
 * The MIT License
 * Copyright © 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
module fr.djaytan.mc.jrppb.core {
  // Internal dependencies
  requires fr.djaytan.mc.jrppb.api;

  // General dependencies
  requires static org.jetbrains.annotations;
  requires com.google.guice;
  requires com.zaxxer.hikari;
  requires flyway.core;
  requires jakarta.inject;
  requires jakarta.validation;
  requires java.sql;
  requires org.apache.commons.lang3;
  requires org.apache.commons.io;
  requires org.slf4j;
  requires org.spongepowered.configurate;
  requires org.spongepowered.configurate.hocon;

  // Reflection for Guice
  opens fr.djaytan.mc.jrppb.core to
      com.google.guice;
  opens fr.djaytan.mc.jrppb.core.config to
      com.google.guice;
  opens fr.djaytan.mc.jrppb.core.config.serialization to
      com.google.guice;
  opens fr.djaytan.mc.jrppb.core.config.validation to
      com.google.guice;
  opens fr.djaytan.mc.jrppb.core.inject to
      com.google.guice;
  opens fr.djaytan.mc.jrppb.core.storage.sql to
      com.google.guice;
  opens fr.djaytan.mc.jrppb.core.storage.sql.access to
      com.google.guice;
  opens fr.djaytan.mc.jrppb.core.storage.sql.jdbc to
      com.google.guice;
  opens fr.djaytan.mc.jrppb.core.storage.sql.provider to
      com.google.guice;
  opens fr.djaytan.mc.jrppb.core.storage.sql.serializer to
      com.google.guice;

  // Reflection for Flyway
  opens db.migration.mysql;
  opens db.migration.sqlite;

  // Reflection for Configurate
  opens fr.djaytan.mc.jrppb.core.config.properties;
}
