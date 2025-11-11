# capstone-project-hooked

# Description:
Hooked is a website application that will allow a user to register with the site and enter
specific information of the fish they catch. Information such as species, length, weight, type of bait used, 
date and location can be entered and stored on the website for future reference by the user. The user will 
have the ability to share their entries with other site users or keep the information for their own records.

# Installation:
Prerequisites
JDK 21 or higher, Git, Maven, Local Tomcat server
1) Set location to clone bash: cd project-folder
2) Clone the repository bash: git clone git@github.com:it-sd-capstone/capstone-project-hooked.git
3) Set folder directory: cd capstone-project-hooked
4) Build the project bash: ./mvnw clean package -DskipTests
5) Open project in IntelliJ
6) Create Tomcat Server > Local
7) Add artifact > War exploded
8) Run Tomcat
9) Access the site http://localhost:8080/

May need to do file -> project structure -> artifacts -> + -> directory content -> templates

# Testing
Description: Automated tests are included in `MainTest.java` file under `edu.cvtc.hooked` package. These tests verify that the database connection and basic query functionality works.
1. Open the project.
2. Navigate to `src/test/java/edu/cvtc/hooked/MainTest.java`.
3. Right-click the file and select Run MainTest.
4. Make sure it passes both tests.

# Deploy to AWS Elastic Beanstalk
1) Build app.war bash: ./mvnw clean package -DskipTests
2) In Elastic Beanstalk > Upload and deploy > app.war
3) Use next version label (Hooked-0.0.x) > click deploy
# Website URL
http://Hooked-App.eba-psp7th8j.us-east-1.elasticbeanstalk.com