# `uk.ac.diamond.gda.hc`

This project is a GDA plugin for the EMBL's HC Control Software. It allows MJPEG video sources to be processed by the HC software.

## Getting started

Copy `build.properties.template` as `build.properties`, and set the properties in the new file appropriately.

`gda.root` is only required if you want to use Ant to compile the plugin or run the HC software.

## Building the plugin

### Compiling with Eclipse

To build this project using Eclipse, you will need these plugins in your workspace:

 * `uk.ac.gda.core`
 * `uk.ac.gda.video`
 * `uk.ac.gda.video.mjpeg`

### Compiling with Ant

Run `ant compile`.

## Deploying the plugin to the HC software

Run `ant deploy`. This copies the class files and properties file into the HC software directory.

## Running the HC software

Running `ant run` will run the HC software with the appropriate entries on the classpath so that the plugin works.

You can use any mechanism for running the HC software, as long as the required entries are included on the classpath. The required entries can be found in the Ant build file (`build.xml`).

## Using the plugin

The plugin's `GdaMjpegImageSource.properties` file specifies a list of sources using this format:

    sourcename=http://url/of/video/source

The plugin adds **GdaMjpegImageSource** to the "Type" menu in the HC software's "Software Configuration" dialog box.

The "Source" should be set to one of the source names in the properties file. The plugin will connect to the corresponding URL.
