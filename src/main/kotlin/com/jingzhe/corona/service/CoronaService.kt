package com.jingzhe.corona.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.jingzhe.corona.coronaDataUrl
import com.jingzhe.corona.dbName
import com.jingzhe.corona.model.DistrictData
import com.jingzhe.corona.model.Snapshot
import com.jingzhe.corona.utils.CoronaUtils
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.bson.Document
import org.litote.kmongo.coroutine.CoroutineClient


class CoronaService(private val counterService: CounterService,
                    private val dbClient: CoroutineClient,
                    private val httpClient: HttpClient) {

    private val gson: Gson = GsonBuilder().create()
    suspend fun getSnapshotData(): Map<String, Int> = coroutineScope {
        launch { counterService.updateCounter() }
        val todayDate = CoronaUtils.getTodayDate()
        val snap = async {
            dbClient.getDatabase(dbName)
                .getCollection<Snapshot>("snapshot")
                .findOne(Document("_id", todayDate))
                ?: doFetchSnapshot(todayDate)
        }
        snap.await().data
    }

    suspend fun getOldSnapshotData(date: String): Map<String, DistrictData> = coroutineScope {
        val nextDate = CoronaUtils.getNextDate(date)
        val snapCollection = dbClient.getDatabase(dbName)
            .getCollection<Snapshot>("snapshot")
        val nextSnap = async {
            snapCollection
                .findOne(Document("_id", nextDate))
                ?: throw Exception("Wrong next date")
        }
        val prevSnap = async {
            snapCollection
                .findOne(Document("_id", date))
                ?: throw Exception("Wrong date")
        }

        calculateDistrictData(nextSnap.await(), prevSnap.await())
    }

    suspend fun doFetchSnapshot(todayDate: String): Snapshot {
        val httpResponse = httpClient.get<String> {
            url(coronaDataUrl)
        }

        println("fetch data from THL")
        return Snapshot(todayDate, getDistrictData(httpResponse))
    }

    private fun getDistrictData(data: String): Map<String, Int> {
        val updatedData = replaceDistrict(data)
        val type = object : TypeToken<Map<String, String>>() {}.type
        val map = gson.fromJson<Map<String, String>>(updatedData, type)
        return map.filterKeys { !it.matches(Regex("^[0-9]+$")) }
            .mapValues { Integer.valueOf(it.value) }
    }

    private fun replaceDistrict(data: String): String {
        val start = data.indexOf("\"value\": {")
        val end = data.indexOf("}", start)
        var updatedData = data.substring(start + 9, end + 1)
        updatedData = updatedData.replace("\"1801\": ", "\"Pohjois-Pohjanmaa\": ")
            .replace("\"2225\": ", "\"HUS\": ")
            .replace("\"2119\": ", "\"Lappi\": ")
            .replace("\"423\": ", "\"Kanta-Häme\": ")
            .replace("\"529\": ", "\"Pirkanmaa\": ")
            .replace("\"847\": ", "\"Etelä-Karjala\": ")
            .replace("\"1271\": ", "\"Pohjois-Savo\": ")
            .replace("\"1377\": ", "\"Keski-Suomi\": ")
            .replace("\"211\": ", "\"Varsinais-Suomi\": ")
            .replace("\"1165\": ", "\"Pohjois-Karjala\": ")
            .replace("\"1483\": ", "\"Etelä-Pohjanmaa\": ")
            .replace("\"317\": ", "\"Satakunta\": ")
            .replace("\"1589\": ", "\"Vaasa\": ")
            .replace("\"1695\": ", "\"Keski-Pohjanmaa\": ")
            .replace("\"635\": ", "\"Päijät-Häme\": ")
            .replace("\"953\": ", "\"Etelä-Savo\": ")
            .replace("\"741\": ", "\"Kymenlaakso\": ")
            .replace("\"2013\": ", "\"Länsi-Pohja\": ")
            .replace("\"105\": ", "\"Ahvenanmaa\": ")
            .replace("\"119\": ", "\"Itä-Savo\": ")
            .replace("\"1907\": ", "\"Kainuu\": ")
            .replace("\"2331\": ", "\"All\": ")
        return updatedData
    }

    private fun calculateDistrictData(snapshot: Snapshot, previousSnapshot: Snapshot): Map<String, DistrictData> {
        return snapshot.data.mapValues { DistrictData(snapshot.data[it.key]!!, snapshot.data[it.key]!! - previousSnapshot.data[it.key]!!) }
    }
}