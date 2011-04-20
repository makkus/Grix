Grix
====

Grix is an open source Java gui application to help users handle security related tasks within a grid environment. It is open source software and under the GPL license.

Grix can:

* Request and download a x509 certificate
* Export this pem formatted certificate into pkcs format
* Create a local grid proxy
* Subscribe to a VO (via VOMRS, not VOMS!)
* Subscribe to a group within a VO (again via VOMRS, not VOMS!)
* Create a MyProxy credential (with or without VOMS information)

Grix was developed by Markus Binsteiner (except for the VOMS proxy functionality which is developed by Gidon Moont from the Imperial College London,  http://gridportal.hep.ph.ic.ac.uk, thanks again Gidon), it is open source software and under the GPL license.
 
Prerequisites
--------------------

In order to build Grisu from the git sources, you need: 

- Sun Java Development Kit (version ≥ 6)
- [git](http://git-scm.com) 
- [Apache Maven](http://maven.apache.org) (version >=2)


Checking out sourcecode
----------------------------------------

 `git clone git://github.com/makkus/Grix.git`

Configuring Grix to deploy it on your site
----------------------------------------------------------------

The "deployment-config" sub-folder contains all the config/help-files that can be easily modified. Also the certificates.zip and vomses.zip files which contains all the root certificates/vomses files that are supported. Here's a short description in order find out what these files do and how you can change them:

### config.properties
This file constains the most important settings for Grix, like URL of OpenCA and MyProxy server, which modules of Grix to enable/disable and so on. You should not have too many difficulties editing it since it's pretty well documented.

### certificates.zip
This zip archive contains all CA certificates your site trusts. The one important one is the one for the CA that signed your MyProxy? server's host cert, though.

### vomses.zip
This zip archive contains the VOs you want your user to connect to. Every VO has got a vomses file which looks something like "APACGrid" "vomrs.apac.edu.au" "15001" "/C=AU/O=APACGrid/OU=APAC/CN=vomrs.apac.edu.au" "APACGrid"
The first token is the name of the VO, the second one the hostname. Then comes the port of this VO (ask your voms administrator if you are not sure). Now the DN of the hostcertificate of the voms server. To be honest, I don't know what the last token is for.

All containing vomses files are copied into a folder .glite/vomses_available into the users home directory. The user can manage this directory within Grix and move VOs he wants to contact into the .glite/vomses folder.

### org/vpac/grix/html/requested.html
This html file is displayed after a user successfully uploaded his certificate request to the OpenCA server. Change it in a way so that a user learns what to do next (give a list of your site's RAOs, tell to take a drivers license or provide a link to a webpage). Don't include the 

    <html><body> and </body></html> 
    
tags.

### org/vpac/grix/html/infoCandidate.html
This html file is displayed after a user completed phase I of a vomrs registration. You may want to edit the entry in the vomrs server's database ([EditVOMRSDatabase using these instructions] to include the email verification keycode vomrs uses into the email. This keycode is needed in order to sucessfully apply the phase I. We use it also as kind of "I agree with the VOMRS usage policy" tickbox. Again, don't include the <html><body> and </body></html> tags.

OK. Now you have to zip the content of your temporary folder again and give it a .jar extension. Take care not to include the folder itself because otherwise it won't work.

In order to deploy Grix to your users, you have to upload the newly created jar along with a recent copy of the bouncy castle security provider jar ( http://www.bouncycastle.org/latest_releases.html named something like bcprov-jdk15-xxx.jar) which needs to be renamed to bcprov.jar to a publicly available location. Tell your users to download both files into the same directory and either double click the grix jar or run it via commandline:

   java -jar <the name you gave your archive>.jar
   



Building Grix using Maven
------------------------------------------

To build one of the above modules, cd into the module root directory of the module to build and execute: 

    cd Grix
    mvn clean install

Now you should have a subdirectory called "target" in your current directory. To start Grix, you first have to copy the "bcprov.jar" into the same directory. Now, type

    java -jar grix.jar
    
and if everything is right you should be able to see the Grix main panel.

If you want to deploy Grix using Java Webstart or via an Windows installer, drop me an email and I can give you instructions on how to do that.

Since Grix is not deployed at any other Grid all of this is not really that well tested. So if you experience problems or spot something that's not working, just contact me.

