# KliniSys - Operation Slot Manegement Tool

> Klinisys makes the planning of operations easier
>

### Version
1.0 - June 2015

### Dependencies

Please install those software before running the init script
* Docker
* Bower
* Gradle

### Installation-Guide
The Software is based on open source technologies, so deploying it on Linux based systems is the easiest way to get the tool running.  
It's no problem to run the software on Windows or Mac too, but the setup is a little bit more complicated.  
  
First it's necessary to install Docker, so that container can be handled.  
More information about the installation of docker can be found here: https://docs.docker.com/installation

After running the docker Server it's necessary to know the IP Address of the Docker host.
  
To start the application simple call the shell-script in the main directory.


```sh
$ sh startDocker.sh <IP-Address of Docker host>
```
After initializing all containers the web-application can be opened on Docker's IP Address on Port 80

### Initializing Demo Data
For initializing Demo Data simple call the generating Web-Script on Docker's IP on port 9000
```sh
$ wget http://<IP-Address of Docker host>:9000/demo
```
### Troubleshooting
There are some complications on certain Command Line Tools while using Microsoft Windows.  
For solving most of those problems please use Window's PowerShell and make sure, that the git and bower bin folders are set into the Environment Variables.
  
Feel free to contact us when having troubles during the installation: dse@kraenkl.com 


### Authors
Thomas Hieﬂl, Lukas Kr‰nkl & Michael Mittermayr

### License
MIT