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
* Maven 2
* GitHub account
* Personal Access Token (PAT)

### Build instructions
To build the project, go into the root directory of the project and run `mvn test` in your terminal. This will build the project and run all tests using JUnit.

### Webhook
Our server exposes the port `8012`, and we recommend using `ngrok` to tunnel incoming requests. The server triggers the build using webhook requests from GitHub.

### Configuring a Personal Access Token 
This server sends requests to the GitHub REST API, which requires a Personal Access Token. 
You can generate one by doing these [steps](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#creating-a-personal-access-token-classic).
To use the PAT in your current session, set the PAT as an environment variable in your command line:
* Mac OS X or Linux: `export ci_token=<YOUR TOKEN HERE>`
* Windows: `set CI_TOKEN=<YOUR TOKEN HERE>` (Command Line) or `$Env:CI_TOKEN="<YOUR TOKEN HERE>"` (PowerShell)


## Running the server ##
(We assume that you have installed Maven and set the path variable)
1. Set up port forwarding and Webhook for your repo
2. Set up your personal access token (see section above)
2. run `mvn test` in the root of your project to trigger the building and testing process locally
3. run `mvn clean compile exec:java` to start the server
4. Go to `localhost:8012` in your browser
5. Push a commit to your repo

## Features

### Compilation 
Our server automatically tests all corresponding repo tests using Maven Invoker Builder. 
The compilation class generates build results and logs. This part was unit tested using JUnit, where the test tries to build a project locally,
and returns the results of the build.

### Cloning the repo
Our server will automatically clone and checkout the correct commit in the repo using the method cloneRepo and checkoutCommit, implemented with Jgit in the GitRepo.java file. 
The cloned repo will be stored in a temporary folder which will be deleted once the CI server is finished. 
This feature has tests where it tries to clone this repo with valid and invalid data.

### Execution of automated tests
Our server will analyze the HTTP request received from the Webhook, extract the branch, clone URL, and commit IDs. 
These details will be used to clone the git repository and add build history. 
The compilation process will then test the repository and update the relevant notification and history log accordingly.
To test this feature, we have an invalid and valid indata test that runs the process on a past commit in this repo.

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

It is available **[here](https://composed-cheaply-liger.ngrok-free.app/buildhistory)**.


## Assessing our Team
According to the checklist on p.52, we find our team to be in state "Performing". This is due to the fact that our team consistently delivers on our goals, achieving the first criteria of the list.
Furthermore, when we discover problems we can easily address them within the group and change our workflow, meaning that we "continuously adapt to the changing context" 
and "identifies and addresses problems without outside help". 
Additionally, we have regular meetings where we discuss the current state of everyone's progress and also potential solutions to any problems anyone might have,
this allows us to have an effective process which minimizes any mistakes, fulfilling the last two criteria of the checklist.

In order to progress to the next state, Adjourned, we would have to hand over our responsibilities to another group or disband our group. 

### Contributions
**Emil Hultcrantz**
* Implemented GitRepo and associated tests
* Code reviewing
* Bugfixing

**Charlotta Johnson** (pair programmed with Filippa)
* Implemented commit status update and wrote tests for it
* Wrote draft of README
* Code reviewing
* Bugfixing
* Generated Javadoc 

**Tianning Liang**
* Implemented MavenInvokerBuilder and TestAutomationHandler, and associated tests
* Code reviewing
* Bugfixing

**Anna Mårtensson**
* Implemented RequestParser and BuildHistoryManager
* Bugfixing the final product
* Merging the final product into `main`
* Code reviewing
* Bugfixing

**Filippa Nilsson** (pair-programmed with Charlotta)
* Implemented commit status update and wrote tests for it
* Wrote draft of README + finishing touches
* Code reviewing
* Bugfixing
