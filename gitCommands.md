# Notes

- General recommendation is to pull before you start working and before you push to repo!!!
- You can and should pull from main branch when in a non-main branch every time before you continue working and before you push that non-main branch!!!

# Steps for using branches

Step 1) Make a local branch and switch to it.

Step 2) Make you code and file edits while in the non-main branch.

Step 3) Push the changes to non-main branch.

Step 4) Go to the git repo on GitHub and you should see a pull request option in yellow. Click pull request and then Click merge.

Step 5) Switch to main branch locally and pull main down. You should see your changes there.

Step 6) if done with branch (should be done after each pull request), then delete both the local and remote versions of the branch you pushed.

# Reference area

## Pushing to repo

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

## Pulling from repo

```
git pull origin <branchName>
	- checks/updates from remote repo
	- add -u after push once to beable to check for updates properly (only need to do this once)
```

## Making a local branch

```
git branch <Branch Name>
```

## Switching to local branch

```
git switch <Branch Name>
```

## Deleting local and remote branches

```
- switch to main branch

local:
	git branch -d <Branch Name>

remote:
	git push -d origin <Branch Name>
```

## Seeing existing branches

```
local:
	git branch

remote:
	git branch -r

both local and remote:
	git branch -a
```
