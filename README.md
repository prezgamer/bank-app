# Simple Bank App (Java + Maven + Jenkins)

A small console bank app used to practise CI/CD. Features: open accounts, deposit, withdraw,
transfer between accounts, balance, statement. Money uses `BigDecimal`. 13 unit tests (JUnit 5).

## Open in VS Code

1. Install the **Extension Pack for Java** (VS Code will suggest it when you open the folder).
2. Install **JDK 17 or newer** and **Maven** (check with `java -version` and `mvn -v`).
3. File > Open Folder > select `bank-app`.

## Run locally

```powershell
mvn clean verify                      # compile + run tests + build the jar
java -jar target/bank-app-1.0.0.jar   # start the console app
```

## Layout

```
pom.xml                         Maven build (Java 17, JUnit 5)
src/main/java/com/example/bank  Account, Bank, Main, InsufficientFundsException
src/test/java/com/example/bank  AccountTest, BankTest
Jenkinsfile                     the CI/CD pipeline
jenkins-docker/Dockerfile       Jenkins image with Maven installed
```

## Run it through Jenkins

1. Build a Jenkins image that has Maven (the plain image does not):

   ```powershell
   docker stop jenkins; docker rm jenkins
   docker build -t jenkins-maven ./jenkins-docker
   docker run -d --name jenkins -p 8080:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home jenkins-maven
   ```

   The `jenkins_home` volume keeps your Jenkins setup, so you will not have to redo it.

2. Push this project to a GitHub repository:

   ```powershell
   git init
   git add .
   git commit -m "Initial bank app"
   git branch -M main
   git remote add origin https://github.com/<you>/bank-app.git
   git push -u origin main
   ```

3. In Jenkins: **New Item > Pipeline > Pipeline script from SCM > Git**. Paste the repo URL,
   set the branch to `*/main`, script path `Jenkinsfile`, then Save and **Build Now**.

You should see the stages Checkout, Build, Test, Package and Deploy go green, the test results on the
build page, and `bank-app-1.0.0.jar` under the build's artifacts.

## Things to try for learning

- Break a test on purpose (change an expected value), push, and watch the Test stage fail.
- Add a new feature plus a test (for example, interest or a daily withdrawal limit) and push.
- Make Jenkins build on every push (GitHub webhook, or poll SCM `H/2 * * * *`).
