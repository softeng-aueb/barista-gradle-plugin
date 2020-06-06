# Barista Testing Framework

**A tool for context-aware Android application testing**

Developed By: Tsiskomichelis Stelios - stsiskomixelis@aueb.gr - AUEB (2018 - 2019), 
 
Inspiration/Architectural design: [Vassilis Zafeiris](https://github.com/bzafiris)
 
---

## Table of Contents

* [Getting Started](#getting-started)
	* [Barista Plugin Installation. (Manually) ](#barista-plugin-installation)
	* [Barista Library Installation. ](#barista-library-installation)
* [How It Works.](#how-it-works)
    * [Barista in Android testing.](#barista-in-android-testing)
    * [Barista in Android fuzzing.](#barista-in-android-fuzzing-testing)
* [Supported Options.](#supported-options-for-fuzzer)
* [Supported Commands.](#supported-commands)
* [How to Use.](#how-to-use)
* [Common Problems.](#common-problems)

---

##	Getting Started

Barista  is a tool consisted of the [Barista Gradle Plugin](https://github.com/softeng-aueb/barista-gradle-plugin/tree/master/barista-plugin) and the [Barista Library](https://github.com/softeng-aueb/barista-android).
Both together offer a great framework for customizing the runtime Android environment during traditional instrumentation testing. 
More specific, it allows the developer to customize various emulator options such as Wifi/Data connectivity, geolocation, battery status, screen preferences etc., using declarations before each test method.

From now on, Barista can act as a fuzzer, taking advantage of Android's Monkey tool and the new context fuzzing that has been implemented.
 
For this time of being, Barista is tested with the following prerequisites:
 
 	Android Gradle Plugin Version 3.2.1
	Gradle Version 4.6
	Compile Sdk Version API 28
	Android Studio 3.5
	Android Studio AVD emulators
	
## Barista Plugin Installation

a) Clone the project from the repository

b) Install the plugin to the local maven repository using the command
     
    gradlew build install  
    
c) At any android project use the follwing .gradle configurations:

*Global build.gradle file*

     buildscript{
                ...
                repositories{
                    ...
                    mavenLocal()
                }
            
            }
            ...
            dependencies{
                ....
                classpath 'gr.aueb.android:barista-plugin:*version*'
            }

> The above configuration makes sure that gradle will look into the local
> maven repository to find the previous installed plugin. (Later will be
> available in global maven repo). It also sets the classpath of the
> plugin executable as an android build dependency.

In order to use the Barista Plugin just declare the usage of the
plugin using the following code in your app specific .gradle file

*Module specific build.gradle file (Your main android app module .gradle file)*
    
    plugins{
        ...
        id 'gr.aueb.android.barista-plugin'
    }
    
d) Update debug android build type to have test coverage enabled

    buildTypes {
        ...
        
        debug {
            ...
            testCoverageEnabled true
        }
    }

e) When you run **any android test** (basically when the *assembleDebugAndroidTest* gradle task is trigered) 
the plugin should deploy a HTTP server container right before the execution of tests in the emulator. This HTTP server 
is alive until all the tests are finished. The default listening port of the server is **8040**.  In order to set a specific listening port you can use
the below configuration structure inside your gradle.build file
    
    ...
    baristaSettings {
        host = "xxx.xxx.xxx.xxx" (IP of computer) 
        port = 8070
    }	
    ...	

> Normally the http server closes automaticaly after each test build. However there may be cases where this is not happening 
correctly for various reasons. In such cases you have to shutdown the server manually by using http://localhost:8040/barista/kill request from your browser.

f) In order to get coverage reports working.
   
   Extend your main activity with BaristaActivity:
    
    ...
    
    public class YourMainActivity extends BaristaActivity {
        ...
    }
   
   Copy paste the following task for the coverage reports

    task jacocoTestReport(type: JacocoReport) {
        reports {
            xml.enabled = true
            html.enabled = true
        }
    
        def fileFilter = ['**/R.class',
                          '**/R$*.class',
                          '**/BuildConfig.*',
                          '**/Manifest*.*',
                          '**/*Test*.*',
                          'android/**/*.*',
                          '**/data/models/*']
        def debugTree = fileTree(dir: "${buildDir}/intermediates/javac/debug", excludes: fileFilter)
        def mainSrc = "${project.projectDir}/src/main/java"
    
        sourceDirectories = files([mainSrc])
        classDirectories = files([debugTree])
        executionData = fileTree(dir: "$project.rootDir", includes: [
                "coverage.exec"
        ])
    }

After successfully deploying the barista plugin this message should appear in the build output of your android project 

    ...
    
    :app:assembleDebugAndroidTest
	:app:deployTestDispacherServerTask
    Deploying Server at: http://localhost:8040/barista/
    
Now there is a HTTP Server container ready to accept REST requests from the barista library module.

## Barista Library Installation

The Barista library provides the developer a set of commands in the form of annotations 
that can be used at the instrumentation test in order to execute adb and telnet commands at test time to the connected emulator(s).

1. Clone the library files

2. Install the library to the local maven repository using
		
    gradlew clean build publishToMavenLocal
	 	
    > Later will be available to the global repository. 
	 
3.  Add the library to the classpath.

    Inside the project build.gradle file
    
        buildscript {
            repositories {
                ....
                
            }
            dependencies {
                classpath 'com.android.tools.build:gradle:3.2.1'
                classpath 'gr.aueb.android:barista-plugin:1.0-SNAPSHOT'
                classpath 'gr.aueb.android:barista:1.0'
            }
        }
  	
4. Assign the Barista Instrumentation Test  Listener inside the module specific gradle.build file
	
		android {
			compileSdkVersion 28
			defaultConfig {
				...
				testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
				testInstrumentationRunnerArgument "listener", "gr.aueb.android.barista.core.BaristaRunListener"
			}
			...
		}
  	
5. Add the dependecies needed for Barista Library to work
			
	````			
	dependencies {
		...
	
		//BARISTA DEPENDENCIES START
		implementation 'gr.aueb.android:barista:2.0'
		implementation 'com.squareup.retrofit2:retrofit:2.4.0'
		implementation 'com.jakewharton.timber:timber:4.7.1'
		implementation 'com.squareup.retrofit2:converter-jackson:2.4.0'
		implementation 'com.squareup.okhttp3:okhttp:3.6.0'
		implementation 'com.squareup.okhttp3:logging-interceptor:3.6.0'
		//BARISTA DEPENDENCIES END
		...
	}  	
	````

## How it works

### Barista in Android testing
    
- Workflow

	1. Barista plugin task checks if target project is Android. Check based on imported plugins and packages
	
	2. If it is an Android Project, check in the gradle task graph if there is a task named "assembleDebugAndroidTest"
	   This indicates that a test is about to take place.
	   
	3. Just before the instrumentation tests run, The HTTP server is deployed and access tokens are shared among the connected emulators.
	    This way each time a request arrives the server knows to which emulator a command should be executed.
	       
	4. At test time the Barista Run Listener parses the barista annotation of each test, 
	constructs an appropriate command requests and sends it to the server.
	 
	5. The server receives which command(s) to execute to which emulator and does so.
	
	6. The emulator now has the requested settings and the body of the instrumentation test is executed.
	
	7. After the completion of each test the emulator is reseted to the previous/default settings
	
	8. Step 4 is executed again until all tests are finished.
	
	9. When all tests are finished the server shuts down. 
	
- Host-Emulator connectivity   
	AVD emulators can communicate with the host-machine by using the default NAT-provided IP 10.0.2.2 (https://developer.android.com/studio/run/emulator-networking). 
	Many factors can affect the communication between the emulator and the host machine such as a local firewall.
	
### Barista in Android fuzzing

- Workflow
    
    1. Barista plugin task checks if target project is Android. Check based on imported plugins and packages
    
    2. If it is an Android Project, register the following gradle tasks:
    
        1. startMonkey, which is a task that calls Android's Monkey tool for UI fuzzing.
        2. startBaristaFuzzer, which is a task that calls Android's Monkey tool and or own context fuzzing logic.
        
    3. By calling one of those 2 tasks, Barista calls the appropriate adb commands to fuzz the application under test.   

	4. Gradle captures the options given and launches the appropriate tests.

- Fuzzing
	
	Barista fuzzer injects UI events through the Monkey tool, and changes the context of the emulator with the help of the following commands.

## Supported Options for fuzzer

| Declaration | Description |
|---|---|
| --events | The number of events that the fuzzer is gonna inject to the UI. |
| --epochs | The number of testing cycles. |
| --throttle | The pause time after each UI event is ejected (in ms). |
| --config | The *.properties config file that includes the context fuzzing parameters. |
| --parallelRun | By default Barista fuzzer runs UI events and context events sequantially. By adding this parameter the events are run in parallel. | 

The .properties file contains which context models with be included when the fuzzer will start.

There are six context models that are based on the commands following. These models are:

| Model | Description |
|---|---|
| RandomWalkModel | Changes the location of the device based on street walk model. |
| FuzzMovementModel | Changes the location of the device unpredictably. The change can be either small or really large. |
| BatteryDrainModel | Drains the battery. |
| PoorConnectivityModel | Simulates a poor connection either wifi or data. |
| RandomConnectivityModel | Changes wifi and data connection unpredictably. |
| FuzzGPSModel | Turns GPS on or off unpredictably. |

> An example *.properties file is included under the fuzzer folder. **The file must be in the directory of the app under test.** 

## Supported Commands

| Declaration  | Description   |
|---|---|
| [@Wifi](#wifi-connection-status)  | Enable or disable the internet connection using WiFi |
| [@Data](#mobile-data-connection-status)  | Enable or disable the internet connection using Mobile Data  |
| [@Gps](#gps-connection-status) | Enable or disable GPS |
| [@GeoFix](#geofix)  | Set the geographic location of the device (longitude, latitude)  |
| [@Permission](#permission-provider)  | Grant the app package a "dangerous permission"  |
| [@BatteryOptions](#battery-options)  | Set the battery level and the charging status of the device  |
| [@ScreenSize](#screen-size-set)  | Set the screen dimensions (width, height)  |
| [@Density](#screen-density)  | Set the screen pixel density  |
| [@Orientation](#screen-orientation)  | Set the orientation of the device  |

#### Screen Options

- ##### Screen Size Set 
	
	@ScreenSize ( **width** = _[integerValue]_,  **height** = _[integerValue]_ )
	
   	Set the screen dimensions of the target device. This command is equivalent to:
	
		adb shell wm size [width]x[height]
	
- ##### Screen Density

 	 @Density ( **density** = _[integerValue]_ )

	Set the screen density of the target device. This command is equivalent to:
	
		adb shell wm density [density_value]
	
- ##### Screen Orientation
	
	 @Orientation ( **OrientationOptions** )
	
	Set the screen orientation of the target device. This command is equivalent to:
	
		adb shell content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:[user option]
	

#### GeoFix

- ##### Change GPS coordinates

	@GeoFix ( **lat** = _[doubleValue]_ , **longt** = _[doubleValue]_ )
	
	Set the latitude and longtitude of the emulator using android telnet command:
	
		telnet localhost [target_port]
		geo fix [longtitude] [latitude]

#### Battery Options

- ##### Charging Status

	@BatteryOption ( **plugged** = _[booleanValue]_ )
	
	Set the charging status of the emulator to true or false using the equivalent 
		adb command:
		
			adb shell dumpsys battery set ac 1|0
			
	_default value is **true**._	

- ##### Battery Level
	
	@BatteryOption (  **level** = _[integerValue]_ )
	
	
	Set the level of the battery using the equivalent adb command: 
		
		adb command dumpsys battery level [1-100]
		
	_default value is **100**._	
 
- ##### Combination
	
	Both command options can be combined into one 
	
	e.x @BatteryOption ( plugged = [booleanValue] , level = [integerValue] )

#### Connectivity Options

- ##### Wifi Connection Status

	@Wifi ( **enabled** = _[NetworkAdapterStateType]_ )
	
	Disables or enables the wifi connection of the emulator using the equivalent adb command:

	shell svc wifi [enable|disable]

- ##### Mobile Data Connection Status

	@Data ( **enabled** = _[NetworkAdapterStateType]_ )
	
	Disables or enables the mobile data connection of the emulator using 
	the equivalent adb command:
	
		shell svc data [enable|disable]

- ##### GPS Connection Status

	@Gps ( **enabled** = _[NetworkAdapterStateType]_ )

	Disables or enables the GPS connection of the emulator using the equivalent adb command:

		shell settings put secure location_providers_allowed [+gps|-gps]
	
	
#### Permission Provider

- ##### Grant Permission Runtime

	@Permission ( **type** = _[stringValue]_ )

	Grants permission for a specific android permission type. This command is best used for bypassing 
	the user input for granting explicit permissions. The requested permission to grant must also be present into 
	the manifest file of the application

	Usage example: 
		
		@Permission ( type = "android.permission.BODY_SENSORS" )

## How to use

The easiest way to understand how to use the Barista Framework is to think of 
of the state of the emulator before running a test.

If you want to test a feature of your app that depends on 
a specific state of the device, then you add the appropriate annotations that will build this state
for you at test time.

- ### Android Test Example

	````
	@Test
	@Wifi(NetworkAdapterStateType.ENABLED)
	public void enterExampleActivityWithWiFiOff() {
		...
	}

	@Test
	@Data(NetworkAdapterStateType.ENABLED)
	public void emableWifiPopupCloses() {
		...
		Barista.setWifiState(NetworkAdapterStateType.ENABLED);
		...
	}
	````

- ### Fuzzing with Barista

	````
	gradlew :app:startBaristaFuzzer --events="100" --throttle="100" --epochs="4" --config="fuzzer/fuzzerTest1.properties" --parallelRun --stacktrace
	````

## Common problems

1. Barista cannot find the `adb` executable in system path: Ensure that you set the path of android binaries to `/etc/profile` or other global configuration files.