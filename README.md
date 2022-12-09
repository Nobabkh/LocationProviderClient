
# LocationProviderClient

A Libary to Empliment LocationService on Android easily



## Badges



[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)
[![](https://jitpack.io/v/Nobabkh/LocationProviderClient.svg)](https://jitpack.io/#Nobabkh/LocationProviderClient)
[![CodeFactor](https://www.codefactor.io/repository/github/nobabkh/locationproviderclient/badge)](https://www.codefactor.io/repository/github/nobabkh/locationproviderclient)



## Implement

#### step-1: Add It to your settings.gradle file


```java
  repositories {
			...
			maven { url 'https://jitpack.io' }
		}
```

step-2: In your app add this to your build.gradle

```java
  dependencies {
	        implementation 'com.github.Nobabkh:LocationProviderClient:1.0.0'
	}
```

Add permission to AndroidManifest.xml 

```xml
	<uses-permission android:name="android.permission.INTERNET"/>
    	<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    	<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
```

### After implementetion It is easy to start just Initialize LocationProviderClient to get start

### Initialize from Activity

```java
  public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocationProviderClient l = new LocationProviderClient(this, this);
        l.requestpermission();
        if(l.checkpermission()){
          try {
                location();
            } catch (ClientException e) {
                Log.i("Clientexception", e.toString());
            }
        }

    }

    void location() throws ClientException {
        
        if(l.isGPSturnedON())
        {
            l.getLocationWithCallback(new ClientCallback() {
                @Override
                public void CallBackFunc(Location location) {
                    Toast.makeText(MainActivity.this, location.getLatitude()+"\n"+location.getLongitude(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
```

### Initialize from Service

```java
  public class MyService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      LocationProviderClient l = new LocationProviderClient(this);
      if(l.checkpermission()){
          try {
                location();
            } catch (ClientException e) {
                Log.i("Clientexception", e.toString());
            }
        }
    }

    void location() throws ClientException {
        
        if(l.isGPSturnedON())
        {
            l.getLocationWithCallback(new ClientCallback() {
                @Override
                public void CallBackFunc(Location location) {
                    Toast.makeText(MainActivity.this, location.getLatitude()+"\n"+location.getLongitude(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

```
## Functions
```java
  public boolean checkpermission();
```
Checks if permission accepted. uses context
```java
  public boolean isGPSturnedON();
```
Checks if GPS is Turned onCreate. uses context

```java
  void turnonLocation() throws ClientException
```
Turns Location of the Device on
```java
  public void getLocationWithCallback(ClientCallback clientCallback) throws ClientException
```
Use this function to get location with callback. use context




