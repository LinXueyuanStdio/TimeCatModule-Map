package com.timecat.module.map.view.source

import android.content.Context
import com.timecat.module.map.view.GameTileSource
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.ITileSource
import org.osmdroid.tileprovider.tilesource.TileSourceFactory

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/16
 * @description null
 * @usage null
 */
class TileSourceManager {
    fun getAllSources(): List<ITileSource> {
        val tileSource = GameTileSource(
            "原神", 6, 11, 256, ".jpg",
            arrayOf("https://gim.appsample.net/teyvat/v21/"),
            "© Genshin Impact contributors"
        )
        return listOf(
            tileSource,
            TileSourceFactory.MAPNIK,
        )
    }

    fun getInitSource(): ITileSource {
        return GameTileSource(
            "GenshinImpact", 6, 11, 256, ".jpg",
            arrayOf("https://gim.appsample.net/teyvat/v21/"),
            "© Genshin Impact contributors"
        )
    }
}

class MapTileSource(
    var tileSource: ITileSource,
)

fun ITileSource.toMapTileSource(): MapTileSource {
    return MapTileSource(this)
}

fun ITileSource.toProvider(context: Context): MapTileProviderBasic {
    val provider = MapTileProviderBasic(context)
    provider.tileSource = this
    return provider
}
