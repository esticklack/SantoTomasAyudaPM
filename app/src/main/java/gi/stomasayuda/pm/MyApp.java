package gi.stomasayuda.pm;

import android.app.Application;

import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;

public class MyApp extends Application {

    public class ApplicationClass extends Application {

        // NOTE: Replace the below with your own ONESIGNAL_APP_ID
        private static final String ONESIGNAL_APP_ID = "########-####-####-####-############";

        @Override
        public void onCreate() {
            super.onCreate();

            // Verbose Logging set to help debug issues, remove before releasing your app.
            OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);

            // OneSignal Initialization
            OneSignal.initWithContext(this, ONESIGNAL_APP_ID);

            // requestPermission will show the native Android notification permission prompt.
            // NOTE: It's recommended to use a OneSignal In-App Message to prompt instead.
            OneSignal.getNotifications().requestPermission(true, Continue.with(r -> {
                if (r.isSuccess()) {
                    if (r.getData()) {
                        // `requestPermission` completed successfully and the user has accepted permission
                    }
                    else {
                        // `requestPermission` completed successfully but the user has rejected permission
                    }
                }
                else {
                    // `requestPermission` completed unsuccessfully, check `r.getThrowable()` for more info on the failure reason
                }
            }));
        }
    }


}
