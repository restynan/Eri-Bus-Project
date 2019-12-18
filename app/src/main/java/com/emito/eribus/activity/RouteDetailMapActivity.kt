
package com.emito.eribus.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.emito.eribus.R
import com.emito.eribus.directionhelpers.FetchURL
import com.emito.eribus.directionhelpers.TaskLoadedCallback
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

class RouteDetailMapActivity : AppCompatActivity(), OnMapReadyCallback, TaskLoadedCallback {
    lateinit var map: GoogleMap
    lateinit var btnGetDirection: Button
    lateinit var place1:MarkerOptions
    lateinit var place2:MarkerOptions
    lateinit var currentPolyline:Polyline

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_detail_map)
        btnGetDirection = findViewById(R.id.btnGetDirection)
//        var mapFragment:MapFragment = fragmentManager.findFragmentById(R.id.mapNearBy) as MapFragment
//        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapNearBy as SupportMapFragment
//        mapFragment.getMapAsync(this)
//                    mapFragment.getMapAsync(this)

//        (activity.supportFragmentManager.findFragmentById(R.id.mapNearBy) as SupportMapFragment?)?.let {
////            it.getMapAsync(this)
////        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapNearBy) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        place1 = MarkerOptions().position(LatLng(37.778008, -122.431272)).title("Location 1");
        place2 = MarkerOptions().position(LatLng(41.921673, -93.312271)).title("Location 2");

        var url = getUrl(place1.position, place2.position, "driving")
        FetchURL(this).execute(url,"driving")
    }

//    override fun onMapReady(p0: GoogleMap) {
//        map = p0
//    }



    override fun onMapReady(googleMap: GoogleMap ) {
        map = googleMap;
        Log.d("mylog", "Added Markers");
        map.addMarker(place1);
        map.addMarker(place2);
    }


    private fun getUrl(origin: LatLng , dest: LatLng , directionMode: String ): String {
        // Origin of route
        var str_origin: String = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        var str_dest:String = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        var mode:String = "mode=" + directionMode;
        // Building the parameters to the web service
        var parameters:String = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        var output:String = "json";
        // Building the url to the web service
        var url:String = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(
            R.string.google_maps_key)
        return url;
    }

//    override fun onTaskDone(vararg values: Any?) {
//        if(currentPolyline != null){
//
//            currentPolyline.remove()
//        }
//        currentPolyline = map.addPolyline(values[0] as PolylineOptions?)
//    }


    override fun onTaskDone(vararg values: Any?) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = map.addPolyline(values[0] as PolylineOptions);
    }
}
