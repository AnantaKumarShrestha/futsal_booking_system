The main purpose of my Java-Project is to make users easy to book futsal.
1) User, Futsal-Owner, Admin can signup, sign in and logout 
2) Users can book futsal, cancel futsal, see the list of futsals.
3) Admins can see the list of users, futsalOwners and futsals which are registred in the application.
4) Futsal Owners have to send the Futsal Registration Request to Admin to register futsal.
5) Admins have the authority to accept and reject the futsal registration request, which is sent by the futsal Owner.

Strategy of git pull request 
Most of the time I have created new branches for every new feature. After completing the feature, I pushed code into Github and created Pull-Requests to merge in the main branch, and after merging the feature branch in the main branch in Github then I pull the code in the local git main branch to update the local main branch.

Challanges while making project

Most of the time I have created the git branch in a linear way, so I didnt face Challange while merging, but I have created the jwt_feature branch at the middle of the project and worked a lot in that branch. So while merging, that branch Main was merged with many Git-Branches, so there was a Merge-Conflict

Overcomed the challanges

So I overcomed that Challange by pulling the main branch of cloud into the local main branch and merging the jwt_feature branch in the main branch and removed the merge conflict. After merging the jwt_feature.I have pushed the code into Github and created a Pull-Request and merged the jwt_feature branch in the main branch.
