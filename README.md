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

        git clone https://www.github.com/matblair/SWEN30006 path/to/localdirectory
        
The difference lies in how we branch and merge. As a default security protocol we should never be working on the master copy (i.e. the trunk) we should all create our own branch on our own computer so that we don't necessarily corrupt the master. Only known good working code should be published to the trunk and if you publish something that breaks anything to the trunk I will chase you down. 

Ensure that you are in 'path/to/localdirectory' before continuing. This should be the directory you are working with for the project in eclipse.

This is how we do it: first checkout master (which should do nothing but will ensure you are in the correct directory)
        
        git checkout master
        
Then we assign a new branch and switch to it

        git checkout yourname
        
This will then track the remote branches I have setup on the server
        
I have created the branches on the server already so you can use those, they are james for Jamie, joana for Joana and champ for Champ. 
        
After which we have to add everything to your new branch.

        git add .
        
Now you can work with the branch you have, and it shouldn't affect the master branch. You can commit to that branch as follows

        git commit -a -m "your message here"
        
Please ensure you add decent messages to your commits so that we can track what is happening and who has done what.
Once you have commited you should be able to push your branch to the server. First we have to set git to only push current branch:

        git config push.default current

Then that will commit your branch to the server. If you are 100% confident there are no issues with your code you can merge with master, which we do as so.

        git checkout master
        git merge yourname

This should bring up git fastforward merge, which should do everything for you. If it doesn't then I'd rather you try merge by hand, the way that the project is set up we should never be working on the same thing at the same time so this is easier than stuffing up the git master by not using the diff merge correctly.

Once you have done the above process and the master branch properly runs, I'm happy for you to commit to the master branch like so:

        git commit -a -m "your message about what you changed here"
        git push
        
Then switch back to your current branch

        git checkout yourname
        
And you should be good to go. I will be maintaining a backup of the git repository (I have a shell script set up to download the master every 4 hours and keep a revised copy of that on a separate hard drive) so if anything does go dramatically wrong we can rever to that but this seems to be the best method.

A NOTE ON ECLIPSE AND GIT
=========================

DON'T USE ECLIPSES BUILT IN GIT MANAGEMENT SYSTEM. It sucks. Really really sucks. It uses git-svn and that is old, deprecated and stupid. It breaks merges and often loses things. The easiest way is to have your workspace set to the same directory that you are cloning your git branch into. (i.e. my workspace is ~/SWEN30006/Projects and my git checkout is at ~/SWEN30006/Projects/Portal2D) Then you can set up your project by simply adding a project with the same name as the clonedrepo and you will be good.



