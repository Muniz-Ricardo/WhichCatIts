package com.ricardomuniz.whichcatits.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.ricardomuniz.whichcatits.data.model.Cat
import com.ricardomuniz.whichcatits.domain.model.State
import com.ricardomuniz.whichcatits.domain.usecase.GetCatListUseCase
import com.ricardomuniz.whichcatits.domain.usecase.GetCatMoreListUseCase
import com.ricardomuniz.whichcatits.util.GetStateConnection
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.times
import retrofit2.Response

@ExperimentalCoroutinesApi
class CatListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var stateObserver: Observer<State>

    @Mock
    private lateinit var getCatListUseCase: GetCatListUseCase

    @Mock
    private lateinit var getCatMoreListUseCase: GetCatMoreListUseCase

    @Mock
    private lateinit var getStateConnection: GetStateConnection

    private lateinit var catListViewModel: CatListViewModel

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        catListViewModel =
            CatListViewModel(getCatListUseCase, getCatMoreListUseCase, getStateConnection)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        catListViewModel.loadingState.removeObserver(stateObserver)
    }

    @Test
    fun `getCatListUseCase should return success state`() =
        testScope.runBlockingTest {
            val viewModel = CatListViewModel(
                getCatListUseCase,
                getCatMoreListUseCase,
                getStateConnection
            )

            `when`(getStateConnection.isInternetAvailable()).thenReturn(true)
            `when`(getCatListUseCase.invoke(anyInt(), anyInt())).thenReturn(
                Response.success(
                    arrayListOf()
                )
            )

            viewModel.getCatList(10, 1)

            assertEquals(
                State.Success(message = "Response successful to get data."),
                viewModel.loadingState.value
            )
        }

    @Test
    fun `test error message when no network available`() =
        testScope.runBlockingTest {

            whenever(getStateConnection.isInternetAvailable()).thenReturn(false)
            catListViewModel.loadingState.observeForever(stateObserver)

            catListViewModel.getCatList(10, 1)

            verify(
                stateObserver,
                times(1)
            ).onChanged(State.Error("Error to load! Connectivity problem."))

            catListViewModel.loadingState.removeObserver(stateObserver)
        }

    @Test
    fun `test loading more cat list with success`() = testScope.runBlockingTest {
        whenever(catListViewModel.isNetworkAvailable()).thenReturn(true)

        stateObserver = mock()
        catListViewModel.loadingStateEndless.observeForever(stateObserver)

        // Mock successful response from use case
        val mockCatList = arrayListOf(Cat("1", "url_01"), Cat("2", "url_02"))
        whenever(catListViewModel.catMoreListUseCase.invoke(anyInt(), anyInt())).thenReturn(
            Response.success(mockCatList)
        )

        catListViewModel.getMoreCatList(10, 2)

        verify(
            stateObserver,
            times(1)
        ).onChanged(State.Success(message = "Response successful to get more data."))

        catListViewModel.loadingStateEndless.removeObserver(stateObserver)
    }
}