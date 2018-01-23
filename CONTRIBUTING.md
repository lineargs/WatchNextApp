# Contributing

When contributing to this repository, please first discuss the change you wish to make via issue,
email, or any other method with the owner of this repository before making a change.

## <a name="requests"></a> Bugs, Features

### <a name="issue"></a> Found an Issue or Bug?

If you find a bug in the source code, you can help us by submitting an issue to our
[GitHub Repository][github-issues]. Even better, you can submit a Pull Request with a fix.

**Please see the [Submission Guidelines](#submit) below.**

### <a name="feature"></a> Missing a Feature?

You can request a new feature by submitting an issue to our [GitHub Repository][github-issues].

If you would like to implement a new feature then consider what kind of change it is:

* **Major Changes** that you wish to contribute to the project should be discussed first in an
  [GitHub issue][github-issues] that clearly outlines the changes and benefits of the feature.
* **Small Changes** can directly be crafted and submitted to the [GitHub Repository][github]
  as a Pull Request.

## <a name="submit"></a> Issue Submission Guidelines
Before you submit your issue search the archive, maybe your question was already answered.

If your issue appears to be a bug, and hasn't been reported, open a new issue. Help us to maximize
the effort we can spend fixing issues and adding new features, by not reporting duplicate issues.

The "[new issue][github-new-issue]" form contains a number of prompts that you should fill out to
make it easier to understand and categorize the issue.

In general, providing the following information will increase the chances of your issue being dealt
with quickly:

* **Overview of the Issue** - if an error is being thrown a stack trace helps.
* **Motivation for or Use Case** - explain why this is a bug for you.
* **Reproduce the Error** - where possible provide a live example.
* **Related Issues** - has a similar issue been reported before?
* **Suggest a Fix** - if you can't fix the bug yourself, perhaps you can point to what might be
  causing the problem (line of code or commit).

## <a name="submit-pr"></a> Pull Request Submission Guidelines
Before you submit your pull request consider the following guidelines:

* Search [GitHub](https://github.com/lineargs/watchnext/pulls) for an open or closed Pull Request
  that relates to your submission. You don't want to duplicate effort.
* [Fork][fork] this repository
* Make your changes in a new git branch:

    ```shell
    git checkout -b my-awesome-fix-branch master
    ```

* Create your patch commit.
* Follow our [Code of conduct][coc].
* Commit your changes using a descriptive commit message:

    ```shell
    git commit -a
    ```
* Push your branch to GitHub:

    ```shell
    git push origin my-awesome-fix-branch
    ```

* In GitHub, send a pull request to `WatchNextApp:master`.

* If we suggest changes, then:

  * Make the required updates.
  * Commit your changes to your branch (e.g. `my-awesome-fix-branch`).
  * Push the changes to your GitHub repository (this will update your Pull Request).

    You can also amend the initial commits and force push them to the branch.

    ```shell
    git rebase master -i
    git push origin my-fix-branch -f
    ```

    This is generally easier to follow, but seperate commits are useful if the Pull Request contains
    iterations that might be interesting to see side-by-side.

#### After your pull request is merged

After your pull request is merged, you can safely delete your branch and pull the changes
from the main (upstream) repository:

* Delete the remote branch on GitHub either through the GitHub web UI or your local shell as follows:

    ```shell
    git push origin --delete my-awesome-fix-branch
    ```

* Check out the master branch:

    ```shell
    git checkout master -f
    ```

* Delete the local branch:

    ```shell
    git branch -D my-fix-branch
    ```

* Update your master with the latest upstream version:

    ```shell
    git pull --ff upstream master
    ```
***That's it! Thank you for your contribution!***


[github]: https://github.com/lineargs/WatchNextApp
[github-issues]: https://github.com/lineargs/WatchNextApp/issues
[coc]: https://github.com/lineargs/WatchNextApp/blob/master/CODE_OF_CONDUCT.md
[github-new-issue]: https://github.com/lineargs/WatchNextApp/issues/new
[fork]: https://github.com/lineargs/WatchNextApp/fork
