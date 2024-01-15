@file:OptIn(ExperimentalCoroutinesApi::class)

package com.ricardomuniz.whichcatits.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import com.ricardomuniz.whichcatits.data.model.Cat
import com.ricardomuniz.whichcatits.domain.model.State
import com.ricardomuniz.whichcatits.domain.usecase.GetCatDetailUseCase
import com.ricardomuniz.whichcatits.domain.usecase.ShareImageUseCase
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
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class CatDetailViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var catDetailUseCase: GetCatDetailUseCase

    @Mock
    private lateinit var shareImageUseCase: ShareImageUseCase

    @Mock
    private lateinit var getStateConnection: GetStateConnection

    private lateinit var catDetailViewModel: CatDetailViewModel

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        catDetailViewModel =
            CatDetailViewModel(catDetailUseCase, shareImageUseCase, getStateConnection)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        Mockito.framework().clearInlineMock(mock())
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `test success loading cat details`() = testScope.runBlockingTest {
        val mockCat = Cat(id = "1", url = "https://example.com/cat.jpg")

        whenever(getStateConnection.isInternetAvailable()).thenReturn(true)
        whenever(catDetailUseCase.invoke(anyString())).thenReturn(Response.success(mockCat))

        catDetailViewModel.getCatById("1")

        assertEquals(State.Success::class.java, catDetailViewModel.loadingState.value!!::class.java)

        assertEquals(mockCat, catDetailViewModel.catData.value)
    }
}