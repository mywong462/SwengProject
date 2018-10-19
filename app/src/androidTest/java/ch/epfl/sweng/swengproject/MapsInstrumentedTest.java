package ch.epfl.sweng.swengproject;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
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

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import ch.epfl.sweng.swengproject.util.FakeLocation;

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

    @Before
    public void injectLocation(){

        LocationServer ls = new FakeLocation();

        //inject the mocked object in the activity
        mActivityRule.launchActivity(new Intent().putExtra("loc",ls));
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

            }

            @Override
            public String getId() throws RemoteException {
                return null;
            }

            @Override
            public void setPosition(LatLng latLng) throws RemoteException {

            }

            @Override
            public LatLng getPosition() throws RemoteException {
                return new LatLng(1,2);
            }

            @Override
            public void setTitle(String s) throws RemoteException {

            }

            @Override
            public String getTitle() throws RemoteException {
                return null;
            }

            @Override
            public void setSnippet(String s) throws RemoteException {

            }

            @Override
            public String getSnippet() throws RemoteException {
                return null;
            }

            @Override
            public void setDraggable(boolean b) throws RemoteException {

            }

            @Override
            public boolean isDraggable() throws RemoteException {
                return false;
            }

            @Override
            public void showInfoWindow() throws RemoteException {

            }

            @Override
            public void hideInfoWindow() throws RemoteException {

            }

            @Override
            public boolean isInfoWindowShown() throws RemoteException {
                return false;
            }

            @Override
            public void setVisible(boolean b) throws RemoteException {

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

            }

            @Override
            public void setAnchor(float v, float v1) throws RemoteException {

            }

            @Override
            public void setFlat(boolean b) throws RemoteException {

            }

            @Override
            public boolean isFlat() throws RemoteException {
                return false;
            }

            @Override
            public void setRotation(float v) throws RemoteException {

            }

            @Override
            public float getRotation() throws RemoteException {
                return 0;
            }

            @Override
            public void setInfoWindowAnchor(float v, float v1) throws RemoteException {

            }

            @Override
            public void setAlpha(float v) throws RemoteException {

            }

            @Override
            public float getAlpha() throws RemoteException {
                return 0;
            }

            @Override
            public void setZIndex(float v) throws RemoteException {

            }

            @Override
            public float getZIndex() throws RemoteException {
                return 0;
            }

            @Override
            public void zze(IObjectWrapper iObjectWrapper) throws RemoteException {

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
        doNothing().when(t).setText(isA(String.class));

        mActivityRule.getActivity().setTestMode();

        mActivityRule.getActivity().displayOnMenu(menu,new GeoPoint(12,13));

    }
}