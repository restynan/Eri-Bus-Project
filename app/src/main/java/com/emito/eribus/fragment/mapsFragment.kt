package com.emito.eribus.fragment

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.emito.eribus.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_maps.*
import androidx.core.content.ContextCompat.getSystemService
import java.util.*


class mapsFragment : Fragment(),OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var mMapview: MapView
    private lateinit var mapview: View
    private lateinit var locationManager:LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var  latLng:LatLng
    private  val Min_Time:Long=1000
    private  val Min_Dist:Float=5f
    lateinit  var address:String
    lateinit  var city:String
    lateinit  var state:String
    lateinit  var country:String
    lateinit  var mapFragment:SupportMapFragment



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       mapview= inflater.inflate(R.layout.fragment_maps, container, false)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        if(mapFragment==null){
            var fm =fragmentManager
            var ft=fm?.beginTransaction()
            mapFragment=SupportMapFragment.newInstance()
            ft?.replace(R.id.map,mapFragment)?.commit()
        }
        mapFragment.getMapAsync(this)
        return mapview
    }

   /* override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMapview = mapview.findViewById(R.id.map) as MapView
        if (mMapview!=null){
            mMapview.onCreate(null)
            mMapview.onResume()
            mMapview.getMapAsync(this)
        }
    }*/


    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        MapsInitializer.initialize(context)
        mMap = googleMap
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        locationManager=getContext()?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Add a marker in Sydney and move the camera

        locationListener= object: LocationListener {
            override fun onLocationChanged(location: Location?) {
                // Add a marker to over new location****************************************
                // editTextlatitude.setText(location?.latitude.toString())
                //   editTextlongitude.setText(location?.longitude.toString())


                try {
                    latLng = LatLng(location!!.latitude, location!!.longitude)
                    mMap.clear() // clear old location markerin google map
                    mMap.addMarker(MarkerOptions().position(latLng).title("user Location "))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                }
                catch (e:SecurityException){
                    e.printStackTrace()
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

            }

            override fun onProviderEnabled(provider: String?) {

            }

            override fun onProviderDisabled(provider: String?) {

            }
        }

        ///asking for map pemissions/user persmissions

        askPermission()
        /* var latitude=latLng.latitude
          var longitude =latLng.longitude

          geocoder= Geocoder(this, Locale.getDefault())


          try {
              addresses = geocoder.getFromLocation(latitude, longitude, 1)

              address = addresses.get(0).getAddressLine(0)
              city = addresses.get(0).locality
              state = addresses.get(0).adminArea
              country = addresses.get(0).countryName
              // postaladdress = addresses.get(0).postalCode
              // knonNames = addresses.get(0).featureName
          }
          catch (e: IOException){
              e.printStackTrace()

          }
         locat.setText( country)*/

    }
  fun askPermission(){
        Dexter.withActivity(this.activity).withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION).withListener(object :
            PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                if (ActivityCompat.checkSelfPermission( mapview.context, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(mapview.context, android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                    return
                }
             //  geocoder= Geocoder(this, Locale.getDefault())

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Min_Time, Min_Dist, locationListener)
                var lastLocation =locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                latLng = LatLng( lastLocation!!.latitude, lastLocation!!.longitude)
                mMap.clear() // clear old location markerin google map
                mMap.addMarker(MarkerOptions().position(latLng).title("user Location "))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))


            }

            override fun onPermissionRationaleShouldBeShown(
                permission: PermissionRequest?,
                token: PermissionToken?
            ) {token?.continuePermissionRequest()

            }

            override fun onPermissionDenied(response: PermissionDeniedResponse?) {

            }

        }).check()
    }
}
