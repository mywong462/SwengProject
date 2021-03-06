package ch.epfl.sweng.swengproject;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.maps.zzac;
import com.google.android.gms.internal.maps.zzk;
import com.google.android.gms.internal.maps.zzn;
import com.google.android.gms.internal.maps.zzt;
import com.google.android.gms.internal.maps.zzw;
import com.google.android.gms.internal.maps.zzz;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.internal.ILocationSourceDelegate;
import com.google.android.gms.maps.internal.IProjectionDelegate;
import com.google.android.gms.maps.internal.IUiSettingsDelegate;
import com.google.android.gms.maps.internal.zzab;
import com.google.android.gms.maps.internal.zzad;
import com.google.android.gms.maps.internal.zzaf;
import com.google.android.gms.maps.internal.zzaj;
import com.google.android.gms.maps.internal.zzal;
import com.google.android.gms.maps.internal.zzan;
import com.google.android.gms.maps.internal.zzap;
import com.google.android.gms.maps.internal.zzar;
import com.google.android.gms.maps.internal.zzat;
import com.google.android.gms.maps.internal.zzav;
import com.google.android.gms.maps.internal.zzax;
import com.google.android.gms.maps.internal.zzaz;
import com.google.android.gms.maps.internal.zzbb;
import com.google.android.gms.maps.internal.zzbd;
import com.google.android.gms.maps.internal.zzbf;
import com.google.android.gms.maps.internal.zzbs;
import com.google.android.gms.maps.internal.zzc;
import com.google.android.gms.maps.internal.zzh;
import com.google.android.gms.maps.internal.zzl;
import com.google.android.gms.maps.internal.zzp;
import com.google.android.gms.maps.internal.zzr;
import com.google.android.gms.maps.internal.zzv;
import com.google.android.gms.maps.internal.zzx;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.GeoPoint;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;

import java.util.List;

import ch.epfl.sweng.swengproject.util.FakeLocation;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
public class MapsInstrumentedTest {

    @Rule
    public GrantPermissionRule grantPermissionRule  = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public final ActivityTestRule<MapsActivity> mActivityRule = new ActivityTestRule<>(MapsActivity.class,false,false);

    private UiDevice mDevice;

    @Before
    public void injectLocation(){

        mDevice = UiDevice.getInstance(getInstrumentation());

        LocationServer ls = new FakeLocation();

        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("settings put location_providers_allowed +gps");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("settings put location_providers_allowed +network");

        //inject the mocked object in the activity
        mActivityRule.launchActivity(new Intent().putExtra("loc",ls));
    }

    @After
    public void after(){
        clickOKLocationIfAsked();
    }

    @Test
    public void popUpWork(){

        FirebaseAuth fAuth = mock(FirebaseAuth.class);
        FirebaseUser fUser = mock(FirebaseUser.class);
        when(fUser.getEmail()).thenReturn("info@epfl.ch");
        when(fAuth.getCurrentUser()).thenReturn(fUser);
        mActivityRule.getActivity().setAuth(fAuth);

        mActivityRule.getActivity().onMarkerClick(new Marker(new zzt() {
            @Override
            public void remove() throws RemoteException {
				//for overriding
            }

            @Override
            public String getId() throws RemoteException {
                return null;
            }

            @Override
            public void setPosition(LatLng latLng) throws RemoteException {
				//for overriding
            }

            @Override
            public LatLng getPosition() throws RemoteException {
                return new LatLng(1,2);
            }

            @Override
            public void setTitle(String s) throws RemoteException {
				//for overriding
            }

            @Override
            public String getTitle() throws RemoteException {
                return null;
            }

            @Override
            public void setSnippet(String s) throws RemoteException {
				//for overriding
            }

            @Override
            public String getSnippet() throws RemoteException {
                return null;
            }

            @Override
            public void setDraggable(boolean b) throws RemoteException {
				//for overriding
            }

            @Override
            public boolean isDraggable() throws RemoteException {
                return false;
            }

            @Override
            public void showInfoWindow() throws RemoteException {
				//for overriding
            }

            @Override
            public void hideInfoWindow() throws RemoteException {
				//for overriding
            }

            @Override
            public boolean isInfoWindowShown() throws RemoteException {
                return false;
            }

            @Override
            public void setVisible(boolean b) throws RemoteException {
				//for overriding
            }

            @Override
            public boolean isVisible() throws RemoteException {
                return false;
            }

            @Override
            public boolean zzj(zzt zzt) throws RemoteException {
                return false;
            }

            @Override
            public int zzi() throws RemoteException {
                return 0;
            }

            @Override
            public void zzg(IObjectWrapper iObjectWrapper) throws RemoteException {
				//for overriding
            }

            @Override
            public void setAnchor(float v, float v1) throws RemoteException {
				//for overriding
            }

            @Override
            public void setFlat(boolean b) throws RemoteException {
				//for overriding
            }

            @Override
            public boolean isFlat() throws RemoteException {
                return false;
            }

            @Override
            public void setRotation(float v) throws RemoteException {
				//for overriding
            }

            @Override
            public float getRotation() throws RemoteException {
                return 0;
            }

            @Override
            public void setInfoWindowAnchor(float v, float v1) throws RemoteException {
				//for overriding
            }

            @Override
            public void setAlpha(float v) throws RemoteException {
				//for overriding
            }

            @Override
            public float getAlpha() throws RemoteException {
                return 0;
            }

            @Override
            public void setZIndex(float v) throws RemoteException {
				//for overriding
            }

            @Override
            public float getZIndex() throws RemoteException {
                return 0;
            }

            @Override
            public void zze(IObjectWrapper iObjectWrapper) throws RemoteException {
				//for overriding
            }

            @Override
            public IObjectWrapper zzj() throws RemoteException {
                return null;
            }

            @Override
            public IBinder asBinder() {
                return null;
            }
        }));


    }


    @Test
    public void canSwitchActivity() {

        onView(withId(R.id.create_need_btn)).perform(click());
        

    }


    @Test
    public void canDisplayOnMenu(){

        View menu =  mock(View.class);

        TextView t = mock(TextView.class);

        when(menu.findViewById(R.id.needDescription)).thenReturn(t);
        doNothing().when(t).setText("good and long description");

        mActivityRule.getActivity().setTestMode();

        mActivityRule.getActivity().displayOnMenu(menu,new GeoPoint(12,13));

    }

    @Test
    public void onRestoreTest(){

        Bundle b = new Bundle();

        mActivityRule.getActivity().onRestoreInstanceState(b);

    }

    @Test
    public void onActResTest(){
        mActivityRule.getActivity().onActivityResult(0,0,new Intent());
    }


    @Test
    public void onReqPermResTest(){
        mActivityRule.getActivity().onRequestPermissionsResult(0,new String[0],new int[0]);
    }


    @Test
    public void canBindButton(){

        mActivityRule.getActivity().bindAddNeedButton();
    }

    @Test
    public void canUpdateUI(){

        GoogleMap m = new GoogleMap(new IGoogleMapDelegate() {
            @Override
            public CameraPosition getCameraPosition() throws RemoteException {
                return null;
            }

            @Override
            public float getMaxZoomLevel() throws RemoteException {
                return 0;
            }

            @Override
            public float getMinZoomLevel() throws RemoteException {
                return 0;
            }

            @Override
            public void moveCamera(IObjectWrapper iObjectWrapper) throws RemoteException {
				//for overriding
            }

            @Override
            public void animateCamera(IObjectWrapper iObjectWrapper) throws RemoteException {
				//for overriding
            }

            @Override
            public void animateCameraWithCallback(IObjectWrapper iObjectWrapper, zzc zzc) throws RemoteException {
				//for overriding
            }

            @Override
            public void animateCameraWithDurationAndCallback(IObjectWrapper iObjectWrapper, int i, zzc zzc) throws RemoteException {
				//for overriding
            }

            @Override
            public void stopAnimation() throws RemoteException {
				//for overriding
            }

            @Override
            public zzz addPolyline(PolylineOptions polylineOptions) throws RemoteException {
                return null;
            }

            @Override
            public zzw addPolygon(PolygonOptions polygonOptions) throws RemoteException {
                return null;
            }

            @Override
            public zzt addMarker(MarkerOptions markerOptions) throws RemoteException {
                return null;
            }

            @Override
            public zzk addGroundOverlay(GroundOverlayOptions groundOverlayOptions) throws RemoteException {
                return null;
            }

            @Override
            public zzac addTileOverlay(TileOverlayOptions tileOverlayOptions) throws RemoteException {
                return null;
            }

            @Override
            public void clear() throws RemoteException {
				//for overriding
            }

            @Override
            public int getMapType() throws RemoteException {
                return 0;
            }

            @Override
            public void setMapType(int i) throws RemoteException {
				//for overriding
            }

            @Override
            public boolean isTrafficEnabled() throws RemoteException {
                return false;
            }

            @Override
            public void setTrafficEnabled(boolean b) throws RemoteException {
				//for overriding
            }

            @Override
            public boolean isIndoorEnabled() throws RemoteException {
                return false;
            }

            @Override
            public boolean setIndoorEnabled(boolean b) throws RemoteException {
                return false;
            }

            @Override
            public boolean isMyLocationEnabled() throws RemoteException {
                return false;
            }

            @Override
            public void setMyLocationEnabled(boolean b) throws RemoteException {
				//for overriding
            }

            @Override
            public Location getMyLocation() throws RemoteException {
                return null;
            }

            @Override
            public void setLocationSource(ILocationSourceDelegate iLocationSourceDelegate) throws RemoteException {
				//for overriding
            }

            @Override
            public IUiSettingsDelegate getUiSettings() throws RemoteException {
                return new IUiSettingsDelegate() {
                    @Override
                    public void setZoomControlsEnabled(boolean b) throws RemoteException {
						//for overriding
                    }

                    @Override
                    public void setCompassEnabled(boolean b) throws RemoteException {
						//for overriding
                    }

                    @Override
                    public void setMyLocationButtonEnabled(boolean b) throws RemoteException {
						//for overriding
                    }

                    @Override
                    public void setScrollGesturesEnabled(boolean b) throws RemoteException {
						//for overriding
                    }

                    @Override
                    public void setZoomGesturesEnabled(boolean b) throws RemoteException {
						//for overriding
                    }

                    @Override
                    public void setTiltGesturesEnabled(boolean b) throws RemoteException {
						//for overriding
                    }

                    @Override
                    public void setRotateGesturesEnabled(boolean b) throws RemoteException {
						//for overriding
                    }

                    @Override
                    public void setAllGesturesEnabled(boolean b) throws RemoteException {
						//for overriding
                    }

                    @Override
                    public boolean isZoomControlsEnabled() throws RemoteException {
                        return false;
                    }

                    @Override
                    public boolean isCompassEnabled() throws RemoteException {
                        return false;
                    }

                    @Override
                    public boolean isMyLocationButtonEnabled() throws RemoteException {
                        return false;
                    }

                    @Override
                    public boolean isScrollGesturesEnabled() throws RemoteException {
                        return false;
                    }

                    @Override
                    public boolean isZoomGesturesEnabled() throws RemoteException {
                        return false;
                    }

                    @Override
                    public boolean isTiltGesturesEnabled() throws RemoteException {
                        return false;
                    }

                    @Override
                    public boolean isRotateGesturesEnabled() throws RemoteException {
                        return false;
                    }

                    @Override
                    public void setIndoorLevelPickerEnabled(boolean b) throws RemoteException {
						//for overriding
                    }

                    @Override
                    public boolean isIndoorLevelPickerEnabled() throws RemoteException {
                        return false;
                    }

                    @Override
                    public void setMapToolbarEnabled(boolean b) throws RemoteException {
						//for overriding
                    }

                    @Override
                    public boolean isMapToolbarEnabled() throws RemoteException {
                        return false;
                    }

                    @Override
                    public IBinder asBinder() {
                        return null;
                    }
                };
            }

            @Override
            public IProjectionDelegate getProjection() throws RemoteException {
                return null;
            }

            @Override
            public void setOnCameraChangeListener(zzl zzl) throws RemoteException {
						//for overriding
            }

            @Override
            public void setOnMapClickListener(zzaj zzaj) throws RemoteException {
						//for overriding
            }

            @Override
            public void setOnMapLongClickListener(zzan zzan) throws RemoteException {
						//for overriding
            }

            @Override
            public void setOnMarkerClickListener(zzar zzar) throws RemoteException {
						//for overriding
            }

            @Override
            public void setOnMarkerDragListener(zzat zzat) throws RemoteException {
						//for overriding
            }

            @Override
            public void setOnInfoWindowClickListener(zzab zzab) throws RemoteException {
						//for overriding
            }

            @Override
            public void setInfoWindowAdapter(zzh zzh) throws RemoteException {
						//for overriding
            }

            @Override
            public com.google.android.gms.internal.maps.zzh addCircle(CircleOptions circleOptions) throws RemoteException {
                return null;
            }

            @Override
            public void setOnMyLocationChangeListener(zzax zzax) throws RemoteException {
						//for overriding
            }

            @Override
            public void setOnMyLocationButtonClickListener(zzav zzav) throws RemoteException {
						//for overriding
            }

            @Override
            public void snapshot(zzbs zzbs, IObjectWrapper iObjectWrapper) throws RemoteException {
						//for overriding
            }

            @Override
            public void setPadding(int i, int i1, int i2, int i3) throws RemoteException {
						//for overriding
            }

            @Override
            public boolean isBuildingsEnabled() throws RemoteException {
                return false;
            }

            @Override
            public void setBuildingsEnabled(boolean b) throws RemoteException {
						//for overriding
            }

            @Override
            public void setOnMapLoadedCallback(zzal zzal) throws RemoteException {
						//for overriding
            }

            @Override
            public zzn getFocusedBuilding() throws RemoteException {
                return null;
            }

            @Override
            public void setOnIndoorStateChangeListener(com.google.android.gms.maps.internal.zzz zzz) throws RemoteException {
						//for overriding
            }

            @Override
            public void setWatermarkEnabled(boolean b) throws RemoteException {
						//for overriding
            }

            @Override
            public void getMapAsync(zzap zzap) throws RemoteException {
						//for overriding
            }

            @Override
            public void onCreate(Bundle bundle) throws RemoteException {
						//for overriding
            }

            @Override
            public void onResume() throws RemoteException {
						//for overriding
            }

            @Override
            public void onPause() throws RemoteException {
						//for overriding
            }

            @Override
            public void onDestroy() throws RemoteException {
						//for overriding
            }

            @Override
            public void onLowMemory() throws RemoteException {
						//for overriding
            }

            @Override
            public boolean useViewLifecycleWhenInFragment() throws RemoteException {
                return false;
            }

            @Override
            public void onSaveInstanceState(Bundle bundle) throws RemoteException {
						//for overriding
            }

            @Override
            public void setContentDescription(String s) throws RemoteException {
						//for overriding
            }

            @Override
            public void snapshotForTest(zzbs zzbs) throws RemoteException {
						//for overriding
            }

            @Override
            public void setOnPoiClickListener(zzbb zzbb) throws RemoteException {
						//for overriding
            }

            @Override
            public void onEnterAmbient(Bundle bundle) throws RemoteException {
						//for overriding
            }

            @Override
            public void onExitAmbient() throws RemoteException {
						//for overriding
            }

            @Override
            public void setOnGroundOverlayClickListener(zzx zzx) throws RemoteException {
						//for overriding
            }

            @Override
            public void setOnInfoWindowLongClickListener(zzaf zzaf) throws RemoteException {
						//for overriding
            }

            @Override
            public void setOnPolygonClickListener(zzbd zzbd) throws RemoteException {
						//for overriding
            }

            @Override
            public void setOnInfoWindowCloseListener(zzad zzad) throws RemoteException {
						//for overriding
            }

            @Override
            public void setOnPolylineClickListener(zzbf zzbf) throws RemoteException {
						//for overriding
            }

            @Override
            public void setOnCircleClickListener(zzv zzv) throws RemoteException {
						//for overriding
            }

            @Override
            public void setMinZoomPreference(float v) throws RemoteException {
						//for overriding
            }

            @Override
            public void setMaxZoomPreference(float v) throws RemoteException {
						//for overriding
            }

            @Override
            public void resetMinMaxZoomPreference() throws RemoteException {

            }

            @Override
            public void setLatLngBoundsForCameraTarget(LatLngBounds latLngBounds) throws RemoteException {
						//for overriding
            }

            @Override
            public void setOnCameraMoveStartedListener(com.google.android.gms.maps.internal.zzt zzt) throws RemoteException {
						//for overriding
            }

            @Override
            public void setOnCameraMoveListener(zzr zzr) throws RemoteException {
						//for overriding
            }

            @Override
            public void setOnCameraMoveCanceledListener(zzp zzp) throws RemoteException {
						//for overriding
            }

            @Override
            public void setOnCameraIdleListener(com.google.android.gms.maps.internal.zzn zzn) throws RemoteException {
						//for overriding
            }

            @Override
            public boolean setMapStyle(MapStyleOptions mapStyleOptions) throws RemoteException {
                return false;
            }

            @Override
            public void onStart() throws RemoteException {
						//for overriding
            }

            @Override
            public void onStop() throws RemoteException {
						//for overriding
            }

            @Override
            public void setOnMyLocationClickListener(zzaz zzaz) throws RemoteException {
						//for overriding
            }

            @Override
            public IBinder asBinder() {
                return null;
            }
        });

        mActivityRule.getActivity().setMap(m);
        mActivityRule.getActivity().updateUI();

    }

    private void clickOKLocationIfAsked(){
        try {
            UiObject OKBtn = mDevice.findObject(new UiSelector()
                    .text("OK")
                    .className("android.widget.Button"));
            OKBtn.waitForExists(500);
            if (OKBtn.exists()) {
                OKBtn.click();
            }
            clickAgreeImproveLocationAccuracy();
        }catch(UiObjectNotFoundException e){}
    }

    private void clickAgreeImproveLocationAccuracy() throws UiObjectNotFoundException {
        UiObject agreeImprove = mDevice.findObject(new UiSelector()
                .text("AGREE")
                .index(1)
                .resourceId("android:id/button1")
                .className("android.widget.Button")
                .clickable(true)
                .packageName("com.google.android.gms"));
        agreeImprove.waitForExists(500);
        if (agreeImprove.exists()) {
            agreeImprove.click();
        }
    }

}

