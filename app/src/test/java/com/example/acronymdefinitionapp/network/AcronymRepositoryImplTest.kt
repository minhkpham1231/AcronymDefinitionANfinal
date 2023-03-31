package com.example.acronymdefinitionapp.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.acronymdefinitionapp.model.Definition
import com.example.acronymdefinitionapp.utils.RequestState
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

@ExperimentalCoroutinesApi
class AcronymRepositoryImplTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val mockService = mockk<AcronymService>(relaxed = true)

    private lateinit var testObject: AcronymRepository

    @Before
    fun startup() {
        Dispatchers.setMain(testDispatcher)
        testObject = AcronymRepositoryImpl(mockService)
    }

    @After
    fun shutdown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `get acronym definitions when retrieving list of acronyms returns success`() {
        coEvery { mockService.getAcronymDefinition("HMM") } returns mockk {
            every { isSuccessful } returns true
            every { body() } returns listOf(mockk {
                every { lfs } returns listOf(mockk{
                    every { lf } returns "name"
                })
            })
        }
        val list = mutableListOf<RequestState>()
        val job = testScope.launch {
            testObject.getAcronymDefinition("HMM").collect {
                list.add(it)
            }
        }

        val result = list[1] as RequestState.SUCCESS<List<Definition>>

        Assert.assertEquals(2, list.size)
        Assert.assertEquals(1, result.definitions.first().lfs.size)
        Assert.assertEquals(1, result.definitions.size)
        Assert.assertEquals("name", result.definitions.first().lfs.first().lf)

        job.cancel()
    }

    @Test
    fun `get acronym definitions when retrieving list of acronyms returns error`() {
        coEvery { mockService.getAcronymDefinition("HMM") } returns mockk {
            every { isSuccessful } returns false
        }
        val list = mutableListOf<RequestState>()
        val job = testScope.launch {
            testObject.getAcronymDefinition("HMM").collect {
                list.add(it)
            }
        }

        val result = list[1] as RequestState.ERROR

        Assert.assertEquals(2, list.size)
        Assert.assertEquals("Acronym definition response is a failure", result.exception.localizedMessage)

        job.cancel()
    }

    @Test
    fun `get acronym definitions when retrieving list of acronyms returns error with null body`() {
        coEvery { mockService.getAcronymDefinition("HMM") } returns mockk {
            every { isSuccessful } returns true
            every { body() } returns null
        }
        val list = mutableListOf<RequestState>()
        val job = testScope.launch {
            testObject.getAcronymDefinition("HMM").collect {
                list.add(it)
            }
        }

        val result = list[1] as RequestState.ERROR

        Assert.assertEquals(2, list.size)
        Assert.assertEquals("Acronym response is null", result.exception.localizedMessage)

        job.cancel()
    }

    @Test
    fun `get acronym definitions when retrieving list of acronyms returns error with any exception`() {
        coEvery { mockService.getAcronymDefinition("HMM") } throws Exception("Error")
        val list = mutableListOf<RequestState>()
        val job = testScope.launch {
            testObject.getAcronymDefinition("HMM").collect {
                list.add(it)
            }
        }

        val result = list[1] as RequestState.ERROR

        Assert.assertEquals(2, list.size)
        Assert.assertEquals("Error", result.exception.localizedMessage)

        job.cancel()
    }

    @Test
    fun `get acronym definitions when retrieving list of acronyms returns loading`() {
        coEvery { mockService.getAcronymDefinition("HMM") } returns mockk {
            every { isSuccessful } returns true
            every { body() } returns listOf(mockk {
                every { lfs } returns listOf(mockk{
                    every { lf } returns "name"
                })
            })
        }
        val list = mutableListOf<RequestState>()
        val job = testScope.launch {
            testObject.getAcronymDefinition("HMM").collect {
                list.add(it)
            }
        }

        val result = list[0] as RequestState.LOADING

        Assert.assertEquals(2, list.size)
        Assert.assertTrue(result.loading)

        job.cancel()
    }
}