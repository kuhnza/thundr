package com.threewks.thundr.persist;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService.SetPolicy;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class DatastoreCounterRepositoryTest {
	private DatastoreCounterRepository counterRepository;
	private DatastoreService datastoreService;
	private MemcacheService cacheService;

	@Before
	public void before() throws EntityNotFoundException {
		datastoreService = mock(DatastoreService.class);
		cacheService = mock(MemcacheService.class);
		counterRepository = new DatastoreCounterRepository(cacheService, datastoreService);
		counterRepository = spy(counterRepository);
		doReturn(11L).when(counterRepository).incrementCount(anyString(), anyString(), anyLong());
		doReturn(43L).when(counterRepository).sumFromQuery(anyString(), anyString());
	}

	@Test
	public void shouldGetCountAndStoreInCache() {
		assertThat(counterRepository.getCount("CounterKey"), is(43L));
		verify(cacheService).put("CounterKeyCount", 43L, Expiration.byDeltaSeconds(60), SetPolicy.ADD_ONLY_IF_NOT_PRESENT);
	}

	@Test
	public void shouldReturnCachedValueOnGetIfValueInCache() {
		when(cacheService.get("CounterKeyCount")).thenReturn(51L);
		assertThat(counterRepository.getCount("CounterKey"), is(51L));
		verify(cacheService, times(0)).put(anyString(), anyLong(), Mockito.any(Expiration.class), Mockito.any(SetPolicy.class));
	}

	@Test
	public void shouldIncrementGivenCounterBy1() {
		assertThat(counterRepository.incrementCount("CounterKey"), is(11L));
		verify(counterRepository).incrementCount("CounterKey", null, 1L);
	}

	@Test
	public void shouldIncrementGivenCounterWithAssocationBy1() {
		assertThat(counterRepository.incrementCount("CounterKey", "123"), is(11L));
		verify(counterRepository).incrementCount("CounterKey", "123", 1L);
	}

	@Test
	public void shouldIncrementCounterByGivenAmount() {
		counterRepository.incrementCountBy("CounterKey", null, 25L);
		verify(counterRepository).incrementCount("CounterKey", null, 25L);
	}

	@Test
	public void shouldDecrementGivenCounterBy1() {
		assertThat(counterRepository.decrementCount("CounterKey"), is(11L));
		verify(counterRepository).incrementCount("CounterKey", null, -1L);
	}

	@Test
	public void shouldDecrementGivenCounterWithAssocationBy1() {
		assertThat(counterRepository.decrementCount("CounterKey", "!23"), is(11L));
		verify(counterRepository).incrementCount("CounterKey", "!23", -1L);
	}

	@Test
	public void shouldDecrementCounterByGivenAmount() {
		counterRepository.decrementCountBy("CounterKey", null, 25L);
		verify(counterRepository).incrementCount("CounterKey", null, -25L);
	}

	@Test
	public void shouldCreateMemcacheKey() {
		assertThat(counterRepository.memcacheKey("CounterKey", null), is("CounterKeyCount"));
		assertThat(counterRepository.memcacheKey("CounterKey", "123"), is("CounterKeyCount_123"));
	}

	@Test
	public void shouldCreateShardKey() {
		assertThat(counterRepository.shardCountMemcacheKey("CounterKey", null), is("CounterKeyCountShards"));
		assertThat(counterRepository.shardCountMemcacheKey("CounterKey", "123"), is("CounterKeyCountShards_123"));
	}

	@Test
	public void shouldReturnShardCountFromCacheIfPresent() {
		when(counterRepository.readCache("CounterKeyCountShards_123")).thenReturn(1234L);
		long shardCount = counterRepository.shardCount("CounterKey", "123");
		assertThat(shardCount, is(1234L));
	}

	@Test
	public void shouldSetCounterByDeletingAllShardsAndCreatingANewOne() {
		LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(), new LocalMemcacheServiceTestConfig());
		helper.setUp();
		datastoreService = DatastoreServiceFactory.getDatastoreService();
		cacheService = MemcacheServiceFactory.getMemcacheService();
		counterRepository = new DatastoreCounterRepository(cacheService, datastoreService);
		counterRepository.incrementCountBy("Counter", null, 10);
		counterRepository.incrementCountBy("Counter", null, 10);
		counterRepository.incrementCountBy("Counter", null, 8);
		assertThat(counterRepository.getCount("Counter", null), is(28L));
		counterRepository.setCount("Counter", null, 2);
		assertThat(counterRepository.getCount("Counter", null), is(2L));

	}
}
