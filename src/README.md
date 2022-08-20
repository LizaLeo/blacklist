## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## General info
This project find name matches over black list.
Currently realises 
* Match whole word algorithm
* Match words with ommited space
* Match initials   
	
## Technologies
Project is created with Java 11
	
## Setup
Build jar to run this project. 

Resourse files like blacklist.txt and noiselist.txt will be also copied to jar directory.

jar takes 4 parameters:
* name (required)
* black list file name (by default "blacklist.txt")
* noise words list file name (by default "noiselist.txt")
* alarm percentage - start from this match percentage matched name will be added to results (by default 50)

As long as files blacklist.txt and noiselist.txt are outside of jar - they can be modified to add new blacklist names or noise words.