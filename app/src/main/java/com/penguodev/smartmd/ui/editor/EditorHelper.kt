package com.penguodev.smartmd.ui.editor

import com.penguodev.smartmd.repository.MDRepository
import com.squareup.moshi.JsonAdapter
import xute.markdeditor.datatype.DraftDataItemModel
import xute.markdeditor.models.DraftModel

//fun DraftModel.toJson(): String {
//    return StringBuilder().apply {
//        append("{")
//        append("\"draftId\":$draftId,")
//        append("\"draftTitle\":\"$draftTitle\",")
//        append("\"items\":[")
//        items.forEachIndexed { index, item ->
//            if (index != 0) append(",")
//            append(item.toJson())
//        }
//        append("]}")
//    }.toString()
//}
//
//fun DraftDataItemModel.toJson(): String {
//    return StringBuilder().apply {
//        append(
//            "{" +
//                    "\"itemType\":$itemType," +
//                    "\"mode\":$mode," +
//                    "\"style\":$style," +
//                    "\"content\":\"$content\"," +
//                    "\"downloadUrl\":\"$downloadUrl\"," +
//                    "\"caption\":\"$caption\"" +
//                    "}"
//        )
//    }.toString()
//}

fun DraftModel.toJson(): String {
    return MDRepository.moshi.adapter(DraftModel::class.java).toJson(this)
}

fun fromJsonToDraftModel(json: String): DraftModel? {
    return MDRepository.moshi.adapter(DraftModel::class.java).fromJson(json)
}

fun DraftDataItemModel.toJson(): String {
    return MDRepository.moshi.adapter(DraftDataItemModel::class.java).toJson(this)
}

fun fromJsonToDraftDataItemModel(json: String): DraftDataItemModel? {
    return MDRepository.moshi.adapter(DraftDataItemModel::class.java).fromJson(json)
}