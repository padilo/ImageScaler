ImageScaler
===========

This project only pretends to be a test of Apache Camel.

Listens to a folder and scales images to 1024xAAA or AAAx1024, where AAA<1024 with same aspect ratio.

Packaging
=========

Just do a mvn package, it will generate 2 jars:

- ImageScales-X.X.X.jar
	Basic jar.
- ImageScales-X.X.X-jar-with-dependencies.jar
	Jar with all embedded dependencies.

How it works 
============

It will listen to a subfolder input for any files recursively, and will try to scale it. The files in
this folder will be eliminated as it is processed, but a original copy will be available.

The file will be at:

- In /output subfolder will be the scaled version of the image.
- In /input/.error will be any file that cannot be scaled.
- In /input/.copy will be the original file.

Usage
=====

java -jar ImageScales-X.X.X-jar-with-dependencies.jar [folder]

Folder param is optional by default will use /imagescaler.

