# capstone-project-hooked

# Description:
Hooked is a website application that will allow a user to register with the site and enter
specific information of the fish they catch. Information such as species, length, weight, type of bait used, 
date and location can be entered and stored on the website for future reference by the user. The user will 
have the ability to share their entries with other site users or keep the information for their own records.

# Installation:
Prerequisites
JDK 21 or higher, Git, Maven, Apache Tomcat 10.1.49
1) Set location to clone bash: cd project-folder
2) Clone the repository bash: git clone git@github.com:it-sd-capstone/capstone-project-hooked.git
3) Set folder directory: cd capstone-project-hooked
4) Build the project (mvnw is a Maven wrapper script which downloads the correct Maven version if not installed)
bash: ./mvnw clean package -DskipTests
5) Open project in IntelliJ: Open IntelliJ > File > Open > select the folder: capstone-project-hooked
6) Add artifact: File > Project Structure > Select Artifacts > Click + (add) > Web Applications > Exploded >
Choose From Modules > select module (hooked) > Click OK.
7) Configure Tomcat in IntelliJ: Run > Edit Configurations > Click + (add new configuration) > Tomcat Server > Local
Under Application Server select Apache Tomcat 10.1.49 Go to Deployment tab and click + (add) > Artifact
select (hooked:war exploded) > click Apply and then OK.
8) Run Tomcat: Select Tomcat 10.1.49 > Click Run
9) Access the application: open http://localhost:8080/hooked_war_exploded/ in your browser.

# Testing
Description: Automated tests are included in `MainTest.java`, `UserDaoTest.java` and `UserTest.java` file under `src/test/edu.cvtc.hooked` package.
These tests verify that the database connection and basic query functionality works.
1. Open the project.
2. Navigate to `src/test/edu.cvtc.hooked/MainTest.java`.
3. Right-click the file and select Run `MainTest`.
4. Make sure it passes both tests. 
5. Navigate to `src/test/edu.cvtc.hooked/UserDaoTest.java`.
6. Right-click the file and select Run `UserDaoTest`.
7. Make sure it passes both tests.
8. Navigate to `src/test/edu.cvtc.hooked/UserTest.java`.
9. Right-click the file and select Run `UserTest`.
10. Make sure it passes all 8 tests.

# Deploy to AWS Elastic Beanstalk
1) Build app.war bash: ./mvnw clean package -DskipTests
2) In Elastic Beanstalk > Upload and deploy > app.war
3) Use next version label (Hooked-0.0.x) > click deploy
# Website URL
http://Hooked-App.eba-psp7th8j.us-east-1.elasticbeanstalk.com

# Admin Login Information
Description: Logging in as admin gives edit/delete permissions for Species, Location & Bait database along
with overall added catch table database. Individual user can edit/delete their own entries only.
Admin Login Details:
Username: admin
Password: admin
