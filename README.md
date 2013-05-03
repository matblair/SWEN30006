SWEN30006
=========

Group repository for SWEN30006 Project
Managed using github available at https://www.github.com/matblair/SWEN30006

INSTALLING GITHUB
=================

If you have installed xCode command line utils on mac then you should already have git installed (check by typing git in terminal). If you haven't yet done that, please download xCode and install it, then go into the preferences and select install command line utilities).

USING GITHUB 
============

In order to push to the repository you will have to have a github account, which is free to register for at www.github.com. Once you have registered for one, let me know and I will add you to the contributors and then you will be able to commit. I've had to do this in order to prevent random members of the public from commiting to our repository.

Usage is very straight forward and operates very similarly to svn. To clone a git repository to your computer (i.e. pull down the master branch) we type:

        git clone https://www.github.com/matblair/SWEN30006 $path_to_desired_location
        
The difference lies in how we branch and merge. As a default security protocol we should never be working on the master copy (i.e. the trunk) we should all create our own branch on our own computer so that we don't necessarily corrupt the master. Only known good working code should be published to the trunk and if you publish something that breaks anything to the trunk I will chase you down. 
