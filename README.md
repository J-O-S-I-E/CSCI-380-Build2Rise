# Build2Rise – Founder & Investor Networking Platform

Where innovation meets opportunity.  
Build2Rise empowers founders and investors to discover each other through smart AI-driven matching, visual storytelling, and meaningful professional connections — all in one intuitive, social-inspired experience.

| Member | Roles | Specific Contributions |
|--------|-------|----------------------|
| **Tanha** | Project Lead, Diagram Designer, UI Contributor, Front-End Contributor | Helped manage the project and designated tasks; Contributed to the core app concept and feature ideation; Participated in app theme color research, brainstorming and final selection; Assisted in creating the ER diagram and fully developed the use case diagram; Contributed to designing Figma wireframes; Supported front-end development through component creation and UI refinement |
| **Zarrin** | Documenter, UI Contributor, Front-End Developer, Back-End Developer | Contributed to the documentation of DF diagrams, Use Case diagram, and ER diagram; Contributed to the documentation of Figma design; Set up the environment within Android Studio; Contributed to front-end and back-end development of features; Expanded and updated the database schema to support new engagement features |
| **Chelsea** | Documenter, Diagram Designer, UI Contributor, Front-End Developer | Contributed to the documentation of DF diagrams and ER diagram; Contributed in the creation of the ER diagram; Contributed to the front-end development of features; Developed custom AI-generated visuals for the welcome and login interfaces; Participated in app theme color research, brainstorming and final selection |
| **Joseann** | Documenter, UI/UX Designer, Diagram Contributor | Feature brainstorming informed by competitive analysis and UI/UX research; Generated multiple logo concepts and visual assets for the mobile app; Created and maintained the Figma file and design libraries with prototyping; Set up the GitHub repository and initialized the core Android Studio environment; Contributed to the documentation of ERD and User Manual |
| **Evelyn** | Documenter, UI/UX Designer, Front-End & Back-End Contributor, Database Integration | Contributed to the documentation of system architecture, DF diagrams and ER diagrams; Contributed to creating UI/UX Figma design; Helped set up Supabase integration, authentication and image/video storage; Contributed to front-end development of features; Structured backend/IntelliJ setup for team access and collaboration; Helped build the backend and integrate with the front-end UI |



# 📌 About:

Build2Rise is a platform that combines the structured, professional features founders and angel investors need with the engaging, visual posting style people are used to on social media. It gives founders a dedicated space to showcase their startups and attract investor attention, while giving investors an easy way to discover, evaluate, and connect with promising projects/startups.



With features like tailored feeds, smart matching with AI, targeted search tools, and connection-based messaging, Build2Rise streamlines how founders and investors discover each other, interact, and build meaningful professional relationships, all within one intuitive platform.



#📱 User Guide: 

## How to install and set up: 

Open the Build2Rise download link or APK file provided by the development team.

Open the app and select “Get Started” to sign up as a first time user. Returning users can log in to their existing account by clicking the “Log In” button. 

Choose your user type: Founder or Investor.

Complete your profile to begin using the app.



## 🧭 How to use the app:

| Feature | Description | Founder Access | Investor Access | Both |
|---------|-------------|---------------|-----------------|------|
| **Feed** | The first tab in the navigation bar; displays all posts from founders and investors | Can post pictures, videos, or text to the "Founders" section only | Can post pictures, videos, or text to the "Investors" section only | Can view all posts; can like, comment, and share any post; like and comment counts are visible |
| **Search** | The second tab in the navigation bar; divided into "Smart Match" and "Search" sections | Smart Match shows matched investor profiles; can send connection requests | Smart Match shows matched founder profiles; can send connection requests and support founders | Can filter search by user type (Founder/Investor) and keyword (e.g., "FinTech", "AI"); AI match score (0–100%) based on industry, funding stage, location, and team size; scores below 30% filtered out, above 80% flagged as excellent |
| **Messages** | The third tab in the navigation bar; enables direct messaging between connected users | Can message connected founders and investors | Can message connected founders and investors | Messaging is only available between mutually connected users; connection requires a sent and accepted request |
| **Profile** | The last tab in the navigation bar; displays user info, posts, and projects | Has About tab (startup description); Posts section; limited to one project under "Projects" | Has Posts section; can support multiple projects shown under "Supported Projects" | Shows profile/company name; Connections button to view all connections; Log out button on top right corner |

# 🧪 Testing Strategies and Test Cases:

To ensure that the Build2Rise platform works reliably for both Founder and Investors, several types of testing strategies were used throughout development. These strategies have helped validate that the app’s core features, account creation, posting updates, showcasing projects and supporting startups, function correctly and provide a smooth user experience.


## Test Case 1: AI Smart Match - Founder to Investor

Description: This test checks if the AI Smart Match feature correctly analyzes a founder’s profile and displays suitable investor profiles.

Expected Behavior: 

Recommended investors should align with the founder’s industry

Funding stage Preferences should match (Seed, Series A, etc.)

Geographic or sector preferences should influence recommendations.
<p> 
  <img width="147" height="572" alt="Screenshot 2025-12-07 163459" src="https://github.com/user-attachments/assets/063445de-5129-4691-bb40-bce434aca1dc" />
  <img width="149" height="575" alt="Screenshot 2025-12-07 195906" src="https://github.com/user-attachments/assets/5ae7dc51-d9fa-443d-9510-63f4ee0cecba" />

</p>


## Test Case 2: AI Smart Match - Investor to Founder

Description: This test checks if the AI Smart Match feature correctly analyzes an investor’s preferences and displays suitable founder’s profiles.

Expected Behavior:

Recommended startups that align with the investor’s industry and region.
<p> 
<img width="147" height="577" alt="Screenshot 2025-12-07 183657" src="https://github.com/user-attachments/assets/140ed633-dd01-4a8e-9ad9-412bdf590945" />
<img width="152" height="575" alt="Screenshot 2025-12-07 194413" src="https://github.com/user-attachments/assets/868fd33e-c22b-4c09-9891-1c4e4cc5a1a6" />
</p>

## Test Case 3: Add a post to their feed

Description: Verify a founder can add a post to their feed:

Steps:

Select on the Feed tab in the navigation bar

Click on the "+" icon on the top right corner

Add a caption

Tap “post.”

Wait for an UI update

Expected Results:
<p> 
<img width="147" height="583" alt="Screenshot 2025-12-07 163519" src="https://github.com/user-attachments/assets/a1afe1c4-0b19-4e5d-b8b6-be1bcd4ee8ad" />
<img width="151" height="581" alt="Screenshot 2025-12-07 163625" src="https://github.com/user-attachments/assets/a31516da-aec2-4e0c-b7b8-6c74bfbed3f5" />
<img width="150" height="578" alt="Screenshot 2025-12-07 163636" src="https://github.com/user-attachments/assets/74147409-cf7c-40ba-8352-63957bc1e298" />
</p>


## Test Case 4: Investor Supports a Founder’s Project

Description: Check that investor can support a project

Steps:

Select the Search tab in the navigation bar

Review the Smart Matches or Search for a founder by name or based on startup information

Click on the support button

Wait for confirmation message or UI Update

Expected Results:
<p>
<img width="152" height="575" alt="Screenshot 2025-12-07 194413" src="https://github.com/user-attachments/assets/564d1226-a814-4afc-8e44-c72632b6e83e" />
<img width="151" height="574" alt="Screenshot 2025-12-07 195146" src="https://github.com/user-attachments/assets/ce30ec4d-ebf1-4157-9cbc-0d4924e01d49" />
<img width="149" height="573" alt="Screenshot 2025-12-07 195242" src="https://github.com/user-attachments/assets/d079a2ca-17d4-421c-9cd8-a47e691e4ad8" />

</p>

In the Investor’s Profile, the supported projects would be updated



## Test Case 5: Connect with another user

Description: Ensure users can send a connection request to another user

Steps:

Select on the Search tab in the navigation bar

Review the profiles of other users

Tap on the “Connect” button

Wait for confirmation message or UI Update
<p>
<img width="149" height="575" alt="Screenshot 2025-12-07 195906" src="https://github.com/user-attachments/assets/0581960a-a9c7-47fd-8016-2c34b405d0c8" />
<img width="151" height="574" alt="Screenshot 2025-12-07 195146" src="https://github.com/user-attachments/assets/0fb60480-bec3-4bfd-ba1e-a3fae65d7194" />
<img width="150" height="575" alt="Screenshot 2025-12-07 193928" src="https://github.com/user-attachments/assets/0e000223-b5fb-40c7-b63f-5934da743596" />
</p>

Expected Results:

The connection request is sent successfully

This request can be viewed from their profile by clicking the “Connections” text

The target user received a request in “My Connections” with the option to Accept or Decline the request.
<p> 
<img width="147" height="577" alt="Screenshot 2025-12-07 183657" src="https://github.com/user-attachments/assets/7af23285-ee5c-4d1d-ad29-b8e2e1f265ae" />
<img width="151" height="576" alt="Screenshot 2025-12-07 182850" src="https://github.com/user-attachments/assets/f481bdb9-52db-4a7b-b25c-1d79bf437918" />
</p>


## Test Case 6: Login & Load Feed

Description:

Ensure Users can log in and view their personalized dashboard

Steps:

Open the app

Select the “log in” text at the button of the screen

Enter your account details

Email: zislam04@nyit.edu

Password: 

Click on the log-in button

Expected Results:

<p>
<img width="148" height="580" alt="Screenshot 2025-12-07 163852" src="https://github.com/user-attachments/assets/f7aaafc4-66d2-4813-a967-4a0b77c7c9bc" />
<img width="145" height="573" alt="Screenshot 2025-12-07 183305" src="https://github.com/user-attachments/assets/c051c643-2b9f-4eaa-aff9-6f8616258fa4" />
<img width="150" height="575" alt="Screenshot 2025-12-07 183322" src="https://github.com/user-attachments/assets/4ab2f3b8-c5bf-40b4-946a-af87e99000cf" />
<img width="150" height="578" alt="Screenshot 2025-12-07 163636" src="https://github.com/user-attachments/assets/5abe1919-4119-41f6-a082-ac03124d493c" />

</p>


## Test Case 6: Signup & Load Feed

Description:

Ensure new users can sign in and view their personalized dashboard

Steps: 

Open the app

Click on the Sign up button

Select your user type: Founder or Investor:
<p>
<img width="147" height="576" alt="Screenshot 2025-12-07 163904" src="https://github.com/user-attachments/assets/fbdbebd3-6e24-48a8-b4a6-dce78ed0545f" />
<img width="150" height="578" alt="Screenshot 2025-12-07 180257" src="https://github.com/user-attachments/assets/9774e0bb-18aa-4f16-95fe-6f8f817ced2d" />
</p>

For Investor:

Investors enter details about their investment style and preferences

Then select the “Continue” button

Lastly confirm their account login credentials for future 

Then click on the “Create Account” button

Expected Results:
<p>
<img width="143" height="574" alt="Screenshot 2025-12-07 180353" src="https://github.com/user-attachments/assets/3d0ae10e-d90c-47eb-a953-7bdf19dadddd" />
<img width="149" height="580" alt="Screenshot 2025-12-07 154855" src="https://github.com/user-attachments/assets/132fcd55-db15-4ac3-bc17-53e3f93c492c" />

</p>

The user receives confirmation that their account has been created successfully.

Suggested next steps appear, such as completing profile details or exploring the feed.

Users may proceed directly to the Feed.



For Founder: 

Founders must provide basic company and startup-related information

Then select the “Continue” button

Lastly confirm their account login credentials for future 

Then click on the “Create Account” button
<p>
<img width="146" height="569" alt="Screenshot 2025-12-07 154813" src="https://github.com/user-attachments/assets/8b63bb54-295c-45bd-83d1-64d3edb3ec5e" />
<img width="149" height="580" alt="Screenshot 2025-12-07 154855" src="https://github.com/user-attachments/assets/132fcd55-db15-4ac3-bc17-53e3f93c492c" />
</p>

Expected Results:

The user receives confirmation that their account has been created successfully.

Suggested next steps appear, such as completing profile details or exploring the feed.

Users may proceed directly to the Feed.
<p>
<img width="145" height="578" alt="Screenshot 2025-12-07 155009" src="https://github.com/user-attachments/assets/c6744eab-0e5f-4016-8bd2-727b48770bd4" />
<img width="145" height="576" alt="Screenshot 2025-12-07 155041" src="https://github.com/user-attachments/assets/f1aae0eb-eca2-49e1-8353-10c937d139c5" />
</p>


# 🛠 Technology Stack:

Frontend: The application was built as a native Android application using Kotlin and the Jetpack Compose for modern UI development. This helps build the dynamic interfaces that are essential for the pitch feed, real time community interactions, and video content.  

Backend: The server-side architecture uses Spring Boot framework which is implemented in Kotlin. Core services include user management for multiple user types (founders and investors), content management for ranking pitch videos based on engagement metrics, real-time communication infrastructure for messaging and video calling, and automated scheduling systems for investor meetings. The security for Spring Boot’s features can be enhanced with JWT authentication, rate limiter to protect user data and ensure secure founder-investor interactions through the platform. Included RESTful api architecture such as controllers(api endpoints), services (business logic), repositories (data access) and DTOs (data transfer objects).

AI: Leverages Google Gemini AI, utilizes Google's generative language API to perform semantic analysis of user profiles, to understand contextual relationships.  

Database:  Supabase is where the data will be stored, as it provides a PostgreSQL database with real-time capabilities, authentication, and file storage that supports complex data relationships needed for social interactions. The database will store user profiles for both user types, startup information with pitch video, social interaction data (likes, comments, engagement metrics). 





# ⚙️Setup Guide: 
To set up the Build2Rise development environment, begin by cloning the repository, CSCI380-Build2Rise-server, and opening the backend folder in IntelliJ IDEA. Inside the backend, a new application.properties file must be created manually within the src/main/resources directory, since this file contains private keys and credentials that are intentionally excluded from version control. The backend server can then be run.

For the frontend setup, open the frontend in Android Studio and run the app once the backend is still running. Together, these steps prepare both the server and client environments for development or testing. The production environment follows the same process but requires hosting the Spring Boot backend on a cloud server and updating the mobile app’s base URL to match the backend.

# ✨ Features and Technical Implementation: 
The Build2Rise app has 2 types of users: Founders and Investors. After signing up or logging in, the users will be exposed to the following features of the app:



## Feed: 
The Feed page is divided into two sections, “Founders” and “Investors”. If users (either a founder or an investor), clicks on the “Founders” section, they will be able to see all the founders’ posts that are on there and they can interact with those posts by liking or commenting. On the other hand, if a user clicks on the “Investors” section, they will see all the posts made by investors and also interact with those posts by liking and commenting. 

## Search: 
The Search page is divided into two sections: Smart Match and Search. The default section for users when in the Search page is Smart Match. Build2Rise’s smart matching system uses Gemini AI to intelligently recommend the best match between an investor with a founder and vice versa. Provides a 0-100% match score by analyzing industry, funding stage, location and team size capability. Matches above the 80% threshold represent an excellent match, while scores below 30% are filtered out. When a user clicks on the Search tab, that page will show two sections: Founders and Investors. Users can filter their results by selecting the appropriate section before searching. For example, if someone is looking for FinTech investors, they simply select Investors and type “FinTech”, only investors interested in FinTech will appear. Likewise, selecting Founders and searching “AI” will display only founders working in AI. This separation makes the search process highly efficient, focused, and relevant to the user’s needs.

## Messages: 
Investors and founders can message others investors and founders once they make connections with one another. When going through the feed or looking up users manually through the “Search” feature, if a user sees someone they would like to connect with, they can send one another a request and upon the user who receives the request accepts it, they can send one another messages. Investors can connect and message both other investors and founders, and founders can do the same, allowing seamless communication across the entire community. Messaging is only available between users who have connected with one another. 

## Founder’s Profile: 
A founder’s profile page has their own Profile/Company/Product name, how many connections they have, all of their posts under the “Posts” section and their one and only project posted under the “Projects” section. A founder can make multiple posts, which can help them get the traction they need and find someone who will invest in them. Founders are limited to post one project under one account only because this maintains the seriousness and credibility of each founders’ profile, ensures founders present their primary venture with focus and dedication, and lets investors easily understand what a founder is building.

## Investor’s Profile: 
An investor’s profile page has their own Profile/Company name, how many connections they have, all of their posts under the “Posts” section and the projects they are supporting under the “Supported Projects” section. An investor can make multiple posts, which can help them find companies/products they can invest into. An investor can support more than one project because supporting multiple projects increases the chances for founders to be discovered and funded.

## Log out: 
All users have the option to log out by clicking the log out account above their profile page on the top right. 


# 📦 Packages and APIs:

The Build2Rise app uses a combination of Android libraries and backend technologies to create a secure, efficient, and responsive platform. On the frontend, Retrofit is used as the primary HTTP client due to its reliability and seamless integration with Kotlin coroutines, while the OkHttp Interceptor enables logging for debugging network calls. Gson is used for JSON parsing, ensuring smooth communication with the backend. For secure local storage, DataStore stores authentication tokens and user information. The UI navigation is handled with Navigation Compose, providing a clean approach to screen transitions. Media handling is supported by Coil for image loading and ExoPlayer for pitch video playback, while ViewModel and Kotlin Coroutines manage application state and asynchronous operations efficiently.

On the backend, Build2Rise uses Spring Web to define RESTful API endpoints, Gemini API for AI-powered semantic matching and Spring Security to enforce authentication and authorization policies. Spring Data JPA serves as the ORM layer, simplifying database interactions with PostgreSQL, while the PostgreSQL Driver manages direct communication with the database. Authentication is implemented using JJWT, which supports secure token-based authorization across the app. Commons IO helps handle file uploads and media management, and OkHttp is used server-side for interacting with external storage services.

These technologies were chosen so that Jetpack Compose enables UI development with rapid iteration, and Retrofit provides a reliable HTTP layer ideal for mobile apps. Additionally,  Supabase/PostgreSQL is used as a relational database for social interactions and media-driven features. Spring Boot provides a backend framework, and JWT authentication ensures secure communication. The Gemini API was used to perform semantic analysis by generating high quality text embeddings to compute AI-generated Smart Match.
