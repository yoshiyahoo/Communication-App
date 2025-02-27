For pushing to repo:
```
	git add <somefilename>
		- adds files to be ready for commit
		- if you use . instead of somefilename it adds all files that can be added in current directory

	git commit -m "some message"
		- commits added files to be ready for push
		- you can check if your commit worked before push by using "git log" command

	git push origin <branchName>
		- to push commits to remote repo for that branch
		- add -u after push once to beable to check for updates properly (only need to do this once)
```
For pulling from repo:
```
	git pull origin <branchName>
		- checks/updates from remote repo
		- add -u after push once to beable to check for updates properly (only need to do this once)
```
Notes:
	*General recommendation is to pull before you start working and before you push to repo!!!
