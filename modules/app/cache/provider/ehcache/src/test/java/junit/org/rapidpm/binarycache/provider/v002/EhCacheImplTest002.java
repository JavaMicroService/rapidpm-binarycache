package junit.org.rapidpm.binarycache.provider.v002;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.rapidpm.binarycache.api.CacheByteArray;
import org.rapidpm.binarycache.api.CacheKey;
import org.rapidpm.binarycache.api.Result;
import org.rapidpm.binarycache.provider.ehcache.EhCacheImpl;
import org.rapidpm.ddi.DI;

import javax.cache.Cache;
import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Copyright (C) 2010 RapidPM
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
 * Created by RapidPM - Team on 09.03.2017.
 */
public class EhCacheImplTest002 {

  public static final String TEST_CACHE = "default";

  @Inject private EhCacheImpl cacheClient;

  private CacheByteArray cacheByteArray;

  @Before
  public void setUp() {
    DI.clearReflectionModel();
    DI.activatePackages("org.rapidpm");
    DI.activateDI(this);
    cacheByteArray = new CacheByteArray("123".getBytes());
  }

  @After
  public void tearDown() {
    cacheClient.clearCache(TEST_CACHE);
    DI.clearReflectionModel();
  }

  @Test
  public void test001() {
    final Cache<CacheKey, CacheByteArray> cache = cacheClient.getCache(TEST_CACHE);
    assertNotNull(cache);
  }

  @Test
  public void test002() {
    final Result result = cacheClient.cacheBinary(TEST_CACHE , new SimpleCacheKey() , cacheByteArray);
    assertEquals(Result.OK , result);
  }

  @Test
  public void test003() {
    final SimpleCacheKey cacheKey = new SimpleCacheKey();
    final Result result01 = cacheClient.cacheBinary(TEST_CACHE , cacheKey , cacheByteArray);
    assertEquals(Result.OK , result01);

    final Result result02 = cacheClient.cacheBinaryIfAbsent(TEST_CACHE , cacheKey , cacheByteArray);
    assertEquals(Result.FAILED , result02);
  }

  @Test
  public void test004_a() {
    final SimpleCacheKey cacheKey = new SimpleCacheKey();
    final Result resultPut = cacheClient.cacheBinary(TEST_CACHE , cacheKey , cacheByteArray);
    assertEquals(Result.OK , resultPut);

    final Optional<CacheByteArray> cachedElement = cacheClient.getCachedElement(TEST_CACHE , cacheKey);
    assertTrue(cachedElement.isPresent());

    final Optional<CacheByteArray> notCachedElement = cacheClient.getCachedElement(TEST_CACHE , new SimpleCacheKey());
    assertFalse(notCachedElement.isPresent());
  }

  @Test
  public void test004_b() {
    final SimpleCacheKey cacheKey = new SimpleCacheKey();
    final Result resultPut = cacheClient.cacheBinary(TEST_CACHE , cacheKey , cacheByteArray);
    assertEquals(Result.OK , resultPut);

    final Result resultRemove = cacheClient.removeEntry(TEST_CACHE , cacheKey);
    assertEquals(Result.OK , resultRemove);

    final Optional<CacheByteArray> cachedElement = cacheClient.getCachedElement(TEST_CACHE , cacheKey);
    assertFalse(cachedElement.isPresent());
  }

  @Test
  public void test005() {
    final Result resultRemove = cacheClient.removeEntry(TEST_CACHE , new SimpleCacheKey());
    assertEquals(Result.FAILED , resultRemove);
  }

  public static class SimpleCacheKey implements CacheKey {

    private String key;

    public SimpleCacheKey() {
      this.key = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      SimpleCacheKey that = (SimpleCacheKey) o;

      return key != null ? key.equals(that.key) : that.key == null;
    }

    @Override
    public int hashCode() {
      return key != null ? key.hashCode() : 0;
    }

    @Override
    public String keyAsString() {
      return this.key;
    }
  }
}

