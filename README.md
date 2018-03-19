## Executive Abstract
This document proposes QRU,  an Android application that facilitates quality connections between recruiters and students, reduces the amount of idle time at job fairs, and promotes a sustainable alternative to printed resumes. Aimed at students, campus recruiters, and job fair personnel, we show how the proposed application uses QR codes, LinkedIn integration, and cloud storage to store and transmit company and student data between recruiters and students. We propose four unique user personas for QRU, and include a UML case diagram and use case diagram to illustrate the main modules in our proposed design and how each unique user persona will interact with the application. Furthermore, we outline the non-functional requirements of our project including storage, cost, performance, and our reliance on third-party APIS, and examine potential testing plans and relevant intellectual property concerns for our application. 

## Project Motivation
Even though business and networking services like LinkedIn have improved recruiting workflows online, face-to-face interaction between employees and prospective applicants is still a critical part of the networking process. Job fairs are an important way for recruiters and prospective employees engage in the critical action of face-to-face communication; however, job fairs require an inordinate amount of paper passing and administrative formalities like sharing resumes and contact information. Not only are these formalities unnecessarily time consuming, but they waste valuable paper and business resources. 

The project we propose aims to eliminate the redundancy and clunkiness of these formalities via an Android application. The application will use QR functionality to facilitate the connection between students and recruiters at career fairs at universities around the nation. Allowing students and employers to upload their resumes and contact information prior to meeting at networking events will allow for individuals to shift conversation immediately to pertinent details about the employee and employer. We believe this application will improve the expediency of networking events, encourage productive communication, allow employees to easily and universally screen resumes before and after an event, and ultimately match the best candidates to the best opportunities. 

## Feature Description 
Because QRU emphasizes expediency and facilitating quality connections, we intend to develop and Android application on which students, recruiters, and job fair personnel will be able to interact. Our server-side database will securely store student, company, and job fair data, and allow each entity to access information in accordance with their role. Most of the processing load will be the responsibility of the server we create; the app will simply interface with the server. We intend to integrate user registration with LinkedIn’s profile API to expedite the user registration process, allowing users to import profile information directly from LinkedIn as opposed to manually filling out forms. We also hope to be able to extrapolate information from a student’s resume in order to recommend companies that they should talk to. The app will have QR code generating and reading capabilities; recruiters and students can transmit their information and resumes to one another via a custom-generated QR code during their interaction. We also hope to recommend potential employers to students from by scraping data from their resume and registration credentials. We will implement an automated email service for communication of company and student materials after the networking event, as well as create a portal for students, employers, and job fair personnel to access logistical information about the career fair.
 
## User Personas
We generated four personas from students, recruiters, and job fair organizers to comment on which features of the app they liked and how it helped them improve the career fair.
	Evan, Freshman:
This was my first time at a career fair and coming in I was anxious about the silliest things like running out of resumes or dropping my resumes all over the floor. I found QRU while I was looking for a resume helper app on the Google Play Store, and it was a great help looking up companies and learning what they did before talking with the recruiters.
	Jeni, Career Fair Organizer:
QRU has been a great addition to this year’s career fair. Using the app, I’ve been able to reduce the number of logistical problems with organizing booths and has helped volunteers and students quickly find information about a company and where they can be found at the fair. I’m looking forward to plan out next year’s career fair with QRU again!
	Enrique Callado, Junior:
Using the QRU app, I no longer need to carry around my resumes, so now I have a lot more room to carry around swag I get from companies! But also, the QRU app is intuitive to use and helped me immediately focus on talking with the recruiter instead of starting off by awkwardly trading papers and exchanging formalities. 
	Zameer, Recruiter:
After years of collecting paper copies of resumes, switching to QRU was a nice change that quite literally eased a load off of my back! With QRU’s integrated company applications, I can easily collect, compile, and screen all the resumes in just a few hours now! We created a company profile on the app, so when students come talk to us we can focus on more pertinent information instead of wasting time familiarizing the student with our company.

## Design Specifications
Our application calls for third-party APIs, a succinct testing plan, and careful consideration of potential intellectual property infringements.

## Third-party APIs
We intend to use third-party APIs to support our application. We intend to integrate LinkedIn sign-in for both students and recruiters, Google’s mobile vision API for QR codes, and the Android SDK for development of our application. Our team intends to spend a significant portion of time developing familiarity with these services before implementing them. 

## Testing Plan
Our application will support only one-to-one interactions between students and recruiters. We will not support interactions (such as email and resume collection) from one recruiter to multiple students at the same time. This design decision was made in the interest of limiting the load placed on our database and server--the bottleneck of our design. We intend to test server load thoroughly and carefully. Furthermore, limiting the application to one-to-one interactions permits relatively simplistic testing. 

## Intellectual Property Concerns
Because we intend to use QR code functionality in our application, we intend to abide by relevant patent laws and licensing agreements for QR codes. NeoMedia, a company that owns several QR-code related patents, has a history of litigation against major retailers such as Michael’s and Bed Bath and Beyond [1]. We intend to abide by any patent laws pertinent to QR codes and obtain relevant licensing if necessary. 
	
## Requirements
Non-functional requirements for QRU include cloud storage, server performance, and the reliability of third-party APIs. QRU requires cloud storage to dynamically read and store user information throughout the lifecycle of use for each user. Additionally, we emphasize server bandwidth and performance as an important non-functional requirement for our application. During career fair weeks, registration and data access will be quite frequent, so being able to simultaneously store and access multiple user’s information is critical to the success of QRU. Because we intend for QRU to interface with LinkedIn’s API and  Google’s Mobile Vision API, we will need to thoroughly test QRU’s integration with these APIs. 
			 
## Feasibility
As we continue to design our application we must consider factors that impact the feasibility of our application including potential competitors, the wide scope of our features, and the potential licensing costs for our application. One of QRU’s main competitors is LinkedIn, whose main objective is to connect business professionals. We have chosen to integrate LinkedIn into our application to mitigate the potential for competition with LinkedIn. Because QRU targets several different types of users, we have proposed a wide set of features that require advanced integration with third-parties;  we intend to implement core functionalities first and then implement supplemental features as time permits. Finally, and perhaps most critically, we must consider the potential licensing costs associated with implementing QRCode functionality. If licensing fees exceed our budget, we will seek alternative design solutions. 

		
## Next Steps
Pending feedback from the instructional team, we intend to move forward with obtaining licensing permissions for QR technology, designing subsystems of QRU, and implementing our application.  

## References 
 [1] http://www.qode.com/solutions/ip_licensing


