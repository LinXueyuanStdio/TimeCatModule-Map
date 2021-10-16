package com.timecat.module.map.fragment

import android.location.Location
import android.os.Build
import android.view.InputDevice
import android.view.MotionEvent
import android.view.View
import android.view.View.OnGenericMotionListener
import android.widget.*
import com.google.android.material.chip.Chip
import com.timecat.layout.ui.layout.*
import com.timecat.layout.ui.standard.textview.HintTextView
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.map.BuildConfig
import com.timecat.module.map.R
import com.timecat.module.map.mark.GameMarker
import com.timecat.module.map.mark.GameMarkerData
import com.timecat.module.map.view.*
import com.timecat.module.map.view.panel.*
import com.timecat.module.map.view.source.MapTileSource
import com.timecat.module.map.view.source.TileSourceManager
import com.timecat.module.map.view.source.toProvider
import com.timecat.module.map.view.zoom.SeekBarListener
import com.timecat.module.map.view.zoom.VerticalSeekBar
import com.timecat.page.base.base.simple.BaseSimpleSupportFragment
import com.xiaojinzi.component.anno.FragmentAnno
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.*
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme
import kotlin.math.roundToInt


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
    override fun bindView(view: View) {
        super.bindView(view)
//        MapView.setTileSystem(GameTileSystem())
        mMapView = view.findViewById(R.id.mapView)
        water = view.findViewById(R.id.water)
        zoom_slider = view.findViewById(R.id.zoom_slider)
        zoom_add = view.findViewById(R.id.zoom_add)
        seek_zoom = view.findViewById(R.id.seek_zoom)
        zoom_sub = view.findViewById(R.id.zoom_sub)
        map_icon = view.findViewById(R.id.map_icon)
        map_name = view.findViewById(R.id.map_name)
        close = view.findViewById(R.id.close)
        panel = view.findViewById(R.id.panel)
        l = SeekBarListener(seek_zoom, 50) { progress ->
            zoomTo(progress)
        }
    }

    lateinit var mMapView: MapView
    private lateinit var water: Chip
    private lateinit var zoom_slider: LinearLayout
    private lateinit var zoom_add: TextView
    private lateinit var seek_zoom: VerticalSeekBar
    private lateinit var zoom_sub: TextView
    private lateinit var map_icon: ImageView
    private lateinit var map_name: HintTextView
    private lateinit var close: ImageView
    private lateinit var panel: PanelView
    private lateinit var l: SeekBarListener

    fun switchMap(selected: Int = 0) {
        //TODO 初版不需要切换地图
//        _mActivity.showDialog {
//            title(text = "选择地图")
//            val data = listOf(
//                "主地图"
//            )
//            listItemsSingleChoice(items = data, initialSelection = selected) { _, idx, text ->
//
//            }
//        }
        panel.PanelSwitchMap(_mActivity, tileSourceManager) {
            setTileSource(it)
        }
    }

    fun closeMap() {

    }

    override fun lazyInit() {
        seek_zoom.setOnSeekBarChangeListener(l)
        zoom_add.setOnClickListener {
            seek_zoom.progress += 1
            zoomTo(seek_zoom.progress)
        }
        zoom_sub.setOnClickListener {
            seek_zoom.progress -= 1
            zoomTo(seek_zoom.progress)
        }
        map_icon.setShakelessClickListener {
            switchMap()
        }
        map_name.setShakelessClickListener {
            switchMap()
        }
        close.setShakelessClickListener {
            closeMap()
        }
        mMapView.addMapListener(object : MapListener {
            override fun onScroll(event: ScrollEvent?): Boolean {
                return false
            }

            override fun onZoom(event: ZoomEvent): Boolean {
                seek_zoom.progress = event.zoomLevel.roundToInt()
                return true
            }
        })
        mMapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
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
        loadMarkers()
        mMapView.setMultiTouchControls(true)
        mMapView.isTilesScaledToDpi = true
        mMapView.setUseDataConnection(true)
        mMapView.isHorizontalMapRepetitionEnabled = false
        mMapView.isVerticalMapRepetitionEnabled = false
        mMapView.setZoomRounding(true)
    }

    lateinit var gameMap: GameMap

    fun zoomTo(zoomLevel: Int) {
        mMapView.controller.zoomTo(zoomLevel, 500)
    }

    fun goTo(x: Double, y: Double) {
        mMapView.controller.animateTo(gameMap.pos(x, y))
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
    val tileSourceManager: TileSourceManager = TileSourceManager()

    val miniMapOverlay by lazy { MinimapOverlay(context, mMapView.getTileRequestCompleteHandler()) }
    val mRotationGestureOverlay by lazy {
        val overlay = RotationGestureOverlay(mMapView)
        overlay.setEnabled(false)
        overlay
    }

    protected fun addOverlays() {
        setTileSource(tileSourceManager.getInitSource())

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

        mMapView.overlays.add(mRotationGestureOverlay)
        mMapView.overlays.add(miniMapOverlay)

        val copyrightOverlay = CopyrightOverlay(activity)
        copyrightOverlay.setTextSize(10)
        mMapView.overlays.add(copyrightOverlay)
    }

    fun loadMarkers() {
        mMapView.overlays.add(GameMarker(GameMarkerData(IconLoader.randomAvatar(), "里约", "hhh"), panel))
        mMapView.overlays.add(GameMarker(GameMarkerData(IconLoader.randomAvatar(), "里约", "hhh"), panel))
        mMapView.overlays.add(GameMarker(GameMarkerData(IconLoader.randomAvatar(), "里约", "hhh"), panel))
        mMapView.overlays.add(GameMarker(GameMarkerData(IconLoader.randomAvatar(), "里约", "hhh"), panel))
        mMapView.overlays.add(GameMarker(GameMarkerData(IconLoader.randomAvatar(), "里约", "hhh"), panel))
        mMapView.overlays.add(GameMarker(GameMarkerData(IconLoader.randomAvatar(), "里约", "hhh"), panel))
    }

    fun setTileSource(source: MapTileSource) {
        val tileSource = source.tileSource
        mMapView.tileProvider = tileSource.toProvider(_mActivity)

        miniMapOverlay.setTileSource(tileSource)

        seek_zoom.max = tileSource.maximumZoomLevel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seek_zoom.min = tileSource.minimumZoomLevel
        }
        mMapView.minZoomLevel = tileSource.minimumZoomLevel.toDouble()
        mMapView.maxZoomLevel = tileSource.maximumZoomLevel.toDouble()

        val box = source.box
        gameMap = GameMap(box)
        mMapView.setScrollableAreaLimitDouble(box)

        mMapView.controller.animateTo(gameMap.pos(source.initX, source.initY))
        mMapView.controller.zoomTo(source.initZoomLevel, 500)
    }
}