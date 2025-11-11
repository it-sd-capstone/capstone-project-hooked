# capstone-project-hooked

# Description:
Hooked is a website application that will allow a user to register with the site and enter
specific information of the fish they catch. Information such as species, length, weight, type of bait used, 
date and location can be entered and stored on the website for future reference by the user. The user will 
have the ability to share their entries with other site users or keep the information for their own records.

# Installation:
Prerequisites
JDK 21 or higher, Git
1) Set location to clone bash: cd project-folder
2) Clone the repository bash: git clone git@github.com:it-sd-capstone/capstone-project-hooked.git
3) Set folder directory: cd capstone-project-hooked
4) Build the project bash: ./mvnw clean package -DskipTests
5) java -jar target/app.jar
6) Access the site http://localhost:5000

May need to do file -> project structure -> artifacts -> + -> directory content -> templates

# Testing
Description: Automated tests are included in `MainTest.java` file under `edu.cvtc.hooked` package. These tests verify that the database connection and basic query functionality works.
1. Open the project.
2. Navigate to `src/test/java/edu/cvtc/hooked/MainTest.java`.
3. Right-click the file and select Run MaintTest.
4. Make sure it passes both tests.

# Deploy to AWS Elastic Beanstalk
1) java -jar app.jar  (optional - view page before new upload - browse: http://localhost:5000)
2) ./deploy.sh
3) In Elastic Beanstalk > Upload and deploy > bundle.zip
4) Use next version label (Hooked-0.0.x) > click deploy
# Website URL
http://hooked-app-env.eba-psp7th8j.us-east-1.elasticbeanstalk.com/