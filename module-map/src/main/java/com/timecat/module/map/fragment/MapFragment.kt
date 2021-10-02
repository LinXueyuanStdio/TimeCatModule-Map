package com.timecat.module.map.fragment

import android.graphics.Point
import android.location.Location
import android.view.InputDevice
import android.view.MotionEvent
import android.view.View
import android.view.View.OnGenericMotionListener
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.module.map.BuildConfig
import com.timecat.module.map.R
import com.timecat.module.map.view.GameTileSource
import com.timecat.module.map.view.UserLocationProvider
import com.timecat.page.base.base.simple.BaseSimpleSupportFragment
import com.xiaojinzi.component.anno.FragmentAnno
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.TileSystemWebMercator
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.*
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider


/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/1
 * @description null
 * @usage null
 */
@FragmentAnno("map/MapFragment")
class MapFragment : BaseSimpleSupportFragment() {
    override fun layout(): Int = R.layout.map_layout_main
    lateinit var mMapView: MapView
    override fun bindView(view: View) {
        super.bindView(view)
//        MapView.setTileSystem(GameTileSystem())
        mMapView = view.findViewById(R.id.mapView)
    }

    override fun lazyInit() {
        Configuration.getInstance().isDebugMapTileDownloader = true
        Configuration.getInstance().isDebugMapView = true
        Configuration.getInstance().isDebugTileProviders = true
        Configuration.getInstance().isDebugMode = true
        Configuration.getInstance().setUserAgentValue(BuildConfig.LIBRARY_PACKAGE_NAME)

//        val prov = MapTileAssetsProvider(SimpleRegisterReceiver(context), _mActivity.assets)
//        mMapView.tileProvider = MapTileProviderArray(TileSourceFactory.MAPNIK, SimpleRegisterReceiver(context), arrayOf<MapTileModuleProviderBase>(prov))
        mMapView.setOnGenericMotionListener(OnGenericMotionListener { v, event ->
            /**
             * mouse wheel zooming ftw
             * http://stackoverflow.com/questions/11024809/how-can-my-view-respond-to-a-mousewheel
             * @param v
             * @param event
             * @return
             */
            if (0 != event.source and InputDevice.SOURCE_CLASS_POINTER) {
                when (event.action) {
                    MotionEvent.ACTION_SCROLL -> {
                        if (event.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0.0f) {
                            mMapView.controller.zoomOut()
                        } else {
                            mMapView.controller.zoomIn()
                        }
                        return@OnGenericMotionListener true
                    }
                }
            }
            false
        })
        addOverlays()
        mMapView.setMultiTouchControls(true)
        mMapView.isTilesScaledToDpi = true
        mMapView.setUseDataConnection(true)
        mMapView.isHorizontalMapRepetitionEnabled = false
        mMapView.isVerticalMapRepetitionEnabled = false
//        mMapView.tilesScaleFactor = 0.5f
        mMapView.minZoomLevel = 6.0
        mMapView.maxZoomLevel = 11.0
        mMapView.setZoomRounding(true)
        val p = Point()
        MapView.getTileSystem().TileXYToPixelXY(4, 4, p)
        val geoPoint = GeoPoint(0.0, 0.0)
        MapView.getTileSystem().PixelXYToLatLong(p.x, p.y, 6.0, geoPoint)
        LogUtil.e(p)
        LogUtil.e(geoPoint)
        mMapView.setScrollableAreaLimitDouble(
            BoundingBox(
                TileSystemWebMercator.MaxLatitude,
                geoPoint.longitude,//TileSystemWebMercator.MinLongitude + geoPoint.longitude,
                geoPoint.latitude,//TileSystemWebMercator.MaxLatitude - geoPoint.latitude,
                TileSystemWebMercator.MinLongitude
            )
        )
    }

    override fun onResume() {
        super.onResume()
//        Configuration.getInstance().load(_mActivity.applicationContext, DEF.block())
        mMapView.onResume()
    }

    //    https://gim.appsample.net/teyvat/v21/10/tile-8_-14.jpg
    override fun onPause() {
        super.onPause()
//        Configuration.getInstance().save(_mActivity.applicationContext, DEF.block())
        mMapView.onPause()
        mGpsMyLocationProvider?.stopLocationProvider()
    }

    var mGpsMyLocationProvider: GpsMyLocationProvider? = null
    var mMyLocationOverlay: ItemizedOverlayWithFocus<OverlayItem>? = null
    protected fun addOverlays() {
        val provider = MapTileProviderBasic(_mActivity)
        val tileSource = GameTileSource(
            "GenshinImpact", 6, 11, 256, ".jpg",
            arrayOf("https://gim.appsample.net/teyvat/v21/"),
            "© Genshin Impact contributors"
        )
        provider.tileSource = tileSource
        mMapView.tileProvider = provider
        val userLocationProvider = UserLocationProvider()
        userLocationProvider.startLocationProvider(object : IMyLocationConsumer {
            override fun onLocationChanged(location: Location, source: IMyLocationProvider) {
                userLocationProvider.stopLocationProvider()
                if (mMyLocationOverlay == null) {
                    val items = ArrayList<OverlayItem>()
                    items.add(OverlayItem("Me", "My Location", GeoPoint(location)));

                    mMyLocationOverlay = ItemizedOverlayWithFocus(items, object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
                        override fun onItemSingleTapUp(index: Int, item: OverlayItem): Boolean {
                            val mapController = mMapView.controller
                            mapController.setCenter(item.point)
                            mapController.zoomTo(mMapView.maxZoomLevel)
                            return true
                        }

                        override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean {
                            return false
                        }
                    }, context).apply {
                        setFocusItemsOnTap(true)
                        setFocusedItem(0)
                    }
                    mMapView.overlays.add(mMyLocationOverlay)

                    mMapView.controller.setZoom(10)
                    val geoPoint = mMyLocationOverlay?.getFocusedItem()?.point
                    mMapView.controller.animateTo(geoPoint)
                }
            }
        })

        // Add tiles layer
//        val provider = MapTileProviderBasic(_mActivity)
//        provider.tileSource = XYTileSource(
//            "GenshinImpact", 0, 10, 256, ".jpg",
//            arrayOf("https://gim.appsample.net/teyvat/v21/"),
//            "© GenshinImpact contributors"
//        )
//        val tilesOverlay = TilesOverlay(provider, _mActivity)
//        tilesOverlay.loadingBackgroundColor = Color.TRANSPARENT
//        mMapView.overlays.add(tilesOverlay)

        val mRotationGestureOverlay = RotationGestureOverlay(mMapView)
        mRotationGestureOverlay.setEnabled(false)
        mMapView.overlays.add(mRotationGestureOverlay)

        val miniMapOverlay = MinimapOverlay(context, mMapView.getTileRequestCompleteHandler())
        miniMapOverlay.setTileSource(tileSource)
        mMapView.overlays.add(miniMapOverlay)

        val copyrightOverlay = CopyrightOverlay(activity)
        copyrightOverlay.setTextSize(10)
        mMapView.overlays?.add(copyrightOverlay)
    }

}