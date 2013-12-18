VisITMeta
=========
VisITMeta is an *experimental* [IF-MAP][1] 2.0/2.1 compliant MAP client written in Java
that visualizes metadata stored on a MAP server.
It supports features like showing a history of all metadata stored on a MAPS, using
filter and search techniques to navigate the metadata as well as some visualization
techniques like animations, highlighting and so on.
Development was done by [Hochschule Hannover (University of Applied Sciences and Arts, 
Hanover)][2] within the [VisITMeta research project][3], (support code 17PNT032) which is 
funded by the german [BMBF (Federal Ministry of Education and Research)][8].

Features
========
The VisITMeta software consists of two main components:

* dataservice:
The dataservice component is responsible for collecting metadata from a MAP server.
Currently works with one subscription, and so records all data it can get with that
subscription.
Metadata is stored via a [`neo4j`][9] database, with timestamps attached to save the
information, when this metadata was created, changed and deleted.

The dataservice component is the only component in VisITMeta, that is talking IF-MAP
with a MAP server.
Every other component, like the visualization component, connects via a `REST`-like
interface to get the current MAP graph, the graph at a specific timestamp or the
changes between two timestamps, as well as a list of all timestamps with the number
of changes (separated into updates and deletes).
This allows arbitrary clients work with the history of metadata collected by the dataservice.

* visualization:
The visualization component is a GUI that displays the information gathered by the
dataservice.
Metadata is rendered a graph, with different colors for different publisher IDs and
highlighting changes in the graph.
It supports navigating through the metadata via panning and zooming, as well as
navigating through all timestamps of the metadata.

The list of visualization features is going to change over the duration of the project.

Building
========
This section describes, how to build VisITMeta from scratch.

Prerequisites
-------------
In order to build VisITMeta with Maven you need to install
[Maven 3][4] manually or via the package manager of your
operating system.

Build VisITMeta
---------------
Now you can build VisITMeta, simply execute:

    $ mvn package

in the root directory of the VisITMeta project (the directory
containing this `README` file). Maven should download all further
needed dependencies for you.
After a successful build you should find a zip-archive called
`visitmeta-distribution-<version>-bundle.zip` in 
`visitmeta-distribution/target`.

Configuration
=============
This section describes the configuration needed to get VisITMeta working.

MAP server configuration
------------------------
To use VisITMeta you need to have a MAP server running. 
Make sure that a basic-authentication user exists in the corresponding
configuration file of your MAP server.
On irond, it would be `basicauthusers.properties`. 

If not present, add the line

    visitmeta:visitmeta

at the end of the file.

VisITMeta configuration
-----------------------
VisITMeta itself has to be configured, too.
Inside the `config` directory, you find all needed configuration files.

Most settings will work just fine in your environment (or when using our
`irondemo` environment []), but you maybe have to adjust at least the following entries:

* config.properties

	* ifmap.auth.url = the URL of your MAP server (default: http://localhost:8443)
	* ifmap.start.type = Type of the start identifier for VisITMeta
	* ifmap.start.identifier = Value of the start idenfitier for VisITMeta

Running
=======
1. Start a MAP server, e.g. irond (Download section at [Trust@HsH website] [2] or via
[Github] [6]

2. Change your working directory to the root directory of the VisITMeta project.

3. Start VisITMeta:

First, you must start the dataservice-component via

	$ sh start-dataservice.sh

Afterwards, you can start the visualization-component (or at any later time, as the 
dataservice is already recording and providing the complete history).
Start the GUI via

	$ sh start-visualization.sh

4. Publish some data:

Within the `scripts` directory, you will find some Shell scripts that work with our
ifmapcli-tools and will publish and delete some metadata, so that you can test the
history-feature of VisITMeta.

Feedback
========
If you have any questions, problems or comments, please contact
	trust@f4-i.fh-hannover.de

License
=======
VisITMeta is licensed under the [Apache License, Version 2.0][7].
The visualization component uses the Java Swing Range Slider source code from [Ernie You][10] ([Github][11]).
The corresponding license is provided with the file LICENSE-swingRangeSlider.txt in the
root-directory of `visitmeta-distribution`.

----

[1]: http://www.trustedcomputinggroup.org/resources/tnc_ifmap_binding_for_soap_specification
[2]: http://www.hs-hannover.de/start/index.html
[3]: http://trust.f4.hs-hannover.de/projects/visitmeta.html
[4]: https://maven.apache.org/download.html
[6]: https://github.com/trustathsh/irond.git
[7]: http://www.apache.org/licenses/LICENSE-2.0.html
[8]: http://www.bmbf.de/en/index.php
[9]: http://www.neo4j.org/
[10]: http://ernienotes.wordpress.com/2010/12/27/creating-a-java-swing-range-slider/
[11]: https://github.com/ernieyu/Swing-range-slider
