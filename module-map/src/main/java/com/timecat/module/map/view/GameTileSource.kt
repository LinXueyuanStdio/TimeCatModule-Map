package com.timecat.module.map.view

import com.timecat.component.commonsdk.utils.override.LogUtil
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.TileSourcePolicy
import org.osmdroid.util.MapTileIndex

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/2
 * @description null
 * @usage null
 */
class GameTileSource : OnlineTileSourceBase {
    constructor(
        aName: String?, aZoomMinLevel: Int,
        aZoomMaxLevel: Int, aTileSizePixels: Int, aImageFilenameEnding: String?,
        aBaseUrl: Array<String?>?
    ) : super(
        aName, aZoomMinLevel, aZoomMaxLevel, aTileSizePixels,
        aImageFilenameEnding, aBaseUrl
    ) {
    }

    constructor(
        aName: String?, aZoomMinLevel: Int,
        aZoomMaxLevel: Int, aTileSizePixels: Int, aImageFilenameEnding: String?,
        aBaseUrl: Array<String?>?, copyright: String?
    ) : super(
        aName, aZoomMinLevel, aZoomMaxLevel, aTileSizePixels,
        aImageFilenameEnding, aBaseUrl, copyright
    ) {
    }

    /**
     * @param aName this is used for caching purposes, make sure it is consistent and unique
     * @since 6.1.0
     */
    constructor(
        aName: String?, aZoomMinLevel: Int,
        aZoomMaxLevel: Int, aTileSizePixels: Int, aImageFilenameEnding: String?,
        aBaseUrl: Array<String?>?, copyright: String?,
        pTileSourcePolicy: TileSourcePolicy?
    ) : super(
        aName, aZoomMinLevel, aZoomMaxLevel, aTileSizePixels,
        aImageFilenameEnding, aBaseUrl, copyright, pTileSourcePolicy
    ) {
    }

    override fun toString(): String {
        return name()
    }

    //https://gim.appsample.net/teyvat/v21/10/tile-2_-16.jpg
    override fun getTileURLString(pMapTileIndex: Long): String {
        LogUtil.se("$baseUrl${MapTileIndex.getZoom(pMapTileIndex)}/tile-${MapTileIndex.getX(pMapTileIndex)}_${MapTileIndex.getY(pMapTileIndex)}$mImageFilenameEnding")
        return (baseUrl + MapTileIndex.getZoom(pMapTileIndex) + "/tile-" + MapTileIndex.getX(pMapTileIndex) + "_" + MapTileIndex.getY(pMapTileIndex)
            + mImageFilenameEnding)
    }
}
