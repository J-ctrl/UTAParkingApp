package com.example.myapplication2.ui.login;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication2.R;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.OnMapRenderListener;
import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.ViewObject;
import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.guidance.TrafficUpdater;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapGesture;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.mapping.MapState;
import com.here.android.mpa.routing.CoreRouter;
import com.here.android.mpa.routing.DynamicPenalty;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;
import com.here.android.mpa.routing.RouteTta;
import com.here.android.mpa.routing.RouteWaypoint;
import com.here.android.mpa.routing.Router;
import com.here.android.mpa.routing.RoutingError;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapBasicActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE
    };
     public static int position;
    // map embedded in the map fragment
    public static Map map;
    private   AppCompatActivity m_activity = this;
    private  Button m_naviControlButton;
    private PointF m_mapTransformCenter;
    private boolean m_returningToRoadViewMode = false;
    private TrafficUpdater.RequestInfo m_requestInfo;
    // map fragment embedded in this activity
    private AndroidXMapFragment mapFragment;
    private NavigationManager m_navigationManager;
    private GeoBoundingBox m_geoBoundingBox;
    private Route m_route;
    private MapMarker m_positionIndicatorFixed = null;
    private boolean m_foregroundServiceStarted;
    private Button m_calculateRouteBtn;
static GeoCoordinate[] coordinates;

 static void post(int position2){position=position2;}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermissions();
}
    void initialize() {
        setContentView(R.layout.activity_mapbasic);
        // Search for the map fragment to finish setup by calling init().
        mapFragment = (AndroidXMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapfragment);
                // Set up disk cache path for the map service for this application
                // It is recommended to use a path under your application folder for storing the disk cache

        boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(
                getApplicationContext().getExternalFilesDir(null) + File.separator + ".here-maps",
                "android.intent.action.MAIN"); /* ATTENTION! Do not forget to update {YOUR_INTENT_NAME} */
        if (!success) {
            Toast.makeText(getApplicationContext(), "Unable to set isolated disk cache path.",
                    Toast.LENGTH_LONG).show();
        } else {
            mapFragment.init(new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                    if (error == OnEngineInitListener.Error.NONE) {
                        mapFragment.getMapGesture().addOnGestureListener(gestureListener, 100, true);

                        // retrieve a reference of the map from the map fragment
                        map = mapFragment.getMap();
                        // Set the map center to the Vancouver region (no animation)
                        map.setCenter(new GeoCoordinate(32.7300973, -97.1143442, 0.0),
                                Map.Animation.NONE);
                        // Set the zoom level to the average between min and max
                        map.setZoomLevel(13.2);
                        m_navigationManager = NavigationManager.getInstance();

                    } else {
                        new AlertDialog.Builder(m_activity).setMessage(
                                "Error : " + error.name() + "\n\n" + error.getDetails())
                                .setTitle(R.string.engine_init_error)
                                .setNegativeButton(android.R.string.cancel,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                m_activity.finish();
                                            }
                                        }).create().show();
                    }

                }
            });
            mapFragment.addOnMapRenderListener(new OnMapRenderListener() {
                @Override
                public void onPreDraw() {
                    if (m_positionIndicatorFixed != null) {
                        if (NavigationManager.getInstance()
                                .getMapUpdateMode().equals(NavigationManager.MapUpdateMode.ROADVIEW)) {
                            if (!m_returningToRoadViewMode) {
                                // when road view is active, we set the position indicator to align
                                // with the current map transform center to synchronize map and map
                                // marker movements.
                                m_positionIndicatorFixed.setCoordinate(map.pixelToGeo(m_mapTransformCenter));
                            }
                        }
                    }
                }

                @Override
                public void onPostDraw(boolean var1, long var2) {
                }

                @Override
                public void onSizeChanged(int var1, int var2) {
                }

                @Override
                public void onGraphicsDetached() {
                }

                @Override
                public void onRenderBufferCreated() {
                }
            });


        }
        coordinates = new GeoCoordinate[]{new GeoCoordinate(32.7343284, -97.1131157),
                new GeoCoordinate(32.732370, -97.114987),
                new GeoCoordinate(32.727639, -97.111675),
                new GeoCoordinate(32.728551, -97.107697),
                new GeoCoordinate(32.731257, -97.119128),
                new GeoCoordinate(32.726451, -97.112895),
                new GeoCoordinate(32.724613, -97.112537)
        };
    }
    private void calculateTtaUsingDownloadedTraffic(){
        /* Turn on traffic updates */
        TrafficUpdater.getInstance().enableUpdate(true);

        m_requestInfo = TrafficUpdater.getInstance().request(
                m_route, new TrafficUpdater.Listener() {
                    @Override
                    public void onStatusChanged(TrafficUpdater.RequestState requestState) {
                        final RouteTta ttaDownloaded = m_route.getTtaUsingDownloadedTraffic(
                                Route.WHOLE_ROUTE);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final TextView tvDownload = findViewById(R.id.tvTtaInclude);

                                if (tvDownload != null) {
                                    tvDownload.setText("Tta downloaded: " +
                                            String.valueOf(ttaDownloaded.getDuration()));
                                }
                            }
                        });
                    }
                });
    }
    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[0]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            for (int index = permissions.length - 1; index >= 0; --index) {
                if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                    // exit the app if one permission is not granted
                    Toast.makeText(this, "Required permission '" + permissions[index]
                            + "' not granted, exiting", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            }
            // all permissions were granted
            initialize();
            initNaviControlButton();

        }
    }
    private void calculateTta() {
        /*
         * Receive arrival time for the whole m_route, if you want to get time only for part of
         * m_route pass parameter in bounds 0 <= m_route.getSublegCount()
         */
        final RouteTta ttaExcluding = m_route.getTtaExcludingTraffic(Route.WHOLE_ROUTE);
        final RouteTta ttaIncluding = m_route.getTtaIncludingTraffic(Route.WHOLE_ROUTE);

        final TextView tvInclude = findViewById(R.id.tvTtaInclude);
        tvInclude.setText(String.valueOf(ttaIncluding.getDuration()));

    }


    private void createRoute() {
        /* Initialize a CoreRouter */
        CoreRouter coreRouter = new CoreRouter();
        DynamicPenalty dynamicPenalty = new DynamicPenalty();
        dynamicPenalty.setTrafficPenaltyMode(Route.TrafficPenaltyMode.OPTIMAL);
        coreRouter.setDynamicPenalty(dynamicPenalty);
        /* Initialize a RoutePlan */
        RoutePlan routePlan = new RoutePlan();

        /*
         * Initialize a RouteOption. HERE Mobile SDK allow users to define their own parameters for the
         * route calculation,including transport modes,route types and route restrictions etc.Please
         * refer to API doc for full list of APIs
         */
        RouteOptions routeOptions = new RouteOptions();
        /* Other transport modes are also available e.g Pedestrian */
        routeOptions.setTransportMode(RouteOptions.TransportMode.CAR);
        /* Disable highway in this route. */
        routeOptions.setHighwaysAllowed(false);
        /* Calculate the shortest route available. */
        routeOptions.setRouteType(RouteOptions.Type.FASTEST);
        /* Calculate 1 route. */
        routeOptions.setRouteCount(1);
        /* Finally set the route option */
        routePlan.setRouteOptions(routeOptions);

        /* Define waypoints for the route */
        /* START: 4350 Still Creek Dr */
        RouteWaypoint startPoint = new RouteWaypoint(coordinates[position]);
        /* END: Langley BC */
        RouteWaypoint destination = new RouteWaypoint(coordinates[position+1]);
        map.addTransformListener(onTransformListener);

        PositioningManager.getInstance().start(PositioningManager.LocationMethod.GPS_NETWORK);
        /* Add both waypoints to the route plan */
        routePlan.addWaypoint(startPoint);
        routePlan.addWaypoint(destination);

        /* Trigger the route calculation,results will be called back via the listener */
        coreRouter.calculateRoute(routePlan,
                new Router.Listener<List<RouteResult>, RoutingError>() {

                    @Override
                    public void onProgress(int i) {
                        /* The calculation progress can be retrieved in this callback. */
                    }

                    @Override
                    public void onCalculateRouteFinished(List<RouteResult> routeResults,
                                                         RoutingError routingError) {
                        /* Calculation is done.Let's handle the result */
                        if (routingError == RoutingError.NONE) {
                            if (routeResults.get(0).getRoute() != null) {

                                m_route = routeResults.get(0).getRoute();
                                /* Create a MapRoute so that it can be placed on the map */

                                MapRoute mapRoute = new MapRoute(routeResults.get(0).getRoute());

                                /* Show the maneuver number on top of the route */
                                mapRoute.setManeuverNumberVisible(true);

                                /* Add the MapRoute to the map */
                                map.addMapObject(mapRoute);

                                /*
                                 * We may also want to make sure the map view is orientated properly
                                 * so the entire route can be easily seen.
                                 */
                                m_geoBoundingBox = routeResults.get(0).getRoute().getBoundingBox();
                                map.zoomTo(m_geoBoundingBox, Map.Animation.NONE,
                                        Map.MOVE_PRESERVE_ORIENTATION);


                                startNavigation();
                            } else {
                                Toast.makeText(m_activity,
                                        "Error:route results returned is not valid",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(m_activity,
                                    "Error:route calculation returned error code: " + routingError,
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void initNaviControlButton() {
        m_naviControlButton = findViewById(R.id.btnCalculateRoute);
        m_naviControlButton.setText(R.string.start_navi);
        m_naviControlButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                /*
                 * To start a turn-by-turn navigation, a concrete route object is required.We use
                 * the same steps from Routing sample app to create a route from 4350 Still Creek Dr
                 * to Langley BC without going on HWY.
                 *
                 * The route calculation requires local map data.Unless there is pre-downloaded map
                 * data on device by utilizing MapLoader APIs,it's not recommended to trigger the
                 * route calculation immediately after the MapEngine is initialized.The
                 * INSUFFICIENT_MAP_DATA error code may be returned by CoreRouter in this case.
                 *
                 */
                if (m_route == null) {
                    createRoute();
                } else {
                    m_navigationManager.stop();
                    /*
                     * Restore the map orientation to show entire route on screen
                     */

                    map.zoomTo(m_geoBoundingBox, Map.Animation.NONE, 0f);
                    m_naviControlButton.setText(R.string.start_navi);
                    m_route = null;
                }
            }
        });
    }

    /*
     * Android 8.0 (API level 26) limits how frequently background apps can retrieve the user's
     * current location. Apps can receive location updates only a few times each hour.
     * See href="https://developer.android.com/about/versions/oreo/background-location-limits.html
     * In order to retrieve location updates more frequently start a foreground service.
     * See https://developer.android.com/guide/components/services.html#Foreground
     */
    private void startForegroundService() {
        if (!m_foregroundServiceStarted) {
            m_foregroundServiceStarted = true;
            Intent startIntent = new Intent(m_activity, ForegroundService.class);
            startIntent.setAction(ForegroundService.START_ACTION);
            m_activity.getApplicationContext().startService(startIntent);
        }
    }

    private void stopForegroundService() {
        if (m_foregroundServiceStarted) {
            m_foregroundServiceStarted = false;
            Intent stopIntent = new Intent(m_activity, ForegroundService.class);
            stopIntent.setAction(ForegroundService.STOP_ACTION);
            m_activity.getApplicationContext().startService(stopIntent);
        }
    }

    private void startNavigation() {
        m_naviControlButton.setText(R.string.stop_navi);
        /* Configure Navigation manager to launch navigation on current map */
        m_navigationManager.setMap(map);
        calculateTtaUsingDownloadedTraffic();

        /*
         * Start the turn-by-turn navigation.Please note if the transport mode of the passed-in
         * route is pedestrian, the NavigationManager automatically triggers the guidance which is
         * suitable for walking. Simulation and tracking modes can also be launched at this moment
         * by calling either simulate() or startTracking()
         */

        /* Choose navigation modes between real time navigation and simulation */
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(m_activity);
        alertDialogBuilder.setTitle("Navigation");
        alertDialogBuilder.setMessage("Choose Mode");
        alertDialogBuilder.setNegativeButton("Navigation",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
                m_navigationManager.startNavigation(m_route);
                map.setTilt(60);
                startForegroundService();
            }
        });
        alertDialogBuilder.setPositiveButton("Simulation",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
                m_navigationManager.simulate(m_route,60);//Simualtion speed is set to 60 m/s
                map.setTilt(60);
                startForegroundService();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        /*
         * Set the map update mode to ROADVIEW.This will enable the automatic map movement based on
         * the current location.If user gestures are expected during the navigation, it's
         * recommended to set the map update mode to NONE first. Other supported update mode can be
         * found in HERE Mobile SDK for Android (Premium) API doc
         */
        m_navigationManager.setMapUpdateMode(NavigationManager.MapUpdateMode.ROADVIEW);

        /*
         * NavigationManager contains a number of listeners which we can use to monitor the
         * navigation status and getting relevant instructions.In this example, we will add 2
         * listeners for demo purpose,please refer to HERE Android SDK API documentation for details
         */
        addNavigationListeners();
    }

    private void addNavigationListeners() {

        /*
         * Register a NavigationManagerEventListener to monitor the status change on
         * NavigationManager
         */
        m_navigationManager.addNavigationManagerEventListener(
                new WeakReference<>(
                        m_navigationManagerEventListener));

        /* Register a PositionListener to monitor the position updates */
        m_navigationManager.addPositionListener(
                new WeakReference<>(m_positionListener));
    }

    private NavigationManager.PositionListener m_positionListener = new NavigationManager.PositionListener() {
        @Override
        public void onPositionUpdated(GeoPosition geoPosition) {
               }
    };

    private NavigationManager.NavigationManagerEventListener m_navigationManagerEventListener = new NavigationManager.NavigationManagerEventListener() {
        @Override
        public void onRunningStateChanged() {
            Toast.makeText(m_activity, "Running state changed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNavigationModeChanged() {
            Toast.makeText(m_activity, "Navigation mode changed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEnded(NavigationManager.NavigationMode navigationMode) {
            Toast.makeText(m_activity, navigationMode + " was ended", Toast.LENGTH_SHORT).show();
            stopForegroundService();
        }

        @Override
        public void onMapUpdateModeChanged(NavigationManager.MapUpdateMode mapUpdateMode) {
            Toast.makeText(m_activity, "Map update mode is changed to " + mapUpdateMode,
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRouteUpdated(Route route) {
            Toast.makeText(m_activity, "Route updated", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCountryInfo(String s, String s1) {
            Toast.makeText(m_activity, "Country info updated from " + s + " to " + s1,
                    Toast.LENGTH_SHORT).show();
        }
    };

    private MapGesture.OnGestureListener gestureListener = new MapGesture.OnGestureListener() {
        @Override
        public void onPanStart() {

        }

        @Override
        public void onPanEnd() {
        }

        @Override
        public void onMultiFingerManipulationStart() {
        }

        @Override
        public void onMultiFingerManipulationEnd() {
        }

        @Override
        public boolean onMapObjectsSelected(List<ViewObject> objects) {
            return false;
        }

        @Override
        public boolean onTapEvent(PointF p) {
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(PointF p) {
            return false;
        }

        @Override
        public void onPinchLocked() {
        }

        @Override
        public boolean onPinchZoomEvent(float scaleFactor, PointF p) {

            return false;
        }

        @Override
        public void onRotateLocked() {
        }

        @Override
        public boolean onRotateEvent(float rotateAngle) {
            return false;
        }

        @Override
        public boolean onTiltEvent(float angle) {

            return false;
        }

        @Override
        public boolean onLongPressEvent(PointF p) {
            return false;
        }

        @Override
        public void onLongPressRelease() {
        }

        @Override
        public boolean onTwoFingerTapEvent(PointF p) {
            return false;
        }
    };

    final private NavigationManager.RoadView.Listener roadViewListener = new NavigationManager.RoadView.Listener() {
        @Override
        public void onPositionChanged(GeoCoordinate geoCoordinate) {
            // an active RoadView provides coordinates that is the map transform center of it's
            // movements.
            m_mapTransformCenter = map.projectToPixel
                    (geoCoordinate).getResult();
        }
    };

    final private Map.OnTransformListener onTransformListener = new Map.OnTransformListener() {
        @Override
        public void onMapTransformStart() {
        }

        @Override
        public void onMapTransformEnd(MapState mapsState) {
            // do not start RoadView and its listener until moving map to current position has
            // completed
            if (m_returningToRoadViewMode) {
                NavigationManager.getInstance().setMapUpdateMode(NavigationManager.MapUpdateMode
                        .ROADVIEW);
                NavigationManager.getInstance().getRoadView().addListener(new
                        WeakReference<NavigationManager.RoadView.Listener>(roadViewListener));
                m_returningToRoadViewMode = false;
            }
        }

    };


    public void onDestroy() {
        /* Stop the navigation when app is destroyed */
        super.onDestroy();
        if (m_navigationManager != null) {
            stopForegroundService();
            m_navigationManager.stop();
        }
        if (MapEngine.isInitialized()) {
            TrafficUpdater.getInstance().enableUpdate(false);
        }

        if (m_requestInfo != null) {
            /*  Cancel request by request Id */
            TrafficUpdater.getInstance().cancelRequest(m_requestInfo.getRequestId());
        }
    }



        }
