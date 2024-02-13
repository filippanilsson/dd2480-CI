# dd2480-CI - Continuous Integration Server

## Description

This is a continuous integration server written in Java, that can test Maven projects. It is triggered by pushes to the repository using Webhooks. The server can clone the repo, build the project and test it, as well as displaying commit statuses of the build on GitHub.

Furthermore, the CI server saves the build history, which persists even if the server is rebooted.

## Overview
The CI server is a Maven project and contains three top folders inside the DD2480-CI project folder:
* **src**: contains the source code of the server.
  * **main**:
    * **BuildHistoryManager**:Manages the build history of a continuous integration server
    * **BuildStatus**: An enum containing the different types of commit statuses
    * **ContinuousIntegrationServer**: Houses the server, based on the skeleton found [here](https://github.com/KTH-DD2480/smallest-java-ci).
    * **GitRepo**: Clones the repo to the server
    * **GitStatusUpdate**: Sends a POST request to the GitHub REST API to update a commit’s status
    * **MavenInvokerBuilder**: Executes the build for Maven repository and retrieves the build output and result status
    * **RequestParser**: Checks if an HTTP request represents a GitHub push event and extracts information needed for build and notification.
    * **TestAutomationHandler**: Executing the automated tests of the commit on the branch where the change has been made
      and sends notification to GitHub
  * test: contains all tests for the created classes


## Build requirements
* JDK 21
* GitHub account
* Personal Access Token (PAT)

### Build instructions
To build the project, go into the root directory of the project and run `mvn test` in your terminal. This will compile the project and run all tests using JUnit.

### Webhook
Our server exposes the port `8012`, and we recommend using `ngrok` to tunnel incoming requests. The server triggers the build using webhook requests from GitHub.

### Configuring a Personal Access Token 
This server sends requests to the GitHub REST API, which requires a Personal Access Token.

Create a file called `.env` in the root of your project, then follow these [steps](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens) to generate a personal access token (give it `repo` access). Then, put the line `ci_token=<YOUR TOKEN HERE>` into the .env file.

## Running the server ##
1. Set up port forwarding and Webhook for your repo
2. run `mvn test` in the root of your project to trigger the building process locally
3. run `java -jar ` to start the server
4. Go to `localhost:8012` in your browser
5. Push a commit to your repo

## Features

### Compilation 
...

### Cloning the repo
Our server will automatically clone and checkout the correct commit in the repo using the method cloneRepo and checkoutCommit, implemented with Jgit in the GitRepo.java file. The cloned repo will be stored in a temporary folder which will be deleted once the CI server is finished.

### Execution of automated tests
...

### Update Github status
Our server will report a build’s status (`success`, `error`, `failure` or `pending`) to GitHub by sending a `POST` request to the REST API. To view the status you can simply click on the commit.
To accomplish this, we have generated a Personal Access Token which we placed in a .env file to authenticate to the REST API.
We created an enum which contains all possible build states (`Buildstatus.java`), and when we send a `POST` request, we create a JSONObject which contains all necessary headers and payload to be able to set a status on the commit being pushed to the repo.
We used Apache's Http dependencies to create `POST` requests, as well as Dotenv. 

To unit test this feature, we have created two simple JUnit tests, one for invalid SHA input and one for valid SHA input.

### Viewing build History
The CI server stores information about previous builds even if the server is rebooted, every entry contains:
* SHA id
* timestamp
* build status
* build logs

It is available **[here](https://grouper-valid-hugely.ngrok-free.app/buildhistory)**.

## Assessing our Team
According to the checklist on p.52, we find our team to be in state "Performing". This is due to the fact that our team consistently delivers on our goals, achieving the first criteria of the list.
Furthermore, when we discover problems we can easily address them within the group and change our workflow, meaning that we "continuously adapt to the changing context" 
and "identifies and addresses problems without outside help". 
Additionally, we have regular meetings where we discuss the current state of everyone's progress and also potential solutions to any problems anyone might have,
this allows us to have an effective process which minimizes any mistakes, fulfilling the last two criteria of the checklist.

In order to progress to the next state, Adjourned, we would have to hand over our responsibilities to another group or disband our group. 

### Contributions
**Emil Hultcrantz**
* 

**Charlotta Johnson** (pair programmed with Filippa)
* Implemented commit status update and wrote tests for it
* Wrote draft of README
* Code reviewing
* Bugfixing

**Tianning Liang**
*

**Anna Mårtensson**
*


**Filippa Nilsson** (pair-programmed with Charlotta)
* Implemented commit status update and wrote tests for it
* Wrote draft of README
* Code reviewing
* Bugfixing
