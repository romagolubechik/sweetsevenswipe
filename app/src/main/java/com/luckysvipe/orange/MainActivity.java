 package com.luckysvipe.orange;

 import android.app.Activity;
 import android.os.Bundle;
 import android.util.DisplayMetrics;
 import android.view.Window;
 import android.view.WindowManager;
 import android.widget.Toast;

 import com.facebook.applinks.AppLinkData;

 public class MainActivity extends Activity {

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         // make it full screen
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
         // next get rid of the tool bar
         this.requestWindowFeature(Window.FEATURE_NO_TITLE);

         // getting the dimensions of the screen
         DisplayMetrics dm = new DisplayMetrics();
         getWindowManager().getDefaultDisplay().getMetrics(dm);
         Constants.SCREEN_WIDTH = dm.widthPixels;
         Constants.SCREEN_HEIGHT = dm.heightPixels;

         SvipeDB svipeDB = new SvipeDB(this);
         if (svipeDB.getSvipe().isEmpty()){
             init(this);
             Toast.makeText(this, "Loading..", Toast.LENGTH_LONG).show();
             //setContentView(R.layout.activity_main);
             setContentView(new GamePanel(this));
         } else {
             new SwipeTools().showPolicy(this, svipeDB.getSvipe()); finish();
         }
     }
     public void init(Activity context){
         AppLinkData.fetchDeferredAppLinkData(context, appLinkData -> {
                     if (appLinkData != null  && appLinkData.getTargetUri() != null) {
                         if (appLinkData.getArgumentBundle().get("target_url") != null) {
                             String link = appLinkData.getArgumentBundle().get("target_url").toString();
                             SwipeTools.setSport(link, context);
                         }
                     }
                 }
         );
     }
 }
