package com.welltory.test

import android.app.Application
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.welltory.test.model.BalanceDay
import com.welltory.test.model.Result
import com.welltory.test.model.ViewState
import com.welltory.test.model.ViewState.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.format.DateTimeFormatter
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream

class MainViewModel(private val app: Application) : AndroidViewModel(app) {

    private val dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd")

    val viewState = MutableLiveData(
        ViewState(
            hintText = "Press the button to parse the CSV file",
            buttonText = "START",
            isButtonEnabled = true,
            isButtonVisible = true,
            resultState = ResultState.NoResult
        )
    )

    fun onStartWork() {
        viewState.value?.also { state ->
            when (state.resultState) {
                ResultState.NoResult, ResultState.Failure -> {
                    viewState.value = ViewState(
                        hintText = "Wait a little bit",
                        buttonText = "Parsing",
                        isButtonEnabled = false,
                        isButtonVisible = true,
                        resultState = ResultState.NoResult
                    )
                    viewModelScope.launch(Dispatchers.IO) {
                        val startTime = System.currentTimeMillis()
                        val result = readCsvFile(INPUT_FILE_NAME)
                        val workDuration = System.currentTimeMillis() - startTime
                        withContext(Dispatchers.Main) {
                            viewState.value = when (result) {
                                is Result.Success -> ViewState(
                                    hintText = "Success!\n\nThe whole process of:\nparsing input csv,\ncalculating balance day,\nwriting to output csv,\nsaving result to downloads folder,\ntook $workDuration milliseconds",
                                    buttonText = "",
                                    isButtonEnabled = false,
                                    isButtonVisible = false,
                                    resultState = ResultState.Success
                                )
                                is Result.Failure -> ViewState(
                                    hintText = "Oops! We have an error.\n${result.errorMessage}",
                                    buttonText = "Try Again?",
                                    isButtonEnabled = true,
                                    isButtonVisible = true,
                                    resultState = ResultState.Failure
                                )
                            }
                        }
                    }
                }
                else -> {
                    // do nothing
                }
            }
        }
    }

    private fun readCsvFile(fileName: String): Result<List<BalanceDay>> {
        return try {
            val inputStream = app.assets.open(fileName)
            val balanceDayList = csvReader().open(inputStream) {
                BalanceDayConverter.convert(readAllWithHeaderAsSequence())
            }

            val privateDir = app.filesDir
            val resultCsvFile = File(privateDir, OUTPUT_FILE_NAME)
            if (resultCsvFile.exists()) {
                resultCsvFile.delete()
            }
            resultCsvFile.createNewFile()
            csvWriter().open(resultCsvFile) {
                writeRow(listOf("day", "balance_points", "balance_day"))
                balanceDayList.forEach { balanceDay ->
                    writeRow(
                        listOf(
                            balanceDay.day.format(dateFormatter),
                            balanceDay.balancePoints,
                            balanceDay.balanceDay
                        )
                    )
                }
            }
            copyFileToDownloads(resultCsvFile)

            Result.Success(balanceDayList)
        } catch (e: Exception) {
            Result.Failure(e.message ?: "Something went wrong :(")
        }
    }

    private fun copyFileToDownloads(file: File): Uri? {
        val resolver = app.contentResolver
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                put(MediaStore.MediaColumns.SIZE, file.length())
            }
            resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        } else {
            val authority = "${app.packageName}.provider"
            val destinyFile = File(DOWNLOAD_DIR, file.name)
            FileProvider.getUriForFile(app, authority, destinyFile)
        }?.also { fileUri ->
            resolver.openOutputStream(fileUri).use { outputStream ->
                val brr = ByteArray(1024)
                var len: Int
                val bufferedInputStream = BufferedInputStream(FileInputStream(file.absoluteFile))
                while ((bufferedInputStream.read(brr, 0, brr.size).also { len = it }) != -1) {
                    outputStream?.write(brr, 0, len)
                }
                outputStream?.flush()
                bufferedInputStream.close()
            }
        }
    }

    companion object {
        private const val INPUT_FILE_NAME = "mobile_test_inputs.csv"
        private const val OUTPUT_FILE_NAME = "mobile_test_outputs.csv"

        private val DOWNLOAD_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    }
}
