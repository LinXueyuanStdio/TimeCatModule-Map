package com.timecat.module.map.view.source

import android.content.Context
import com.timecat.module.map.view.panel.gameBoundBox
import com.timecat.module.map.view.panel.worldBoundBox
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.ITileSource
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/16
 * @description null
 * @usage null
 */
class TileSourceManager {
    fun getAllSources(): List<MapTileSource> {
        return listOf(
            getGameSource(),
            MapTileSource(worldBoundBox(), TileSourceFactory.MAPNIK),
        )
    }

    fun getGameSource(): MapTileSource {
        val tileSource = GameTileSource(
            "原神", 6, 11, 256, ".jpg",
            arrayOf("https://gim.appsample.net/teyvat/v21/"),
            "© Genshin Impact contributors"
        )
        return MapTileSource(gameBoundBox(), tileSource)
    }

    fun getInitSource(): MapTileSource {
        return getGameSource()
    }
}

class MapTileSource(
    var box: BoundingBox,
    var tileSource: ITileSource,
    var uuid:String = UUID.randomUUID().toString(),
    var initZoomLevel: Int = 9,
    var initX: Double = 0.5,
    var initY: Double = 0.5,
)

fun ITileSource.toMapTileSource(): MapTileSource {
    return MapTileSource(worldBoundBox(), this)
}

fun ITileSource.toProvider(context: Context): MapTileProviderBasic {
    val provider = MapTileProviderBasic(context)
    provider.tileSource = this
    return provider
}
