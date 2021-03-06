package org.rapidpm.binarycache.api;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.Configuration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import java.util.Optional;

/**
 * Copyright (C) 2017 RapidPM - Sven Ruppert
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Created by Sven Ruppert - RapidPM - Team on 07.03.17.
 */
public interface BinaryCacheClient {

  /* make private with Java9 */
  default Cache<CacheKey, CacheByteArray> getCache(String cacheName) {
    final CachingProvider cachingProvider = Caching.getCachingProvider();
    final CacheManager cacheManager = cachingProvider.getCacheManager();
    return cacheManager.getCache(cacheName, CacheKey.class, CacheByteArray.class);
  }

  default Cache<CacheKey, CacheByteArray> createCache(String cacheName) {
    final CachingProvider cachingProvider = Caching.getCachingProvider();
    final CacheManager cacheManager = cachingProvider.getCacheManager();
    final MutableConfiguration<CacheKey, CacheByteArray> mutableConfiguration = new MutableConfiguration<CacheKey, CacheByteArray>()
        .setTypes(CacheKey.class, CacheByteArray.class);
    return cacheManager.createCache(cacheName, mutableConfiguration);
  }

  default Cache<CacheKey, CacheByteArray> createCache(String cacheName, Configuration<CacheKey, CacheByteArray> configuration) {
    final CachingProvider cachingProvider = Caching.getCachingProvider();
    final CacheManager cacheManager = cachingProvider.getCacheManager();
    return cacheManager.createCache(cacheName, configuration);
  }

  default Result cacheBinary(String cacheName, CacheKey cacheKey, CacheByteArray binary) {
    final Cache<CacheKey, CacheByteArray> cache = getCache(cacheName);
    cache.put(cacheKey, binary);
    return Result.OK;
  }

  default Result cacheBinaryIfAbsent(String cacheName, CacheKey cacheKey, CacheByteArray binary) {
    final Cache<CacheKey, CacheByteArray> cache = getCache(cacheName);
    final boolean ifAbsent = cache.putIfAbsent(cacheKey, binary);
    return ifAbsent ? Result.OK : Result.FAILED;
  }

  default Optional<CacheByteArray> getCachedElement(String cacheName, CacheKey cacheKey) {
    return Optional.ofNullable(getCache(cacheName).get(cacheKey));
  }

  default Result clearCache(String cacheName) {
    getCache(cacheName).clear();
    return Result.OK;
  }

  default Result removeEntry(String cacheName, CacheKey cacheKey) {
    final boolean remove = getCache(cacheName).remove(cacheKey);
    return remove ? Result.OK : Result.FAILED;
  }




}
